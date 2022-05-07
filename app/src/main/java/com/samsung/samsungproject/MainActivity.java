package com.samsung.samsungproject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.samsung.samsungproject.fragments.ProfileFragment;
import com.samsung.samsungproject.fragments.TaskFragment;
import com.samsung.samsungproject.fragments.UsersFragment;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigationView;
    ProfileFragment profileFragment = new ProfileFragment();
    TaskFragment taskFragment = new TaskFragment();
    UsersFragment usersFragment = new UsersFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_tasks:
                        openFragment(taskFragment);
                        return true;
                    case R.id.nav_users:
                        openFragment(usersFragment);
                        return true;
                    case R.id.nav_profile:
                        openFragment(profileFragment);
                        return true;
                }
                return false;
            }
        });
        openFragment(taskFragment);
    }

    void initViews() {
        navigationView = findViewById(R.id.bottomNavigationView);
    }

    void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}