package com.example.recipefinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.recipefinder.frags.Basket;
import com.example.recipefinder.frags.Feed;
import com.example.recipefinder.frags.home;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    String username,userid;
    BottomNavigationView bottomNavigationView;
    FragmentTransaction ft;
    Fragment currentFragment=null;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ft = getSupportFragmentManager().beginTransaction();
        pref= getSharedPreferences("userdetail", MODE_PRIVATE);
        username=pref.getString("username","");
        userid=pref.getString("userid","");
        currentFragment = new home();
        ft.replace(R.id.mainframe, currentFragment);
        ft.commit();
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.search:
                        ft = getSupportFragmentManager().beginTransaction();
                        currentFragment = new home();
                        ft.replace(R.id.mainframe, currentFragment);
                        ft.commit();
                        break;
                    case R.id.basket:
                        ft = getSupportFragmentManager().beginTransaction();
                        currentFragment = new Basket();
                        ft.replace(R.id.mainframe, currentFragment);
                        ft.commit();
                        break;
                    case R.id.feed:
                        ft = getSupportFragmentManager().beginTransaction();
                        currentFragment = new Feed();
                        ft.replace(R.id.mainframe, currentFragment);
                        ft.commit();
                        break;
                    case R.id.fav:
                        Toast.makeText(MainActivity.this, "Fav", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }
}