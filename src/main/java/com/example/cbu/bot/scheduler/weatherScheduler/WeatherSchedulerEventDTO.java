package com.example.cbu.bot.scheduler.weatherScheduler;

import com.example.cbu.entity.UserSubscription;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
@Setter
public class WeatherSchedulerEventDTO extends ApplicationEvent {
    private List<UserSubscription> tasks;

    public WeatherSchedulerEventDTO(List<UserSubscription> source) {
        super(source);
        this.tasks = source;
    }

}
