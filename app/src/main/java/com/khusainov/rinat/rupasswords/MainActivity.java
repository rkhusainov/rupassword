package com.khusainov.rinat.rupasswords;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText sourceEditText;
    private TextView resultTextView;
    private String[] russians;
    private String[] latins;

    private PasswordsHelper helper;

    private View copyButton;

    private CompoundButton checkCaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sourceEditText = findViewById(R.id.edit_source);
        resultTextView = findViewById(R.id.text_result);
        copyButton = findViewById(R.id.btn_copy_password);
        checkCaps = findViewById(R.id.check_caps);
        checkCaps.isChecked();

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

//                generatedTextView = getResources().getQuantityText(R.plurals.symbols_count,count,count);
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
    }

}
