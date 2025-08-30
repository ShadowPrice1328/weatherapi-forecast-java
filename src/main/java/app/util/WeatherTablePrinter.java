package app.util;

import app.model.DayForecast;

import java.util.Map;

public class WeatherTablePrinter {
    private static final String FORMAT = "| %-12s | %-13s | %-13s | %-12s | %-17s | %-8s |%n";
    private static final String BORDER = "+--------------+---------------+---------------+--------------+-------------------+----------+%n";

    public static void print(Map<String, DayForecast> forecastData) {
        System.out.format(BORDER);
        System.out.format(FORMAT, "City", "Min Temp (°C)", "Max Temp (°C)", "Humidity (%)", "Wind Speed (kph)", "Wind Dir");
        System.out.format(BORDER);

        for (Map.Entry<String, DayForecast> entry : forecastData.entrySet()) {
            DayForecast d = entry.getValue();
            System.out.format(FORMAT,
                    entry.getKey(),
                    String.format("%.1f", d.minTemp()),
                    String.format("%.1f", d.maxTemp()),
                    d.humidity(),
                    String.format("%.1f", d.windSpeed()),
                    d.windDir()
            );
        }
    }
}
