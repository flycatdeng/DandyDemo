package com.dandy.module.touchzoom;

import android.view.MotionEvent;

public class TouchZoomAider {
    private static final int DEFULT_THRESHOLD = 20;
    private int mThreshold = DEFULT_THRESHOLD;
    private int mTouchPointCount = 0;
    private float mOldTwoPointDownDistance;

    /**
     * <pre>
     * 设置阈值，这个阈值表示你双指滑动的距离（要减去初始位置的值）
     * </pre>
     *
     * @param threshold
     */
    public void setThreshold(int threshold) {
        mThreshold = threshold;
    }

    /**
     * <pre>
     * 处理触碰是否缩放
     * </pre>
     *
     * @param event
     * @return true:有缩放动作，具体动作通过回调{@link #setTouchZoomListener(TouchZoomListener)}返回
     */
    public boolean dealTouchZoom(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mTouchPointCount = 1;
                break;
            case MotionEvent.ACTION_UP:
                mTouchPointCount = 0;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mTouchPointCount -= 1;
                if (mTouchPointCount == 1 && mTouchZoomListener != null) {
                    mTouchZoomListener.zoomEnd();
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mOldTwoPointDownDistance = spacing(event);
                mTouchPointCount += 1;
                if (mTouchPointCount == 2 && mTouchZoomListener != null) {
                    mTouchZoomListener.zoomStart();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (mTouchPointCount >= 2) {
                    float newDist = spacing(event);
                    if (newDist > mOldTwoPointDownDistance + mThreshold) {
                        float scale = newDist / mOldTwoPointDownDistance;// 得到缩放倍数
                        mOldTwoPointDownDistance = newDist;
                        if (mTouchZoomListener != null) {
                            mTouchZoomListener.zoomOut(scale);
                        }
                        return true;
                    }
                    if (newDist < mOldTwoPointDownDistance - mThreshold) {
                        float scale = newDist / mOldTwoPointDownDistance;// 得到缩放倍数
                        mOldTwoPointDownDistance = newDist;
                        if (mTouchZoomListener != null) {
                            mTouchZoomListener.zoomIn(scale);
                        }
                        return true;
                    }
                }
                break;
        }
        return false;
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private TouchZoomListener mTouchZoomListener;

    public void setTouchZoomListener(TouchZoomListener listener) {
        mTouchZoomListener = listener;
    }

    public interface TouchZoomListener {
        /**
         * <pre>
         * 向外滑动，放大手势
         * </pre>
         */
        void zoomOut(float scale);

        void zoomStart();

        void zoomEnd();

        /**
         * <pre>
         * 向里滑动，缩小手势
         * </pre>
         */
        void zoomIn(float scale);
    }

    public static class TouchZoomListenerAdapter implements TouchZoomListener {
        @Override
        public void zoomOut(float scale) {
        }

        @Override
        public void zoomStart() {
        }

        @Override
        public void zoomEnd() {
        }

        @Override
        public void zoomIn(float scale) {
        }
    }
}
