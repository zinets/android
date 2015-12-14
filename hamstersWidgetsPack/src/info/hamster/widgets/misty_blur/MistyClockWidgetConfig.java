package info.hamster.widgets.misty_blur;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import info.hamster.widgets.*;

/**
 * Created by IntelliJ IDEA.
 * User: Zinetz Victor
 * Date: 19.02.11
 */
public class MistyClockWidgetConfig extends BaseClockConfig {

    // preferences prefixes
    static final String TimeFormatPrefix = "timeFormat_misty";

    private int themeIndex = -1;
    static final String ThemePrefix = "theme_misty";

    private int clockActionIndex = -1;
    static final String ClockActionPrefix = "clockActivity_misty";
    static final String ClockActivityPrefix = "clockAction_misty";
    static final String ClockActionIndex = "clockActionIndex_misty";

    private int clockColor = Color.TRANSPARENT;
    static final String ClockColorPrefix = "clockColor_misty";

    private int dateColor = Color.TRANSPARENT;
    static final String DateColorPrefix = "dateColor_misty";

    private int weekDayColor = Color.TRANSPARENT;
    static final String WeekDaysColorPrefix = "weekDaysColor_misty";

    private int inactiveDaysColor = Color.TRANSPARENT;
    static final String WeekDaysInactiveColorPrefix = "weekDaysInactiveColor_misty";

    static final String ShowAlarmsPrefix = "showAlarms_misty";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.misty_clock_config);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        CheckBoxPreference checkBox = (CheckBoxPreference) findPreference("timeFormat");
        checkBox.setChecked(prefs.getBoolean(TimeFormatPrefix + mNewWidgetId, true));

        checkBox = (CheckBoxPreference) findPreference("showAlarms");
        checkBox.setChecked(prefs.getBoolean(ShowAlarmsPrefix + mNewWidgetId, false));
        if (Config.LOGGING)
            Config.Log("showAlarms = " + prefs.getBoolean(ShowAlarmsPrefix + mNewWidgetId, false));

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

        Preference colorPref = findPreference("clockColor");
        colorPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                int color = prefs.getInt(ClockColorPrefix + mNewWidgetId, Color.WHITE);
                new ColorPickerDialog(MistyClockWidgetConfig.this, new ColorPickerDialog.OnColorChangedListener() {
                    public void colorChanged(int color) {
                        clockColor = color;
                    }
                }, color).show();
                return true;
            }
        });

        colorPref = findPreference("dateColor");
        colorPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                int color = prefs.getInt(DateColorPrefix + mNewWidgetId, Color.WHITE);
                new ColorPickerDialog(MistyClockWidgetConfig.this, new ColorPickerDialog.OnColorChangedListener() {
                    public void colorChanged(int color) {
                        dateColor = color;
                    }
                }, color).show();
                return true;
            }
        });

        colorPref = findPreference("weekDaysColor");
        colorPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                int color = prefs.getInt(WeekDaysColorPrefix + mNewWidgetId, Color.WHITE);
                new ColorPickerDialog(MistyClockWidgetConfig.this, new ColorPickerDialog.OnColorChangedListener() {
                    public void colorChanged(int color) {
                        weekDayColor = color;
                    }
                }, color).show();
                return true;
            }
        });

        colorPref = findPreference("weekDaysInactiveColor");
        colorPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                int color = prefs.getInt(WeekDaysInactiveColorPrefix + mNewWidgetId, Color.WHITE);
                new ColorPickerDialog(MistyClockWidgetConfig.this, new ColorPickerDialog.OnColorChangedListener() {
                    public void colorChanged(int color) {
                        inactiveDaysColor = color;
                    }
                }, color).show();
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();

        CheckBoxPreference pref = (CheckBoxPreference) findPreference("timeFormat");
        editor.putBoolean(TimeFormatPrefix + mNewWidgetId, pref.isChecked());
        pref = (CheckBoxPreference) findPreference("showAlarms");
        editor.putBoolean(ShowAlarmsPrefix + mNewWidgetId, pref.isChecked());

        if (themeIndex >= 0)
            editor.putInt(ThemePrefix + mNewWidgetId, themeIndex);
        if (clockActionIndex >= 0) {
            editor.putString(ClockActivityPrefix + mNewWidgetId, appActivityNames[clockActionIndex]);
            editor.putString(ClockActionPrefix + mNewWidgetId, appPackageNames[clockActionIndex]);
            editor.putInt(ClockActionIndex + mNewWidgetId, clockActionIndex);
        }
        if (clockColor != Color.TRANSPARENT)
            editor.putInt(ClockColorPrefix + mNewWidgetId, clockColor);
        if (dateColor != Color.TRANSPARENT)
            editor.putInt(DateColorPrefix + mNewWidgetId, dateColor);
        if (weekDayColor != Color.TRANSPARENT)
            editor.putInt(WeekDaysColorPrefix + mNewWidgetId, weekDayColor);
        if (inactiveDaysColor != Color.TRANSPARENT)
            editor.putInt(WeekDaysInactiveColorPrefix + mNewWidgetId, inactiveDaysColor);

        editor.commit();

        if (Config.LOGGING) {
            Config.Log("showAlarms = " + prefs.getBoolean(ShowAlarmsPrefix + mNewWidgetId, false));
        }
        return super.onOptionsItemSelected(item);
    }
}
