package com.example.cbu;

import com.example.cbu.bot.command.impl.SwitchStatesCommand;
import com.example.cbu.helper.WeatherHelper;
import com.example.cbu.model.WeatherDTO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class MockingWeatherUnitTest {
    @InjectMocks
    private SwitchStatesCommand switchStatesCommand;

    @Mock
    private WeatherHelper weatherHelper;

    @Test
    public void checkSwitchStatesExecuteWeatherCommandTest() {
        // given
        Message message = new Message();
        SendMessage sendMessage = new SendMessage();

        WeatherDTO dto = new WeatherDTO("Namangan", "-4", "-10", "200", "21.12.2022");
        String city = dto.getCity();
        // when
        Mockito.when(weatherHelper.getWeather(city)).thenReturn(dto);
        message.setText(city);
        switchStatesCommand.executeWeatherCommand(message, sendMessage);

        String sentWeather = sendMessage.getText();
        String actualWeather = dto.toString();
        // then
        Assert.assertEquals(sentWeather, actualWeather);

        Mockito.verify(weatherHelper).getWeather(city);
    }
}
