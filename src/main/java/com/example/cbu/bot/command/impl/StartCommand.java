package com.example.cbu.bot.command.impl;

import com.example.cbu.bot.BotState;
import com.example.cbu.bot.command.Command;
import com.example.cbu.entity.real.User;
import com.example.cbu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import static com.example.cbu.helper.KeyBoardHelper.getKeyboard;

@Component
public class StartCommand implements Command {
    @Autowired
    private Environment env;

    public String getStartMessage(){
        return env.getProperty("messages.start.greeting-message");
    }

    private final UserService service;
    public StartCommand(UserService service) {
        this.service = service;
    }
    @Override
    public void execute(Message message, SendMessage sendMessage) {
        User user = new User(
                message.getFrom().getId(),
                message.getFrom().getFirstName(),
                message.getFrom().getUserName(),
                BotState.START
        );
        service.save(user);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(getStartMessage() + " " + message.getFrom().getFirstName() + "!");
        sendMessage.setReplyMarkup(getKeyboard());
    }

    @Override
    public String getCommandName() {
        return "start";
    }
}
