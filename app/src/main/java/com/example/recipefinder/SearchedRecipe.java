package com.example.recipefinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.recipefinder.frags.home;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class SearchedRecipe extends AppCompatActivity implements View.OnClickListener {
    ImageButton goBack;
    SearchView searchrecipe;
    String searchitem;
    ArrayList<ItemModel> itemList=new ArrayList<>();
    RecyclerView itemrecycle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_recipe);
        goBack=findViewById(R.id.returnbutton);
        searchrecipe=findViewById(R.id.searchrecipe);
        itemrecycle=findViewById(R.id.itemrecycle);
        itemrecycle.setAdapter(new ItemAdapter());
        itemrecycle.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        searchitem=getIntent().getStringExtra("searchrecipe");
        getItems(searchitem);
        goBack.setOnClickListener(this);
        searchrecipe.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                itemList.clear();
                itemrecycle.removeAllViews();
                getItems(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.returnbutton:
                finish();
                break;
        }
    }
    public void getItems(String recipename){
        String url ="https://sanjaygurung.000webhostapp.com/gethomeitem.php?name="+recipename;
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++){
                        String recipeId= jsonArray.getJSONObject(i).getString("id");
                        String recipeName = jsonArray.getJSONObject(i).getString("title");
                        String recipeImage = jsonArray.getJSONObject(i).getString("image");
                        itemList.add(new ItemModel(recipeId,recipeName,recipeImage));
                        itemrecycle.setAdapter(new ItemAdapter());
                        itemrecycle.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }
    public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder>{
        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View root = getLayoutInflater().inflate(R.layout.recipesinglelayout,parent,false);
            ItemViewHolder varietyViewHolder = new ItemViewHolder(root);
            return varietyViewHolder;
        }
        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
            holder.recipename.setText(itemList.get(position).getRecipename());
            Glide
                    .with(getApplicationContext())
                    .load("https://spoonacular.com/recipeImages/"+itemList.get(position).getRecipeid()+"-556x370.jpg")
                    .centerCrop()
                    .into(holder.recipeimage);
            holder.recipeimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(getApplicationContext(),recipedetail.class);
                    i.putExtra("recipeid",itemList.get(position).getRecipeid());
                    startActivity(i);
                }
            });
        }
        @Override
        public int getItemCount() {
            return itemList.size();
        }
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView recipeimage;
        TextView recipename;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            recipename=itemView.findViewById(R.id.recipename);
            recipeimage=itemView.findViewById(R.id.recipeimage);

        }
    }
    public class ItemModel extends ViewModel {
        String recipeid,recipename,recipeimage;
        public ItemModel(String recipeid,String recipename,String recipeimage){
            this.recipeid=recipeid;
            this.recipename=recipename;
            this.recipeimage=recipeimage;
        }
        public String getRecipename() {
            return recipename;
        }
        public String getRecipeid() {
            return recipeid;
        }
        public String getRecipeimage() {
            return recipeimage;
        }
    }
}