package ru.mlarinsky.interview.devex.settings;

import android.content.Context;
import android.preference.Preference;
import android.widget.Toast;
import ru.mlarinsky.interview.devex.R;

/**
 * @author Mikhail Larinskiy
 */
public class ToPreferenceChangeListener extends BasePreferenceChangeListener {
	public ToPreferenceChangeListener(Context context) {
		super(context, R.integer.to_min, R.integer.to_max, R.string.to_summary_pattern);
	}

	@Override
	protected boolean checkDependencies(int newValue) {
		if (newValue > settings.getInputSize()) {
			Toast.makeText(context, context.getString(R.string.to_is_too_big_error_message), Toast.LENGTH_SHORT).show();
			return false;
		}

		if (newValue < settings.getFrom()) {
			Toast.makeText(context, context.getString(R.string.to_is_too_small_error_message), Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}
}
