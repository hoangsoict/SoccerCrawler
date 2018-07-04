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

import com.soccercrawler.dal.SoccermatchDAOCustomer;
import com.soccercrawler.entities.Soccermatch;
import com.soccercrawler.entities.SoccermatchTemp;
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
		
		final List<Soccermatch> totalTemp = new ArrayList<>();
		totalTemp.addAll(a1Matchs);
		totalTemp.addAll(a188Matchs);
		
		final List<Soccermatch> list1 = analysisMatchs(a1Matchs, ngMatchs);
		persit(list1);
		
		final List<Soccermatch> list188 = analysisMatchs(a188Matchs, ngMatchs);
		persit(list188);

		/* Persit Temp*/
		final List<SoccermatchTemp> tempList = convertObject(totalTemp);
		persitTemp(tempList);

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
				if (tempScore > score && tempScore > Constant.MIN_SCORE_SIMILARITY_NAME
						&& compareTime(aM, ngM)) {
					score = tempScore;
					aMatch = aM;
					ngMatch = ngM;
				}
			}
			if (aMatch != null && ngMatch != null) {
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
	
	public void persitTemp(final List<SoccermatchTemp> matchs){
		final EntityManager em = emFactory.createEntityManager();
		em.getTransaction().begin();

		for(Long i = 1L; i<=400; i++){
			final SoccermatchTemp temp = em.find(SoccermatchTemp.class, i);
			if(temp!=null){
				em.remove(temp);
			}else{
				System.out.println("------ DELETE TEMP DONE : " + i);
				break; // Remove all done
			}
		}
		
		em.getTransaction().commit();
		
		
		em.getTransaction().begin();
		
		for (final SoccermatchTemp match : matchs) {
			em.persist(match);
		}
		System.out.println("------ DONE UPDATE TEMP :  " + matchs.size() );
		em.getTransaction().commit();
		
		// em.flush();
		em.close();
		// emFactory.close();
	}
	
	
	public List<SoccermatchTemp> convertObject(final List<Soccermatch> matchs){
		final List<SoccermatchTemp> result = new ArrayList<>();
		int i = 1;
		for(final Soccermatch match : matchs){
			final SoccermatchTemp temp = new SoccermatchTemp();
			temp.setCreatedDate(match.getCreatedDate());
			temp.setNameTeam1(match.getNameTeam1());
			temp.setCurrentSpreadTeam1(match.getCurrentSpreadTeam1());
			temp.setCurrentOddsTeam1(match.getCurrentOddsTeam1());
			temp.setOpenSpreadTeam1(match.getOpenSpreadTeam1());
			temp.setOpenOddsTeam1(match.getOpenOddsTeam1());
			temp.setCurrentTotalLine(match.getCurrentTotalLine());
			temp.setOpenTotalLine(match.getOpenTotalLine());
			// over - under text
			temp.setCurrentOuOddsTeam1(match.getCurrentOuOddsTeam1());
			temp.setOpenOuOddsTeam1(match.getOpenOuOddsTeam1());

			temp.setNameTeam2(match.getNameTeam2());
			temp.setCurrentSpreadTeam2(match.getCurrentSpreadTeam2());
			temp.setCurrentOddsTeam2(match.getCurrentOddsTeam2());
			temp.setOpenSpreadTeam2(match.getOpenSpreadTeam2());
			temp.setOpenOddsTeam2(match.getOpenOddsTeam2());
			// over - under text
			temp.setCurrentOuOddsTeam2(match.getCurrentOuOddsTeam2());
			temp.setOpenOuOddsTeam2(match.getOpenOuOddsTeam2());
			
			temp.setId(i);
			temp.setMode(match.getId().getMode());
			
			result.add(temp);
			
			calculateWinLost(temp);
			
			i++;
			
		}
		return result;
	}
	
	public void calculateWinLost(final SoccermatchTemp temp){
		final EntityManager em = emFactory.createEntityManager();
		em.getTransaction().begin();
		
		final BigDecimal currentTotalLine = temp.getCurrentTotalLine();
		final BigDecimal openTotalLine = temp.getOpenTotalLine();
		
		final BigDecimal currentOuOddsTeam1 = temp.getCurrentOuOddsTeam1();
		final BigDecimal openOuOddsTeam1 = temp.getOpenOddsTeam1();
		final BigDecimal currentOuOddsTeam2 = temp.getCurrentOddsTeam2();
		final BigDecimal openOuOddsTeam2 = temp.getOpenOddsTeam2();
		
		final List<Object> overUnderResultList = new SoccermatchDAOCustomer().searchOverUnderWinResult(em,temp.getMode(), currentTotalLine, openTotalLine,
				currentOuOddsTeam1, openOuOddsTeam1, currentOuOddsTeam2, openOuOddsTeam2);
		
		
		final BigDecimal currentOddsTeam1BD = temp.getCurrentOddsTeam1();
		final BigDecimal currentOddsTeam2BD = temp.getCurrentOddsTeam2();
		final BigDecimal currentSpreadTeam1BD = temp.getCurrentSpreadTeam1();
		final BigDecimal currentSpreadTeam2BD = temp.getCurrentSpreadTeam2();
		
		final BigDecimal openOddsTeam1BD = temp.getOpenOddsTeam1();
		final BigDecimal openOddsTeam2BD = temp.getOpenOddsTeam2();
		final BigDecimal openSpreadTeam1BD = temp.getOpenSpreadTeam1();
		final BigDecimal openSpreadTeam2BD = temp.getOpenSpreadTeam2();
		
		final List<Object> spreadOddsResultList= new SoccermatchDAOCustomer().searchSpreadOddsWinResult
				(em, temp.getMode(), currentSpreadTeam1BD,			  currentOddsTeam1BD ,  openSpreadTeam1BD  , openOddsTeam1BD
						  ,currentSpreadTeam2BD ,  currentOddsTeam2BD  , openSpreadTeam2BD
						  ,openOddsTeam2BD);
		
		
		temp.setOuBetWinPercent((Short) overUnderResultList.get(0));
		temp.setOuBetTotalCount(new BigDecimal((Long)overUnderResultList.get(1)));
		temp.setSpreadBetWinPercent((Short) spreadOddsResultList.get(0));
		temp.setSpreadBetTotalCount(new BigDecimal((Long)spreadOddsResultList.get(1)));
		
		em.getTransaction().commit();
		
		// em.flush();
		em.close();
		// emFactory.close();
	}
	
}
