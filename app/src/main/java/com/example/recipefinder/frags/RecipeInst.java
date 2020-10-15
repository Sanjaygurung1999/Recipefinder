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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.recipefinder.R;
import com.example.recipefinder.recipedetail;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeInst#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeInst extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecipeInst() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeInst.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeInst newInstance(String param1, String param2) {
        RecipeInst fragment = new RecipeInst();
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
    ArrayList<ContactModel> arrayList=new ArrayList<>();
    RecyclerView recycle_instruction;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_recipe_inst, container, false);
        recipeId=getArguments().getString("recipeid");
        recycle_instruction=root.findViewById(R.id.recipe_instruction);
        recycle_instruction.setAdapter(new ContactAdapter());
        recycle_instruction.setLayoutManager(new LinearLayoutManager(getActivity()));
        getRecipeInstruction();
        return root;
    }
    public void getRecipeInstruction()
    {
        String url="https://sanjaygurung.000webhostapp.com/getrecipeInstruction.php?id="+recipeId;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if(jsonArray.length()>0){
                        JSONObject insideobj=jsonArray.getJSONObject(0);
                        JSONArray steps=insideobj.getJSONArray("steps");
                        for (int i = 0; i <steps.length() ; i++) {
                            String instruction=steps.getJSONObject(i).getString("step");
                            arrayList.add(new ContactModel(instruction));
                            recycle_instruction.setAdapter(new ContactAdapter());
                            recycle_instruction.setLayoutManager(new LinearLayoutManager(getActivity()));
                        }
                    }
                }
                catch (Exception ex){
                    Toast.makeText(getContext(), "Recipe ins error", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }
    public class ContactAdapter extends RecyclerView.Adapter<ContactViewHolder>{

        @NonNull
        @Override
        public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View root = getLayoutInflater().inflate(R.layout.fragment_singleinstruction,parent,false);
            ContactViewHolder contactViewHolder = new ContactViewHolder(root);
            return contactViewHolder;
        }
        @Override
        public void onBindViewHolder(@NonNull final ContactViewHolder holder, final int position) {
            holder.recipeInstruction.setText(arrayList.get(position).getInstruction());
            recipedetail.Language.put(position,arrayList.get(position).getInstruction());
            holder.recipeInstruction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recipedetail.Language.size()==arrayList.size()){
                        recipedetail.Language.clear();
                    }
                    if(holder.recipeInstruction.getHint().equals("normal")){
                        holder.recipeInstruction.setBackgroundResource(R.drawable.gradientred);
                        holder.recipeInstruction.setTextColor(getResources().getColor(R.color.white));
                        holder.recipeInstruction.setHint("notnormal");
                        recipedetail.Language.put(position,arrayList.get(position).getInstruction());
                    }
                    else{
                        holder.recipeInstruction.setBackgroundResource(R.color.white);
                        holder.recipeInstruction.setTextColor(getResources().getColor(R.color.default_secondary));
                        holder.recipeInstruction.setHint("normal");
                        recipedetail.Language.remove(position);
                    }
                }
            });
        }
        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }
    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView recipeInstruction;
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeInstruction=itemView.findViewById(R.id.textView24);
        }
    }
    public class ContactModel extends ViewModel {
        String instruction;
        public ContactModel(String instruction){
            this.instruction=instruction;
        }
        public String getInstruction(){
            return instruction;
        }
    }
}