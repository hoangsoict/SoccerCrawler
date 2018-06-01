package com.soccercrawler.servlet;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.soccercrawler.entities.Soccermatch;
import com.soccercrawler.util.Constant;
import com.soccercrawler.util.Utility;
import com.soccercrawler.website.Asianodds;
import com.soccercrawler.website.Nowgoal;

import info.debatty.java.stringsimilarity.JaroWinkler;

public class AutoCrawlerJob implements Job {
	static EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("SoccerCrawler");

	@Override
	public void execute(final JobExecutionContext context) throws JobExecutionException {
		System.err.println("----------- START JOB CRAWLER --- "
				+ new SimpleDateFormat(Constant.ASIANODD_DATE_FORMAT).format(new Date()));
		final Asianodds asianodds1BET = new Asianodds(Constant.ASIANODDS_URL, Constant.ASIANODD_MODE_1BET);
		final List<Soccermatch> a1Matchs = asianodds1BET.getData();

		final Asianodds asianodds188BET = new Asianodds(Constant.ASIANODDS_URL, Constant.ASIANODD_MODE_188BET);
		final List<Soccermatch> a188Matchs = asianodds188BET.getData();

		final Nowgoal nowgoal = new Nowgoal(Constant.NOWGOAL_URL);
		final List<Soccermatch> ngMatchs = nowgoal.getData();

		final List<Soccermatch> list1 = analysisMatchs(a1Matchs, ngMatchs);
		persit(list1);

		final List<Soccermatch> list188 = analysisMatchs(a188Matchs, ngMatchs);
		persit(list188);

		System.err.println("----------- END JOB CRAWLER --- "
				+ new SimpleDateFormat(Constant.ASIANODD_DATE_FORMAT).format(new Date()));
	}
	
	public List<Soccermatch> analysisMatchsTest(final List<Soccermatch> aMatchs, final List<Soccermatch> ngMatchs) {
		for(final Soccermatch match : aMatchs){
			match.getId().setId(ThreadLocalRandom.current().nextInt(0, 9999999 + 1));
			match.setScoreTeam1((short) ThreadLocalRandom.current().nextInt(0, 5 + 1));
			match.setScoreTeam2((short) ThreadLocalRandom.current().nextInt(0, 5 + 1));

			calculateResult(match);
		}
		
		return aMatchs;
		
	}

	public List<Soccermatch> analysisMatchs(final List<Soccermatch> aMatchs, final List<Soccermatch> ngMatchs) {
		final List<Soccermatch> result = new ArrayList<>();
		double score = -1;
		Soccermatch aMatch = null;
		Soccermatch ngMatch = null;
		for (final Soccermatch ngM : ngMatchs) {
			for (final Soccermatch aM : aMatchs) {
				final double tempScore = scoringMatchTeamName(aM, ngM);
				if (tempScore > score && tempScore > Constant.MIN_SCORE_SIMILARITY_NAME) {
					score = tempScore;
					aMatch = aM;
					ngMatch = ngM;
				}
			}
			if (aMatch != null && ngMatch != null && compareTime(aMatch, ngMatch)) {
				Utility.displaySummaryMatch(aMatch, ngMatch);
				aMatchs.remove(aMatch);

				aMatch.getId().setId(ngMatch.getId().getId());
				aMatch.setScoreTeam1(ngMatch.getScoreTeam1());
				aMatch.setScoreTeam2(ngMatch.getScoreTeam2());

				calculateResult(aMatch);

				result.add(aMatch);
			}
			aMatch = null;
			ngMatch = null;
			score = -1;
		}
		System.out.println("--------------Analysis Match : " + result.size() + "---------------");
		return result;
	}

	public void calculateResult(final Soccermatch aMatch) {
		// Spread Odds
		final int spreadOdds = aMatch.getCurrentSpreadTeam1().add(new BigDecimal(aMatch.getScoreTeam1()))
				.subtract(new BigDecimal(aMatch.getScoreTeam2())).compareTo(new BigDecimal(0));
		if (spreadOdds > 0) {
			aMatch.setSpreadBetResult(new Short("1"));
		} else if (spreadOdds < 0) {
			aMatch.setSpreadBetResult(new Short("-1"));
		} else if (spreadOdds == 0) {
			aMatch.setSpreadBetResult(new Short("0"));
		}

		// Over Under
		final int overUnder = new BigDecimal(aMatch.getScoreTeam1() + aMatch.getScoreTeam2())
				.compareTo(aMatch.getCurrentTotalLine());
		if (overUnder > 0) {
			aMatch.setOuBetResult(new Short("1"));
		} else if (overUnder < 0) {
			aMatch.setOuBetResult(new Short("-1"));
		} else if (overUnder == 0) {
			aMatch.setOuBetResult(new Short("0"));
		}
	}

	public boolean compareTime(final Soccermatch aMatch, final Soccermatch ngMatch) {
		final long seconds = Math.abs(aMatch.getCreatedDate().getTime() - ngMatch.getCreatedDate().getTime()) / 1000;
		if (seconds < Constant.COMPARE_TIME_SECOND) {
			return true;
		} else {
			return false;
		}
	}

	public double scoringMatchTeamName(final Soccermatch aMatch, final Soccermatch ngMatch) {

		if (StringUtils.isBlank(aMatch.getNameTeam1()) || StringUtils.isBlank(aMatch.getNameTeam2())
				|| StringUtils.isBlank(ngMatch.getNameTeam1()) || StringUtils.isBlank(ngMatch.getNameTeam1())) {
			return 0;
		}
		final JaroWinkler jw = new JaroWinkler();
		final double score1 = jw.similarity(aMatch.getNameTeam1(), ngMatch.getNameTeam1());
		final double score2 = jw.similarity(aMatch.getNameTeam2(), ngMatch.getNameTeam2());

		return score1 + score2;
	}

	public List<Soccermatch> readFromAssianOdds(final String mode) {
		final String urlString = "http://asianodds.com/past_200_asian_odds.asp";
		final Asianodds asianodds = new Asianodds(urlString, mode);
		return asianodds.getData();
	}

	public List<Soccermatch> readFromNowGoal() {
		final String urlString = "http://www.nowgoal.top/data/bf_en2.js";
		final Nowgoal nowgoal = new Nowgoal(urlString);
		return nowgoal.getData();
	}

	public void persit(final List<Soccermatch> matchs) {
		int i = 0;
		final EntityManager em = emFactory.createEntityManager();
		em.getTransaction().begin();
		for (final Soccermatch match : matchs) {
			if (em.find(Soccermatch.class, match.getId()) == null) {
				em.persist(match);
				Utility.displayResultMatch(match);
				i ++;
			}
		}
		System.out.println("------ PERSIT : " + i );
		em.getTransaction().commit();
		// em.flush();
		em.close();
		// emFactory.close();
	}
}
