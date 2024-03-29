package com.example.cbu.telegramBot;

import com.example.cbu.entity.UserEntity;
import com.example.cbu.entity.UserSubscription;
import com.example.cbu.repository.UserSubscriptionRepository;
import com.example.cbu.service.UserService;
import com.example.cbu.service.UserSubscriptionService;
import com.example.cbu.telegramBot.enums.BotState;
import com.example.cbu.util.CurrencyGetter;
import com.example.cbu.util.WeatherGetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This example bot is an echo bot that just repeats the messages sent to him
 */
@Service
class ServerBot extends TelegramLongPollingBot {
    private static final Logger logger = LoggerFactory.getLogger(ServerBot.class);
    private final UserService userService;
    private final UserSubscriptionService subscriptionService;

    private final String token;
    private final String username;

    ServerBot(UserService userService, UserSubscriptionService subscriptionService, @Value("${bot.token}") String token, @Value("${bot.username}") String username) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
        this.token = token;
        this.username = username;
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
        if (update.hasMessage()) {
            handleMessage(update.getMessage());
        }
    }

    private void handleMessage(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        if (message.getText().equals("/start")) {
            UserEntity user = new UserEntity(
                    message.getFrom().getId(),
                    message.getFrom().getFirstName(),
                    message.getFrom().getLastName(),
                    message.getFrom().getUserName(),
                    BotState.START
            );
            userService.save(user);
            sendMessage.setText("Assalomu alaykum " + message.getFrom().getFirstName() + "!");
            sendMessage.setReplyMarkup(getKeyboard());
        }

        if (message.getText().equals(Keyboards.HAVO_SA)) {
            userService.findById(message.getFrom().getId()).ifPresent(user -> {
                user.setLastBotState(BotState.WEATHER);
                userService.save(user);
            });
            sendMessage.setText("Shahar nomini kiriting");
            sendMessage.setReplyMarkup(getObuna());
        }
        else if (message.getText().equals(Keyboards.KURS_SA)) {
            userService.findById(message.getFrom().getId()).ifPresent(user -> {
                user.setLastBotState(BotState.CURRENCY);
                userService.save(user);
            });
            sendMessage.setText("Valyutani tanlang");
            sendMessage.setReplyMarkup(getCurrencyKeyBoard());
        }
        else if (message.getText().equals(Keyboards.BOSH_SA)) {
            userService.findById(message.getFrom().getId()).ifPresent(user -> {
                user.setLastBotState(BotState.MAIN_MENU);
                userService.save(user);
            });
            sendMessage.setText("🏠 Bosh sahifa");
            sendMessage.setReplyMarkup(getKeyboard());
        }
        else if (message.getText().equals(Keyboards.OBUNA_SA)) {

            Optional<UserEntity> userEntity = userService.findById(message.getFrom().getId());

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
        else if (message.hasText()) {
            Optional<UserEntity> userEntity = userService.findById(message.getFrom().getId());
            if (userEntity.isPresent()) {
                switch (userEntity.get().getLastBotState()) {
                    case CURRENCY:
                        if (message.hasText()) {
                            if (message.getText().equals(Keyboards.USD)) {
                                CurrencyGetter.getCurrencies().forEach(currencyDTO -> {
                                    if (currencyDTO.getCcy().equals(Keyboards.USD)) {
                                        String overall = currencyDTO.getCcy() + " \uD83C\uDDFA\uD83C\uDDF8" + "\n";
                                        sendMessage.setText(overall + currencyDTO);
                                    }
                                });
                                sendMessage.setReplyMarkup(getCurrencyKeyBoard());
                            }
                            else if (message.getText().equals(Keyboards.EUR)) {
                                CurrencyGetter.getCurrencies().forEach(currencyDTO -> {
                                    if (currencyDTO.getCcy().equals(Keyboards.EUR)) {
                                        String overall = currencyDTO.getCcy() + " \uD83C\uDDEA\uD83C\uDDFA" + "\n";
                                        sendMessage.setText(overall + currencyDTO);
                                    }
                                });
                                sendMessage.setReplyMarkup(getCurrencyKeyBoard());
                            }
                            else if (message.getText().equals(Keyboards.GBP)) {
                                CurrencyGetter.getCurrencies().forEach(currencyDTO -> {
                                    if (currencyDTO.getCcy().equals(Keyboards.GBP)) {
                                        String overall = currencyDTO.getCcy() + " \uD83C\uDDEC\uD83C\uDDE7" + "\n";
                                        sendMessage.setText(overall + currencyDTO);
                                    }
                                });
                                sendMessage.setReplyMarkup(getCurrencyKeyBoard());
                            }
                            else if (message.getText().equals(Keyboards.RUB)) {
                                CurrencyGetter.getCurrencies().forEach(currencyDTO -> {
                                    if (currencyDTO.getCcy().equals(Keyboards.RUB)) {
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
                            sendMessage.setText(WeatherGetter.getWeather(city).toString());
                            sendMessage.setReplyMarkup(getObuna());
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


    public ReplyKeyboardMarkup getObuna() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>(4);
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(Keyboards.OBUNA_SA);
        keyboardRow.add(Keyboards.BOSH_SA);
        keyboardRows.add(keyboardRow);

        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }

    @PostConstruct
    public void start() {
        logger.info("username: {}, token: {}", username, token);
    }

    public ReplyKeyboardMarkup getKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>(4);
        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRow.add(Keyboards.KURS_SA);
        keyboardRow.add(Keyboards.HAVO_SA);

        keyboardRows.add(keyboardRow);

        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup getMainMenuKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>(2);
        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRow.add(Keyboards.BOSH_SA);
        keyboardRows.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup getCurrencyKeyBoard(){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>(12);

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(Keyboards.USD);
        keyboardRow.add(Keyboards.EUR);


        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(Keyboards.GBP);
        keyboardRow1.add(Keyboards.RUB);

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(Keyboards.OBUNA_SA);
        keyboardRow2.add(Keyboards.BOSH_SA);


        keyboardRows.add(keyboardRow);
        keyboardRows.add(keyboardRow1);
        keyboardRows.add(keyboardRow2);

        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }

}


//    Optional<UserEntity> user = userService.findById(message.getFrom().getId());
//            if (user.isPresent()) {
//        if (user.get().getLastBotState() == BotState.STEP_4) {
//            user.get().setLastBotState(BotState.STEP_6);
//            userService.save(user.get());
//        } else if (user.get().getLastBotState() == BotState.STEP_1) {
//            user.get().setLastBotState(BotState.STEP_3);
//            userService.save(user.get());
//        }
//    }
