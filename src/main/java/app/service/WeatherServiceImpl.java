package app.service;

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
    public Map<String, Object> getNextDayForecast(String city) throws IOException {
        Response<JsonObject> response = api.getForecast(apiKey, city, 2).execute();
        if (!response.isSuccessful() || response.body() == null) {
            throw  new IOException("Failed to fetch forecast for " + city);
        }

        LocalDate tomorrow = LocalDate.now().plusDays(1);

        JsonObject dayForecast = response.body()
                .getAsJsonObject("forecast")
                .getAsJsonArray("forecastday")
                .asList()
                .stream()
                .map(JsonElement::getAsJsonObject)
                .filter(e -> e.get("date").getAsString().equals(tomorrow.toString()))
                .findFirst()
                .orElseThrow(() -> new IOException("No data found for tomorrow"));

        JsonObject day = dayForecast.getAsJsonObject("day");

        Map<String, Object> result = new HashMap<>();
        result.put("minTemp", day.get("mintemp_c").getAsDouble());
        result.put("maxTemp", day.get("maxtemp_c").getAsDouble());
        result.put("humidity", day.get("avghumidity").getAsInt());

        JsonObject firstHour = dayForecast.getAsJsonArray("hour").get(0).getAsJsonObject();
        result.put("windSpeed", firstHour.get("wind_kph").getAsDouble());
        result.put("windDir", firstHour.get("wind_dir").getAsString());

        return result;
    }
}
