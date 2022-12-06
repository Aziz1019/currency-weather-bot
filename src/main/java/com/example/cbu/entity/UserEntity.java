package com.example.cbu.entity;

import com.example.cbu.telegramBot.enums.BotState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    private Long userId;
    private String firstName;
    private String lastName;
    private String username;
    private BotState lastBotState;

}
