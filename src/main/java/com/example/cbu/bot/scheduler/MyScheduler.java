//package com.example.cbu.bot.scheduler;
//
//import org.springframework.scheduling.TaskScheduler;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.support.CronTrigger;
//import org.springframework.stereotype.Component;
//
//
//@Component
//@EnableScheduling
//public class MyScheduler {
//    private final TaskScheduler executor;
//    public MyScheduler(TaskScheduler taskExecutor) {
//        this.executor = taskExecutor;
//    }
//    public void scheduling(final Runnable task, String cron) {
//        // Every cron seconds
////        executor.schedule(task, new CronTrigger("*/" + cron + " * * * * * "));
//
//        // Daily at cron time
//        executor.schedule(task, new CronTrigger("0 0 " + cron + " * * * "));
//    }
//}