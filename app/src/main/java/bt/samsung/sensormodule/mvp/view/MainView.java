package bt.samsung.sensormodule.mvp.view;

/**
 * Created by xinyu.he on 2016/2/23.
 */
public interface MainView extends MVPView {
    void onServiceConnected();
    void onServiceDisconnected();
}
