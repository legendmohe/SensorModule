package bt.samsung.sensormodule.ui.fragment;

import android.support.v7.widget.RecyclerView;

import java.util.Arrays;

import bt.samsung.sensormodule.datasource.poj.HouseInfoData;
import bt.samsung.sensormodule.ui.adapter.HouseViewRecyclerAdapter;

/**
 * Created by xinyu.he on 2016/2/23.
 */
public class HouseViewFragment extends CardViewBaseFragment {

    private HouseViewRecyclerAdapter mAdapter;

    @Override
    protected void setupAdapter(RecyclerView mRecyclerView) {
        mAdapter = new HouseViewRecyclerAdapter(Arrays.asList(new HouseInfoData("1", "2", "3"), "JACK"));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected String getTitle() {
        return "House";
    }
}
