package com.example.cbu.bot.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.support.CronTrigger;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
@EnableScheduling
public class MyBean {
    private final TaskScheduler executor;

    public MyBean(TaskScheduler taskExecutor) {
        this.executor = taskExecutor;
    }

    public void scheduling(final Runnable task) {
        // Schedule a task with the given cron expression
        executor.schedule(task, new CronTrigger("*/5 * * * * MON-FRI"));
    }
}