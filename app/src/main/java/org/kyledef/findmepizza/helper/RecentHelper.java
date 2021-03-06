package org.kyledef.findmepizza.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.kyledef.findmepizza.model.OutletModel;
import org.kyledef.findmepizza.model.PizzaModelManager;

import java.util.ArrayList;

public class RecentHelper {

    private static final String OUTLET_DB_REF = "recent_outlets";

    public static void saveRecentOutlets(Context context, ArrayList<OutletModel> outlets){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String data = "";
        for (OutletModel o : outlets){
            data += o.getId() + ",";
        }
        sp.edit().putString(OUTLET_DB_REF, data).commit();
    }

    public static void saveRecentOutlet(Context context, OutletModel outlet){
        ArrayList<OutletModel>list = getRecentOutlets(context);
        if (!list.contains(list)){
            list.add(outlet);
            saveRecentOutlets(context, list);
        }
    }

    public static ArrayList<OutletModel> getRecentOutlets(Context context){
        ArrayList<OutletModel> models = new ArrayList<>();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String data = sp.getString(OUTLET_DB_REF, null);
        if (data != null){
            for (String id : data.split(",")){
                OutletModel m = PizzaModelManager.getInstance(context).getOutletById(id);
                if (m != null)models.add(m);
            }
        }
        return models;
    }
}
