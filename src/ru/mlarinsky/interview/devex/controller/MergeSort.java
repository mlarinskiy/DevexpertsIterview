package ru.mlarinsky.interview.devex.controller;

import android.util.Log;
import ru.mlarinsky.interview.devex.io.IntegerStreamFileWriter;
import ru.mlarinsky.interview.devex.streaming.IntegerArrayStream;
import ru.mlarinsky.interview.devex.streaming.IntegerFileStream;
import ru.mlarinsky.interview.devex.streaming.IntegerMergedSortedStream;
import ru.mlarinsky.interview.devex.streaming.IntegerStream;

import java.io.File;
import java.util.Arrays;

/**
 * Sorts input file containing integers only.
 *
 * @author Mikhail Larinskiy
 */
public class MergeSort implements IntegerStreamFileWriter.CallbackListener {
	private static final String TAG = MergeSort.class.getSimpleName();
	private static final String SORT_ERROR_MESSAGE = "Failed to sort input data.";
	private static final String DELETE_TMP_ERROR_MESSAGE = "Failed to delete tmp file ";

	private static final String HOLDER_FILE_SUFFIX = "tmp";

	private final IntegerStreamFileWriter fileWriter = new IntegerStreamFileWriter(this);
	private final IntegerFileStream inputStream = new IntegerFileStream();
	private final IntegerFileStream sortedStream = new IntegerFileStream();

	private final int bufferSize;
	private final CallbackListener callbackListener;

	private String mergedFileName;
	private String mergedFileNameHolder;

	public MergeSort(int bufferSize, String sortedFileNamePrefix, CallbackListener callbackListener) {
		this.bufferSize = bufferSize;
		this.callbackListener = callbackListener;

		mergedFileName = sortedFileNamePrefix;
		mergedFileNameHolder = sortedFileNamePrefix + HOLDER_FILE_SUFFIX;
	}

	@Override
	public void onStreamWritten() {
		// If all input has been read stop sorting and return the result
		if (!inputStream.hasNext()) {
			callbackListener.onSortingFinished(mergedFileName);
			cleanUp(); // Don't forget to cleanup!
			return;
		}

		// If there is something left continue the sorting and merging process
		mergeInputWith(mergedFileName);
	}

	public void sort(String inputFilePath) {
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

	public interface CallbackListener {
		void onSortingFinished(String sortedFileName);
	}
}
