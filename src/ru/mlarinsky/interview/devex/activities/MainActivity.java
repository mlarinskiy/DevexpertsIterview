package ru.mlarinsky.interview.devex.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import ru.mlarinsky.interview.devex.R;
import ru.mlarinsky.interview.devex.logic.*;

public class MainActivity extends Activity implements Controller.Listener {
	public static final String VALIDATION_DATA_KEY = "VALIDATION_DATA";
	private static final String SORTED_FILE_NAME_KEY = "SORTED_FILE_NAME";
	private static final String IS_DATA_AVAILABLE_KEY = "IS_DATA_AVAILABLE";
	private static final String IS_DATA_SORTED_KEY = "IS_DATA_SORTED";

	private static final String BUILD_INPUT_TASK_LABEL = "Building input data...";
	private static final String SORT_INPUT_TASK_LABEL = "Sorting input data...";
	private static final String BUILD_VALIDATION_DATA_TASK_LABEL = "Building validation data...";
	private static final String INPUT_NAME = "input";
	private static final String OUTPUT_NAME = "output";

	private TaskProgressDialog progressDialog;
	private Button sortButton;
	private Button validateButton;

	private String sortedFileName;

	// -------------- Activity lifecycle callbacks implementation -------------
	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		setContentView(R.layout.main);

		progressDialog = new TaskProgressDialog(getFragmentManager());
		sortButton = (Button)findViewById(R.id.sort_button);
		validateButton = (Button)findViewById(R.id.validate_button);

		Controller.instance().setListener(this);

		restore(savedState);
	}

	@Override
	protected void onSaveInstanceState(Bundle savedState) {
		super.onSaveInstanceState(savedState);

		savedState.putString(SORTED_FILE_NAME_KEY, sortedFileName);
		savedState.putBoolean(IS_DATA_AVAILABLE_KEY, sortButton.isEnabled());
		savedState.putBoolean(IS_DATA_SORTED_KEY, validateButton.isEnabled());

		Settings.instance().saveState(savedState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedState) {
		super.onRestoreInstanceState(savedState);
		restore(savedState);
	}

	private void restore(Bundle savedState) {
		Settings.instance().restore(savedState);

		if (savedState != null) {
			sortedFileName = savedState.getString(SORTED_FILE_NAME_KEY);
			sortButton.setEnabled(savedState.getBoolean(IS_DATA_AVAILABLE_KEY));
			validateButton.setEnabled(savedState.getBoolean(IS_DATA_SORTED_KEY));
		} else {
			sortButton.setEnabled(false);
			validateButton.setEnabled(false);
		}
	}

	// -------------- User input action callback implementation --------------
	@SuppressWarnings("unchecked")
	public void onClick(View view) {
		String progressDialogTitle;
		TaskListener taskResultListener;
		String[] params;
		BaseTask task;
		switch (view.getId()) {
			case R.id.build_button:
				progressDialogTitle = BUILD_INPUT_TASK_LABEL;
				params = new String[]{
						getFileStreamPath(INPUT_NAME).getAbsolutePath()
				};
				task = new BuildInputTask();
				taskResultListener = new BuildInputDataTaskListener();
				break;
			case R.id.sort_button:
				progressDialogTitle = SORT_INPUT_TASK_LABEL;
				params = new String[]{
						getFileStreamPath(INPUT_NAME).getAbsolutePath(),
						getFileStreamPath(OUTPUT_NAME).getAbsolutePath()
				};
				task = new SortInputTask();
				taskResultListener = new SortInputTaskListener();
				break;
			case R.id.validate_button:
				progressDialogTitle = BUILD_VALIDATION_DATA_TASK_LABEL;
				params = new String[]{
						sortedFileName
				};
				task = new BuildVerificationDataTask();
				taskResultListener = new BuildVerificationDataTaskListener();
				break;
			case R.id.settings_button:
				Intent intent = new Intent(this, SettingsActivity.class);
				startActivity(intent);
				return;
			default:
				throw new IllegalArgumentException("Unknown click source: " + view);
		}

		progressDialog.setTitle(progressDialogTitle);
		task.addListener(progressDialog);
		task.addListener(taskResultListener);
		task.execute(params);
	}

	// -------------- Controller callbacks implementation -------------
	@Override
	public void onInputBuildStart() {
		sortButton.setEnabled(false);
		validateButton.setEnabled(false);
	}

	@Override
	public void onInputBuildEnd() {
		sortButton.setEnabled(true);
	}

	@Override
	public void onInputSortStart() {
		validateButton.setEnabled(false);
	}

	@Override
	public void onInputSortEnd(String sortedFileName) {
		this.sortedFileName = sortedFileName;
		validateButton.setEnabled(true);
	}

	@Override
	public void onVerificationDataBuildEnd(String[] verificationDataLabels) {
		Intent intent = new Intent(MainActivity.this, ValidationActivity.class);
		intent.putExtra(VALIDATION_DATA_KEY, verificationDataLabels);
		startActivity(intent);
	}

	// -------------- Asynchronous tasks callbacks implementation --------------
	private class BuildInputDataTaskListener extends TaskAdapter<Void> {
		@Override
		public void onTaskStarted() {
			Controller.instance().onInputBuildStart();
		}

		@Override
		public void onTaskFinished(Void result) {
			Controller.instance().onInputBuildEnd();
		}
	}

	private class SortInputTaskListener extends TaskAdapter<String> {
		@Override
		public void onTaskStarted() {
			Controller.instance().onInputSortStart();
		}

		@Override
		public void onTaskFinished(String result) {
			Controller.instance().onInputSortEnd(result);
		}
	}

	private class BuildVerificationDataTaskListener extends TaskAdapter<String[]> {
		@Override
		public void onTaskFinished(String[] result) {
			Controller.instance().onVerificationDataBuildEnd(result);
		}
	}
}
