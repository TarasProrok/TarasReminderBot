package Services;

import LogService.LogService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class WeatherRetrieveService {
    private WeatherRetrieveService() {
    }

    private static final String OPEN_WEATHER_MAP_API_KEY = "2e87361fc4aa00e17b64a24b7ae7cca6";
    private static final String OPEN_WEATHER_MAP_API_URL =
            "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";
    private static final String OPEN_FORECAST_MAP_API_URL =
            "http://api.openweathermap.org/data/2.5/forecast?q=%s&appid=%s&units=metric";

    static URL url;
    static URL forecastUrl;

    static {
        try {
            url = new URL(String.format(OPEN_WEATHER_MAP_API_URL, "Rivne", OPEN_WEATHER_MAP_API_KEY));
            forecastUrl = new URL(String.format(OPEN_FORECAST_MAP_API_URL, "Rivne", OPEN_WEATHER_MAP_API_KEY));
        } catch (MalformedURLException e) {
            LogService.addEvent("Connecting to weather service error: " + e);
        }
    }


    public static String getWeather() {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();

            JSONObject obj = new JSONObject(response.toString());
            String description = convertValueIntoString();
            int temp = getDailyMaxTemperature();
            int tempFeelsLike = obj.getJSONObject("main").getInt("feels_like");
            int windSpeed = obj.getJSONObject("wind").getInt("speed");
            int humidity = obj.getJSONObject("main").getInt("humidity");

            return String.format("Сьогодні %s," +
                            "\n️\uD83C\uDF21️ Найвища температура %s°C" +
                            "\n\uD83C\uDF21️ Зараз відчувається як %s°C" +
                            "\n\uD83E\uDE81️ Вітер %s м/с" +
                            "\n\uD83D\uDCA7 Вологість %s%%\n" +
                            "----------------------\n",
                    description, temp, tempFeelsLike, windSpeed, humidity);

        } catch (Exception e) {
            LogService.addEvent("Connecting to weather service error: " + e);
            return "не вдалося отримати прогноз погоди";
        }
    }

    private static int getDailyMaxTemperature() {
        try {
            HttpURLConnection con = (HttpURLConnection) forecastUrl.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
            JsonArray list = jsonObject.getAsJsonArray("list");
            int maxTemperature = Integer.MIN_VALUE;

            for (int i = 0; i < 3; i++) {
                JsonObject listItem = list.get(i).getAsJsonObject();
                int temperature = listItem.getAsJsonObject("main").get("temp").getAsInt();
                if (temperature > maxTemperature) {
                    maxTemperature = temperature;
                }
            }

            return maxTemperature;
        } catch (Exception e) {
            LogService.addEvent("Getting Max daily temperature error: " + e);
            return 100;
        }
    }
    private static int getDailyMaxWeatherCondition() throws IOException {

        HttpURLConnection con = (HttpURLConnection) forecastUrl.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
        JsonArray list = jsonObject.getAsJsonArray("list");
        HashMap<Integer, Integer> conditionCounts = new HashMap<>();

        for (int i = 0; i < 3; i++) {
            JsonObject listItem = list.get(i).getAsJsonObject();
            JsonArray weatherList = listItem.getAsJsonArray("weather");
            int condition = weatherList.get(0).getAsJsonObject().get("id").getAsInt();
            if (conditionCounts.containsKey(condition)) {
                conditionCounts.put(condition, conditionCounts.get(condition) + 1);
            } else {
                conditionCounts.put(condition, 1);
            }
        }

        // Sort the condition counts map in descending order of values
        List<Map.Entry<Integer, Integer>> entries = new ArrayList<>(conditionCounts.entrySet());
        Collections.sort(entries, (e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        return entries.get(0).getKey();
    }

    private static String convertValueIntoString () throws IOException {
        int conditionValue = getDailyMaxWeatherCondition();
        String description;
        switch (conditionValue) {
            case 200:
            case 201:
            case 202:
            case 210:
            case 211:
            case 212:
            case 221:
            case 230:
            case 231:
            case 232:
                description = "гроза️ ⛈️";
                break;
            case 300:
            case 301:
            case 302:
            case 310:
            case 311:
            case 312:
            case 313:
            case 314:
            case 321:
                description = "мряка";
                break;
            case 500:
            case 501:
            case 502:
            case 503:
            case 511:
                description = "дощ \uD83C\uDF27️";
                break;
            case 504:
            case 520:
            case 521:
            case 522:
            case 531:
                description = "зливи \uD83C\uDF27️";
                break;
            case 600:
            case 601:
            case 602:
            case 611:
            case 612:
            case 613:
            case 615:
            case 616:
            case 620:
            case 621:
            case 622:
                description = "сніг \uD83C\uDF28️";
                break;
            case 701:
            case 741:
                description = "туман \uD83C\uDF01";
                break;
            case 800:
                description = "ясно ☀️";
                break;
            case 801:
                description = "мінлива хмарність \uD83C\uDF24️";
                break;
            case 802:
                description = "хмарно  ⛅";
                break;
            case 803:
                description = "хмарно з проясненнями \uD83C\uDF25️";
                break;
            case 804:
                description = "похмуро️ ☁️";
                break;
                default:
                description = "гарний день! \uD83E\uDEF6";
                break;
            }
            return description;
    }
}


