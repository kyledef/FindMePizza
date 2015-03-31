package org.kyledef.findmepizza.ui.fragments;

import android.accounts.Account;
import android.content.res.TypedArray;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.kyledef.findmepizza.R;
import org.kyledef.findmepizza.helper.AccountUtils;
import org.kyledef.findmepizza.helper.DrawerAdapter;
import org.kyledef.findmepizza.model.NarBarItem;

import java.util.ArrayList;


public class NavDrawerFragment extends Fragment {

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private NavigationDrawerCallbacks mCallbacks;
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mDrawerTitles;
    private TypedArray mDrawerIcons;
    private ArrayList<NarBarItem> mDrawerItems;

    public NavDrawerFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mDrawerListView = (ListView) inflater.inflate( R.layout.fragment_navigation_drawer, container, false);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        return mDrawerListView;
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }


    public void setUp(final ActionBarActivity activity, int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = activity.findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        if (toolbar != null) activity.setSupportActionBar(toolbar);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mTitle = mDrawerTitle = activity.getTitle();

        mDrawerToggle = new ActionBarDrawerToggle(
                activity,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer icon to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description */
                R.string.navigation_drawer_close  /* "close drawer" description */
        ) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                activity.getSupportActionBar().setTitle(mTitle);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                activity.getSupportActionBar().setTitle(mDrawerTitle);
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        buildNavMenuOptions(activity);
    }

    private void buildNavMenuOptions(final ActionBarActivity activity){
        mDrawerTitles = getResources().getStringArray(R.array.menu_items);
        mDrawerIcons = getResources().obtainTypedArray(R.array.drawer_icons);
        mDrawerItems = new ArrayList<>();

        for (int i = 0; i < mDrawerTitles.length; i++){
            mDrawerItems.add(new NarBarItem(mDrawerTitles[i].toString(),mDrawerIcons.getResourceId(i, 0)));
        }

        LayoutInflater inflater = activity.getLayoutInflater();
        final ViewGroup header = (ViewGroup)inflater.inflate(R.layout.header, mDrawerListView, false);
        final ViewGroup footer = (ViewGroup)inflater.inflate(R.layout.footer, mDrawerListView, false);

        // Configure the Header
        this.setUpHeader(activity, header);

        mDrawerListView.addHeaderView(header, null, true); // true = clickable
        mDrawerListView.addFooterView(footer, null, true); // true = clickable

        DrawerAdapter adapter = new DrawerAdapter(activity, mDrawerItems);
        mDrawerListView.setAdapter(adapter);
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    public void setUpHeader(ActionBarActivity activity, ViewGroup header){
        if (header.findViewById(R.id.account_name) == null){
            return;
        }
        Account chosenAccount = AccountUtils.getActiveAccount(activity);
        if (chosenAccount != null){
            String plusName = AccountUtils.getPlusName(activity);
            if (plusName != null)
                ((TextView)header.findViewById(R.id.account_name)).setText(plusName);
            else
                header.findViewById(R.id.account_name).setVisibility(View.GONE);
            ((TextView)header.findViewById(R.id.account_email)).setText(chosenAccount.name);
            ImageView imageView = (ImageView)header.findViewById(R.id.profile_image);
            AccountUtils.loadAccountImage(activity, chosenAccount, imageView);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

//        if (item.getItemId() == R.id.action_example) {
//            Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT).show();
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }
}
