import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ChatIdService {

    private static final String USERS_FILE = "users.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public ChatIdService() {
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

//    public static void deleteChatId(String chatId) throws IOException {
//        File inFile = new File(USERS_FILE);
//        File tempFile = new File("temp.json");
//        PrintWriter out = new PrintWriter(new FileWriter(tempFile));
//
//        Files.lines(inFile.toPath())
//                .filter(line -> !line.contains(chatId))
//                .forEach(out::println);
//        out.flush();
//        out.close();
//        tempFile.renameTo(new File(USERS_FILE));
//        System.out.println("ID: " + chatId + " deleted successfully");
//    }
}
