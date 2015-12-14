package info.hamster.widgets.providers;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.util.Log;
import info.hamster.widgets.Config;

/**
 * Created by IntelliJ IDEA.
 * User: Zinetz Victor
 * Date: 01.05.11
 */
public class MissedSmsProvider extends ContentObserver {

    private Context mContext = null;

    public MissedSmsProvider(Context context) {
        super(null);

        mContext = context;
        this.onChange(true);
    }

    private int getMissedSms() {
        int res = -1;

        try {
            if (mContext != null) {
                Cursor c = mContext.getContentResolver().query(
                        Config.MissedSmsUri, null,
                        "read = ?", new String[]{"0"},
                        "date DESC");
                if (c != null)
                    res = c.getCount();
            }
        } catch (Exception e) {
            Log.e("MissedSmsProvider", "Unexpected exception in getMissedSms: " + e.getLocalizedMessage());
        } finally {
            return res;
        }
    }

    @Override
    public void onChange(boolean selfChange) {

        if (Config.LOGGING)
            Config.Log("MissedSmsProvider fired..");

        final int missedSms = getMissedSms();
        if (mContext != null) {
            Intent intent = new Intent("info.hamster.widgets.time_to_update");
            intent.putExtra(Config.MissedSmsCountExtra, missedSms);
            mContext.sendBroadcast(intent);
        }
        if (Config.LOGGING)
            Config.Log(".. missed sms count - " + missedSms);
    }
}
