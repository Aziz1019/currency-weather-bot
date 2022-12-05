package com.example.cbu.telegramBot;

import com.example.cbu.util.CurrencyGetter;
import com.example.cbu.util.WeatherGetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * This example bot is an echo bot that just repeats the messages sent to him
 *
 */
@Component
class ExampleBot extends TelegramLongPollingBot {
	
	private static final Logger logger = LoggerFactory.getLogger(ExampleBot.class);
	
	private final String token;
	private final String username;

	ExampleBot(@Value("${bot.token}") String token, @Value("${bot.username}") String username) {
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
			Message message = update.getMessage();
			SendMessage sendMessage = new SendMessage();

			sendMessage.setChatId(message.getChatId().toString());
			if (update.getMessage().getText().equals("/start")) {
				sendMessage.setText("Ассалому Алeйкум " + message.getFrom().getFirstName() + "!");
				sendMessage.setReplyMarkup(getKeyboard());
			}

			if (update.getMessage().getText().equals(Keyboards.OB_HAVO)) {
				sendMessage.setText(WeatherGetter.getWeather("Tashkent").toString());
				sendMessage.setReplyMarkup(getObuna());
			}

			if (update.getMessage().getText().equals(Keyboards.KURS)) {
				sendMessage.setText(CurrencyGetter.getCurrencies().get(0).toString());
				sendMessage.setReplyMarkup(getObuna());
			}


//			if (update.getMessage().getText().equals(Keyboards.OBUNA)) {
//				sendMessage.setText("Obuna bo'lish");
//				sendMessage.setReplyMarkup(getObuna());
//			}


//			SendMessage response = new SendMessage();
//			Long chatId = message.getChatId();
//			String city = message.getText();
//			response.setChatId(String.valueOf(chatId));
//			String text = WeatherGetter.getWeather(city).toString();
//			response.setText(text);
			try {
				execute(sendMessage);
				logger.info("Sent message \"{}\" to {}", "text", "chatId");
			} catch (TelegramApiException e) {
				logger.error("Failed to send message \"{}\" to {} due to error: {}", "text", "text", e.getMessage());
			}
		}
	}

	public ReplyKeyboardMarkup getObuna() {
		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		List<KeyboardRow> keyboardRows = new ArrayList<>(2);
		KeyboardRow keyboardRow = new KeyboardRow();
		keyboardRow.add(Keyboards.OBUNA);
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

		keyboardRow.add(Keyboards.KURS);
		keyboardRow.add(Keyboards.OB_HAVO);

		keyboardRows.add(keyboardRow);

		replyKeyboardMarkup.setKeyboard(keyboardRows);
		replyKeyboardMarkup.setResizeKeyboard(true);
		return replyKeyboardMarkup;
	}

}
