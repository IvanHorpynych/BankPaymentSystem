package controller.schedulers;

import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class SchedulerInit {

    private SchedulerInit() {
        init();
    }

    private static class Singleton {
        private static final SchedulerInit INSTANCE = new SchedulerInit();
    }

    public static SchedulerInit getInstance() {
        return SchedulerInit.Singleton.INSTANCE;
    }

    private static void init() {

        SchedulerFactory schedFact = new StdSchedulerFactory();

        Scheduler scheduler;
        try {
            scheduler = schedFact.getScheduler();
            scheduler.start();
            Logger.getLogger(Scheduler.class).info("Scheduler started correctly!");
        } catch (SchedulerException e) {
            Logger.getLogger(Scheduler.class).error(e);
            return;
        }

        Trigger creditTrigger = newTrigger()
                .withIdentity("creditTrigger", "mainGroup")
                .withSchedule(dailyAtHourAndMinute(1, 00))
                .build();

        Trigger depositTrigger = newTrigger()
                .withIdentity("depositTrigger", "mainGroup")
                .withSchedule(dailyAtHourAndMinute(3, 00))
                .build();

        JobDetail creditJob = newJob(CreditJob.class)
                .withIdentity("creditJob", "mainGroup")
                .build();

        JobDetail depositJob = newJob(DepositJob.class)
                .withIdentity("depositJob", "mainGroup")
                .build();

        try {
            scheduler.scheduleJob(creditJob, creditTrigger);
            scheduler.scheduleJob(depositJob, depositTrigger);
        } catch (SchedulerException e) {
            Logger.getLogger(Scheduler.class).error(e);
        }
    }
}
