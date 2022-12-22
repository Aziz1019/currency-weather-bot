package com.example.cbu.helper;

import com.example.cbu.model.WeatherDTO;
import com.github.prominence.openweathermap.api.OpenWeatherMapClient;
import com.github.prominence.openweathermap.api.enums.UnitSystem;
import com.github.prominence.openweathermap.api.model.weather.Weather;
import org.springframework.stereotype.Component;

@Component
public class WeatherHelper {
    public WeatherDTO getWeather(String city) {

        OpenWeatherMapClient openWeatherClient = new OpenWeatherMapClient("0a9cf92a59f4148067c0a2b4cba62047");

         Weather weather = openWeatherClient
                .currentWeather()
                .single()
                .byCityName(city)
                .unitSystem(UnitSystem.METRIC)
                .retrieve()
                .asJava();

        String location = weather.getLocation().getName();
        String temperature = weather.getTemperature().getMaxTemperature() + " °C";
        String humidity = weather.getHumidity().getValue() + weather.getHumidity().getUnit();
        String windSpeed = weather.getWind().getSpeed() + " " + weather.getWind().getUnit();
        String date = weather.getCalculationTime().toLocalDate().toString();

        return new WeatherDTO(
                location,
                temperature,
                humidity,
                windSpeed,
                date
        );
    }
}
