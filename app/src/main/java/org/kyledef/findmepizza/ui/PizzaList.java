package org.kyledef.findmepizza.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.kyledef.findmepizza.R;
import org.kyledef.findmepizza.helper.Constants;
import org.kyledef.findmepizza.helper.OutletAdapter;
import org.kyledef.findmepizza.helper.RecyclerHelper;
import org.kyledef.findmepizza.model.OutletModel;
import org.kyledef.findmepizza.model.PizzaModelManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class PizzaList extends BaseActivity implements OutletAdapter.OutletClickListener {

    public static final String TAG = "PizzaList";
    protected OutletAdapter adapter;
    protected ArrayList<OutletModel> list;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add the Current Layout as part of the parent layout
        FrameLayout content = (FrameLayout) findViewById(R.id.content_frame);
        View pizzaLayout = LayoutInflater.from(this).inflate(R.layout.activity_pizza_list, content, false);
        content.addView(pizzaLayout);

        recyclerView = (RecyclerView) findViewById(R.id.pizza_list);
        RecyclerHelper.configureRecycler(this, recyclerView);

        list = new ArrayList<>();
        adapter = new OutletAdapter(list);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        updateView(Constants.ALL, Constants.ALL);
    }

    @Override
    public void onItemClick(OutletModel outlet) {
        Log.d(TAG, "Selected: " + outlet);
        Intent i = new Intent(this, MenuList.class);
        Bundle b = new Bundle();

        i.putExtra("outlet", outlet);
        b.putString("franchise", outlet.getFranchise());
        i.putExtra("details", b);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            startActivityForResult(new Intent(this, OutletFilterActivity.class), Constants.OUTLET_FILTER);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case Constants.OUTLET_FILTER:
                //TODO Check if extra exists
                String location = null, franchise = null;
                if (data.hasExtra("location"))
                    location  = data.getStringExtra("location");
                if (data.hasExtra("location"))
                    franchise = data.getStringExtra("franchise");

                updateView(franchise, location);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void updateView(final String franchise, final String area){
        new Thread(new Runnable() {
            @Override
            public void run() {
                PizzaModelManager pmm = PizzaModelManager.getInstance(getApplicationContext());
                list = new ArrayList<>();
                list.addAll(pmm.getOuLets(franchise, area));
                Collections.sort(list, new Comparator<OutletModel>() {
                    @Override
                    public int compare(OutletModel outletModel, OutletModel outletModel2) {
                        if (outletModel.getId() == outletModel2.getId())return 0;
                        return outletModel.getFranchise().compareTo(outletModel2.getFranchise()) + outletModel.getName().compareTo(outletModel2.getName());
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


    protected String getScreenName(){
        return "Pizza Home";
    }
}
