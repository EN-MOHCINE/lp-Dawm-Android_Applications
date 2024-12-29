package com.example.firstapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CircularProgressBar extends View {

    private Paint backgroundPaint;
    private Paint progressPaint;
    private Paint textPaint;

    private float progress = 0;
    private String progressText = "0%";
    private int maxProgress = 100;

    public CircularProgressBar(Context context) {
        super(context);
        init();
    }

    public CircularProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.parseColor("#CCCCCC"));
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(20);
        backgroundPaint.setAntiAlias(true);

        progressPaint = new Paint();
        progressPaint.setColor(Color.parseColor("#FF0000"));
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(20);
        progressPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(50);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Calculate the radius and center
        int width = getWidth();
        int height = getHeight();
        int min = Math.min(width, height);
        int radius = min / 2 - 20;

        // Draw the background circle
        canvas.drawCircle(width / 2, height / 2, radius, backgroundPaint);

        // Draw the progress circle
        float sweepAngle = (360 * progress) / maxProgress;
        RectF rectF = new RectF(width / 2 - radius, height / 2 - radius, width / 2 + radius, height / 2 + radius);
        canvas.drawArc(rectF, -90, sweepAngle, false, progressPaint);

        // Draw the progress text in the center
        canvas.drawText(progressText, width / 2, height / 2 + 20, textPaint);
    }

    // Update the progress
    public void setProgress(float progress) {
        this.progress = progress;
        this.progressText = String.format("%.0f%%", progress);
        invalidate();  // Redraw the view
    }

    // Set the maximum progress value
    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }
}
