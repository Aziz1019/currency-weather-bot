package com.example.cbu.bot.command.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@EnableScheduling
public class TestClass {
    Logger logger = LoggerFactory.getLogger(TestClass.class);

    private final String crons = "* * * * * *";

    @Scheduled(cron = crons)
    public void job() throws InterruptedException {
        logger.info("Job current time: " + new Date());
        Thread.sleep(1000L);
    }
}
