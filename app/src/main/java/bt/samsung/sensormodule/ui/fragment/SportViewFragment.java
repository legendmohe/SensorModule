package bt.samsung.sensormodule.ui.fragment;

import android.support.v7.widget.RecyclerView;

import java.util.Arrays;

import bt.samsung.sensormodule.datasource.poj.HouseInfoData;
import bt.samsung.sensormodule.datasource.poj.SportInfoData;
import bt.samsung.sensormodule.ui.adapter.HouseViewRecyclerAdapter;
import bt.samsung.sensormodule.ui.adapter.SportViewRecyclerAdapter;

/**
 * Created by xinyu.he on 2016/2/23.
 */
public class SportViewFragment extends CardViewBaseFragment {

    private SportViewRecyclerAdapter mAdapter;

    @Override
    protected void setupAdapter(RecyclerView mRecyclerView) {
        mAdapter = new SportViewRecyclerAdapter(Arrays.asList(new SportInfoData("1", "2", "3"), "JACK", "JACK"));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected String getTitle() {
        return "Sport";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdapter.onPause();
    }
}
