package com.example.cbu.telegramBot;

import com.example.cbu.entity.UserEntity;
import com.example.cbu.model.CurrencyDTO;
import com.example.cbu.service.UserService;
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
class ExampleBot extends TelegramLongPollingBot {
    private static final Logger logger = LoggerFactory.getLogger(ExampleBot.class);
    private final UserService userService;
    private final String token;
    private final String username;


    ExampleBot(UserService userService, @Value("${bot.token}") String token, @Value("${bot.username}") String username) {
        this.userService = userService;
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
            sendMessage.setText("Ассалому Алeйкум " + message.getFrom().getFirstName() + "!");
            sendMessage.setReplyMarkup(getKeyboard());
        }
        else if (message.getText().equals(Keyboards.HAVO_SA)) {
            userService.findById(message.getFrom().getId()).ifPresent(user -> {
                user.setLastBotState(BotState.STEP_4);
                userService.save(user);
            });
            sendMessage.setText(WeatherGetter.getWeather("Tashkent").toString());
            sendMessage.setReplyMarkup(getObuna());
        }
        else if (message.getText().equals(Keyboards.KURS_SA)) {
            userService.findById(message.getFrom().getId()).ifPresent(user -> {
                user.setLastBotState(BotState.STEP_1);
                userService.save(user);
            });
            sendMessage.setText("Kurs kodini kiriting");
            sendMessage.setReplyMarkup(getObuna());
        }
        else if (message.getText().equals("840")) {
            List<CurrencyDTO> currencies = CurrencyGetter.getCurrencies();
            sendMessage.setText(currencies.get(0).getCcyNm_EN() + " " + currencies.get(0).getRate());
            sendMessage.setReplyMarkup(getObuna());
        }
        else if (message.getText().equals(Keyboards.BOSH_SA)) {
            userService.findById(message.getFrom().getId()).ifPresent(user -> {
                user.setLastBotState(BotState.STEP_7);
                userService.save(user);
            });
            sendMessage.setText("🏠 Bosh sahifa");
            sendMessage.setReplyMarkup(getKeyboard());
        }
        else if (message.getText().equals(Keyboards.OBUNA_SA)) {
            Optional<UserEntity> user = userService.findById(message.getFrom().getId());
            if (user.isPresent()) {
                if (user.get().getLastBotState() == BotState.STEP_4) {
                    user.get().setLastBotState(BotState.STEP_6);
                    userService.save(user.get());
                } else if (user.get().getLastBotState() == BotState.STEP_1) {
                    user.get().setLastBotState(BotState.STEP_3);
                    userService.save(user.get());
                }
            }
            sendMessage.setText("Siz muvaffaqiyatli Obuna bo'ldingiz!✅");
            sendMessage.setReplyMarkup(getKeyboard());
        }

//			SendMessage response = new SendMessage();
//			Long chatId = message.getChatId();
//			String city = message.getText();
//			response.setChatId(String.valueOf(chatId));
//			String text = WeatherGetter.getWeather(city).toString();
//			response.setText(text);
        try {
            execute(sendMessage);
            logger.info("Sent message \"{}\" to {}", "text", "chatId");
        } catch (
                TelegramApiException e) {
            logger.error("Failed to send message \"{}\" to {} due to error: {}", "text", "text", e.getMessage());
        }

    }

    private void handleCallback(CallbackQuery callbackQuery) {

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

}
