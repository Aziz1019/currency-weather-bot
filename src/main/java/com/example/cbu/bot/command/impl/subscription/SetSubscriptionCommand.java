package com.example.cbu.bot.command.impl.subscription;

import com.example.cbu.bot.BotState;
import com.example.cbu.bot.command.Command;
import com.example.cbu.entity.User;
import com.example.cbu.helper.KeyBoardHelper;
import com.example.cbu.service.UserService;
import com.example.cbu.service.UserSubscriptionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.util.Optional;

@Component
public class SetSubscriptionCommand implements Command {
    private final UserService userService;

    public SetSubscriptionCommand(UserService userService, UserSubscriptionService subscriptionService) {
        this.userService = userService;
    }

    @Override
    public void execute(Message message, SendMessage sendMessage) {
        sendMessage.setChatId(message.getChatId().toString());
        Optional<User> userEntity = userService.findById(message.getFrom().getId());
        userEntity.ifPresent(user -> {
            switch (userEntity.get().getLastBotState()) {
                case WEATHER -> {
                    userEntity.get().setLastBotState(BotState.WEATHER_DAILY_SENDING_HOURS);
                    userService.save(userEntity.get());
                    sendMessage.setText("Ob-havo ga obuna bo'lish uchun quyidagi shaharlardan birini tanlang");
                    sendMessage.setReplyMarkup(KeyBoardHelper.getCitySubKeyboard());
                }
                case CURRENCY -> {
                    userEntity.get().setLastBotState(BotState.CURRENCY_DAILY_SENDING_HOURS);
                    userService.save(userEntity.get());
                    sendMessage.setText("Valyuta kursiga obuna bo'lish uchun quyidagi valyutalardan birini tanlang");
                    sendMessage.setReplyMarkup(KeyBoardHelper.getCurrencySubKeyboard());
                }
            }
        });
    }

    @Override
    public String getCommandName() {
        return "subscribe";
    }
}

