package org.kyledef.findmepizza.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import org.kyledef.findmepizza.R;
import org.kyledef.findmepizza.helper.OutletAdapter;
import org.kyledef.findmepizza.helper.RecyclerHelper;
import org.kyledef.findmepizza.model.OutletModel;
import org.kyledef.findmepizza.model.PizzaModelManager;

import java.util.ArrayList;


public class PizzaList extends BaseActivity implements OutletAdapter.OutletClickListener {

    private RecyclerView recyclerView;
    public static final String TAG = "PizzaList";
    protected OutletAdapter adapter;
    protected ArrayList<OutletModel> list;

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

        new Thread(new Runnable() {
            @Override
            public void run() {
                PizzaModelManager pmm = PizzaModelManager.getInstance(getApplicationContext());
                list.addAll(pmm.getOuLets());
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
    public void onItemClick(OutletModel outlet) {
        Log.d(TAG, "Selected: " + outlet);
        Intent i = new Intent(this, MenuList.class);
        Bundle b = new Bundle();

        i.putExtra("outlet", outlet);
        b.putString("franchise", outlet.getFranchise());
        i.putExtra("details", b);
        startActivity(i);
    }
}
