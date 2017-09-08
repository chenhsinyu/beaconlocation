package youten.redo.ble.util;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.radiusnetworks.ibeacon.IBeacon;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * LeScanned Bluetooth Device
 */
public class ScannedDevice {
    private static final String UNKNOWN = "Unknown";
    /**
     * BluetoothDevice
     */
    private BluetoothDevice mDevice;
    /**
     * RSSI
     */
    private int mRssi;
    /**
     * Display Name
     */
    private String mDisplayName;
    /**
     * Advertise Scan Record
     */
    private byte[] mScanRecord;
    /**
     * parsed iBeacon Data
     */
    private IBeacon mIBeacon;
    /**
     * last updated (Advertise scanned)
     */
    private long mLastUpdatedMs;
    private static double r1, r2, r3;
    private static double[] x;
    /*
            藍色
            rssiaverage
            rssimax
            rssimin
            times
            dis
         */
    public static double blue_rssiaverage = -1;
    public static double blue_rssimax = -1;
    public static double blue_rssimin = -1;
    public static int blue_times = 0;
    public static double blue_dis = 0;
    public static Queue<Double> blue_buf = new LinkedList<>();
    public static double blue_disaverage = -1;
    public static double blue_dismax = -1;
    public static double blue_dismin = -1;
    public static double green_rssiaverage = -1;
    public static double green_rssimax = -1;
    public static double green_rssimin = -1;
    public static int green_times = 0;
    public static double green_dis = 0;
    public static Queue<Double> green_buf = new LinkedList<>();
    public static double green_disaverage = -1;
    public static double green_dismax = -1;
    public static double green_dismin = -1;
    public static double purple_rssiaverage = -1;
    public static double purple_rssimax = -1;
    public static double purple_rssimin = -1;
    public static double purple_times = 0;
    public static double purple_dis = 0;
    public static Queue<Double> purple_buf = new LinkedList<>();
    public static double purple_disaverage = -1;
    public static double purple_dismax = -1;
    public static double purple_dismin = -1;
    public ScannedDevice(BluetoothDevice device, int rssi, byte[] scanRecord, long now) {
        if (device == null) {
            throw new IllegalArgumentException("BluetoothDevice is null");
        }
        mLastUpdatedMs = now;
        mDevice = device;
        mDisplayName = device.getName();
        if ((mDisplayName == null) || (mDisplayName.length() == 0)) {
            mDisplayName = UNKNOWN;
        }
        mRssi = rssi;
        mScanRecord = scanRecord;
        checkIBeacon();
    }

    private void checkIBeacon() {
        if (mScanRecord != null) {
            mIBeacon = IBeacon.fromScanData(mScanRecord, mRssi);
        }
    }

    public BluetoothDevice getDevice() {
        return mDevice;
    }

    public int getRssi() {
        return mRssi;
    }

    public void setRssi(int rssi) {
        mRssi = rssi;
    }

    public long getLastUpdatedMs() {
        return mLastUpdatedMs;
    }

    public void setLastUpdatedMs(long lastUpdatedMs) {
        mLastUpdatedMs = lastUpdatedMs;
    }

    public String getScanRecordHexString() {
        return ScannedDevice.asHex(mScanRecord);
    }

    public void setScanRecord(byte[] scanRecord) {
        mScanRecord = scanRecord;
        checkIBeacon();
    }

