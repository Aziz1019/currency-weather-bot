package com.example.cbu.bot.scheduler.currencyScheduler.services;

import com.example.cbu.bot.SubscriptionFeign;
import com.example.cbu.entity.real.UserSubscription;
import com.example.cbu.helper.CurrencyHelper;
import com.example.cbu.utils.keyboards.CurrencyKeyboard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class CronService implements SchedulingConfigurer {
    @Autowired
    SubscriptionFeign subscriptionFeign;

    private final CurrencyHelper currencyHelper;

    public CronService(CurrencyHelper currencyHelper) {
        this.currencyHelper = currencyHelper;
    }

    public ScheduledTaskRegistrar createScheduler(UserSubscription userSubscription) {
        SendMessage sendMessage = new SendMessage();
        Runnable runnable = () -> {
            log.info("Start process at : {}", LocalDateTime.now());
            sendMessage.setChatId(userSubscription.getUserId().toString());
            String currencyCode = userSubscription.getCurrencyCode();
            List<String> currencyButtons = CurrencyKeyboard.getCurrencyButtons();
            HashMap<String, String> flags = CurrencyKeyboard.getFlags();
            if (currencyButtons.contains(currencyCode)) {
                defineCurrencyType(currencyCode, flags.get(currencyCode), sendMessage);
            }
            subscriptionFeign.sendMessage(sendMessage);
        };
        CronTask cronTask = createCronTask(runnable, userSubscription.getCurrencyTime());
        ScheduledTaskRegistrar taskRegistrar = new ScheduledTaskRegistrar();
        taskRegistrar.addCronTask(cronTask);
        configureTasks(taskRegistrar);
        return taskRegistrar;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.afterPropertiesSet();
    }

    public CronTask createCronTask(Runnable action, String expression) {
        return new CronTask(action, new CronTrigger(expression));
    }
    public void defineCurrencyType(String currencyType, String flag, SendMessage sendMessage) {
        currencyHelper.getCurrencies().forEach(currencyDTO -> {
            if (currencyDTO.getCcy().equals(currencyType)) {
                String overall = currencyDTO.getCcy() + " " + flag + "\n";
                sendMessage.setText(overall + currencyDTO);
            }
        });
    }
}
