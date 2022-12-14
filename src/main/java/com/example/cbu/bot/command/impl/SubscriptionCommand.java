package com.example.cbu.bot.command.impl;

import com.example.cbu.bot.BotState;
import com.example.cbu.bot.command.Command;
import com.example.cbu.entity.User;
import com.example.cbu.entity.UserSubscription;
import com.example.cbu.service.UserService;
import com.example.cbu.service.UserSubscriptionService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

import static com.example.cbu.helper.KeyBoardHelper.getMainMenuKeyboard;

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
        if (userEntity.isPresent()) {
            Optional<UserSubscription> subscriptionId = subscriptionService.findById(userEntity.get().getUserId());
            if (userEntity.get().getLastBotState() == BotState.WEATHER) {
                userEntity.get().setLastBotState(BotState.WEATHER_SUBSCRIPTION);
                userService.save(userEntity.get());
                if (subscriptionId.isPresent()) {
                    subscriptionId.get().setWeatherSubscription(true);
                    subscriptionService.save(subscriptionId.get());
                }
                else {
                    subscriptionService.save(new UserSubscription(
                            userEntity.get().getUserId(),
                            userEntity.get().getFirstName(),
                            userEntity.get().getLastName(),
                            userEntity.get().getUsername(),
                            true
                    ));
                }
            }
            else if (userEntity.get().getLastBotState() == BotState.CURRENCY) {
                userEntity.get().setLastBotState(BotState.CURRENCY_SUBSCRIPTION);
                userService.save(userEntity.get());
                if (subscriptionId.isPresent()) {
                    subscriptionId.get().setCurrencySubscription(true);
                    subscriptionService.save(subscriptionId.get());
                }
                else {
                    subscriptionService.save(new UserSubscription(
                            true,
                            userEntity.get().getUserId(),
                            userEntity.get().getFirstName(),
                            userEntity.get().getLastName(),
                            userEntity.get().getUsername()
                    ));
                }
            }
        }
        sendMessage.setText("Siz muvaffaqiyatli Obuna bo'ldingiz!✅");
        sendMessage.setReplyMarkup(getMainMenuKeyboard());
    }
}
