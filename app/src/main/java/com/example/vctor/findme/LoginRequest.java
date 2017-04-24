package com.example.vctor.findme;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Víctor on 23/04/2017.
 */

public class LoginRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL="https://viktorgonzalezv123.000webhostapp.com/login.php";
    private Map<String,String> params;


    public LoginRequest(String email, String password, Response.Listener<String> listener){
        super(Method.POST,LOGIN_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put("email",email);
        params.put("password",password);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
