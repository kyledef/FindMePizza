package org.kyledef.findmepizza.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import org.kyledef.findmepizza.R;
import org.kyledef.findmepizza.model.NarBarItem;

import java.util.ArrayList;

public class DrawerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NarBarItem> items;

    public DrawerAdapter(Context context, ArrayList<NarBarItem> list){
        this.context = context;
        this.items = list;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public NarBarItem getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.nav_drawer_item, viewGroup, false);
        }

        ((ImageView) view.findViewById(R.id.item_icon)).setImageResource(items.get(position).getIcon());
        ((TextView) view.findViewById(R.id.item_text)).setText(items.get(position).getTitle());

        return view;
    }
}
