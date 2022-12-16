package com.example.cbu.bot;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@FeignClient(value = "subscription-feign", url = "https://api.telegram.org/bot5774286154:AAFoBmm5Y7pazz9DtrG8V2nVERB6d2bFiTs")
public interface SubscriptionFeign {
    @PostMapping("/sendMessage")
    Message sendMessage(@RequestBody SendMessage sendMessage);
}
