package app.service;

import app.model.DayForecast;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import app.net.RetrofitClient;
import app.net.WeatherApi;
import retrofit2.Response;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Service that interacts with WeatherAPI
 */
public class WeatherServiceImpl implements WeatherService {
    private final WeatherApi api;
    private final String apiKey;
    public WeatherServiceImpl(String apiKey) {
        // Creating of a Retrofit client which creates endpoints written in given interface.
        this.api = RetrofitClient.getClient().create(WeatherApi.class);
        this.apiKey = apiKey;
    }
    @Override
    public DayForecast getNextDayForecast(String city) throws IOException {
        Response<JsonObject> response = api.getForecast(apiKey, city, 2).execute();
        if (!response.isSuccessful() || response.body() == null) {
            throw  new IOException("Failed to fetch forecast for " + city);
        }

        LocalDate tomorrow = LocalDate.now().plusDays(1);

        JsonArray forecastDays = response.body()
                .getAsJsonObject("forecast")
                .getAsJsonArray("forecastday");

        JsonObject dayForecast = forecastDays
                .asList()
                .stream()
                .map(JsonElement::getAsJsonObject)
                .filter(e -> e.get("date").getAsString().equals(tomorrow.toString()))
                .findFirst()
                .orElseThrow(() -> new IOException("No data found for tomorrow"));

        JsonObject day = dayForecast.getAsJsonObject("day");
        JsonArray hourly = dayForecast.getAsJsonArray("hour");
        JsonObject firstHour = hourly.get(0).getAsJsonObject();

        return new DayForecast(
                day.get("mintemp_c").getAsDouble(),
                day.get("maxtemp_c").getAsDouble(),
                day.get("avghumidity").getAsInt(),
                firstHour.get("wind_kph").getAsDouble(),
                firstHour.get("wind_dir").getAsString()
        );
    }
}
