package ru.mlarinsky.interview.devex.logic;

import android.os.AsyncTask;
import android.util.Log;
import ru.mlarinsky.interview.devex.settings.Settings;

import java.util.ArrayList;
import java.util.List;

/**
 * Bse class for long running computation tasks.
 *
 * @author Mikhail Larinskiy
 */
public abstract class BaseTask<T> extends AsyncTask<String, Integer, T> {
	protected final String tag;
	protected final Settings settings;
	protected final List<TaskListener<T>> taskListeners = new ArrayList<TaskListener<T>>();

	protected int progressCounter;
	protected int taskSize;

	protected BaseTask(String tag, Settings settings) {
		this.tag = tag;
		this.settings = settings;
	}

	public void addListener(TaskListener<T> listener) {
		taskListeners.add(listener);
	}

	@Override
	protected void onPreExecute() {
		Log.d(tag, "Execution start.");
		progressCounter = 0;

		for (TaskListener taskListener : taskListeners)
			taskListener.onTaskStarted();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		Integer progress = values[0];
		Log.d(tag, "Progress = " + progress);

		for (TaskListener taskListener : taskListeners)
			taskListener.onProgress(progress);
	}

	@Override
	protected void onPostExecute(T result) {
		Log.d(tag, "Execution end.");

		for (TaskListener<T> taskListener : taskListeners)
			taskListener.onTaskFinished(result);
	}

	// Updates and returns task progress
	protected int getProgress() {
		return taskSize == 0 ? 0 : 100 * ++progressCounter / taskSize;
	}
}