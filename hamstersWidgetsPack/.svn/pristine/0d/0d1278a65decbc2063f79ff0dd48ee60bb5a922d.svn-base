package info.hamster.widgets.providers;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import info.hamster.widgets.Config;

/**
 * Created by IntelliJ IDEA.
 * User: Zinetz Victor
 * Date: 22.05.11
 */

public class GMailProvider {

    private Context mContext = null;
    private int[] unreadedMessagesCount;
    private GMailObserver[] observers;

    final Uri gmailUri = Uri.parse("content://gmail-ls/labels/");

    protected void fireEvent() {
        int count = 0;
        for (int i : unreadedMessagesCount) {
            Config.Log("  fireEvent: i = " + i);
            count += i;
        }

        Config.Log("  count = " + count);
        Intent intent = new Intent("info.hamster.widgets.time_to_update");
        intent.putExtra(Config.UnreadedMailEventExtra, count);

        mContext.sendBroadcast(intent);
    }

    private class GMailObserver extends ContentObserver {

        private int index = -1;
        private Uri uri = null;

        public GMailObserver(int i) {
            super(null);
            index = i;
        }

        public void setUri(Uri u) {
            uri = u;
            onChange(true);
        }

        private int getUnreaded() {

            int res = -1;
            try {
                Config.Log("  uri = " + uri);
                Cursor cursor = mContext.getContentResolver().query(
                        uri,
                        new String[]{"canonicalName", "numUnreadConversations"},
                        null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    while (cursor.moveToNext()) {
                        String name = cursor.getString(0);
                        if (name.equals("^i")) {
                            res = cursor.getInt(1);
                            break;
                        }
                    }
                }

            } catch (Exception e) {
                Log.e("GMail Observer", "Unexpected error in getUnreaded()" + e.getLocalizedMessage());
                e.printStackTrace();
                res = -1;
            } finally {
                return res;
            }
        }

        @Override
        public void onChange(boolean selfChange) {

            if (Config.LOGGING)
                Config.Log("gMail provider fired..");
            final int i = getUnreaded();
            Config.Log("  unreaded = " + i + ", in array = " + unreadedMessagesCount[index]);
            if (i != unreadedMessagesCount[index]) {
                unreadedMessagesCount[index] = i;
                fireEvent();
            }
        }
    }

    public GMailProvider(Context context) {
        mContext = context;

        AccountManager mgr = AccountManager.get(mContext);
        Account[] accounts = mgr.getAccountsByType("com.google");
        final ContentResolver cr = context.getContentResolver();

        unreadedMessagesCount = new int[accounts.length];
        observers = new GMailObserver[accounts.length];
        Config.Log("getting mail count..");
        for (int x = 0; x < accounts.length; x++) {
            GMailObserver gmo = new GMailObserver(x);
            Config.Log("  created provider for " + accounts[x].name);

            observers[x] = gmo;
            Uri observerUri = Uri.withAppendedPath(gmailUri, accounts[x].name);
            gmo.setUri(observerUri);
            cr.registerContentObserver(observerUri, true, gmo);
        }
    }

    public void unregisterAll() {
        final ContentResolver cr = mContext.getContentResolver();

        for (GMailObserver gmo : observers) {
            cr.unregisterContentObserver(gmo);
        }
    }
}
