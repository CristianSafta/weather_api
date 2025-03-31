public class WeatherService {
    WeatherAPIClient weatherClient;
    WeatherData weatherData;
    public WeatherService(WeatherAPIClient weatherClient) {

        this.weatherClient = weatherClient;


    }

    public void updateWeather(String city){
        weatherData = weatherClient.getWeatherData(city);
    }

    public String getCurrentWeather(){
        return weatherData.toString();
    }


}