    public IBeacon getIBeacon() {
        return mIBeacon;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public double[] getPosition(ScannedDevice device) {
        //10個值取中間6個平均
        double x1 = 350.0, x2 = 50.0, x3 = 650.0;
        double y1 = 90.0, y2 = 610.0, y3 = 610.0, i, j, Y1, Y2;
        x = new double[2];
        double distance = mIBeacon.getDistance();
        String addr = device.getDevice().getAddress();
        if (addr.equals("E3:7A:AA:95:09:0A")) {//blue
            if(blue_times==0){
                blue_rssimin = blue_rssimax = mIBeacon.getRssi();
                blue_buf.offer(mIBeacon.getDistance());
                blue_rssiaverage = blue_rssimax;
                blue_times = 1;
                blue_dismin = blue_dismax = mIBeacon.getDistance();
            }else {
                double temp = mIBeacon.getDistance();
                blue_buf.offer(temp);
                if(blue_times>=19){
                    blue_buf.poll();
                    double sum = 0;
                    double max1 = -999999;
                    double max2 = -999999;
                    double min1 = 0;
                    double min2 = 0;
                    for(double s : blue_buf) {
                        sum += s;
                        if(s>max1){
                            max1 = s;
                        }else if(s>max2){
                            max2 = s;
                        }else if(s<min1){
                            min1 = s;
                        }else if(s<min2){
                            min2 = s;
                        }
                    }
                    sum -= max1 + max2 +min1+min2;
                    blue_dis = sum/16;
                }
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
             r1 = blue_dis * 220;//藍
             //r1 = blue_disaverage * 220;//藍
        } else if (addr.equals("E6:18:AB:E5:61:70")) {//green
            if(green_times==0){
                green_rssimin = green_rssimax = mIBeacon.getRssi();
                green_buf.offer(mIBeacon.getDistance());
                green_rssiaverage = green_rssimax;
                green_times = 1;
                green_dismin = green_dismax = mIBeacon.getDistance();
            }else {
                double temp = mIBeacon.getDistance();
                green_buf.offer(temp);
                if(green_times>=19){
                    green_buf.poll();
                    double sum = 0;
                    double max1 = -999999;
                    double max2 = -999999;
                    double min1 = 0;
                    double min2 = 0;
                    for(double s : green_buf) {
                        sum += s;
                        if(s>max1){
                            max1 = s;
                        }else if(s>max2){
                            max2 = s;
                        }else if(s<min1){
                            min1 = s;
                        }else if(s<min2){
                            min2 = s;
                        }
                    }
                    sum -= max1 + max2 +min1+min2;
                    green_dis = sum/16;
                }
                double tmpdis = mIBeacon.getDistance();
                if(tmpdis>green_dismax)
                    green_dismax = tmpdis;
                else if(tmpdis<green_dismin)
                    green_dismin = tmpdis;
                green_disaverage = ((green_disaverage * green_times)+tmpdis)/(green_times+1);
                if(temp>green_rssimax)
                    green_rssimax = temp;
                else if(temp<green_rssimin)
                    green_rssimin = temp;
                green_rssiaverage = ((green_rssiaverage * green_times)+temp)/(green_times+1);
                green_times++;
            }
            r2 = green_dis * 210;
            //r2 = green_disaverage * 210;
        } else if (addr.equals("DE:A7:2D:53:BA:E8")) {//purple
            if(purple_times==0){
                purple_rssimin = purple_rssimax = mIBeacon.getRssi();
                purple_buf.offer(mIBeacon.getDistance());
                purple_rssiaverage = purple_rssimax;
                purple_times = 1;
                purple_dismin = purple_dismax = mIBeacon.getDistance();
            }else {
                double temp = mIBeacon.getDistance();
                purple_buf.offer(temp);
                if(purple_times>=19){
                    purple_buf.poll();
                    double sum = 0;
                    double max1 = -999999;
                    double max2 = -999999;
                    double min1 = 0;
                    double min2 = 0;
                    for(double s : purple_buf) {
                        sum += s;
                        if(s>max1){
                            max1 = s;
                        }else if(s>max2){
                            max2 = s;
                        }else if(s<min1){
                            min1 = s;
                        }else if(s<min2){
                            min2 = s;
                        }
                    }
                    sum -= max1 + max2 +min1+min2;
                    purple_dis = sum/16;
                }
                double tmpdis = mIBeacon.getDistance();
                if(tmpdis>purple_dismax)
                    purple_dismax = tmpdis;

                
                else if(tmpdis<purple_dismin)
                    purple_dismin = tmpdis;
                purple_disaverage = ((purple_disaverage * purple_times)+tmpdis)/(purple_times+1);
                if(temp>purple_rssimax)
                    purple_rssimax = temp;
                else if(temp<purple_rssimin)
                    purple_rssimin = temp;
                purple_rssiaverage = ((purple_rssiaverage * purple_times)+temp)/(purple_times+1);
                purple_times++;
            }
            r3 = purple_dis * 220;
            //r3 = purple_disaverage * 220;
        }
//        Log.e("debug"," r1="+r1+" r2="+r2+" r3="+r3);
        //三角演算法
        i = r1 * r1 - r2 * r2 - x1 * x1 - y1 * y1 + x2 * x2 + y2 * y2;
        j = r1 * r1 - r3 * r3 - x1 * x1 - y1 * y1 + x3 * x3 + y3 * y3;
        x[0] = (i * (y3 - y2) - j * (y2 - y1)) / (2 * ((x2 - x1) * (y3 - y1) - (x3 - x1) * (y2 - y1)));
        Y1 = (i - 2 * x[0] * (x2 - x1)) / (2 * (y2 - y1));
        Y2 = (j - 2 * x[0] * (x3 - x1)) / (2 * (y3 - y1));
        x[1] = (Y1 + Y2) / 2;
        int a = (int) (x[0] * 1000), b = (int) (x[1] * 1000);
        x[0] = (double) a / 1000;
        x[1] = (double) b / 1000;
//        JDBC_MySQL test = new JDBC_MySQL("10.21.20.36","beacon","Big5","root","gs3m5bm1");
//        test.dropTable("User");
//        test.CreateTable("CREATE TABLE Beacon(id INTEGER,rssi VARCHAR(20),distance VARCHAR(20),MAC VARCHAR(20))");
//        test.ExecuteIns("Insert into location(location) Values('("+x[0]+","+x[1]+")')");
//        test.ShowTable("location");

        return x;
    }


    public static void postData2() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://163.17.136.253/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CallPhp service = retrofit.create(CallPhp.class);
        String str = x[0] + "," + x[1];
        String B= String.valueOf(blue_dis);
        String G= String.valueOf(green_dis);
        String P= String.valueOf(purple_dis);

        Call<ResponseBody> call = service.getMethod("true", "UTF-8", str,B,G,P);
        //Call<ResponseBody> callb = service.getMethod("true", "UTF-8",B);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("Success", response.raw().toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Failure", t.toString());
            }
        });

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://163.17.136.253/s1410232003/beacon.php");
        try {
            List<NameValuePair> nameValuePairs = new ArrayList();
            nameValuePairs.add(new BasicNameValuePair("location",str));
            nameValuePairs.add(new BasicNameValuePair("blue", B));
            nameValuePairs.add(new BasicNameValuePair("green",G));
            nameValuePairs.add(new BasicNameValuePair("purple",P));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));
