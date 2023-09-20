import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

import static org.telegram.telegrambots.meta.api.methods.ParseMode.HTML;

public class MessageHandler extends SchedulerBot {

    public void responseMessage(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String messageText = update.getMessage().getText();

        SendMessage message = new SendMessage();
        message.setParseMode(HTML);
        message.setChatId(chatId);

        switch (messageText) {
            case "/start":
                System.out.println("Received command START!");
                message.setText("Вітаннячко!\nЦей ботик щодня о 10 присилатиме\nплани дітей на день!");
                break;
            case "/today":
                System.out.println("Received command TODAY! Sending plans for today!");
                message.setText(DayPlanes.getDayPlanes(LocalDateTime.now().getDayOfWeek()));
                break;
            case "/weather":
                System.out.println("Received command WEATHER! Sending weather forecast for today!");
                message.setText(Weather.getWeather());
                break;
            case "/currency":
                try {
                    System.out.println("Received command CURRENCY! Sending currency rates!");
                    message.setText(CurrencyParser.prettyRates());
                } catch (IOException e) {
                    message.setText("Не вдалося отримати круси валют");
                    System.out.println("Currency response error: " + e);
                }
                break;
            case "/full":
                System.out.println("Received command FULL! Sending week schedule!");
                message.setText(
                        DayPlanes.getWeekPlans());
                break;
        }
        try {
            execute(message);
        } catch (Exception e) {
            System.out.println("Executing response message error: " + e);
        }
    }

    public void sendDailyMessage(String chatId) throws IOException {
        DayOfWeek dayOfWeek = DayOfWeek.from(LocalDateTime.now());
        String iLoveYou = "\nПуська, я тебе люблю! \uD83E\uDEF6";
        String greetingDay = "Вітаю! \uD83E\uDEF6 \n";

        SendMessage message = new SendMessage();

        if (chatId.equals("5289935625")) {
            message.setChatId(chatId);
            message.setParseMode(HTML);
            message.setText(greetingDay
                    + DayPlanes.getDayPlanes(dayOfWeek)
                    + Weather.getWeather()
                    + CurrencyParser.prettyRates()
                    + iLoveYou);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                System.out.println("Vika`s daily message error: " + e);
            }
        } else {
            message.setChatId(chatId);
            message.setParseMode(HTML);
            message.setText(greetingDay
                    + DayPlanes.getDayPlanes(dayOfWeek)
                    + Weather.getWeather()
                    + CurrencyParser.prettyRates());
            try {
                execute(message);
            } catch (TelegramApiException e) {
                System.out.println("Daily message error: " + e);
            }
        }
    }

    public void sendMessageForTomorrow (String chatId) {
        DayOfWeek nextDay = DayOfWeek.from(LocalDateTime.now().plusDays(1L));

        SendMessage message = new SendMessage();

            message.setChatId(chatId);
            message.setParseMode(HTML);
            message.setText(DayPlanes.getDayPlanes(nextDay));
            try {
                execute(message);
            } catch (TelegramApiException e) {
                System.out.println("Next day message error: " + e);
            }
        }
    }