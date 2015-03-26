package org.kyledef.findmepizza.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import org.kyledef.findmepizza.R;

public class RecentActivity  extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add the Current Layout as part of the parent layout
        FrameLayout content = (FrameLayout) findViewById(R.id.content_frame);
        View layout = LayoutInflater.from(this).inflate(R.layout.activity_recent_list, content, false);
        content.addView(layout);
    }

    @Override
    protected String getScreenName(){
        return "Recent";
    }
}