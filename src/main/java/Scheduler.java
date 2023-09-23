import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Scheduler {

    private Scheduler() {
    }

    // задаємо час розсилки повідомлення
    private static final LocalTime notificationTime = LocalTime.of(10, 0);

    // задаємо час розсилки повідомлення про плани на завтра
    private static final LocalTime notificationTomorrow = LocalTime.of(20, 0);

    public static void setTimer() {
        int initialHour = LocalDateTime.now().getHour();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, initialHour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 1);

        TimerTask timerTaskSendNotification = new TimerTask() {
            @Override
            public void run() {
                sendUsersNotifications();
            }
        };

        Timer timer = new Timer();
        timer.schedule(
                timerTaskSendNotification,
                calendar.getTime(),
                60000 // wait one minute and check
        );

    }

    public static void sendUsersNotifications() {
        MessageHandler messageHandler = new MessageHandler();

        int currentHour = LocalTime.now().getHour();
        int currentMinute = LocalTime.now().getMinute();
        int notificationHour = notificationTime.getHour();
        int notificationMinute = notificationTime.getMinute();
        int tomorrowNotificationHour = notificationTomorrow.getHour();
        int tomorrowNotificationMinute = notificationTomorrow.getMinute();

        List<String> chatIds = BotData.getChatIds();

        if (currentHour == notificationHour && currentMinute == notificationMinute) {
            System.out.println("It is " + LocalDateTime.now().withNano(0) + "! Sending plans for today...");
            chatIds.forEach(chatId -> {
                try {
                    messageHandler.sendDailyMessage(chatId);
                } catch (IOException e) {
                    System.out.println("sendUsersNotifications Error: " + e);
                }
            });
        } else if (currentHour == tomorrowNotificationHour && currentMinute == tomorrowNotificationMinute) {
            System.out.println("It is " + LocalDateTime.now().withNano(0) + "! Sending plans for tomorrow...");
            chatIds.forEach(messageHandler::sendMessageForTomorrow);
        } else {
            System.out.println("It is " + LocalDateTime.now().withNano(0) + " ..not now. Tic tac.. ");
        }
    }
}
