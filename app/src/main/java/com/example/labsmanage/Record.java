package com.example.labsmanage;

public class Record {
    private String equiID;
    private String type;
    private String date;


    public Record(String labID, String type, String date){
        this.equiID = labID;
        this.type = type;
        this.date = date;
    }

    // 无参构造方法
    public Record(){
    }

    public String getID() {
        return equiID;
    }
    public String getType() {
        return type;
    }
    public String getDate() {
        return date;
    }
}
