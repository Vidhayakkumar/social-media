package com.onechance.socialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.firebase.auth.FirebaseAuth;
import com.onechance.socialmedia.Fragments.AddFragment;
import com.onechance.socialmedia.Fragments.AddPostFragment;
import com.onechance.socialmedia.Fragments.HomeFragment;
import com.onechance.socialmedia.Fragments.NotificationFragment;
import com.onechance.socialmedia.Fragments.ProfileFragment;
import com.onechance.socialmedia.Fragments.SearchFragment;
import com.onechance.socialmedia.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth auth=FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        MainActivity.this.setTitle("My Profile");

        binding.toolbar.setVisibility(View.GONE);

        LoadFrag(new HomeFragment(), true);

        binding.bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int Id = item.getItemId();
                if (Id == R.id.home) {
                    binding.toolbar.setVisibility(View.GONE);
                    LoadFrag(new HomeFragment(), false);

                } else if (Id == R.id.notification) {
                    binding.toolbar.setVisibility(View.GONE);
                    LoadFrag(new NotificationFragment(), false);

                } else if (Id == R.id.add) {
                    binding.toolbar.setVisibility(View.GONE);
                    LoadFrag(new AddPostFragment(), false);

                } else if (Id == R.id.search) {
                    binding.toolbar.setVisibility(View.GONE);
                    LoadFrag(new SearchFragment(), false);

                } else {
                    binding.toolbar.setVisibility(View.VISIBLE);
                    LoadFrag(new ProfileFragment(), false);
                }
                return true;
            }
        });
    }

    private void LoadFrag(Fragment fragment, Boolean Flag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (Flag == true) {
            ft.add(R.id.container, fragment);
        } else {
            ft.replace(R.id.container, fragment);
        }
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_sample, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.setting:
                auth.signOut();
                Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}