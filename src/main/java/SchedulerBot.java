import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

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
        ChatIdService service = new ChatIdService();
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            service.saveChatId(chatId);
            service.newUserNotificationMessage(chatId);
            MessageHandler messageHandler = new MessageHandler();
            messageHandler.responseMessage(update);
        }
    }
}
