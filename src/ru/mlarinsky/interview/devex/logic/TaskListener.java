package ru.mlarinsky.interview.devex.logic;

/**
 * @author Mikhail Larinskiy
 */
public interface TaskListener<T> {
	void onTaskStarted();
	void onTaskFinished(T result);

	void onProgress(int progress);
}
