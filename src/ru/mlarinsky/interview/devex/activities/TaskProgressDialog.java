package ru.mlarinsky.interview.devex.activities;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import ru.mlarinsky.interview.devex.R;
import ru.mlarinsky.interview.devex.logic.TaskListener;

/**
 * @author Mikhail Larinskiy
 */
public class TaskProgressDialog extends DialogFragment implements TaskListener {
	private static final String TAG = TaskProgressDialog.class.getSimpleName();

	private final FragmentManager fragmentManager;
	private final DialogInterface.OnKeyListener ignoreBackButtonListener = new DialogInterface.OnKeyListener() {
		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			return keyCode == KeyEvent.KEYCODE_BACK;
		}
	};
	private String title;
	private ProgressBar progressBar;
	private boolean isTaskRunning;

	public TaskProgressDialog(FragmentManager fragmentManager) {
		this.fragmentManager = fragmentManager;
	}

	// ------------- Task callbacks implementation ----------------
	@Override
	public void onProgress(int progress) {
		if (progressBar != null)
			progressBar.setProgress(progress);
	}

	@Override
	public void onTaskStarted() {
		isTaskRunning = true;
		show(fragmentManager, TAG);
	}

	@Override
	public void onTaskFinished(Object result) {
		isTaskRunning = false;
		progressBar.setProgress(0);
		if (isResumed())
			dismiss();
	}

	// ------------- System callbacks implementation --------------
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setShowsDialog(true);
		setRetainInstance(true);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!isTaskRunning)
			dismiss();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.progress_dialog, container, false);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

		Dialog dialog = getDialog();
		dialog.setTitle(title);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setOnKeyListener(ignoreBackButtonListener);

		return view;
	}

	@Override
	public void onDestroyView() {
		if (getDialog() != null && getRetainInstance())
			getDialog().setDismissMessage(null);
		super.onDestroyView();
	}

	// ------------- Personal public API --------------------------
	public void setTitle(String title) {
		this.title = title;
	}
}
