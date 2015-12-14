package info.hamster.widgets;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Service;
import android.content.*;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import info.hamster.widgets.providers.*;

/**
 * Created by IntelliJ IDEA.
 * User: Zinetz Victor
 * Date: 11.02.11
 */
public class WidgetService extends Service {

    private MissedCallsProvider callsEventsObserver = null;
    private MissedSmsProvider smsEventsObserver = null;
    private BatteryLevelProvider batteryObserver = null;
    private AlarmsProvider alarmObserver = null;
    private CalendarEventsProvider calendarObserver = null;
    private GMailProvider gmailObserver = null;

    private TimeEventsReceiver timeEventsReceiver = null;

    public static boolean ServiceStarted = false;

    private class TimeEventsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Config.Log("TIME_TICK incoming broadcast..");

            if (Config.TESTING) {
            }

            context.sendBroadcast(new Intent("info.hamster.widgets.time_to_update"));

            Config.Log("widgets updated.");
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final Context context = getApplicationContext();
        final ContentResolver cr = context.getContentResolver();

        if (callsEventsObserver == null) {
            callsEventsObserver = new MissedCallsProvider(context);
            cr.registerContentObserver(Config.MissedCallsUri, true, callsEventsObserver);
        }

        if (smsEventsObserver == null) {
            smsEventsObserver = new MissedSmsProvider(context);
            cr.registerContentObserver(Config.MissedSmsUri, true, smsEventsObserver);
            cr.registerContentObserver(Config.MissedMmsSmsUri, true, smsEventsObserver);
        }

        if (timeEventsReceiver == null) {
            timeEventsReceiver = new TimeEventsReceiver();
            registerReceiver(timeEventsReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
            registerReceiver(timeEventsReceiver, new IntentFilter(Intent.ACTION_TIME_CHANGED));
            registerReceiver(timeEventsReceiver, new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED));
            registerReceiver(timeEventsReceiver, new IntentFilter(Intent.ACTION_DATE_CHANGED));
        }

        if (batteryObserver == null) {
            batteryObserver = new BatteryLevelProvider();
            registerReceiver(batteryObserver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            registerReceiver(batteryObserver, new IntentFilter(Intent.ACTION_BATTERY_LOW));
            registerReceiver(batteryObserver, new IntentFilter(Intent.ACTION_BATTERY_OKAY));
            registerReceiver(batteryObserver, new IntentFilter(Intent.ACTION_POWER_CONNECTED));
            registerReceiver(batteryObserver, new IntentFilter(Intent.ACTION_POWER_DISCONNECTED));
        }

        if (alarmObserver == null) {
            alarmObserver = new AlarmsProvider(context);
            cr.registerContentObserver(Config.AlarmUri, true, alarmObserver);
        }

        if (gmailObserver == null) {
            gmailObserver = new GMailProvider(context);
        }

        if (Config.TESTING) {
            if (calendarObserver == null) {
                calendarObserver = new CalendarEventsProvider(context);
                cr.registerContentObserver(Config.CalendarObserverUri(), true, calendarObserver);
            }

        }

        ServiceStarted = true;

        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        final ContentResolver cr = getApplicationContext().getContentResolver();

        if (gmailObserver != null) {
            gmailObserver.unregisterAll();
            gmailObserver = null;
        }

        if (callsEventsObserver != null) {
            cr.unregisterContentObserver(callsEventsObserver);
            callsEventsObserver = null;
        }

        if (smsEventsObserver != null) {
            cr.unregisterContentObserver(smsEventsObserver);
            smsEventsObserver = null;
        }

        if (timeEventsReceiver != null) {
            unregisterReceiver(timeEventsReceiver);
            timeEventsReceiver = null;
        }

        if ((Config.TESTING) && (calendarObserver != null)) {
            cr.unregisterContentObserver(calendarObserver);
            calendarObserver = null;
        }

        if (batteryObserver != null) {
            unregisterReceiver(batteryObserver);
            batteryObserver = null;
        }

        if (alarmObserver != null) {
            cr.unregisterContentObserver(alarmObserver);
            alarmObserver = null;
        }

        if (Config.TESTING) {

        }

        Config.Log("Broadcaster unregistered, service stopping");

        ServiceStarted = false;
        super.onDestroy();
    }
}
