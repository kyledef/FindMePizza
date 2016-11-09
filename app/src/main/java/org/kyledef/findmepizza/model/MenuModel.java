package org.kyledef.findmepizza.model;


public class MenuModel {
    int id;
    String franchise;
    String name;
    String category;
    String type;
    String cost;


    public MenuModel(){

    }

    public MenuModel(int id, String franchise, String name, String category, String type, String cost) {
        this.id = id;
        this.franchise = franchise;
        this.name = name;
        this.category = category;
        this.type = type;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public String getFranchise() {
        return franchise;
    }

    public void setFranchise(String franchise) {
        this.franchise = franchise;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
