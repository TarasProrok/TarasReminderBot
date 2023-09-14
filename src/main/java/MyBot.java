import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static org.telegram.telegrambots.meta.api.methods.ParseMode.HTML;

public class MyBot extends TelegramLongPollingBot {

    private final LocalTime notificationTime = LocalTime.of(10, 0); // задаємо час розсилки повідомлення

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
        return "5958623884:AAGahM1YxcgehMyFGHSPeRperjJCY9BUlHg";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String messageText = update.getMessage().getText();
            BotData.saveChatId(update.getMessage().getChatId().toString());
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setParseMode(HTML);

            switch (messageText) {
                case "/start":
                    message.setText("Вітаннячко!\nЦей ботик щодня о 10 присилатиме\nплани дітей на день!");
                    break;
                case "/today":
                    try {
                        message.setText(getDayPlanes(LocalDateTime.now().getDayOfWeek())
                        + Weather.getWeather()
                        + CurrencyParser.prettyRates());
                    } catch (IOException e) {
                        message.setText(getDayPlanes(LocalDateTime.now().getDayOfWeek())
                        + Weather.getWeather()
                        + "Не вдалося отримати курси.");
                    }
                    break;
                case "/weather":
                    message.setText(Weather.getWeather());
                    break;
                case "/currency":
                    try {
                        message.setText(CurrencyParser.prettyRates());
                    } catch (IOException e) {
                        message.setText("Не вдалося отримати круси валют");
                    }
                    break;
                case "/full":
                    message.setText(
                            getDayPlanes(DayOfWeek.MONDAY)
                            + "\n"
                            + getDayPlanes(DayOfWeek.TUESDAY)
                            + "\n"
                            + getDayPlanes(DayOfWeek.WEDNESDAY)
                            + "\n"
                            + getDayPlanes(DayOfWeek.THURSDAY)
                            +"\n"
                            + getDayPlanes(DayOfWeek.FRIDAY)
                            +"\n"
                            + getDayPlanes(DayOfWeek.SATURDAY));
                    break;
            }
            try {
                execute(message);
            } catch (Exception e) {
                System.out.println("Сталася помилка");
            }
        }
    }

    public void startBot() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                List<String> chatIds = BotData.getChatIds();
                chatIds.forEach(chatId -> {
                    try {
                        sendDailyMessage(chatId);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 60 * 1000L); // звіряємо час кожну хвилину
    }

    public void sendDailyMessage(String chatId) throws IOException {
        DayOfWeek dayOfWeek = DayOfWeek.from(LocalDateTime.now());
        int localHour = LocalTime.now().getHour();
        int localMinute = LocalTime.now().getMinute();
        int notificationHour = notificationTime.getHour();
        int notificationMinute = notificationTime.getMinute();

        if (notificationHour == localHour && notificationMinute == localMinute) {
            String iLoveYou = "\nПуська, я тебе люблю! \uD83E\uDEF6";
            String greetingDay = "Вітаю! \uD83E\uDEF6 \n";
            if (chatId.equals("5289935625")) {
                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText(greetingDay
                        + getDayPlanes(dayOfWeek)
                        + Weather.getWeather()
                        + CurrencyParser.prettyRates()
                        + iLoveYou);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText(greetingDay
                        + getDayPlanes(dayOfWeek)
                        + Weather.getWeather()
                        + CurrencyParser.prettyRates());
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getDayPlanes(DayOfWeek dayOfWeek) {
        String delim = "----------------------\n";
        String max = "\uD83D\uDC66\uD83C\uDFFC <b>Макс:</b>\n";
        String anna = "\uD83D\uDC67\uD83C\uDFFB <b>Анюта:</b>\n";
        String schoolSixLessons = "🏫 Школа - 14:10\n";
        String schoolSevenLessons = "🏫 Школа - 15:05\n";
        String schoolEightLessons = "🏫 Школа - 16:00\n";
        String eng = "🇬🇧 Англійська - ";
        String pool = "\uD83C\uDFCA\u200D♂️ Басейн - 17:30-19:45\n";
        String dances = "\uD83D\uDC83\uD83C\uDFFB Танці - ";
        String germ = "\uD83C\uDDE9\uD83C\uDDEA Німецька - ";
        String dayPlanes = "";

        switch (dayOfWeek) {
            case MONDAY:
                    dayPlanes =
                            "<i>Плани на Понеділок:</i>\n" +
                                    delim +
                                    max +
                                    schoolSevenLessons +
                                    pool +
                                    delim +
                                    anna +
                                    schoolSevenLessons +
                                    delim;
                break;
            case TUESDAY:
                    dayPlanes =
                            "<i>Плани на Вівторок:</i>\n" +
                                    delim +
                                    max +
                                    schoolSevenLessons +
                                    eng + "16:00-17:00\n" +
                                    pool +
                                    delim +
                                    anna +
                                    schoolSevenLessons +
                                    dances + "19:00-20:30\n(Текстильник)\n" +
                                    delim;
                    break;
            case WEDNESDAY:
                    dayPlanes =
                            "<i>Плани на Середу:</i>\n" +
                            delim +
                            max +
                            schoolSixLessons +
                            germ + "15:00-16:00\n" +
                            pool +
                            delim +
                            anna +
                            schoolEightLessons +
                            eng + "17:00-18:00\n" +
                            delim;
                    break;
            case THURSDAY:
                    dayPlanes =
                            "<i>Плани на Четвер:</i>\n" +
                            delim +
                            max +
                            schoolSevenLessons +
                            eng + "16:00-17:00\n" +
                            pool +
                            delim +
                            anna +
                            schoolEightLessons +
                            dances + "17:30-19:00\n(Коновальця)\n" +
                            delim;
                break;
            case FRIDAY:
                    dayPlanes =
                            "<i>Плани на П'ятницю:</i>\n" +
                            delim +
                            max +
                            schoolSevenLessons +
                            pool +
                            delim +
                            anna +
                            schoolSevenLessons +
                            dances + "19:00-20:30\n(Текстильник)\n" +
                            delim;
                break;
            case SATURDAY:
                    dayPlanes =
                            "<i>Плани на Суботу:</i>\n" +
                            delim +
                            max +
                            "\uD83C\uDFCA\u200D♂️ Басейн - 11:00-12:45\n" +
                            delim +
                            anna +
                            eng + "11:45-12:45\n" +
                            germ + "12:00-13:00\n" +
                            delim;
                break;
            case SUNDAY:
                    dayPlanes =
                            "<b>НЕДІЛЯ!\nВИХІДНИЙ!</b>\n";
                    break;
        }
        return dayPlanes;
        }
    }