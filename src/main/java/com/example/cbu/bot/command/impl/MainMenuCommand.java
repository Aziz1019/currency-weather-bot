package com.example.cbu.bot.command.impl;

import com.example.cbu.bot.BotState;
import com.example.cbu.bot.command.Command;
import com.example.cbu.service.UserService;
import com.example.cbu.utils.keyboards.MenuKeyboard;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.example.cbu.helper.KeyBoardHelper.getKeyboard;

@Component
public class MainMenuCommand implements Command {
    private final UserService userService;

    public MainMenuCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void execute(Message message, SendMessage sendMessage) {
        sendMessage.setChatId(message.getChatId().toString());
        userService.findById(message.getFrom().getId()).ifPresent(user -> {
            user.setLastBotState(BotState.MAIN_MENU);
            userService.save(user);
        });
        sendMessage.setText(MenuKeyboard.BOSH_SA);
        sendMessage.setReplyMarkup(getKeyboard());
    }

    @Override
    public String getCommandName() {
        return "menu";
    }
}
