package youten.redo.ble.ibeacondetector;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import youten.redo.ble.util.BleUtil;
import youten.redo.ble.util.DrawView;
import youten.redo.ble.util.ScannedDevice;

/**
 * BLEデバイスをスキャンし、一覧に表示するActivity。
 */
public class ScanActivity extends Activity implements BluetoothAdapter.LeScanCallback {
    private static final String TAG_LICENSE = "license";
    private BluetoothAdapter mBTAdapter;
    private DeviceAdapter mDeviceAdapter;
    private boolean mIsScanning;

    public int c=0;
    public double i=0,j=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_scan);
        DrawView pv = new DrawView(this);
        TextView textView = (TextView)findViewById(R.id.textView);
        ImageView iv1 = (ImageView) findViewById(R.id.imageView1);
        ImageView iv2 = (ImageView) findViewById(R.id.imageView2);
        ImageView iv3 = (ImageView) findViewById(R.id.imageView3);
        RelativeLayout.LayoutParams Params1 = new RelativeLayout.LayoutParams(75, 100);
        iv1.setLayoutParams(Params1);
        Params1.setMargins(330,30, 0, 0);
        RelativeLayout.LayoutParams Params2 = new RelativeLayout.LayoutParams(75, 100);
        iv2.setLayoutParams(Params2);
        Params2.setMargins(0, 590, 0, 0);
        RelativeLayout.LayoutParams Params3 = new RelativeLayout.LayoutParams(75, 100);
        iv3.setLayoutParams(Params3);
        Params3.setMargins(630, 590, 0, 0);
        String str = "Blue\nRSSI_MAX:" + mDeviceAdapter.blue_rssimax+
                // "\nRSSI_AVG:" + mDeviceAdapter.blue_rssiaverage+
                // "\nRSSI_Min:" + mDeviceAdapter.blue_rssimin+
                // "\nDIS_MAX:" + mDeviceAdapter.blue_dismax+
                 "\nDIS_AVG:" + mDeviceAdapter.blue_disaverage+
                 //"\nDIS_Min:" + mDeviceAdapter.blue_dismin+
                 "\nGreen\nRSSI_MAX:" + mDeviceAdapter.green_rssimax+
                 //"\nRSSI_AVG:" + mDeviceAdapter.green_rssiaverage+
                 //"\nRSSI_Min:" + mDeviceAdapter.green_rssimin+
                // "\nDIS_MAX:" + mDeviceAdapter.green_dismax+
                 "\nDIS_AVG:" + mDeviceAdapter.green_disaverage+
                 //"\nDIS_Min:" + mDeviceAdapter.green_dismin+
                 "\nPurple\nRSSI_MAX:" + mDeviceAdapter.purple_rssimax+
                 //"\nRSSI_AVG:" + mDeviceAdapter.purple_rssiaverage+
                // "\nRSSI_Min:" + mDeviceAdapter.purple_rssimin+
                // "\nDIS_MAX:" + mDeviceAdapter.purple_dismax+
                 "\nDIS_AVG:" + mDeviceAdapter.purple_disaverage+
                 "\nDIS_Min:" + mDeviceAdapter.purple_dismin;
        textView.setText(str);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if ((mBTAdapter != null) && (!mBTAdapter.isEnabled())) {
            Toast.makeText(this, R.string.bt_not_enabled, Toast.LENGTH_SHORT).show();
            invalidateOptionsMenu();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        stopScan();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mIsScanning) {
            menu.findItem(R.id.action_scan).setVisible(false);
            menu.findItem(R.id.action_stop).setVisible(true);
        } else {
            menu.findItem(R.id.action_scan).setEnabled(true);
            menu.findItem(R.id.action_scan).setVisible(true);
            menu.findItem(R.id.action_stop).setVisible(false);
        }
        if ((mBTAdapter == null) || (!mBTAdapter.isEnabled())) {
            menu.findItem(R.id.action_scan).setEnabled(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            // ignore
            return true;
        } else if (itemId == R.id.action_scan) {
            startScan();
            return true;
        } else if (itemId == R.id.action_stop) {
            stopScan();
            return true;
        } else if (itemId == R.id.action_clear) {
            if ((mDeviceAdapter != null) && (mDeviceAdapter.getCount() > 0)) {
                mDeviceAdapter.clear();
                mDeviceAdapter.notifyDataSetChanged();
                getActionBar().setSubtitle("");
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onLeScan(final BluetoothDevice newDeivce, final int newRssi, final byte[] newScanRecord) {
        final DrawView view=new DrawView(this);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String summary = mDeviceAdapter.update(newDeivce, newRssi, newScanRecord);
                double x[] = mDeviceAdapter.x;

                if (summary != null) {
                    if (mDeviceAdapter.clc) {
                        TextView textView = (TextView) findViewById(R.id.textView);
                        String str = "Blue\nRSSI_MAX:" + mDeviceAdapter.blue_rssimax+
                                 //"\nRSSI_AVG:" + mDeviceAdapter.blue_rssiaverage+
                                 //"\nRSSI_Min:" + mDeviceAdapter.blue_rssimin +
                                 //"\nDIS_MAX:" + mDeviceAdapter.blue_dismax+
                                 "\nDIS_AVG:" + mDeviceAdapter.blue_disaverage+
                                 //"\nDIS_Min:" + mDeviceAdapter.blue_dismin+
                                 "\nbluedis:" + mDeviceAdapter.blue_dis+
                                 "\nGreen\nRSSI_MAX:" + mDeviceAdapter.green_rssimax+
                                 //"\nRSSI_AVG:" + mDeviceAdapter.green_rssiaverage+
                                 //"\nRSSI_Min:" + mDeviceAdapter.green_rssimin+
                                 //"\nDIS_MAX:" + mDeviceAdapter.green_dismax+
                                 "\nDIS_AVG:" + mDeviceAdapter.green_disaverage+
                                 //"\nDIS_Min:" + mDeviceAdapter.green_dismin+
                                 "\ngreendis:" + mDeviceAdapter.green_dis+
                                 "\nPurple\nRSSI_MAX:" + mDeviceAdapter.purple_rssimax+
                                 //"\nRSSI_AVG:" + mDeviceAdapter.purple_rssiaverage+
                                 //"\nRSSI_Min:" + mDeviceAdapter.purple_rssimin+
                                 //"\nDIS_MAX:" + mDeviceAdapter.purple_dismax+
                                 "\nDIS_AVG:" + mDeviceAdapter.purple_disaverage+
                                 //"\nDIS_Min:" + mDeviceAdapter.purple_dismin+
                                 "\npurpledis:" + mDeviceAdapter.purple_dis;
                        textView.setText(str);
                        mDeviceAdapter.clc = false;
                    }
                    ImageView iv4 = (ImageView) findViewById(R.id.imageView4);
                    FrameLayout layout = (FrameLayout) findViewById(R.id.root);

                    if (x != null) {
                        //i = i + x[0];
                       // j = j + x[1];
                       // c++;
                        //if (c >= 5) {
                         //   i /= 5;
                         //   j /= 5;
                         //   c = 0;
                            // 創建畫筆]
//                            Paint p = new Paint();
//                            p.setColor(Color.BLUE);//blue
//                            p.setAntiAlias(true);// 設置畫筆的鋸齒效果。 true是去除，大家一看效果就明白了
//                            p.setAlpha(100);
                            // canvas.drawCircle(350, 100, (float) mDeviceAdapter.blue_dis*150, p);
                            view.invalidate();
                            layout.addView(view);

                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
                            iv4.setLayoutParams(params);
                            params.setMargins((int) x[0], (int) x[1], 0, 0);
//                            params.setMargins((int) i, (int) j, 0, 0);
                       // }
                    }
                    getActionBar().setSubtitle(summary);
                }
            }
        });
    }

    private void init() {
        // BLE check
        if (!BleUtil.isBLESupported(this)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // BT check
        BluetoothManager manager = BleUtil.getManager(this);
        if (manager != null) {
            mBTAdapter = manager.getAdapter();
        }
        if (mBTAdapter == null) {
            Toast.makeText(this, R.string.bt_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // init listview
        ListView deviceListView = (ListView) findViewById(R.id.list);
        mDeviceAdapter = new DeviceAdapter(this, R.layout.listitem_device, new ArrayList<ScannedDevice>());
        deviceListView.setAdapter(mDeviceAdapter);
        stopScan();
    }

    private void startScan() {
        if ((mBTAdapter != null) && (!mIsScanning)) {
            mBTAdapter.startLeScan(this);
            mIsScanning = true;
            setProgressBarIndeterminateVisibility(true);
            invalidateOptionsMenu();

        }
    }

    private void stopScan() {
        if (mBTAdapter != null) {
            mBTAdapter.stopLeScan(this);
        }
        mIsScanning = false;
        setProgressBarIndeterminateVisibility(false);
        invalidateOptionsMenu();
    }

}
