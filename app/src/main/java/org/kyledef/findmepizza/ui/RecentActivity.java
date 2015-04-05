package org.kyledef.findmepizza.ui;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import org.kyledef.findmepizza.R;
import org.kyledef.findmepizza.helper.OutletAdapter;
import org.kyledef.findmepizza.helper.RecentAdapter;
import org.kyledef.findmepizza.helper.RecyclerHelper;
import org.kyledef.findmepizza.model.OutletModel;
import org.kyledef.findmepizza.model.PizzaModelManager;

import java.util.ArrayList;
import java.util.Collections;

public class RecentActivity  extends BaseActivity{

    private RecyclerView recyclerView;
    protected ArrayList<OutletModel> list;
    protected RecentAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add the Current Layout as part of the parent layout
        FrameLayout content = (FrameLayout) findViewById(R.id.content_frame);
        View layout = LayoutInflater.from(this).inflate(R.layout.activity_recent_list, content, false);
        content.addView(layout);

        recyclerView = (RecyclerView) findViewById(R.id.recent_list);
        RecyclerHelper.configureRecycler(this, recyclerView);
        list = new ArrayList<>();
        adapter = new RecentAdapter(list);
//        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        updateView();
    }

    protected void updateView(){
        new Thread(() -> {
            PizzaModelManager pmm = PizzaModelManager.getInstance(getApplicationContext());
            list = new ArrayList<>();
            list.addAll(pmm.getOuLets());
            Collections.sort(list, (outletModel, outletModel2) -> {
                if (outletModel.getId() == outletModel2.getId()) return 0;
                return outletModel.getFranchise().compareTo(outletModel2.getFranchise()) + outletModel.getName().compareTo(outletModel2.getName());
            });
            adapter.addModels(list);
            runOnUiThread(adapter::notifyDataSetChanged);
        }).start();
    }

    @Override
    protected String getScreenName(){
        return "Recent";
    }
}