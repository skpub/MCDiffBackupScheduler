package org.sk_dev.mcdiffbackupscheduler;

import org.bukkit.plugin.java.JavaPlugin;

public final class MCDiffBackupScheduler extends JavaPlugin {
    private BackupMain mainThread;

    @Override
    public void onEnable() {
        this.mainThread = new BackupMain();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
