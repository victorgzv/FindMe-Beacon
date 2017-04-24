package com.example.vctor.findme;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Víctor on 24/04/2017.
 */

public class BeaconAdapter extends ArrayAdapter<String> {
    public BeaconAdapter(Context context, String[] list) {
        super(context, 0, list);
    }
    TextView uuid,distance;
    public View getView(int position, View convert, ViewGroup parent){
        View temp= convert;
        if(temp== null){
            temp= LayoutInflater.from(getContext()).inflate(R.layout.beacon_items,parent, false);

        }
        String a = getItem(position);
        //Create object class beacon
        uuid= (TextView) temp.findViewById(R.id.uuid);
        distance= (TextView)temp.findViewById(R.id.distance);
        uuid.setText(a);
        distance.setText("Hello");

        return temp;
    }
}
