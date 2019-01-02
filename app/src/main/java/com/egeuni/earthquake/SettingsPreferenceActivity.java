package com.egeuni.earthquake;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;

public class SettingsPreferenceActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String KEY_LIST_PREFERENCE = "listPref";
    public static final String KEY_LIST_MAG_PREFERENCE = "listPrefMag";
    public static final String KEY_LIST_DEPTH_PREFERENCE = "listPrefDepth";
    public static final String KEY_SWITCH_PREFERENCE = "autoUpdate";
    private ListPreference mListPreference;
    private ListPreference mListPreferenceMag;
    private SwitchPreference mSwitchPreference;
    private ListPreference mListPreferenceDepth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        mListPreference = (ListPreference) getPreferenceScreen().findPreference(KEY_LIST_PREFERENCE);
        mListPreferenceMag = (ListPreference) getPreferenceScreen().findPreference(KEY_LIST_MAG_PREFERENCE);
        mSwitchPreference = (SwitchPreference) getPreferenceScreen().findPreference(KEY_SWITCH_PREFERENCE);
        mListPreferenceDepth = (ListPreference) getPreferenceScreen().findPreference(KEY_LIST_DEPTH_PREFERENCE);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(KEY_LIST_PREFERENCE) || key.equals(KEY_LIST_MAG_PREFERENCE) || key.equals(KEY_LIST_DEPTH_PREFERENCE)) {
            if(key.equals(KEY_LIST_PREFERENCE)) {
                mListPreference.setSummary(mListPreference.getEntry().toString());
            }
            if(key.equals(KEY_LIST_MAG_PREFERENCE)) {
                mListPreferenceMag.setSummary(mListPreferenceMag.getEntry().toString());
            }
            if(key.equals(KEY_LIST_DEPTH_PREFERENCE)) {
                mListPreferenceDepth.setSummary(mListPreferenceDepth.getEntry().toString());
            }

            UpdateTask.executeTask(getApplicationContext(), UpdateTask.ACTION_UPDATE_EARTHQUAKES_PREFERENCE);
        }

        if(key.equals(KEY_SWITCH_PREFERENCE)) {
            boolean res = sharedPreferences.getBoolean(KEY_SWITCH_PREFERENCE, true);

            if(res) {
                mSwitchPreference.setSummary("Enabled");
            } else {
                mSwitchPreference.setSummary("Disabled");
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mListPreference.setSummary(mListPreference.getEntry().toString());

        boolean res = getPreferenceScreen().getSharedPreferences().getBoolean(KEY_SWITCH_PREFERENCE, true);

        if(res) {
            mSwitchPreference.setSummary("Enabled");
        } else {
            mSwitchPreference.setSummary("Disabled");
        }

        mListPreferenceMag.setSummary(mListPreferenceMag.getEntry().toString());
        mListPreferenceDepth.setSummary(mListPreferenceDepth.getEntry().toString());

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

}
