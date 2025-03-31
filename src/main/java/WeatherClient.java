public interface WeatherClient {
    /**
     * Fetches weather data for a given city.
     * @param city the city name to look up
     * @return a WeatherData object containing current weather details
     */
    WeatherData getWeatherData(String city);
}