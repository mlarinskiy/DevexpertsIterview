package ru.mlarinsky.interview.devex.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ru.mlarinsky.interview.devex.R;

/**
 * @author Mikhail Larinskiy
 */
public class ValidationActivity extends Activity {
	private static final String FIRST_TITLE_PATTERN = "First %d integers in ascending order";
	private static final String LAST_TITLE_PATTERN = "Last %d integers in ascending order";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.validation);
		init();
	}

	private void init() {
		Bundle extras = getIntent().getExtras();

		String[] labels = extras.getStringArray(MainActivity.VALIDATION_DATA_KEY);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, labels);

		String titlePattern = Settings.instance().isFirstSelected() ? FIRST_TITLE_PATTERN : LAST_TITLE_PATTERN;
		setTitle(String.format(titlePattern, labels.length));

		ListView listView = (ListView) findViewById(R.id.validationList);
		listView.setAdapter(adapter);
	}
}
