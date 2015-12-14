package info.hamster.widgets.line_clock;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import info.hamster.widgets.BaseClockConfig;
import info.hamster.widgets.Config;
import info.hamster.widgets.R;
import info.hamster.widgets.colorpicker.ColorPickerPreference;

/**
 * Created by IntelliJ IDEA.
 * User: Zinetz Victor
 * Date: 15.06.11
 * Time: 22:18
 * To change this template use File | Settings | File Templates.
 */
public class LineClockWidgetConfig extends BaseClockConfig {

    static final String TimeFormatPrefix = "timeFormat_lineClock";

    private int clockActionIndex = -1;
    static final String ClockActionPrefix = "clockActivity_lineClock";
    static final String ClockActivityPrefix = "clockAction_lineClock";
    static final String ClockActionIndex = "clockActionIndex_lineClock";

    private int widgetColor = Color.BLACK;
    static final String WidgetColorPrefix = "widgetColor_lineClock";

    static final String ShowAlarmPrefix = "showAlarms_lineClock";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Config.Log("onCreate for LineClockWidgetConfig");
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.line_clock_config);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

//        CheckBoxPreference checkBox = (CheckBoxPreference) findPreference("timeFormat");
//        checkBox.setChecked(prefs.getBoolean(TimeFormatPrefix + mNewWidgetId, true));

        CheckBoxPreference checkBox = (CheckBoxPreference) findPreference("showAlarms");
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

        final ColorPickerPreference colorPref = ((ColorPickerPreference) findPreference("widgetColor"));
        final int color = prefs.getInt(WidgetColorPrefix + mNewWidgetId, prefs.getInt(WidgetColorPrefix + mNewWidgetId, widgetColor));
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

//        CheckBoxPreference pref = (CheckBoxPreference) findPreference("timeFormat");
//        editor.putBoolean(TimeFormatPrefix + mNewWidgetId, pref.isChecked());

        CheckBoxPreference pref = (CheckBoxPreference) findPreference("showAlarms");
        editor.putBoolean(ShowAlarmPrefix + mNewWidgetId, pref.isChecked());

        if (clockActionIndex >= 0) {
            editor.putString(ClockActivityPrefix + mNewWidgetId, appActivityNames[clockActionIndex]);
            editor.putString(ClockActionPrefix + mNewWidgetId, appPackageNames[clockActionIndex]);
            editor.putInt(ClockActionIndex + mNewWidgetId, clockActionIndex);
        }

        editor.putInt(WidgetColorPrefix + mNewWidgetId, widgetColor);

        editor.commit();
        return super.onOptionsItemSelected(item);
    }
}
