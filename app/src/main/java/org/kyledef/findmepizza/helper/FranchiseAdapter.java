package org.kyledef.findmepizza.helper;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import org.kyledef.findmepizza.R;
import org.kyledef.findmepizza.model.FranchiseModel;

public class FranchiseAdapter extends FirebaseRecyclerAdapter<FranchiseModel, FranchiseAdapter.FranchiseViewHolder> {


	private FranchiseClickListener listener;

	public FranchiseAdapter(DatabaseReference ref) {
		super(FranchiseModel.class, R.layout.franchise_layout, FranchiseViewHolder.class, ref);
		Log.d("FranchiseAdapter", "Adapter Instantiated");
	}

	@Override
	protected void populateViewHolder(FranchiseViewHolder viewHolder, FranchiseModel model, int position) {
		Log.d("FranchiseAdapter", "populateViewHolder method called");
		viewHolder.bind(model);
		viewHolder.setListener(listener);
	}

	public void setListener(FranchiseClickListener listener) {
		this.listener = listener;
	}

	public interface FranchiseClickListener {
		void onItemClick(FranchiseModel franchise);
	}

	public static class FranchiseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


		private final TextView name;
		private final ImageView logo;
		private FranchiseClickListener listener;
		private FranchiseModel franchise;

		public FranchiseViewHolder(View itemView) {
			super(itemView);
			Log.d("FranchiseViewHolder", "instantiated");

			itemView.setOnClickListener(this);

			name = (TextView) itemView.findViewById(R.id.franchise_name);
			logo = (ImageView) itemView.findViewById(R.id.franchise_logo);
			Log.d("FranchiseViewHolder", "Accessed the TextView and ImageView");
		}

		public void bind(FranchiseModel franchise){
			Log.d("FranchiseViewHolder", "attempting to bind model to view");
			this.franchise = franchise;
			name.setText(franchise.getName());
			logo.setImageResource(franchise.getLogoR());
		}

		public void setListener(FranchiseClickListener listener){
			this.listener = listener;
		}


		@Override
		public void onClick(View view) {
			if (listener != null)listener.onItemClick(franchise);
		}
	}
}
