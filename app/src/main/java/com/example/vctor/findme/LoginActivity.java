package com.example.vctor.findme;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final ProgressBar prgBar=(ProgressBar)findViewById(R.id.prgBar);
        final EditText etUserLogin= (EditText) findViewById(R.id.etLoginEmail);
        final EditText etPasswordLogin= (EditText) findViewById(R.id.etLoginPwd);

        final Button btnLogin= (Button) findViewById(R.id.btnLogin);
        final TextView registerLink= (TextView) findViewById(R.id.tRegisterHere);

        registerLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent registerIntent= new Intent(LoginActivity.this,RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email= etUserLogin.getText().toString();
                final String password= etPasswordLogin.getText().toString();
                prgBar.setVisibility(View.VISIBLE);
                Response.Listener<String> responseListener= new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if(success){
                                prgBar.setVisibility(View.GONE);
                                String username= jsonResponse.getString("userName");
                                String email= jsonResponse.getString("email");
                                Intent intent= new Intent(LoginActivity.this,MainActivity.class);
                                intent.putExtra("userName",username);
                                intent.putExtra("email",email);

                                LoginActivity.this.startActivity(intent);

                            }else{
                                prgBar.setVisibility(View.GONE);
                                AlertDialog.Builder builder= new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("Login failed")
                                        .setNegativeButton("Retry",null)
                                        .create()
                                        .show();


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };


                LoginRequest loginRequest = new LoginRequest(email,password,responseListener);
                RequestQueue queue= Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);


            }
        });
    }
}
