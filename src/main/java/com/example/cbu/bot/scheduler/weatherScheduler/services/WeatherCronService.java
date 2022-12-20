//package com.example.cbu.bot.scheduler.weatherScheduler.services;
//
//import com.example.cbu.bot.SubscriptionFeign;
//import com.example.cbu.entity.UserSubscription;
//import com.example.cbu.helper.WheatherHelper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.SchedulingConfigurer;
//import org.springframework.scheduling.config.CronTask;
//import org.springframework.scheduling.config.ScheduledTaskRegistrar;
//import org.springframework.scheduling.support.CronTrigger;
//import org.springframework.stereotype.Service;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import java.time.LocalDateTime;
//
//
//@Service
//@Slf4j
//public class WeatherCronService implements SchedulingConfigurer {
//    @Autowired
//    SubscriptionFeign subscriptionFeign;
//
//    public ScheduledTaskRegistrar createScheduler(UserSubscription userSubscription) {
//        SendMessage sendMessage = new SendMessage();
//        Runnable runnable = () -> {
//            log.info("Start process at : {}", LocalDateTime.now());
//            sendMessage.setChatId(userSubscription.getUserId().toString());
//            sendMessage.setText(WheatherHelper.getWeather(userSubscription.getCityName()).toString());
//            subscriptionFeign.sendMessage(sendMessage);
//        };
//        CronTask cronTask = createCronTask(runnable, userSubscription.getWeatherTime());
//        ScheduledTaskRegistrar taskRegistrar = new ScheduledTaskRegistrar();
//        taskRegistrar.addCronTask(cronTask);
//        configureTasks(taskRegistrar);
//        return taskRegistrar;
//    }
//
//    @Override
//    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
//        taskRegistrar.afterPropertiesSet();
//    }
//
//    public CronTask createCronTask(Runnable action, String expression) {
//        return new CronTask(action, new CronTrigger(expression));
//    }
//}
