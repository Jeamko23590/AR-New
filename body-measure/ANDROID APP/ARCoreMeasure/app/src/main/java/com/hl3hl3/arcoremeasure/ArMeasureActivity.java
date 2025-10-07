package com.hl3hl3.arcoremeasure;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Ultra-minimal Activity for basic build test
 */
public class ArMeasureActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Create a simple TextView programmatically
        TextView textView = new TextView(this);
        textView.setText("ARCore Measure App - Ultra Minimal Build");
        textView.setTextSize(18);
        textView.setPadding(50, 50, 50, 50);
        
        setContentView(textView);
    }
}