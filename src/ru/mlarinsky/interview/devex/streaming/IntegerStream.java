package ru.mlarinsky.interview.devex.streaming;

/**
 * @author Mikhail Larinskiy
 */
public interface IntegerStream {


	int next();
	int peek();
	boolean hasNext();
}
