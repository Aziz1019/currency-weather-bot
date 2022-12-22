package com.example.cbu.bot.scheduler.currencyScheduler.mapper;

import com.example.cbu.entity.real.UserSubscription;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrencySubscriptionMapper implements RowMapper<UserSubscription> {
    public static final String ID = "user_id";
    public static final String CURRENCY_CODE = "currency_code";
    public static final String CURRENCY_TIME = "currency_time";
    public static final String FIRST_NAME = "first_name";

    @Override
    public UserSubscription mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserSubscription responseDTO = new UserSubscription();
        responseDTO.setUserId(rs.getLong(ID));
        responseDTO.setCurrencyTime(rs.getString(CURRENCY_TIME));
        responseDTO.setFirstName(rs.getString(FIRST_NAME));
        responseDTO.setCurrencyCode(rs.getString(CURRENCY_CODE));
        return responseDTO;
    }
}
