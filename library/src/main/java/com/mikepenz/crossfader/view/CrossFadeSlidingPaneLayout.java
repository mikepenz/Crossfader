package com.mikepenz.crossfader.view;

import android.content.Context;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * SlidingPaneLayout that is partially visible, with cross fade.
 * https://github.com/chiuki/sliding-pane-layout
 */
public class CrossFadeSlidingPaneLayout extends SlidingPaneLayout implements ICrossFadeSlidingPaneLayout {
    private View partialView = null;
    private View fullView = null;

    // helper flag pre honeycomb used in visibility and click response handling
    // helps avoid unnecessary layouts
    private boolean wasOpened = false;
    private boolean mCanSlide = true;

    public CrossFadeSlidingPaneLayout(Context context) {
        super(context);
    }

    public CrossFadeSlidingPaneLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CrossFadeSlidingPaneLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() < 1) {
            return;
        }

        View panel = getChildAt(0);
        if (!(panel instanceof ViewGroup)) {
            return;
        }

        ViewGroup viewGroup = (ViewGroup) panel;
        if (viewGroup.getChildCount() != 2) {
            return;
        }
        fullView = viewGroup.getChildAt(0);
        partialView = viewGroup.getChildAt(1);

        super.setPanelSlideListener(crossFadeListener);

        //make sure we prevent click on the fullView when we create the crossfader
        //just do this if we are not opened
        if (!isOpen()) {
            enableDisableView(fullView, false);
        }
        fullView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (!isOpen()) {
                    enableDisableView(v, false);
                }
            }
        });
    }

    @Override
    public void setPanelSlideListener(final PanelSlideListener listener) {
        if (listener == null) {
            super.setPanelSlideListener(crossFadeListener);
            return;
        }

        super.setPanelSlideListener(new PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                crossFadeListener.onPanelSlide(panel, slideOffset);
                listener.onPanelSlide(panel, slideOffset);
            }

            @Override
            public void onPanelOpened(View panel) {
                listener.onPanelOpened(panel);
            }

            @Override
            public void onPanelClosed(View panel) {
                listener.onPanelClosed(panel);
            }
        });
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (partialView != null) {
            partialView.setVisibility(isOpen() ? View.GONE : VISIBLE);
        }
    }

    private SimplePanelSlideListener crossFadeListener = new SimplePanelSlideListener() {
        @Override
        public void onPanelSlide(View panel, float slideOffset) {
            super.onPanelSlide(panel, slideOffset);
            if (partialView == null || fullView == null) {
                return;
            }

            setOffset(slideOffset);
        }
    };

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mCanSlide && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mCanSlide && super.onTouchEvent(ev);
    }

    public void setCanSlide(boolean canSlide) {
        this.mCanSlide = canSlide;
    }

    public void setOffset(float slideOffset) {
        partialView.setAlpha(1 - slideOffset);
        fullView.setAlpha(slideOffset);
        partialView.setVisibility(isOpen() ? View.GONE : VISIBLE);

        //if the fullView is hidden we prevent the click on all its views and subviews
        //otherwhise enable it again
        if (slideOffset == 0 && fullView.isEnabled() || slideOffset != 0 && !fullView.isEnabled()) {
            enableDisableView(fullView, slideOffset != 0);
        }
    }

    /**
     * helper method to disable a view and all its subviews
     *
     * @param view
     * @param enabled
     */
    private void enableDisableView(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;

            for (int idx = 0; idx < group.getChildCount(); idx++) {
                enableDisableView(group.getChildAt(idx), enabled);
            }
        }
    }
}