//            HttpResponse response = httpclient.execute(httppost);
//            HttpEntity resEntity = response.getEntity();
//            String result = EntityUtils.toString(resEntity);
//            Log.e("HTTP result", result);
//        } catch (ClientProtocolException e) {
//            Log.e("HTTP ERR", e.toString());

        } catch (IOException e) {
            Log.e("HTTP ERR", e.toString());
        }
        catch (Exception e) {
            Log.e("HTTP ERR", e.toString());
        }
    }


    @SuppressLint("DefaultLocale")
    public static String asHex(byte bytes[]) {
        if ((bytes == null) || (bytes.length == 0)) {
            return "";
        }

        // バイト配列の２倍の長さの文字列バッファを生成。
        StringBuffer sb = new StringBuffer(bytes.length * 2);

        // バイト配列の要素数分、処理を繰り返す。
        for (int index = 0; index < bytes.length; index++) {
            // バイト値を自然数に変換。
            int bt = bytes[index] & 0xff;

            // バイト値が0x10以下か判定。
            if (bt < 0x10) {
                // 0x10以下の場合、文字列バッファに0を追加。
                sb.append("0");
            }

            // バイト値を16進数の文字列に変換して、文字列バッファに追加。
            sb.append(Integer.toHexString(bt).toUpperCase());
        }
        /// 16進数の文字列を返す。
        return sb.toString();
    }

//    void post(double[] x) {
//        try {
//            HttpClient client = new DefaultHttpClient();
//            HttpPost post = new HttpPost("http://163.17.136.253/s1410232003/beacon.php");
//            List<NameValuePair> params = new ArrayList<NameValuePair>();
//            params.add(new BasicNameValuePair("location", x[0] + "," + x[1]));
//            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
//            HttpResponse responsePost = client.execute(post);
//            HttpEntity resEntity = responsePost.getEntity();
//            String result = EntityUtils.toString(resEntity);
//            client.getConnectionManager().shutdown();
//        } catch (Exception e) {
//            Log.e("err  ", e.toString());
//        }
//    }
}
