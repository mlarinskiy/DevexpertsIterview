package ru.mlarinsky.interview.devex.model.streaming;

/**
 * @author Mikhail Larinskiy
 */
public class NoMoreElementsException extends RuntimeException {
	public NoMoreElementsException() {
		super("Stream has no more elements.");
	}
}
