package com.example.cbu.bot;
import com.example.cbu.bot.command.Command;
import com.example.cbu.bot.command.CommandContainer;
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
import java.util.HashMap;
import java.util.List;

@Service
public class BotService extends TelegramLongPollingBot {
    private static final Logger logger = LoggerFactory.getLogger(BotService.class);
    private final UserService userService;
    private final UserSubscriptionService subscriptionService;
    private final CurrencyHelper currencyHelper;

    private final List<Command> commandLists;

    @Value("${bot.token}")
    private String token;
    @Value("${bot.username}")
    private String username;

    public BotService(UserService userService, UserSubscriptionService subscriptionService, CurrencyHelper currencyHelper, List<Command> commandLists) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
        this.currencyHelper = currencyHelper;
        this.commandLists = commandLists;
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
        CommandContainer commandContainer = new CommandContainer(commandLists);
        HashMap<String, Command> commands = commandContainer.getCommands();
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleMessage(update.getMessage(), commands, commandContainer);
        }
    }
    private void handleMessage(Message message,HashMap<String, Command> commands, CommandContainer commandContainer ) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        commandContainer.defineCommandButton(message.getText(), commands, message, sendMessage);

        try {
            execute(sendMessage);
            logger.info("Sent message \"{}\" to {}", message.getText(), message.getChatId());
        } catch (
                TelegramApiException e) {
            logger.error("Failed to send message \"{}\" to {} due to error: {}", message.getText(), "text", e.getMessage());
        }
    }

    @PostConstruct
    public void start() {
        logger.info("username: {}, token: {}", username, token);
    }
}
