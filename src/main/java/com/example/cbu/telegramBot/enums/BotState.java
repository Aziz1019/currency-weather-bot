package com.example.cbu.telegramBot.enums;

public enum BotState {
    START("start"),
    STEP_1("Currency"),
    STEP_2("Entering Code"),
    STEP_3("Currency Subscription"),

    STEP_4("Weather"),
    STEP_5("Entering City"),
    STEP_6("Weather Subscription"),
    STEP_7("Return to main menu");


    private final String state;

    BotState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return this.state;
    }
}