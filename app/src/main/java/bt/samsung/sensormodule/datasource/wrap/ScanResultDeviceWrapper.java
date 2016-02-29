package bt.samsung.sensormodule.datasource.wrap;

import android.bluetooth.BluetoothDevice;

/**
 * Created by xinyu.he on 2016/2/29.
 */
public class ScanResultDeviceWrapper {
    BluetoothDevice mDevice;

    public ScanResultDeviceWrapper(BluetoothDevice device) {
        mDevice = device;
    }

    public BluetoothDevice getDevice() {
        return mDevice;
    }

    @Override
    public String toString() {
        return mDevice.getAddress();
    }
}
