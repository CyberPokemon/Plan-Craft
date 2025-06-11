package com.cyberpokemon.plancraft;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    public static final String PREF_NAME= "PlanCraftPreferences";
    public static final String KEY_IS_REGISTERED= "is_registered";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        sharedPreferences=getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        boolean isRegistered=sharedPreferences.getBoolean(KEY_IS_REGISTERED,false);

        if(isRegistered)
        {
            startActivity(new Intent(MainActivity.this,TodoActivity.class));
        }
        else
        {
            startActivity(new Intent(MainActivity.this,RegistrationActivity.class));
        }
        finish();
    }
}