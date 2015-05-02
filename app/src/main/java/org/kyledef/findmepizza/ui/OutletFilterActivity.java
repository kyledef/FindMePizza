package org.kyledef.findmepizza.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import org.kyledef.findmepizza.R;
import org.kyledef.findmepizza.helper.Constants;

public class OutletFilterActivity extends AppCompatActivity {

    String franchise;
    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlet_filter);
    }

    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();
        if (checked) {
            switch (view.getId()) {
                case R.id.radio_franchise_all:
                    franchise = Constants.ALL;
                    break;
                case R.id.radio_domino:
                    franchise = "dominos";
                    break;
                case R.id.radio_mario:
                    franchise = "marios";
                    break;
                case R.id.radio_papa_john:
                    franchise = "papajohn";
                    break;
                case R.id.radio_pizza_hut:
                    franchise = "pizzahut";
                    break;
                case R.id.radio_loc_all:
                    location = Constants.ALL;
                    break;
                case R.id.radio_north:
                    location = Constants.NORTH;
                    break;
                case R.id.radio_west:
                    location = Constants.WEST;
                    break;
                case R.id.radio_east:
                    location = Constants.EAST;
                    break;
                case R.id.radio_central:
                    location = Constants.CENTRAL;
                    break;
                case R.id.radio_south:
                    location = Constants.SOUTH;
                    break;
                default:
                    location = franchise = Constants.ALL;
            }
        }
    }

    public void onButtonClicked(View view){
        if (view.getId() == R.id.run_filter){
            Intent intent = new Intent();
            intent.putExtra("franchise", franchise);
            intent.putExtra("location", location);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

}
