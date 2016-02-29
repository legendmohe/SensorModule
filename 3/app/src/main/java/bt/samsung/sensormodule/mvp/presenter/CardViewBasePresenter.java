package bt.samsung.sensormodule.mvp.presenter;

import android.util.Log;

import java.lang.ref.WeakReference;
import java.security.InvalidParameterException;

import bt.samsung.sensormodule.mvp.view.CardViewBaseView;

/**
 * Created by xinyu.he on 2016/2/23.
 */
public class CardViewBasePresenter extends MVPPresenter  {
    protected static final String TAG = "CardViewBasePresenter";

    private WeakReference<CardViewBaseView> mCardViewBaseView;

    public CardViewBasePresenter(CardViewBaseView view) {
        if (view == null)
            throw new InvalidParameterException("view cannot be null");

        mCardViewBaseView = new WeakReference<CardViewBaseView>(view);
    }

    @Override
    public void start() {
        Log.d(TAG, "CardViewBasePresenter start.");
    }

    @Override
    public void stop() {
        Log.d(TAG, "CardViewBasePresenter stop.");
    }
}
