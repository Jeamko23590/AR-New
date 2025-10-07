package com.hl3hl3.arcoremeasure;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.PoseLandmark;
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions;

import java.util.List;

/**
 * ML Kit Pose Detection wrapper for human body detection
 */
public class PoseDetectionHelper {
    private static final String TAG = "PoseDetectionHelper";
    
    private PoseDetector poseDetector;
    private boolean isDetecting = false;
    
    public interface PoseDetectionCallback {
        void onPoseDetected(Pose pose, boolean isValidPose);
        void onDetectionFailed(String error);
    }
    
    public PoseDetectionHelper() {
        // Use accurate pose detection for better body detection
        AccuratePoseDetectorOptions options = new AccuratePoseDetectorOptions.Builder()
                .setDetectorMode(AccuratePoseDetectorOptions.STREAM_MODE)
                .build();
        
        poseDetector = PoseDetection.getClient(options);
    }
    
    public void detectPose(Bitmap bitmap, PoseDetectionCallback callback) {
        if (isDetecting) return;
        
        isDetecting = true;
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        
        poseDetector.process(image)
                .addOnSuccessListener(pose -> {
                    isDetecting = false;
                    boolean isValid = validatePose(pose);
                    callback.onPoseDetected(pose, isValid);
                })
                .addOnFailureListener(e -> {
                    isDetecting = false;
                    Log.e(TAG, "Pose detection failed", e);
                    callback.onDetectionFailed(e.getMessage());
                });
    }
    
    private boolean validatePose(Pose pose) {
        // Check for key body landmarks to ensure a person is detected
        List<PoseLandmark> landmarks = pose.getAllPoseLandmarks();
        
        // Require at least these key points for a valid human pose
        boolean hasHead = pose.getPoseLandmark(PoseLandmark.NOSE) != null;
        boolean hasShoulders = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER) != null && 
                              pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER) != null;
        boolean hasHips = pose.getPoseLandmark(PoseLandmark.LEFT_HIP) != null && 
                         pose.getPoseLandmark(PoseLandmark.RIGHT_HIP) != null;
        
        // Check pose confidence
        float minConfidence = 0.3f;
        boolean confidentPose = true;
        for (PoseLandmark landmark : landmarks) {
            if (landmark.getInFrameLikelihood() <= minConfidence) {
                confidentPose = false;
                break;
            }
        }
        
        return hasHead && hasShoulders && hasHips && confidentPose;
    }
    
    public void cleanup() {
        if (poseDetector != null) {
            poseDetector.close();
        }
    }
    
    // Helper method to draw pose landmarks on bitmap for debugging
    public static Bitmap drawPoseOnBitmap(Bitmap originalBitmap, Pose pose) {
        Bitmap result = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(result);
        
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(4f);
        paint.setStyle(Paint.Style.STROKE);
        
        // Draw pose landmarks
        for (PoseLandmark landmark : pose.getAllPoseLandmarks()) {
            PointF point = landmark.getPosition();
            canvas.drawCircle(point.x, point.y, 8f, paint);
        }
        
        return result;
    }
}
