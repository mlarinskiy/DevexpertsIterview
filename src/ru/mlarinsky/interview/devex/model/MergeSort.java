package ru.mlarinsky.interview.devex.model;

import android.util.Log;
import ru.mlarinsky.interview.devex.io.IntegerStreamFileWriter;
import ru.mlarinsky.interview.devex.model.streaming.IntegerArrayStream;
import ru.mlarinsky.interview.devex.model.streaming.IntegerFileStream;
import ru.mlarinsky.interview.devex.model.streaming.IntegerMergedSortedStream;
import ru.mlarinsky.interview.devex.model.streaming.IntegerStream;

import java.io.File;
import java.util.Arrays;

/**
 * @author Mikhail Larinskiy
 */
public class MergeSort implements IntegerStreamFileWriter.CallbackListener {
	private static final String TAG = MergeSort.class.getSimpleName();
	private static final String SORT_ERROR_MESSAGE = "Failed to sort input data.";
	private static final String DELETE_TMP_ERROR_MESSAGE = "Failed to delete tmp file ";

	private final IntegerStreamFileWriter fileWriter = new IntegerStreamFileWriter(this);
	private final IntegerFileStream inputStream = new IntegerFileStream();
	private final IntegerFileStream sortedStream = new IntegerFileStream();

	private final int bufferSize;
	private final CallbackListener callbackListener;

	private String sortedFileName;
	private String sortedFileNamePrefix;
	private int sortedFileNameIndex;

	public MergeSort(int bufferSize, String sortedFileNamePrefix, CallbackListener callbackListener) {
		this.bufferSize = bufferSize;
		this.callbackListener = callbackListener;
		this.sortedFileNamePrefix = sortedFileNamePrefix;

		sortedFileName = sortedFileNamePrefix + (sortedFileNameIndex++);
	}

	@Override
	public void onStreamWritten() {
		if (!inputStream.hasNext()) {
			callbackListener.onSortingFinished(sortedFileName);
			inputStream.close();
			return;
		}

		mergeInputWith(sortedFileNamePrefix + (sortedFileNameIndex++));
	}

	public void sort(String inputFilePath) {
		try {
			inputStream.open(inputFilePath);

			File emptySortedFile = new File(sortedFileName);
			emptySortedFile.createNewFile();
			mergeInputWith(sortedFileName);
		} catch (Throwable e) {
			Log.e(TAG, SORT_ERROR_MESSAGE, e);
			inputStream.close();
		}
	}

	private void mergeInputWith(String alreadySortedFilePath) {
		try {
			sortedStream.open(alreadySortedFilePath);
			IntegerStream inputBuffer = nextInputBuffer(inputStream);
			IntegerMergedSortedStream mergedSortedStream = new IntegerMergedSortedStream(inputBuffer, sortedStream);

			sortedFileName = sortedFileNamePrefix + (sortedFileNameIndex++);
			fileWriter.writeStream(sortedFileName, mergedSortedStream);
		} catch (Throwable e) {
			Log.e(TAG, SORT_ERROR_MESSAGE, e);
		} finally {
			sortedStream.close();
			cleanUp(alreadySortedFilePath);
		}
	}

	private IntegerArrayStream nextInputBuffer(IntegerStream inputStream) {
		int[] buffer = new int[bufferSize];

		StringBuilder sb = new StringBuilder("New buffer = ");
		for (int i = 0; i < buffer.length && inputStream.hasNext(); i++) {
			buffer[i] = inputStream.next();
			sb.append(buffer[i]).append(",");
		}
		System.out.println(sb);

		Arrays.sort(buffer);
		return new IntegerArrayStream(buffer);
	}

	private void cleanUp(String tmpFilePath) {
		boolean tmpFileIsDeleted = new File(tmpFilePath).delete();
		if (!tmpFileIsDeleted)
			Log.e(TAG, DELETE_TMP_ERROR_MESSAGE + tmpFilePath);
	}

	public interface CallbackListener {
		void onSortingFinished(String sortedFilePath);
	}
}
