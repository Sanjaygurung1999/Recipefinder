package com.example.recipefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

public class last_step_signup extends AppCompatActivity {
    ImageView loginicon;
    LinearLayout bottomlayout;
    Button signup;
    EditText username,password;
    String pnumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_step_signup);
        pnumber=getIntent().getStringExtra("number");
        loginicon=findViewById(R.id.imageView3);
        bottomlayout=findViewById(R.id.bottomlayout);
        signup=findViewById(R.id.signup);
        username=findViewById(R.id.editText2);
        password=findViewById(R.id.editText3);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("") || password.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Incomplete Fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    checkValidity(username.getText().toString(),password.getText().toString());
                }
            }
        });
    }
    public void checkValidity(final String username, final String password){
        String url="http://192.168.1.64/receipefinderweb/checkuservalidity.php?checkusername&username="+username;
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("0")){
                    signupUser(username,password);
                }
                else{
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.constraintlayout),"Account already exist", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    snackbar.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }
    public void signupUser(final String username, String password){
        String url="http://192.168.1.64/receipefinderweb/registeruser.php?username="+username+"&password="+password+"&number="+pnumber;
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("1")){
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.constraintlayout), "Account created successfully" , Snackbar.LENGTH_LONG);
                    snackbar.setAction("Continue", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i=new Intent(getApplicationContext(), Signin.class);
                            startActivity(i);
                        }
                    });
                    snackbar.show();
                    Intent i=new Intent(getApplicationContext(),Signin.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }
}
