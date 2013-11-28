package ru.mlarinsky.interview.devex.logic;

import android.util.Log;
import ru.mlarinsky.interview.devex.settings.Settings;
import ru.mlarinsky.interview.devex.io.IntegerStreamFileWriter;
import ru.mlarinsky.interview.devex.streaming.IntegerArrayStream;
import ru.mlarinsky.interview.devex.streaming.IntegerFileStream;
import ru.mlarinsky.interview.devex.streaming.IntegerMergedSortedStream;
import ru.mlarinsky.interview.devex.streaming.IntegerStream;

import java.io.File;
import java.util.Arrays;

/**
 * @author Mikhail Larinskiy
 */
public class SortInputTask extends BaseTask<String> implements IntegerStreamFileWriter.CallbackListener {
	private static final String TAG = SortInputTask.class.getSimpleName();
	private static final String SORT_ERROR_MESSAGE = "Failed to sort input data.";
	private static final String DELETE_TMP_ERROR_MESSAGE = "Failed to delete tmp file ";

	private static final String HOLDER_FILE_SUFFIX = "tmp";

	private final IntegerStreamFileWriter fileWriter = new IntegerStreamFileWriter(this);
	private final IntegerFileStream inputStream = new IntegerFileStream();
	private final IntegerFileStream sortedStream = new IntegerFileStream();

	private int bufferSize;
	private String mergedFileName;
	private String mergedFileNameHolder;

	public SortInputTask(Settings settings) {
		super(TAG, settings);
	}

	// ---------------- IntegerStreamFileWriter callbacks implementation ---------------
	@Override
	public void onNextWritten() {
		publishProgress(getProgress());
	}

	@Override
	public void onStreamWritten() {
		// If all input has been read stop sorting and return the result
		if (!inputStream.hasNext()) {
			cleanUp(); // Don't forget to cleanup!
			return;
		}

		// If there is something left continue the sorting and merging process
		mergeInputWith(mergedFileName);
	}

	// ---------------- AsynchronousTask callbacks implementation ----------------
	@Override
	protected void onPreExecute() {
		bufferSize = settings.getBufferSize();

		// Input size is a sum of arithmetic progression of merged buffers sizes
		int inputSize = settings.getInputSize();
		int n = inputSize / bufferSize;
		taskSize = (n * (bufferSize + inputSize) / 2);

		super.onPreExecute();
	}


	@Override
	protected String doInBackground(String... params) {
		String inputFileName = params[0];
		String sortedFileNamePrefix = params[1];

		mergedFileName = sortedFileNamePrefix;
		mergedFileNameHolder = sortedFileNamePrefix + HOLDER_FILE_SUFFIX;

		sort(inputFileName);
		return mergedFileName;
	}

	// ---------------- Logic implementation ----------------
	private void sort(String inputFilePath) {
		try {
			inputStream.open(inputFilePath);

			// Create transitional results holder.
			File emptySortedFile = new File(mergedFileName);
			if (!emptySortedFile.createNewFile()) {
				emptySortedFile.delete();
				emptySortedFile.createNewFile();
			}

			// Start merging input with result file
			mergeInputWith(mergedFileName);
		} catch (Throwable e) {
			Log.e(TAG, SORT_ERROR_MESSAGE, e);
			inputStream.close();
		}
	}

	private void mergeInputWith(String sortedFileName) {
		try {
			// Start to read transitional result
			sortedStream.open(sortedFileName);

			// Read and sort another chunk of the input
			IntegerStream inputBuffer = nextInputBuffer(inputStream);
			// Merge the chunk and the transitional result
			IntegerMergedSortedStream mergedSortedStream = new IntegerMergedSortedStream(inputBuffer, sortedStream);

			// Reuse transitional result holder on the next merge
			mergedFileName = mergedFileNameHolder;
			mergedFileNameHolder = sortedFileName;

			// Write merge result to the transitional result holder
			fileWriter.writeStream(mergedFileName, mergedSortedStream);
		} catch (Throwable e) {
			Log.e(TAG, SORT_ERROR_MESSAGE, e);
		} finally {
			sortedStream.close();
		}
	}

	private IntegerArrayStream nextInputBuffer(IntegerStream inputStream) {
		int[] buffer = new int[bufferSize];
		for (int i = 0; i < buffer.length && inputStream.hasNext(); i++)
			buffer[i] = inputStream.next();

		Arrays.sort(buffer);
		return new IntegerArrayStream(buffer);
	}

	private void cleanUp() {
		inputStream.close();

		boolean resultHolderDeleted = new File(mergedFileNameHolder).delete();
		if (!resultHolderDeleted)
			Log.e(TAG, DELETE_TMP_ERROR_MESSAGE + mergedFileNameHolder);
	}
}
