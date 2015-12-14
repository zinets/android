package info.hamster.widgets.minimal_clock;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.*;
import android.os.BatteryManager;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.widget.RemoteViews;
import info.hamster.widgets.BaseWidgetProvider;
import info.hamster.widgets.Config;
import info.hamster.widgets.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: Zinetz Victor
 * Date: 18.02.11
 */
public class MinimalClockWidget2 extends BaseWidgetProvider {

    public void UpdateWidget(Context context) {

        Calendar curDateTime = Calendar.getInstance();

        final int mm = curDateTime.get(Calendar.MINUTE);

        final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        final float density = displayMetrics.density;

        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        final ComponentName thisWidget = new ComponentName(context, MinimalClockWidget2.class);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        Resources r = context.getResources();
        final Bitmap digits = BitmapFactory.decodeResource(r, R.drawable.digits);
        final int w = digits.getWidth();
        final int h = digits.getHeight() / 10;
        Config.Log("h = " + digits.getHeight() + " / " + h);
        int lOffset;

        for (int appWidgetId : appWidgetIds) {
            final boolean am = (!prefs.getBoolean(MinimalClockWidgetConfig.TimeFormatPrefix + appWidgetId, true));
            final int widgetPlace = prefs.getInt(MinimalClockWidgetConfig.WidgetPlacePrefix + appWidgetId, 0);
            int color = prefs.getInt(MinimalClockWidgetConfig.WidgetColorPrefix + appWidgetId, Color.BLUE);

            int colorA = Color.alpha(color);
            if (colorA == 0xff)
                colorA = 220;
            color = Color.rgb(Color.red(color), Color.green(color), Color.blue(color));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.minimalitic2);

            Bitmap bmp = null;
            bmp = Bitmap.createBitmap((int) (160 * density), (int) (220 * density), Bitmap.Config.ARGB_8888);
            lOffset = widgetPlace * (int) (bmp.getWidth() - 2 * w);

            Canvas canvas = new Canvas(bmp);
            Paint p = new Paint();
//            canvas.drawRect(new Rect(0, 0, bmp.getWidth(), bmp.getHeight()), p);
            p.setAntiAlias(true);
            p.setSubpixelText(true);
            p.setStyle(Paint.Style.FILL);
            p.setTextSize((int) (14 * density));

            p.setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP));
            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
            int dh;
            // часы
            if (am) {   // String.format("%1$tp", curDateTime)
                final int hh = curDateTime.get(Calendar.HOUR);
                dh = hh / 10;
                Rect srcRect = new Rect(0, dh * h, w, (dh + 1) * h);
                Config.Log("src: " + srcRect.toString());
                canvas.drawBitmap(digits, srcRect, new Rect(lOffset, 0, lOffset + w, h), p);
                dh = hh % 10;
                srcRect = new Rect(0, dh * h, w, (dh + 1) * h);
                canvas.drawBitmap(digits, srcRect, new Rect(lOffset + w, 0, lOffset + 2 * w, h), p);
                Config.Log("src: " + srcRect.toString());

                final String pm = String.format("%1$tp", curDateTime).toUpperCase();
                final Rect rect = new Rect();
                p.getTextBounds(pm, 0, pm.length(), rect);
                p.setColorFilter(null);
                p.setColor(Color.WHITE);
                if (widgetPlace == 0)
                    canvas.drawText(pm, (int) ((bmp.getWidth() - (rect.right - rect.left) - 2)), (int) (density * (rect.bottom - rect.top)), p);
                else
                    canvas.drawText(pm, (int) (density * 2), (int) (density * (rect.bottom - rect.top)), p);
            } else {
                final int hh = curDateTime.get(Calendar.HOUR_OF_DAY);
                dh = hh / 10;
                canvas.drawBitmap(digits, new Rect(0, dh * h, w, (dh + 1) * h), new Rect(lOffset, 0, lOffset + w, h), p);
                dh = hh % 10;
                canvas.drawBitmap(digits, new Rect(0, dh * h, w, (dh + 1) * h), new Rect(lOffset + w, 0, lOffset + 2 * w, h), p);
            }

            p.setTextSize((int) (18 * density));
            final int shift = (int) (60 * density);
            p.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));

            p.setAlpha(colorA);
            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

            if (prefs.getBoolean(MinimalClockWidgetConfig.MinsSwapPrefix + appWidgetId, false)) {
                if (lOffset > 0)
                    lOffset = 0;
                else
                    lOffset = bmp.getWidth() - 2 * w;
            }

            dh = mm / 10;
            canvas.drawBitmap(digits, new Rect(0, dh * h, w, (dh + 1) * h), new Rect(lOffset, shift, lOffset + w, shift + h), p);
            dh = mm % 10;
            canvas.drawBitmap(digits, new Rect(0, dh * h, w, (dh + 1) * h), new Rect(lOffset + w, shift, lOffset + 2 * w, shift + h), p);

            p.setColorFilter(null);
            p.setColor(Color.WHITE);

            Bitmap ico = null;
            if (missedCallsCount > 0) {
                ico = BitmapFactory.decodeResource(context.getResources(), R.drawable.phone_icon);
            } else if (missedSmsCount > 0) {
                ico = BitmapFactory.decodeResource(context.getResources(), R.drawable.chat_icon);
            } else if (unreadedMailCount > 0) {
                ico = BitmapFactory.decodeResource(context.getResources(), R.drawable.mail_icon);
            } else if (calEventsCount > 0) {
                ico = BitmapFactory.decodeResource(context.getResources(), R.drawable.calendar_icon);
            } else if ((alarmsCount > 0) && (prefs.getBoolean(MinimalClockWidgetConfig.ShowAlarmPrefix + appWidgetId, false))) {
                ico = BitmapFactory.decodeResource(context.getResources(), R.drawable.alarm_icon);
            } else { // battery
                switch (batteryStatus) {
                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        ico = BitmapFactory.decodeResource(context.getResources(), R.drawable.battery_icon);

                        break;
                    default:
                        if (batteryLevel < 20)
                            ico = BitmapFactory.decodeResource(context.getResources(), R.drawable.battery_e_icon);
                        break;
                }
            }

            float textLeft;
            final float textLeftPadding = 3 * density;
            float textTop = 199 * density;
            float lineLeft;
            final float lineLeftPadding = 2 * density;
            float icoLeft = 0, icoTop = 0;

            final float topOffset = density * 182;

            if (widgetPlace == 0) {
                p.setTextAlign(Paint.Align.LEFT);
                lineLeft = lineLeftPadding;
                textLeft = lineLeft + lineLeftPadding;
                if (ico != null) {
                    textLeft += ico.getWidth() + 2 * textLeftPadding;
                    icoLeft = lineLeft + 2 * lineLeftPadding;
                    icoTop = topOffset + ((bmp.getHeight() - topOffset) - ico.getHeight()) / 2;
                }
            } else {
                p.setTextAlign(Paint.Align.RIGHT);
                lineLeft = bmp.getWidth() - lineLeftPadding;
                textLeft = lineLeft - lineLeftPadding;

                if (ico != null) {
                    textLeft -= (ico.getWidth() + 2 * textLeftPadding);
                    icoLeft = lineLeft - 2 * lineLeftPadding - ico.getWidth();
                    icoTop = topOffset + ((bmp.getHeight() - topOffset) - ico.getHeight()) / 2;
                }
            }

            if (ico != null) {
                canvas.drawBitmap(ico, icoLeft, icoTop, p);
                ico.recycle();
            }

            canvas.drawText(String.format("%1$tA", curDateTime), textLeft, textTop, p);
            textTop += 17 * density;
            canvas.drawText(String.format("%1$te %1$tB", curDateTime), textLeft, textTop, p);

            p.setColor(color);
            canvas.drawLine(lineLeft, topOffset, lineLeft, bmp.getHeight(), p);

            remoteViews.setImageViewBitmap(R.id.idClock, bmp);

            String action = prefs.getString(MinimalClockWidgetConfig.ClockActionPrefix + appWidgetId, "");
            if (!action.equals("")) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                ComponentName name = new ComponentName(action, prefs.getString(MinimalClockWidgetConfig.ClockActivityPrefix + appWidgetId, ""));

                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                intent.setComponent(name);

                PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                remoteViews.setOnClickPendingIntent(R.id.idClock, pendingIntent);
            } else {
                Intent intent = new Intent(context, MinimalClockWidgetConfig.class);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

                PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                remoteViews.setOnClickPendingIntent(R.id.idClock, pendingIntent);
            }

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }

        digits.recycle();
    }
}
