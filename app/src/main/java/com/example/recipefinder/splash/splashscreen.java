package com.example.recipefinder.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.recipefinder.MainActivity;
import com.example.recipefinder.R;

public class splashscreen extends AppCompatActivity {
    ImageView splashscreenImage,spinningImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        splashscreenImage=findViewById(R.id.splashscreen);
        spinningImage=findViewById(R.id.spinningicon);
        splashscreenImage.setBackgroundResource(R.raw.splashscreen);
        spinningImage.startAnimation(
                AnimationUtils.loadAnimation(this, R.anim.rotate_infinte) );
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent start = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(start);
                finish();
            }
        }, 2500);
    }
}