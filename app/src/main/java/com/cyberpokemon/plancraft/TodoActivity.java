package com.cyberpokemon.plancraft;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cyberpokemon.plancraft.fragmentfiles.CompletedFragment;
import com.cyberpokemon.plancraft.fragmentfiles.OngoingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TodoActivity extends AppCompatActivity {

    public static final String PREF_NAME= "PlanCraftPreferences";
    public static final String KEY_USERNAME= "username";

    FrameLayout todoframelayout;
    BottomNavigationView bottomnavigationview;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_todo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0,0);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String username = sharedPreferences.getString(KEY_USERNAME, "User");

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Welcome, "+username);

        todoframelayout=findViewById(R.id.todoframelayout);
        bottomnavigationview=findViewById(R.id.bottomnavigationview);

        bottomnavigationview.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemid = item.getItemId();

                if(itemid==R.id.ongoingtab)
                {
                    loadFragment(new OngoingFragment(),true);
                }
                else if (itemid==R.id.completedtab)
                {
                    loadFragment(new CompletedFragment(),true);
                }
                else
                {
                    loadFragment(new OngoingFragment(),true);
                }


                return true;
            }
        });

        loadFragment(new OngoingFragment(),false);


    }

    private void loadFragment(Fragment fragment,boolean isAppInitialized)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(!isAppInitialized)
        {
            fragmentTransaction.add(R.id.todoframelayout,fragment);
        }
        else
        {
            fragmentTransaction.replace(R.id.todoframelayout,fragment);
        }
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}