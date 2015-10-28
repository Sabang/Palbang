package com.sabang.sp.api;

import java.io.Serializable;

/**
 * Created by yoo2 on 15. 9. 20..
 */
public class RoomModel extends BaseModel implements Serializable{
    public int size;
    public int management_cost;
    public int floor;
    public int securityDeposit;
    public int monthPrice;
    public int pet;
    public String term;
    public String detail;
    public String hash1;
    public String hash2;
    public String hash3;
    public int veranda;
    public int kitchen;
    public int tv;
    public int microwave;
    public int twoRoom;
    public int area;
    public String[] images;
}
