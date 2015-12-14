package info.hamster.widgets.providers;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import info.hamster.widgets.Config;

/**
 * Created by IntelliJ IDEA.
 * User: Zinetz Victor
 * Date: 11.05.11
 */

public class CalendarEventsProvider extends ContentObserver {

    private Context mContext = null;


    public CalendarEventsProvider(Context context) {
        super(null);

        mContext = context;
        this.onChange(true);
    }

    private int getEventsCount() {
        int res = -1;
        try {
            if (mContext != null) {
                final Cursor c = mContext.getContentResolver().
                        query(Config.CalendarObserverUri(), null,
                                "state = ? or state = ?",
                                new String[]{"0", "1"}, null);
                if (c != null)
                    res = c.getCount();
            }
        } catch (Exception e) {
            Log.e("CalendarEventsProvider", "Unexpected exception in getEventsCount: " + e.getLocalizedMessage());
        } finally {
            return res;
        }
    }

    @Override
    public void onChange(boolean selfChange) {

        if (Config.LOGGING)
            Config.Log("CalendarEventsProvider fired..");

        final int count = getEventsCount();
        if (mContext != null) {
            Intent intent = new Intent("info.hamster.widgets.time_to_update");
            intent.putExtra(Config.PendingCalendarEventsExtra, count);

            mContext.sendBroadcast(intent);
        }
        if (Config.LOGGING)
            Config.Log(".. pending calendar events count " + count);
    }
}
