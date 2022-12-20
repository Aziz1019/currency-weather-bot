package com.example.cbu.bot.scheduler.currencyScheduler.services;

import com.example.cbu.bot.scheduler.events.SchedulerEventDTO;
import com.example.cbu.bot.scheduler.currencyScheduler.mapper.CurrencySubscriptionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class CronManager {
    private final JdbcTemplate jdbcTemplate;
    protected final ApplicationEventPublisher publisher;

    @Value("select user_id, currency_code, currency_time, first_name from user_subscription where currency_subscription = true")
    private String sql1;

    public CronManager(JdbcTemplate jdbcTemplate, ApplicationEventPublisher publisher) {
        this.jdbcTemplate = jdbcTemplate;
        this.publisher = publisher;
    }

    @PostConstruct
    public void collectTasks() {
        log.info("Sql for get tasks : {}", sql1);
        var taskList = jdbcTemplate.query(sql1, new CurrencySubscriptionMapper());
        log.info("Found all active tasks :{}", taskList);
        taskList.removeIf(task -> !CronExpression.isValidExpression(task.getCurrencyTime()));
        log.info("All valid tasks :{}", taskList);
        publisher.publishEvent(new SchedulerEventDTO(taskList));
        publisher.publishEvent(new SchedulerEventDTO(taskList));
    }
}