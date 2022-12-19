//package com.example.cbu.bot.command.impl.subscription;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.SchedulingConfigurer;
//import org.springframework.scheduling.config.CronTask;
//import org.springframework.scheduling.config.ScheduledTaskRegistrar;
//import org.springframework.scheduling.support.CronTrigger;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//
//
//@Component
//@EnableScheduling
//public class CronService implements SchedulingConfigurer {
//
//    public void createScheduler(Runnable runnable, String expression) {
//        CronTask cronTask = createCronTask(runnable, "*/" + expression + " * * * * *");
//        ScheduledTaskRegistrar taskRegistrar = new ScheduledTaskRegistrar();
//        taskRegistrar.addCronTask(cronTask);
//        configureTasks(taskRegistrar);
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
