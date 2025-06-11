package com.cyberpokemon.plancraft;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegistrationActivity extends AppCompatActivity {

    EditText username;
    CheckBox termsandconditioncheckbox;
    Button continuebutton;

    public static final String PREF_NAME= "PlanCraftPreferences";
    public static final String KEY_IS_REGISTERED= "is_registered";
    public static final String KEY_USERNAME= "username";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);

        username=findViewById(R.id.username);
        termsandconditioncheckbox=findViewById(R.id.termsandconditioncheckbox);
        continuebutton=findViewById(R.id.continuebutton);

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        termsandconditioncheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateButtonState();
            }
        });

        continuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences(PREF_NAME,MODE_PRIVATE)
                        .edit()
                        .putString(KEY_USERNAME,username.getText().toString())
                        .putBoolean(KEY_IS_REGISTERED,true)
                        .apply();

                startActivity(new Intent(RegistrationActivity.this,TodoActivity.class));
                finish();

            }
        });
    }

    private void updateButtonState(){
        boolean isUsernameEmpty=username.getText().toString().isEmpty();
        boolean isTermsAndConditionChecked=termsandconditioncheckbox.isChecked();

        continuebutton.setEnabled((!isUsernameEmpty)&& isTermsAndConditionChecked);
    }
}