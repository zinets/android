package info.hamster.widgets.providers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import info.hamster.widgets.Config;

/**
 * Created by IntelliJ IDEA.
 * User: Zinetz Victor
 * Date: 01.05.11
 */

public class BatteryLevelProvider extends BroadcastReceiver {

    private int batteryState = BatteryManager.BATTERY_STATUS_UNKNOWN;
    private int batteryLevel = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        final int t1 = batteryLevel;
        final int t2 = batteryState;

        if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {

            final int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            final int state = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            if ((level >= 0) && (state > 0))
                batteryLevel = (level * 100) / state;

            batteryState = intent.getIntExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_UNKNOWN);

            if ((batteryLevel != t1) || (batteryState != t2)) {
                Intent intnt = new Intent("info.hamster.widgets.time_to_update");
                intnt.putExtra(Config.BatteryLevel, batteryLevel);
                intnt.putExtra(Config.BatteryStatus, batteryState);
                context.sendBroadcast(intnt);
            }
        }
    }
}
