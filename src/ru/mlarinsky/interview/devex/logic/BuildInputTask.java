package ru.mlarinsky.interview.devex.logic;

import android.util.Log;
import ru.mlarinsky.interview.devex.activities.Settings;
import ru.mlarinsky.interview.devex.io.IntegerStreamFileWriter;
import ru.mlarinsky.interview.devex.streaming.IntegerRandomStream;

/**
 * Asynchronous task for building input data file on a background thread.
 *
 * @author Mikhail Larinskiy
 */
public class BuildInputTask extends BaseTask<Void> {
	private static final String TAG = BuildInputTask.class.getSimpleName();

	public BuildInputTask() {
		super(TAG);
	}

	private IntegerStreamFileWriter inputDataFileWriter;
	private IntegerRandomStream randomStream;

	@Override
	protected void onPreExecute() {
		// Input size = size_in_kilobytes * 1000 / integer_size
		taskSize = Settings.instance().getInputSize() * Settings.INPUT_SIZE_MULTIPLIER;

		// Build integer randomiser
		randomStream = new IntegerRandomStream(taskSize);
		inputDataFileWriter = new IntegerStreamFileWriter(
				new IntegerStreamFileWriter.CallbackListener() {
					@Override
					// On input file building complete callback
					public void onStreamWritten() {
						Log.d(TAG, "onStreamWritten()");
					}

					@Override
					// On next random integer written callback
					public void onNextWritten() {
						publishProgress(getProgress());
					}
				});

		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(String... params) {
		String inputFileName = params[0];
		Log.d(TAG, "Trying to write " + inputFileName);

		// Build input file containing randomised integers
		inputDataFileWriter.writeStream(inputFileName, randomStream);

		return null;
	}
}
