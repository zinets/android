package info.hamster.widgets.minimal_clock;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import info.hamster.widgets.BaseClockConfig;
import info.hamster.widgets.ColorPickerDialog;
import info.hamster.widgets.Config;
import info.hamster.widgets.R;
import info.hamster.widgets.colorpicker.ColorPickerPreference;

import static info.hamster.widgets.colorpicker.ColorPickerPreference.*;

/**
 * Created by IntelliJ IDEA.
 * User: Zinetz Victor
 * Date: 18.02.11
 */
public class MinimalClockWidgetConfig extends BaseClockConfig {

    // preferences prefixes
    static final String TimeFormatPrefix = "timeFormat_minimalClock";

    private int clockActionIndex = -1;
    static final String ClockActionPrefix = "clockActivity_minimalClock";
    static final String ClockActivityPrefix = "clockAction_minimalClock";
    static final String ClockActionIndex = "clockActionIndex_minimalClock";
    static final String MinsSwapPrefix = "minSwap_minimalClock";
    static final String ShowAlarmPrefix = "showAlarms_minimalClock";

    private int widgetPlaceIndex = -1;
    static final String WidgetPlacePrefix = "widgetPlace_minimalClock";

    private int widgetColor = Color.TRANSPARENT;
    static final String WidgetColorPrefix = "widgetColor_minimalClock";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.minimal_clock_config);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        final CheckBoxPreference timeFormat = (CheckBoxPreference) findPreference("timeFormat");
        timeFormat.setChecked(prefs.getBoolean(TimeFormatPrefix + mNewWidgetId, true));

        final CheckBoxPreference checkBox = (CheckBoxPreference) findPreference("showAlarms");
        checkBox.setChecked(prefs.getBoolean(ShowAlarmPrefix + mNewWidgetId, false));

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

        final ListPreference listWidgetPlaces = (ListPreference) findPreference("widgetPlace");
        listWidgetPlaces.setOnPreferenceChangeListener(
                new Preference.OnPreferenceChangeListener() {
                    public boolean onPreferenceChange(Preference preference, Object o) {
                        widgetPlaceIndex = ((ListPreference) preference).findIndexOfValue(o.toString());
                        return true;
                    }
                }
        );

        final ColorPickerPreference colorPref = ((ColorPickerPreference) findPreference("widgetColor"));
        int color = prefs.getInt(WidgetColorPrefix + mNewWidgetId, Color.BLUE);
        colorPref.setValue(color);

        colorPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            public boolean onPreferenceClick(Preference preference) {
                int color = prefs.getInt(WidgetColorPrefix + mNewWidgetId, Color.BLUE);
                preference.setDefaultValue(new Integer(color));
                return true;
            }

            public boolean onPreferenceChange(Preference preference, Object newValue) {
                widgetColor = Integer.valueOf(String.valueOf(newValue));
                Config.Log("new color = " + widgetColor);
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
        editor.putBoolean(ShowAlarmPrefix + mNewWidgetId, pref.isChecked());

        if (clockActionIndex >= 0) {
            editor.putString(ClockActivityPrefix + mNewWidgetId, appActivityNames[clockActionIndex]);
            editor.putString(ClockActionPrefix + mNewWidgetId, appPackageNames[clockActionIndex]);
            editor.putInt(ClockActionIndex + mNewWidgetId, clockActionIndex);
        }
        if (widgetPlaceIndex != -1)
            editor.putInt(WidgetPlacePrefix + mNewWidgetId, widgetPlaceIndex);
        if (widgetColor != Color.TRANSPARENT)
            editor.putInt(WidgetColorPrefix + mNewWidgetId, widgetColor);

        editor.commit();

        return super.onOptionsItemSelected(item);
    }
}
