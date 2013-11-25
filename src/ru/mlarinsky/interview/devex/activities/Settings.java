package ru.mlarinsky.interview.devex.activities;

import android.os.Bundle;

/**
 * @author Mikhail Larinskiy
 */
public class Settings {
	public static final int INPUT_SIZE_MULTIPLIER = 1000 / 4; // Size is specified in kilobytes. One int takes 4 bytes.

	private static final String INPUT_SIZE_KEY = "INPUT_SIZE";
	private static final String BUFFER_SIZE_KEY = "BUFFER_SIZE";
	private static final String FIRST_DEPTH_KEY = "FIRST_DEPTH";
	private static final String LAST_DEPTH_KEY = "LAST_DEPTH";
	private static final String IS_FIRST_SELECTED_KEY = "IS_FIRST_SELECTED";

	private static final int DEFAULT_INPUT_SIZE = 100;
	private static final int DEFAULT_BUFFER_SIZE = DEFAULT_INPUT_SIZE / 10;
	private static final int DEFAULT_FIRST_DEPTH = 15;
	private static final int DEFAULT_LAST_DEPTH = DEFAULT_FIRST_DEPTH;
	private static final boolean DEFAULT_FIRST_SELECTED = true;

	private static final Settings INSTANCE = new Settings();

	private int inputSize;
	private int bufferSize;
	private int firstDepth;
	private int lastDepth;
	private boolean isFirstSelected;

	private Settings() {
		restoreDefaults();
	}

	public static Settings instance() {
		return INSTANCE;
	}

	public void restore(Bundle savedState) {
		if (savedState == null) {
			restoreDefaults();
			return;
		}

		setInputSize(savedState.getInt(INPUT_SIZE_KEY));
		setBufferSize(savedState.getInt(BUFFER_SIZE_KEY));
		setFirstDepth(savedState.getInt(FIRST_DEPTH_KEY));
		setLastDepth(savedState.getInt(LAST_DEPTH_KEY));
		setFirstSelected(savedState.getBoolean(IS_FIRST_SELECTED_KEY));
	}

	private void restoreDefaults() {
		setInputSize(DEFAULT_INPUT_SIZE);
		setBufferSize(DEFAULT_BUFFER_SIZE);
		setFirstDepth(DEFAULT_FIRST_DEPTH);
		setLastDepth(DEFAULT_LAST_DEPTH);
		setFirstSelected(DEFAULT_FIRST_SELECTED);
	}

	public void saveState(Bundle savedState) {
		savedState.putInt(INPUT_SIZE_KEY, getInputSize());
		savedState.putInt(BUFFER_SIZE_KEY, getBufferSize());
		savedState.putInt(FIRST_DEPTH_KEY, getFirstDepth());
		savedState.putInt(LAST_DEPTH_KEY, getLastDepth());
		savedState.putBoolean(IS_FIRST_SELECTED_KEY, isFirstSelected());
	}

	public boolean isFirstSelected() {
		return isFirstSelected;
	}

	public void setFirstSelected(boolean firstSelected) {
		isFirstSelected = firstSelected;
	}

	public int getInputSize() {
		return inputSize;
	}

	public void setInputSize(int inputSize) {
		this.inputSize = inputSize;
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public int getFirstDepth() {
		return firstDepth;
	}

	public void setFirstDepth(int firstDepth) {
		this.firstDepth = firstDepth;
	}

	public int getLastDepth() {
		return lastDepth;
	}

	public void setLastDepth(int lastDepth) {
		this.lastDepth = lastDepth;
	}
}
