package com.example.cbu.bot.command.impl;

import com.example.cbu.bot.command.Command;
import com.example.cbu.entity.User;
import com.example.cbu.helper.CurrencyHelper;
import com.example.cbu.helper.WhetherHelper;
import com.example.cbu.service.UserService;
import com.example.cbu.utils.keyboards.CurrencyKeyboard;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

import static com.example.cbu.helper.KeyBoardHelper.getCityKeyboard;
import static com.example.cbu.helper.KeyBoardHelper.getCurrencyKeyBoard;

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
                    if (message.getText().equals(CurrencyKeyboard.USD)) {
                        currencyHelper.getCurrencies().forEach(currencyDTO -> {
                            if (currencyDTO.getCcy().equals(CurrencyKeyboard.USD)) {
                                String overall = currencyDTO.getCcy() + " \uD83C\uDDFA\uD83C\uDDF8" + "\n";
                                sendMessage.setText(overall + currencyDTO);
                            }
                        });
                        sendMessage.setReplyMarkup(getCurrencyKeyBoard());
                    } else if (message.getText().equals(CurrencyKeyboard.EUR)) {
                        currencyHelper.getCurrencies().forEach(currencyDTO -> {
                            if (currencyDTO.getCcy().equals(CurrencyKeyboard.EUR)) {
                                String overall = currencyDTO.getCcy() + " \uD83C\uDDEA\uD83C\uDDFA" + "\n";
                                sendMessage.setText(overall + currencyDTO);
                            }
                        });
                        sendMessage.setReplyMarkup(getCurrencyKeyBoard());
                    } else if (message.getText().equals(CurrencyKeyboard.GBP)) {
                        currencyHelper.getCurrencies().forEach(currencyDTO -> {
                            if (currencyDTO.getCcy().equals(CurrencyKeyboard.GBP)) {
                                String overall = currencyDTO.getCcy() + " \uD83C\uDDEC\uD83C\uDDE7" + "\n";
                                sendMessage.setText(overall + currencyDTO);
                            }
                        });
                        sendMessage.setReplyMarkup(getCurrencyKeyBoard());
                    } else if (message.getText().equals(CurrencyKeyboard.RUB)) {
                        currencyHelper.getCurrencies().forEach(currencyDTO -> {
                            if (currencyDTO.getCcy().equals(CurrencyKeyboard.RUB)) {
                                String overall = currencyDTO.getCcy() + " \uD83C\uDDF7\uD83C\uDDFA" + "\n";
                                sendMessage.setText(overall + currencyDTO);
                            }
                        });
                        sendMessage.setReplyMarkup(getCurrencyKeyBoard());
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
}
