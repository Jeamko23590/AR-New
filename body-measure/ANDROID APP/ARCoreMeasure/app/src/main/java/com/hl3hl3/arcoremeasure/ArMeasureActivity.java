package com.hl3hl3.arcoremeasure;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.hl3hl3.arcoremeasure.R;

/**
 * Minimal ARCore Measure Activity - temporarily stubbed for basic build test
 * This is a simplified version to get a working APK first
 */
public class ArMeasureActivity extends AppCompatActivity {
    private static final String TAG = "ArMeasureActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Log.d(TAG, "ArMeasureActivity created (minimal version)");
        
        // Show a simple message
        TextView textView = findViewById(R.id.textView);
        if (textView != null) {
            textView.setText("ARCore Measure App - Minimal Build Test");
        }
        
        Toast.makeText(this, "ARCore Measure App - Minimal Build", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "ArMeasureActivity destroyed");
    }
}