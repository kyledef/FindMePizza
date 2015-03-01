package org.kyledef.findmepizza.model;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kyle on 2/23/15.
 */
public class PizzaModelManager {
    private static PizzaModelManager instance;
    Context context;
    JSONObject fullData = null;
    JSONArray outlets = null;
    JSONArray franchises = null;

    public static PizzaModelManager getInstance(Context context){
        if (instance == null)instance = new PizzaModelManager(context);
        else{
            instance.setContext(context);
        }
        return instance;
    }

    private PizzaModelManager(Context context){
        this.context = context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<OutletModel>getOuLets(){
        ArrayList<OutletModel> list = new ArrayList<>();
        try {
            JSONArray jList = this.retrieveOutlets();
            Map<String, Integer> logoMap = this.getLogos();
            for (int i = 0; i < jList.length(); i++) {
                JSONObject json = jList.getJSONObject(i);
                Integer logoR = logoMap.get(json.getString("franchise"));
//                System.out.println(json.getString("franchise")+" "+logoR);
                OutletModel mModel = new OutletModel(json.getString("name"), json.getString("address"), json.getString("franchise"),logoR.intValue(), json.getString("phone1"));
//                OutletModel mModel = new OutletModel(json.getString("name"), json.getString("address"), json.getString("franchise"),json.getString("phone1"));
                list.add(mModel);

            }
        }catch(Exception e){e.printStackTrace();}
        return list;
    }

    public ArrayList<OutletModel>getOuLets(String franchise){
        ArrayList<OutletModel> list = new ArrayList<>();
        try {
            JSONArray jList = this.retrieveOutlets();
            Map<String, Integer> logoMap = this.getLogos();
            for (int i = 0; i < jList.length(); i++) {
                JSONObject json = jList.getJSONObject(i);
                if (franchise.equalsIgnoreCase(json.getString("franchise"))) {
                    Integer logoR = logoMap.get(json.getString("franchise"));
                    OutletModel mModel = new OutletModel(json.getString("name"), json.getString("address"), json.getString("franchise"),logoR.intValue(), json.getString("phone1"));
//                    OutletModel mModel = new OutletModel(json.getString("name"), json.getString("address"), json.getString("franchise"), json.getString("phone1"));
                    list.add(mModel);
                }
            }
        }catch(Exception e){e.printStackTrace();}
        return list;
    }


    private JSONObject retrieveAllInfo(){
        if (fullData == null) {
            try {
                InputStream stream = context.getResources().getAssets().open("default.json");
                int size = stream.available();
                byte[] buffer = new byte[size];
                stream.read(buffer);
                stream.close();
                String jsonString = new String(buffer, "UTF-8");
                fullData = new JSONObject(jsonString);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return fullData;

    }

    private JSONArray retrieveFranchises(){
        if (franchises == null){
            try{
                JSONObject mainObj = retrieveAllInfo();
                franchises = mainObj.getJSONArray("franchises");
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return franchises;
    }

    public Map<String, Integer> getLogos(){
        Map<String, Integer> map = new HashMap<>();
        try{
            JSONArray jList = this.retrieveFranchises();
            for (int i = 0; i < jList.length(); i++) {
                JSONObject json = jList.getJSONObject(i);
                String code = json.getString("shortcode");
//                System.out.println(code +":"+json.getString("logo"));
//                System.out.println(json.getString("logo").split("[@.]").length);
                String logo = json.getString("logo").split("[@.]")[0];
                int resource = context.getResources().getIdentifier(logo,"drawable", context.getPackageName());
//                System.out.println(resource);
                map.put(code, Integer.valueOf(resource));
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    private JSONArray retrieveOutlets(){
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
}
