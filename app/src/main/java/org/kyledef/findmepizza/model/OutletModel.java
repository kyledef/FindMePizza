package org.kyledef.findmepizza.model;


import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class OutletModel implements Parcelable {
    String name;
    String address;
    String[] contacts;
    String franchise;
    int logoR;

    public OutletModel(String name, String address, String franchise, int logo, String... contacts) {
        this.name = name;
        this.address = address;
        this.contacts = contacts;
        this.franchise = franchise;
        this.logoR = logo;
    }

    public OutletModel(Parcel parcel) {
        String[] strArr = new String[4];
        parcel.readStringArray(strArr);
        this.name = strArr[0];
        this.address = strArr[1];
        this.franchise = strArr[3];

        int[] intArr = new int[1];
        parcel.readIntArray(intArr);
        this.logoR = intArr[0];

        Bundle b = parcel.readBundle();
        if (b != null) this.contacts = b.getStringArray("contacts");
        else {
            this.contacts = new String[1];
            this.contacts[0] = strArr[2];
        }
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

    public String getContact() {
        return contacts[0];
    }

    public String getContact(int i) {
        if (i < contacts.length) return contacts[i];
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
        dest.writeStringArray(new String[]{name, address, contacts[0], franchise});
        dest.writeIntArray(new int[]{logoR});
        Bundle b = new Bundle();
        b.putStringArray("contacts", contacts);
        dest.writeBundle(b);
    }

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
}
