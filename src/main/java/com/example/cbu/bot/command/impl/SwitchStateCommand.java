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

import java.util.HashMap;
import java.util.List;
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
        userEntity.ifPresent(user -> {
            switch (userEntity.get().getLastBotState()) {
                case CURRENCY:
                    List<String> currencyButtons = CurrencyKeyboard.getCurrencyButtons();
                    HashMap<String, String> flags = CurrencyKeyboard.getFlags();
                    if (currencyButtons.contains(message.getText())) {
                        defineCurrencyType(message.getText(), flags.get(message.getText()), sendMessage);
                    }
                    break;
                case WEATHER:
                    String city = message.getText();
                    sendMessage.setText(WhetherHelper.getWeather(city).toString());
                    sendMessage.setReplyMarkup(getCityKeyboard());
                    break;
            }
        });
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
