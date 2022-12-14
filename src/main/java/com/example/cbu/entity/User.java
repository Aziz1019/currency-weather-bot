package com.example.cbu.entity;

import com.example.cbu.bot.BotState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "user_entity")
@AllArgsConstructor
@NoArgsConstructor
public class User{
    @Id
    private Long userId;
    private String firstName;
    private String lastName;
    private String username;
    private BotState lastBotState;

}
