package com.soccercrawler.servlet;

import org.quartz.JobExecutionException;

import info.debatty.java.stringsimilarity.JaroWinkler;
import info.debatty.java.stringsimilarity.LongestCommonSubsequence;

public class Test {

	public static void main(final String[] args) {

		final AutoCrawlerJob job = new AutoCrawlerJob();
		try {
			job.execute(null);
		} catch (final JobExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void testString(){
		final String[] test = new String[]{
				"Curico Unido", "Curico Unido",
				"Gezesite Santiago", "San Diego Zest",
				"Deportivo Toluca", "Toluca",
				"Nashville Rhythm (W)","Nashville Rhythm FC (w)",
				"Peachtree City MOBA (W)","Peachtree City (W)"
		};
		final LongestCommonSubsequence lcs = new LongestCommonSubsequence();
		final JaroWinkler jw = new JaroWinkler();
		for(int i = 0; i< test.length; i=i+2){
			System.out.println(jw.similarity(test[i], test[i+1]));
		}
	}

}
