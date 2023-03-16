import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class MyBot extends TelegramLongPollingBot {
    private static final String OPEN_WEATHER_MAP_API_KEY = ;
    private static final String OPEN_WEATHER_MAP_API_URL =
            "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";

    private final String SEND_MESSAGE_ERROR = "Помилка відправки повідомлення: ";
    private List<String> chatIds = new ArrayList<>();
    private LocalTime notificationTime = LocalTime.of(12, 20); // задаємо час розсилки повідомлення

    public static void main(String[] args) throws TelegramApiException {
        MyBot bot = new MyBot();
        bot.startBot();
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            botsApi.registerBot(bot);
        } catch (
                TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "TarasReminderBot";
    }

    @Override
    public String getBotToken() {
        return ;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String messageText = update.getMessage().getText();

            if ("/start".equals(messageText)) {
                chatIds.add(chatId);
                String reply = "Доброго дня!\nЩодня о 11 я присилатиму Вам плани дітей на день!";
                SendMessage message = new SendMessage(chatId, reply);
                try {
                    execute(message);
                } catch (Exception e) {
                    System.err.println(SEND_MESSAGE_ERROR + e.getMessage());
                }
            }
            if ("/today".equals(messageText)) {
                SendMessage message = new SendMessage(chatId, getDayPlanes(LocalDateTime.now().getDayOfWeek()));
                try {
                    execute(message);
                } catch (Exception e) {
                    System.err.println(SEND_MESSAGE_ERROR + e.getMessage());
                }
            }
            if ("/weather".equals(messageText)) {
                String reply = getWeather();
                SendMessage message = new SendMessage(chatId, reply);
                try {
                    execute(message);
                } catch (Exception e) {
                    System.err.println(SEND_MESSAGE_ERROR + e.getMessage());
                }
            }
        }
    }

    public void startBot() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                chatIds.forEach(chatId -> sendDailyMessage(chatId));
            }
        }, 0, 60 * 1000L); // звіряємо час кожну хвилину
    }

    public void sendDailyMessage(String chatId) {
        String weather = getWeather();
        DayOfWeek dayOfWeek = DayOfWeek.from(LocalDateTime.now());
        int localHour = LocalTime.now().getHour();
        int localMinute = LocalTime.now().getMinute();
        int notificationHour = notificationTime.getHour();
        int notificationMinute = notificationTime.getMinute();

        if (notificationHour == localHour && notificationMinute == localMinute) {
            String greetingDay = "Вітаю!\n";
            SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText(greetingDay + getDayPlanes(dayOfWeek) + weather);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getWeather() {
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API_URL, "Rivne", OPEN_WEATHER_MAP_API_KEY));
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
            String description = obj.getJSONArray("weather").getJSONObject(0).getString("description");
            int temp = obj.getJSONObject("main").getInt("temp_max");
            int tempFeelsLike = obj.getJSONObject("main").getInt("feels_like");
            int windSpeed = obj.getJSONObject("wind").getInt("speed");
            int humidity = obj.getJSONObject("main").getInt("humidity");

            switch (description) {
                case "clear sky":
                    description = "чисте небо";
                    break;
                case "few clouds":
                    description = "мінлива хмарність";
                    break;
                case "scattered clouds":
                    description = "розкидані хмари";
                    break;
                case "broken clouds":
                    description = "хмарно з проясненнями";
                    break;
                case "overcast clouds":
                    description = "похмуро";
                    break;
                case "shower rain":
                    description = "зливи";
                    break;
                case "rain":
                    description = "дощ";
                    break;
                case "thunderstorm":
                    description = "гроза";
                    break;
                case "snow":
                    description = "сніг";
                    break;
                case "mist":
                    description = "туман";
                    break;
                default:
                    description = "гарний день!";
                    break;
            }

            return String.format("Сьогодні буде %s" +
                    "\nНайвища температура %s°C" +
                    "\nЗараз відчувається як %s°C" +
                    "\nВітер %s метрів/с" +
                    "\nВологість %s%%",
                    description, temp, tempFeelsLike, windSpeed, humidity);

        } catch (Exception e) {
            e.printStackTrace();
            return "не вдалося отримати прогноз погоди";
        }
    }
    private String getDayPlanes (DayOfWeek dayOfWeek) {
        String delim = "----------------------\n";
        String dayPlanes = "";
        switch (dayOfWeek) {
            case MONDAY:
                    dayPlanes =
                            "Плани на Понеділок:\n" +
                            delim +
                            "Макс:\n" +
                            "Школа - 14:10\n" +
                            "Басейн - 16:00-17:30\n" +
                            delim +
                            "Анюта:\n" +
                            "Школа - 16:00\n" +
                            "Англійська - 17:00-18:00\n" +
                            delim;
                break;
            case TUESDAY:
                    dayPlanes =
                            "Плани на Вівторок:\n" +
                            delim +
                            "Макс:\n" +
                            "Школа - 14:10\n" +
                            "Англійська - 14:30-15:30\n" +
                            delim +
                            "Басейн - 18:00-19:00\n" +
                            "Анюта:\n" +
                            "Школа - 15:05\n" +
                            "Танці(Текст) - 16:00-17:30\n" +
                            delim;
                    break;
            case WEDNESDAY:
                    dayPlanes =
                            "Плани на Середу:\n" +
                            delim +
                            "Макс:\n" +
                            "Школа - 15:05\n" +
                            "Басейн - 18:00-19:30\n" +
                            delim +
                            "Анюта:\n" +
                            "Школа - 15:05\n" +
                            "Англійська 17:00-18:00\n" +
                            delim;
                    break;
            case THURSDAY:
                    dayPlanes =
                            "Плани на Четвер:\n" +
                            delim +
                            "Макс:\n" +
                            "Школа - 15:05\n" +
                            "Басейн - 18:00-19:30\n" +
                            delim +
                            "Анюта:\n" +
                            "Школа - 14:10\n" +
                            "Танці(Кон) - 18:00-19:30\n" +
                            delim;
                break;
            case FRIDAY:
                    dayPlanes =
                            "Плани на П'ятницю:\n" +
                            delim +
                            "Макс\n" +
                            "Школа - 13:15\n" +
                            "Німецька - 15:00-16:00\n" +
                            "Басейн - 18:00-19:30\n" +
                            delim +
                            "Анюта:\n" +
                            "Школа - 15:05\n" +
                            "Танці(Текст) - 18:00-19:00\n" +
                            delim;
                break;
            case SATURDAY:
                    dayPlanes =
                            "Плани на Суботу:\n" +
                            delim +
                            "Макс:\n" +
                            "Англійська - 10:30-11:30\n" +
                            delim +
                            "Анюта:\n" +
                            "Німецька - 11:00-12:00\n" +
                            delim;
                break;
            case SUNDAY:
                    dayPlanes =
                            "НЕДІЛЯ!\n" +
                            "ВИХІДНИЙ!\n";
                    break;
        }
        return dayPlanes;
        }
    }