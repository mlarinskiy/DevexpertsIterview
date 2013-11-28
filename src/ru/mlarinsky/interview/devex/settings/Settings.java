package ru.mlarinsky.interview.devex.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import ru.mlarinsky.interview.devex.R;

/**
 * @author Mikhail Larinskiy
 */
public class Settings {
	private static final int DEFAULT_INPUT_SIZE = 25000;
	private static final int DEFAULT_BUFFER_SIZE = DEFAULT_INPUT_SIZE / 10;
	private static final int DEFAULT_FROM = 0;
	private static final int DEFAULT_TO = DEFAULT_FROM;

	private final Context context;
	private final SharedPreferences sharedPreferences;

	public Settings(Context context) {
		this.context = context;
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public int getInputSize() {
		String key = context.getString(R.string.input_size_key);
		return Integer.valueOf(sharedPreferences.getString(key, String.valueOf(DEFAULT_INPUT_SIZE)));
	}

	public int getBufferSize() {
		String key = context.getString(R.string.buffer_size_key);
		return Integer.valueOf(sharedPreferences.getString(key, String.valueOf(DEFAULT_BUFFER_SIZE)));
	}

	public int getFrom() {
		String key = context.getString(R.string.from_key);
		return Integer.valueOf(sharedPreferences.getString(key, String.valueOf(DEFAULT_FROM)));
	}

	public int getTo() {
		String key = context.getString(R.string.to_key);
		return Integer.valueOf(sharedPreferences.getString(key, String.valueOf(DEFAULT_TO)));
	}
}
