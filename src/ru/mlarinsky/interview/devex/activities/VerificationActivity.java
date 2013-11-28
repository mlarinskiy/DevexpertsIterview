package ru.mlarinsky.interview.devex.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ru.mlarinsky.interview.devex.R;
import ru.mlarinsky.interview.devex.settings.Settings;

/**
 * @author Mikhail Larinskiy
 */
public class VerificationActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.validation);
		init();
	}

	private void init() {
		Bundle extras = getIntent().getExtras();

		String[] labels = extras.getStringArray(MainActivity.VERIFICATION_DATA_KEY);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, labels);

		Settings settings = new Settings(this);
		setTitle(String.format(getString(R.string.verification_activity_title_pattern),
				settings.getFrom(), settings.getTo()));

		ListView listView = (ListView) findViewById(R.id.validationList);
		listView.setAdapter(adapter);
	}
}
