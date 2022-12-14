package com.example.cbu.bot.command;

import com.example.cbu.helper.CurrencyHelper;
import com.example.cbu.service.UserService;
import com.example.cbu.service.UserSubscriptionService;
import com.example.cbu.utils.keyboards.MenuKeyboard;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;


public class CommandContainer {
    private final List<Command> commandLists;

    public CommandContainer(List<Command> commandLists) {
        this.commandLists = commandLists;
    }

    public HashMap<String, Command> getCommands() {
        HashMap<String, Command> commands = new HashMap<>();
        commandLists.forEach(
                com -> {
                    commands.put(com.getCommandName(), com);
                });
        return commands;
    }

    public void defineCommandButton(String button, HashMap<String, Command> commands, Message message, SendMessage sendMessage) {
        HashMap<String, String> mainButtons = MenuKeyboard.getMainButtons();
        commands.get(mainButtons.getOrDefault(button, "switch")).execute(message, sendMessage);
    }
}
