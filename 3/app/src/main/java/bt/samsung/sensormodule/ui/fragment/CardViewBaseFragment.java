package bt.samsung.sensormodule.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bt.samsung.sensormodule.R;
import bt.samsung.sensormodule.mvp.presenter.CardViewBasePresenter;
import bt.samsung.sensormodule.mvp.view.CardViewBaseView;
import bt.samsung.sensormodule.ui.adapter.BaseRecyclerAdapter;

public abstract class CardViewBaseFragment extends Fragment implements CardViewBaseView {

    protected CardViewBasePresenter mCardViewBasePresenter;
    protected RecyclerView mRecyclerView;

    public CardViewBaseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        this.mCardViewBasePresenter.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        this.mCardViewBasePresenter.stop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_card_view_base, container, false);
        this.setupViews(rootView);
        this.setupData();

        return rootView;
    }

    private void setupData() {
        this.mCardViewBasePresenter = new CardViewBasePresenter(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void setupViews(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        this.setupAdapter(mRecyclerView);

        String title = getTitle();
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (!TextUtils.isEmpty(title) && actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    @Override
    public Context getContext() {
        return this.getActivity();
    }

    protected abstract void setupAdapter(RecyclerView mRecyclerView);
    protected abstract String getTitle();
}
