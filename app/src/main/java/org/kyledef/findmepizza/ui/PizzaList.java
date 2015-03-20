package org.kyledef.findmepizza.ui;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.kyledef.findmepizza.R;
import org.kyledef.findmepizza.helper.OutletAdapter;
import org.kyledef.findmepizza.helper.RecyclerHelper;
import org.kyledef.findmepizza.model.OutletModel;
import org.kyledef.findmepizza.model.PizzaModelManager;
import org.kyledef.findmepizza.ui.fragments.NavDrawerFragment;

import java.util.ArrayList;


public class PizzaList extends BaseActivity {

    private RecyclerView recyclerView;
    public static final String TAG = "PizzaList";
    protected OutletAdapter adapter;
    protected ArrayList<OutletModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout content = (FrameLayout) findViewById(R.id.content_frame);
        View pizzaLayout = LayoutInflater.from(this).inflate(R.layout.activity_pizza_list, content, false);
        content.addView(pizzaLayout);


        recyclerView = (RecyclerView) findViewById(R.id.pizza_list);
        RecyclerHelper.configureRecycler(this, recyclerView);

        list = new ArrayList<>();
        adapter = new OutletAdapter(list);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        Log.d(TAG, "Relaunching onCreate");

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



}
