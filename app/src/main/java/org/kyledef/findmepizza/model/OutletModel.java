package org.kyledef.findmepizza.model;


import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OutletModel implements Parcelable {
    public static final Parcelable.Creator<OutletModel> CREATOR = new Parcelable.Creator<OutletModel>() {

        @Override
        public OutletModel createFromParcel(Parcel source) {
            return new OutletModel(source);
        }

        @Override
        public OutletModel[] newArray(int size) {
            return new OutletModel[size];
        }
    };
    int id;
    String name;
    String address;
    List<String> contacts;
    String franchise;
    int logoR;

    public OutletModel(int id, String name, String address, String franchise, int logo, String... contacts) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.contacts = new ArrayList<>(Arrays.asList(contacts));
        this.franchise = franchise;
        this.logoR = logo;
    }

    public OutletModel(Parcel parcel) {
        String[] strArr = new String[4];
        parcel.readStringArray(strArr);
        this.name = strArr[0];
        this.address = strArr[1];
        this.franchise = strArr[3];

        int[] intArr = new int[2];
        parcel.readIntArray(intArr);
        this.id = intArr[0];
        this.logoR = intArr[1];

        Bundle b = parcel.readBundle(getClass().getClassLoader());
        if (b != null) this.contacts = new ArrayList<>(Arrays.asList( b.getStringArray("contacts") ));
        else {
            this.contacts =new ArrayList<>();
            this.contacts.add(strArr[2]);
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public List<String> getContacts() {
        return  contacts;
    }

    public String getContact() {
        return contacts.get(0);
    }

    public String getContact(int i) {
        if (i < contacts.size()) return contacts.get(i);
        return null;
    }

    public String getFranchise() {
        return franchise;
    }

    public int getLogoR() {
        return logoR;
    }

    @Override
    public int describeContents() {
        return 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{name, address, contacts.get(0), franchise});
        dest.writeIntArray(new int[]{id, logoR});
        Bundle b = new Bundle();
        b.putStringArray("contacts", contacts.toArray(new String[2]));
        dest.writeBundle(b);
    }

    @Override
    public boolean equals(Object object){
        if (object instanceof OutletModel)
            return this.id == ((OutletModel)object).id;
        return false;
    }
}
