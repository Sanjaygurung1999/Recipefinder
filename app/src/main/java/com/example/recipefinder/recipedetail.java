package com.example.recipefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.recipefinder.adapter.MyAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONObject;

import java.util.HashMap;

public class recipedetail extends AppCompatActivity implements View.OnClickListener {
    ImageView recipeImage;
    TabLayout tabLayout;
    ViewPager viewPager;
    int checkFav=0;
    FloatingActionButton favbutton;
    String recipeId;
    TextView recipeName,recipeSummary,recipeServing,recipeTime;
    public static HashMap<Integer,String> Language=new HashMap<Integer, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipedetail);
        recipeId=getIntent().getStringExtra("recipeid");
        recipeName=findViewById(R.id.recipename);
        recipeSummary=findViewById(R.id.recipescore);
        recipeServing=findViewById(R.id.recipeserving);
        recipeTime=findViewById(R.id.recipetime);
        recipeImage=findViewById(R.id.recipeimage);
        favbutton=findViewById(R.id.favbutton);
        favbutton.setOnClickListener(this);
        //set recipe details
        getRecipeName();
        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewPager);
        tabLayout.addTab(tabLayout.newTab().setText("Instruction"));
        tabLayout.addTab(tabLayout.newTab().setText("Ingredients"));
        tabLayout.addTab(tabLayout.newTab().setText("Extras"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        final MyAdapter adapter = new MyAdapter(this,getSupportFragmentManager(), tabLayout.getTabCount(),recipeId);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.favbutton:
                if(checkFav==0){
                    favbutton.setColorFilter(R.color.main2);
                    favbutton.setImageResource(R.drawable.ic_heart_fill);
                    Toast.makeText(this, "Added to favourites", Toast.LENGTH_SHORT).show();
                    checkFav=1;
                }
                else{
                    favbutton.setColorFilter(R.color.main3);
                    favbutton.setImageResource(R.drawable.ic_heart_empty);
                    Toast.makeText(this, "Removed from favourites", Toast.LENGTH_SHORT).show();
                    checkFav=0;
                }
        }
    }
    public void getRecipeName() {
        String url = "https://sanjaygurung.000webhostapp.com/gethomeitem.php?id=" + recipeId;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    String recipeName = jsonObj.getString("title");
                    String recipeScore = jsonObj.getString("healthScore");
                    String recipeImage = jsonObj.getString("image");
                    String recipeCooking = jsonObj.getString("readyInMinutes") + " min";
                    String recipeServing = jsonObj.getString("servings") + " Person";
                    setDetails(recipeName,recipeScore, recipeImage, recipeCooking, recipeServing);
                } catch (Exception ex) {
                    Toast.makeText(recipedetail.this, "Recipe name error", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }
    public void setDetails(String recipeName,String recipeScore,String recipeImage,String recipeCooking,String recipeServing){
        this.recipeName.setText(recipeName);
        this.recipeSummary.setText("Health score is "+recipeScore);
        this.recipeServing.setText(recipeServing);
        this.recipeTime.setText(recipeCooking);
        Glide
                .with(getApplicationContext())
                .load(recipeImage)
                .centerCrop()
                .into(this.recipeImage);
    }
}