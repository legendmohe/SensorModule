package bt.samsung.sensormodule.mvp.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.security.InvalidParameterException;

import bt.samsung.sensormodule.domain.service.BLEConnectionService;
import bt.samsung.sensormodule.mvp.view.MainView;

/**
 * Created by xinyu.he on 2016/2/23.
 */
public class MainPresenter extends MVPPresenter {
    protected static final String TAG = "MainPresenter";

    private WeakReference<MainView> mMainView;
    Messenger mService = null;
    boolean mBound;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);
            mBound = true;

            if (mMainView.get() != null) {
                mMainView.get().onServiceConnected();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
            mBound = false;

            if (mMainView.get() != null) {
                mMainView.get().onServiceDisconnected();
            }
        }
    };

    public MainPresenter(MainView view) {
        if (view == null)
            throw new InvalidParameterException("view cannot be null");

        mMainView = new WeakReference<>(view);
    }

    @Override
    public void start() {
        Log.d(TAG, "MainPresenter start.");
        MainView mainView = mMainView.get();
        if (mainView != null) {
            mainView.getContext().bindService(new Intent(mainView.getContext(), BLEConnectionService.class), mConnection,
                    Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public void stop() {
        Log.d(TAG, "MainPresenter stop.");
        MainView mainView = mMainView.get();
        if (mBound && mainView != null) {
            mainView.getContext().unbindService(mConnection);
            mBound = false;
        }
    }

    public void stopBLEService() {
        if (!mBound) return;
        Message msg = Message.obtain(null, BLEConnectionService.MSG_STOP_SERVICE, 0, 0);
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
