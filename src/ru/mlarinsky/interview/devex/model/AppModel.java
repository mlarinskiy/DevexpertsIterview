package ru.mlarinsky.interview.devex.model;

import static ru.mlarinsky.interview.devex.io.IntegerStreamFileWriter.*;

import android.content.Context;
import ru.mlarinsky.interview.devex.io.IntegerStreamFileWriter;
import ru.mlarinsky.interview.devex.model.streaming.IntegerRandomStream;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Mikhail Larinskiy
 */
public class AppModel {
	private static final String INPUT_BUILDER_THREAD_NAME = "Input Builder";
	private static final String INPUT_SORTER_THREAD_NAME = "Input Sorter";
	private static final String INPUT_NAME = "input";
	private static final String OUTPUT_NAME = "output";
	private static final int INPUT_SIZE = 40 / 4;
	private static final int BUFFER_SIZE = INPUT_SIZE / 10;

	private final ModelListener listener;

	private boolean isInputDataAvailable;
	private boolean isInputDataSorted;

	public AppModel(final ModelListener listener) {
		this.listener = listener;
	}

	public void buildInputData(final Context context) {
		listener.onInputDataBuildStart();
		new Thread(new Runnable() {
			@Override
			public void run() {
				final String inputFilePath = context.getFileStreamPath(INPUT_NAME).getAbsolutePath();
				IntegerRandomStream integerRandomStream = new IntegerRandomStream(INPUT_SIZE);
				IntegerStreamFileWriter inputDataFileWriter = new IntegerStreamFileWriter(new CallbackListener() {
					@Override
					public void onStreamWritten() {
						isInputDataAvailable = true;
						isInputDataSorted = false;
						listener.onInputDataBuildFinish();

						printResult(inputFilePath);
					}
				});
				inputDataFileWriter.writeStream(inputFilePath, integerRandomStream);
			}
		}, INPUT_BUILDER_THREAD_NAME).start();
	}

	public void sortInputData(final Context context) {
		listener.onInputDataSortStart();

		new Thread(new Runnable() {
			@Override
			public void run() {
				String outputFilePathPrefix = context.getFileStreamPath(OUTPUT_NAME).getAbsolutePath();
				MergeSort mergeSort = new MergeSort(BUFFER_SIZE, outputFilePathPrefix,
						new MergeSort.CallbackListener() {
							@Override
							public void onSortingFinished(String sortedFilePath) {
								isInputDataSorted = true;
								listener.onInputDataSortFinish();

								printResult(sortedFilePath);
							}
						});
				String input = context.getFileStreamPath(INPUT_NAME).getAbsolutePath();
				mergeSort.sort(input);
			}
		}, INPUT_SORTER_THREAD_NAME).start();
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




	private void printResult(String filePath) {
		try {
			DataInputStream dis = new DataInputStream(new FileInputStream(filePath));
			StringBuilder sb = new StringBuilder();
			while (dis.available() > 0)
				sb.append(dis.readInt()).append(" ");
			System.out.println("Result = " + sb);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
