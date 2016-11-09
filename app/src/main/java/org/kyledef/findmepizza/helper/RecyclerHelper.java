package org.kyledef.findmepizza.helper;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;

public class RecyclerHelper {

    public static void configureListRecycler(Context context, RecyclerView recyclerView){
        LinearLayoutManager lm = new LinearLayoutManager(context);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        configureRecycler(recyclerView, lm);

    }

    public static void configureGridRecycler(Context context, RecyclerView recyclerView){
        GridLayoutManager gm = new GridLayoutManager(context, 2);
        configureRecycler(recyclerView, gm);
    }

    private static void configureRecycler(RecyclerView recyclerView, LayoutManager layoutManager){
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setClickable(true);

        recyclerView.setHasFixedSize(true);
    }
}
