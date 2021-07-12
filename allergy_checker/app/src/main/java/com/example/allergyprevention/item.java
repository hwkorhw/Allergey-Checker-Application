package com.example.allergyprevention;


public class item {

    String name;
    String nutrite;
    String allergy;

    public String getName(){
        return this.name;
    }

    public String getNutrite(){
        return this.nutrite;
    }

    public String getAllergy(){
        return this.allergy;
    }

    public void setName(String s){
        this.name = s;
    }

    public void setNutrite(String s){
        this.nutrite = s;
    }

    public void setAllergy(String s){
        this.allergy = s;
    }
}