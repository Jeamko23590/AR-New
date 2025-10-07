package com.hl3hl3.arcoremeasure;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Custom overlay view for measurement guidance
 */
public class GuidanceOverlay extends View {
    private Paint paint;
    private Paint textPaint;
    private Paint silhouettePaint;
    
    private String guidanceText = "";
    private int countdown = 0;
    private boolean showSilhouette = false;
    private boolean showCountdown = false;
    
    public enum GuidanceType {
        NONE, SCAN_BODY, FRONT_POSE, SIDE_POSE
    }
    
    private GuidanceType currentType = GuidanceType.NONE;
    
    public GuidanceOverlay(Context context) {
        super(context);
        init();
    }
    
    public GuidanceOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4f);
        paint.setColor(Color.GREEN);
        
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(48f);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        
        silhouettePaint = new Paint();
        silhouettePaint.setAntiAlias(true);
        silhouettePaint.setStyle(Paint.Style.FILL);
        silhouettePaint.setColor(Color.argb(100, 0, 255, 0)); // Semi-transparent green
    }
    
    public void setGuidance(GuidanceType type, String text, int countdownSec) {
        this.currentType = type;
        this.guidanceText = text;
        this.countdown = countdownSec;
        this.showSilhouette = (type == GuidanceType.FRONT_POSE || type == GuidanceType.SIDE_POSE);
        this.showCountdown = countdownSec > 0;
        invalidate();
    }
    
    public void hideGuidance() {
        this.currentType = GuidanceType.NONE;
        this.guidanceText = "";
        this.countdown = 0;
        this.showSilhouette = false;
        this.showCountdown = false;
        invalidate();
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        if (currentType == GuidanceType.NONE) return;
        
        int width = getWidth();
        int height = getHeight();
        
        // Draw guidance text
        if (!guidanceText.isEmpty()) {
            canvas.drawText(guidanceText, width / 2f, height / 4f, textPaint);
        }
        
        // Draw countdown
        if (showCountdown && countdown > 0) {
            String countdownText = String.valueOf(countdown);
            textPaint.setTextSize(72f);
            textPaint.setColor(Color.RED);
            canvas.drawText(countdownText, width / 2f, height / 2f, textPaint);
            textPaint.setTextSize(48f);
            textPaint.setColor(Color.WHITE);
        }
        
        // Draw pose silhouettes
        if (showSilhouette) {
            drawPoseSilhouette(canvas, width, height);
        }
    }
    
    private void drawPoseSilhouette(Canvas canvas, int width, int height) {
        int centerX = width / 2;
        int centerY = height / 2;
        int silhouetteWidth = width / 3;
        int silhouetteHeight = height / 2;
        
        if (currentType == GuidanceType.FRONT_POSE) {
            // Front view silhouette - standing figure
            drawFrontSilhouette(canvas, centerX, centerY, silhouetteWidth, silhouetteHeight);
        } else if (currentType == GuidanceType.SIDE_POSE) {
            // Side view silhouette - profile figure
            drawSideSilhouette(canvas, centerX, centerY, silhouetteWidth, silhouetteHeight);
        }
    }
    
    private void drawFrontSilhouette(Canvas canvas, int centerX, int centerY, int width, int height) {
        Path path = new Path();
        
        // Head
        RectF head = new RectF(centerX - width/6, centerY - height/2, 
                              centerX + width/6, centerY - height/3);
        path.addOval(head, Path.Direction.CW);
        
        // Body
        RectF body = new RectF(centerX - width/4, centerY - height/3, 
                              centerX + width/4, centerY + height/6);
        path.addRect(body, Path.Direction.CW);
        
        // Arms (outstretched for measurement)
        RectF leftArm = new RectF(centerX - width/2, centerY - height/4, 
                                 centerX - width/4, centerY);
        path.addRect(leftArm, Path.Direction.CW);
        
        RectF rightArm = new RectF(centerX + width/4, centerY - height/4, 
                                  centerX + width/2, centerY);
        path.addRect(rightArm, Path.Direction.CW);
        
        // Legs
        RectF leftLeg = new RectF(centerX - width/6, centerY + height/6, 
                                centerX - width/12, centerY + height/2);
        path.addRect(leftLeg, Path.Direction.CW);
        
        RectF rightLeg = new RectF(centerX + width/12, centerY + height/6, 
                                 centerX + width/6, centerY + height/2);
        path.addRect(rightLeg, Path.Direction.CW);
        
        canvas.drawPath(path, silhouettePaint);
    }
    
    private void drawSideSilhouette(Canvas canvas, int centerX, int centerY, int width, int height) {
        Path path = new Path();
        
        // Head (profile)
        RectF head = new RectF(centerX - width/8, centerY - height/2, 
                              centerX + width/8, centerY - height/3);
        path.addOval(head, Path.Direction.CW);
        
        // Body (profile)
        RectF body = new RectF(centerX - width/6, centerY - height/3, 
                              centerX + width/6, centerY + height/6);
        path.addRect(body, Path.Direction.CW);
        
        // Arms (one forward, one back)
        RectF frontArm = new RectF(centerX + width/6, centerY - height/4, 
                                  centerX + width/3, centerY);
        path.addRect(frontArm, Path.Direction.CW);
        
        RectF backArm = new RectF(centerX - width/3, centerY - height/4, 
                                centerX - width/6, centerY);
        path.addRect(backArm, Path.Direction.CW);
        
        // Legs (profile)
        RectF frontLeg = new RectF(centerX - width/12, centerY + height/6, 
                                 centerX + width/12, centerY + height/2);
        path.addRect(frontLeg, Path.Direction.CW);
        
        RectF backLeg = new RectF(centerX - width/6, centerY + height/6, 
                                centerX - width/12, centerY + height/2);
        path.addRect(backLeg, Path.Direction.CW);
        
        canvas.drawPath(path, silhouettePaint);
    }
}
