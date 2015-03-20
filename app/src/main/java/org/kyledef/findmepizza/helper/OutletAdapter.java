package org.kyledef.findmepizza.helper;

import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.kyledef.findmepizza.R;
import org.kyledef.findmepizza.model.OutletModel;

import java.util.ArrayList;

/**
 * Created by kyle on 2/24/15.
 */
public class OutletAdapter extends RecyclerView.Adapter<OutletAdapter.OutletViewHolder> {

    private ArrayList<OutletModel> list;

    public OutletAdapter(ArrayList<OutletModel> list) {
        this.list = list;
    }

    @Override
    public OutletViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.outlet_layout, viewGroup, false);
        return new OutletViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OutletViewHolder pizzaViewHolder, int listPosition) {
        pizzaViewHolder.name.setText(list.get(listPosition).getName());
        pizzaViewHolder.address.setText(list.get(listPosition).getAddress());
        pizzaViewHolder.phone1.setText(list.get(listPosition).getContact());
        pizzaViewHolder.franchise.setText(list.get(listPosition).getFranchise());
        pizzaViewHolder.logo.setImageResource(list.get(listPosition).getLogoR());
    }

    public OutletAdapter addModels(ArrayList<OutletModel> list){
        this.list = list;
        return this;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class OutletViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name;
        TextView address;
        TextView phone1;
        TextView franchise;
        ImageView logo;

        public OutletViewHolder(View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.name);
            address = (TextView)itemView.findViewById(R.id.address);
            phone1 = (TextView)itemView.findViewById(R.id.phone1);
            franchise = (TextView)itemView.findViewById(R.id.franchise);
            logo = (ImageView)itemView.findViewById(R.id.logo_view);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "Selected: " + v.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
