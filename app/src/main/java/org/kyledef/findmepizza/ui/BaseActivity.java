package org.kyledef.findmepizza.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.plus.People;

import org.kyledef.findmepizza.R;
import org.kyledef.findmepizza.helper.AccountUtils;
import org.kyledef.findmepizza.helper.GAnalyticsHelper;
import org.kyledef.findmepizza.helper.GAuthListeners;
import org.kyledef.findmepizza.ui.fragments.NavDrawerFragment;

public abstract class BaseActivity extends ActionBarActivity implements NavDrawerFragment.NavigationDrawerCallbacks {

    private NavDrawerFragment mNavigationDrawerFragment;

    protected abstract String getScreenName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_base);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) setSupportActionBar(toolbar);

        mNavigationDrawerFragment = (NavDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(this, R.id.navigation_drawer,(DrawerLayout) findViewById(R.id.drawer_layout));

        //Setup Header
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onResume(){
        super.onResume();
        GAnalyticsHelper.getInstance(this.getApplicationContext()).sendScreenView(getScreenName());
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        switch(position){
            case 0:
                launchAccount();
                break;
            case 1: // Home
                launchHome();
                break;
            case 2: // Recent
                launchRecent();
                break;
            case 3: // Favourites
                launchFavourites();
                break;
            case 4: // Settings
                launchSettings();
                break;
            default:
                launchHome();
        }
    }


    public void restoreActionBar() {

    }

    private void launchAccount(){
        Toast.makeText(this, "Account Manager", Toast.LENGTH_SHORT).show();
        if (AccountUtils.hasActiveAccount(this)){

        }else{
            // Start Process for user to sign in
            AccountManager am = AccountManager.get(this);
            Account[] accounts = am.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
            if (accounts.length > 0){
                String accountName = accounts[0].name; // Use the default google account // TODO Give user the ability to choose
                AccountUtils.setActiveAccountName(this, accountName);
                AccountUtils.signInUser(this, accountName);
            }
        }
    }

    private void launchHome(){
        startActivity(new Intent(this, PizzaList.class));
        if (this instanceof PizzaList)
            finish();
    }
    
    private void launchRecent(){
        startActivity(new Intent(this, RecentActivity.class ));
    }

    private void launchFavourites(){
        startActivity(new Intent(this, FavouritesActivity.class));
    }

    private void launchSettings(){
        startActivity(new Intent(this, SettingsActivity.class ));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.pizza_list, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            launchSettings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
