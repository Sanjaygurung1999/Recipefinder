package com.example.recipefinder.frags;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.recipefinder.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeEquip#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeEquip extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecipeEquip() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeEquip.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeEquip newInstance(String param1, String param2) {
        RecipeEquip fragment = new RecipeEquip();
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
    String recipeId;
    RecyclerView toolRecycle,nutriRecycle;
    ArrayList<NutriModel> nutriList=new ArrayList<>();
    ArrayList<EquipModel> equipList=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_recipe_equip, container, false);
        recipeId=getArguments().getString("recipeid");
        toolRecycle=root.findViewById(R.id.equipmentrecycle);
        nutriRecycle=root.findViewById(R.id.nutrirecycle);
        getEquipments();
        getNutri();
        return root;
    }
    public void getEquipments()
    {
        String url="https://sanjaygurung.000webhostapp.com/getEquipment.php?id="+recipeId;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("equipment");
                    if(jsonArray.length()>0){
                        for (int i = 0; i <jsonArray.length() ; i++) {
                            String name=jsonArray.getJSONObject(i).getString("name");
                            String image=jsonArray.getJSONObject(i).getString("image");
                            equipList.add(new EquipModel(name,image));
                            toolRecycle.setAdapter(new EquipAdapter());
                            toolRecycle.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                        }
                    }
                }
                catch (Exception ex){
                    Toast.makeText(getContext(), "Equipment error", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }
    public class EquipAdapter extends RecyclerView.Adapter<EquipViewHolder>{

        @NonNull
        @Override
        public EquipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View root = getLayoutInflater().inflate(R.layout.fragment_recipe_item_single,parent,false);
            EquipViewHolder contactViewHolder = new EquipViewHolder(root);
            return contactViewHolder;
        }
        @Override
        public void onBindViewHolder(@NonNull EquipViewHolder holder, int position) {
            holder.ingredientName.setText(equipList.get(position).getName());
            Glide
                    .with(getContext()) // replace with 'this' if it's in activity
                    .load("https://spoonacular.com/cdn/equipment_100x100/"+equipList.get(position).getImage())
                    .into(holder.ingredientImage);
        }
        @Override
        public int getItemCount() {
            return equipList.size();
        }
    }
    public class EquipViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientName;
        ImageView ingredientImage;
        public EquipViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientName=itemView.findViewById(R.id.textView6);
            ingredientImage=itemView.findViewById(R.id.imageView11);
        }
    }
    public class EquipModel extends ViewModel {
        String name,image;
        public EquipModel(String name,String image){
            this.name=name;
            this.image=image;
        }
        public String getName(){
            return name;
        }
        public String getImage(){
            return image;
        }
    }
    public void getNutri(){
        String badurl="https://sanjaygurung.000webhostapp.com/getnutrition.php?type=bad&id="+recipeId;
        String goodurl="https://sanjaygurung.000webhostapp.com/getnutrition.php?type=good&id="+recipeId;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, badurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i <jsonArray.length(); i++) {
                        String name=jsonArray.getJSONObject(i).getString("title");
                        String amount=jsonArray.getJSONObject(i).getString("amount");
                        nutriList.add(new NutriModel(name,amount));
                        nutriRecycle.setAdapter(new NutriAdapter());
                        nutriRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                }
                catch (Exception ex){
                    Toast.makeText(getContext(), "Calories error", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        StringRequest stringRequest1= new StringRequest(Request.Method.GET, goodurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i <jsonArray.length(); i++) {
                        String name=jsonArray.getJSONObject(i).getString("title");
                        String amount=jsonArray.getJSONObject(i).getString("amount");
                        nutriList.add(new NutriModel(name,amount));
                        nutriRecycle.setAdapter(new NutriAdapter());
                        nutriRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                }
                catch (Exception ex){
                    Toast.makeText(getContext(), "Calories error", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(stringRequest);
        requestQueue.add(stringRequest1);
    }
    public class NutriAdapter extends RecyclerView.Adapter<NutriViewHolder>{
        @NonNull
        @Override
        public NutriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View root = getLayoutInflater().inflate(R.layout.nutritionlayout,parent,false);
            NutriViewHolder nutriViewHolder = new NutriViewHolder(root);
            return nutriViewHolder;
        }
        @Override
        public void onBindViewHolder(@NonNull NutriViewHolder holder, int position) {
            holder.nutritionName.setText(nutriList.get(position).getName());
            holder.nutritionValue.setText(nutriList.get(position).getAmount());
        }
        @Override
        public int getItemCount() {
            return nutriList.size();
        }
    }
    public class NutriViewHolder extends RecyclerView.ViewHolder {
        TextView nutritionName,nutritionValue;
        public NutriViewHolder(@NonNull View itemView) {
            super(itemView);
            nutritionName=itemView.findViewById(R.id.textView31);
            nutritionValue=itemView.findViewById(R.id.textView30);
        }
    }
    public class NutriModel extends ViewModel {
        String name,amount;
        public NutriModel(String name,String amount){
            this.name=name;
            this.amount=amount;
        }
        public String getName(){
            return name;
        }
        public String getAmount(){
            return amount;
        }
    }
}