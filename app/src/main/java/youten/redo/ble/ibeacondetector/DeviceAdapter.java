/*
 * Copyright (C) 2013 youten
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package youten.redo.ble.ibeacondetector;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import youten.redo.ble.util.DateUtil;
import youten.redo.ble.util.ScannedDevice;

/**
 * スキャンされたBLEデバイスリストのAdapter
 */
public class DeviceAdapter extends ArrayAdapter<ScannedDevice> {
    private static final String PREFIX_RSSI = "RSSI:";
    private static final String PREFIX_LASTUPDATED = "Last Udpated:";
    private List<ScannedDevice> mList;
    private LayoutInflater mInflater;
    private int mResId;
    public double[] x;
    public DeviceAdapter(Context context, int resId, List<ScannedDevice> objects) {
        super(context, resId, objects);
        mResId = resId;
        mList = objects;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ScannedDevice item = (ScannedDevice) getItem(position);

        if (convertView == null) {
            convertView = mInflater.inflate(mResId, null);
        }
        TextView name = (TextView) convertView.findViewById(R.id.device_name);
        name.setText(item.getDisplayName());
        TextView address = (TextView) convertView.findViewById(R.id.device_address);
        address.setText(item.getDevice().getAddress());
        TextView rssi = (TextView) convertView.findViewById(R.id.device_rssi);
        rssi.setText(PREFIX_RSSI + Integer.toString(item.getRssi()));
        TextView lastupdated = (TextView) convertView.findViewById(R.id.device_lastupdated);
        lastupdated.setText(PREFIX_LASTUPDATED + DateUtil.get_yyyyMMddHHmmssSSS(item.getLastUpdatedMs()));

        TextView ibeaconInfo = (TextView) convertView.findViewById(R.id.device_ibeacon_info);
        Resources res = convertView.getContext().getResources();
        if (item.getIBeacon() != null) {
            ibeaconInfo.setText(res.getString(R.string.label_ibeacon) + "\n" + item.getIBeacon().toString());
            x=item.getPosition(item);
        } else {
            ibeaconInfo.setText(res.getString(R.string.label_not_ibeacon));
        }
        TextView scanRecord = (TextView) convertView.findViewById(R.id.device_scanrecord);
        scanRecord.setText(item.getScanRecordHexString());

        return convertView;
    }

