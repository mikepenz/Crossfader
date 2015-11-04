package com.mikepenz.crossfader;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.mikepenz.crossfader.view.ICrossFadeSlidingPaneLayout;

/**
 * Created by mikepenz on 15.07.15.
 */
public class Crossfader<T extends SlidingPaneLayout & ICrossFadeSlidingPaneLayout> {
    /**
     * BUNDLE param to store the selection
     */
    protected static final String BUNDLE_CROSS_FADED = "bundle_cross_faded";

    private T mCrossFadeSlidingPaneLayout;

    public Crossfader() {
    }

    private int mBaseLayout = R.layout.crossfader_base;

    /**
     * defines the base layout to be used for this crossfader
     * look at the sample definition of the crossfader_base
     *
     * @param baseLayout
     * @return
     */
    public Crossfader withBaseLayout(@LayoutRes int baseLayout) {
        this.mBaseLayout = baseLayout;
        return this;
    }

    private View mContent = null;

    /**
     * define the content which is shown on the right of the crossfader
     *
     * @param content
     * @return
     */
    public Crossfader withContent(View content) {
        this.mContent = content;
        return this;
    }

    private View mFirst = null;
    private int mFirstWidth = -1;

    /**
     * define the default (first) view of the crossfader
     *
     * @param first
     * @param width
     * @return
     */
    public Crossfader withFirst(View first, int width) {
        this.mFirst = first;
        this.mFirstWidth = width;
        return this;
    }

    private View mSecond = null;
    private int mSecondWidth = -1;

    /**
     * define the slided (second) view of the crossfader
     *
     * @param first
     * @param width
     * @return
     */
    public Crossfader withSecond(View first, int width) {
        this.mSecond = first;
        this.mSecondWidth = width;
        return this;
    }

    /**
     * define the default view and the slided view of the crossfader
     *
     * @param first
     * @param firstWidth
     * @param second
     * @param secondWidth
     * @return
     */
    public Crossfader withStructure(View first, int firstWidth, View second, int secondWidth) {
        withFirst(first, firstWidth);
        withSecond(second, secondWidth);
        return this;
    }

    // savedInstance to restore state
    protected Bundle mSavedInstance;

    /**
     * Set the Bundle (savedInstance) which is passed by the activity.
     * No need to null-check everything is handled automatically
     *
     * @param savedInstance
     * @return
     */
    public Crossfader withSavedInstance(Bundle savedInstance) {
        this.mSavedInstance = savedInstance;
        return this;
    }

    // enable the panelSlide
    protected boolean mCanSlide = true;

    /**
     * Allow the panel to slide
     *
     * @param canSlide
     * @return
     */
    public Crossfader withCanSlide(boolean canSlide) {
        this.mCanSlide = canSlide;
        if (mCrossFadeSlidingPaneLayout != null) {
            mCrossFadeSlidingPaneLayout.setCanSlide(mCanSlide);
        }
        return this;
    }

    //a panelSlideListener
    protected SlidingPaneLayout.PanelSlideListener mPanelSlideListener;

    /**
     * set a PanelSlideListener used with the CrossFadeSlidingPaneLayout
     *
     * @param panelSlideListener
     * @return
     */
    public Crossfader withPanelSlideListener(SlidingPaneLayout.PanelSlideListener panelSlideListener) {
        this.mPanelSlideListener = panelSlideListener;
        if (mCrossFadeSlidingPaneLayout != null) {
            mCrossFadeSlidingPaneLayout.setPanelSlideListener(mPanelSlideListener);
        }
        return this;
    }

    // if enabled we use a PanelSlideListener to resize the content panel instead of moving it out
    protected boolean mResizeContentPanel = false;

    /**
     * if enabled we use a PanelSlideListener to resize the content panel instead of moving it out
     *
     * @param resizeContentPanel
     * @return
     */
    public Crossfader withResizeContentPanel(boolean resizeContentPanel) {
        this.mResizeContentPanel = resizeContentPanel;
        enableResizeContentPanel(mResizeContentPanel);
        return this;
    }


