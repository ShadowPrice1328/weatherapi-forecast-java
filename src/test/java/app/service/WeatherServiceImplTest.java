package app.service;

import app.model.DayForecast;
import app.net.WeatherApi;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Call;
import retrofit2.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;


import java.time.LocalDate;

public class WeatherServiceImplTest {
    private WeatherApi apiMock;
    private WeatherServiceImpl service;

    @BeforeEach
    void setUp() {
        apiMock = mock(WeatherApi.class);
        service = new WeatherServiceImpl(apiMock, "dummyKey");
    }

    @Test
    void testGetNextDayForecast() throws Exception {
        JsonObject day = new JsonObject();
        day.addProperty("mintemp_c", 10.0);
        day.addProperty("maxtemp_c", 20.0);
        day.addProperty("avghumidity", 50);

        JsonObject firstHour = new JsonObject();
        firstHour.addProperty("wind_kph", 15.0);
        firstHour.addProperty("wind_dir", "N");

        JsonArray hourly = new JsonArray();
        hourly.add(firstHour);

        JsonObject tomorrowForecast = new JsonObject();
        tomorrowForecast.add("day", day);
        tomorrowForecast.add("hour", hourly);

        JsonArray forecastDays = new JsonArray();
        JsonObject forecastDay = new JsonObject();
        forecastDay.addProperty("date", LocalDate.now().plusDays(1).toString());
        forecastDay.add("day", day);
        forecastDay.add("hour", hourly);
        forecastDays.add(forecastDay);

        JsonObject responseBody = new JsonObject();
        JsonObject forecast = new JsonObject();
        forecast.add("forecastday", forecastDays);
        responseBody.add("forecast", forecast);

        // Mock call
        Call<JsonObject> mockCall = mock(Call.class);
        when(apiMock.getForecast(anyString(), anyString(), anyInt())).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(Response.success(responseBody));

        DayForecast result = service.getNextDayForecast("Kyiv");

        assertEquals(10.0, result.minTemp());
        assertEquals(20.0, result.maxTemp());
        assertEquals(50, result.humidity());
        assertEquals(15.0, result.windSpeed());
        assertEquals("N", result.windDir());
    }
}
