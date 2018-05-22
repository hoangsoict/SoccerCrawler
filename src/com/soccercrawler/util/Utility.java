package com.soccercrawler.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Properties;

import com.soccercrawler.entities.Soccermatch;

public class Utility {
	
	static Properties configProp = null;
	static Utility u = new Utility();

	public static String getConfigProperties(final String key) {
		if(configProp == null){
			configProp = new Properties();
			final InputStream input = u.getClass().getClassLoader().getResourceAsStream("config.properties");
			try {
				configProp.load(input);
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return configProp.getProperty(key);
	}

	public static void displayMatch(final Soccermatch match) {
		if (Constant.DISPLAY_MATCH_CRALER_INFO == false) {
			return;
		}
		System.out.println(
				"--------------- " + new SimpleDateFormat(Constant.ASIANODD_DATE_FORMAT).format(match.getCreatedDate())
						+ " --------------------");
		System.out.println(match.getNameTeam1() + " * " + match.getCurrentSpreadTeam1() + " * "
				+ match.getCurrentOddsTeam1() + " * " + match.getOpenSpreadTeam1() + " * " + match.getOpenOddsTeam1());
		System.out.println(match.getNameTeam2() + " * " + match.getCurrentSpreadTeam2() + " * "
				+ match.getCurrentOddsTeam2() + " * " + match.getOpenSpreadTeam2() + " * " + match.getOpenOddsTeam2());
		System.out.println("-------- Over - Under ------------");
		System.out.println(match.getCurrentTotalLine() + " * " + match.getOpenTotalLine());
		System.out.println(match.getCurrentOuOddsTeam1() + " * " + match.getOpenOuOddsTeam1());
		System.out.println(match.getCurrentOuOddsTeam2() + " * " + match.getOpenOuOddsTeam2());
		System.out.println("-------- Result ------------");
		System.out.println(match.getScoreTeam1() + " - " + match.getScoreTeam2());
		System.out.println("---------------------------------------------------");
	}

	public static void displayResultMatch(final Soccermatch match) {
		if (Constant.DISPLAY_MATCH_RESULT_INFO == false) {
			return;
		}
		System.out.println(
				"--------------- " + new SimpleDateFormat(Constant.ASIANODD_DATE_FORMAT).format(match.getCreatedDate())
						+ " --------------------");
		System.out.println(match.getNameTeam1() + " * " + match.getCurrentSpreadTeam1());
		System.out.println(match.getNameTeam2() + " * " + match.getCurrentSpreadTeam2());
		System.out.println("-------- Over - Under ------------");
		System.out.println("Total line : " + match.getCurrentTotalLine());
		System.out.println("Score : " + match.getScoreTeam1() + " - " + match.getScoreTeam2());
		System.out.println("-------- Result ------------");
		System.out.println("Spread Odds : " + match.getSpreadBetResult());
		System.out.println("Over Under  : " + match.getOuBetResult());
		System.out.println("---------------------------------------------------");
	}

	public static void displaySummaryMatch(final Soccermatch aMatch, final Soccermatch ngMatch) {
		if (Constant.DISPLAY_MATCH_SUMMARY_INFO == false) {
			return;
		}
		System.out.println("-------------------MATCH--------------------------");

		System.out.println(
				"--------------- " + new SimpleDateFormat(Constant.ASIANODD_DATE_FORMAT).format(aMatch.getCreatedDate())
						+ " --------------------");
		System.out.println(aMatch.getNameTeam1() + " * " + aMatch.getNameTeam2());
		System.out.println("---------------------------------------------------");

		System.out.println("--------------- "
				+ new SimpleDateFormat(Constant.ASIANODD_DATE_FORMAT).format(ngMatch.getCreatedDate())
				+ " --------------------");
		System.out.println(ngMatch.getNameTeam1() + " * " + ngMatch.getNameTeam2());

		System.out.println("-------------------END--------------------------");
	}

	/**
	 * Maps a String to double. Spaces and ','s are accepted.
	 */
	public static BigDecimal mapStringToBigDecimal(final String aString) {
		BigDecimal bd = null;
		if (aString != null) {
			String ss = removeCharFromString(aString, ' '); // remove spaces
			ss = removeCommas(ss); // remove commas
			bd = (ss.length() == 0) ? null : new BigDecimal(ss);
		}
		return bd;
	}

	private static String removeCharFromString(final String input, final char removeChar) {
		String output = null;

		if (input != null) {
			final StringBuffer buffer = new StringBuffer("");
			final char[] allChars = input.toCharArray();
			for (int i = 0; i < allChars.length; i++) {
				if (allChars[i] != removeChar) {
					buffer.append(allChars[i]);
				}
			}
			output = buffer.toString();
		}

		return output;
	}

	/**
	 * removes comma ',' characters from the given string.
	 * 
	 * @param input
	 *            String from which "," is to be removed.
	 * @return String without commas
	 */
	private static String removeCommas(final String input) {
		return removeCharFromString(input, ',');
	}

	public static String getResponseText(String u) {

		URL url;
		u = "http://asianodds.com/past_200_asian_odds.asp";
		try {
			url = new URL(u);
			final URLConnection urlConn = url.openConnection();
			urlConn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.29 Safari/537.36");

			urlConn.setRequestProperty("Cookie", "source=188bet%2Ecom");
			urlConn.setUseCaches(true);
			final BufferedReader br = new BufferedReader(new InputStreamReader((urlConn.getInputStream())));
			final StringBuilder sb = new StringBuilder();
			String output;
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
			return sb.toString();
		} catch (final MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
} // end of class definition
