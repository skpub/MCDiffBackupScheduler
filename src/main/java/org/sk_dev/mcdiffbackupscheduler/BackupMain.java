package org.sk_dev.mcdiffbackupscheduler;

import dirbackup.DBackup;
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
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Properties;
import java.util.TimerTask;

public class BackupMain {
    private static Path CONF_FILE = Paths.get("MCDiffBackup.properties");
    private static String DEF_CRON = "0 0 * * *";
    private DBackup dbkupWorld;
    private DBackup dbkupNether;
    private DBackup dbkupTheEnd;
    private String destPath;
    private Cron cron;
    private Properties conf;
    private CronNavigator cronNav;
    public BackupMain() {
        this.conf = new Properties();
        if (Files.exists(this.CONF_FILE)) {
            // The configuration file exists.
            FileInputStream input = null;
            try {
                input = new FileInputStream(this.CONF_FILE.toString());
            } catch (IOException e) {
                Bukkit.getLogger().info(String.format("Can't read the configuration file! "
                    + "The backup process will operates with default settings %s", this.DEF_CRON));
            }
            try {
                this.conf.load(input);
                this.cron = new Cron(this.conf.getProperty("cron"));
                this.cronNav = new CronNavigator(this.cron);
                Bukkit.getLogger().info(String.format(
                    "The cron configuration (%s) has been set successfully",
                    this.conf.getProperty("cron")
                ));
            } catch (IOException e) {
                Bukkit.getLogger().info( "Can't load the configuration file."
                    + "The format of the configuration is illegal."
                );
            }
        } else {
            // The configuration file does not exist. Adopt the default setting.
            this.initConf(Optional.empty(), Optional.empty());
            this.cron = new Cron(this.conf.getProperty("cron"));
            this.cronNav = new CronNavigator(this.cron);
            this.writeConf();
        }

        this.dbkupWorld = new DBackup(
            Paths.get("world"),
            Paths.get(this.conf.getProperty("dest_path") + File.separator + "world")
        );
        this.dbkupNether = new DBackup(
            Paths.get("world_nether"),
            Paths.get(this.conf.getProperty("dest_path") + File.separator + "nether")
        );
        this.dbkupWorld = new DBackup(
            Paths.get("world_the_end"),
            Paths.get(this.conf.getProperty("dest_path") + File.separator + "the_end")
        );

        CronScheduler scheduler = new CronScheduler(this.cronNav);
        scheduler.addSchedulerListener(ev -> Bukkit.getLogger().info("now::: " + LocalDateTime.now().toString()));
        scheduler.runScheduler();

        TimerTask task = new TimerTask() {
            @Override
            public void run () {
                BackupMain.this.dbkupWorld.dBackup();
                BackupMain.this.dbkupNether.dBackup();
                BackupMain.this.dbkupTheEnd.dBackup();
            }
        };
    }

    private void initConf(Optional<String> cron, Optional<String> destDir) {
        this.conf.setProperty("cron", cron.isEmpty() ?
            "0 0 * * *":
            cron.get()
        );
        this.conf.setProperty("dest_dir", destDir.isEmpty() ?
            Paths.get("" + File.separator + "MCDiffBackup" + File.separator).toAbsolutePath().toString() :
            destDir.get()
        );
    }
    private String configuration() {
        StringBuilder sb = new StringBuilder();
        this.conf.forEach((k,v) -> sb.append(k.toString() + "=" + v.toString()));
        return sb.toString();
    }

    private void writeConf() {
        String conf = this.configuration();
        File file = new File(this.CONF_FILE.toString());
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(conf);
        } catch (IOException e) {
            Bukkit.getLogger().info("The configuration file does not exist." +
                "Default settings, as shown below are written.\n" +
                "____MCDiffBackup CONFIGURATION____\n" +
                this.configuration() +
                "____EOF____"
            );
        }
    }
}
