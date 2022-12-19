package com.example.cbu.bot.command.impl;

import com.example.cbu.bot.command.Command;
import com.example.cbu.bot.command.SubscriptionSender;
import com.example.cbu.bot.command.impl.subscription.SubscriptionCommand;
import com.example.cbu.entity.User;
import com.example.cbu.entity.UserSubscription;
import com.example.cbu.helper.CurrencyHelper;
import com.example.cbu.helper.WheatherHelper;
import com.example.cbu.service.UserService;
import com.example.cbu.service.UserSubscriptionService;
import com.example.cbu.utils.keyboards.CurrencyKeyboard;
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

    private final SubscriptionSender subscriptionSender;

    public SwitchStatesCommand(UserService userService, CurrencyHelper currencyHelper, UserSubscriptionService subscriptionService, SubscriptionSender sendToSubscribersCommand, SubscriptionCommand subscriptionCommand, SubscriptionSender subscriptionSender) {
        this.userService = userService;
        this.currencyHelper = currencyHelper;
        this.subscriptionService = subscriptionService;
        this.subscriptionCommand = subscriptionCommand;
        this.subscriptionSender = subscriptionSender;
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
                case WEATHER_SUBSCRIPTION -> executeWeatherSubscription(sendMessage, message, subscriptionId, userEntity);
                case CURRENCY_SUBSCRIPTION -> executeCurrencySubscription(sendMessage, message, subscriptionId, userEntity);
            }
        });
    }

    private void executeWeatherDaily(Message message, SendMessage sendMessage) {
        subscriptionCommand.execute(message, sendMessage);
    }

    private void executeCurrencyDaily(Message message, SendMessage sendMessage) {
        subscriptionCommand.execute(message, sendMessage);
    }

    public void executeCurrencyCommand(Message message, SendMessage sendMessage){
        List<String> currencyButtons = CurrencyKeyboard.getCurrencyButtons();
        HashMap<String, String> flags = CurrencyKeyboard.getFlags();
        if (currencyButtons.contains(message.getText())) {
            defineCurrencyType(message.getText(), flags.get(message.getText()), sendMessage);
        }
    }

    private void executeCurrencySubscription(SendMessage sendMessage, Message message, Optional<UserSubscription> subscriptionId, Optional<User> userEntity) {
        String currencyFTime = message.getText();
        String currencyTime = currencyFTime.substring(0, currencyFTime.indexOf(":"));
        if (subscriptionId.isPresent()) {
            subscriptionId.get().setCurrencySubscription(true);
            subscriptionId.get().setCurrencyTime(currencyTime);
            subscriptionService.save(subscriptionId.get());
            sendMessage.setText("Tanlangan vaqt: " + currencyFTime);
            sendMessage.setReplyMarkup(getMainMenuKeyboard());
            userEntity.ifPresent(user -> subscriptionSender.subscribed(currencyFTime, user.getUserId()));
        }
    }

    private void executeWeatherSubscription(SendMessage sendMessage, Message message, Optional<UserSubscription> subscriptionId, Optional<User> userEntity) {
        String weatherFTime = message.getText();
        String weatherTime = weatherFTime.substring(0, weatherFTime.indexOf(":"));
        if (subscriptionId.isPresent()) {
            subscriptionId.get().setWeatherSubscription(true);
            subscriptionId.get().setWeatherTime(weatherTime);
            subscriptionService.save(subscriptionId.get());
            sendMessage.setText("Tanlangan vaqt: " + weatherFTime);
            sendMessage.setReplyMarkup(getMainMenuKeyboard());
        }
    }

    private void executeWeatherCommand(Message message, SendMessage sendMessage) {
        String city = message.getText();
        sendMessage.setText(WheatherHelper.getWeather(city).toString());
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
