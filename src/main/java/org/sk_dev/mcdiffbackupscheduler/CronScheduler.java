package org.sk_dev.mcdiffbackupscheduler;

import org.bukkit.Bukkit;
import org.sk_dev.Cron.CronNavigator;

import javax.swing.event.EventListenerList;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.EventObject;

public class CronScheduler {
    EventListenerList evList;
    CronNavigator nav;

    public void addSchedulerListener(CronSchedulerListener listener) {
        evList.add(CronSchedulerListener.class, listener);
    }

    public void removeSchedulerListener(CronSchedulerListener listener) {
        evList.remove(CronSchedulerListener.class, listener);
    }

    protected void fireBkup(Object source) {
        EventObject ev = new EventObject(source);
        CronSchedulerListener[] listeners = this.evList.getListeners(CronSchedulerListener.class);

        for (CronSchedulerListener listener: listeners) {
            listener.signal(ev);
        }
    }
    public CronScheduler(CronNavigator nav) {
        this.evList = new EventListenerList();
        this.nav = nav;
    }

    public void runScheduler() {
        while (true) {
            Bukkit.getLogger().info("next: " + this.nav.getDateTime());
            Bukkit.getLogger().info("sleep Mills: " + Duration.between(
                LocalDateTime.now(),
                this.nav.getDateTime()
            ).toMillis());
            try {
                Thread.sleep(Duration.between(LocalDateTime.now(), this.nav.getDateTime()).toMillis());
            } catch (InterruptedException e) {
                Bukkit.getLogger().info("BUG");
            }
            fireBkup("bkup");
            this.nav.tick();
        }
    }
}
