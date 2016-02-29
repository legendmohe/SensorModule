package bt.samsung.sensormodule.ui.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bt.samsung.sensormodule.R;
import bt.samsung.sensormodule.datasource.poj.OutdoorInfoData;
import butterknife.Bind;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;
import vendor.ui.CompassView;
import vendor.ui.StepCountView;

/**
 * Created by xinyu.he on 2016/2/23.
 */
public class OutdoorViewRecyclerAdapter extends BaseRecyclerAdapter<Object> {

    public static final int INFO_CARDVIEW_VIEWTYPE = 0;
    public static final int SETP_CARDVIEW_VIEWTYPE = 1;
    public static final int SETP_HISTORY_CARDVIEW_VIEWTYPE = 2;
    private static final int[] CARDVIEW_LAYOUT_IDS = {
            R.layout.outdoor_info_cardview,
            R.layout.outdoor_step_cardview,
            R.layout.outdoor_step_history_cardview
    };

    public OutdoorViewRecyclerAdapter(List<Object> dataArgs) {
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
                OutdoorInfoViewHolder outdoorInfoViewHolder = (OutdoorInfoViewHolder) holder;
                OutdoorInfoData outdoorInfoData = (OutdoorInfoData) data;
                outdoorInfoViewHolder.AltValueTextview.setText(outdoorInfoData.uv);
                outdoorInfoViewHolder.HumValueTextview.setText(outdoorInfoData.luminance);
                outdoorInfoViewHolder.TempalueTextview.setText(outdoorInfoData.temperature);
                break;
            case SETP_CARDVIEW_VIEWTYPE:
                break;
            case SETP_HISTORY_CARDVIEW_VIEWTYPE:
                int numSubcolumns = 1;
                int numColumns = 8;
                boolean hasAxes = true;
                boolean hasAxesNames = true;
                boolean hasLabels = false;
                boolean hasLabelForSelected = false;
                // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
                List<Column> columns = new ArrayList<Column>();
                List<SubcolumnValue> values;
                for (int i = 0; i < numColumns; ++i) {

                    values = new ArrayList<SubcolumnValue>();
                    for (int j = 0; j < numSubcolumns; ++j) {
                        values.add(new SubcolumnValue((float) Math.random() * 50f + 5, ChartUtils.pickColor()));
                    }

                    Column column = new Column(values);
                    column.setHasLabels(hasLabels);
                    column.setHasLabelsOnlyForSelected(hasLabelForSelected);
                    columns.add(column);
                }

                ColumnChartData columnData = new ColumnChartData(columns);

                if (hasAxes) {
                    Axis axisX = new Axis();
                    Axis axisY = new Axis().setHasLines(true);
                    if (hasAxesNames) {
                        axisX.setName("Axis X");
                        axisY.setName("Axis Y");
                    }
                    columnData.setAxisXBottom(axisX);
                    columnData.setAxisYLeft(axisY);
                } else {
                    columnData.setAxisXBottom(null);
                    columnData.setAxisYLeft(null);
                }


                OutdoorStepHistoryViewHolder outdoorStepHistoryViewHolder = (OutdoorStepHistoryViewHolder)holder;
                outdoorStepHistoryViewHolder.columnChartView.setColumnChartData(columnData);
                break;
        }
    }

    @Override
    public RecyclerView.ViewHolder initViewHolder(View itemView, int viewType) {
        switch (viewType) {
            case INFO_CARDVIEW_VIEWTYPE:
                return new OutdoorInfoViewHolder(itemView);
            case SETP_CARDVIEW_VIEWTYPE:
                return new OutdoorStepViewHolder(itemView);
            case SETP_HISTORY_CARDVIEW_VIEWTYPE:
                return new OutdoorStepHistoryViewHolder(itemView);
        }
        return null;
    }

    public static class OutdoorInfoViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.outdoor_info_alt_value_textview) TextView AltValueTextview;
        @Bind(R.id.outdoor_info_hum_value_textview) TextView HumValueTextview;
        @Bind(R.id.outdoor_info_temp_value_textview) TextView TempalueTextview;

        public OutdoorInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class OutdoorStepViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.outdoor_stepcount)
        StepCountView stepCountView;

        public OutdoorStepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class OutdoorStepHistoryViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.outdoor_stepcount_history_columnchartview)
        ColumnChartView columnChartView;

        public OutdoorStepHistoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
