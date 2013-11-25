package ru.mlarinsky.interview.devex.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import ru.mlarinsky.interview.devex.R;

/**
 * @author Mikhail Larinskiy
 */
public class SettingsActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		if (savedInstanceState != null)
			Settings.instance().restore(savedInstanceState);

		init();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		Settings.instance().restore(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Settings.instance().saveState(outState);
	}

	public void selectFirst(View view) {
		Settings.instance().setFirstSelected(true);
	}

	public void selectLast(View view) {
		Settings.instance().setFirstSelected(false);
	}

	private void init() {
		EditText inputSizeEdit = (EditText) findViewById(R.id.inputSizeEdit);
		inputSizeEdit.setText(String.valueOf(Settings.instance().getInputSize()));
		inputSizeEdit.addTextChangedListener(new BaseTextWatcher(inputSizeEdit) {
			@Override
			protected void updateSettingValue(int value) {
				Settings.instance().setInputSize(value);
			}
		});

		EditText bufferSizeEdit = (EditText) findViewById(R.id.bufferSizeEdit);
		bufferSizeEdit.setText(String.valueOf(Settings.instance().getBufferSize()));
		bufferSizeEdit.addTextChangedListener(new BaseTextWatcher(bufferSizeEdit) {
			@Override
			protected void updateSettingValue(int value) {
				Settings.instance().setBufferSize(value);
			}
		});

		EditText firstDepthEdit = (EditText) findViewById(R.id.firstDepthEdit);
		firstDepthEdit.setText(String.valueOf(Settings.instance().getFirstDepth()));
		firstDepthEdit.addTextChangedListener(new BaseTextWatcher(firstDepthEdit) {
			@Override
			protected void updateSettingValue(int value) {
				Settings.instance().setFirstDepth(value);
			}
		});

		EditText lastDepthEdit = (EditText) findViewById(R.id.lastDepthEdit);
		lastDepthEdit.setText(String.valueOf(Settings.instance().getLastDepth()));
		lastDepthEdit.addTextChangedListener(new BaseTextWatcher(lastDepthEdit) {
			@Override
			protected void updateSettingValue(int value) {
				Settings.instance().setLastDepth(value);
			}
		});

		RadioGroup depthSelectionGroup = (RadioGroup) findViewById(R.id.validationSelector);
		int selectedButtonId = Settings.instance().isFirstSelected() ? R.id.selectFirst : R.id.selectLast;
		depthSelectionGroup.check(selectedButtonId);
	}

	private static abstract class BaseTextWatcher implements TextWatcher {
		private final EditText editText;

		protected BaseTextWatcher(EditText editText) {
			this.editText = editText;
		}

		protected abstract void updateSettingValue(int value);

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			String textValue = editText.getText().toString();
			if (textValue == null || textValue.isEmpty()) {
				editText.setText(String.valueOf(0));
				return;
			}
			updateSettingValue(Integer.valueOf(textValue));
		}
	}
}
