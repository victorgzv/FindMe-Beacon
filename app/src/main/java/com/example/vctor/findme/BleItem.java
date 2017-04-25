package com.example.vctor.findme;

/**
 * Created by Víctor on 24/04/2017.
 */

public class BleItem {
    public String uuid_no;
    public String distance;
    public BleItem(String uuid, String dist){
        this.distance=dist;
        this.uuid_no=uuid;

    }
}
