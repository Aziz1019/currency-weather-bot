package com.example.cbu.helper;

import com.example.cbu.utils.keyboards.CityKeyboard;
import com.example.cbu.utils.keyboards.CurrencyKeyboard;
import com.example.cbu.utils.keyboards.MenuKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class KeyBoardHelper {
    public static ReplyKeyboardMarkup getKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>(4);
        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRow.add(MenuKeyboard.KURS_SA);
        keyboardRow.add(MenuKeyboard.HAVO_SA);

        keyboardRows.add(keyboardRow);

        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup getMainMenuKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>(2);
        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRow.add(MenuKeyboard.BOSH_SA);
        keyboardRows.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup getCurrencyKeyBoard(){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>(12);

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(CurrencyKeyboard.USD);
        keyboardRow.add(CurrencyKeyboard.EUR);


        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(CurrencyKeyboard.GBP);
        keyboardRow1.add(CurrencyKeyboard.RUB);

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(MenuKeyboard.OBUNA_SA);
        keyboardRow2.add(MenuKeyboard.BOSH_SA);


        keyboardRows.add(keyboardRow);
        keyboardRows.add(keyboardRow1);
        keyboardRows.add(keyboardRow2);

        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup getCityKeyboard(){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> cityKeyboardRows = new ArrayList<>(14);

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(CityKeyboard.NAVOIY);
        keyboardRow1.add(CityKeyboard.ANDIJON);

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(CityKeyboard.BUKHARA);
        keyboardRow2.add(CityKeyboard.FARGONA);

        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(CityKeyboard.JIZZAX);
        keyboardRow3.add(CityKeyboard.NAMANGAN);

        KeyboardRow keyboardRow4 = new KeyboardRow();
        keyboardRow4.add(CityKeyboard.SAMARKAND);
        keyboardRow4.add(CityKeyboard.QARSHI);

        KeyboardRow keyboardRow5 = new KeyboardRow();
        keyboardRow5.add(CityKeyboard.QORAQALPOQ);
        keyboardRow5.add(CityKeyboard.QASHQADARYO);

        KeyboardRow keyboardRow6 = new KeyboardRow();
        keyboardRow6.add(CityKeyboard.TASHKENT);
        keyboardRow6.add(CityKeyboard.XIVA);

        KeyboardRow keyboardRow7 = new KeyboardRow();
        keyboardRow7.add(MenuKeyboard.OBUNA_SA);
        keyboardRow7.add(MenuKeyboard.BOSH_SA);


        cityKeyboardRows.add(keyboardRow1);
        cityKeyboardRows.add(keyboardRow2);
        cityKeyboardRows.add(keyboardRow3);
        cityKeyboardRows.add(keyboardRow4);
        cityKeyboardRows.add(keyboardRow5);
        cityKeyboardRows.add(keyboardRow6);
        cityKeyboardRows.add(keyboardRow7);

        replyKeyboardMarkup.setKeyboard(cityKeyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }

}
