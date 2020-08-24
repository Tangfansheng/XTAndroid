package com.example.XTproject.model;

import android.content.Context;

public class Function {
    public static final int Force =1;
    public static final int Stress = 2;
    public static final int VideoMonitor = 3;
    public static final int Syn = 4;
    public static final int Environment = 5;
    public static final int Loc = 6;

    private int ID;
    private String FunctionName;
    private Context mContext;

    public Function(int id, Context context){
        this.ID = id;
        this.mContext = context;
        switch(ID){
            case Force:
                FunctionName = "力监控";
                break;
            case Stress:
                FunctionName = "应力监控";
                break;
            case VideoMonitor:
                FunctionName = "视频监控";
                break;
            case Syn:
                FunctionName = "同步性监控";
                break;
            case Environment:
                FunctionName ="施工环境监控";
                break;
            case Loc:
                FunctionName= "北斗定位";
                break;
            default:
                break;
        }
    }

    public int getFunctionID() {
        return ID;
    }

    public String getFunctionName(){
        return FunctionName;
    }



}
