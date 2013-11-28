package ru.mlarinsky.interview.devex.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import ru.mlarinsky.interview.devex.R;

/**
 * @author Mikhail Larinskiy
 */
public class InputSizePreferenceChangeListener extends BasePreferenceChangeListener {
	public InputSizePreferenceChangeListener(Context context) {
		super(context, R.integer.input_size_min, R.integer.input_size_max, R.string.input_size_summary_pattern);
	}

	@Override
	protected boolean checkDependencies(int newValue) {
		String fromPreferenceKey = context.getString(R.string.from_key);
		String toPreferenceKey = context.getString(R.string.to_key);

		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();

		editor.putString(fromPreferenceKey, String.valueOf(Math.min(settings.getFrom(), newValue)));
		editor.putString(toPreferenceKey, String.valueOf(Math.min(settings.getTo(), newValue)));

		editor.apply();

		return true;
	}
}
