package ru.mlarinsky.interview.devex.logic;

/**
 * Is used to hold asynchronous task result listener between activity's reincarnations.
 *
 * @author Mikhail Larinskiy
 */
public class Controller {
	private static final Controller INSTANCE = new Controller();

	private Listener listener;

	public static Controller instance() {
		return INSTANCE;
	}

	public void setListener(Listener listener) {
		this.listener = listener;
	}

	public void onInputBuildStart() {
		listener.onInputBuildStart();
	}

	public void onInputBuildEnd() {
		listener.onInputBuildEnd();
	}

	public void onInputSortStart() {
		listener.onInputSortStart();
	}

	public void onInputSortEnd(String sortedFileName) {
		listener.onInputSortEnd(sortedFileName);
	}

	public void onVerificationDataBuildEnd(String[] verificationDataLabels) {
		listener.onVerificationDataBuildEnd(verificationDataLabels);
	}

	public interface Listener {
		void onInputBuildStart();
		void onInputBuildEnd();
		void onInputSortStart();
		void onInputSortEnd(String sortedFileName);
		void onVerificationDataBuildEnd(String[] verificationDataLabels);
	}
}
