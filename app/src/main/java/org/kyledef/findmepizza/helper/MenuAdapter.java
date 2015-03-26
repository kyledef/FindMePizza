package org.kyledef.findmepizza.helper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.kyledef.findmepizza.R;
import org.kyledef.findmepizza.model.MenuModel;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private ArrayList<MenuModel> list;
    private MenuClickListener onItemClickListener;

    public MenuAdapter(ArrayList<MenuModel> list) {
        this.list = list;
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_layout, viewGroup, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuViewHolder menuViewHolder, int position) {
        menuViewHolder.bind(list.get(position));
    }

    public MenuAdapter addModels(ArrayList<MenuModel> list) {
        this.list = list;
        return this;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnItemClickListener(MenuClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface MenuClickListener {
        public void onItemClick(MenuModel menu);
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        TextView franchise;
        TextView category;
        TextView type;
        TextView cost;

        private MenuModel model;

        public MenuViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            franchise = (TextView) itemView.findViewById(R.id.franchise);
            category = (TextView) itemView.findViewById(R.id.category);
            type = (TextView) itemView.findViewById(R.id.type);
            cost = (TextView) itemView.findViewById(R.id.cost);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(model);
        }

        public void bind(MenuModel menuModel) {
            this.model = menuModel;
            name.setText(TextHelper.formatUserText(model.getName()));
            franchise.setText(TextHelper.formatUserText(model.getFranchise()));
            category.setText(TextHelper.formatUserText(model.getCategory()));
            type.setText(TextHelper.formatUserText(model.getType()));

            String costStr = model.getCost();
            costStr = TextHelper.convertPrice(costStr);
            cost.setText(TextHelper.formatUserText(costStr));
        }
    }
}
