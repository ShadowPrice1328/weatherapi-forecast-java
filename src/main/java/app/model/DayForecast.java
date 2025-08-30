package app.model;

/**
 * Record for storing daily forecast data
 */
public record DayForecast(double minTemp, double maxTemp, int humidity, double windSpeed, String windDir) { }
