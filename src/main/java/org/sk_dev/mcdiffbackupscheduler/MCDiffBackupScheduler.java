package org.sk_dev.mcdiffbackupscheduler;

import org.bukkit.plugin.java.JavaPlugin;

public final class MCDiffBackupScheduler extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        BackupMain main = new BackupMain();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
