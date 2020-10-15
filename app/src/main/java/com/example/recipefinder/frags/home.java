package com.example.recipefinder.frags;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
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
import com.example.recipefinder.R;
import com.example.recipefinder.SearchedRecipe;
import com.example.recipefinder.recipedetail;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class home extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment home.
     */
    // TODO: Rename and change types and number of parameters
    public static home newInstance(String param1, String param2) {
        home fragment = new home();
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
    RecyclerView varietyrecycle,popularrecycle,bestsaladrecycle;
    ArrayList<VarietyModel> varietyitems=new ArrayList<>();
    ArrayList<ChoicesModel> popularitems=new ArrayList<>();
    ArrayList<ChoicesModel> saladitems=new ArrayList<>();
    ImageButton filterButton,featuredImage;
    String featuredId;
    SearchView recipeSearch;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_home, container, false);
        varietyrecycle=root.findViewById(R.id.varietyrecycle);
        featuredImage=root.findViewById(R.id.featuredimage);
        popularrecycle=root.findViewById(R.id.popularrecycle);
        bestsaladrecycle=root.findViewById(R.id.bestsaladrecycle);
        filterButton=root.findViewById(R.id.filterbutton);
        recipeSearch=root.findViewById(R.id.searchrecipe);
        filterButton.setOnClickListener(this);
        varietyrecycle.setAdapter(new VarietyAdapter());
        varietyrecycle.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        popularrecycle.setAdapter(new PopularAdapter());
        popularrecycle.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        bestsaladrecycle.setAdapter(new SaladAdapter());
        bestsaladrecycle.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        recipeSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Intent i=new Intent(getContext(), SearchedRecipe.class);
                i.putExtra("searchrecipe",s);
                startActivity(i);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        setVarietyItems();
        setFeaturedImage();
        setSaladItems();
        setPopularItems();
        return root;
    }
    public void setVarietyItems(){
        varietyitems.add(new VarietyModel("Pizza",R.drawable.ic_pizza));
        varietyitems.add(new VarietyModel("Deserts",R.drawable.ic_ice_cream_cone));
        varietyitems.add(new VarietyModel("Fried food",R.drawable.ic_french_fries));
        varietyitems.add(new VarietyModel("Dumplings",R.drawable.ic_dumpling));
        varietyitems.add(new VarietyModel("Burger",R.drawable.ic_burger));
        varietyitems.add(new VarietyModel("Salad",R.drawable.ic_salad));
        varietyrecycle.setAdapter(new VarietyAdapter());
        varietyrecycle.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
    }
    public void setFeaturedImage(){
        featuredId="284420";
        Glide
                .with(getContext())
                .load("https://spoonacular.com/recipeImages/"+featuredId+"-556x370.jpg")
                .centerCrop()
                .into(featuredImage);
        featuredImage.setOnClickListener(this);
    }
    public void setSaladItems(){
        String url ="https://sanjaygurung.000webhostapp.com/gethomeitem.php?name=salad";
        RequestQueue requestQueue=Volley.newRequestQueue(getContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++){
                        String recipeId= jsonArray.getJSONObject(i).getString("id");
                        String recipeName = jsonArray.getJSONObject(i).getString("title");
                        String recipeImage = jsonArray.getJSONObject(i).getString("image");
                        saladitems.add(new ChoicesModel(recipeId,recipeName,recipeImage));
                        bestsaladrecycle.setAdapter(new SaladAdapter());
                        bestsaladrecycle.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
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
    public void setPopularItems(){
        String url ="https://sanjaygurung.000webhostapp.com/gethomeitem.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++){
                        String recipeId= jsonArray.getJSONObject(i).getString("id");
                        String recipeName = jsonArray.getJSONObject(i).getString("title");
                        String recipeImage = jsonArray.getJSONObject(i).getString("image");
                        popularitems.add(new ChoicesModel(recipeId,recipeName,recipeImage));
                        popularrecycle.setAdapter(new PopularAdapter());
                        popularrecycle.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
                    }
                } catch (Exception ex) {
                    Toast.makeText(getContext(), ex+"", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.filterbutton:
                showDialog();
                break;
            case R.id.featuredimage:
                Intent i=new Intent(getContext(), recipedetail.class);
                i.putExtra("recipeid",featuredId);
                startActivity(i);
                break;
        }
    }
    void showDialog() {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        // Create and show the dialog.
        DialogFragment newFragment = DialogFilter.newInstance(2);
        newFragment.show(ft, "dialog");
    }

    public class VarietyAdapter extends RecyclerView.Adapter<VarietyViewHolder>{
        @NonNull
        @Override
        public VarietyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View root = getLayoutInflater().inflate(R.layout.fragment_varietylayout,parent,false);
            VarietyViewHolder varietyViewHolder = new VarietyViewHolder(root);
            return varietyViewHolder;
        }
        @Override
        public void onBindViewHolder(@NonNull VarietyViewHolder holder, final int position) {
            holder.varietyName.setText(varietyitems.get(position).getName());
            holder.varietyImage.setBackgroundResource(varietyitems.get(position).getImage());
            holder.variteylayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(getContext(), SearchedRecipe.class);
                    i.putExtra("searchrecipe",varietyitems.get(position).getName());
                    startActivity(i);
                }
            });
        }
        @Override
        public int getItemCount() {
            return varietyitems.size();
        }
    }
    public class VarietyViewHolder extends RecyclerView.ViewHolder {
        ImageView varietyImage;
        TextView varietyName;
        CardView variteylayout;
        public VarietyViewHolder(@NonNull View itemView) {
            super(itemView);
            varietyImage=itemView.findViewById(R.id.varietyphoto);
            varietyName=itemView.findViewById(R.id.varietyname);
            variteylayout=itemView.findViewById(R.id.varietylayout);
        }
    }
    public class VarietyModel extends ViewModel {
        String name;
        int image;
        public VarietyModel(String name,int image){
            this.name=name;
            this.image=image;
        }
        private String getName(){ return name;}
        private int getImage(){ return image;}
    }
    public class PopularAdapter extends RecyclerView.Adapter<PopularViewHolder>{
        @NonNull
        @Override
        public PopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View root = getLayoutInflater().inflate(R.layout.fragment_choicesfrag,parent,false);
            PopularViewHolder popularViewHolder= new PopularViewHolder(root);
            return popularViewHolder;
        }
        @Override
        public void onBindViewHolder(@NonNull PopularViewHolder holder, final int position) {
            holder.popularName.setText(popularitems.get(position).getName());
            Glide
                    .with(getContext())
                    .load("https://spoonacular.com/recipeImages/"+popularitems.get(position).getId()+"-556x370.jpg")
                    .centerCrop()
                    .into(holder.popularImage);
            holder.popularImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(getContext(),recipedetail.class);
                    i.putExtra("recipeid",popularitems.get(position).getId());
                    startActivity(i);
                }
            });
    }
        @Override
        public int getItemCount() {
            return popularitems.size();
        }
    }
    public class PopularViewHolder extends RecyclerView.ViewHolder {
        ImageView popularImage;
        TextView popularName;
        public PopularViewHolder(@NonNull View itemView) {
            super(itemView);
            popularImage=itemView.findViewById(R.id.choiceimage);
            popularName=itemView.findViewById(R.id.choicename);
        }
    }
    public class SaladAdapter extends RecyclerView.Adapter<SaladViewHolder>{
        @NonNull
        @Override
        public SaladViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View root = getLayoutInflater().inflate(R.layout.fragment_choicesfrag,parent,false);
            SaladViewHolder saladViewHolder= new SaladViewHolder(root);
            return saladViewHolder;
        }
        @Override
        public void onBindViewHolder(@NonNull SaladViewHolder holder, final int position) {
            holder.saladName.setText(saladitems.get(position).getName());
            Glide
                    .with(getContext())
                    .load("https://spoonacular.com/recipeImages/"+saladitems.get(position).getId()+"-556x370.jpg")
                    .centerCrop()
                    .into(holder.saladImage);
            holder.saladImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(getContext(),recipedetail.class);
                    i.putExtra("recipeid",saladitems.get(position).getId());
                    startActivity(i);
                }
            });
        }
        @Override
        public int getItemCount() {
            return saladitems.size();
        }
    }
    public class SaladViewHolder extends RecyclerView.ViewHolder {
        ImageView saladImage;
        TextView saladName;
        public SaladViewHolder(@NonNull View itemView) {
            super(itemView);
            saladImage=itemView.findViewById(R.id.choiceimage);
            saladName=itemView.findViewById(R.id.choicename);
        }
    }
    public class ChoicesModel extends ViewModel {
        String id,name,image;
        public ChoicesModel(String id,String name,String image){
            this.id=id;
            this.name=name;
            this.image=image;
        }
        private String getId(){return id;}
        private String getName(){ return name;}
        private String getImage(){ return image;}
    }
}