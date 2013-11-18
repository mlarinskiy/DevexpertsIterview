package ru.mlarinsky.interview.devex.controller;

import static ru.mlarinsky.interview.devex.io.IntegerStreamFileWriter.*;

import android.content.Context;
import android.os.Bundle;
import ru.mlarinsky.interview.devex.io.IntegerStreamFileWriter;
import ru.mlarinsky.interview.devex.streaming.IntegerFileStream;
import ru.mlarinsky.interview.devex.streaming.IntegerRandomStream;

/**
 * @author Mikhail Larinskiy
 */
public class Controller {
	private static final String SORTED_FILE_NAME_KEY = "SORTED_FILE_NAME";
	private static final String IS_DATA_AVAILABLE_KEY = "IS_DATA_AVAILABLE";
	private static final String IS_DATA_SORTED_KEY = "IS_DATA_SORTED";

	private static final String INPUT_BUILDER_THREAD_NAME = "Input Builder";
	private static final String INPUT_SORTER_THREAD_NAME = "Input Sorter";
	private static final String INPUT_NAME = "input";
	private static final String OUTPUT_NAME = "output";

	private final ModelListener listener;

	private String sortedFileName;
	private boolean isInputDataAvailable;
	private boolean isInputDataSorted;

	public Controller(ModelListener listener, Bundle savedState) {
		this.listener = listener;

		if (savedState != null) {
			sortedFileName = savedState.getString(SORTED_FILE_NAME_KEY);
			isInputDataAvailable = savedState.getBoolean(IS_DATA_AVAILABLE_KEY);
			isInputDataSorted = savedState.getBoolean(IS_DATA_SORTED_KEY);
		}
	}

	public void saveState(Bundle savedState) {
		savedState.putString(SORTED_FILE_NAME_KEY, sortedFileName);
		savedState.putBoolean(IS_DATA_AVAILABLE_KEY, isInputDataAvailable);
		savedState.putBoolean(IS_DATA_SORTED_KEY, isInputDataSorted);
	}

	/**
	 * Builds input file filled with randomised integers.
	 * @param context current view context
	 */
	public void buildInputData(final Context context) {
		listener.onInputDataBuildStart();
		new Thread(new Runnable() {
			@Override
			public void run() {
				final String inputFilePath = context.getFileStreamPath(INPUT_NAME).getAbsolutePath();

				// Input size = size_in_kilobytes * 1000 / integer_size
				int inputSize = Settings.instance().getInputSize() * Settings.INPUT_SIZE_MULTIPLIER;

				// Build integer randomiser
				IntegerRandomStream integerRandomStream = new IntegerRandomStream(inputSize);
				IntegerStreamFileWriter inputDataFileWriter = new IntegerStreamFileWriter(new CallbackListener() {
					@Override
					// On input file building complete callback
					public void onStreamWritten() {
						isInputDataAvailable = true;
						isInputDataSorted = false;
						listener.onInputDataBuildFinish();
					}
				});

				// Build input file containing randomised integers
				inputDataFileWriter.writeStream(inputFilePath, integerRandomStream);
			}
		}, INPUT_BUILDER_THREAD_NAME).start();
	}

	/**
	 * Sorts integers from input file using MergeSort and stores the result into output file.
	 * @param context current view context
	 */
	public void sortInputData(final Context context) {
		listener.onInputDataSortStart();

		new Thread(new Runnable() {
			@Override
			public void run() {
				String outputFilePathPrefix = context.getFileStreamPath(OUTPUT_NAME).getAbsolutePath();

				// Buffer size = size_in_kilobytes * 1000 / integer_size
				int bufferSize = Settings.instance().getBufferSize() * Settings.INPUT_SIZE_MULTIPLIER;

				// Init sorter
				MergeSort mergeSort = new MergeSort(bufferSize, outputFilePathPrefix,
						new MergeSort.CallbackListener() {
							@Override
							// On input file sorting complete callback
							public void onSortingFinished(String sortedFileName) {
								Controller.this.sortedFileName = sortedFileName;
								isInputDataSorted = true;
								listener.onInputDataSortFinish();
							}
						});

				// Sort the input
				String input = context.getFileStreamPath(INPUT_NAME).getAbsolutePath();
				mergeSort.sort(input);
			}
		}, INPUT_SORTER_THREAD_NAME).start();
	}

	/**
	 * Returns an array of sorted data used to validate the result of sorting.
	 * @return an array of sorted data used to validate the result of sorting.
	 */
	public int[] getValidationData() {
		if (!isInputDataAvailable || !isInputDataSorted)
			throw new IllegalStateException("No sorted data yet");

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
			}

			return sortedData;
		} finally {
			sortedStream.close();
		}
	}

	public boolean isInputDataAvailable() {
		return isInputDataAvailable;
	}

	public boolean isInputDataSorted() {
		return isInputDataSorted;
	}

	public interface ModelListener {
		void onInputDataBuildStart();
		void onInputDataBuildFinish();
		void onInputDataSortStart();
		void onInputDataSortFinish();
	}
}
