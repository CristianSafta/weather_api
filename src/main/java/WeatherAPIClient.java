import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * {@code WeatherAPIClient} is a concrete implementation of the {@code WeatherClient} interface.
 * It uses the OpenWeatherMap API to fetch current weather data for a given city.
 *
 * <p>This client first performs geocoding (city name → latitude and longitude) and then
 * queries the One Call API to retrieve weather data.</p>
 *
 * <p>It expects an {@code OPENWEATHER_API_KEY} to be defined in a {@code config.properties}
 * file located in the classpath.</p>
 */
public class WeatherAPIClient implements WeatherClient {

    /** The API key for authenticating requests to OpenWeatherMap */
    private final String apiKey;

    /**
     * Constructs a new {@code WeatherAPIClient}, loading the API key from the config file.
     */
    public WeatherAPIClient() {
        this.apiKey = loadApiKey();
    }

    /**
     * Loads the OpenWeatherMap API key from the {@code config.properties} file.
     *
     * @return The API key as a string
     * @throws RuntimeException if the file cannot be read or the key is missing
     */
    private String loadApiKey() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(input);
            return properties.getProperty("OPENWEATHER_API_KEY");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load API key from config.properties", e);
        }
    }

    /**
     * Builds the endpoint URL for querying weather data by first resolving the
     * geolocation (latitude and longitude) of the given city using OpenWeatherMap's
     * geocoding API.
     *
     * @param city The name of the city
     * @return The full One Call API URL for the city's weather
     * @throws RuntimeException if the HTTP request fails
     */
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

    /**
     * Retrieves the current weather data for the specified city.
     * This includes temperature, weather conditions, etc., as returned by OpenWeatherMap.
     *
     * @param city The name of the city
     * @return A {@code WeatherData} object containing the weather info
     * @throws RuntimeException if the HTTP request fails
     */
    public WeatherData getWeatherData(String city) {
        OkHttpClient client = new OkHttpClient();
        String endpoint = getEndpoint(city); // Constructs URL using dynamic lat/lon

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
