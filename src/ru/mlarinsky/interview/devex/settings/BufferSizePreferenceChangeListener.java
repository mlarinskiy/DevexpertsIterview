package ru.mlarinsky.interview.devex.settings;

import android.content.Context;
import ru.mlarinsky.interview.devex.R;

/**
 * @author Mikhail Larinskiy
 */
public class BufferSizePreferenceChangeListener extends BasePreferenceChangeListener {
	public BufferSizePreferenceChangeListener(Context context) {
		super(context, R.integer.buffer_size_min, R.integer.buffer_size_max, R.string.buffer_size_summary_pattern);
	}

	@Override
	protected boolean checkDependencies(int newValue) {
		return true;
	}
}
