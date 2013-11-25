package ru.mlarinsky.interview.devex.logic;

/**
 * @author Mikhail Larinskiy
 */
public class TaskAdapter<T> implements TaskListener<T> {
	@Override
	public void onTaskStarted() {
	}

	@Override
	public void onTaskFinished(T result) {
	}

	@Override
	public void onProgress(int progress) {
	}
}
