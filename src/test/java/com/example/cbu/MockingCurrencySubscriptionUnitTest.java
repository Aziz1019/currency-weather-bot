package com.example.cbu;

import com.example.cbu.bot.command.impl.SwitchStatesCommand;
import com.example.cbu.entity.real.UserSubscription;
import com.example.cbu.service.UserSubscriptionService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class MockingCurrencySubscriptionUnitTest {
    @InjectMocks
    private SwitchStatesCommand switchStatesCommand;
    @Mock
    private UserSubscriptionService service;
    @Mock
    private Environment environment;

    @Test
    public void checkCurrencySubscription(){
        //given
        Message message = new Message();
        SendMessage sendMessage = new SendMessage();
        UserSubscription userSubscription = new UserSubscription();
        userSubscription.setUserId(1L);
        userSubscription.setCurrencyCode("USD");
        userSubscription.setCurrencySubscription(false);
        Mockito.when(service.save(userSubscription)).thenReturn(true);

        Mockito.when(switchStatesCommand.getSelectedTimeForTest(environment)).thenReturn("Sizning tanlangan vaqtingiz: ");
        message.setText("1:00");
        //when
        switchStatesCommand.executeCurrencySubscription(sendMessage,message,Optional.of(userSubscription), environment);
        String actual = sendMessage.getText();
        String expected = "Sizning tanlangan vaqtingiz: 1:00";
        //then
        Assert.assertTrue(userSubscription.getCurrencySubscription());
        Assert.assertEquals(expected, actual);

        Mockito.verify(service).save(userSubscription);
    }

}
