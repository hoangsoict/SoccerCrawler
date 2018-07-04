package com.soccercrawler.website;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.soccercrawler.entities.Soccermatch;
import com.soccercrawler.entities.SoccermatchId;
import com.soccercrawler.util.Constant;
import com.soccercrawler.util.Utility;

public class Asianodds implements WebsiteCraw {
	String mode;
	String urlString;

	public Asianodds(final String urlString, final String mode) {
		this.urlString = urlString;
		this.mode = mode;
	}

	@Override
	public List<Soccermatch> getData() {
	final List<Soccermatch> matchs = new ArrayList<>();
		System.out.println("--------------------START CRAW ASISANODDS "+ this.mode + " ------------");
		try {
			Document doc = null;
			String source = "";
			if(Constant.ASIANODD_MODE_188BET.equals(this.mode)){
				source = Constant.ASIANODD_MODE_188BET_SOURCE;
			}else{
				source = Constant.ASIANODD_MODE_1BET_SOURCE;
			}
			doc = Jsoup.connect(this.urlString).userAgent("Chrome").cookie("source", source).timeout(30000).post();

			final Element mainTable = doc.selectFirst(("table[bgcolor='#D2D7BF']"));
			if (mainTable != null) {
				final Elements matchElements = mainTable.select("tr[class='down']");
				String cupName = "";
				String dateStr = "";
				for (final Element matchElement : matchElements) {
					try {

						final Soccermatch match = new Soccermatch();
						final Element cupNameElement = matchElement.selectFirst("strong");
						if (cupNameElement != null) {
							cupName = cupNameElement.text();
							dateStr = cupName.substring(cupName.lastIndexOf("-") + 2);
						}

						final Element team1 = matchElement.nextElementSibling();

						dateStr = dateStr + " " + team1.child(0).text();
						match.setCreatedDate(new SimpleDateFormat(Constant.ASIANODD_DATE_FORMAT).parse(dateStr));
						match.setNameTeam1(team1.child(1).text());
						match.setCurrentSpreadTeam1(Utility.mapStringToBigDecimal(team1.child(2).text()));
						match.setCurrentOddsTeam1(Utility.mapStringToBigDecimal(team1.child(3).text()));
						match.setOpenSpreadTeam1(Utility.mapStringToBigDecimal(team1.child(4).text()));
						match.setOpenOddsTeam1(Utility.mapStringToBigDecimal(team1.child(5).text()));
						match.setCurrentTotalLine(Utility.mapStringToBigDecimal(team1.child(6).text()));
						match.setOpenTotalLine(Utility.mapStringToBigDecimal(team1.child(7).text()));
						// over - under text
						match.setCurrentOuOddsTeam1(Utility.mapStringToBigDecimal(team1.child(9).text()));
						match.setOpenOuOddsTeam1(Utility.mapStringToBigDecimal(team1.child(10).text()));

						final Element team2 = team1.nextElementSibling();
						match.setNameTeam2(team2.child(0).text());
						match.setCurrentSpreadTeam2(Utility.mapStringToBigDecimal(team2.child(1).text()));
						match.setCurrentOddsTeam2(Utility.mapStringToBigDecimal(team2.child(2).text()));
						match.setOpenSpreadTeam2(Utility.mapStringToBigDecimal(team2.child(3).text()));
						match.setOpenOddsTeam2(Utility.mapStringToBigDecimal(team2.child(4).text()));
						// over - under text
						match.setCurrentOuOddsTeam2(Utility.mapStringToBigDecimal(team2.child(6).text()));
						match.setOpenOuOddsTeam2(Utility.mapStringToBigDecimal(team2.child(7).text()));
						
						final SoccermatchId id = new SoccermatchId();
						id.setMode(this.mode);
						match.setId(id);
						
						Utility.displayMatch(match);
						matchs.add(match);
					} catch (final ParseException e) {
						System.out.println(e.getMessage());
						// TODO: handle exception
					}
				}
				;
			}

		} catch (final IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		System.out.println("--------------------END CRAW ASISANODDS : " + matchs.size() + "------------");
		return matchs;
	}

}
