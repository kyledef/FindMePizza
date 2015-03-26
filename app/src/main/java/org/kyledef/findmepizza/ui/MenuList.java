package org.kyledef.findmepizza.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import org.kyledef.findmepizza.R;
import org.kyledef.findmepizza.helper.MenuAdapter;
import org.kyledef.findmepizza.helper.RecyclerHelper;
import org.kyledef.findmepizza.model.MenuModel;
import org.kyledef.findmepizza.model.OutletModel;
import org.kyledef.findmepizza.model.PizzaModelManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MenuList extends BaseActivity implements MenuAdapter.MenuClickListener {

    private RecyclerView recyclerView;
    private ArrayList<MenuModel> list;
    private MenuAdapter adapter;
    private String franchise;
    private OutletModel outletModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        franchise = i.getExtras().getBundle("details").getString("franchise");
        outletModel = i.getExtras().getParcelable("outlet");


        // Add the Current Layout as part of the parent layout
        FrameLayout content = (FrameLayout) findViewById(R.id.content_frame);
        View menuLayout = LayoutInflater.from(this).inflate(R.layout.menu_list, content, false);
        content.addView(menuLayout);

        recyclerView = (RecyclerView) findViewById(R.id.menu_list);
        RecyclerHelper.configureRecycler(this, recyclerView);

        list = new ArrayList<>();
        adapter = new MenuAdapter(list);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                PizzaModelManager pmm = PizzaModelManager.getInstance(getApplicationContext());
                list.addAll(pmm.getMenus(franchise));
                Collections.sort(list, new Comparator<MenuModel>() {
                    @Override
                    public int compare(MenuModel menuModel, MenuModel menuModel2) {
                        if (menuModel.getId() == menuModel2.getId())return 0;
                        return menuModel.getName().compareTo(menuModel2.getName());
                    }
                });
                adapter.addModels(list);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    @Override
    public void onItemClick(MenuModel menu) {

    }

    @Override
    protected String getScreenName(){
        return "Menu List";
    }
}
