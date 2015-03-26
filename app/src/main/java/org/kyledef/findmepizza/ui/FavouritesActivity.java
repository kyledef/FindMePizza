package org.kyledef.findmepizza.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import org.kyledef.findmepizza.R;

public class FavouritesActivity  extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add the Current Layout as part of the parent layout
        FrameLayout content = (FrameLayout) findViewById(R.id.content_frame);
        View layout = LayoutInflater.from(this).inflate(R.layout.activity_favourites_list, content, false);
        content.addView(layout);
    }

    protected String getScreenName(){
        return "Favourites";
    }
}