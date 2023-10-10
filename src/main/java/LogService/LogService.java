package LogService;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class LogService {

    private LogService() {
    }

    private static final String MY_BOT_LOG_FILE = "logg.txt";

    public static void addEvent(String event) {

        LocalDateTime timestamp = LocalDateTime.now().withNano(0);

            try {
                FileWriter writer = new FileWriter(MY_BOT_LOG_FILE, true);
                writer.write(timestamp + " " + event + "\n");
                writer.close();
            } catch (IOException e) {
                LogService.addEvent("Adding log event error: " + e);
            }
        }
    }
