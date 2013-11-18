package ru.mlarinsky.interview.devex.io;

import android.util.Log;
import ru.mlarinsky.interview.devex.model.streaming.IntegerStream;

import java.io.*;

/**
 * @author Mikhail Larinskiy
 */
public class IntegerStreamFileWriter {
	private static final String TAG = IntegerStreamFileWriter.class.getSimpleName();
	private static final String ON_WRITE_ERROR_PREFIX = "Failed to write to ";
	private static final String ON_CLOSE_ERROR_PREFIX = "Failed to close ";

	private final CallbackListener listener;

	public IntegerStreamFileWriter(CallbackListener listener) {
		this.listener = listener;
	}

	public void writeStream(String filePath, IntegerStream stream) {
		DataOutputStream dos = null;
		try {
			dos = new DataOutputStream(new FileOutputStream(filePath));
			while (stream.hasNext())
				dos.writeInt(stream.next());
		} catch (IOException e) {
			Log.e(TAG, ON_WRITE_ERROR_PREFIX + filePath, e);
		} finally {
			if (dos != null) {
				try {
					dos.close();
					listener.onStreamWritten();
				} catch (IOException e) {
					Log.e(TAG, ON_CLOSE_ERROR_PREFIX + filePath, e);
				}
			}
		}
	}

	public interface CallbackListener {
		void onStreamWritten();
	}
}
