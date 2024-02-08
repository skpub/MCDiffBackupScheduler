package org.sk_dev.mcdiffbackupscheduler;

import org.bukkit.Bukkit;
import org.sk_dev.Cron.Cron;
import org.sk_dev.Cron.CronNavigator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class BackupMain {
    private static Path CONF_FILE = Paths.get("MCDiffBackup.properties");
    private static String DEF_CONF = "0 0 * * *";
    private Cron cron;
    private CronNavigator cronNav;
    public BackupMain() {
        Properties conf = new Properties();
        if (Files.exists(this.CONF_FILE)) {
            // The configuration file exists.
            try {
                String conf_str = Files.readString(this.CONF_FILE);
                this.cron = new Cron(conf_str);
                conf.setProperty("cron", conf_str);
            } catch (IOException e) {
                Bukkit.getLogger().info(String.format(
                    "Can't read the configuration file! " +
                        "The backup process will operates with default settings %s",
                    this.DEF_CONF
                ));
            }
        } else {
            // The configuration file does not exist. Adopt the default setting.
            conf.setProperty("cron", this.DEF_CONF);
            File file = new File(this.CONF_FILE.toString());
            try {
                FileWriter writer = new FileWriter(file);
                writer.write(this.DEF_CONF);
                writer.close();
                Bukkit.getLogger().info(String.format(
                    "The configuration file does not exist." +
                        "Default settings %s written.",
                    this.DEF_CONF
                ));
            } catch (IOException e) {
                Bukkit.getLogger().info(String.format(
                    "Can't save the configuration file! " +
                        "The backup process will operates with default settings %s",
                    this.DEF_CONF
                ));
            }
        }
    }
}
