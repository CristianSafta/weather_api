import java.util.Scanner;

public class WeatherApp {

    public static void main(String[] args) {

        //Welcome message
        System.out.println("Welcome to the weather app!");
        System.out.println("Please enter the name of the city of which you want the weather:");

        //Reading the name of the city for which to display the weather
        Scanner input = new Scanner(System.in);
        String city = input.nextLine();

        WeatherService weatherService = new WeatherService(new WeatherAPIClient());
        weatherService.updateWeather(city);

        System.out.println("Here is the weather for " + city);
        System.out.println(weatherService.getCurrentWeather());
    }
}
