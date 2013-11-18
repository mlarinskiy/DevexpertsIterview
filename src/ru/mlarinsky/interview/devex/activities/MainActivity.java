package ru.mlarinsky.interview.devex.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import ru.mlarinsky.interview.devex.R;
import ru.mlarinsky.interview.devex.controller.Controller;

public class MainActivity extends Activity implements Controller.ModelListener {
	public static final String VALIDATION_DATA_KEY = "VALIDATION_DATA";

	private static final String BUILD_INPUT_TASK_LABEL = "Building input data...";
	private static final String SORT_INPUT_TASK_LABEL = "Sorting input data...";

	private Controller controller;
	private ProgressDialog progressDialog;

	private Button sortButton;
	private Button validateButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		controller = new Controller(this, savedInstanceState);

		sortButton = (Button)findViewById(R.id.sort_button);
		validateButton = (Button)findViewById(R.id.validate_button);

		enableButtons();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		enableButtons();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		controller.saveState(outState);
	}

	@Override
	public void onInputDataBuildStart() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				onTaskStarted(BUILD_INPUT_TASK_LABEL);
			}
		});
	}

	@Override
	public void onInputDataBuildFinish() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				onTaskFinished();
			}
		});
	}

	@Override
	public void onInputDataSortStart() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				onTaskStarted(SORT_INPUT_TASK_LABEL);
			}
		});
	}

	@Override
	public void onInputDataSortFinish() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				onTaskFinished();
			}
		});
	}

	public void onTaskStarted(String taskProgressLabel) {
		progressDialog = ProgressDialog.show(this, "", taskProgressLabel);
	}

	public void onTaskFinished() {
		progressDialog.dismiss();
		enableButtons();
	}

	public void buildInputData(View view) {
		controller.buildInputData(this);
	}

	public void sortInputData(View view) {
		controller.sortInputData(this);
	}

	public void validateSortedData(View view) {
		Intent intent = new Intent(this, ValidationActivity.class);

		// Build validation data labels list
		int[] validationData = controller.getValidationData();
		String[] labels = new String[validationData.length];
		for (int i = 0; i < validationData.length; i++)
			labels[i] = String.valueOf(validationData[i]);

		intent.putExtra(VALIDATION_DATA_KEY, labels);
		startActivity(intent);
	}

	public void settings(View view) {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	private void enableButtons() {
		sortButton.setEnabled(controller.isInputDataAvailable());
		validateButton.setEnabled(controller.isInputDataSorted());
	}
}
