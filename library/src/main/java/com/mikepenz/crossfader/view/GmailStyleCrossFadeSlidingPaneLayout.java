package com.mikepenz.crossfader.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.mikepenz.crossfader.util.UIUtils;

/**
 * Created on 05.11.15
 *
 * @author github @androideveloper (Roland Yeghiazaryan)
 * @author github @suren1525 (Suren Khachatryan)
 */
public class GmailStyleCrossFadeSlidingPaneLayout extends CrossFadeSlidingPaneLayout {
    private boolean isEventHandled = false;

    public GmailStyleCrossFadeSlidingPaneLayout(Context context) {
        super(context);
    }

    public GmailStyleCrossFadeSlidingPaneLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GmailStyleCrossFadeSlidingPaneLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isOutOfSecond(ev))
            return true;
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isOutOfSecond(ev))
            return true;
        return super.onTouchEvent(ev);
    }

    private boolean isOutOfSecond(MotionEvent ev) {
        if (MotionEvent.ACTION_UP == ev.getAction() || MotionEvent.ACTION_CANCEL == ev.getAction()) {
            isEventHandled = false;
        }
        LinearLayout mCrossFadeSecond = (LinearLayout) findViewById(com.mikepenz.crossfader.R.id.second);
        if ((!isOpen() && ev.getAction() == MotionEvent.ACTION_DOWN && !UIUtils.isPointInsideView(ev.getRawX(), ev.getRawY(), mCrossFadeSecond)) || isEventHandled) {
            isEventHandled = true;
            return true;

        }
        return false;
    }
}