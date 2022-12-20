package com.example.cbu.bot.command.impl;
import com.example.cbu.bot.BotState;
import com.example.cbu.bot.command.Command;
import com.example.cbu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import static com.example.cbu.helper.KeyBoardHelper.getCityKeyboard;

@Component
public class WeatherCommand implements Command {
    private final UserService userService;
    public WeatherCommand(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private Environment env;
    public String getChooseCity(){
        return env.getProperty("messages.main.choose-city");
    }
    @Override
    public void execute(Message message, SendMessage sendMessage) {
        sendMessage.setChatId(message.getChatId().toString());
        userService.findById(message.getFrom().getId()).ifPresent(user -> {
            user.setLastBotState(BotState.WEATHER);
            userService.save(user);
        });
        sendMessage.setText(getChooseCity());
        sendMessage.setReplyMarkup(getCityKeyboard());
    }
    @Override
    public String getCommandName() {
        return "weather";
    }
}
