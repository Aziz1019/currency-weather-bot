package com.example.cbu.bot.scheduler.weatherScheduler.services;

import com.example.cbu.bot.scheduler.events.SchedulerEventDTO;
import com.example.cbu.bot.scheduler.weatherScheduler.mapper.WeatherSubscriptionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class WeatherCronManager {
    private final JdbcTemplate jdbcTemplate;
    protected final ApplicationEventPublisher publisher;

    @Value("select user_id, city_name, weather_time, first_name from user_subscription where currency_subscription = true")
    private String sql;
    public WeatherCronManager(JdbcTemplate jdbcTemplate, ApplicationEventPublisher publisher) {
        this.jdbcTemplate = jdbcTemplate;
        this.publisher = publisher;
    }

    @PostConstruct
    public void collectTasks() {
        log.info("Sql for get tasks : {}", sql);
        var taskList = jdbcTemplate.query(sql, new WeatherSubscriptionMapper());
        log.info("Found all active tasks :{}", taskList);
        taskList.removeIf(task -> !CronExpression.isValidExpression(task.getWeatherTime()));
        log.info("All valid tasks :{}", taskList);
        publisher.publishEvent(new SchedulerEventDTO(taskList));
    }
}