package MessageHandler;

import LogService.LogService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Notifications extends SchedulerBotResponseHandlers {

    private static final Long ADMIN_ID = 1073000130L;

    public void newUserNotificationMessage(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(ADMIN_ID);
        message.setText("Додався новий користувач з ID: " + chatId);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            LogService.addEvent("New user message error: " + e);
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
            LogService.addEvent("Access denied message error: " + e);
        }
    }
}
