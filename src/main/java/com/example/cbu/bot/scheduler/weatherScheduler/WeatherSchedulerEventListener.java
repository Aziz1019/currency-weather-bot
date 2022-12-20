package com.example.cbu.bot.scheduler.weatherScheduler;

import com.example.cbu.bot.scheduler.events.SchedulerEventDTO;
import com.example.cbu.bot.scheduler.weatherScheduler.services.WeatherCronService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class WeatherSchedulerEventListener implements ApplicationListener<WeatherSchedulerEventDTO> {
    private final WeatherCronService cronService;
    private final Map<String, ScheduledTaskRegistrar> schedulers = new HashMap<>();

    public WeatherSchedulerEventListener(WeatherCronService cronService) {
        this.cronService = cronService;
    }

    @Override
    public void onApplicationEvent(WeatherSchedulerEventDTO dto) {
        var tasks = dto.getTasks();
        tasks.forEach(task -> {
            var scheduler = cronService.createScheduler(task);
            schedulers.put(task.getCityName(), scheduler);
        });
        log.info("All running schedulers : {}", schedulers.keySet());
    }
}
