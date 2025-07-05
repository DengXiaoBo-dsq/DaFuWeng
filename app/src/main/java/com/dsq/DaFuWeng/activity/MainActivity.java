package com.dsq.DaFuWeng.activity;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.dsq.DaFuWeng.R;
import com.dsq.DaFuWeng.fragment.AccountFragment;
import com.dsq.DaFuWeng.fragment.FamilyFragment;
import com.dsq.DaFuWeng.fragment.ParticipationFragment;
import com.dsq.DaFuWeng.fragment.RichFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        // 设置默认选中项
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ParticipationFragment())
                .commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    int itemId = item.getItemId();

                    if (itemId == R.id.nav_participation) {
                        selectedFragment = new ParticipationFragment();
                    } else if (itemId == R.id.nav_family) {
                        selectedFragment = new FamilyFragment();
                    } else if (itemId == R.id.nav_rich) {
                        selectedFragment = new RichFragment();
                    } else if (itemId == R.id.nav_account) {
                        selectedFragment = new AccountFragment();
                    }

                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, selectedFragment)
                                .commit();
                    }

                    return true;
                }
            };

}