package com.blstream.kaczynska.kozaczekrssreader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.blstream.kaczynska.kozaczekrssreader.ItemListFragment.PullToRefreshListener;
import com.blstream.kaczynska.kozaczekrssreader.Preferences.AppPreferences;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PullToRefreshListener, Constants {

    Fragment itemListFragment;
    OnConnectivityChangeReceiver connectivityChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            itemListFragment = new ItemListFragment();
            fragmentTransaction.add(R.id.fragment, itemListFragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, FRAGMENT_ID, itemListFragment);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        itemListFragment = getSupportFragmentManager().getFragment(outState, FRAGMENT_ID);
        super.onSaveInstanceState(outState);
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
        if(item.getItemId() == R.id.action_settings)
        {
            Intent i = new Intent(this, AppPreferences.class);
            startActivity(i);
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



    /*@Override
    public void onHeaderSelected(int position, List<Item> itemList) {
        String urlString = itemList.get(position).getLink();
        Uri uri = Uri.parse(urlString);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(browserIntent);

    }*/

    private class OnConnectivityChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            boolean isConnected = NetworkUtils.isConnected(connMgr);
            if (isConnected) {
                getData();
            }
        }
    }
}