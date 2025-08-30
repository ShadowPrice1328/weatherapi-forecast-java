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

    /**
     * @param city Name of the city.
     * @return DayForecast record with parsed data.
     * @throws Exception Problem with parsing data.
     */
    @Override
    public DayForecast getNextDayForecast(String city) throws Exception {
        JsonObject tomorrowForecast = fetchTomorrowForecast(city);

        JsonObject day = tomorrowForecast.getAsJsonObject("day");
        JsonArray hourly = tomorrowForecast.getAsJsonArray("hour");
        JsonObject firstHour = hourly.get(0).getAsJsonObject();

        return new DayForecast(
                day.get("mintemp_c").getAsDouble(),
                day.get("maxtemp_c").getAsDouble(),
                day.get("avghumidity").getAsInt(),
                firstHour.get("wind_kph").getAsDouble(),
                firstHour.get("wind_dir").getAsString()
        );
    }

    /**
     * @param city Name of the city.
     * @return Fetched forecast data.
     * @throws Exception Problem fetching getting data.
     */
    private JsonObject fetchTomorrowForecast(String city) throws Exception {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Response<JsonObject> response = api.getForecast(apiKey, city, 2).execute();

        if (!response.isSuccessful() || response.body() == null) {
            throw new Exception("Failed to fetch forecast for " + city);
        }

        JsonArray forecastDays = response.body()
                .getAsJsonObject("forecast")
                .getAsJsonArray("forecastday");

        return forecastDays
                .asList()
                .stream()
                .map(JsonElement::getAsJsonObject)
                .filter(e -> e.get("date").getAsString().equals(tomorrow.toString()))
                .findFirst()
                .orElseThrow(() -> new IOException("No data found for tomorrow"));
    }
}
