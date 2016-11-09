package org.kyledef.findmepizza.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.kyledef.findmepizza.R;
import org.kyledef.findmepizza.helper.AccountUtils;
import org.kyledef.findmepizza.helper.AnalyticsHelper;
import org.kyledef.findmepizza.helper.SignInCallback;
import org.kyledef.findmepizza.ui.fragments.NavDrawerFragment;

public abstract class BaseActivity extends AppCompatActivity implements NavDrawerFragment.NavigationDrawerCallbacks, SignInCallback {

    private NavDrawerFragment mNavigationDrawerFragment;
    protected AnalyticsHelper analyticsHelper;

    protected abstract String getScreenName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_base);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) setSupportActionBar(toolbar);

        analyticsHelper = new AnalyticsHelper(this);
        analyticsHelper.setScreen(getScreenName());

        mNavigationDrawerFragment = (NavDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(this, R.id.navigation_drawer,(DrawerLayout) findViewById(R.id.drawer_layout));

        //Setup Header
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
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
        //TODO
    }

    private void launchAccount(){
        Log.d("BaseActivity", "Launching Sign In Process");
        AccountUtils accountUtils = AccountUtils.getInstance();
        accountUtils.signInUser(this);
    }

    private void launchHome(){
        if (!(this instanceof  FranchiseList))
            startActivity(new Intent(this, FranchiseList.class));
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

    // Used for Sign In Request
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AccountUtils.RC_SIGN_IN){
            AccountUtils.getInstance().handleSignInResponse(this, resultCode);
        }
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

    public void onSignInSuccess(){
        Toast.makeText(this, R.string.signInSuccess, Toast.LENGTH_SHORT).show();
        analyticsHelper.signIn(true);
        startActivity(new Intent(this, PizzaList.class));
        finish();
    }
    public void onSignInFailure(String message){
        analyticsHelper.signIn(false);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void onSignOutSuccess() {
        Toast.makeText(this, R.string.signOutSuccess, Toast.LENGTH_SHORT).show();
        analyticsHelper.signOut(true);
        startActivity(new Intent(this, PizzaList.class));
        finish();
    }

    public void onSignOutFailure(String message) {
        analyticsHelper.signOut(false);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
