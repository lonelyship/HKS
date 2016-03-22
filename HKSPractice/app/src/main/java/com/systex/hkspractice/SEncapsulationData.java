package com.systex.hkspractice;

import org.json.JSONObject;

import java.util.ArrayList;


public class SEncapsulationData {

    private static SEncapsulationData s_instance=null;

    private ArrayList<JSONObject> alData = new ArrayList<>();
    private String strSearch = "";
    private Double dLat=0.0;
    private Double dlon=0.0;

    public static SEncapsulationData getInstance(){

        if(s_instance==null){
            s_instance=new SEncapsulationData();
        }

        return  s_instance;
    }


    public void setAlData(ArrayList<JSONObject> alData) {
        this.alData = alData;
    }

    public ArrayList<JSONObject> getAlData() {
        return alData;
    }

    public  void setStrSearch(String strSearch) {
        this.strSearch = strSearch;
    }

    public String getStrSearch() {
        return strSearch;
    }

    public void setDLat(Double dLat) {
        this.dLat = dLat;
    }

    public void setDlon(Double dlon) {
        this.dlon = dlon;
    }

    public Double getDLat() {
        return dLat;
    }

    public Double getDlon() {
        return dlon;
    }
}
