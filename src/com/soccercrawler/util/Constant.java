package com.soccercrawler.util;

public interface Constant {
	
	public static String ASIANODDS_URL = "http://asianodds.com/past_200_asian_odds.asp";
	
	// New result
	public static String NOWGOAL_URL = "http://www.nowgoal.top/data/bf_en2.js";
	
	// Old result
//	public static String NOWGOAL_URL = "file:///D:/Download/SoccerCrawler/bf_en2-21-5-2018-14h52.js";

	public static String ASIANODD_DATE_FORMAT = "dd/MM/yyyy HH:mm";
	
	public static double MIN_SCORE_SIMILARITY_NAME = 1.2;

	public static Boolean DISPLAY_MATCH_CRALER_INFO = false;

	public static Boolean DISPLAY_MATCH_SUMMARY_INFO = false;
	
	public static Boolean DISPLAY_MATCH_RESULT_INFO = true;

	public static String ASIANODD_MODE_1BET_SOURCE = "";
	
	public static String ASIANODD_MODE_188BET_SOURCE = "188bet%2Ecom";
	
	public static long COMPARE_TIME_SECOND = 7200;
	
	public static double COMPARE_NAME_SCORE = 1.3;
	
	public static String ASIANODD_MODE_1BET = "1BET";
	public static String ASIANODD_MODE_188BET = "188BET";
	
}
