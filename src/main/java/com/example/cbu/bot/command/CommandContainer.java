package com.example.cbu.bot.command;

import com.example.cbu.bot.command.impl.*;
import com.example.cbu.helper.CurrencyHelper;
import com.example.cbu.service.UserService;
import com.example.cbu.service.UserSubscriptionService;
import com.example.cbu.utils.keyboards.MenuKeyboard;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;

public class CommandContainer {
    private final UserService userService;
    private final CurrencyHelper currencyHelper;
    private final UserSubscriptionService subscriptionService;

    private  Command startCommand;
    private  Command currencyCommand;
    private  Command mainMenuCommand;
    private  Command subscribeCommand;
    private  Command switchStateCommand;
    private  Command weatherCommand;

    public CommandContainer(UserService userService,  CurrencyHelper currencyHelper, UserSubscriptionService subscriptionService) {
        this.userService = userService;
        this.currencyHelper = currencyHelper;
        this.subscriptionService = subscriptionService;

        this.startCommand = new StartCommand(userService);
        this.currencyCommand = new CurrencyCommand(userService);
        this.mainMenuCommand = new MainMenuCommand(userService);
        this.switchStateCommand = new SwitchStateCommand(userService, currencyHelper);
        this.subscribeCommand = new SubscriptionCommand(userService, subscriptionService);
        this.weatherCommand = new WeatherCommand(userService);
    }

    public HashMap<String, Command> getCommands() {
        HashMap<String, Command> commands = new HashMap<>();
        commands.put("start", startCommand);
        commands.put("currency", currencyCommand);
        commands.put("menu", mainMenuCommand);
        commands.put("subscribe", subscribeCommand);
        commands.put("switch", switchStateCommand);
        commands.put("weather", weatherCommand);
        return commands;
    }

    public void defineCommandButton(String button, HashMap<String, Command> commands, Message message, SendMessage sendMessage) {
        switch (button) {
            case "/start":
                commands.get("start").execute(message, sendMessage);
                break;
            case MenuKeyboard.HAVO_SA:
                commands.get("weather").execute(message, sendMessage);
                break;
            case MenuKeyboard.KURS_SA:
                commands.get("currency").execute(message, sendMessage);
                break;
            case MenuKeyboard.BOSH_SA:
                commands.get("menu").execute(message, sendMessage);
                break;
            case MenuKeyboard.OBUNA_SA:
                commands.get("subscribe").execute(message, sendMessage);
                break;
            default:
                commands.get("switch").execute(message, sendMessage);
                break;
        }
    }


}
