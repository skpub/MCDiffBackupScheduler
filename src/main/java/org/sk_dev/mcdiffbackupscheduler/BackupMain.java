package org.sk_dev.mcdiffbackupscheduler;

import org.bukkit.Bukkit;
import org.sk_dev.Cron.Cron;
import org.sk_dev.Cron.CronNavigator;

import java.io.File;
import java.io.FileInputStream;
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
            FileInputStream input = null;
            try {
                input = new FileInputStream(this.CONF_FILE.toString());
            } catch (IOException e) {
                Bukkit.getLogger().info(String.format("Can't read the configuration file! "
                    + "The backup process will operates with default settings %s", this.DEF_CONF));
            }
            try {
                conf.load(input);
                this.cron = new Cron(conf.getProperty("cron"));
                Bukkit.getLogger().info(String.format(
                    "The cron configuration (%s) has been set successfully",
                    conf.getProperty("cron")
                ));
            } catch (IOException e) {
                Bukkit.getLogger().info( "Can't load the configuration file."
                    + "The format of the configuration is illegal."
                );
            }
        } else {
            // The configuration file does not exist. Adopt the default setting.
            conf.setProperty("cron", this.DEF_CONF);
            File file = new File(this.CONF_FILE.toString());
            StringBuilder sb = new StringBuilder();
            conf.forEach((k,v) -> sb.append(k.toString() + "=" + v.toString()+ "\n"));
            try {
                // FILE OUT
                FileWriter writer = new FileWriter(file);
                writer.write(sb.toString());
                Bukkit.getLogger().info(conf.toString());
                writer.close();
                // FILE OUT

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
