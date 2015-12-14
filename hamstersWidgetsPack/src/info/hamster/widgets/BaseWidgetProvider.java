package info.hamster.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.widget.Toast;
import info.hamster.widgets.analog_clock.AnalogClockWidget;
import info.hamster.widgets.compact_clock.CompactClockWidget;
import info.hamster.widgets.line_clock.LineClockWidget;
import info.hamster.widgets.minimal_clock.MinimalClockWidget2;
import info.hamster.widgets.misty_blur.MistyClockWidget;
import info.hamster.widgets.test.TestWidget;
import info.hamster.widgets.wall_clock.WallClockWidget;
import info.hamster.widgets.wide_clock_4x1.WideClock4x1Widget;
import info.hamster.widgets.wide_clock_4x2.WideClock4x2Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Zinetz Victor
 * Date: 11.02.11
 */

abstract public class BaseWidgetProvider extends AppWidgetProvider {

    abstract public void UpdateWidget(Context context);

    protected static int missedCallsCount = 0;
    protected static String missedCallTime = "";
    protected static int missedSmsCount = 0;
    protected static int batteryLevel = 0;
    protected static int batteryStatus = BatteryManager.BATTERY_STATUS_UNKNOWN;
    protected static int alarmsCount = 0;
    protected static String alarmNearest = "";
    protected static int calEventsCount = 0;
    protected static int unreadedMailCount = 0;

    @Override
    public void onReceive(Context context, Intent intent) {

        super.onReceive(context, intent);

        if (intent.getAction().equals("info.hamster.widgets.time_to_update")) {

            int i = intent.getIntExtra(Config.MissedCallsCountExtra, -1);
            if (i > -1) missedCallsCount = i;
            String s = intent.getStringExtra(Config.MissedCallTimeExtra);
            if ((s != null) && !s.equals(""))
                missedCallTime = s;
            s = null;
            i = intent.getIntExtra(Config.MissedSmsCountExtra, -1);
            if (i > -1) missedSmsCount = i;
            i = intent.getIntExtra(Config.BatteryLevel, -1);
            if (i > -1) batteryLevel = i;
            i = intent.getIntExtra(Config.BatteryStatus, -1);
            if (i > -1) batteryStatus = i;
            i = intent.getIntExtra(Config.AlarmsCountExtra, -1);
            if (i > -1) alarmsCount = i;
            s = intent.getStringExtra(Config.NearestAlarmExtra);
            if ((s != null) && !s.equals(""))
                alarmNearest = s;
            i = intent.getIntExtra(Config.PendingCalendarEventsExtra, -1);
            if (i > -1) calEventsCount = i;
            i = intent.getIntExtra(Config.UnreadedMailEventExtra, -1);
            if (i > -1) unreadedMailCount = i;

            UpdateWidget(context);
        }
    }

    @Override
    public void onEnabled(Context context) {

        Config.Log("BaseWidgetProvider onUpdate");

        if (!WidgetService.ServiceStarted)
            context.startService(new Intent(context, WidgetService.class));

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (prefs.getBoolean("first_start", true)) {
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, R.string.widget_hint, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("first_start", false);
            editor.commit();
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        if (!WidgetService.ServiceStarted)
            context.startService(new Intent(context, WidgetService.class));

        if (Config.LOGGING)
            Config.Log("onUpdate, sending Broadcast");

        UpdateWidget(context);
    }

    @Override
    public void onDisabled(Context context) {

        int widgetCount = 0;
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        ComponentName thisWidget = new ComponentName(context, WideClock4x2Widget.class);
        widgetCount += appWidgetManager.getAppWidgetIds(thisWidget).length;

        thisWidget = new ComponentName(context, WideClock4x1Widget.class);
        widgetCount += appWidgetManager.getAppWidgetIds(thisWidget).length;

        thisWidget = new ComponentName(context, WallClockWidget.class);
        widgetCount += appWidgetManager.getAppWidgetIds(thisWidget).length;

        thisWidget = new ComponentName(context, MistyClockWidget.class);
        widgetCount += appWidgetManager.getAppWidgetIds(thisWidget).length;

        thisWidget = new ComponentName(context, MinimalClockWidget2.class);
        widgetCount += appWidgetManager.getAppWidgetIds(thisWidget).length;

        thisWidget = new ComponentName(context, CompactClockWidget.class);
        widgetCount += appWidgetManager.getAppWidgetIds(thisWidget).length;

        thisWidget = new ComponentName(context, AnalogClockWidget.class);
        widgetCount += appWidgetManager.getAppWidgetIds(thisWidget).length;

        thisWidget = new ComponentName(context, LineClockWidget.class);
        widgetCount += appWidgetManager.getAppWidgetIds(thisWidget).length;

        if (info.hamster.widgets.Config.TESTING) {
            thisWidget = new ComponentName(context, TestWidget.class);
            widgetCount += appWidgetManager.getAppWidgetIds(thisWidget).length;
        }

        if (widgetCount == 0)
            context.stopService(new Intent(context, WidgetService.class));
    }
}
