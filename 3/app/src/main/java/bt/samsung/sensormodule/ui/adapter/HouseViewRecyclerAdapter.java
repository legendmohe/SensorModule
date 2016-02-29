package bt.samsung.sensormodule.ui.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bt.samsung.sensormodule.R;
import bt.samsung.sensormodule.datasource.poj.HouseInfoData;
import butterknife.Bind;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by xinyu.he on 2016/2/23.
 */
public class HouseViewRecyclerAdapter extends BaseRecyclerAdapter<Object> {

    public static final int INFO_CARDVIEW_VIEWTYPE = 0;
    public static final int INFO_HISTORY_CARDVIEW_VIEWTYPE = 1;
    private static final int[] CARDVIEW_LAYOUT_IDS = {R.layout.house_info_cardview, R.layout.house_info_history_cardview};

    public HouseViewRecyclerAdapter(List<Object> dataArgs) {
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
                HouseInfoViewHolder houseInfoViewHolder = (HouseInfoViewHolder) holder;
                HouseInfoData houseInfoData = (HouseInfoData) data;
                houseInfoViewHolder.UVValueTextview.setText(houseInfoData.UV);
                houseInfoViewHolder.HumValueTextview.setText(houseInfoData.Humidity);
                houseInfoViewHolder.TempalueTextview.setText(houseInfoData.Temperature);
                break;
            case INFO_HISTORY_CARDVIEW_VIEWTYPE:
                List<PointValue> values = new ArrayList<>();
                values.add(new PointValue(0, 2));
                values.add(new PointValue(1, 4));
                values.add(new PointValue(2, 3));
                values.add(new PointValue(3, 4));

                //In most cased you can call data model methods in builder-pattern-like manner.
                Line line = new Line(values).setColor(Color.YELLOW);
                line.setStrokeWidth(2);
                List<Line> lines = new ArrayList<>();
                lines.add(line);

                LineChartData lineChartData = new LineChartData();
                lineChartData.setAxisXBottom(new Axis());
                lineChartData.setLines(lines);


                HouseInfoHistoryViewHolder houseInfoHistoryViewHolder = (HouseInfoHistoryViewHolder)holder;
                houseInfoHistoryViewHolder.lineChartView.setLineChartData(lineChartData);
                break;
        }
    }

    @Override
    public RecyclerView.ViewHolder initViewHolder(View itemView, int viewType) {
        switch (viewType) {
            case INFO_CARDVIEW_VIEWTYPE:
                return new HouseInfoViewHolder(itemView);
            case INFO_HISTORY_CARDVIEW_VIEWTYPE:
                return new HouseInfoHistoryViewHolder(itemView);
        }
        return null;
    }

    public static class HouseInfoViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.house_info_uv_value_textview) TextView UVValueTextview;
        @Bind(R.id.house_info_hum_value_textview) TextView HumValueTextview;
        @Bind(R.id.house_info_temp_value_textview) TextView TempalueTextview;

        public HouseInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class HouseInfoHistoryViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.house_info_history_linechartview)
        LineChartView lineChartView;

        public HouseInfoHistoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
