package com.khusainov.rinat.rupasswords;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final int MIN_LENGTH = 8;

    private PasswordsHelper mHelper;
    private EditText mSourceEditText;
    private TextView mResultTextView;
    private String[] russians;
    private String[] latins;
    private View mCopyButton;
    private View mCopyNewPasswordButton;
    private CompoundButton mCheckCaps;
    private CompoundButton mCheckDigits;
    private CompoundButton mCheckSpecSymbols;
    private TextView mStatusTextView;
    private ImageView mLevelImageView;
    private SeekBar mSeekBar;
    private Button mNewPasswordButton;
    private TextView mNewPasswordTextView;
    private TextView mPasswordLengthTextView;
    private int mPasswordLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        russians = getResources().getStringArray(R.array.russians);
        latins = getResources().getStringArray(R.array.latin);

        mHelper = new PasswordsHelper(russians, latins);

        mResultTextView = findViewById(R.id.text_result);
        mLevelImageView = findViewById(R.id.iv_password_status);
        mStatusTextView = findViewById(R.id.tv_password_status);
        mNewPasswordTextView = findViewById(R.id.tv_generated_pass);
        mPasswordLengthTextView = findViewById(R.id.tv_password_length);

        mCheckCaps = (CheckBox) findViewById(R.id.check_caps);
        mCheckDigits = (CheckBox) findViewById(R.id.check_digits);
        mCheckSpecSymbols = (CheckBox) findViewById(R.id.check_spec_symbols);

        initEditText();
        initButtons();
        initSeekBar();

        // Первое попадание в активити
        if (savedInstanceState == null) {
            updateCount(0);
        }
    }

    private void initEditText() {

        mSourceEditText = findViewById(R.id.edit_source);

        mSourceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCopyButton.setEnabled(s.length() > 0);
                mResultTextView.setText(mHelper.convert(s));

                int level = mHelper.passwordLevel(mResultTextView.getText().toString());
                mLevelImageView.setImageLevel(level * 1000);
                mStatusTextView.setText(getResources().getStringArray(R.array.levels)[level]);

                // Программная установка ресурса для imageView
                // mLevelImageView.setImageResource(R.drawable.password_clip_drawable);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initButtons() {

        mCopyButton = findViewById(R.id.btn_copy_password);
        mCopyNewPasswordButton = findViewById(R.id.btn_copy_generated);
        mNewPasswordButton = findViewById(R.id.btn_generate_pass);

        mCopyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (manager != null) {
                    manager.setPrimaryClip(
                            ClipData.newPlainText(
                                    getString(R.string.clipboard_title), mResultTextView.getText().toString()));
                    Toast.makeText(MainActivity.this, R.string.toast_copied, Toast.LENGTH_SHORT).show();
                }
            }
        });

        mCopyNewPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (manager != null) {
                    manager.setPrimaryClip(
                            ClipData.newPlainText(
                                    getString(R.string.clipboard_title), mNewPasswordTextView.getText().toString()));
                    Toast.makeText(MainActivity.this, R.string.toast_copied, Toast.LENGTH_SHORT).show();
                }
            }
        });

        mNewPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNewPasswordTextView.setText(mHelper.generatePassword(
                        MIN_LENGTH + mSeekBar.getProgress(),
                        mCheckCaps.isChecked(),
                        mCheckDigits.isChecked(),
                        mCheckSpecSymbols.isChecked()));
            }
        });
    }

    private void initSeekBar() {
        mSeekBar = findViewById(R.id.sb_pass_status);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateCount(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void updateCount(int progress) {
        mPasswordLength = MIN_LENGTH + progress;
        String generatedTextView = getResources().getQuantityString(R.plurals.symbols_count, mPasswordLength, mPasswordLength);
        mPasswordLengthTextView.setText(generatedTextView);
    }
}
