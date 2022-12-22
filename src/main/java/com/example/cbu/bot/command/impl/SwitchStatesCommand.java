package com.example.cbu.bot.command.impl;

import com.example.cbu.bot.command.Command;
import com.example.cbu.bot.command.impl.subscription.SubscriptionCommand;
import com.example.cbu.entity.User;
import com.example.cbu.entity.UserSubscription;
import com.example.cbu.helper.CurrencyHelper;
import com.example.cbu.helper.WeatherHelper;
import com.example.cbu.service.UserService;
import com.example.cbu.service.UserSubscriptionService;
import com.example.cbu.utils.keyboards.CurrencyKeyboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.example.cbu.helper.KeyBoardHelper.*;

@Component
public class SwitchStatesCommand implements Command {
    private final UserService userService;
    private final CurrencyHelper currencyHelper;
    private final UserSubscriptionService subscriptionService;
    private final SubscriptionCommand subscriptionCommand;
    private final WeatherHelper weatherHelper;
    @Autowired
    private Environment env;

    public String getSelectedTime() {
        return env.getProperty("messages.subscribe.selected-time");
    }

    public String getSelectedTimeForTest(Environment environment) {
        return environment.getProperty("messages.subscribe.selected-time");
    }


    public SwitchStatesCommand(UserService userService, CurrencyHelper currencyHelper, UserSubscriptionService subscriptionService, SubscriptionCommand subscriptionCommand, WeatherHelper weatherHelper) {
        this.userService = userService;
        this.currencyHelper = currencyHelper;
        this.subscriptionService = subscriptionService;
        this.subscriptionCommand = subscriptionCommand;
        this.weatherHelper = weatherHelper;
    }

    @Override
    public void execute(Message message, SendMessage sendMessage) {
        sendMessage.setChatId(message.getChatId().toString());
        Optional<User> userEntity = userService.findById(message.getFrom().getId());
        userEntity.ifPresent(user -> {
            Optional<UserSubscription> subscriptionId = subscriptionService.findById(userEntity.get().getUserId());
            switch (userEntity.get().getLastBotState()) {
                case CURRENCY -> executeCurrencyCommand(message, sendMessage);
                case CURRENCY_DAILY_SENDING_HOURS -> executeCurrencyDaily(message, sendMessage);
                case WEATHER -> executeWeatherCommand(message, sendMessage);
                case WEATHER_DAILY_SENDING_HOURS -> executeWeatherDaily(message, sendMessage);
                case WEATHER_SUBSCRIPTION -> executeWeatherSubscription(sendMessage, message, subscriptionId);
                case CURRENCY_SUBSCRIPTION -> executeCurrencySubscription(sendMessage, message, subscriptionId);
            }
        });
    }

    private void executeWeatherDaily(Message message, SendMessage sendMessage) {
        subscriptionCommand.execute(message, sendMessage);
    }

    private void executeCurrencyDaily(Message message, SendMessage sendMessage) {
        subscriptionCommand.execute(message, sendMessage);
    }

    public void executeCurrencyCommand(Message message, SendMessage sendMessage) {
        List<String> currencyButtons = CurrencyKeyboard.getCurrencyButtons();
        HashMap<String, String> flags = CurrencyKeyboard.getFlags();
        if (currencyButtons.contains(message.getText())) {
            defineCurrencyType(message.getText(), flags.get(message.getText()), sendMessage);
        }
    }

    public void executeCurrencySubscription(SendMessage sendMessage, Message message, Optional<UserSubscription> subscriptionId) {
        String currencyFTime = message.getText();
        String currencyTime = currencyFTime.substring(0, currencyFTime.indexOf(":"));
        if (subscriptionId.isPresent()) {
            subscriptionId.get().setCurrencySubscription(true);
            subscriptionId.get().setCurrencyTime("0 0 " + currencyTime + " * * * ");
            subscriptionService.save(subscriptionId.get());
            sendMessage.setText(getSelectedTime() + currencyFTime);
            sendMessage.setReplyMarkup(getMainMenuKeyboard());
        }
    }

    public void executeCurrencySubscription(SendMessage sendMessage, Message message, Optional<UserSubscription> subscriptionId, Environment env) {
        String currencyFTime = message.getText();
        String currencyTime = currencyFTime.substring(0, currencyFTime.indexOf(":"));
        if (subscriptionId.isPresent()) {
            subscriptionId.get().setCurrencySubscription(true);
            subscriptionId.get().setCurrencyTime("0 0 " + currencyTime + " * * * ");
            subscriptionService.save(subscriptionId.get());
            sendMessage.setText(getSelectedTimeForTest(env) + currencyFTime);
            sendMessage.setReplyMarkup(getMainMenuKeyboard());
        }
    }


    public void executeWeatherSubscription(SendMessage sendMessage, Message message, Optional<UserSubscription> subscriptionId) {
        String weatherFTime = message.getText();
        String weatherTime = weatherFTime.substring(0, weatherFTime.indexOf(":"));
        if (subscriptionId.isPresent()) {
            subscriptionId.get().setWeatherSubscription(true);
            subscriptionId.get().setWeatherTime("0 0 " + weatherTime + " * * * ");
            subscriptionService.save(subscriptionId.get());
            sendMessage.setText(getSelectedTime() + weatherFTime);
            sendMessage.setReplyMarkup(getMainMenuKeyboard());
        }
    }

    public void executeWeatherSubscription(SendMessage sendMessage, Message message, Optional<UserSubscription> subscriptionId, Environment env) {
        String weatherFTime = message.getText();
        String weatherTime = weatherFTime.substring(0, weatherFTime.indexOf(":"));
        if (subscriptionId.isPresent()) {
            subscriptionId.get().setWeatherSubscription(true);
            subscriptionId.get().setWeatherTime("0 0 " + weatherTime + " * * * ");
            subscriptionService.save(subscriptionId.get());
            sendMessage.setText(getSelectedTimeForTest(env) + weatherFTime);
            sendMessage.setReplyMarkup(getMainMenuKeyboard());
        }
    }

    public void executeWeatherCommand(Message message, SendMessage sendMessage) {
        String city = message.getText();
        sendMessage.setText(weatherHelper.getWeather(city).toString());
        sendMessage.setReplyMarkup(getCityKeyboard());
    }

    public void defineCurrencyType(String currencyType, String flag, SendMessage sendMessage) {
        currencyHelper.getCurrencies().forEach(currencyDTO -> {
            if (currencyDTO.getCcy().equals(currencyType)) {
                String overall = currencyDTO.getCcy() + " " + flag + "\n";
                sendMessage.setText(overall + currencyDTO);
            }
        });
        sendMessage.setReplyMarkup(getCurrencyKeyBoard());
    }

    @Override
    public String getCommandName() {
        return "switch";
    }
}
