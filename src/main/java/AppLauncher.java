import Config.Scheduler;
import LogService.LogService;
import MessageHandler.SchedulerBotResponseHandlers;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class AppLauncher {
    public static void main(String[] args) throws TelegramApiException {

        SchedulerBotResponseHandlers bot = new SchedulerBotResponseHandlers();
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            botsApi.registerBot(bot);
        } catch (
                TelegramApiException e) {
            LogService.addEvent("Register Bot error: " + e);
        }
        LogService.addEvent("Bot started!");
        Scheduler.setTimer();
    }
}
