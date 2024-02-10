package org.sk_dev.mcdiffbackupscheduler;

import java.util.EventListener;
import java.util.EventObject;

public interface CronSchedulerListener extends EventListener {
    public void signal(EventObject ev);
}
