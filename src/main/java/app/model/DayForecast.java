package app.model;

/**
 * POJO for storing daily forecast
 */
public class DayForecast {
    private final double minTemp;
    private final double maxTemp;
    private final int humidity;
    private final double windSpeed;
    private final String windDir;

    public DayForecast(double minTemp, double maxTemp, int humidity, double windSpeed, String windDir) {
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.windDir = windDir;
    }

    public double getMinTemp() { return minTemp; }
    public double getMaxTemp() { return maxTemp; }
    public int getHumidity() { return humidity; }
    public double getWindSpeed() { return windSpeed; }
    public String getWindDir() { return windDir; }
}
