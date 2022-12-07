package com.example.cbu.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WeatherDTO {
    private Integer id;
    private String city;
    private String temperature;
    private String humidity;
    private String windSpeed;
    private String date;

    public WeatherDTO(String city, String temperature, String humidity, String windSpeed, String date) {
        this.city = city;
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.date = date;
    }

    @Override
    public String toString() {
        return  "Shahar: " + city + "\n" +
                "Harorat: "+ temperature + "\n" +
                "Namlik: " + humidity + "\n" +
                "Shamol: " + windSpeed + "\n" +
                "Sana: " + date + "\n";
    }
}
