package com.example.cbu.repository;
import com.example.cbu.entity.real.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {

   List<UserSubscription> findAllByCurrencySubscriptionIsTrue();

   List<UserSubscription> findAllByWeatherSubscriptionIsTrue();

   Optional<UserSubscription> findByUserIdAndAndCurrencyCode(Long userId, String currencyCode);

}
