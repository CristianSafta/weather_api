import java.util.Scanner;

/**
 * The {@code WeatherApp} class serves as the entry point to a simple console-based
 * weather application. It prompts the user for a city name, fetches the weather
 * for that city using a weather service, and displays it.
 *
 */
public class WeatherApp {

    /**
     * Main method which initiates the weather application.
     * It prompts the user for input, fetches weather data, and displays it.
     *
     * @param args Command-line arguments (not used in this application)
     */
    public static void main(String[] args) {

        // Welcome message
        System.out.println("Welcome to the weather app!");
        System.out.println("Please enter the name of the city of which you want the weather:");

        // Reading the name of the city for which to display the weather
        Scanner input = new Scanner(System.in);
        String city = input.nextLine();

        // Create a weather service that uses a WeatherAPIClient to fetch weather data
        WeatherService weatherService = new WeatherService(new WeatherAPIClient());

        // Update the weather data for the provided city
        weatherService.updateWeather(city);

        // Display the fetched weather information
        System.out.println("Here is the weather for " + city);
        System.out.println(weatherService.getCurrentWeather());
    }
}
