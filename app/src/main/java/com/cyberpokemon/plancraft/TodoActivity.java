package com.cyberpokemon.plancraft;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

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

    FrameLayout todoframelayout;
    BottomNavigationView bottomnavigationview;
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
}