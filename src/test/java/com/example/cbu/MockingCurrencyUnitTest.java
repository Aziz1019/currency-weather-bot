package com.example.cbu;

import com.example.cbu.bot.command.impl.SwitchStatesCommand;
import com.example.cbu.helper.CurrencyHelper;
import com.example.cbu.model.CurrencyDTO;
import com.example.cbu.utils.keyboards.CurrencyKeyboard;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@RunWith(MockitoJUnitRunner.class)
public class MockingCurrencyUnitTest {
    @InjectMocks
    private SwitchStatesCommand switchStatesCommand;

    @Mock
    private CurrencyHelper currencyHelper;


    @Test
    public void checkValueOfCurrencyHelperTest() {
        SendMessage sendMessage = new SendMessage();
        List<CurrencyDTO> currencies2 = new ArrayList<>();
        currencies2.add(new CurrencyDTO("69", "840", "USD", "US Dollar", "1", "11277.83", "-8.35", "21.12.2022"));
        Mockito.when(currencyHelper.getCurrencies()).thenReturn(currencies2);
        String overall2 = null;
        String currencyType = currencies2.get(0).getCcy();
        List<String> currencyButtons = CurrencyKeyboard.getCurrencyButtons();
        HashMap<String, String> flags = CurrencyKeyboard.getFlags();
        String currencyFlag = flags.get(currencyType);
        if (currencyButtons.contains(currencyType)) {
            List<CurrencyDTO> currencies = currencyHelper.getCurrencies();
            for (CurrencyDTO currency : currencies) {
                if (currency.getCcy().equals(currencyType)) {
                    overall2 = currency.getCcy() + " " + currencyFlag + "\n";
                    sendMessage.setText(overall2 + currency);
                }
            }
        }
        Assert.assertEquals(sendMessage.getText(), overall2 + currencies2.get(0).toString());
        Mockito.verify(currencyHelper).getCurrencies();
    }

    @Test
    public void checkExecuteCurrencyCommandTest() {
        // given
        Message message = new Message();
        SendMessage sendMessage = new SendMessage();
        List<CurrencyDTO> usdCurrency = new ArrayList<>();
        usdCurrency.add(new CurrencyDTO("69", "840", "USD", "US Dollar", "1", "11277.83", "-8.35", "21.12.2022"));
        String usdCountryCode = usdCurrency.get(0).getCcy();
        // when
        Mockito.when(currencyHelper.getCurrencies()).thenReturn(usdCurrency);
        message.setText(usdCountryCode);
        switchStatesCommand.executeCurrencyCommand(message, sendMessage);
        String sentCurrency = sendMessage.getText().substring(sendMessage.getText().indexOf("\n") + 1);
        String actualCurrency = usdCurrency.get(0).toString();
        // then

        Assert.assertEquals(actualCurrency, sentCurrency);
        Assert.assertTrue(sendMessage.getText().contains(usdCountryCode));
        Assert.assertTrue(sendMessage.getText().contains(usdCurrency.get(0).getCcy()));


        Mockito.verify(currencyHelper).getCurrencies();
    }


}