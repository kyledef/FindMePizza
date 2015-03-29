package org.kyledef.findmepizza.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.kyledef.findmepizza.R;
import org.kyledef.findmepizza.helper.MenuAdapter;
import org.kyledef.findmepizza.helper.RecyclerHelper;
import org.kyledef.findmepizza.helper.TextHelper;
import org.kyledef.findmepizza.model.MenuModel;
import org.kyledef.findmepizza.model.OutletModel;
import org.kyledef.findmepizza.model.PizzaModelManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MenuList extends BaseActivity implements MenuAdapter.MenuClickListener {

    private static final String TAG = "MenuList";
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

        assignFranchise(outletModel, menuLayout);

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

    private void assignFranchise(OutletModel model, View view){
        ((TextView)view.findViewById(R.id.franchise_name)).setText(TextHelper.formatUserText(model.getName()));
        ((ImageView)view.findViewById(R.id.franchise_logo)).setImageDrawable(getResources().getDrawable(model.getLogoR()));
        View.OnClickListener listener = new OutletActionListener(model);
        view.findViewById(R.id.call_action).setOnClickListener(listener);
        view.findViewById(R.id.website_action).setOnClickListener(listener);
    }

    @Override
    public void onItemClick(MenuModel menu) {

    }

    @Override
    protected String getScreenName(){
        return "Menu List";
    }

    public class OutletActionListener implements View.OnClickListener {

        private OutletModel outletModel;
        public OutletActionListener(OutletModel model){
            this.outletModel = model;
        }
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id){
                case R.id.call_action:
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +  outletModel.getContact()));
                    startActivity(intent);
                    break;
                case R.id.website_action:
                    Toast.makeText(getApplicationContext(), "Website Not available", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "Action Not Supported", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
