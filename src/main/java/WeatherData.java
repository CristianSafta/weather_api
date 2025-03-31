import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherData {

    private double lat;
    private double lon;
    private String timezone;
    private int timezone_offset;
    private Current current;

    // --- Getters & Setters ---

    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }

    public double getLon() { return lon; }
    public void setLon(double lon) { this.lon = lon; }

    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }

    public int getTimezone_offset() { return timezone_offset; }
    public void setTimezone_offset(int timezone_offset) { this.timezone_offset = timezone_offset; }

    public Current getCurrent() { return current; }
    public void setCurrent(Current current) { this.current = current; }

    /**
     * Produces a more human-readable weather summary.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("==== Weather Report ====\n");
        sb.append(String.format("Location: (lat: %.4f, lon: %.4f)\n", lat, lon));
        sb.append(String.format("Timezone: %s (offset: %d seconds from UTC)\n",
                timezone, timezone_offset));

        if (current != null) {
            sb.append(current.toString());
        } else {
            sb.append("No current weather data available.\n");
        }

        return sb.toString();
    }

    // =======================
    // Nested 'Current' class
    // =======================
    public static class Current {
        private long dt;
        private long sunrise;
        private long sunset;
        private double temp;
        private double feels_like;
        private int pressure;
        private int humidity;
        private double dew_point;
        private double uvi;
        private int clouds;
        private int visibility;
        private double wind_speed;
        private int wind_deg;
        private List<Weather> weather;
        private Rain rain; // might be null if there's no rain

        // --- Getters & Setters ---
        public long getDt() { return dt; }
        public void setDt(long dt) { this.dt = dt; }

        public long getSunrise() { return sunrise; }
        public void setSunrise(long sunrise) { this.sunrise = sunrise; }

        public long getSunset() { return sunset; }
        public void setSunset(long sunset) { this.sunset = sunset; }

        public double getTemp() { return temp; }
        public void setTemp(double temp) { this.temp = temp; }

        public double getFeels_like() { return feels_like; }
        public void setFeels_like(double feels_like) { this.feels_like = feels_like; }

        public int getPressure() { return pressure; }
        public void setPressure(int pressure) { this.pressure = pressure; }

        public int getHumidity() { return humidity; }
        public void setHumidity(int humidity) { this.humidity = humidity; }

        public double getDew_point() { return dew_point; }
        public void setDew_point(double dew_point) { this.dew_point = dew_point; }

        public double getUvi() { return uvi; }
        public void setUvi(double uvi) { this.uvi = uvi; }

        public int getClouds() { return clouds; }
        public void setClouds(int clouds) { this.clouds = clouds; }

        public int getVisibility() { return visibility; }
        public void setVisibility(int visibility) { this.visibility = visibility; }

        public double getWind_speed() { return wind_speed; }
        public void setWind_speed(double wind_speed) { this.wind_speed = wind_speed; }

        public int getWind_deg() { return wind_deg; }
        public void setWind_deg(int wind_deg) { this.wind_deg = wind_deg; }

        public List<Weather> getWeather() { return weather; }
        public void setWeather(List<Weather> weather) { this.weather = weather; }

        public Rain getRain() { return rain; }
        public void setRain(Rain rain) { this.rain = rain; }

        /**
         * Creates a simple multi-line summary of current conditions.
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("\n--- Current Weather Conditions ---\n");
            sb.append(String.format("Temperature: %.2f°C (feels like %.2f°C)\n", temp, feels_like));
            sb.append(String.format("Humidity: %d%% | Pressure: %dhPa\n", humidity, pressure));
            sb.append(String.format("Cloud Coverage: %d%% | UV Index: %.2f\n", clouds, uvi));
            sb.append(String.format("Wind: %.1fm/s from %d°\n", wind_speed, wind_deg));
            sb.append(String.format("Visibility: %dm\n", visibility));

            if (rain != null) {
                sb.append(String.format("Rain (last hour): %.2f mm\n", rain.getOneHour()));
            }

            // Show weather descriptions if present
            if (weather != null && !weather.isEmpty()) {
                sb.append("Weather summary:\n");
                for (Weather w : weather) {
                    sb.append("  - ").append(w.toString()).append("\n");
                }
            } else {
                sb.append("No specific weather condition data.\n");
            }

            return sb.toString();
        }
    }

    // ======================
    // Nested 'Weather' class
    // ======================
    public static class Weather {
        private int id;
        private String main;
        private String description;
        private String icon;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getMain() { return main; }
        public void setMain(String main) { this.main = main; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getIcon() { return icon; }
        public void setIcon(String icon) { this.icon = icon; }

        /**
         * A more user-friendly summary of the weather condition.
         */
        @Override
        public String toString() {
            // e.g. "Light Rain (icon: 10d)"
            return String.format("%s - %s (icon: %s)",
                    main, description, icon);
        }
    }

    // =====================
    // Nested 'Rain' class
    // =====================
    public static class Rain {
        @SerializedName("1h")
        private double oneHour;

        public double getOneHour() { return oneHour; }
        public void setOneHour(double oneHour) { this.oneHour = oneHour; }

        @Override
        public String toString() {
            return String.format("Rain{oneHour=%.2f mm}", oneHour);
        }
    }
}
