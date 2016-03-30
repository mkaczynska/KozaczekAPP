package com.blstream.kaczynska.kozaczekrssreader;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ItemListFragment extends Fragment implements Constants, IGetListListener, OnHeadlineSelectedListener {

    private static final String LIST_STATE_KEY = "list_state";
    BroadcastReceiver receiver;
    SwipeRefreshLayout pullToRefresh;
    PullToRefreshListener pullToRefreshListener;
//    OnHeadlineSelectedListener onHeadlineSelectedListener;
    private RecyclerView recyclerView;
    private Channel rssChannel = new Channel();
    private MyRssFeedRecyclerViewAdapter adapter = new MyRssFeedRecyclerViewAdapter(rssChannel);
    private MenuItem refreshMenuButton;
    private IGetListListener iGetListListener;

    public void setiGetListListener(IGetListListener iGetListListener) {
        this.iGetListListener = iGetListListener;
    }

    public ItemListFragment() {
    }

    @Override
    public void onGetListListener(Channel receivedChannel) {
        rssChannel.itemsList.clear();
        rssChannel.itemsList.addAll(receivedChannel.itemsList);
        rssChannel.setChannelTitle(receivedChannel.getChannelTitle());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        if (savedInstanceState != null) {
            adapter = savedInstanceState.getParcelable(Constants.SAVED_ADAPTER);
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(LIST_STATE_KEY);
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
        setiGetListListener(this);
        adapter.setOnHeadlineSelectedListener(this);
        recyclerView.setAdapter(adapter);
        setHasOptionsMenu(true);
    }

    private void setupRecyclerView() {
        if (recyclerView == null) {
            recyclerView = (RecyclerView) getView().findViewById(R.id.list);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (NetworkUtils.isConnected(connMgr)) {
            if (item.getItemId() == R.id.action_refresh) {
                refreshMenuButton = item;
                refreshMenuButton.setEnabled(false);
                pullToRefresh.setEnabled(false);
                startRefreshButtonAnimation();
            }
        }
        return true;
    }

    private void stopRefreshButtonAnimation() {
        if (refreshMenuButton != null && refreshMenuButton.getActionView() != null) {
            refreshMenuButton.getActionView().clearAnimation();
            refreshMenuButton.setActionView(null);
        }
    }

    private void startRefreshButtonAnimation() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ImageView imageButton = (ImageView) inflater.inflate(R.layout.refresh_action, null);

        ObjectAnimator animator = ObjectAnimator.ofFloat(imageButton, "rotation", 0f, 360f);

        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(1000);
        animator.start();
        refreshMenuButton.setActionView(imageButton);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupPullToRefreshListener();
        IntentFilter rssReceiverFilter = new IntentFilter(Constants.INTENT_MAIN);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Channel receivedChannel = intent.getParcelableExtra(Constants.CHANNEL);
                iGetListListener.onGetListListener(receivedChannel);
                pullToRefresh.setRefreshing(false);
                pullToRefresh.setEnabled(true);
                if (refreshMenuButton != null) {
                    refreshMenuButton.setEnabled(true);
                }
                stopRefreshButtonAnimation();
            }
        };
        getActivity().registerReceiver(receiver, rssReceiverFilter);
    }


    private void setupPullToRefreshListener() {
        pullToRefresh = (SwipeRefreshLayout) getView().findViewById(R.id.pullToRefresh);
        pullToRefresh.setRefreshing(false);
        pullToRefresh.setEnabled(true);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ConnectivityManager connMgr = (ConnectivityManager)
                        getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                if (NetworkUtils.isConnected(connMgr)) {
                    pullToRefresh.setRefreshing(true);
                    pullToRefresh.setEnabled(false);
                    if (refreshMenuButton != null) {
                        refreshMenuButton.setEnabled(false);
                    }
                    pullToRefreshListener.onRefreshList();
                }
            }
        });
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(receiver);

        super.onPause();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            pullToRefreshListener = (PullToRefreshListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement PullToRefreshListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        pullToRefreshListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.SAVED_ADAPTER, adapter);
        outState.putParcelable(LIST_STATE_KEY, recyclerView.getLayoutManager()
                .onSaveInstanceState());
    }

    @Override
    public void onHeaderSelected(int position) {
        String urlString = rssChannel.itemsList.get(position).getLink();
        Uri uri = Uri.parse(urlString);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(browserIntent);
    }

    public interface PullToRefreshListener {
        void onRefreshList();
    }
}