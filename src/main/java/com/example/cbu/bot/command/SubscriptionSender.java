package com.example.cbu.bot.command;
import com.example.cbu.bot.SubscriptionFeign;
import com.example.cbu.entity.User;
import com.example.cbu.entity.UserSubscription;
import com.example.cbu.helper.CurrencyHelper;
import com.example.cbu.helper.WheatherHelper;
import com.example.cbu.service.UserSubscriptionService;
import com.example.cbu.utils.keyboards.CurrencyKeyboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
@EnableScheduling
public class SubscriptionSender {
    private final UserSubscriptionService subscriptionService;
    private final CurrencyHelper currencyHelper;
    @Autowired
    SubscriptionFeign subscriptionFeign;
    Logger logger = LoggerFactory.getLogger(SubscriptionSender.class);
    SendMessage sendMessage = new SendMessage();
    public SubscriptionSender(UserSubscriptionService subscriptionService, CurrencyHelper currencyHelper) {
        this.subscriptionService = subscriptionService;
        this.currencyHelper = currencyHelper;
    }

    public void subscribed(String currencyFTime, Long userId) {
        Optional<UserSubscription> byId = subscriptionService.findById(userId);
        if (byId.isPresent()) {
            String currencyCode = byId.get().getCurrencyCode();
            List<String> currencyButtons = CurrencyKeyboard.getCurrencyButtons();
            HashMap<String, String> flags = CurrencyKeyboard.getFlags();
            if (currencyButtons.contains(currencyCode)) {
                defineCurrencyType(currencyCode, flags.get(currencyCode), sendMessage);
            }
        }
    }

@Scheduled(cron = "* * * * * *")
    public void executeCurrency() {
        subscriptionFeign.sendMessage(sendMessage);
        logger.info("\nLogging: " + sendMessage.getText());
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

}
