package ru.mlarinsky.interview.devex.logic;

import ru.mlarinsky.interview.devex.activities.Settings;
import ru.mlarinsky.interview.devex.streaming.IntegerFileStream;

/**
 * Asynchronous task for building verification data labels on a background thread.
 *
 * @author Mikhail Larinskiy
 */
public class BuildVerificationDataTask extends BaseTask<String[]> {
	private static final String TAG = BuildVerificationDataTask.class.getSimpleName();

	public BuildVerificationDataTask() {
		super(TAG);
	}

	@Override
	protected void onPreExecute() {
		Settings settings = Settings.instance();
		taskSize = settings.isFirstSelected() ? settings.getFirstDepth() :
				settings.getInputSize() * Settings.INPUT_SIZE_MULTIPLIER; // We have to read all sorted data.

		super.onPreExecute();
	}

	@Override
	protected String[] doInBackground(String... params) {
		// Build validation data labels list
		int[] validationData = getValidationData(params[0]);

		String[] labels = new String[validationData.length];
		for (int i = 0; i < validationData.length; i++) {
			labels[i] = String.valueOf(validationData[i]);
			publishProgress(getProgress());
		}

		return labels;
	}

	/**
	 * Returns an array of sorted data used to validate the result of sorting.
	 * @return an array of sorted data used to validate the result of sorting.
	 */
	private int[] getValidationData(String sortedFileName) {
		// Init validation data settings
		Settings settings = Settings.instance();
		boolean selectFirstIntegers = settings.isFirstSelected();
		int validationDataSize = selectFirstIntegers ? settings.getFirstDepth() : settings.getLastDepth();

		int[] sortedData = new int[validationDataSize];
		int inputSize = settings.getInputSize() * Settings.INPUT_SIZE_MULTIPLIER;
		int startFrom = selectFirstIntegers ? 0 : inputSize - validationDataSize;

		// Start to read the sorting result
		IntegerFileStream sortedStream = new IntegerFileStream();
		try {
			sortedStream.open(sortedFileName);

			int i = 0;
			while (sortedStream.hasNext() && i < sortedData.length) {
				int next = sortedStream.next();
				if (startFrom-- <= 0)
					sortedData[i++] = next;

				publishProgress(getProgress());
			}

			return sortedData;
		} finally {
			sortedStream.close();
		}
	}
}
