package info.hamster.widgets.wide_clock_4x2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import info.hamster.widgets.BaseClockConfig;
import info.hamster.widgets.R;

/**
 * Created by IntelliJ IDEA.
 * User: Zinetz Victor
 * Date: 29.01.11
 */
public class WideClock4x2WidgetConfig extends BaseClockConfig {

    // preferences prefixes
    static final String TimeFormatPrefix = "timeFormat_wide4x2";
    static final String ClockActionPrefix = "clockActivity_wide4x2";
    static final String ClockActivityPrefix = "clockAction_wide4x2";
    static final String DateActionPrefix = "dateActivity_wide4x2";
    static final String DateActivityPrefix = "dateAction_wide4x2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.wide_clock_4x2_config);

        final ListPreference listClockActions = (ListPreference) findPreference("clockAction");
        listClockActions.setEntries(appLabels);
        listClockActions.setEntryValues(appPackageNames);

        final ListPreference listDateActions = (ListPreference) findPreference("dateAction");
        listDateActions.setEntries(appLabels);
        listDateActions.setEntryValues(appPackageNames);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //final SharedPreferences prefs = TimeEventsReceiver.getInstance().mContext.getSharedPreferences("hamsterWidgets", 0);

        listClockActions.setOnPreferenceChangeListener(
                new Preference.OnPreferenceChangeListener() {

                    public boolean onPreferenceChange(Preference preference, Object o) {
                        int i = ((ListPreference) preference).findIndexOfValue(o.toString());

                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(ClockActivityPrefix + mNewWidgetId, appActivityNames[i]);
                        editor.putString(ClockActionPrefix + mNewWidgetId, appPackageNames[i]);
                        editor.commit();

                        return true;
                    }
                }
        );

        listDateActions.setOnPreferenceChangeListener(
                new Preference.OnPreferenceChangeListener() {

                    public boolean onPreferenceChange(Preference preference, Object o) {
                        int i = ((ListPreference) preference).findIndexOfValue(o.toString());
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(DateActivityPrefix + mNewWidgetId, appActivityNames[i]);
                        editor.putString(DateActionPrefix + mNewWidgetId, appPackageNames[i]);
                        editor.commit();

                        return true;
                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        // надо сохранить:
        // 1. формат даты
        final CheckBoxPreference pref = (CheckBoxPreference) findPreference("timeFormat");
        editor.putBoolean(TimeFormatPrefix + mNewWidgetId, pref.isChecked());

        // 2. выбор для тапа по часам
        //      сохраняется выше
        // 3. выбор для тапа по дате
        //      сохраняется выше

        editor.commit();
        return super.onOptionsItemSelected(item);
    }
}
