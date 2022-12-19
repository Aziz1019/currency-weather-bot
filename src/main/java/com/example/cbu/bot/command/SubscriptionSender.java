package com.example.cbu.bot.command;

import com.example.cbu.bot.SubscriptionFeign;
import com.example.cbu.entity.UserSubscription;
import com.example.cbu.helper.CurrencyHelper;
import com.example.cbu.helper.WheatherHelper;
import com.example.cbu.service.UserSubscriptionService;
import com.example.cbu.utils.keyboards.CurrencyKeyboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.HashMap;
import java.util.List;

@Component
@EnableScheduling
public class SubscriptionSender {
    private final UserSubscriptionService subscriptionService;
    private final CurrencyHelper currencyHelper;
    private final MyScheduler scheduler;

    @Autowired
    SubscriptionFeign subscriptionFeign;
    Logger logger = LoggerFactory.getLogger(SubscriptionSender.class);
    SendMessage sendMessage = new SendMessage();

    public SubscriptionSender(UserSubscriptionService subscriptionService, CurrencyHelper currencyHelper, MyScheduler scheduler) {
        this.subscriptionService = subscriptionService;
        this.currencyHelper = currencyHelper;
        this.scheduler = scheduler;
    }

    public void executeCurrency() {
        List<UserSubscription> allByCurrencySubscriptionIsTrue = subscriptionService.findAllByCurrencySubscriptionIsTrue();
        for (UserSubscription userSubscription : allByCurrencySubscriptionIsTrue) {
            sendMessage.setChatId(userSubscription.getUserId().toString());
            String currencyCode = userSubscription.getCurrencyCode();
            List<String> currencyButtons = CurrencyKeyboard.getCurrencyButtons();
            HashMap<String, String> flags = CurrencyKeyboard.getFlags();
            if (currencyButtons.contains(currencyCode)) {
                defineCurrencyType(currencyCode, flags.get(currencyCode), sendMessage);
            }
            subscriptionFeign.sendMessage(sendMessage);
            logger.info("\nLogging: " + sendMessage.getText());
        }
    }


    public void executeWeather() {
        List<UserSubscription> allByWeatherSubscriptionIsTrue = subscriptionService.findAllByWeatherSubscriptionIsTrue();
        for (UserSubscription userSubscription : allByWeatherSubscriptionIsTrue) {
            sendMessage.setChatId(userSubscription.getUserId().toString());
            sendMessage.setText(WheatherHelper.getWeather(userSubscription.getCityName()).toString());
        }
        subscriptionFeign.sendMessage(sendMessage);
        logger.info("\nLogging: " + sendMessage.getText());
    }

    public void defineCurrencyType(String currencyType, String flag, SendMessage sendMessage) {
        currencyHelper.getCurrencies().forEach(currencyDTO -> {
            if (currencyDTO.getCcy().equals(currencyType)) {
                String overall = currencyDTO.getCcy() + " " + flag + "\n";
                sendMessage.setText(overall + currencyDTO);
            }
        });
    }

    public void scheduleCurrency(String cronHour) {
        scheduler.scheduling(this::executeCurrency, cronHour + " * * * * *");
    }

    public void scheduleWeather(String cronHour) {
        scheduler.scheduling(this::executeWeather, cronHour + " * * * * *");
    }
}
