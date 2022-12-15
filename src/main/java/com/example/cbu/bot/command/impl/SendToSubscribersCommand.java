package com.example.cbu.bot.command.impl;
import com.example.cbu.bot.command.Command;
import com.example.cbu.entity.UserSubscription;
import com.example.cbu.helper.CurrencyHelper;
import com.example.cbu.service.UserService;
import com.example.cbu.service.UserSubscriptionService;
import com.example.cbu.utils.keyboards.CurrencyKeyboard;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.util.HashMap;
import java.util.List;

@Component
public class SendToSubscribersCommand implements Command {
    private final UserService userService;
    private final UserSubscriptionService subscriptionService;

    private final CurrencyHelper currencyHelper;
    public SendToSubscribersCommand(UserService userService, UserSubscriptionService subscriptionService, CurrencyHelper currencyHelper) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
        this.currencyHelper = currencyHelper;
    }
    @Override
    public void execute(Message message, SendMessage sendMessage) {
        List<UserSubscription> allByCurrencySubscriptionIsTrue = subscriptionService.findAllByCurrencySubscriptionIsTrue();
        for (UserSubscription userSubscription : allByCurrencySubscriptionIsTrue) {
            sendMessage.setChatId(userSubscription.getUserId().toString());
            String currencyCode = userSubscription.getCurrencyCode();
            List<String> currencyButtons = CurrencyKeyboard.getCurrencyButtons();
            HashMap<String, String> flags = CurrencyKeyboard.getFlags();
            if (currencyButtons.contains(currencyCode)) {
                defineCurrencyType(currencyCode, flags.get(currencyCode), sendMessage);
            }
        }
    }
    @Override
    public String getCommandName() {
        return "notification-sender";
    }

    public void defineCurrencyType(String currencyType, String flag, SendMessage sendMessage) {
        currencyHelper.getCurrencies().forEach(currencyDTO -> {
            if (currencyDTO.getCcy().equals(currencyType)) {
                String overall = currencyDTO.getCcy() + " " + flag + "\n";
                sendMessage.setText(overall + currencyDTO);
            }
        });
    }
}
