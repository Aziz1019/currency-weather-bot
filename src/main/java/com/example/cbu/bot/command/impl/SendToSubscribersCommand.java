package com.example.cbu.bot.command.impl;

import com.example.cbu.bot.command.Command;
import com.example.cbu.entity.User;
import com.example.cbu.entity.UserSubscription;
import com.example.cbu.service.UserService;
import com.example.cbu.service.UserSubscriptionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.util.List;
import java.util.Optional;

import static com.example.cbu.helper.KeyBoardHelper.getMainMenuKeyboard;
public class SendToSubscribersCommand implements Command {
    private final UserService userService;
    private final UserSubscriptionService subscriptionService;
    public SendToSubscribersCommand(UserService userService, UserSubscriptionService subscriptionService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }
    @Override
    public void execute(Message message, SendMessage sendMessage) {
        sendMessage.setChatId(message.getChatId().toString());
        List<UserSubscription> allByCurrencySubscriptionIsTrue = subscriptionService.findAllByCurrencySubscriptionIsTrue();
//        List<UserSubscription> allByWeatherSubscriptionIsTrue = subscriptionService.findAllByWeatherSubscriptionIsTrue();

        for (UserSubscription userSubscription : allByCurrencySubscriptionIsTrue) {
            Optional<User> user = userService.findById(userSubscription.getUserId());
            if (user.isPresent()) {
                sendMessage.setChatId(user.get().getUserId().toString());
                sendMessage.setText("Hello, " + user.get().getFirstName() + "!");
                sendMessage.setReplyMarkup(getMainMenuKeyboard());
            }
        }
    }
    @Override
    public String getCommandName() {
        return "notification-sender";
    }
}
