package MessageHandler;

import DayPlansService.DayPlanes;
import LogService.LogService;
import Services.CurrencyParserService;
import Services.UserService;
import Services.WeatherRetrieveService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

import static org.telegram.telegrambots.meta.api.methods.ParseMode.HTML;

public class MessageHandler extends SchedulerBotResponseHandlers {

    public void responseMessage(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String messageText = update.getMessage().getText();

        SendMessage message = new SendMessage();
        message.setParseMode(HTML);
        message.setChatId(chatId);

        String[] commandTwoSplit = messageText.split(" ", 2);
        String userId = commandTwoSplit[1].trim();
        String command = commandTwoSplit[0].trim();

        switch (command) {
            case "/start":
                LogService.addEvent("Received command START from ID: " + chatId);
                message.setText("So\n" +
                        "Scheduled time: 9:45 and 20:00\n" +
                        "Commands:\n" +
                        "today - plans for today\n" +
                        "getlog - see log" +
                        "2auth + id - add auth user" +
                        "users - authorised users\n" +
                        "all - all users");
                break;
            case "/today":
                try {
                    LogService.addEvent("Received command " + messageText + " from ID: " + chatId);
                    message.setParseMode(HTML);
                    message.setText(DayPlanes.getDayPlanes(DayOfWeek.from(LocalDateTime.now()))
                            + WeatherRetrieveService.getWeather()
                            + CurrencyParserService.prettyRates());
                } catch (IOException e) {
                    message.setText("Не вдалося отримати круси валют");
                    LogService.addEvent("Currency response error: " + e);
                }
                break;
            case "/getlog":
                LogService.addEvent("Received command " + messageText + " from ID: " + chatId);
                message.setText(LogService.getLog().toString());
                break;
            case "/users":
                LogService.addEvent("Received command " + messageText + " from ID: " + chatId);
                message.setText(UserService.getAuthUsers().toString());
                break;
            case "2auth":
                UserService.addAuthUser(userId);
                LogService.addEvent("Received command " + messageText + " from ID: " + chatId);
                break;
            case "/all":
                LogService.addEvent("Received command " + messageText + " from ID: " + chatId);
                message.setText(UserService.getAllUsers().toString());
                break;
            default:
                LogService.addEvent("Received " + messageText + " from ID: " + chatId);
                message.setText("Invalid command!");
        }
        try {
            execute(message);
        } catch (Exception e) {
            LogService.addEvent("Executing response message error. ID: " + chatId);
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
                    + WeatherRetrieveService.getWeather()
                    + CurrencyParserService.prettyRates()
                    + iLoveYouString);
            try {
                execute(message);
                LogService.addEvent("Message to Vika sent successfully.");
            } catch (TelegramApiException e) {
                LogService.addEvent("Vika`s daily message error: " + e);
            }
        } else {
            message.setChatId(chatId);
            message.setParseMode(HTML);
            message.setText(greetingDay
                    + DayPlanes.getDayPlanes(dayOfWeek)
                    + WeatherRetrieveService.getWeather()
                    + CurrencyParserService.prettyRates());
            try {
                execute(message);
                LogService.addEvent("Message to ID: " + chatId + " sent successfully");
            } catch (TelegramApiException e) {
                LogService.addEvent("Error sending daily message to ID: " + chatId + "\nError: " + e);
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
            LogService.addEvent("Message to ID: " + chatId + " sent successfully.");
        } catch (TelegramApiException e) {
            LogService.addEvent("Error sending next day message to ID: " + chatId + "\nError: " + e);
        }
    }
}