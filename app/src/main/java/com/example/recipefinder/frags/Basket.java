package com.example.recipefinder.frags;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.example.recipefinder.R;
import com.example.recipefinder.recipedetail;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Basket#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Basket extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Basket() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Basker.
     */
    // TODO: Rename and change types and number of parameters
    public static Basket newInstance(String param1, String param2) {
        Basket fragment = new Basket();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    ExtendedFloatingActionButton extended_search,extended_basket;
    RecyclerView recycle_ingredients,basket_recycle;
    SearchView search_ingregients;
    ArrayList<IngreModel> ingreitems=new ArrayList<>();
    RelativeLayout popoutrelative;
    HashMap<String,String> basketItems=new HashMap<>();
    TextView initialText;
    ArrayList<ContactModel3> recipeArray=new ArrayList<>();
    Animation animShake;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_basker, container, false);
        extended_search=root.findViewById(R.id.extended_search);
        extended_basket=root.findViewById(R.id.extended_basket);
        search_ingregients=root.findViewById(R.id.search_ingre);
        popoutrelative=root.findViewById(R.id.popup_basket);
        initialText=root.findViewById(R.id.textView37);
        basket_recycle=root.findViewById(R.id.basket_recycle);
        recycle_ingredients=root.findViewById(R.id.recycle_all_ing);
        recycle_ingredients.setLayoutManager(new LinearLayoutManager(getContext()));
        recycle_ingredients.setAdapter(new IngreAdapter());
        extended_basket.setOnClickListener(this);
        animShake= AnimationUtils.loadAnimation(getContext(), R.anim.shake);
        extended_search.shrink();
        search_ingregients.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                initialText.setVisibility(View.GONE);
                ingreitems.clear();
                showIngredients(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return root;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.extended_basket:
                if(popoutrelative.getVisibility()==View.VISIBLE){
                    popoutrelative.setVisibility(View.GONE);
                }
                else{
                    initialText.setVisibility(View.GONE);
                    popoutrelative.setVisibility(View.VISIBLE);
                    basket_recycle.setAdapter(new BasketAdapter());
                    basket_recycle.setLayoutManager(new LinearLayoutManager(getContext()));
                }
                break;
            case R.id.search_ingre:
                initialText.setVisibility(View.GONE);
                String totalText="";
                for (Map.Entry<String,String> entry:basketItems.entrySet()
                ) {
                    String entryValue=entry.getValue();
                    totalText=totalText+","+entryValue;
                }
                String lastText=totalText.replaceFirst(",","");
                getRecipebyIngre(lastText);
                Toast.makeText(getContext(),lastText+ "", Toast.LENGTH_SHORT).show();
        }
    }
    public void showIngredients(String name){
        String url="https://sanjaygurung.000webhostapp.com/searchIngredients.php?name="+name;
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray=new JSONArray(response);
                    for (int i=0;i<jsonArray.length();i++){
                        String id=jsonArray.getJSONObject(i).getString("id");
                        String name=jsonArray.getJSONObject(i).getString("name");
                        String image=jsonArray.getJSONObject(i).getString("image");
                        ingreitems.add(new IngreModel(id,name,image));
                        recycle_ingredients.setAdapter(new IngreAdapter());
                        recycle_ingredients.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                }
                catch (Exception ex){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(stringRequest);
    }
    public class IngreAdapter extends RecyclerView.Adapter<IngreViewHolder>{
        @NonNull
        @Override
        public IngreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View root = getLayoutInflater().inflate(R.layout.single_ingredient_layout,parent,false);
            IngreViewHolder ingreViewHolder = new IngreViewHolder(root);
            return ingreViewHolder;
        }
        @SuppressLint("ResourceType")
        @Override
        public void onBindViewHolder(@NonNull final IngreViewHolder holder, final int position) {
            holder.ingreName.setText(ingreitems.get(position).getname());
            Glide
                    .with(getContext())
                    .load("https://spoonacular.com/cdn/ingredients_100x100/"+ingreitems.get(position).getImage())
                    .into(holder.ingreImage);
            holder.ingredient_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    basketItems.put(ingreitems.get(position).getImage(),ingreitems.get(position).getname());
                    extended_basket.startAnimation(animShake);
                    Toast.makeText(getContext(), "Added to basked !", Toast.LENGTH_SHORT).show();
                }
            });
        }
        @Override
        public int getItemCount() {
            return ingreitems.size();
        }
    }
    public class IngreViewHolder extends RecyclerView.ViewHolder{
        ImageView ingreImage;
        TextView ingreName;
        LinearLayout ingredient_layout;
        public IngreViewHolder(@NonNull View itemView) {
            super(itemView);
            ingreName=itemView.findViewById(R.id.textView200);
            ingreImage=itemView.findViewById(R.id.imageView2);
            ingredient_layout=itemView.findViewById(R.id.ingredient_layout);
        }
    }
    public class IngreModel extends ViewModel {
        String id,name,image;
        public IngreModel(String id,String name,String image){
            this.id=id;
            this.name=name;
            this.image=image;
        }
        public String getIngreId(){return id;}
        public String getname(){return name;}
        public String getImage(){return image;}
    }
    public class BasketAdapter extends RecyclerView.Adapter<BasketViewHolder>{
        @NonNull
        @Override
        public BasketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View root = getLayoutInflater().inflate(R.layout.single_basket_layout,parent,false);
            BasketViewHolder basketViewHolder = new BasketViewHolder(root);
            return basketViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final BasketViewHolder holder, final int position) {
            final String basketname,basketimage;
            basketimage=basketItems.keySet().toArray()[position].toString();
            basketname=basketItems.get(basketimage);
            holder.ingreName.setText(basketname);
            Glide
                    .with(getContext())
                    .load("https://spoonacular.com/cdn/ingredients_100x100/"+basketimage)
                    .into(holder.ingreImage);
            holder.discardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    basketItems.remove(basketimage);
                    basket_recycle.removeAllViews();
                    basket_recycle.setAdapter(new BasketAdapter());
                    basket_recycle.setLayoutManager(new LinearLayoutManager(getContext()));
                }
            });
        }
        @Override
        public int getItemCount() {
            return basketItems.size();
        }
    }
    public class BasketViewHolder extends RecyclerView.ViewHolder{
        ImageView ingreImage;
        TextView ingreName;
        Button discardButton;
        public BasketViewHolder(@NonNull View itemView) {
            super(itemView);
            ingreName=itemView.findViewById(R.id.textView4);
            ingreImage=itemView.findViewById(R.id.imageView13);
            discardButton=itemView.findViewById(R.id.button5);
        }
    }
    public void getRecipebyIngre(String name){
        recycle_ingredients.removeAllViews();
        String url="https://sanjaygurung.000webhostapp.com/getrecipebyIngre.php?name="+name;
        RequestQueue requestQueue=Volley.newRequestQueue(getContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray=new JSONArray(response);
                    if(jsonArray.length()==0){
                        Toast.makeText(getContext(), "No Results", Toast.LENGTH_SHORT).show();
                    }
                    for (int i=0;i<jsonArray.length();i++){
                        String id=jsonArray.getJSONObject(i).getString("id");
                        String name=jsonArray.getJSONObject(i).getString("title");
                        String image=jsonArray.getJSONObject(i).getString("image");
                        recipeArray.add(new ContactModel3(id,name,image));
                        recycle_ingredients.setAdapter(new ContactAdapter3());
                        recycle_ingredients.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                }
                catch (Exception ex){
                    Toast.makeText(getContext(),ex+ "", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(stringRequest);
    }
    public class ContactAdapter3 extends RecyclerView.Adapter<ContactViewHolder3>{
        @NonNull
        @Override
        public ContactViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View root = getLayoutInflater().inflate(R.layout.fragment_home,parent,false);
            ContactViewHolder3 contactViewHolder = new ContactViewHolder3(root);
            return contactViewHolder;
        }
        @Override
        public void onBindViewHolder(@NonNull ContactViewHolder3 holder, int position) {
            holder.recipename.setText(recipeArray.get(position).getRecipeName());
            Glide
                    .with(getContext())
                    .load("https://spoonacular.com/recipeImages/"+recipeArray.get(position).getRecipeId()+"-556x370.jpg")
                    .into(holder.recipeImage);
            final String recipeId=recipeArray.get(position).getRecipeId();
            final String recipeName=recipeArray.get(position).getRecipeName();
            final String imagetoSend=recipeArray.get(position).getRecipeImage();
            holder.recipeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(getContext(), recipedetail.class);
                    i.putExtra("id",recipeId);
                    i.putExtra("recipename",recipeName);
                    i.putExtra("image",imagetoSend);
                    startActivity(i);
                }
            });
        }
        @Override
        public int getItemCount() {
            return recipeArray.size();
        }
    }
    public class ContactViewHolder3 extends RecyclerView.ViewHolder{
        TextView recipename;
        ImageView recipeImage;
        public ContactViewHolder3(@NonNull View itemView) {
            super(itemView);
            recipeImage=itemView.findViewById(R.id.imageView2);
            recipename=itemView.findViewById(R.id.textView200);
        }
    }
    public class ContactModel3 extends ViewModel{
        String id,name,image;
        public ContactModel3(String id,String name,String image){
            this.id=id;
            this.name=name;
            this.image=image;
        }
        public String getRecipeName(){return name;}
        public String getRecipeId(){return id;}
        public String getRecipeImage(){return image;}

    }
}