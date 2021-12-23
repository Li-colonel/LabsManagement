package com.example.labsmanage;

public class Equi {
    private String equiID;
    private String labID;
    private String type;
    private String status;

    public Equi(String id, String lab, String type, String state){
        this.equiID = id;
        this.labID = lab;
        this.type = type;
        this.status = state;
    }

    // 无参构造方法
    public Equi(){
    }

    public String getID() {
        return equiID;
    }
    public String getLab() {
        return labID;
    }
    public String getType() {
        return type;
    }
    public String getState() {
        return status;
    }
}
