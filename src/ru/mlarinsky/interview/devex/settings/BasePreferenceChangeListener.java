package ru.mlarinsky.interview.devex.settings;

import android.content.Context;
import android.content.res.Resources;
import android.preference.Preference;
import android.widget.Toast;
import ru.mlarinsky.interview.devex.R;

/**
 * @author Mikhail Larinskiy
 */
public abstract class BasePreferenceChangeListener implements Preference.OnPreferenceChangeListener {
	private final IntPreferenceValidator validator;
	protected final Settings settings;
	protected final Context context;

	private final String summaryPattern;
	private final String errorMessagePattern;

	public BasePreferenceChangeListener(Context context, int minValueId, int maxValueId, int summaryPatternId) {
		this.context = context;
		settings =  new Settings(context);

		Resources resources = context.getResources();
		summaryPattern = resources.getString(summaryPatternId);
		errorMessagePattern = resources.getString(R.string.out_of_limits_error_message_pattern);
		validator = new IntPreferenceValidator(resources.getInteger(minValueId), resources.getInteger(maxValueId));
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (!validator.validate(newValue)) {
			String errorMessage = String.format(errorMessagePattern, validator.getMinValue(), validator.getMaxValue());

			Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();

			return false;
		}

		Integer value = Integer.valueOf(String.valueOf(newValue));
		if (!checkDependencies(value))
			return false;

		preference.setSummary(String.format(summaryPattern, value));

		return true;
	}

	protected abstract boolean checkDependencies(int newValue);
}
