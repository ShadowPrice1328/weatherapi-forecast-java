package main.java.app.net;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    //forecast.json?key=47ba6033966f46f3854122612252808&q=Kyiv&days=1
    @GET("forecast.json")
    Call<JsonObject> getForecast(
            @Query("key") String apiKey,
            @Query("q") String city,
            @Query("days") int days
    );
}
