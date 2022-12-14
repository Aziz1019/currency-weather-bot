package com.example.cbu.bot.command.impl;

import com.example.cbu.bot.command.Command;
import com.example.cbu.entity.User;
import com.example.cbu.helper.CurrencyHelper;
import com.example.cbu.helper.WhetherHelper;
import com.example.cbu.service.UserService;
import com.example.cbu.utils.keyboards.CurrencyKeyboard;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

import static com.example.cbu.helper.KeyBoardHelper.getCityKeyboard;
import static com.example.cbu.helper.KeyBoardHelper.getCurrencyKeyBoard;

@Component
public class SwitchStateCommand implements Command {
    private final UserService userService;
    private final CurrencyHelper currencyHelper;

    public SwitchStateCommand(UserService userService, CurrencyHelper currencyHelper) {
        this.userService = userService;
        this.currencyHelper = currencyHelper;
    }

    @Override
    public void execute(Message message, SendMessage sendMessage) {
        sendMessage.setChatId(message.getChatId().toString());
        Optional<User> userEntity = userService.findById(message.getFrom().getId());
        if (userEntity.isPresent()) {
            switch (userEntity.get().getLastBotState()) {
                case CURRENCY:
                    switch (message.getText()) {
                        case CurrencyKeyboard.USD:
                            defineCurrencyType(message.getText(), "\uD83C\uDDFA\uD83C\uDDF8", sendMessage);
                            break;
                        case CurrencyKeyboard.EUR:
                            defineCurrencyType(message.getText(), "\uD83C\uDDEA\uD83C\uDDFA", sendMessage);
                            break;
                        case CurrencyKeyboard.GBP:
                            defineCurrencyType(message.getText(), "\uD83C\uDDEC\uD83C\uDDE7", sendMessage);
                            break;
                        case CurrencyKeyboard.RUB:
                            defineCurrencyType(message.getText(), "\uD83C\uDDF7\uD83C\uDDFA", sendMessage);
                            break;
                    }
                    break;
                case WEATHER:
                    String city = message.getText();
                    sendMessage.setText(WhetherHelper.getWeather(city).toString());
                    sendMessage.setReplyMarkup(getCityKeyboard());
                    break;
            }
        }

    }

    @Override
    public String getCommandName() {
        return "switch";
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
}
