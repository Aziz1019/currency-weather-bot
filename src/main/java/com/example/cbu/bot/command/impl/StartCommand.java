package com.example.cbu.bot.command.impl;

import com.example.cbu.bot.BotState;
import com.example.cbu.bot.command.Command;
import com.example.cbu.entity.User;
import com.example.cbu.helper.KeyBoardHelper;
import com.example.cbu.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.example.cbu.helper.KeyBoardHelper.getKeyboard;

@Component
public class StartCommand implements Command {
    private final UserService service;
    public StartCommand(UserService service) {
        this.service = service;
    }
    @Override
    public void execute(Message message, SendMessage sendMessage) {
        User user = new User(
                message.getFrom().getId(),
                message.getFrom().getFirstName(),
                message.getFrom().getLastName(),
                message.getFrom().getUserName(),
                BotState.START
        );
        service.save(user);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("Assalomu alaykum, " + message.getFrom().getFirstName() + "!");
        sendMessage.setReplyMarkup(getKeyboard());
    }

    @Override
    public String getCommandName() {
        return "start";
    }
}
