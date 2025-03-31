/**
 * {@code WeatherService} acts as a layer between the application logic and the weather client.
 * It fetches weather data for a given city and provides a formatted weather summary.
 *
 * <p>This service uses a {@code WeatherAPIClient} to retrieve data and exposes
 * a simple API for updating and accessing the weather information.</p>
 */
public class WeatherService {

    /** The client responsible for fetching weather data from an external API */
    WeatherAPIClient weatherClient;

    /** Holds the most recently fetched weather data */
    WeatherData weatherData;

    /**
     * Constructs a new {@code WeatherService} with the provided weather client.
     *
     * @param weatherClient The client to use for fetching weather data
     */
    public WeatherService(WeatherAPIClient weatherClient) {
        this.weatherClient = weatherClient;
    }

    /**
     * Updates the internal weather data by querying the API for the specified city.
     *
     * @param city The name of the city for which to fetch weather data
     */
    public void updateWeather(String city) {
        weatherData = weatherClient.getWeatherData(city);
    }

    /**
     * Returns a human-readable string of the current weather conditions.
     *
     * @return Weather summary formatted via {@code WeatherData.toString()}
     */
    public String getCurrentWeather() {
        return weatherData.toString();
    }
}
