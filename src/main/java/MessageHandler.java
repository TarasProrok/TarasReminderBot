import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

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
                System.out.println("Received command START from ID: " + chatId + "\nSending greetings message!");
                message.setText("Вітаннячко!\n" +
                        "Цей ботик щодня о 9:45 присилатиме\n" +
                        "плани дітей на поточний день\n" +
                        "і о 20:00 на завтра!");
                break;
            case "/today":
                try {
                    System.out.println("Received command TODAY from ID: " + chatId + "\nSending plans for today!");
                    message.setParseMode(HTML);
                    message.setText(DayPlanes.getDayPlanes(DayOfWeek.from(LocalDateTime.now()))
                            + Weather.getWeather()
                            + CurrencyParser.prettyRates());
                } catch (IOException e) {
                    message.setText("Не вдалося отримати круси валют");
                    System.out.println("Currency response error: " + e);
                }
                break;
            case "/weather":
                System.out.println("Received command WEATHER from ID: " + chatId + "\nSending weather forecast for today!");
                message.setText(Weather.getWeather());
                break;
            case "/currency":
                try {
                    System.out.println("Received command CURRENCY from ID: " + chatId + "\nSending currency rates!");
                    message.setText(CurrencyParser.prettyRates());
                } catch (IOException e) {
                    message.setText("Не вдалося отримати круси валют");
                    System.out.println("Currency response error: " + e);
                }
                break;
            case "/userlist":
                System.out.println("Received command USERLIST from ID: " + chatId + "\nSending users list!");
                message.setText(ChatIdService.getChatIds().toString());
                break;
        }
        try {
            execute(message);
        } catch (Exception e) {
            System.out.println("Executing response message error. ID: " + chatId + "Error: " + e);
        }
    }

    public void sendMessageForToday(String chatId) throws IOException {
        DayOfWeek dayOfWeek = DayOfWeek.from(LocalDateTime.now());
        String iLoveYouString = "\nПуська, я тебе люблю! \uD83E\uDEF6";
        String greetingDay = "Вітаю! \uD83E\uDEF6 \n";

        SendMessage message = new SendMessage();

        if (chatId.equals("5289935625")) {
            message.setChatId(chatId);
            message.setParseMode(HTML);
            message.setText(greetingDay
                    + DayPlanes.getDayPlanes(dayOfWeek)
                    + Weather.getWeather()
                    + CurrencyParser.prettyRates()
                    + iLoveYouString);
            try {
                execute(message);
                System.out.println("Message to Vika sent successfully.");
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
                System.out.println("\nMessage to ID: " + chatId + " sent successfully");
            } catch (TelegramApiException e) {
                System.out.println("Error sending daily message to ID: " + chatId + "\nError: " + e);
            }
        }
    }

    public void sendMessageForTomorrow(String chatId) {
        DayOfWeek nextDay = DayOfWeek.from(LocalDateTime.now().plusDays(1L));

        SendMessage message = new SendMessage();

        message.setChatId(chatId);
        message.setParseMode(HTML);
        message.setText(DayPlanes.getDayPlanes(nextDay));
        try {
            execute(message);
            System.out.println("\nMessage to ID: " + chatId + " sent successfully.");
        } catch (TelegramApiException e) {
            System.out.println("Error sending next day message to ID: " + chatId + "\nError: " + e);
        }
    }

    public void newUserNotificationMessage(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(1073000130L);
        message.setText("Додався новий користувач з ID: " + chatId);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println("Notification message error: " + e);
        }
    }
    public void newUserAccessDeniedMessage(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("⚠️ User ID: " + chatId + "!\nThis is private bot and You are not authorised user! " +
                "You will not receive any messages in the future. Please leave. Thank you." +
                "\n\n⚠️ Користувач з ID: " + chatId + "!\nЦе приватний бот і Ви не є авторизованим користувачем! " +
                "Ви не будете отримувати повідомлень в майбутньому. Будь ласка, видаліть цей бот. Дякую.");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println("Access denied message error: " + e);
        }
    }
}