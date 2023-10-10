package LogService;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public static List<String> getLog() {
        List<String> log = new ArrayList<>();
        try {
            File file = new File(MY_BOT_LOG_FILE);
            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    log.add(line.trim());
                }
                br.close();
            }
        } catch (IOException e) {
            LogService.addEvent("Getting log error: " + e);
        }
        return log;
    }
    }
