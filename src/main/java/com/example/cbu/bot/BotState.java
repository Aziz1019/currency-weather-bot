package com.example.cbu.bot;

public enum BotState {
    START("start"),
    CURRENCY("Currency"),
    CURRENCY_SUBSCRIPTION("Currency Subscription"),
    WEATHER("Weather"),
    WEATHER_SUBSCRIPTION("Weather Subscription"),
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