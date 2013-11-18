package ru.mlarinsky.interview.devex.model.streaming;

import java.util.Iterator;

/**
 * @author Mikhail Larinskiy
 */
public interface IntegerStream {


	int next();
	int peek();
	boolean hasNext();
}
