package game.platformer.database;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Map;

public class User {

    private final static Database DATABASE = new Database();

    private String username;
    private Map<Integer, Timestamp> scores;

    public User(String username) {
        this.username = username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void setScores(Map<Integer, Timestamp> scores) {
        this.scores = scores;
    }

    public Map<Integer, Timestamp> getScores() {
        return this.scores;
    }

    public static boolean logging(String username, String password) {
        int result;
        result = DATABASE.getUserID(username, password);
        return (result != 0) ? true : false;
    }

    public static boolean register(String username, String password) {
        if (DATABASE.register(username, password)) {
            return true;
        } else {
            return false;
        }
    }

    public static Map<Integer, Timestamp> getScore(int index) {
        return DATABASE.getUserData(index);
    }

    public static String hash(String string) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return string;
        }
        byte[] hash = md.digest(string.getBytes());

        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
