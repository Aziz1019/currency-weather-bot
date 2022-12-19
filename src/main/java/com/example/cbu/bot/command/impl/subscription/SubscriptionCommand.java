package com.example.cbu.bot.command.impl.subscription;

import com.example.cbu.bot.BotState;
import com.example.cbu.bot.command.Command;
import com.example.cbu.entity.User;
import com.example.cbu.entity.UserSubscription;
import com.example.cbu.helper.KeyBoardHelper;
import com.example.cbu.service.UserService;
import com.example.cbu.service.UserSubscriptionService;
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
                            userEntity.get().getLastName(),
                            userEntity.get().getUsername(),
                            cityName,
                            true
                    )));
                    sendMessage.setText("Har kuni ob-havo ma'lumotini olish uchun quyidagi vaqtlardan birini tanlang!");
                    sendMessage.setReplyMarkup(KeyBoardHelper.getTimeKeyboards());
                }
                case CURRENCY_DAILY_SENDING_HOURS -> {
                    setBotState(userEntity, BotState.CURRENCY_SUBSCRIPTION);
                    String currencyCode = message.getText();
                    subscriptionId.ifPresentOrElse(userSubscription -> setCurrencyCode(subscriptionId, currencyCode), () -> subscriptionService.save(new UserSubscription(
                            userEntity.get().getUserId(),
                            userEntity.get().getFirstName(),
                            userEntity.get().getLastName(),
                            userEntity.get().getUsername(),
                            currencyCode,
                            true
                    )));
                    sendMessage.setText("Har kuni Valyuta kursini olish uchun quyidagi vaqtlardan birini tanlang!");
                    sendMessage.setReplyMarkup(KeyBoardHelper.getTimeKeyboards());
                }
            }
        });
    }

    private void setCityName(Optional<UserSubscription> subscriptionId, String cityName) {
        subscriptionId.get().setCityName(cityName);
        subscriptionService.save(subscriptionId.get());
    }

    private void setCurrencyCode(Optional<UserSubscription> subscriptionId, String cityName) {
        subscriptionId.get().setCityName(cityName);
        subscriptionService.save(subscriptionId.get());
    }

    private void setBotState(Optional<User> userEntity, BotState subscriptionState) {
        userEntity.get().setLastBotState(subscriptionState);
        userService.save(userEntity.get());
    }

    @Override
    public String getCommandName() {
        return "set-subscribe";
    }
}

