package bt.samsung.sensormodule.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;

import bt.samsung.sensormodule.R;
import bt.samsung.sensormodule.mvp.presenter.MainPresenter;
import bt.samsung.sensormodule.mvp.view.MainView;
import bt.samsung.sensormodule.ui.fragment.CardViewBaseFragment;
import bt.samsung.sensormodule.ui.fragment.HouseViewFragment;
import bt.samsung.sensormodule.ui.fragment.OutdoorViewFragment;
import bt.samsung.sensormodule.ui.fragment.SportViewFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainView {

    public static final String TAG = "MainActivity";

    private MainPresenter mPresenter;
    private CardViewBaseFragment mCurrentCardViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.activity_main);
        this.setupViews(null);

        this.mPresenter = new MainPresenter(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.mPresenter.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.mPresenter.stop();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        }else {
            if (id == R.id.nav_house && !mCurrentCardViewFragment.getClass().getSimpleName().equals("HouseViewFragment")) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                mCurrentCardViewFragment = new HouseViewFragment();

                fragmentTransaction.replace(R.id.main_fragment_container, mCurrentCardViewFragment);
                fragmentTransaction.commit();
            } else if (id == R.id.nav_sport && !mCurrentCardViewFragment.getClass().getSimpleName().equals("SportViewFragment")) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                mCurrentCardViewFragment = new SportViewFragment();

                fragmentTransaction.replace(R.id.main_fragment_container, mCurrentCardViewFragment);
                fragmentTransaction.commit();
            } else if (id == R.id.nav_outdoor && !mCurrentCardViewFragment.getClass().getSimpleName().equals("OutdoorViewFragment")) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                mCurrentCardViewFragment = new OutdoorViewFragment();

                fragmentTransaction.replace(R.id.main_fragment_container, mCurrentCardViewFragment);
                fragmentTransaction.commit();
            } else {

            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setupViews(View rootView) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        CardViewBaseFragment fragment = new HouseViewFragment();

        fragmentTransaction.replace(R.id.main_fragment_container, fragment);
        fragmentTransaction.commit();
        mCurrentCardViewFragment = fragment;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onServiceConnected() {
        Toast.makeText(getApplicationContext(), "ble service connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onServiceDisconnected() {
        Toast.makeText(getApplicationContext(), "ble service disconnected", Toast.LENGTH_SHORT).show();
    }
}
