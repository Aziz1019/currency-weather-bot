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
        if (userEntity.isPresent()) {
            Optional<UserSubscription> subscriptionId = subscriptionService.findById(userEntity.get().getUserId());
            switch (userEntity.get().getLastBotState()) {
                case WEATHER_DAILY_SENDING_HOURS -> {
                    userEntity.get().setLastBotState(BotState.WEATHER_SUBSCRIPTION);
                    userService.save(userEntity.get());
                    String cityName = message.getText();
                    if (subscriptionId.isPresent()) {
                        subscriptionId.get().setCityName(cityName);
                        subscriptionService.save(subscriptionId.get());
                    } else {
                        subscriptionService.save(new UserSubscription(
                                userEntity.get().getUserId(),
                                userEntity.get().getFirstName(),
                                userEntity.get().getLastName(),
                                userEntity.get().getUsername(),
                                cityName,
                                true
                        ));
                    }
                    sendMessage.setText("Har kuni ob-havo ma'lumotini olish uchun quyidagi vaqtlardan birini tanlang!");
                    sendMessage.setReplyMarkup(KeyBoardHelper.getTimeKeyboards());
                }
                case CURRENCY_DAILY_SENDING_HOURS -> {
                    userEntity.get().setLastBotState(BotState.CURRENCY_SUBSCRIPTION);
                    userService.save(userEntity.get());

                    String currencyCode = message.getText();
                    if (subscriptionId.isPresent()) {
                        subscriptionId.get().setCurrencyCode(currencyCode);
                        subscriptionService.save(subscriptionId.get());
                    } else {
                        subscriptionService.save(new UserSubscription(
                                true,
                                userEntity.get().getUserId(),
                                userEntity.get().getFirstName(),
                                userEntity.get().getLastName(),
                                userEntity.get().getUsername(),
                                currencyCode
                        ));
                    }
                    sendMessage.setText("Har kuni Valyuta kursini olish uchun quyidagi vaqtlardan birini tanlang!");
                    sendMessage.setReplyMarkup(KeyBoardHelper.getTimeKeyboards());
                }
            }
        }
    }

    @Override
    public String getCommandName() {
        return "set-subscribe";
    }
}

