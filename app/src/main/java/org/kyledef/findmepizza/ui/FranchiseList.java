package org.kyledef.findmepizza.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.kyledef.findmepizza.R;
import org.kyledef.findmepizza.helper.FranchiseAdapter;
import org.kyledef.findmepizza.helper.FranchiseAdapter.FranchiseViewHolder;
import org.kyledef.findmepizza.helper.RecyclerHelper;
import org.kyledef.findmepizza.model.FranchiseModel;

public class FranchiseList extends BaseActivity implements FranchiseAdapter.FranchiseClickListener {

	private RecyclerView recyclerView;

	@Override
	protected String getScreenName() {
		return "FranchiseList";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FrameLayout content = (FrameLayout) findViewById(R.id.content_frame);
		View franchiseLayout = LayoutInflater.from(this).inflate(R.layout.activity_franchise_list, content, false);
		content.addView(franchiseLayout);

		analyticsHelper.setScreen("franchises");

		recyclerView = (RecyclerView) findViewById(R.id.franchise_list);
		if (recyclerView != null) Log.d("FranchiseList", "RecyclerView is not null");
		RecyclerHelper.configureGridRecycler(this, recyclerView);

		DatabaseReference database = FirebaseDatabase.getInstance().getReference();
		FranchiseAdapter adapter = new FranchiseAdapter(database.child("franchise"));
		adapter.setListener(this);
		recyclerView.setAdapter(adapter);
	}

	@Override
	public void onItemClick(FranchiseModel franchise) {
		Bundle bundle = new Bundle();
		bundle.putString("franchise",franchise.getShortCode());
		Intent i = new Intent(this, PizzaList.class);
		i.putExtras(bundle);
		startActivity(i);
	}
}
