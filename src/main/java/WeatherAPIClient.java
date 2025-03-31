import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;


public class WeatherAPIClient implements WeatherClient{

    private String getEndpoint(String city) {
        String url = "http://api.openweathermap.org/geo/1.0/direct?q=" + city + "&limit=1&appid=e0970253c1aa6310d0a2a27943687533";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = null;
        String responseBody;


        try {
            response = client.newCall(request).execute();
            assert response.body() != null;
            responseBody = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        Gson gson = new Gson();
        // The endpoint returns an ARRAY, so we parse into an array of `GeoResponse`
        GeoResponse[] geoData = gson.fromJson(responseBody, GeoResponse[].class);

        // Because we used limit=1, we expect exactly 1 element in geoData (but it's good to check length anyway)
        double lat = 0;
        double lon = 0;
        if (geoData != null && geoData.length > 0) {
            lat = geoData[0].getLat();
            lon = geoData[0].getLon();

            // Now you have lat & lon!
            System.out.println("Lat: " + lat + " | Lon: " + lon);
        } else {
            // handle the case where no locations were returned
            System.out.println("No geocoding data found for city: " + city);
        }

        return "https://api.openweathermap.org/data/3.0/onecall?lat=" + lat + "&lon=" + lon + "&exclude=alerts,minutely,hourly,daily&appid=e0970253c1aa6310d0a2a27943687533";
    }


    public WeatherData getWeatherData(String city) {
        OkHttpClient client = new OkHttpClient();
        String endpoint = "https://api.openweathermap.org/data/3.0/onecall?lat=33.44&lon=-94.04&exclude=alerts,minutely,hourly,daily&appid=e0970253c1aa6310d0a2a27943687533";

        Request request = new Request.Builder()
                .url(endpoint)
                .build();

        Response response = null;
        String responseBody;


        try {
            response = client.newCall(request).execute();
            assert response.body() != null;
            responseBody = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        // Convert JSON -> WeatherResponse object using Gson
        Gson gson = new Gson();
        WeatherData weatherResponse = gson.fromJson(responseBody, WeatherData.class);

        return weatherResponse;

    }

}
