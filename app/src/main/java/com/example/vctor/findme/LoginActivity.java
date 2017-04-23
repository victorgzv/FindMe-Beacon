package com.example.vctor.findme;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

                Response.Listener<String> responseListener= new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if(success){
                                String username= jsonResponse.getString("userName");
                                Intent intent= new Intent(LoginActivity.this,MainActivity.class);
                                intent.putExtra("userName",username);

                                LoginActivity.this.startActivity(intent);
                                Toast.makeText(getApplicationContext(), "User added ", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "Register failed", Toast.LENGTH_LONG).show();
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


            }
        });
    }
}