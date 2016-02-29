package bt.samsung.sensormodule.domain.service;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import bt.samsung.sensormodule.R;
import bt.samsung.sensormodule.datasource.wrap.ScanResultDeviceWrapper;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class BLEConnectionService extends Service {

    public static final String TAG = "BLEConnectionService";

    private BluetoothAdapter mBluetoothAdapter;
    private ArrayAdapter<ScanResultDeviceWrapper> mScanResultAdapter;
    private HashSet<String> mScanResultFilterHashSet = new HashSet<>();
    private static final long SCAN_PERIOD = 10000;
    private BluetoothLeScanner mLEScanner;
    private ScanSettings settings;
    private List<ScanFilter> filters;
    private BluetoothGatt mGatt;

    private Handler mHandler;

    private final static int REQUEST_ENABLE_BT = 100;

    public static final int MSG_STOP_SERVICE = 0;

    final Messenger mMessenger = new Messenger(new IncomingHandler());
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_STOP_SERVICE:
                    showToast("MSG_STOP_SERVICE");
                    BLEConnectionService.this.stopSelf();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        showToast("Bluetooth off");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        showToast("Turning Bluetooth off...");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        showToast("Bluetooth on");
                        if (Build.VERSION.SDK_INT >= 21) {
                            mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
                            settings = new ScanSettings.Builder()
                                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                                    .build();
                            filters = new ArrayList<>();
                        }
                        scanLeDevice(true);
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        showToast("Turning Bluetooth on...");
                        break;
                }
            }
        }
    };

    public BLEConnectionService() {
        mHandler = new Handler();
    }

    private void initService() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            showToast("BLE Not Supported");
            this.stopSelf();
        }else {
            final BluetoothManager bluetoothManager =
                    (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter();
        }

        mScanResultFilterHashSet.clear();
    }

    @Override
    public IBinder onBind(Intent intent) {
        showToast("ble service binding");
        return mMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initService();
        initBLEConnection();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cleanService();
        cleanBLEConnection();
    }

    private void initBLEConnection() {

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);

        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            this.startActivity(enableBtIntent);
        } else {
            if (Build.VERSION.SDK_INT >= 21) {
                mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
                settings = new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                        .build();
                filters = new ArrayList<>();
            }
            scanLeDevice(true);
        }
    }

    private void cleanBLEConnection() {
        scanLeDevice(false);
        if (mGatt != null) {
            mGatt.disconnect();
        }
    }

    private void cleanService() {
        unregisterReceiver(mReceiver);
        mScanResultFilterHashSet.clear();
    }

    private void showToast(String content) {
        Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT).show();
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            showScanResultDialog();

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT < 21) {
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    } else {
                        mLEScanner.stopScan(mScanCallback);
                    }
                }
            }, SCAN_PERIOD);
            if (Build.VERSION.SDK_INT < 21) {
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            } else {
                mLEScanner.startScan(filters, settings, mScanCallback);
            }
        } else {
            if (Build.VERSION.SDK_INT < 21) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            } else {
                mLEScanner.stopScan(mScanCallback);
            }
        }
    }

    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Log.i(TAG, String.valueOf(callbackType));
            Log.i(TAG, result.toString());
            BluetoothDevice btDevice = result.getDevice();
            if (!mScanResultFilterHashSet.contains(btDevice.getAddress())) {
                mScanResultAdapter.add(new ScanResultDeviceWrapper(btDevice));
                mScanResultFilterHashSet.add(btDevice.getAddress());
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult result : results) {
                Log.i(TAG, result.toString());
                BluetoothDevice btDevice = result.getDevice();
                if (btDevice != null) {
                    if (!mScanResultFilterHashSet.contains(btDevice.getAddress())) {
                        mScanResultAdapter.add(new ScanResultDeviceWrapper(btDevice));
                        mScanResultFilterHashSet.add(btDevice.getAddress());
                    }
                }
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e(TAG, "Error Code: " + errorCode);
        }
    };

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    if (!mScanResultFilterHashSet.contains(device.getAddress())) {
                        mScanResultAdapter.add(new ScanResultDeviceWrapper(device));
                        mScanResultFilterHashSet.add(device.getAddress());
                    }
                }
            };

    public void connectToDevice(BluetoothDevice device) {
        if (mGatt == null) {
            mGatt = device.connectGatt(this, false, gattCallback);
            scanLeDevice(false);// will stop after first device detection
        }
    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.i(TAG, "Status: " + status);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    Log.i(TAG, "STATE_CONNECTED");
                    gatt.discoverServices();
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.e(TAG, "STATE_DISCONNECTED");
                    break;
                default:
                    Log.e(TAG, "STATE_OTHER");
            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            List<BluetoothGattService> services = gatt.getServices();
            Log.i(TAG, services.toString());
//            gatt.readCharacteristic(services.get(1).getCharacteristics().get
//                    (0));
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic
                                                 characteristic, int status) {
            Log.i(TAG, characteristic.toString());
//            gatt.disconnect();
        }
    };

    private void showScanResultDialog() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getApplicationContext());
        builderSingle.setTitle(getString(R.string.select_your_device));

        mScanResultAdapter = new ArrayAdapter<>(
                getApplicationContext(),
                android.R.layout.select_dialog_singlechoice);

        builderSingle.setNegativeButton(
                getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(
                mScanResultAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BluetoothDevice device = mScanResultAdapter.getItem(which).getDevice();
                        connectToDevice(device);
                    }
                });
        AlertDialog alertDialog = builderSingle.create();
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
}
