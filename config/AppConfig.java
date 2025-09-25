package edu.ccrm.config;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AppConfig {
    private static AppConfig instance;
    private final Path dataFolderPath;
    private final int maxCreditsPerSemester;

    private AppConfig() {
        // Default configuration
        this.dataFolderPath = Paths.get(System.getProperty("user.dir"), "data");
        this.maxCreditsPerSemester = 21;
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            synchronized (AppConfig.class) {
                if (instance == null) {
                    instance = new AppConfig();
                }
            }
        }
        return instance;
    }

    public Path getDataFolderPath() {
        return dataFolderPath;
    }

    public int getMaxCreditsPerSemester() {
        return maxCreditsPerSemester;
    }
}