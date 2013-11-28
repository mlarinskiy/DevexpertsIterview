package ru.mlarinsky.interview.devex.settings;

/**
 * @author Mikhail Larinskiy
 */
public class IntPreferenceValidator {
	private final int minValue;
	private final int maxValue;

	public IntPreferenceValidator(int minValue, int maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	public boolean validate(Object newValue) {
		try {
			String stringValue = String.valueOf(newValue);
			Integer intValue = Integer.valueOf(stringValue);
			return intValue >= minValue && intValue <= maxValue;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public int getMinValue() {
		return minValue;
	}

	public int getMaxValue() {
		return maxValue;
	}
}
