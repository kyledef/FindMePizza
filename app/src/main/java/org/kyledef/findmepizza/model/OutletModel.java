package org.kyledef.findmepizza.model;

/**
 * Created by kyle on 2/23/15.
 */
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

    public OutletModel(String name, String address, String franchise, String ... contacts) {
        this.name = name;
        this.address = address;
        this.contacts = contacts;
        this.franchise = franchise;
    }

    public OutletModel(){
        this.contacts = new String[0];
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
