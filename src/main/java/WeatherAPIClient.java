import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class WeatherAPIClient implements WeatherClient {

    private final String apiKey;

    public WeatherAPIClient() {
        this.apiKey = loadApiKey();
    }

    private String loadApiKey() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties");) {
            properties.load(input);
            return properties.getProperty("OPENWEATHER_API_KEY");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load API key from config.properties", e);
        }
    }

    private String getEndpoint(String city) {
        String url = "http://api.openweathermap.org/geo/1.0/direct?q=" + city + "&limit=1&appid=" + apiKey;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        String responseBody;

        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            responseBody = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Gson gson = new Gson();
        GeoResponse[] geoData = gson.fromJson(responseBody, GeoResponse[].class);

        double lat = 0;
        double lon = 0;
        if (geoData != null && geoData.length > 0) {
            lat = geoData[0].getLat();
            lon = geoData[0].getLon();
            System.out.println("Lat: " + lat + " | Lon: " + lon);
        } else {
            System.out.println("No geocoding data found for city: " + city);
        }

        return "https://api.openweathermap.org/data/3.0/onecall?lat=" + lat + "&lon=" + lon + "&exclude=alerts,minutely,hourly,daily&appid=" + apiKey;
    }

    public WeatherData getWeatherData(String city) {
        OkHttpClient client = new OkHttpClient();
        String endpoint = getEndpoint(city); // Corrected to use dynamic lat/lon from getEndpoint

        Request request = new Request.Builder().url(endpoint).build();
        String responseBody;

        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            responseBody = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Gson gson = new Gson();
        return gson.fromJson(responseBody, WeatherData.class);
    }
}
