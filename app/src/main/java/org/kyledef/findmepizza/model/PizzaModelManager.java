package org.kyledef.findmepizza.model;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kyledef.findmepizza.helper.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PizzaModelManager {
    private static PizzaModelManager instance;
    private Context context;
    private JSONObject fullData = null;
    private JSONArray outlets = null;
    private JSONArray franchises = null;

    private PizzaModelManager(Context context) {
        this.context = context;
    }

    public static PizzaModelManager getInstance(Context context) {
        if (instance == null) instance = new PizzaModelManager(context);
        else {
            instance.setContext(context);
        }
        return instance;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<OutletModel> getOuLets() {
        ArrayList<OutletModel> list = new ArrayList<>();
        try {
            JSONArray jList = this.retrieveOutlets();
            Map<String, Integer> logoMap = this.getLogos();
            for (int i = 0; i < jList.length(); i++) {
                JSONObject json = jList.getJSONObject(i);
                Integer logoR = logoMap.get(json.getString("franchise"));
                OutletModel mModel = new OutletModel(json.getInt("id"), json.getString("name"), json.getString("address"), json.getString("franchise"), logoR.intValue(), json.getString("phone1"));
                list.add(mModel);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public OutletModel getOutletById(String id){
        ArrayList<OutletModel> list = getOuLets();
        for (OutletModel outlet : list){
            if (outlet.getId() == Integer.parseInt(id))return outlet;
        }
        return null;
    }

    public ArrayList<OutletModel> getOuLets(String franchise) {
        if (franchise.equals(Constants.ALL)){
            return getOuLets();
        }
        ArrayList<OutletModel> list = new ArrayList<>();
        try {
            JSONArray jList = this.retrieveOutlets();
            Log.d("ModelManager", String.format("getOutLets found %s outlets", jList.length()));
            Map<String, Integer> logoMap = this.getLogos();
            for (int i = 0; i < jList.length(); i++) {
                JSONObject json = jList.getJSONObject(i);
//                Log.d("ModelManager", String.format("Comparing %s with %s", franchise, json.getString("franchise")));
                if (franchise.equalsIgnoreCase(json.getString("franchise"))) {
                    Integer logoR = logoMap.get(json.getString("franchise"));
                    OutletModel mModel = new OutletModel(json.getInt("id"), json.getString("name"), json.getString("address"), json.getString("franchise"), logoR.intValue(), json.getString("phone1"));
                    list.add(mModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<OutletModel> getOuLets(String franchise, String area) {
        //TODO Implement the filter by area functionality
        if (franchise == null)return getOuLets();
        return getOuLets(franchise);
    }

    public ArrayList<FranchiseModel> getFranchises() {
        ArrayList<FranchiseModel> list = new ArrayList<>();
        try {
            JSONArray fList = retrieveFranchises();
            Map<String, Integer> logoMap = this.getLogos();
            for (int i = 0; i < fList.length(); i++) {
                JSONObject json = fList.getJSONObject(i);
                Integer logoR = logoMap.get(json.getString("shortcode"));
                FranchiseModel fm = new FranchiseModel(json.getInt("id"), json.getString("name"), json.getString("shortcode"), json.getString("url"), logoR.intValue());
                list.add(fm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private JSONObject retrieveAllInfo() {
        if (fullData == null) {
            try {
                InputStream stream = context.getResources().getAssets().open("default.json");
                int size = stream.available();
                byte[] buffer = new byte[size];
                stream.read(buffer);
                stream.close();
                String jsonString = new String(buffer, "UTF-8");
                fullData = new JSONObject(jsonString);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
        return fullData;

    }

    private JSONArray retrieveFranchises() {
        if (franchises == null) {
            try {
                JSONObject mainObj = retrieveAllInfo();
                franchises = mainObj.getJSONArray("franchises");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return franchises;
    }

    public ArrayList<MenuModel> getMenus(String franchise) {
        ArrayList<MenuModel> list = new ArrayList<>();
        try {
            JSONObject mainObj = this.retrieveAllInfo();
            JSONArray jList = mainObj.getJSONArray(("menus"));
            for (int i = 0; i < jList.length(); i++) {
                JSONObject json = jList.getJSONObject(i);
                String type = "other";
                if (json.has("type"))
                    type = json.getString("type");

                if (franchise.equalsIgnoreCase(json.getString("franchise"))) {
                    String cost = "Cost Not Listed";
                    if (json.has("cost"))cost = json.getString("cost");
                    MenuModel mModel = new MenuModel(json.getInt("id"), json.getString("franchise"), json.getString("item"), json.getString("category"), type, cost);
                    list.add(mModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Map<String, Integer> getLogos() {
        Map<String, Integer> map = new HashMap<>();
        try {
            JSONArray jList = this.retrieveFranchises();
            for (int i = 0; i < jList.length(); i++) {
                JSONObject json = jList.getJSONObject(i);
                String code = json.getString("shortcode");
                String logo = json.getString("logo").split("[@.]")[0];
                int resource = context.getResources().getIdentifier(logo, "drawable", context.getPackageName());
                map.put(code, resource);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    private JSONArray retrieveOutlets() {
        if (outlets == null) {
            try {
                JSONObject mainObj = retrieveAllInfo();
                outlets = mainObj.getJSONArray("outlets");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return outlets;
    }

    public boolean loadAllData() {
        return (retrieveAllInfo() != null);
    }

    // For Testing Purposes
    public void load2Firebase(){
        Log.d("ModelManager", "Load Firebase");
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        ArrayList<FranchiseModel> franchises = getFranchises();
        for(FranchiseModel franchise : franchises){
            database.child("franchise").child(franchise.getShortCode()).setValue(franchise);
            ArrayList<OutletModel> outletModels = getOuLets(franchise.getShortCode());
            Log.d("ModelManager", String.format("Received %s outlets for %s ", outletModels.size(), franchise.getName()));
            for (OutletModel outlet : outletModels){
                String key = database.child("outlets").child(franchise.getShortCode()).push().getKey();
                database.child("outlets").child(franchise.getShortCode()).child(key).setValue(outlet);
            }
            ArrayList<MenuModel> menuModels = getMenus(franchise.getShortCode());
            Log.d("ModelManager", String.format("Received %s menus for %s ", menuModels.size(), franchise.getName()));
            for (MenuModel menu : menuModels){
                String key = database.child("menus").child(franchise.getShortCode()).push().getKey();
                database.child("menus").child(franchise.getShortCode()).child(key).setValue(menu);
            }

        }
    }

}
