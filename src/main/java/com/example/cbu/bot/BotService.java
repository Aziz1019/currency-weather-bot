package com.example.cbu.bot;
import com.example.cbu.bot.command.impl.*;
import com.example.cbu.utils.keyboards.MenuKeyboard;
import com.example.cbu.service.UserService;
import com.example.cbu.service.UserSubscriptionService;
import com.example.cbu.helper.CurrencyHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import javax.annotation.PostConstruct;
@Service
public class BotService extends TelegramLongPollingBot {
    private static final Logger logger = LoggerFactory.getLogger(BotService.class);
    private final UserService userService;
    private final UserSubscriptionService subscriptionService;
    private final CurrencyHelper currencyHelper;

    @Value("${bot.token}")
    private String token;
    @Value("${bot.username}")
    private String username;

    public BotService(UserService userService, UserSubscriptionService subscriptionService, CurrencyHelper currencyHelper) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
        this.currencyHelper = currencyHelper;
    }
    @Override
    public String getBotToken() {
        return token;
    }
    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleMessage(update.getMessage());
        }
    }
    private void handleMessage(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        // Start Command
        if(message.getText().equals("/start")){
            new StartCommand(userService).execute(message, sendMessage);
        }
        // Weather Command
        else if (message.getText().equals(MenuKeyboard.HAVO_SA)) {
                new WeatherCommand(userService).execute(message, sendMessage);
        }
        // Currency Command
        else if (message.getText().equals(MenuKeyboard.KURS_SA)) {
            new CurrencyCommand(userService).execute(message, sendMessage);
        }
        // MainMenu Command
        else if (message.getText().equals(MenuKeyboard.BOSH_SA)) {
            new MainMenuCommand(userService).execute(message, sendMessage);
        }
        // Subscription Command
        else if (message.getText().equals(MenuKeyboard.OBUNA_SA)) {
            new SubscriptionCommand(userService, subscriptionService).execute(message, sendMessage);
        }
        // Switch Command
        else if (message.hasText()) {
            new SwitchStateCommand(userService, currencyHelper).execute(message, sendMessage);
        }
        try {
            execute(sendMessage);
            logger.info("Sent message \"{}\" to {}", "text", "chatId");
        } catch (
                TelegramApiException e) {
            logger.error("Failed to send message \"{}\" to {} due to error: {}", "text", "text", e.getMessage());
        }
    }
    @PostConstruct
    public void start() {
        logger.info("username: {}, token: {}", username, token);
    }
}
