package com.example.vctor.findme;

/**
 * Created by Víctor on 24/04/2017.
 */

public class BleItem {
    public String uuid_no;
    public double distance;
    public BleItem(String uuid, double dist){
        this.distance=dist;
        this.uuid_no=uuid;

    }
}
