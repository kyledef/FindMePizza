package org.kyledef.findmepizza.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.kyledef.findmepizza.R;
import org.kyledef.findmepizza.helper.Constants;
import org.kyledef.findmepizza.helper.FavouriteHelper;
import org.kyledef.findmepizza.helper.MenuAdapter;
import org.kyledef.findmepizza.helper.RecentHelper;
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
    private String resMsg;

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
        RecyclerHelper.configureListRecycler(this, recyclerView);

        list = new ArrayList<>();
        adapter = new MenuAdapter(list);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        updateView(franchise, Constants.ALL);
    }

    private void assignFranchise(OutletModel model, View view){
        ((TextView)view.findViewById(R.id.franchise_name)).setText(TextHelper.formatUserText(model.getName()));
        ((ImageView)view.findViewById(R.id.franchise_logo)).setImageDrawable(getResources().getDrawable(model.getLogoR()));
        View.OnClickListener listener = new OutletActionListener(model);
        view.findViewById(R.id.call_action).setOnClickListener(listener);
        view.findViewById(R.id.website_action).setOnClickListener(listener);
        view.findViewById(R.id.fav_action).setOnClickListener(listener);
    }

    @Override
    public void onItemClick(MenuModel menu) { }

    protected void updateView(final String franchise, final String area){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("menus").child(franchise).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot franchise) {
                for (DataSnapshot menu: franchise.getChildren()){
                    list.add(menu.getValue(MenuModel.class));
                }
                Log.d("MenuList", String.format("Found %s menus", list.size()));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    @Override
    protected String getScreenName(){
        return "Menu List";
    }

    protected void saveFavourite(final OutletModel outletModel){
        Log.d(TAG, "Attempt to save a favourite model");
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean res = FavouriteHelper.saveFavouriteOutlet(getApplicationContext(), outletModel);
                if (res) {
                    resMsg = "Added " + outletModel.getName() + " to favourite list";
                }else{
                    FavouriteHelper.removeFavouriteOutlet(getApplicationContext(), outletModel);
                    resMsg = "Removed " + outletModel.getName() + " from favourite list";
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),resMsg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    protected void saveRecent(final OutletModel outletModel){
        Log.d(TAG, "Attempt to save a recent model");
        new Thread(new Runnable() {
            @Override
            public void run() {
                RecentHelper.saveRecentOutlet(getApplicationContext(), outletModel);
            }
        }).start();
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
                    saveRecent(outletModel);
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" +  outletModel.getContact()));
                    startActivity(intent);
                    break;
                case R.id.website_action:
                    Toast.makeText(getApplicationContext(), "Website Not available", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.fav_action:
                    saveFavourite(outletModel);
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "Action Not Supported", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
