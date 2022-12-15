package com.example.cbu.entity;

import com.example.cbu.bot.BotState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BotState getLastBotState() {
        return lastBotState;
    }

    public void setLastBotState(BotState lastBotState) {
        this.lastBotState = lastBotState;
    }
}