    /**
     * a small helper class to enable resizing of the content panel / or keep the default behavior
     */
    private void enableResizeContentPanel(boolean enable) {
        if (enable) {
            //activate the resizeFunction
            DisplayMetrics displaymetrics = getContent().getContext().getResources().getDisplayMetrics();
            final int screenWidth = displaymetrics.widthPixels;

            ViewGroup.LayoutParams lp = getContent().getLayoutParams();
            lp.width = screenWidth - getSecondWidth();
            getContent().setLayoutParams(lp);

            if (mCrossFadeSlidingPaneLayout != null) {
                mCrossFadeSlidingPaneLayout.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
                    @Override
                    public void onPanelSlide(View panel, float slideOffset) {
                        ViewGroup.LayoutParams lp = getContent().getLayoutParams();
                        lp.width = (int) (screenWidth - getSecondWidth() - ((getFirstWidth() - getSecondWidth()) * slideOffset));
                        getContent().setLayoutParams(lp);

                        if (mPanelSlideListener != null) {
                            mPanelSlideListener.onPanelSlide(panel, slideOffset);
                        }
                    }

                    @Override
                    public void onPanelOpened(View panel) {
                        if (mPanelSlideListener != null) {
                            mPanelSlideListener.onPanelOpened(panel);
                        }
                    }

                    @Override
                    public void onPanelClosed(View panel) {
                        if (mPanelSlideListener != null) {
                            mPanelSlideListener.onPanelClosed(panel);
                        }
                    }
                });
            }
        } else {
            //reset the resizeFunction
            ViewGroup.LayoutParams lp = getContent().getLayoutParams();
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            getContent().setLayoutParams(lp);

            if (mCrossFadeSlidingPaneLayout != null) {
                mCrossFadeSlidingPaneLayout.setPanelSlideListener(mPanelSlideListener);
            }
        }
    }

    public T getCrossFadeSlidingPaneLayout() {
        return mCrossFadeSlidingPaneLayout;
    }

    /**
     * @return the content view
     */
    public View getContent() {
        return mContent;
    }

    /**
     * @return the first (default) view
     */
    public View getFirst() {
        return mFirst;
    }

    /**
     * @return the width of the first (default) view
     */
    public int getFirstWidth() {
        return mFirstWidth;
    }

    /**
     * @return the second (slided) view
     */
    public View getSecond() {
        return mSecond;
    }

    /**
     * @return the width of the second (slided) view
     */
    public int getSecondWidth() {
        return mSecondWidth;
    }


    /**
     * builds the crossfader and it's content views
     * will define all properties and define and add the layouts
     *
     * @return
     */
    public Crossfader build() {
        if (mFirstWidth < mSecondWidth) {
            throw new RuntimeException("the first layout has to be the layout with the greater width");
        }

        //get the layout which should be replaced by the CrossFadeSlidingPaneLayout
        ViewGroup container = ((ViewGroup) mContent.getParent());

        //remove the content from it's parent
        container.removeView(mContent);

        //create the cross fader container
        mCrossFadeSlidingPaneLayout = (T) LayoutInflater.from(mContent.getContext()).inflate(mBaseLayout, container, false);
        container.addView(mCrossFadeSlidingPaneLayout);

        //find the container layouts
        FrameLayout mCrossFadePanel = (FrameLayout) mCrossFadeSlidingPaneLayout.findViewById(R.id.panel);
        LinearLayout mCrossFadeFirst = (LinearLayout) mCrossFadeSlidingPaneLayout.findViewById(R.id.first);
        LinearLayout mCrossFadeSecond = (LinearLayout) mCrossFadeSlidingPaneLayout.findViewById(R.id.second);
        LinearLayout mCrossFadeContainer = (LinearLayout) mCrossFadeSlidingPaneLayout.findViewById(R.id.content);

        //define the widths
        setWidth(mCrossFadePanel, mFirstWidth);
        setWidth(mCrossFadeFirst, mFirstWidth);
        setWidth(mCrossFadeSecond, mSecondWidth);
        setLeftMargin(mCrossFadeContainer, mSecondWidth);

        //add content to the panel
        mCrossFadeFirst.addView(mFirst, mFirstWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        mCrossFadeSecond.addView(mSecond, mSecondWidth, ViewGroup.LayoutParams.MATCH_PARENT);

        //add back main content
        mCrossFadeContainer.addView(mContent, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        // try to restore all saved values again
        boolean cross_faded = false;
        if (mSavedInstance != null) {
            cross_faded = mSavedInstance.getBoolean(BUNDLE_CROSS_FADED, false);
        }

        if (cross_faded) {
            mCrossFadeSlidingPaneLayout.setOffset(1);
        } else {
            mCrossFadeSlidingPaneLayout.setOffset(0);
        }

        //set the PanelSlideListener for the CrossFadeSlidingPaneLayout
        mCrossFadeSlidingPaneLayout.setPanelSlideListener(mPanelSlideListener);

        //set the ability to slide
        mCrossFadeSlidingPaneLayout.setCanSlide(mCanSlide);

        //define that we don't want a slider color
        mCrossFadeSlidingPaneLayout.setSliderFadeColor(Color.TRANSPARENT);

        //enable / disable the resize functionality
        enableResizeContentPanel(mResizeContentPanel);

        return this;
    }

    /**
     * returns if the crossfader is currently opened (the second view is shown)
     *
     * @return
     */
    public boolean isCrossFaded() {
        return mCrossFadeSlidingPaneLayout.isOpen();
    }

    /**
     * crossfade the current crossfader (toggle between first and second view)
     */
    public void crossFade() {
        if (mCrossFadeSlidingPaneLayout.isOpen()) {
            mCrossFadeSlidingPaneLayout.closePane();
        } else {
            mCrossFadeSlidingPaneLayout.openPane();
        }
    }

    /**
     * add the values to the bundle for saveInstanceState
     *
     * @param savedInstanceState
     * @return
     */
    public Bundle saveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.putBoolean(BUNDLE_CROSS_FADED, mCrossFadeSlidingPaneLayout.isOpen());
        }
        return savedInstanceState;
    }

    /**
     * define the width of the given view
     *
     * @param view
     * @param width
     */
    protected void setWidth(View view, int width) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.width = width;
        view.setLayoutParams(lp);
    }

    /**
     * define the left margin of the given view
     *
     * @param view
     * @param leftMargin
     */
    protected void setLeftMargin(View view, int leftMargin) {
        SlidingPaneLayout.LayoutParams lp = (SlidingPaneLayout.LayoutParams) view.getLayoutParams();
        lp.leftMargin = leftMargin;
        lp.rightMargin = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            lp.setMarginStart(leftMargin);
            lp.setMarginEnd(0);
        }
        view.setLayoutParams(lp);
    }
}
