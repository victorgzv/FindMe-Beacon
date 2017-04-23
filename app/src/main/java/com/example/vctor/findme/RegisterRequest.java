package com.example.vctor.findme;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Víctor on 23/04/2017.
 */

public class RegisterRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL="https://viktorgonzalezv123.000webhostapp.com/register.php";
    private Map<String,String> params;


    public RegisterRequest(String username, String name, String email, String password, Response.Listener<String> listener){
        super(Method.POST,REGISTER_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put("userName",username);
        params.put("Name",name);
        params.put("email",email);
        params.put("password",password);




    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
