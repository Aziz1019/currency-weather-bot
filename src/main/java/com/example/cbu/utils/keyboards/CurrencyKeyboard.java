package com.example.cbu.utils.keyboards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CurrencyKeyboard {
    public static List<String> getCurrencyButtons() {
        List<String> currencyButtons = new ArrayList<>();
        currencyButtons.add(CurrencyKeyboard.EUR);
        currencyButtons.add(CurrencyKeyboard.GBP);
        currencyButtons.add(CurrencyKeyboard.RUB);
        currencyButtons.add(CurrencyKeyboard.USD);
        return currencyButtons;
    }

    public static HashMap<String, String> getFlags() {
        HashMap<String, String> flags = new HashMap<>();
        flags.put(CurrencyKeyboard.EUR, "🇪🇺");
        flags.put(CurrencyKeyboard.GBP, "🇬🇧");
        flags.put(CurrencyKeyboard.RUB, "🇷🇺");
        flags.put(CurrencyKeyboard.USD, "🇺🇸");
        return flags;
    }

    public static final String USD = "USD";
    public static final String EUR = "EUR";
    public static final String RUB = "RUB";
    public static final String GBP = "GBP";

}
