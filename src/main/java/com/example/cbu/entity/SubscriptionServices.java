package com.example.cbu.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "subscription_service_2")
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionServices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chatId;
    @ManyToMany
    @JoinTable(
            name = "user_subscription_service",
            joinColumns = @JoinColumn(name = "sub_chat_id", referencedColumnName = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "user_chat_id", referencedColumnName = "chat_id")
    )
    Set<Sample> samples;
    private String serviceName;
    private String serviceParam;
    private String serviceTime;
    private Boolean serviceSubscription = false;
}
