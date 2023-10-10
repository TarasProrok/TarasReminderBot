package Services;

import LogService.LogService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    private static final String AUTH_USERS_FILE = "users.json";
    private static final String ALL_USERS_FILE = "all-users.json";

    private UserService() {
    }

    public static List<String> getAuthUsers() {
        List<String> chatIds = new ArrayList<>();
        try {
            File file = new File(AUTH_USERS_FILE);
            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    chatIds.add(line.trim());
                }
                br.close();
            }
        } catch (IOException e) {
            LogService.addEvent("Getting chatIds error: " + e);
        }
        return chatIds;
    }

    public static void addUser(String chatId) {
        List<String> chatIds = getAllUsers();
        if (!chatIds.contains(chatId)) {
            try {
                FileWriter writer = new FileWriter(ALL_USERS_FILE, true);
                writer.write(chatId + "\n");
                writer.close();
            } catch (IOException e) {
                LogService.addEvent("Adding new user error: " + e);
            }
        }
    }
    public static List<String> getAllUsers() {
        List<String> allUsers = new ArrayList<>();
        try {
            File file = new File(ALL_USERS_FILE);
            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    allUsers.add(line.trim());
                }
                br.close();
            }
        } catch (IOException e) {
            LogService.addEvent("Getting all users error: " + e);
        }
        return allUsers;
    }
}
