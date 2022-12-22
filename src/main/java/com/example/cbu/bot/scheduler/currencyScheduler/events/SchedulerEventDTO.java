package com.example.cbu.bot.scheduler.currencyScheduler.events;

import com.example.cbu.entity.real.UserSubscription;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import java.util.List;

@Getter
@Setter
public class SchedulerEventDTO extends ApplicationEvent {
    private List<UserSubscription> tasks;

    public SchedulerEventDTO(List<UserSubscription> source) {
        super(source);
        this.tasks = source;
    }

}
