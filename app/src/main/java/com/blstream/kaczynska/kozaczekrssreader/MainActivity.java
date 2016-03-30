package com.blstream.kaczynska.kozaczekrssreader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.blstream.kaczynska.kozaczekrssreader.ItemListFragment.PullToRefreshListener;

public class MainActivity extends AppCompatActivity implements PullToRefreshListener, Constants {

    Fragment itemListFragment;
    Fragment noConnectionFragment;
    OnConnectivityChangeReceiver connectivityChangeReceiver;
    ConnectivityManager connMgr;
    Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.savedInstanceState = savedInstanceState;
//        manageFragments(savedInstanceState);
    }

    public void manageFragments(Bundle savedInstanceState) {

        connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        if (NetworkUtils.isConnected(connMgr)) {
            handleItemListFragment(savedInstanceState);
        } else {
            handleNoConnectionFragment(savedInstanceState);
        }
    }

    private void handleNoConnectionFragment(Bundle savedInstanceState) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (savedInstanceState == null) {
            noConnectionFragment = new NoConnectionFragment();
        } else {
            noConnectionFragment = getSupportFragmentManager().getFragment(savedInstanceState, NO_CONNECTION_FRAGMENT_ID);
            if (noConnectionFragment == null) {
                noConnectionFragment = new NoConnectionFragment();
            }
        }
        fragmentTransaction.replace(R.id.fragment, noConnectionFragment);
        fragmentTransaction.commit();
    }

    private void handleItemListFragment(Bundle savedInstanceState) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (savedInstanceState == null) {
            itemListFragment = new ItemListFragment();
        } else {
            itemListFragment = getSupportFragmentManager().getFragment(savedInstanceState, ITEM_LIST_FRAGMENT_ID);
            if (itemListFragment == null) {
                itemListFragment = new ItemListFragment();
            }
        }
        fragmentTransaction.replace(R.id.fragment, itemListFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (itemListFragment != null) {
            getSupportFragmentManager().putFragment(outState, ITEM_LIST_FRAGMENT_ID, itemListFragment);
        }
        if (noConnectionFragment != null) {
            getSupportFragmentManager().putFragment(outState, NO_CONNECTION_FRAGMENT_ID, noConnectionFragment);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        if (item.getItemId() == R.id.action_refresh && NetworkUtils.isConnected(connMgr)) {
            getData();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onResume() {
        IntentFilter filter =
                new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");

        connectivityChangeReceiver = new OnConnectivityChangeReceiver();
        registerReceiver(connectivityChangeReceiver, filter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(connectivityChangeReceiver);
        super.onPause();
    }

    private void getData() {
        Intent intent = new Intent(this, RssPullService.class);
        intent.putExtra(URL_ID, RSS_LINK);
        startService(intent);
    }

    @Override
    public void onRefreshList() {
        getData();
    }

    private class OnConnectivityChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            boolean isConnected = NetworkUtils.isConnected(connMgr);
            if (isConnected) {
                handleItemListFragment(savedInstanceState);
                getData();
                noConnectionFragment = null;
            } else {
                handleNoConnectionFragment(savedInstanceState);
                itemListFragment = null;

            }
        }
    }
}