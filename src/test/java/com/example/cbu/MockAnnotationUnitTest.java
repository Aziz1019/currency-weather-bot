package com.example.cbu;

import com.example.cbu.bot.command.Command;
import com.example.cbu.bot.command.CommandContainer;
import com.example.cbu.bot.command.impl.WeatherCommand;
import com.example.cbu.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class MockAnnotationUnitTest {
    
    @Mock
    private WeatherCommand weatherCommand;


    @Test
    public void givenCountMethodMocked_WhenCountInvoked_ThenMockValueReturned() {

//        Mockito.when(weatherCommand.execute(ne).thenReturn());
//
//        Assert.assertEquals(123L, userCount);
//        Mockito.verify(weatherCommand).count();
    }
}