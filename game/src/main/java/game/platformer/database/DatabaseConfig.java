package game.platformer.database;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class DatabaseConfig {
    private final File CONNECTION_PROPRETIES;

    private final String DATABASE_URL;
    private final String DATABASE_USERNAME;
    private final String DATABASE_PASSWORD;

    public DatabaseConfig() {
        CONNECTION_PROPRETIES = new File("game\\src\\main\\java\\game\\platformer\\database\\databaseConfig.txt");
        final String[] PROPRETIES = retrieveDbInformations();
        DATABASE_URL = PROPRETIES[0];
        DATABASE_USERNAME = PROPRETIES[1];
        DATABASE_PASSWORD = PROPRETIES[2];
    }

    private String[] retrieveDbInformations() {
        String[] data = new String[3];
        int index = 0;
        try (Scanner scanner = new Scanner(CONNECTION_PROPRETIES)) {
            while (scanner.hasNextLine()) {
                data[index] = scanner.nextLine().split("_")[1].trim();
                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public String getDATABASE_URL() {
        return DATABASE_URL;
    }

    public String getDATABASE_USERNAME() {
        return DATABASE_USERNAME;
    }

    public String getDATABASE_PASSWORD() {
        return DATABASE_PASSWORD;
    }
}