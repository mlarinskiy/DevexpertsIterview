package ru.mlarinsky.interview.devex.model.streaming;

import java.util.NoSuchElementException;
import java.util.Random;

/**
 * @author Mikhail Larinskiy
 */
public class IntegerRandomStream implements IntegerStream {
	private final Random random = new Random();
	private int size;
	private int next;

	public IntegerRandomStream(int size) {
		this.size = size;
		next = random.nextInt();
	}

	@Override
	public int next() {
		if (!hasNext())
			throw new NoMoreElementsException();

		size--;
		int current = next;
		next = random.nextInt();

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
		return size > 0;
	}
}
