package org.kyledef.findmepizza.ui;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import org.kyledef.findmepizza.R;

public class SettingsActivity extends PreferenceActivity {

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_telemetry);
    }
}
