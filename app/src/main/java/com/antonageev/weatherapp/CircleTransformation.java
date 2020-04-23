package com.antonageev.weatherapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import com.squareup.picasso.Transformation;

public class CircleTransformation implements Transformation {
    @Override
    public Bitmap transform(Bitmap source) {
        Path path = new Path();
        path.addCircle(source.getWidth() / 2, source.getHeight() / 2, source.getWidth() / 2, Path.Direction.CCW);
        Bitmap resultBitMap = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(resultBitMap);

        canvas.clipPath(path);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawBitmap(source, 0, 0, paint);
        source.recycle();
        return resultBitMap;

    }

    @Override
    public String key() {
        return "circle";
    }
}
