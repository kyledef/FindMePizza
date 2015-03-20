package org.kyledef.findmepizza.model;


public class OutletModel {
    String name;
    String address;
    String  [] contacts;
    String franchise;
    int logoR;

    public OutletModel(String name, String address, String franchise, int logo, String ... contacts) {
        this.name = name;
        this.address = address;
        this.contacts = contacts;
        this.franchise = franchise;
        this.logoR = logo;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String[] getContacts() {
        return contacts;
    }

    public String getContact(){
        return contacts[0];
    }

    public String getContact(int i){
        if (i < contacts.length)return contacts[i];
        return null;
    }

    public String getFranchise() {
        return franchise;
    }

    public int getLogoR() {
        return logoR;
    }
}
