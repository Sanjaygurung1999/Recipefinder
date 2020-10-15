package com.example.recipefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.hbb20.CountryCodePicker;

public class Signup extends AppCompatActivity {
    TextView tologinPage;
    EditText editTextMobile;
    CountryCodePicker ccp;
    Button Signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        tologinPage = findViewById(R.id.textView7);
        editTextMobile = findViewById(R.id.editTextMobile);
        ccp = findViewById(R.id.ccp);
        tologinPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Signin.class);
                startActivity(i);
            }
        });
        Signup=findViewById(R.id.buttonContinue);
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobile = editTextMobile.getText().toString().trim();
                checkNumber(mobile);
            }
        });
    }
    public void checkNumber(final String number){
        final String code = ccp.getSelectedCountryCode();
        String url="http://192.168.1.64/receipefinderweb/verifynumber.php?pnumber="+code+""+number;
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("1")){
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.constraintlayout), "Phone number already exist", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    snackbar.show();
                }
                else{
                    if (number.isEmpty() || number.length() < 10) {
                        editTextMobile.setError("Enter a valid mobile");
                        editTextMobile.requestFocus();
                        return;
                    }
                    Intent intent = new Intent(getApplicationContext(), VerifyPhone.class);
                    intent.putExtra("mobile", number);
                    intent.putExtra("code", code);
                    startActivity(intent);
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