package ru.mlarinsky.interview.devex.settings;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import ru.mlarinsky.interview.devex.R;

/**
 * @author Mikhail Larinskiy
 */
public class SettingsFragment extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

		init();
	}

	private void init() {
		Context context = getActivity().getBaseContext();
		Settings settings = new Settings(context);
		Resources resources = context.getResources();

		Preference inputSizePreference = initPreference(resources, R.string.input_size_key,
				R.string.input_size_summary_pattern, settings.getInputSize());
		inputSizePreference.setOnPreferenceChangeListener(new InputSizePreferenceChangeListener(context));

		Preference bufferSizePreference = initPreference(resources, R.string.buffer_size_key,
				R.string.buffer_size_summary_pattern, 	settings.getBufferSize());
		bufferSizePreference.setOnPreferenceChangeListener(new BufferSizePreferenceChangeListener(context));

		Preference fromPreference = initPreference(resources, R.string.from_key, R.string.from_summary_pattern,
				settings.getFrom());
		fromPreference.setOnPreferenceChangeListener(new FromPreferenceChangeListener(context));

		Preference toPreference = initPreference(resources, R.string.to_key, R.string.to_summary_pattern,
				settings.getTo());
		toPreference.setOnPreferenceChangeListener(new ToPreferenceChangeListener(context));

		PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
	}

	private Preference initPreference(Resources resources, int preferenceKeyId, int summaryPatternId, int currentValue)	{
		String preferenceKey = resources.getString(preferenceKeyId);
		String summaryPattern = resources.getString(summaryPatternId);

		Preference preference = findPreference(preferenceKey);
		preference.setSummary(String.format(summaryPattern, currentValue));
		return preference;
	}
}
