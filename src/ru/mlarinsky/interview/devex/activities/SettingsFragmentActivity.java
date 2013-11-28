package ru.mlarinsky.interview.devex.activities;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import ru.mlarinsky.interview.devex.R;
import ru.mlarinsky.interview.devex.settings.SettingsFragment;

/**
 * @author Mikhail Larinskiy
 */
public class SettingsFragmentActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new SettingsFragment())
				.commit();
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
	}
}
