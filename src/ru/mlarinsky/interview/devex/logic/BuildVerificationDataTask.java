package ru.mlarinsky.interview.devex.logic;

import ru.mlarinsky.interview.devex.settings.Settings;
import ru.mlarinsky.interview.devex.streaming.IntegerFileStream;

import java.util.ArrayList;
import java.util.List;

/**
 * Asynchronous task for building verification data labels on a background thread.
 *
 * @author Mikhail Larinskiy
 */
public class BuildVerificationDataTask extends BaseTask<String[]> {
	private static final String TAG = BuildVerificationDataTask.class.getSimpleName();

	public BuildVerificationDataTask(Settings settings) {
		super(TAG, settings);
	}

	@Override
	protected void onPreExecute() {
		taskSize =  settings.getTo();

		super.onPreExecute();
	}

	@Override
	protected String[] doInBackground(String... params) {
		// Build validation data labels list
		List<Integer> validationData = getValidationData(params[0]);
		String[] labels = new String[validationData.size()];
		for (int i = 0; i < validationData.size(); i++) {
			labels[i] = String.valueOf(validationData.get(i));
			publishProgress(getProgress());
		}

		return labels;
	}

	/**
	 * Returns an array of sorted data used to validate the result of sorting.
	 * @return an array of sorted data used to validate the result of sorting.
	 */
	private List<Integer> getValidationData(String sortedFileName) {
		List<Integer> sortedData = new ArrayList<Integer>();

		// Start to read the sorting result
		IntegerFileStream sortedStream = new IntegerFileStream();
		try {
			sortedStream.open(sortedFileName);

			int from = settings.getFrom();
			int to = settings.getTo();
			int i = 0;
			while (sortedStream.hasNext()) {
				int next = sortedStream.next();
				i++;

				if (i > from && i <= to)
					sortedData.add(next);

				publishProgress(getProgress());
			}

			return sortedData;
		} finally {
			sortedStream.close();
		}
	}
}
