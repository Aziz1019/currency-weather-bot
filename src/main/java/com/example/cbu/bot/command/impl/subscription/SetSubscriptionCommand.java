package com.example.cbu.bot.command.impl.subscription;

import com.example.cbu.bot.BotState;
import com.example.cbu.bot.command.Command;
import com.example.cbu.entity.User;
import com.example.cbu.helper.KeyBoardHelper;
import com.example.cbu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.util.Optional;

@Component
public class SetSubscriptionCommand implements Command {
    private final UserService userService;

    public SetSubscriptionCommand(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private Environment env;
    public String getSelectCityMessage(){
        return env.getProperty("messages.subscribe.select-city-message");
    }

    public String getSelectCurrencyMessage(){
        return env.getProperty("messages.subscribe.select-currency-message");
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
                    sendMessage.setText(getSelectCityMessage());
                    sendMessage.setReplyMarkup(KeyBoardHelper.getCitySubKeyboard());
                }
                case CURRENCY -> {
                    userEntity.get().setLastBotState(BotState.CURRENCY_DAILY_SENDING_HOURS);
                    userService.save(userEntity.get());
                    sendMessage.setText(getSelectCurrencyMessage());
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

