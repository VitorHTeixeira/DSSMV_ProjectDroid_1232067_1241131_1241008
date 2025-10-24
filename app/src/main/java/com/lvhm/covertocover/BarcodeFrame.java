package com.lvhm.covertocover;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

public class BarcodeFrame extends View {
    private final Paint paint = new Paint();
    private Rect barcode_rect = new Rect();

    public BarcodeFrame(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(Color.parseColor("#FDB515"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);
    }

    public void updateBarcode(Rect new_barcode) {
        barcode_rect = new_barcode;
        invalidate();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (barcode_rect != null) {
            canvas.drawRect(barcode_rect, paint);
        }
    }
}
