package org.kyledef.findmepizza.model;

public class FranchiseModel {
    int id;
    String name;
    String shortCode;
    String url;
    int logoR;


    public FranchiseModel(){

    }

    public FranchiseModel(int id, String name, String shortCode, String url, int logoR) {
        this.id = id;
        this.name = name;
        this.shortCode = shortCode;
        this.url = url;
        this.logoR = logoR;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getShortCode() {
        return shortCode;
    }

    public String getUrl() {
        return url;
    }

    public int getLogoR() {
        return logoR;
    }
}
