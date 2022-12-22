package com.example.cbu.bot.command.impl.subscription;
import com.example.cbu.bot.BotState;
import com.example.cbu.bot.command.Command;
import com.example.cbu.entity.User;
import com.example.cbu.entity.UserSubscription;
import com.example.cbu.helper.KeyBoardHelper;
import com.example.cbu.service.UserService;
import com.example.cbu.service.UserSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.util.Optional;

@Component
public class SubscriptionCommand implements Command {
    private final UserService userService;
    private final UserSubscriptionService subscriptionService;
    public SubscriptionCommand(UserService userService, UserSubscriptionService subscriptionService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

    @Autowired
    private Environment env;
    public String getSelectWeatherTime(){
        return env.getProperty("messages.subscribe.select-weather-time-message");
    }
    public String getSelectCurrencyTime(){
        return env.getProperty("messages.subscribe.select-currency-time-message");
    }

    @Override
    public void execute(Message message, SendMessage sendMessage) {
        sendMessage.setChatId(message.getChatId().toString());
        Optional<User> userEntity = userService.findById(message.getFrom().getId());
        userEntity.ifPresent(user -> {
            Optional<UserSubscription> subscriptionId = subscriptionService.findById(userEntity.get().getUserId());
            switch (userEntity.get().getLastBotState()) {
                case WEATHER_DAILY_SENDING_HOURS -> {
                    setBotState(userEntity, BotState.WEATHER_SUBSCRIPTION);
                    String cityName = message.getText();
                    subscriptionId.ifPresentOrElse(userSubscription -> setCityName(subscriptionId, cityName), () -> subscriptionService.save(new UserSubscription(
                            userEntity.get().getUserId(),
                            userEntity.get().getFirstName(),
                            userEntity.get().getUsername(),
                            cityName,
                            true
                    )));
                    sendMessage.setText(getSelectWeatherTime());
                    sendMessage.setReplyMarkup(KeyBoardHelper.getTimeKeyboards());
                }
                case CURRENCY_DAILY_SENDING_HOURS -> {
                    setBotState(userEntity, BotState.CURRENCY_SUBSCRIPTION);
                    String currencyCode = message.getText();
                    subscriptionId.ifPresentOrElse(userSubscription -> setCurrencyCode(subscriptionId, currencyCode), () -> subscriptionService.save(new UserSubscription(
                            true,
                            userEntity.get().getUserId(),
                            userEntity.get().getFirstName(),
                            userEntity.get().getUsername(),
                            currencyCode
                    )));
                    sendMessage.setText(getSelectCurrencyTime());
                    sendMessage.setReplyMarkup(KeyBoardHelper.getTimeKeyboards());
                }
            }
        });
    }
    private void setCityName(Optional<UserSubscription> subscriptionId, String cityName) {
        subscriptionId.ifPresent(userSubscription -> {
            userSubscription.setCityName(cityName);
            subscriptionService.save(userSubscription);
        });
    }
    private void setCurrencyCode(Optional<UserSubscription> subscriptionId, String cityName) {
        subscriptionId.ifPresent(userSubscription -> {
            userSubscription.setCurrencyCode(cityName);
            subscriptionService.save(userSubscription);
        });
    }
    private void setBotState(Optional<User> userEntity, BotState subscriptionState) {
        userEntity.ifPresent(user -> {
            user.setLastBotState(subscriptionState);
            userService.save(user);
        });
    }

    @Override
    public String getCommandName() {
        return "set-subscribe";
    }
}

