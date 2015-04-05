package org.kyledef.findmepizza.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.kyledef.findmepizza.model.OutletModel;
import org.kyledef.findmepizza.model.PizzaModelManager;

import java.util.ArrayList;

public class FavouriteHelper {

    private static final String FAV_DB_REF = "favourite_outlets";

    public static void saveFavouriteOutlets(Context context, ArrayList<OutletModel> outlets){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String data = "";
        for (OutletModel o : outlets){
            data += o.getId() + ",";
        }
        sp.edit().putString(FAV_DB_REF, data);
    }

    public static ArrayList<OutletModel> getFavouriteOutlets(Context context){
        ArrayList<OutletModel> models = new ArrayList<>();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String data = sp.getString(FAV_DB_REF, null);
        if (data != null){
            for (String id : data.split(",")){
                OutletModel m = PizzaModelManager.getInstance(context).getOutletById(id);
                if (m != null)models.add(m);
            }
        }
        return models;
    }
}
