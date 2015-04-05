package org.kyledef.findmepizza.helper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.kyledef.findmepizza.R;
import org.kyledef.findmepizza.model.OutletModel;

import java.util.ArrayList;

public class RecentAdapter extends RecyclerView.Adapter <RecentAdapter.OutletViewHolder> {
    private ArrayList<OutletModel> list;

    public RecentAdapter(ArrayList<OutletModel> list) {
        this.list = list;
    }

    @Override
    public OutletViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.outlet_layout, viewGroup, false);
        return new OutletViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OutletViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    public RecentAdapter addModels(ArrayList<OutletModel> list){
        this.list = list;
        return this;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class OutletViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        TextView address;
        TextView phone1;
        TextView franchise;
        ImageView logo;
        private OutletModel model;

        public OutletViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            address = (TextView) itemView.findViewById(R.id.address);
            phone1 = (TextView) itemView.findViewById(R.id.phone1);
            franchise = (TextView) itemView.findViewById(R.id.franchise);
            logo = (ImageView) itemView.findViewById(R.id.logo_view);

            itemView.setOnClickListener(this); // Set the ViewHolder to handle click onItemClickListener
        }

        @Override
        public void onClick(View v) {

        }

        public OutletViewHolder bind(OutletModel outletModel) {
            this.model = outletModel;
            name.setText(TextHelper.formatUserText(model.getName()));
            address.setText(TextHelper.formatUserText(model.getAddress()));
            phone1.setText(model.getContact());
            franchise.setText(TextHelper.formatUserText(model.getFranchise()));
            logo.setImageResource(model.getLogoR());

            return this;
        }
    }
}
