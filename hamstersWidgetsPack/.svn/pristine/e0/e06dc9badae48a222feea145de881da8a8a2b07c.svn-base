package info.hamster.widgets.compact_clock;

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
 * Date: 18.02.11
 */
public class CompactClockWidgetConfig extends BaseClockConfig {

    // preferences prefixes
    static final String TimeFormatPrefix = "timeFormat_compactClock";

    private int clockActionIndex = -1;
    static final String ClockActionPrefix = "clockActivity_compactClock";
    static final String ClockActivityPrefix = "clockAction_compactClock";
    static final String ClockActionIndex = "clockActionIndex_misty";

    private boolean lightTheme = true;
    static final String WidgetLightTheme = "theme_compactClock";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.compact_clock_config);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        CheckBoxPreference boolPref = (CheckBoxPreference) findPreference("timeFormat");
        boolPref.setChecked(prefs.getBoolean(TimeFormatPrefix + mNewWidgetId, true));

        ListPreference listPref = (ListPreference) findPreference("clockAction");
        listPref.setEntries(appLabels);
        listPref.setEntryValues(appPackageNames);

        listPref.setOnPreferenceChangeListener(
                new Preference.OnPreferenceChangeListener() {
                    public boolean onPreferenceChange(Preference preference, Object o) {
                        clockActionIndex = ((ListPreference) preference).findIndexOfValue(o.toString());
                        return true;
                    }
                }
        );

        boolPref = (CheckBoxPreference) findPreference("widgetLightTheme");
        boolPref.setChecked(prefs.getBoolean(WidgetLightTheme + mNewWidgetId, true));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();

        CheckBoxPreference pref = (CheckBoxPreference) findPreference("timeFormat");
        editor.putBoolean(TimeFormatPrefix + mNewWidgetId, pref.isChecked());

        pref = (CheckBoxPreference) findPreference("widgetLightTheme");
        editor.putBoolean(WidgetLightTheme + mNewWidgetId, pref.isChecked());

        if (clockActionIndex >= 0) {
            editor.putString(ClockActivityPrefix + mNewWidgetId, appActivityNames[clockActionIndex]);
            editor.putString(ClockActionPrefix + mNewWidgetId, appPackageNames[clockActionIndex]);
            editor.putInt(ClockActionIndex + mNewWidgetId, clockActionIndex);
        }
        editor.commit();
        return super.onOptionsItemSelected(item);
    }
}
