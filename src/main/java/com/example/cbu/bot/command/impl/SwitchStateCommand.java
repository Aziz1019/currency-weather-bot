package com.example.cbu.bot.command.impl;

import com.example.cbu.bot.command.Command;
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
public class SwitchStateCommand implements Command {
    private final UserService userService;
    private final CurrencyHelper currencyHelper;
    private final UserSubscriptionService subscriptionService;

    public SwitchStateCommand(UserService userService, CurrencyHelper currencyHelper, UserSubscriptionService subscriptionService, SubscriptionSender sendToSubscribersCommand) {
        this.userService = userService;
        this.currencyHelper = currencyHelper;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public void execute(Message message, SendMessage sendMessage) {
        sendMessage.setChatId(message.getChatId().toString());
        Optional<User> userEntity = userService.findById(message.getFrom().getId());
        userEntity.ifPresent(user -> {
            Optional<UserSubscription> subscriptionId = subscriptionService.findById(userEntity.get().getUserId());
            switch (userEntity.get().getLastBotState()) {
                case CURRENCY -> executeCurrencyCommand(message, sendMessage);
                case WEATHER -> executeWeatherCommand(message, sendMessage);
                case WEATHER_SUBSCRIPTION -> executeWeatherSubscription(sendMessage, message, subscriptionId, userEntity);
                case CURRENCY_SUBSCRIPTION -> executeCurrencySubscription(sendMessage, message, subscriptionId, userEntity);
            }
        });
    }

    public void executeCurrencyCommand(Message message, SendMessage sendMessage){
        List<String> currencyButtons = CurrencyKeyboard.getCurrencyButtons();
        HashMap<String, String> flags = CurrencyKeyboard.getFlags();
        if (currencyButtons.contains(message.getText())) {
            defineCurrencyType(message.getText(), flags.get(message.getText()), sendMessage);
        }
    }

    private void executeCurrencySubscription(SendMessage sendMessage, Message message, Optional<UserSubscription> subscriptionId, Optional<User> userEntity) {
        String currencyCode = message.getText();
        if (subscriptionId.isPresent()) {
            subscriptionId.get().setCurrencySubscription(true);
            subscriptionId.get().setCurrencyCode(currencyCode);
            subscriptionService.save(subscriptionId.get());
        } else {
            subscriptionService.save(new UserSubscription(
                    true,
                    userEntity.get().getUserId(),
                    userEntity.get().getFirstName(),
                    userEntity.get().getLastName(),
                    userEntity.get().getUsername(),
                    currencyCode
            ));
        }
        sendMessage.setText("Valyuta kursi obuna bo'lish uchun tanlangan valyuta: " + currencyCode);
        sendMessage.setReplyMarkup(getMainMenuKeyboard());
    }

    private void executeWeatherSubscription(SendMessage sendMessage, Message message, Optional<UserSubscription> subscriptionId, Optional<User> userEntity)  {
        String cityName = message.getText();
        if (subscriptionId.isPresent()) {
            subscriptionId.get().setWeatherSubscription(true);
            subscriptionId.get().setCityName(cityName);
            subscriptionService.save(subscriptionId.get());
        } else {
            subscriptionService.save(new UserSubscription(
                    userEntity.get().getUserId(),
                    userEntity.get().getFirstName(),
                    userEntity.get().getLastName(),
                    userEntity.get().getUsername(),
                    cityName,
                    true
            ));
        }
        sendMessage.setText("Ob-havo obuna bo'lish uchun tanlangan shahar: " + cityName);
        sendMessage.setReplyMarkup(getMainMenuKeyboard());
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
