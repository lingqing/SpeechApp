package com.example.liang.speechapp.ui;

import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

/**
 * Created by liang on 2017/6/26.
 */

public class SmoothLinearLayoutManager extends LinearLayoutManager {

    private Context mContext;
    private static final float MILLISECONDS_PER_INCH = 1000f;

    public SmoothLinearLayoutManager(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {

        LinearSmoothScroller smoothScroller = new LinearSmoothScroller(mContext){

            @Nullable
            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return SmoothLinearLayoutManager.this.computeScrollVectorForPosition(targetPosition);
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
            }
        };
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }
}
