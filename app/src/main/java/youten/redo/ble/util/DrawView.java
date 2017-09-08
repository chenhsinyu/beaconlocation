package youten.redo.ble.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import youten.redo.ble.ibeacondetector.DeviceAdapter;

/**
 * Created by Administrator on 2016/12/2.
 */

public class DrawView extends View {
    private DeviceAdapter mDeviceAdapter;
    //private Canvas mCanvas;
   // private Bitmap bitmap;
    public DrawView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       // mCanvas = new Canvas(bitmap);
        /*
         * 方法 說明 drawRect 繪制矩形 drawCircle 繪制圓形 drawOval 繪制橢圓 drawPath 繪制任意多邊形
         * drawLine 繪制直線 drawPoin 繪制點
         */

        Paint r = new Paint();
        r.setColor(Color.WHITE);
        r.setAlpha(255);
        Rect rect = new Rect(0, 0, 10000, 1500);
        canvas.drawRect(rect, r);

        // 創建畫筆
        Paint p = new Paint();

        p.setColor(Color.parseColor("#2894FF"));//blue
        p.setAntiAlias(true);// 設置畫筆的鋸齒效果。 true是去除，大家一看效果就明白了
        p.setAlpha(255);
        p.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(350, 90, (float) mDeviceAdapter.blue_dis * 240, p);

        p.setColor(Color.parseColor("#02C874"));//green
        p.setAntiAlias(true);// 設置畫筆的鋸齒效果。 true是去除，大家一看效果就明白了
        p.setAlpha(255);
        p.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(50, 610, (float) mDeviceAdapter.green_dis * 230, p);

        p.setColor(Color.parseColor("#B15BFF"));//purple
        p.setAntiAlias(true);// 設置畫筆的鋸齒效果。 true是去除，大家一看效果就明白了
        p.setAlpha(255);
        p.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(650, 610, (float) mDeviceAdapter.purple_dis * 240, p);

//        Paint c = new Paint();
//        c.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//        canvas.drawPaint(c);

    }
}