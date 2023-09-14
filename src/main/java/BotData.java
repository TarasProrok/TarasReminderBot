import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BotData {
    private static final String USERS_FILE = "users.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    static List<String> getChatIds() {
        List<String> chatIds = new ArrayList<>();
        try {
            File file = new File(USERS_FILE);
            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    chatIds.add(line.trim());
                }
                br.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return chatIds;
    }

    public static void saveChatId(String chatId) {
        List<String> chatIds = getChatIds();
        if (!chatIds.contains(chatId)) {
            try {
                FileWriter writer = new FileWriter(USERS_FILE, true);
                writer.write(chatId + "\n");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
