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
 * Use the {@link RecipeIngre#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeIngre extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecipeIngre() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeIngre.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeIngre newInstance(String param1, String param2) {
        RecipeIngre fragment = new RecipeIngre();
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
    ArrayList<IngreModel> ingreList=new ArrayList<>();
    RecyclerView ingreRecycle;
    String recipeId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_recipe_ingre, container, false);
        ingreRecycle=root.findViewById(R.id.recycle_ingre_recipedet);
        ingreRecycle.setAdapter(new IngreAdapter());
        recipeId = getArguments().getString("recipeid");
        getIngredients();
        return root;
    }
    public void getIngredients()
    {
        String url="https://sanjaygurung.000webhostapp.com/getingredients.php?id="+recipeId;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("-----",response);
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i <jsonArray.length() ; i++) {
                        String name=jsonArray.getJSONObject(i).getString("name");
                        String image=jsonArray.getJSONObject(i).getString("image");
                        JSONObject amount=jsonArray.getJSONObject(i).getJSONObject("amount");
                        JSONObject metric=amount.getJSONObject("metric");
                        String value=metric.getString("value");
                        String unit=metric.getString("unit");
                        ingreList.add(new IngreModel(name,image,value,unit));
                        ingreRecycle.setAdapter(new IngreAdapter());
                        ingreRecycle.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                    }
                }
                catch (Exception ex){
                    Toast.makeText(getActivity(), ex+"", Toast.LENGTH_SHORT).show();
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
            View root = getLayoutInflater().inflate(R.layout.fragment_recipe_item_single,parent,false);
            IngreViewHolder ingreViewHolder = new IngreViewHolder(root);
            return ingreViewHolder;
        }
        @Override
        public void onBindViewHolder(@NonNull IngreViewHolder holder, int position) {
            holder.ingredientName.setText(ingreList.get(position).getName());
            Glide
                    .with(getContext()) // replace with 'this' if it's in activity
                    .load("https://spoonacular.com/cdn/ingredients_100x100/"+ingreList.get(position).getImage())
                    .into(holder.ingredientImage);
            String totalAmount=ingreList.get(position).getValue()+ingreList.get(position).getUnit();
            holder.ingredientAmount.setText(totalAmount);
        }
        @Override
        public int getItemCount() {
            return ingreList.size();
        }
    }
    public class IngreViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientName,ingredientAmount;
        ImageView ingredientImage;
        public IngreViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientName=itemView.findViewById(R.id.textView6);
            ingredientImage=itemView.findViewById(R.id.imageView11);
            ingredientAmount=itemView.findViewById(R.id.textView11);
        }
    }
    public class IngreModel extends ViewModel {
        String name,image,value,unit;
        public IngreModel(String name,String image,String value,String unit){
            this.name=name;
            this.image=image;
            this.value=value;
            this.unit=unit;
        }
        public String getName(){
            return name;
        }
        public String getImage(){
            return image;
        }
        public String getValue(){
            return value;
        }
        public String getUnit(){
            return unit;
        }
    }
}