package com.soccercrawler.website;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.soccercrawler.entities.Soccermatch;
import com.soccercrawler.entities.SoccermatchId;
import com.soccercrawler.util.Constant;
import com.soccercrawler.util.Utility;

public class Nowgoal implements WebsiteCraw {
	String urlString;

	public Nowgoal(final String urlString) {
		this.urlString = urlString;
	}

	@Override
	public List<Soccermatch> getData() {
		System.out.println("--------------------START CRAW NOWGOAL ------------");
		final List<Soccermatch> matchs = new ArrayList<>();
		try {
			final Document doc = Jsoup.connect(this.urlString).userAgent("Chrome").timeout(30000)
					.ignoreContentType(true).get();
			final String body = doc.body().text();
			final String[] arrays = body.split(";");
			for (final String item : arrays) {
				if (item.startsWith(" A[")) {
					try {

						final int start = item.indexOf("=[");
						final int end = item.lastIndexOf("]");
						String data = "";
						if (end < start) {
							data = item.substring(start + 2);
						} else {
							data = item.substring(start + 2, end);
						}

						final String[] subArrays = data.split(",");

						if ("-1".equals(subArrays[18])) { // Only read final match
							final Soccermatch match = new Soccermatch();
							final SoccermatchId id = new SoccermatchId();
							id.setId(Long.parseLong(subArrays[0]));
							match.setId(id);
							match.setNameTeam1(subArrays[4].replaceAll("'", ""));
							match.setNameTeam2(subArrays[5].replaceAll("'", ""));

							final String year = subArrays[6];
							String month = subArrays[7];
							month = Integer.parseInt(month) + 1 + "";
							final String day = subArrays[8];
							final String hour = subArrays[9];
							final String minute = subArrays[10];
							String dateStr = day + "/" + month + "/" + year + " " + hour + ":" + minute;
							dateStr = dateStr.replace("'", "");
							match.setCreatedDate(new SimpleDateFormat(Constant.ASIANODD_DATE_FORMAT).parse(dateStr));

							match.setScoreTeam1(Short.parseShort(subArrays[19]));
							match.setScoreTeam2(Short.parseShort(subArrays[20]));

							matchs.add(match);
							Utility.displayMatch(match);

						}
					} catch (final ParseException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (final IOException e3) {
			e3.printStackTrace();
		}
		System.out.println("--------------------END CRAW NOWGOAL : " + matchs.size() + " ------------");
		return matchs;
	}

}
