package ru.mlarinsky.interview.devex.streaming;

/**
 * @author Mikhail Larinskiy
 */
public class IntegerArrayStream implements IntegerStream {
	private final int[] array;
	private int current;

	public IntegerArrayStream(int[] array) {
		this.array = array;
	}

	@Override
	public int next() {
		if (!hasNext())
			throw new NoMoreElementsException();
		return array[current++];
	}

	@Override
	public int peek() {
		if (!hasNext())
			throw new NoMoreElementsException();
		return array[current];
	}

	@Override
	public boolean hasNext() {
		return current < array.length;
	}
}
