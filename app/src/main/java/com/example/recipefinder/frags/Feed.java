package com.example.recipefinder.frags;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.recipefinder.R;
import com.example.recipefinder.Signin;
import com.example.recipefinder.Signup;
import com.example.recipefinder.UploadRecipe;
import com.example.recipefinder.VolleyMultipartRequest;
import com.example.recipefinder.extra.EndPoints;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Feed#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Feed extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Feed() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Feed.
     */
    // TODO: Rename and change types and number of parameters
    public static Feed newInstance(String param1, String param2) {
        Feed fragment = new Feed();
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
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseReference = mDatabase.getReference();
    ArrayList<FeedModel> feedlist=new ArrayList<>();
    CircleImageView tosignup,popuppicture;
    String username,userid;
    SharedPreferences pref;
    TextView username_top,popup_username,touploadrecipe,tosignout;
    FrameLayout popup;
    ImageButton shutPopup;
    Button uploadpicture;
    EndPoints endPoints;
    RecyclerView feedrecycle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_feed, container, false);
        pref= getContext().getSharedPreferences("userdetail", Context.MODE_PRIVATE);
        username=pref.getString("username","");
        userid=pref.getString("userid","");
        popup_username=root.findViewById(R.id.textView18);
        popup=root.findViewById(R.id.popup);
        tosignout=root.findViewById(R.id.tosignout);
        tosignout.setOnClickListener(this);
        touploadrecipe=root.findViewById(R.id.touploadrecipe);
        touploadrecipe.setOnClickListener(this);
        shutPopup=root.findViewById(R.id.shutpopup);
        shutPopup.setOnClickListener(this);
        username_top=root.findViewById(R.id.username_top);
        tosignup=root.findViewById(R.id.tosignup);
        tosignup.setOnClickListener(this);
        uploadpicture=root.findViewById(R.id.uploadpicture);
        uploadpicture.setOnClickListener(this);
        popuppicture=root.findViewById(R.id.popuppicture);
        feedrecycle =root.findViewById(R.id.feedrecycle);
        feedrecycle.setAdapter(new FeedAdapter());
        feedrecycle.setLayoutManager(new LinearLayoutManager(getContext()));
        setUserName(username);
        getFeedList();
        return root;
    }
    public void getFeedList(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("recipes");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Result will be holded Here
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            String recipeName=dsp.child("recipeName").getValue().toString();
                            String uploadBy=dsp.child("uploadBy").getValue().toString();
                            feedlist.add(new FeedModel(dsp.getKey(),recipeName,uploadBy));
                            feedrecycle.setAdapter(new FeedAdapter());
                            feedrecycle.setLayoutManager(new LinearLayoutManager(getContext()));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tosignup:
                if(username.equals("")){
                    Intent i=new Intent(getContext(), Signup.class);
                    startActivity(i);
                }
                else{
                    popup.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.uploadpicture:
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
                break;
            case R.id.shutpopup:
                popup.setVisibility(GONE);
                break;
            case R.id.touploadrecipe:
                if(username.equals("")){
                    Toast.makeText(getContext(), "Please login first", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent gotoUpload=new Intent(getContext(), UploadRecipe.class);
                    startActivity(gotoUpload);
                }
                break;
            case R.id.tosignout:
                pref.edit().clear().apply();
                Intent signout=new Intent(getContext(), Signin.class);
                startActivity(signout);
                break;
        }
    }
    public void setUserName(String username){
        if(username.equals("")){
            username_top.setText("User");
        }
        else{
            username_top.setText(username);
            popup_username.setText(username);
            Glide
                    .with(getContext())
                    .load("http://192.168.1.64/receipefinderweb/pics/"+username+".jpg")
                    .centerCrop()
                    .into(popuppicture);
            Glide
                    .with(getContext())
                    .load("http://192.168.1.64/receipefinderweb/pics/"+username+".jpg")
                    .centerCrop()
                    .into(tosignup);
        }
    }
    //upload picture part
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                //displaying selected image to imageview
                //imageView.setImageBitmap(bitmap);

                //calling the method uploadBitmap to upload image
                uploadBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /*
     * The method is taking Bitmap as an argument
     * then it will return the byte[] array for the given bitmap
     * and we will send this array to the server
     * here we are using PNG Compression with 80% quality
     * you can give quality between 0 to 100
     * 0 means worse quality
     * 100 means best quality
     * */
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {
        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, endPoints.ROOT_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Toast.makeText(getContext(), "Picture Uploaded", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                return params;
            }
            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            public Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("pic", new DataPart(username+ ".jpg", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };
        //adding the request to volley
        Volley.newRequestQueue(getContext()).add(volleyMultipartRequest);
    }
    //end of upload part
    public class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder>{
        @NonNull
        @Override
        public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View root = getLayoutInflater().inflate(R.layout.user_recipe_single,parent,false);
            FeedViewHolder varietyViewHolder = new FeedViewHolder(root);
            return varietyViewHolder;
        }
        @Override
        public void onBindViewHolder(@NonNull final FeedViewHolder holder, final int position) {
            holder.recipename.setText(feedlist.get(position).getRecipename());
            Glide
                    .with(getContext())
                    .load("http://192.168.1.64/receipefinderweb/pics/"+feedlist.get(position).getUploadby()+".jpg")
                    .centerCrop()
                    .into(holder.userimage);
            Glide
                    .with(getContext())
                    .load("http://192.168.1.64/receipefinderweb/recipe/Recipe.jpg")
                    .centerCrop()
                    .into(holder.recipeimage);
            holder.useraccname.setText(feedlist.get(position).getUploadby());
            //checking already likes
            FirebaseDatabase.getInstance().getReference().child("likes").child(feedlist.get(position).getKey())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.hasChild(username)) {
                                holder.recipebutton.setColorFilter(ContextCompat.getColor(getContext(), R.color.pureblack), android.graphics.PorterDuff.Mode.SRC_IN);
                            }
                            else{

                                holder.recipebutton.setColorFilter(ContextCompat.getColor(getContext(), R.color.main2), android.graphics.PorterDuff.Mode.SRC_IN);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
            //storing likes
           holder.recipebutton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   FirebaseDatabase.getInstance().getReference().child("likes").child(feedlist.get(position).getKey())
                           .addListenerForSingleValueEvent(new ValueEventListener() {
                               @SuppressLint("ResourceAsColor")
                               @Override
                               public void onDataChange(DataSnapshot dataSnapshot) {
                                   if(!dataSnapshot.hasChild(username)) {
                                       mDatabaseReference.child("likes").child(feedlist.get(position).getKey()).child(username).setValue("true");
                                       holder.recipebutton.setColorFilter(ContextCompat.getColor(getContext(), R.color.main2), android.graphics.PorterDuff.Mode.SRC_IN);
                                       Toast.makeText(getContext(), "Liked", Toast.LENGTH_SHORT).show();
                                   }
                                   else{
                                       mDatabaseReference.child("likes").child(feedlist.get(position).getKey()).child(username).removeValue();
                                       holder.recipebutton.setColorFilter(ContextCompat.getColor(getContext(), R.color.pureblack), android.graphics.PorterDuff.Mode.SRC_IN);
                                       Toast.makeText(getContext(), "Liked Removed", Toast.LENGTH_SHORT).show();

                                   }
                                   //getting likes again
                                   DatabaseReference fbDb = FirebaseDatabase.getInstance().getReference();
                                   fbDb.child("likes/"+feedlist.get(position).getKey())
                                           .addListenerForSingleValueEvent(new ValueEventListener() {
                                               @Override
                                               public void onDataChange(DataSnapshot dataSnapshot) {
                                                   // get total available quest
                                                   holder.recipenumber.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                                               }
                                               @Override
                                               public void onCancelled(DatabaseError databaseError) {

                                               }
                                           });
                               }
                               @Override
                               public void onCancelled(DatabaseError databaseError) {
                               }
                           });
               }
           });
           //reading likes
            DatabaseReference fbDb = FirebaseDatabase.getInstance().getReference();
            fbDb.child("likes/"+feedlist.get(position).getKey())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // get total available quest
                            holder.recipenumber.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
        @Override
        public int getItemCount() {
            return feedlist.size();
        }
    }
    public class FeedViewHolder extends RecyclerView.ViewHolder {
        ImageView recipeimage;
        CircleImageView userimage;
        TextView recipename,recipenumber,useraccname;
        ImageButton recipebutton;
        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            recipename=itemView.findViewById(R.id.recipename);
            recipeimage=itemView.findViewById(R.id.recipeimage);
            recipebutton=itemView.findViewById(R.id.likebutton);
            userimage=itemView.findViewById(R.id.userimage);
            recipenumber=itemView.findViewById(R.id.likenumber);
            useraccname=itemView.findViewById(R.id.username);
        }
    }
    public class FeedModel extends ViewModel {
        String key,recipename,uploadby;
        public FeedModel(String key,String recipename,String uploadby){
            this.key=key;
            this.recipename=recipename;
            this.uploadby=uploadby;
        }
        public String getKey() {
            return key;
        }
        public String getRecipename() {
            return recipename;
        }
        public String getUploadby() {
            return uploadby;
        }
    }
}