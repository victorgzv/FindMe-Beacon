package com.example.vctor.findme;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final ProgressBar prgBarR=(ProgressBar)findViewById(R.id.prgRegister);
        final EditText etUserName= (EditText) findViewById(R.id.etUserName);
        final EditText etName= (EditText) findViewById(R.id.etName);
        final EditText etEmail= (EditText) findViewById(R.id.etEmail);
        final EditText etPwd= (EditText) findViewById(R.id.etPassword);
        final Button btnRegister= (Button) findViewById(R.id.btnRegister);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String userName= etUserName.getText().toString();
                final String name= etName.getText().toString();
                final String email= etEmail.getText().toString();
                final String password= etPwd.getText().toString();
                boolean validEmail=validateEmail(email);
                prgBarR.setVisibility(View.VISIBLE);
                Response.Listener<String> responseListener= new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if(success){
                                prgBarR.setVisibility(View.GONE);
                                Intent intent= new Intent(RegisterActivity.this,LoginActivity.class);
                                RegisterActivity.this.startActivity(intent);
                                Toast.makeText(getApplicationContext(), "User added "+ name, Toast.LENGTH_LONG).show();
                            }else{
                                prgBarR.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Register failed", Toast.LENGTH_LONG).show();
                                AlertDialog.Builder builder= new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("Register failed")
                                        .setNegativeButton("Retry",null)
                                        .create()
                                        .show();


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                if(validEmail==true) {
                    RegisterRequest registerRequest = new RegisterRequest(userName, name, email, password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                    queue.add(registerRequest);
                }else {
                    prgBarR.setVisibility(View.GONE);
                }
            }
        });
    }
    public boolean validateEmail(String email){
        boolean valid=true;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        // onClick of button perform this simplest code.
        if (email.matches(emailPattern))
        {
            return true;
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
            return false;
        }

    }
}
