package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.Toast;

import com.example.instagram.fragments.ComposeFragment;
import com.example.instagram.fragments.PostsFragment;
import com.example.instagram.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private BottomNavigationView bottomNavigation;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigation);


        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.action_compose:
                    fragment = new ComposeFragment();
                    //Toast.makeText(this,"Compose fragment", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.action_home:
                    fragment = new PostsFragment();
//                    Toast.makeText(this,"Home fragment", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.action_profile:
                    fragment = new ProfileFragment();
//                    Toast.makeText(this,"Profile fragment", Toast.LENGTH_SHORT).show();
                    break;
                default: return true;
            }
            fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
            return true;
        });
        bottomNavigation.setSelectedItemId(R.id.action_home);
    }

}