package com.example.recipefinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.recipefinder.frags.RecipeEquip;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
public class UploadRecipe extends AppCompatActivity  implements View.OnClickListener, View.OnFocusChangeListener {
    TextView goback;
    Button uploadrecipe,addEquipment,addIngre,addInst;
    EditText addEquipmentName,addIngreName,addInstDetail,addrecipetime,addrecipeserv,addrecipename;
    ArrayList<ItemModel> recipeEquipment=new ArrayList<>();
    ArrayList<ItemModel> recipeIngre=new ArrayList<>();
    ArrayList<ItemModel> recipeInst=new ArrayList<>();
    RecyclerView equipmentRecycle,ingreRecycle,instRecycle;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseReference = mDatabase.getReference();
    SharedPreferences pref;
    String username,userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_recipe);
        pref= getSharedPreferences("userdetail", MODE_PRIVATE);
        username=pref.getString("username","");
        userid=pref.getString("userid","");
        goback=findViewById(R.id.goback);
        addrecipename=findViewById(R.id.addrecipename);
        addrecipeserv=findViewById(R.id.addrecipeserv);
        addrecipetime=findViewById(R.id.addrecipetime);
        addEquipment=findViewById(R.id.addEquipment);
        addEquipment.setOnClickListener(this);
        addIngre=findViewById(R.id.addIngredients);
        addIngre.setOnClickListener(this);
        addInst=findViewById(R.id.addInst);
        addInst.setOnClickListener(this);
        addEquipmentName=findViewById(R.id.addEquipmentName);
        addIngreName=findViewById(R.id.addIngredientsName);
        addInstDetail=findViewById(R.id.addInstDetail);
        equipmentRecycle=findViewById(R.id.equipmentrecycle);
        ingreRecycle=findViewById(R.id.ingredientsrecycle);
        instRecycle=findViewById(R.id.instrecycle);
        instRecycle.setAdapter(new InstAdapter());
        instRecycle.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
        ingreRecycle.setAdapter(new IngreAdapter());
        ingreRecycle.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
        equipmentRecycle.setAdapter(new EquipAdapter());
        equipmentRecycle.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
        goback.setOnClickListener(this);
        uploadrecipe=findViewById(R.id.uploadrecipe);
        uploadrecipe.setEnabled(false);
        uploadrecipe.setOnClickListener(this);
        //focus change
        addrecipename.setOnFocusChangeListener(this);
        addrecipeserv.setOnFocusChangeListener(this);
        addrecipetime.setOnFocusChangeListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.goback:
                finish();
                break;
            case R.id.uploadrecipe:
                String recipename=addrecipename.getText().toString();
                String recipetime=addrecipetime.getText().toString();
                String recipeserv=addrecipeserv.getText().toString();
                mDatabaseReference.child("recipes").push().setValue(new UserRecipeUpload(username,recipename,recipetime,recipeserv,recipeEquipment,recipeIngre,recipeInst));
                Toast.makeText(this, "Recipe uploaded", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.addEquipment:
                String addEquipmentNameValue=addEquipmentName.getText().toString();
                if(addEquipmentNameValue.equals("")){
                    Toast.makeText(this, "Equipment name is empty !", Toast.LENGTH_SHORT).show();
                }
                else{
                    recipeEquipment.add(new ItemModel(addEquipmentNameValue));
                    addEquipmentName.setText("");
                    equipmentRecycle.setAdapter(new EquipAdapter());
                    equipmentRecycle.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
                }
                break;
            case R.id.addIngredients:
                String addIngreNameValue=addIngreName.getText().toString();
                if(addIngreNameValue.equals("")){
                    Toast.makeText(this, "Equipment name is empty !", Toast.LENGTH_SHORT).show();
                }
                else {
                    recipeIngre.add(new ItemModel(addIngreNameValue));
                    addIngreName.setText("");
                    ingreRecycle.setAdapter(new IngreAdapter());
                    ingreRecycle.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                }
                break;
            case R.id.addInst:
                String addInstNameValue=addInstDetail.getText().toString();
                if(addInstNameValue.equals("")){
                    Toast.makeText(this, "Equipment name is empty !", Toast.LENGTH_SHORT).show();
                }
                else {
                    recipeInst.add(new ItemModel(addInstNameValue));
                    addInstDetail.setText("");
                    instRecycle.setAdapter(new InstAdapter());
                    instRecycle.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                }
                break;
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
            if(addrecipetime.getText().toString().equals("") || addrecipeserv.getText().toString().equals("") || addrecipename.getText().toString().equals("")){
                uploadrecipe.setEnabled(false);
            }
            else{
                uploadrecipe.setEnabled(true);
            }
    }

    public class EquipAdapter extends RecyclerView.Adapter<EquipViewHolder>{
        @NonNull
        @Override
        public EquipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View root = getLayoutInflater().inflate(R.layout.added_item_upload,parent,false);
            EquipViewHolder equipViewHolder = new EquipViewHolder(root);
            return equipViewHolder;
        }
        @Override
        public void onBindViewHolder(@NonNull EquipViewHolder holder, final int position) {
            holder.itemname.setText(recipeEquipment.get(position).getName());
            holder.deleteitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recipeEquipment.remove(position);
                    notifyDataSetChanged();
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.framelayout),"Item removed", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    snackbar.show();
                }
            });
        }
        @Override
        public int getItemCount() {
            return recipeEquipment.size();
        }
    }
    public class EquipViewHolder extends RecyclerView.ViewHolder {
        TextView itemname;
        ImageButton deleteitem;
        public EquipViewHolder(@NonNull View itemView) {
            super(itemView);
            itemname=itemView.findViewById(R.id.itemname);
            deleteitem=itemView.findViewById(R.id.deleteitem);
        }
    }
    public class IngreAdapter extends RecyclerView.Adapter<IngreViewHolder>{
        @NonNull
        @Override
        public IngreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View root = getLayoutInflater().inflate(R.layout.added_item_upload,parent,false);
            IngreViewHolder ingreViewHolder = new IngreViewHolder(root);
            return ingreViewHolder;
        }
        @Override
        public void onBindViewHolder(@NonNull IngreViewHolder holder, final int position) {
            holder.itemname.setText(recipeIngre.get(position).getName());
            holder.deleteitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recipeIngre.remove(position);
                    notifyDataSetChanged();
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.framelayout),"Item removed", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    snackbar.show();
                }
            });
        }
        @Override
        public int getItemCount() {
            return recipeIngre.size();
        }
    }
    public class IngreViewHolder extends RecyclerView.ViewHolder {
        TextView itemname;
        ImageButton deleteitem;
        public IngreViewHolder(@NonNull View itemView) {
            super(itemView);
            itemname=itemView.findViewById(R.id.itemname);
            deleteitem=itemView.findViewById(R.id.deleteitem);
        }
    }
    public class InstAdapter extends RecyclerView.Adapter<InstViewHolder>{
        @NonNull
        @Override
        public InstViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View root = getLayoutInflater().inflate(R.layout.added_item_upload,parent,false);
            InstViewHolder instViewHolder = new InstViewHolder(root);
            return instViewHolder;
        }
        @Override
        public void onBindViewHolder(@NonNull InstViewHolder holder, final int position) {
            holder.itemname.setText(recipeInst.get(position).getName());
            holder.deleteitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recipeInst.remove(position);
                    notifyDataSetChanged();
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.framelayout),"Item removed", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    snackbar.show();
                }
            });
        }
        @Override
        public int getItemCount() {
            return recipeInst.size();
        }
    }
    public class InstViewHolder extends RecyclerView.ViewHolder {
        TextView itemname;
        ImageButton deleteitem;
        public InstViewHolder(@NonNull View itemView) {
            super(itemView);
            itemname=itemView.findViewById(R.id.itemname);
            deleteitem=itemView.findViewById(R.id.deleteitem);
        }
    }
    public class ItemModel extends ViewModel {
        String name;
        public ItemModel(String name){
            this.name=name;
        }
        public String getName(){
            return name;
        }
    }
}