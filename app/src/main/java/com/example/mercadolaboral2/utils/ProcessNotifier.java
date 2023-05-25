package com.example.mercadolaboral2.utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.TextView;


import com.example.mercadolaboral2.R;

public class ProcessNotifier extends Dialog {

    private final Activity activity;
    private TextView title;
    private TextView processText;
//    private ProgressBar progressBar;
    public static final int INDETERMINATE = 1;
    public static final int PROGRESSIVE = 0;

    public ProcessNotifier(Activity activityRecive) {
        super(activityRecive);
        this.activity = activityRecive;
        setContentView(R.layout.process_notifier);
        this.setCanceledOnTouchOutside(false);
        initialize();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    private void initialize() {
        title = findViewById(R.id.process_Title);
        processText = findViewById(R.id.process_text);
//        progressBar = findViewById(R.id.progressBar);
    }

    public void setTitle(String t) {
        activity.runOnUiThread(() -> title.setText(t));
    }

    public void setText(String t) {
        activity.runOnUiThread(() -> processText.setText(t));
    }

/*    public void setProgressPercent(int prog) {
        progressBar.setProgress(prog);
    }*/

//    public void setMode(int mode) {
/*        if (mode == INDETERMINATE) {
            progressBar.setIndeterminate(true);
        }

        if (mode == PROGRESSIVE) {
            progressBar.setIndeterminate(false);
        }*/
//    }

    public void clear() {
        processText.setText("");
        title.setText("");
//        progressBar.setProgress(0);
//        progressBar.setIndeterminate(false);
    }

    public void inflate() {
        activity.runOnUiThread(this::preInflater);
    }

    public void deInflate() {
        activity.runOnUiThread(this::preDeInflater);
    }

    private void preInflater() {
        this.show();
    }

    private void preDeInflater() {
        this.hide();
    }
}
