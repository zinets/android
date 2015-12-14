package info.hamster.widgets.wide_clock_4x1;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import info.hamster.widgets.Config;
import info.hamster.widgets.R;

import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: Zinetz Victor
 * Date: 18.02.11
 */
public class WideClock4x1Widget extends info.hamster.widgets.BaseWidgetProvider {

    final static private int[] daysOfWeek = {R.id.idSunDay, R.id.idMonDay, R.id.idTueDay,
            R.id.idWedDay, R.id.idThuDay, R.id.idFriDay, R.id.idSatDay};

    final static private int[] themeColors = {0x9f000000, 0x7f00007f, 0x7f7f0000, 0x7f007f00, 0x4fffffff};

    public void UpdateWidget(Context context) {

        Calendar curDateTime = Calendar.getInstance();
        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        final ComponentName thisWidget = new ComponentName(context, WideClock4x1Widget.class);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        for (int appWidgetId : appWidgetIds) {
            boolean am = (!prefs.getBoolean(WideClock4x1WidgetConfig.TimeFormatPrefix + appWidgetId, true));
            int textColor = 0xffffffff;
            if (prefs.getBoolean(WideClock4x1WidgetConfig.WidgetThemeInversion + appWidgetId, false))
                textColor = 0xff000000;

            int theme = prefs.getInt(WideClock4x1WidgetConfig.WidgetThemePrefix + appWidgetId, 4);
            RemoteViews remoteViews;
            if (Config.LOGGING)
                Config.Log("theme = " + theme);
            switch (theme) {
                case 0:
                    remoteViews = new RemoteViews(context.getPackageName(), R.layout.wide_clock_4x1_transparent);
                    break;
                case 1:
                    remoteViews = new RemoteViews(context.getPackageName(), R.layout.wide_clock_4x1);
                    break;
                case 2:
                    remoteViews = new RemoteViews(context.getPackageName(), R.layout.wide_clock_4x1_pink);
                    break;
                case 3:
                    remoteViews = new RemoteViews(context.getPackageName(), R.layout.wide_clock_4x1_green);
                    break;
                default:
                    remoteViews = new RemoteViews(context.getPackageName(), R.layout.wide_clock_4x1_gray);
                    break;
            }
            if (am) {
                remoteViews.setTextViewText(R.id.idTimeFormat, String.format("%1$tp", curDateTime));
                remoteViews.setTextViewText(R.id.idTime, String.format("%1$tI:%1$tM", curDateTime));
            } else {
                remoteViews.setTextViewText(R.id.idTimeFormat, "");
                remoteViews.setTextViewText(R.id.idTime, String.format("%1$tH:%1$tM", curDateTime));
            }
            remoteViews.setTextColor(R.id.idTimeFormat, textColor);
            remoteViews.setTextColor(R.id.idTime, textColor);

            remoteViews.setTextViewText(R.id.idDayOfMonth, String.format("%1$td", curDateTime));
            remoteViews.setTextColor(R.id.idDayOfMonth, textColor);

            remoteViews.setTextColor(R.id.idMonthYear, themeColors[theme]);
            remoteViews.setTextViewText(R.id.idMonthYear, String.format("/%1$tm/%1$tY", curDateTime));

            int c = curDateTime.get(Calendar.DAY_OF_WEEK);
            for (int i = 0; i < daysOfWeek.length; i++) { // first day of week = 1
                if (i + 1 == c) {
                    remoteViews.setTextColor(daysOfWeek[i], textColor);
                } else
                    remoteViews.setTextColor(daysOfWeek[i], themeColors[theme]);
            }

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
            if (Config.LOGGING)
                Config.Log("WideClock 4x1 #" + appWidgetId + " updated");
        }
    }
}
