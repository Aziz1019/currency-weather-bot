package com.example.cbu.utils.keyboards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuKeyboard {
    public static HashMap<String, String> getMainButtons() {
        HashMap<String, String> buttons = new HashMap<>();
        buttons.put(MenuKeyboard.BOSH_SA, "menu");
        buttons.put(MenuKeyboard.HAVO_SA, "weather");
        buttons.put(MenuKeyboard.KURS_SA, "currency");
        buttons.put(MenuKeyboard.OBUNA_SA, "subscribe");
        buttons.put(MenuKeyboard.START, "start");
        return buttons;
    }
    public static final String START = "/start";
    public static final String BOSH_SA = "🏠 Bosh sahifa";
    public static final String KURS_SA = "💰 Kurslar";
    public static final String HAVO_SA = "☁ Ob-havo";
    public static final String OBUNA_SA = "📚 Obuna bo'lish";

}
