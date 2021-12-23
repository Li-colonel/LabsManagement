package com.example.labsmanage;

public class Lab {
    private String labID;
    private String manager;
    private int numEqui;


    public Lab(String labID, String manager, int numEqui){
        this.labID = labID;
        this.manager = manager;
        this.numEqui = numEqui;
    }

    // 无参构造方法
    public Lab(){
    }

    public String getID() {
        return labID;
    }
    public String getManager() {
        return manager;
    }
    public int getNumEqui() {
        return numEqui;
    }
}
