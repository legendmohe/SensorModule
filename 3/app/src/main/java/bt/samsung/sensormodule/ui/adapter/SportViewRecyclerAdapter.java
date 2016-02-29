package bt.samsung.sensormodule.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;

import java.util.List;

import bt.samsung.sensormodule.R;
import bt.samsung.sensormodule.datasource.poj.SportInfoData;
import butterknife.Bind;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.view.LineChartView;
import vendor.ui.CompassView;

/**
 * Created by xinyu.he on 2016/2/23.
 */
public class SportViewRecyclerAdapter extends BaseRecyclerAdapter<Object> {

    public static final int INFO_CARDVIEW_VIEWTYPE = 0;
    public static final int COMPASS_CARDVIEW_VIEWTYPE = 1;
    public static final int MAP_CARDVIEW_VIEWTYPE = 2;
    private static final int[] CARDVIEW_LAYOUT_IDS = {
            R.layout.sport_info_cardview,
            R.layout.sport_compass_cardview,
            R.layout.sport_map_cardview
    };

    private SportMapViewHolder mMapViewHolder;

    public SportViewRecyclerAdapter(List<Object> dataArgs) {
        super(dataArgs);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    protected int getLayoutId(int viewType) {
        return CARDVIEW_LAYOUT_IDS[viewType];
    }

    @Override
    public void renderDataToViewHolder(RecyclerView.ViewHolder holder, Object data, int viewType) {
        switch (viewType) {
            case INFO_CARDVIEW_VIEWTYPE:
                SportInfoViewHolder sportInfoViewHolder = (SportInfoViewHolder) holder;
                SportInfoData sportInfoData = (SportInfoData) data;
                sportInfoViewHolder.AltValueTextview.setText(sportInfoData.altitude);
                sportInfoViewHolder.PressureValueTextview.setText(sportInfoData.pressure);
                sportInfoViewHolder.TempalueTextview.setText(sportInfoData.temperature);
                break;
            case COMPASS_CARDVIEW_VIEWTYPE:
                break;
            case MAP_CARDVIEW_VIEWTYPE:
                break;
        }
    }

    @Override
    public RecyclerView.ViewHolder initViewHolder(View itemView, int viewType) {
        switch (viewType) {
            case INFO_CARDVIEW_VIEWTYPE:
                return new SportInfoViewHolder(itemView);
            case COMPASS_CARDVIEW_VIEWTYPE:
                return new SportCompassViewHolder(itemView);
            case MAP_CARDVIEW_VIEWTYPE:
                mMapViewHolder = new SportMapViewHolder(itemView);
                return mMapViewHolder;
        }
        return null;
    }

    public void onDestroy() {
        if (mMapViewHolder != null) {
            mMapViewHolder.mapView.onDestroy();
        }
    }

    public void onResume() {
        if (mMapViewHolder != null) {
            mMapViewHolder.mapView.onResume();
        }
    }

    public void onPause() {
        if (mMapViewHolder != null) {
            mMapViewHolder.mapView.onPause();
        }
    }

    public static class SportInfoViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.sport_info_alt_value_textview) TextView AltValueTextview;
        @Bind(R.id.sport_info_pressure_value_textview) TextView PressureValueTextview;
        @Bind(R.id.sport_info_temp_value_textview) TextView TempalueTextview;

        public SportInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class SportCompassViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.sport_compassview)
        CompassView compassView;

        public SportCompassViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class SportMapViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.sport_map_baidumapview)
        MapView mapView;

        public SportMapViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
