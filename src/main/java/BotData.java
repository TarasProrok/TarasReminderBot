import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BotData {

    private static final String USERS_FILE = "users.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public BotData() {
    }

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
            System.out.println("Gettin chatIds error: " + e);
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
                System.out.println("Saving chatId error: " + e);
            }
        }
    }
}
