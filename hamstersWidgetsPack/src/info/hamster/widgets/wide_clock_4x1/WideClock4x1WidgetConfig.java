package info.hamster.widgets.wide_clock_4x1;

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
public class WideClock4x1WidgetConfig extends BaseClockConfig {

    // preferences prefixes
    static final String TimeFormatPrefix = "timeFormat_wide4x1";


    static final String ClockActionPrefix = "clockActivity_wide4x1";
    static final String ClockActivityPrefix = "clockAction_wide4x1";
    static final String ClockActionIndex = "clockActionIndex_wide4x1";

    static final String DateActionPrefix = "dateActivity_wide4x1";
    static final String DateActivityPrefix = "dateAction_wide4x1";
    static final String DateActionIndex = "dateActionIndex_wide4x1";

    static final String WidgetThemeInversion = "color_invertion_wide4x1";

    private int themeIndex = -1;
    static final String WidgetThemePrefix = "theme_wide4x1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.wide_clock_4x1_config);

        final ListPreference listClockActions = (ListPreference) findPreference("clockAction");
        listClockActions.setEntries(appLabels);
        listClockActions.setEntryValues(appPackageNames);

        final ListPreference listDateActions = (ListPreference) findPreference("dateAction");
        listDateActions.setEntries(appLabels);
        listDateActions.setEntryValues(appPackageNames);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

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

        final ListPreference listWidgetThemes = (ListPreference) findPreference("widgetTheme");
        listWidgetThemes.setOnPreferenceChangeListener(
                new Preference.OnPreferenceChangeListener() {
                    public boolean onPreferenceChange(Preference preference, Object o) {

                        int i = ((ListPreference) preference).findIndexOfValue(o.toString());
                        SharedPreferences.Editor editor = prefs.edit();

                        editor.putInt(WidgetThemePrefix + mNewWidgetId, i);
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

        CheckBoxPreference pref = (CheckBoxPreference) findPreference("timeFormat");
        editor.putBoolean(TimeFormatPrefix + mNewWidgetId, pref.isChecked());

        pref = (CheckBoxPreference) findPreference("widgetInvertedColor");
        editor.putBoolean(WidgetThemeInversion + mNewWidgetId, pref.isChecked());

        editor.commit();
        return super.onOptionsItemSelected(item);
    }


}
