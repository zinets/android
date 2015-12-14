package info.hamster.widgets.analog_clock;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import info.hamster.widgets.BaseClockConfig;
import info.hamster.widgets.Config;
import info.hamster.widgets.R;
import info.hamster.widgets.ThemeSelector;

/**
 * Created by IntelliJ IDEA.
 * User: Zinetz Victor
 * Date: 10.04.11
 */

public class AnalogWidgetConfig extends BaseClockConfig {

    private int themeIndex = -1;
    static final String ThemePrefix = "themePrefix_analog";

    private int clockActionIndex = -1;
    static final String ClockActionPrefix = "clockActivity_analog";
    static final String ClockActivityPrefix = "clockAction_analog";
    static final String ClockActionIndex = "clockActionIndex_analog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.analog_config);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        final ThemeSelector themePreference = (ThemeSelector) findPreference("widgetTheme");
        themePreference.setOnPreferenceChangeListener(
                new ThemeSelector.OnPreferenceChangeListener() {
                    public boolean onPreferenceChange(Preference preference, Object o) {
                        themeIndex = (Integer) o;
                        return true;
                    }
                }
        );

        final ListPreference listClockActions = (ListPreference) findPreference("clockAction");
        listClockActions.setOnPreferenceChangeListener(
                new Preference.OnPreferenceChangeListener() {
                    public boolean onPreferenceChange(Preference preference, Object o) {
                        clockActionIndex = ((ListPreference) preference).findIndexOfValue(o.toString());
                        return true;
                    }
                }
        );

        listClockActions.setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    public boolean onPreferenceClick(Preference preference) {
                        ((ListPreference) preference).setValueIndex(prefs.getInt(ClockActionIndex + mNewWidgetId, 0));
                        return true;
                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();

        Config.Log("Analog theme index = " + themeIndex);
        if (themeIndex >= 0)
            editor.putInt(ThemePrefix + mNewWidgetId, themeIndex);

        if (clockActionIndex >= 0) {
            editor.putString(ClockActivityPrefix + mNewWidgetId, appActivityNames[clockActionIndex]);
            editor.putString(ClockActionPrefix + mNewWidgetId, appPackageNames[clockActionIndex]);
            editor.putInt(ClockActionIndex + mNewWidgetId, clockActionIndex);
        }

        editor.commit();
        return super.onOptionsItemSelected(item);
    }
}
