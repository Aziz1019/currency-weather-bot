package com.example.cbu.bot.scheduler.currencyScheduler;

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
    private final SchedulerEventListener schedulerEventListener;

    @Value("select user_id, currency_code, currency_time, first_name from user_subscription where currency_subscription = true")
    private String sql;
    public CronManager(JdbcTemplate jdbcTemplate, ApplicationEventPublisher publisher, SchedulerEventListener schedulerEventListener) {
        this.jdbcTemplate = jdbcTemplate;
        this.publisher = publisher;
        this.schedulerEventListener = schedulerEventListener;
    }

    @PostConstruct
    public void collectTasks() {
        log.info("Sql for get tasks : {}", sql);
        var taskList = jdbcTemplate.query(sql, new CurrencySubscriptionMapper());
        log.info("Found all active tasks :{}", taskList);
        taskList.removeIf(task -> !CronExpression.isValidExpression(task.getCurrencyTime()));
        log.info("All valid tasks :{}", taskList);
        publisher.publishEvent(new SchedulerEventDTO(taskList));
    }
}