package com.example.cbu.entity;

import com.example.cbu.bot.BotState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "user_entity_2")
@AllArgsConstructor
@NoArgsConstructor
public class Sample {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chatId;
    @ManyToMany(mappedBy = "samples")
    private List<SubscriptionServices> subscriptionServices;
    private String firstName;
    private String username;
    private BotState lastBotState;

}
