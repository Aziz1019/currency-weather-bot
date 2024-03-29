package com.example.cbu.repository;
import com.example.cbu.entity.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {

   List<UserSubscription> findAllByCurrencySubscriptionIsTrue();

   List<UserSubscription> findAllByWeatherSubscriptionIsTrue();

}
