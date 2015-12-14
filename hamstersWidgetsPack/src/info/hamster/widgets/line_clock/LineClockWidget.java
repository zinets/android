package info.hamster.widgets.line_clock;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.*;
import android.os.BatteryManager;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RemoteViews;
import info.hamster.widgets.BaseWidgetProvider;
import info.hamster.widgets.Config;
import info.hamster.widgets.R;

import java.security.PrivateKey;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * Created by IntelliJ IDEA.
 * User: Zinetz Victor
 * Date: 14.06.11
 */
public class LineClockWidget extends BaseWidgetProvider {

    final private String[] rusMonths = {"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
            "Июль", "Август", "Сентябрь", "Октыбрь", "Ноябрь", "Декабрь"};
    final private Locale rusLocale = new Locale("ru_RU");

    @Override
    public void UpdateWidget(Context context) {
        final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        final float density = displayMetrics.density;


        Calendar curDateTime = Calendar.getInstance();
        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        final ComponentName thisWidget = new ComponentName(context, LineClockWidget.class);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        for (int appWidgetId : appWidgetIds) {
            boolean am = (!prefs.getBoolean(LineClockWidgetConfig.TimeFormatPrefix + appWidgetId, true));

            int color = prefs.getInt(LineClockWidgetConfig.WidgetColorPrefix + appWidgetId, Color.BLACK);

            int colorA = 0xff;
            int final_color = Color.rgb(Color.red(color), Color.green(color), Color.blue(color));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.line_clock);
            // time
            if (am) {
                remoteViews.setTextViewText(R.id.idTime, String.format("%1$tp%1$tI:%1$tM", curDateTime));
            } else {
                remoteViews.setTextViewText(R.id.idTime, String.format("%1$tH:%1$tM", curDateTime));
            }
            remoteViews.setTextColor(R.id.idTime, final_color);

            // cur_date
            remoteViews.setTextViewText(R.id.idDay3, String.format("%1$te", curDateTime));
            remoteViews.setTextViewText(R.id.idDayW3, String.format("%1$ta", curDateTime));
            remoteViews.setTextColor(R.id.idDay3, final_color);
            remoteViews.setTextColor(R.id.idDayW3, final_color);

            final_color = Color.argb(0xd0, Color.red(color), Color.green(color), Color.blue(color));
            // month
            if (Locale.getDefault().getDisplayLanguage().equals(rusLocale.getDisplayLanguage())) {
                DateFormatSymbols symbols = new DateFormatSymbols(rusLocale);
                symbols.setMonths(rusMonths);
                SimpleDateFormat df = new SimpleDateFormat("MMMM", symbols);

                remoteViews.setTextViewText(R.id.idMonth, df.format(curDateTime.getTime()));
            } else
                remoteViews.setTextViewText(R.id.idMonth, String.format("%1$tB", curDateTime));
            remoteViews.setTextColor(R.id.idMonth, color);
            // year
            remoteViews.setTextViewText(R.id.idYear, String.format("%1$tY", curDateTime));
            remoteViews.setTextColor(R.id.idYear, final_color);

            Calendar temp = Calendar.getInstance();

            final_color = Color.argb(0xa0, Color.red(color), Color.green(color), Color.blue(color));

            // 1
            temp.add(Calendar.DATE, -2);
            remoteViews.setTextViewText(R.id.idDay1, String.format("%1$te", temp));
            remoteViews.setTextViewText(R.id.idDayW1, String.format("%1$ta", temp));
            remoteViews.setTextColor(R.id.idDay1, final_color);
            remoteViews.setTextColor(R.id.idDayW1, final_color);
            // 2
            temp.add(Calendar.DATE, 1);
            remoteViews.setTextViewText(R.id.idDay2, String.format("%1$te", temp));
            remoteViews.setTextViewText(R.id.idDayW2, String.format("%1$ta", temp));
            remoteViews.setTextColor(R.id.idDay2, final_color);
            remoteViews.setTextColor(R.id.idDayW2, final_color);
            // 3
            temp.add(Calendar.DATE, 1);
            // 4
            temp.add(Calendar.DATE, 1);
            remoteViews.setTextViewText(R.id.idDay4, String.format("%1$te", temp));
            remoteViews.setTextViewText(R.id.idDayW4, String.format("%1$ta", temp));
            remoteViews.setTextColor(R.id.idDay4, final_color);
            remoteViews.setTextColor(R.id.idDayW4, final_color);
            // 5
            temp.add(Calendar.DATE, 1);
            remoteViews.setTextViewText(R.id.idDay5, String.format("%1$te", temp));
            remoteViews.setTextViewText(R.id.idDayW5, String.format("%1$ta", temp));
            remoteViews.setTextColor(R.id.idDay5, final_color);
            remoteViews.setTextColor(R.id.idDayW5, final_color);
            // 6
            temp.add(Calendar.DATE, 1);
            remoteViews.setTextViewText(R.id.idDay6, String.format("%1$te", temp));
            remoteViews.setTextViewText(R.id.idDayW6, String.format("%1$ta", temp));
            remoteViews.setTextColor(R.id.idDay6, final_color);
            remoteViews.setTextColor(R.id.idDayW6, final_color);
            // 7
            temp.add(Calendar.DATE, 1);
            remoteViews.setTextViewText(R.id.idDay7, String.format("%1$te", temp));
            remoteViews.setTextViewText(R.id.idDayW7, String.format("%1$ta", temp));
            remoteViews.setTextColor(R.id.idDay7, final_color);
            remoteViews.setTextColor(R.id.idDayW7, final_color);

            Bitmap ico = null;
            if (missedCallsCount > 0) {
                ico = BitmapFactory.decodeResource(context.getResources(), R.drawable.phone_icon);
            } else if (missedSmsCount > 0) {
                ico = BitmapFactory.decodeResource(context.getResources(), R.drawable.chat_icon);
            } else if (unreadedMailCount > 0) {
                ico = BitmapFactory.decodeResource(context.getResources(), R.drawable.mail_icon);
            } else if (calEventsCount > 0) {
                ico = BitmapFactory.decodeResource(context.getResources(), R.drawable.calendar_icon);
            } else if ((alarmsCount > 0) && (prefs.getBoolean(LineClockWidgetConfig.ShowAlarmPrefix + appWidgetId, false))) {
                ico = BitmapFactory.decodeResource(context.getResources(), R.drawable.alarm_icon);
            } else { // battery
                switch (batteryStatus) {
                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        ico = BitmapFactory.decodeResource(context.getResources(), R.drawable.battery_icon);

                        break;
                    default:
                        if (batteryLevel < 20)
                            ico = BitmapFactory.decodeResource(context.getResources(), R.drawable.battery_e_icon);
                        else
                            ico = BitmapFactory.decodeResource(context.getResources(), R.drawable.empty_icon);
                        break;
                }
            }

            if (ico != null) {
                Bitmap bmp = Bitmap.createBitmap((int) (ico.getWidth() * density), (int) (ico.getHeight() * density), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bmp);
                Paint p = new Paint();
                p.setStyle(Paint.Style.FILL);

                p.setColorFilter(new PorterDuffColorFilter(final_color, PorterDuff.Mode.SRC_ATOP));
                p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
                p.setAlpha(0xdf);

                canvas.drawBitmap(ico, 0, 0, p);
                ico.recycle();

                remoteViews.setImageViewBitmap(R.id.idMissedIcon, bmp);
            }


            String action = prefs.getString(LineClockWidgetConfig.ClockActionPrefix + appWidgetId, "");
            if (!action.equals("")) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                ComponentName name = new ComponentName(action, prefs.getString(LineClockWidgetConfig.ClockActivityPrefix + appWidgetId, ""));

                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                intent.setComponent(name);

                PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                remoteViews.setOnClickPendingIntent(R.id.idClockLayout, pendingIntent);
            } else {
                Intent intent = new Intent(context, LineClockWidgetConfig.class);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

                PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                remoteViews.setOnClickPendingIntent(R.id.idClockLayout, pendingIntent);
            }

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }
}
