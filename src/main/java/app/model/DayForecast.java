package app.model;

/**
 * POJO for storing daily forecast
 */
public record DayForecast(double minTemp, double maxTemp, int humidity, double windSpeed, String windDir) { }
