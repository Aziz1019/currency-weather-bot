package com.example.cbu.telegramBot.enums;

public enum BotState {
    START("start"),
    CURRENCY("Currency"),
    CURRENCY_SUBSCRIPTION("Currency CurrencySubscription"),
    WEATHER("Weather"),
    WEATHER_SUBSCRIPTION("Weather CurrencySubscription"),
    MAIN_MENU("Return to main menu");


    private final String state;

    BotState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return this.state;
    }
}