package bt.samsung.sensormodule.ui.fragment;

import android.support.v7.widget.RecyclerView;

import java.util.Arrays;

import bt.samsung.sensormodule.datasource.poj.OutdoorInfoData;
import bt.samsung.sensormodule.datasource.poj.SportInfoData;
import bt.samsung.sensormodule.ui.adapter.OutdoorViewRecyclerAdapter;

/**
 * Created by xinyu.he on 2016/2/23.
 */
public class OutdoorViewFragment extends CardViewBaseFragment {

    private OutdoorViewRecyclerAdapter mAdapter;

    @Override
    protected void setupAdapter(RecyclerView mRecyclerView) {
        mAdapter = new OutdoorViewRecyclerAdapter(Arrays.asList(new OutdoorInfoData("1", "2", "3"), "JACK", "MAY"));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected String getTitle() {
        return "Outdoor";
    }
}
