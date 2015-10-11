package com.sabang.sp.api;

import java.io.Serializable;

/**
 * Created by yoo2 on 15. 9. 20..
 */
public class RoomModel extends BaseModel implements Serializable{
    public int size;
    public int floor;
    public int securityDeposit;
    public int monthPrice;
    public boolean pet;
    public String term;
    public String detail;
    public String hash1;
    public String hash2;
    public String hash3;
    public boolean veranda;
    public boolean kitchen;
    public boolean tv;
    public boolean microwave;
    public boolean twoRoom;
    public int area;
    public String[] images;
}
