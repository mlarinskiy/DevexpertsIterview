package ru.mlarinsky.interview.devex.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import ru.mlarinsky.interview.devex.R;
import ru.mlarinsky.interview.devex.model.AppModel;

public class MainActivity extends Activity implements AppModel.ModelListener {
	private static final String INPUT_DATA_AVAILABLE_KEY = "INPUT_DATA_AVAILABLE";
	private static final String INPUT_DATA_SORTED_KEY = "INPUT_DATA_SORTED";

	private static final String BUILD_INPUT_TASK_LABEL = "Building input data...";
	private static final String SORT_INPUT_TASK_LABEL = "Sorting input data...";

	private AppModel model = new AppModel(this);
	private ProgressDialog progressDialog;

	private Button sortButton;
	private Button validateButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		sortButton = (Button)findViewById(R.id.sort_button);
		validateButton = (Button)findViewById(R.id.validate_button);

		enableButtons(savedInstanceState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		enableButtons(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putBoolean(INPUT_DATA_AVAILABLE_KEY, model.isInputDataAvailable());
		outState.putBoolean(INPUT_DATA_SORTED_KEY, model.isInputDataSorted());
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
		enableButtons(null);
	}

	public void buildInputData(View view) {
		model.buildInputData(this);
	}

	public void sortInputData(View view) {
		model.sortInputData(this);
	}

	private void enableButtons(Bundle savedInstanceState) {
		if (savedInstanceState == null) {
			sortButton.setEnabled(model.isInputDataAvailable());
			validateButton.setEnabled(model.isInputDataSorted());
			return;
		}

		sortButton.setEnabled(savedInstanceState.getBoolean(INPUT_DATA_AVAILABLE_KEY));
		validateButton.setEnabled(savedInstanceState.getBoolean(INPUT_DATA_SORTED_KEY));
	}
}
