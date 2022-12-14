package com.example.cbu.bot;
import com.example.cbu.bot.command.Command;
import com.example.cbu.bot.command.impl.StartCommand;
import com.example.cbu.utils.keyboards.CurrencyKeyboard;
import com.example.cbu.utils.keyboards.MenuKeyboard;
import com.example.cbu.entity.User;
import com.example.cbu.entity.UserSubscription;
import com.example.cbu.service.UserService;
import com.example.cbu.service.UserSubscriptionService;
import com.example.cbu.helper.CurrencyHelper;
import com.example.cbu.helper.WhetherHelper;
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
import java.util.Optional;

import static com.example.cbu.helper.KeyBoardHelper.*;

@Service
public class BotService extends TelegramLongPollingBot {
    public static String COMMAND_PREFIX = "/";
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
            Command startCommand = new StartCommand(userService);
            startCommand.execute(message, sendMessage);
        }

//        if (message.getText().equals("/start")) {
//            User user = new User(
//                    message.getFrom().getId(),
//                    message.getFrom().getFirstName(),
//                    message.getFrom().getLastName(),
//                    message.getFrom().getUserName(),
//                    BotState.START
//            );
//            userService.save(user);
//            sendMessage.setText("Assalomu alaykum " + message.getFrom().getFirstName() + "!");
//            sendMessage.setReplyMarkup(getKeyboard());
//        }


// Weather Command
        if (message.getText().equals(MenuKeyboard.HAVO_SA)) {
            userService.findById(message.getFrom().getId()).ifPresent(user -> {
                user.setLastBotState(BotState.WEATHER);
                userService.save(user);
            });
            sendMessage.setText("Shahar nomini kiriting yoki tanlang");
            sendMessage.setReplyMarkup(getCityKeyboard());
        }
// Currency Command
        else if (message.getText().equals(MenuKeyboard.KURS_SA)) {
            userService.findById(message.getFrom().getId()).ifPresent(user -> {
                user.setLastBotState(BotState.CURRENCY);
                userService.save(user);
            });
            sendMessage.setText("Valyutani tanlang");
            sendMessage.setReplyMarkup(getCurrencyKeyBoard());
        }
// Main Command
        else if (message.getText().equals(MenuKeyboard.BOSH_SA)) {
            userService.findById(message.getFrom().getId()).ifPresent(user -> {
                user.setLastBotState(BotState.MAIN_MENU);
                userService.save(user);
            });
            sendMessage.setText("🏠 Bosh sahifa");
            sendMessage.setReplyMarkup(getKeyboard());
        }
// Subscription Command
        else if (message.getText().equals(MenuKeyboard.OBUNA_SA)) {

            Optional<User> userEntity = userService.findById(message.getFrom().getId());

            if (userEntity.isPresent()) {
                Optional<UserSubscription> subscriptionId = subscriptionService.findById(userEntity.get().getUserId());
                if (userEntity.get().getLastBotState() == BotState.WEATHER) {
                    userEntity.get().setLastBotState(BotState.WEATHER_SUBSCRIPTION);
                    userService.save(userEntity.get());
                    if (subscriptionId.isPresent()) {
                        subscriptionId.get().setWeatherSubscription(true);
                        subscriptionService.save(subscriptionId.get());
                    }
                    else {
                        subscriptionService.save(new UserSubscription(
                                userEntity.get().getUserId(),
                                userEntity.get().getFirstName(),
                                userEntity.get().getLastName(),
                                userEntity.get().getUsername(),
                                true
                        ));
                    }
                }
                else if (userEntity.get().getLastBotState() == BotState.CURRENCY) {
                    userEntity.get().setLastBotState(BotState.CURRENCY_SUBSCRIPTION);
                    userService.save(userEntity.get());
                    if (subscriptionId.isPresent()) {
                        subscriptionId.get().setCurrencySubscription(true);
                        subscriptionService.save(subscriptionId.get());
                    }
                    else {
                        subscriptionService.save(new UserSubscription(
                                true,
                                userEntity.get().getUserId(),
                                userEntity.get().getFirstName(),
                                userEntity.get().getLastName(),
                                userEntity.get().getUsername()
                        ));
                    }
                }
            }
            sendMessage.setText("Siz muvaffaqiyatli Obuna bo'ldingiz!✅");
            sendMessage.setReplyMarkup(getMainMenuKeyboard());
        }
// Switch Command

        else if (message.hasText()) {
            Optional<User> userEntity = userService.findById(message.getFrom().getId());
            if (userEntity.isPresent()) {
                switch (userEntity.get().getLastBotState()) {
                    case CURRENCY:
                        if (message.hasText()) {
                            if (message.getText().equals(CurrencyKeyboard.USD)) {
                                currencyHelper.getCurrencies().forEach(currencyDTO -> {
                                    if (currencyDTO.getCcy().equals(CurrencyKeyboard.USD)) {
                                        String overall = currencyDTO.getCcy() + " \uD83C\uDDFA\uD83C\uDDF8" + "\n";
                                        sendMessage.setText(overall + currencyDTO);
                                    }
                                });
                                sendMessage.setReplyMarkup(getCurrencyKeyBoard());
                            }
                            else if (message.getText().equals(CurrencyKeyboard.EUR)) {
                                currencyHelper.getCurrencies().forEach(currencyDTO -> {
                                    if (currencyDTO.getCcy().equals(CurrencyKeyboard.EUR)) {
                                        String overall = currencyDTO.getCcy() + " \uD83C\uDDEA\uD83C\uDDFA" + "\n";
                                        sendMessage.setText(overall + currencyDTO);
                                    }
                                });
                                sendMessage.setReplyMarkup(getCurrencyKeyBoard());
                            }
                            else if (message.getText().equals(CurrencyKeyboard.GBP)) {
                                currencyHelper.getCurrencies().forEach(currencyDTO -> {
                                    if (currencyDTO.getCcy().equals(CurrencyKeyboard.GBP)) {
                                        String overall = currencyDTO.getCcy() + " \uD83C\uDDEC\uD83C\uDDE7" + "\n";
                                        sendMessage.setText(overall + currencyDTO);
                                    }
                                });
                                sendMessage.setReplyMarkup(getCurrencyKeyBoard());
                            }
                            else if (message.getText().equals(CurrencyKeyboard.RUB)) {
                                currencyHelper.getCurrencies().forEach(currencyDTO -> {
                                    if (currencyDTO.getCcy().equals(CurrencyKeyboard.RUB)) {
                                        String overall = currencyDTO.getCcy() + " \uD83C\uDDF7\uD83C\uDDFA" + "\n";
                                        sendMessage.setText(overall + currencyDTO);
                                    }
                                });
                                sendMessage.setReplyMarkup(getCurrencyKeyBoard());
                            }
                        }
                        break;
                    case WEATHER:
                        if(message.hasText()){
                            String city = message.getText();
                            sendMessage.setText(WhetherHelper.getWeather(city).toString());
                            sendMessage.setReplyMarkup(getCityKeyboard());
                        }
                        break;
                }
            }
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
