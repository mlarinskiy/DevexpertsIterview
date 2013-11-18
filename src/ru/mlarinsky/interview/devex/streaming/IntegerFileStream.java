package ru.mlarinsky.interview.devex.streaming;

import android.util.Log;

import java.io.*;

/**
 * @author Mikhail Larinskiy
 */
public class IntegerFileStream implements IntegerStream {
	private static final String TAG = IntegerFileStream.class.getSimpleName();
	private static final String ON_OPEN_ERROR_PREFIX = "Failed to open InputStream for ";
	private static final String ON_CLOSE_ERROR_PREFIX = "Failed to close InputStream for ";
	private static final String ON_NEXT_ERROR_PREFIX = "Failed to read next int from ";

	private DataInputStream dis;
	private Integer next;

	private String fileAbsolutePath;

	public void open(String fileAbsolutePath) {
		try {
			dis = new DataInputStream(new FileInputStream(fileAbsolutePath));
			if (dis.available() > 0)
				next = dis.readInt();
		} catch (IOException e) {
			onError(e, ON_OPEN_ERROR_PREFIX);
		}
	}

	public void close() {
		if (dis == null)
			return;

		try {
			dis.close();
		} catch (IOException e) {
			onError(e, ON_CLOSE_ERROR_PREFIX);
		} finally {
			dis = null;
			fileAbsolutePath = null;
		}
	}

	@Override
	public int next() {
		if (!hasNext())
			throw new NoMoreElementsException();

		int current = next;
		try {
			next = dis.available() > 0 ? dis.readInt() : null;
		} catch (IOException e) {
			onError(e, ON_NEXT_ERROR_PREFIX);
			close();
		}
		return current;
	}

	@Override
	public int peek() {
		if (!hasNext())
			throw new NoMoreElementsException();
		return next;
	}

	@Override
	public boolean hasNext() {
		return next != null;
	}

	private void onError(Throwable error, String errorMessagePrefix) {
		Log.e(TAG, errorMessagePrefix + fileAbsolutePath, error);
	}
}
