package app.net;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET("forecast.json")
    Call<JsonObject> getForecast(
            @Query("key") String apiKey,
            @Query("q") String city,
            @Query("days") int days
    );
}
