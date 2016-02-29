package bt.samsung.sensormodule.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bt.samsung.sensormodule.R;

/**
 * Created by xinyu.he on 2016/2/22.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<T> mDatas = new ArrayList<>();

    public BaseRecyclerAdapter(List<T> dataArgs){
        if (dataArgs != null)
            mDatas.addAll(dataArgs);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(this.getLayoutId(viewType), parent, false);

        RecyclerView.ViewHolder viewHolder = this.initViewHolder(view, viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        renderDataToViewHolder(holder, mDatas.get(position), getItemViewType(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    protected abstract int getLayoutId(int viewType);
    public abstract void renderDataToViewHolder(RecyclerView.ViewHolder holder, T data, int viewType);
    public abstract RecyclerView.ViewHolder initViewHolder(View itemView, int viewType);
}

