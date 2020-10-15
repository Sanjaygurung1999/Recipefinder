package com.example.recipefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class Signin extends AppCompatActivity {
    TextInputEditText usernameInput,passwordInput;
    SharedPreferences pref;
    Button signIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        pref= getSharedPreferences("userdetail", MODE_PRIVATE);
        usernameInput=findViewById(R.id.username);
        passwordInput=findViewById(R.id.password);
        signIn=findViewById(R.id.signin);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username=usernameInput.getText().toString();
                final String password=passwordInput.getText().toString();
                checkValidity(username,password);
            }
        });
    }
    public void checkValidity(final String username, final String password){
        String url="http://192.168.1.64/receipefinderweb/checkuservalidity.php?checkuser&username="+username+"&password="+password;
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("0")){
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.constraintlayout),"Username or password doesnt match !", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    snackbar.show();
                }
                else{
                    SharedPreferences.Editor editor= pref.edit();
                    editor.putString("username", username);  // Saving string
                    editor.putString("userid", response);  // Saving userid
                    editor.commit();
                    Intent i=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
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