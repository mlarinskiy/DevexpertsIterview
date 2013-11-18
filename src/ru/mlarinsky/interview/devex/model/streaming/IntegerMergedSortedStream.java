package ru.mlarinsky.interview.devex.model.streaming;

/**
 * @author Mikhail Larinskiy
 */
public class IntegerMergedSortedStream implements IntegerStream {
	private IntegerStream smallerStream;
	private IntegerStream biggerStream;

	public IntegerMergedSortedStream(IntegerStream smallerStream, IntegerStream biggerStream) {
		this.smallerStream = smallerStream;
		this.biggerStream = biggerStream;
	}

	@Override
	public int next() {
		if (!hasNext())
			throw new NoMoreElementsException();

		if (!smallerStream.hasNext())
			return biggerStream.next();
		if (!biggerStream.hasNext())
			return smallerStream.next();

		if (smallerStream.peek() > biggerStream.peek()) {
			IntegerStream tmp = smallerStream;
			smallerStream = biggerStream;
			biggerStream = tmp;
		}

		return smallerStream.next();
	}

	@Override
	public int peek() {
		if (!hasNext())
			throw new NoMoreElementsException();

		if (!smallerStream.hasNext())
			return biggerStream.peek();
		if (!biggerStream.hasNext())
			return smallerStream.peek();

		return Math.min(smallerStream.peek(), biggerStream.peek());
	}

	@Override
	public boolean hasNext() {
		return smallerStream.hasNext() || biggerStream.hasNext();
	}
}
