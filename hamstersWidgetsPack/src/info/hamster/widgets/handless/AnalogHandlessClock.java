package info.hamster.widgets.handless;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.widget.RemoteViews;
import info.hamster.widgets.BaseWidgetProvider;
import info.hamster.widgets.Config;
import info.hamster.widgets.R;
import info.hamster.widgets.line_clock.LineClockWidgetConfig;

import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: zinetz
 * Date: 25.07.11
 */
public class AnalogHandlessClock extends BaseWidgetProvider {

    @Override
    public void UpdateWidget(Context context) {
        Calendar curDateTime = Calendar.getInstance();

        final ComponentName thisWidget = new ComponentName(context, AnalogHandlessClock.class);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.handless_clock);
        final Resources r = context.getResources();
        Bitmap bmp;
        final int hh = curDateTime.get(Calendar.HOUR_OF_DAY);
        final int mm = curDateTime.get(Calendar.MINUTE);
        final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        final float density = displayMetrics.density;

        for (int appWidgetId : appWidgetIds) {

            if (Config.HANDLESS_TYPE == 0) {
                Drawable dial = r.getDrawable(R.drawable.handless_0);
                final int w = (int) (dial.getIntrinsicWidth() * density);
                final int h = (int) (dial.getIntrinsicHeight() * density);
                dial.setBounds(0, 0, w, h);

                bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bmp);
                Paint p = new Paint();
                final int color = Color.RED;
                p.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
                p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));

                canvas.translate(0, 0);
                dial.draw(canvas);
                canvas.save();

                dial = r.getDrawable(R.drawable.handless_1);
                dial.setBounds(0, 0, w, h);
                canvas.translate(w / 2, h / 2);
                canvas.rotate(mm * 6);
                canvas.translate(-w / 2, -h / 2);
                dial.draw(canvas);

                dial = r.getDrawable(R.drawable.handless_2);
                dial.setBounds(0, 0, w, h);

                canvas.restore();
                canvas.translate(w / 2, h / 2);
                canvas.rotate(hh * 30 + mm / 2);
                canvas.translate(-w / 2, -h / 2);
                dial.draw(canvas);

                remoteViews.setImageViewBitmap(R.id.idClockDots, bmp);

                if (missedCallsCount > 0) {
                    remoteViews.setImageViewResource(R.id.idMissed, R.drawable.missed_call_blue);
                } else if (missedSmsCount > 0) {
                    remoteViews.setImageViewResource(R.id.idMissed, R.drawable.missed_sms_blue);
                } else if (unreadedMailCount > 0) {
                    remoteViews.setImageViewResource(R.id.idMissed, R.drawable.missed_mail_blue);
                } else if (calEventsCount > 0) {
                    remoteViews.setImageViewResource(R.id.idMissed, R.drawable.missed_event_blue);
                } else if (alarmsCount > 0) {
                    remoteViews.setImageViewResource(R.id.idMissed, R.drawable.missed_alarm_blue);
                }
            } else if (Config.HANDLESS_TYPE == 1) {
                Bitmap dial = BitmapFactory.decodeResource(r, R.drawable.handless_0);
                final int w = (int) (dial.getWidth() * density);
                final int h = (int) (dial.getHeight() * density);

                bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bmp);
                Paint p = new Paint();
                final int color = Color.BLUE;
                p.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
                p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

                canvas.drawBitmap(dial, new Rect(0, 0, w, h),
                        new Rect(0, 0, w, h), p);
                dial.recycle();
                canvas.save();

                dial = BitmapFactory.decodeResource(r, R.drawable.handless_1);
                canvas.rotate(90);
                canvas.drawBitmap(dial, new Rect(0, 0, w, h),
                        new Rect(0, 0, w, h), p);
                dial.recycle();


                remoteViews.setImageViewBitmap(R.id.idClockDots, bmp);
            }

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }
}
