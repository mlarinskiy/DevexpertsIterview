package ru.mlarinsky.interview.devex.settings;

import android.content.Context;
import android.widget.Toast;
import ru.mlarinsky.interview.devex.R;

/**
 * @author Mikhail Larinskiy
 */
public class FromPreferenceChangeListener extends BasePreferenceChangeListener {
	public FromPreferenceChangeListener(Context context) {
		super(context, R.integer.from_min, R.integer.from_max, R.string.from_summary_pattern);
	}

	@Override
	protected boolean checkDependencies(int newValue) {
		if (newValue > settings.getTo()) {
			Toast.makeText(context, context.getString(R.string.from_is_too_big_error_message), Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
}
