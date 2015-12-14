package info.hamster.widgets.providers;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.util.Log;
import info.hamster.widgets.Config;

import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: Zinetz Victor
 * Date: 03.05.11
 */
public class AlarmsProvider extends ContentObserver {

    private Context mContext = null;
    private int alarmsCount = -1;
    private String nearAlarm = null;

    public AlarmsProvider(Context context) {
        super(null);

        mContext = context;
        this.onChange(true);
    }

    private boolean getAlarmCount() {
        boolean res = false;

        try {
            alarmsCount = 0;
            nearAlarm = "";
            if (mContext != null) {
                Cursor c = mContext.getContentResolver().query(
                        Config.AlarmUri, null,
                        "enabled=?",
                        new String[]{"1"},
                        null);
                if (c != null) {
                    alarmsCount = c.getCount();

                    res = alarmsCount > 0;
                    if (res) {
                        c.moveToFirst();

                        int hour = c.getInt(c.getColumnIndexOrThrow("hour"));
                        int minute = c.getInt(c.getColumnIndexOrThrow("minutes"));
                        nearAlarm = String.format("%1$02d:%2$02d", hour, minute);
                    }
                    ;
                }
            }
        } catch (Exception e) {
            Log.e("AlarmsProvider", "Unexpected exception in getAlarmCount: " + e.getLocalizedMessage());
        } finally {
            return res;
        }
    }

    @Override
    public void onChange(boolean selfChange) {

        if (Config.LOGGING)
            Config.Log("AlarmsProvider fired..");

        if (getAlarmCount() && (mContext != null)) {
            Intent intent = new Intent("info.hamster.widgets.time_to_update");
            intent.putExtra(Config.AlarmsCountExtra, alarmsCount);
            intent.putExtra(Config.NearestAlarmExtra, nearAlarm);
            mContext.sendBroadcast(intent);
        }
        if (Config.LOGGING)
            Config.Log(".. pending alarms count - " + alarmsCount + ", nearest @ " + nearAlarm);
    }
}
