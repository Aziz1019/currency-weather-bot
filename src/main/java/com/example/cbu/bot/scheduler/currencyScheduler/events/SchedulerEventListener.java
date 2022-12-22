package com.example.cbu.bot.scheduler.currencyScheduler.events;

import com.example.cbu.bot.scheduler.currencyScheduler.services.CronService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class SchedulerEventListener implements ApplicationListener<SchedulerEventDTO> {
    private final CronService cronService;
    private final Map<String, ScheduledTaskRegistrar> schedulers = new HashMap<>();

    public SchedulerEventListener(CronService cronService) {
        this.cronService = cronService;
    }

    @Override
    public void onApplicationEvent(SchedulerEventDTO dto) {
        var tasks = dto.getTasks();
        tasks.forEach(task -> {
            var scheduler = cronService.createScheduler(task);
            schedulers.put(task.getCurrencyCode(), scheduler);
        });
        log.info("All running schedulers : {}", schedulers.keySet());
    }
}
