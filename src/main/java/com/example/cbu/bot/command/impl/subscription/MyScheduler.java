package com.example.cbu.bot.command.impl.subscription;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
public class MyScheduler {
    private final TaskScheduler executor;
    public MyScheduler(TaskScheduler taskExecutor) {
        this.executor = taskExecutor;
    }
    public void scheduling(final Runnable task, String cron) {
        executor.schedule(task, new CronTrigger("*/" + cron + " * * * * * "));
    }
}