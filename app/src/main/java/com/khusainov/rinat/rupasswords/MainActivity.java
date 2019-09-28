package com.khusainov.rinat.rupasswords;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText sourceEditText;
    private TextView resultTextView;
    private String[] russians;
    private String[] latins;

    private PasswordsHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sourceEditText = findViewById(R.id.edit_source);
        resultTextView = findViewById(R.id.text_result);

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
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