    /**
     * add or update BluetoothDevice List
     * 
     * @param newDevice Scanned Bluetooth Device
     * @param rssi RSSI
     * @param scanRecord advertise data
     * @return summary ex. "iBeacon:3 (Total:10)"
     */
    /*
            藍色
            rssiaverage
            rssimax
            rssimin
            times
            dis\
                        if(blue_times==0){
                blue_rssimin = blue_rssimax = mIBeacon.getRssi();
                blue_rssiaverage = blue_rssimax;
                blue_times = 1;
                blue_dismin = blue_dismax = mIBeacon.getDistance();
            }else {
                int temp = mIBeacon.getRssi();
                double tmpdis = mIBeacon.getDistance();
                if(tmpdis>blue_dismax)
                    blue_dismax = tmpdis;
                else if(tmpdis<blue_dismin)
                    blue_dismin = tmpdis;
                blue_disaverage = ((blue_disaverage * blue_times)+tmpdis)/(blue_times+1);
                if(temp>blue_rssimax)
                    blue_rssimax = temp;
                else if(temp<blue_rssimin)
                    blue_rssimin = temp;
                blue_rssiaverage = ((blue_rssiaverage * blue_times)+temp)/(blue_times+1);
                blue_times++;
            }
         */
    public static double blue_rssiaverage = -1;
    public static double blue_rssimax = -1;
    public static double blue_rssimin = -1;
    public static double blue_times = 0;
    public static double blue_disaverage = -1;
    public static double blue_dismax = -1;
    public static double blue_dismin = -1;
    public static double blue_dis = 0;
    public static double green_rssiaverage = -1;
    public static double green_rssimax = -1;
    public static double green_rssimin = -1;
    public static double green_times = 0;
    public static double green_disaverage = -1;
    public static double green_dismax = -1;
    public static double green_dismin = -1;
    public static double green_dis = 0;
    public static double purple_rssiaverage = -1;
    public static double purple_rssimax = -1;
    public static double purple_rssimin = -1;
    public static double purple_times = 0;
    public static double purple_disaverage = -1;
    public static double purple_dismax = -1;
    public static double purple_dismin = -1;
    public static double purple_dis = 0;
    public static boolean clc = false ;
    public String update(BluetoothDevice newDevice, int rssi, byte[] scanRecord) {
        if ((newDevice == null) || (newDevice.getAddress() == null)) {
            return "";
        }
        long now = System.currentTimeMillis();

        boolean contains = false;
        for (ScannedDevice device : mList) {
            if (newDevice.getAddress().equals(device.getDevice().getAddress())) {
                contains = true;
                // update
                device.setRssi(rssi);
                device.setLastUpdatedMs(now);
                device.setScanRecord(scanRecord);
                if(device.getDevice().getAddress().equals("E3:7A:AA:95:09:0A")) {
                    clc = true;
                    blue_disaverage = device.blue_disaverage;
                    blue_dismax = device.blue_dismax;
                    blue_dismin = device.blue_dismin;
                    blue_times = device.blue_times;
                    blue_rssiaverage = device.blue_rssiaverage;
                    blue_rssimin = device.blue_rssimin;
                    blue_rssimax = device.blue_rssimax;
                    blue_dis=device.blue_dis;
                }else if(device.getDevice().getAddress().equals("E6:18:AB:E5:61:70")){
                    clc = true;
                    green_disaverage = device.green_disaverage;
                    green_dismax = device.green_dismax;
                    green_dismin = device.green_dismin;
                    green_times = device.green_times;
                    green_rssiaverage = device.green_rssiaverage;
                    green_rssimin = device.green_rssimin;
                    green_rssimax = device.green_rssimax;
                    green_dis=device.green_dis;
                }else if(device.getDevice().getAddress().equals("DE:A7:2D:53:BA:E8")){
                    clc = true;
                    purple_disaverage = device.purple_disaverage;
                    purple_dismax = device.purple_dismax;
                    purple_dismin = device.purple_dismin;
                    purple_times = device.purple_times;
                    purple_rssiaverage = device.purple_rssiaverage;
                    purple_rssimin = device.purple_rssimin;
                    purple_rssimax = device.purple_rssimax;
                    purple_dis=device.purple_dis;
                }
                break;
            }
        }
        if (!contains) {
            // add new BluetoothDevice
            mList.add(new ScannedDevice(newDevice, rssi, scanRecord, now));
        }

        // sort by RSSI
        Collections.sort(mList, new Comparator<ScannedDevice>() {
            @Override
            public int compare(ScannedDevice lhs, ScannedDevice rhs) {
                if (lhs.getRssi() == 0) {
                    return 1;
                } else if (rhs.getRssi() == 0) {
                    return -1;
                }
                if (lhs.getRssi() > rhs.getRssi()) {
                    return -1;
                } else if (lhs.getRssi() < rhs.getRssi()) {
                    return 1;
                }
                return 0;
            }
        });

        notifyDataSetChanged();

        // create summary
        int totalCount = 0;
        int iBeaconCount = 0;
        if (mList != null) {
            totalCount = mList.size();
            for (ScannedDevice device : mList) {
                if (device.getIBeacon() != null) {
                    iBeaconCount++;
                }
            }
        }
        String summary="not ready";
        if(x!=null) {
            summary = //"iBeacon:" + Integer.toString(iBeaconCount) +
                    //" (Total:" + Integer.toString(totalCount) + ")"+
                            "x="+x[0]+"y="+x[1];
            fun();
        }
        return summary;
    }
    private static void fun()
    {
        Runnable rn = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                ScannedDevice.postData2();
            }
        });
        rn.run();
    }
//
//    Thread fun = new Thread(new Runnable() {
//
//        @Override
//        public void run() {
//            try  {
//                //Your code goes here
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }).start();

    //fun.run();
    public List<ScannedDevice> getList() {
        return mList;
    }
}
