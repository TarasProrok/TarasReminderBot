import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class SchedulerBot extends TelegramLongPollingBot {

    public static final BotConfig properties = new BotConfig();

    public SchedulerBot() {
        super(properties.getToken());
    }

    @Override
    public String getBotUsername() {
        return properties.getName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        List<String> chatIds = ChatIdService.getChatIds();
        ChatIdService service = new ChatIdService();
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String userMessage = update.getMessage().getText();
            if (!chatIds.contains(chatId)) {
                System.out.println("New unauthorised user " + chatId);
                System.out.println(chatId + " sent: " + userMessage);
                service.saveChatId(chatId);
                service.newUserNotificationMessage(chatId);
                service.newUserAccessDeniedMessage(chatId);
            } else {
                service.responseMessage(update);
            }
        }
    }
}
