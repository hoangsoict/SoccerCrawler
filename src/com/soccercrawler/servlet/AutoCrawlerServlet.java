package com.soccercrawler.servlet;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.soccercrawler.util.Utility;

public class AutoCrawlerServlet extends GenericServlet {

	@Override
	public void service(final ServletRequest req, final ServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(final ServletConfig config) throws ServletException {
		System.out.println("----------- START AUTO CRAWLER -------------");
		super.init(config);
		
		final String cronjobSetting = Utility.getConfigProperties("cronjob.config");

		final JobDetail job = JobBuilder.newJob(AutoCrawlerJob.class).withIdentity("autoCrawlerJob", "group1").build();

		final Trigger trigger = TriggerBuilder.newTrigger().withIdentity("autoCrawlerTrigger", "group1")
				.withSchedule(CronScheduleBuilder.cronSchedule(cronjobSetting)).build();

		// schedule it
		Scheduler scheduler;
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);
		} catch (final SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
