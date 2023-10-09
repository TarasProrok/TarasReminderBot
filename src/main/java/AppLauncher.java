import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class AppLauncher {
    public static void main(String[] args) throws TelegramApiException {

        SchedulerBot bot = new SchedulerBot();
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            botsApi.registerBot(bot);
        } catch (
                TelegramApiException e) {
            System.out.println("Register Bot error: " + e);
        }
        System.out.println("Starting bot... ");
        Scheduler.setTimer();
    }
}
