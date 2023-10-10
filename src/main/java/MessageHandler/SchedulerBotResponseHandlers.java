package MessageHandler;

import Config.BotConfig;
import LogService.LogService;
import Services.UserService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class SchedulerBotResponseHandlers extends TelegramLongPollingBot {

    public static final BotConfig properties = new BotConfig();

    public SchedulerBotResponseHandlers() {
        super(properties.getToken());
    }

    @Override
    public String getBotUsername() {
        return properties.getName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        List<String> chatIds = UserService.getAuthUsers();
        Notifications notification = new Notifications();
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String userMessage = update.getMessage().getText();
            if (!chatIds.contains(chatId)) {
                LogService.addEvent("New unauthorised user " + chatId);
                LogService.addEvent(chatId + " sent: " + userMessage);
                UserService.addUser(chatId);
                notification.newUserNotificationMessage(chatId);
                notification.newUserAccessDeniedMessage(chatId);
            } else {
                MessageHandler message = new MessageHandler();
                message.responseMessage(update);
            }
        }
    }
}