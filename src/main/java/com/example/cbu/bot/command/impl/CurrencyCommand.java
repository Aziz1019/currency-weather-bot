package com.example.cbu.bot.command.impl;

import com.example.cbu.bot.BotState;
import com.example.cbu.bot.command.Command;
import com.example.cbu.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.example.cbu.helper.KeyBoardHelper.getCurrencyKeyBoard;

@Component
public class CurrencyCommand implements Command {
    private final UserService userService;
    public CurrencyCommand(UserService userService) {
        this.userService = userService;
    }
    @Override
    public void execute(Message message, SendMessage sendMessage) {
        sendMessage.setChatId(message.getChatId().toString());
        userService.findById(message.getFrom().getId()).ifPresent(user -> {
            user.setLastBotState(BotState.CURRENCY);
            userService.save(user);
        });
        sendMessage.setText("Valyutani tanlang");
        sendMessage.setReplyMarkup(getCurrencyKeyBoard());
    }

    @Override
    public String getCommandName() {
        return "currency";
    }
}
