package com.liu.hwkj.baidu.trackutils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import com.liu.hwkj.baidu.trackshow.MonitorService;

public class ServiceUtil {

    private static final int RETRIVE_SERVICE_COUNT = 100;

    private static Intent intent = null;

    private static PendingIntent pendingIntent = null;

    public static boolean isServiceRunning(Context context, String className) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceInfos =
                activityManager.getRunningServices(RETRIVE_SERVICE_COUNT);

        if (null == serviceInfos || serviceInfos.size() < 1) {
            return false;
        }

        for (int i = 0; i < serviceInfos.size(); i++) {
            if (serviceInfos.get(i).service.getClassName().contains(className)) {
                return true;
            }
        }
        return false;
    }

    public static void invokeMonitorService(Context context, int interval) {
        try {
            if (null == intent) {
                intent = new Intent(context, MonitorService.class);
            }

            if (null == pendingIntent) {
                pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            }
        } catch (Exception e) {
            System.out.println("failed to start monitorService");
            return;
        }
        AlarmManager am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
    }

    public static void cancleMonitorService(Context context) {
        if (null != pendingIntent) {
            AlarmManager alarm = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
            alarm.cancel(pendingIntent);
        }
    }

    public static void sendAlarm(Context context, int interval) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent("GATHER_LOCATION");
        PendingIntent alarmIntent =
                PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, alarmIntent);
    }
}
