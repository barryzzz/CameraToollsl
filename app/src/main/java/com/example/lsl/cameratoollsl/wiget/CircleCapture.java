package com.example.lsl.cameratoollsl.wiget;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by lsl on 17-10-15.
 */

public class CircleCapture implements Capture {
    private int cx = 0;
    private int cy = 0;
    private int radius = 100;

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawCircle(cx, cy, radius, paint);
    }

    public int getCx() {
        return cx;
    }

    public void setCxCy(int cx, int cy, int radius) {
        this.cx = cx;
        this.cy = cy;
        this.radius = radius;
    }

    public int getCy() {
        return cy;
    }


    public int getRadius() {
        return radius;
    }

}
