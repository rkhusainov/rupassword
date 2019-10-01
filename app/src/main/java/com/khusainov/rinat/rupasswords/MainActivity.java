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

    private PasswordsHelper helper;
    private PasswordGenerator mGenerator;
    private EditText sourceEditText;
    private TextView resultTextView;
    private String[] russians;
    private String[] latins;
    private View copyButton;
    private View copyGenButton;
    private CompoundButton checkCaps;
    private CompoundButton checkDigits;
    private CompoundButton checkSpecSymbols;
    private TextView mTvStatus;
    private ImageView mPassStatusLine;
    private SeekBar mSeekBar;
    private Button mGenerateBtn;
    private TextView mGeneratedPassword;
    private TextView mTvPasswordLength;
    private int passwordLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sourceEditText = findViewById(R.id.edit_source);
        resultTextView = findViewById(R.id.text_result);
        copyButton = findViewById(R.id.btn_copy_password);
        copyGenButton = findViewById(R.id.btn_copy_generated);
        checkCaps = (CheckBox) findViewById(R.id.check_caps);
        checkDigits = (CheckBox) findViewById(R.id.check_digits);
        checkSpecSymbols = (CheckBox) findViewById(R.id.check_spec_symbols);
        mSeekBar = findViewById(R.id.sb_pass_status);
        mPassStatusLine = findViewById(R.id.iv_password_status);
        mTvStatus = findViewById(R.id.tv_password_status);
        mGenerateBtn = findViewById(R.id.btn_generate_pass);
        mGeneratedPassword = findViewById(R.id.tv_generated_pass);
        mTvPasswordLength = findViewById(R.id.tv_password_length);

        russians = getResources().getStringArray(R.array.russians);
        latins = getResources().getStringArray(R.array.latin);
        helper = new PasswordsHelper(russians, latins);

        sourceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                resultTextView.setText(helper.convert(s));
                copyButton.setEnabled(s.length() > 0);

                int level = helper.passwordLevel(resultTextView.getText().toString());
                // Программная установка ресурса для imageView
                // mPassStatusLine.setImageResource(R.drawable.password_clip_drawable);
                mPassStatusLine.setImageLevel(level);

                String status = "";
                switch (level) {
                    case 2000:
                        status = getString(R.string.very_bad_password);
                        break;
                    case 4000:
                        status = getString(R.string.bad_password);
                        break;
                    case 6000:
                        status = getString(R.string.normal_password);
                        break;
                    case 8000:
                        status = getString(R.string.good_password);
                        break;
                    case 10000:
                        status = getString(R.string.very_good_password);
                        break;
                    default:
                        break;
                }
                mTvStatus.setText(status);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (manager != null) {
                    manager.setPrimaryClip(
                            ClipData.newPlainText(
                                    getString(R.string.clipboard_title), resultTextView.getText().toString()));
                    Toast.makeText(MainActivity.this, R.string.toast_copied, Toast.LENGTH_SHORT).show();
                }
            }
        });

        copyGenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (manager != null) {
                    manager.setPrimaryClip(
                            ClipData.newPlainText(
                                    getString(R.string.clipboard_title), mGeneratedPassword.getText().toString()));
                    Toast.makeText(MainActivity.this, R.string.toast_copied, Toast.LENGTH_SHORT).show();
                }
            }
        });

        passwordLength = mSeekBar.getProgress();
        String generatedTextView = getResources().getQuantityString(R.plurals.symbols_count, passwordLength, passwordLength);
        mTvPasswordLength.setText(generatedTextView);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                passwordLength = progress;
                String generatedTextView = getResources().getQuantityString(R.plurals.symbols_count, passwordLength, passwordLength);
                mTvPasswordLength.setText(generatedTextView);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        mGenerateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordLength == 0) {
                    Toast.makeText(MainActivity.this, R.string.password_wrong, Toast.LENGTH_SHORT).show();
                }
                // CHECK: а) очень запутано, просто ОЧЕНЬ, б) почему это тут, а не в хелпере?
                if (checkDigits.isChecked() && !checkCaps.isChecked() && !checkSpecSymbols.isChecked()) {
                    mGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                            .useLower(true)
                            .useUpper(false)
                            .useDigits(true)
                            .useSpecSymbols(false)
                            .build();
                } else if (checkCaps.isChecked() && !checkDigits.isChecked() && !checkSpecSymbols.isChecked()) {
                    mGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                            .useLower(true)
                            .useUpper(true)
                            .useDigits(false)
                            .useSpecSymbols(false)
                            .build();
                } else if (checkSpecSymbols.isChecked() && !checkCaps.isChecked() && !checkDigits.isChecked()) {
                    mGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                            .useLower(true)
                            .useUpper(false)
                            .useDigits(false)
                            .useSpecSymbols(true)
                            .build();
                } else if (checkDigits.isChecked() && checkCaps.isChecked() && !checkSpecSymbols.isChecked()) {
                    mGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                            .useLower(true)
                            .useUpper(true)
                            .useDigits(true)
                            .useSpecSymbols(false)
                            .build();
                } else if (checkDigits.isChecked() && checkSpecSymbols.isChecked() && !checkCaps.isChecked()) {
                    mGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                            .useLower(true)
                            .useUpper(false)
                            .useDigits(true)
                            .useSpecSymbols(true)
                            .build();
                } else if (checkCaps.isChecked() && checkSpecSymbols.isChecked() && !checkDigits.isChecked()) {
                    mGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                            .useLower(true)
                            .useUpper(true)
                            .useDigits(false)
                            .useSpecSymbols(true)
                            .build();
                } else if (checkDigits.isChecked() && checkCaps.isChecked() && checkSpecSymbols.isChecked()) {
                    mGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                            .useLower(true)
                            .useUpper(true)
                            .useDigits(true)
                            .useSpecSymbols(true)
                            .build();
                } else {
                    mGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                            .useLower(true)
                            .useUpper(false)
                            .useDigits(false)
                            .useSpecSymbols(false)
                            .build();
                }

                String password = mGenerator.generate(passwordLength);
                mGeneratedPassword.setText(password);
            }
        });
    }
}
