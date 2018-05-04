package com.containsnojuice.davidjensen.blumpkey;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * Simple Blumpkey class that shows a background and is on either side of the Blumpkey scope
 * This class will eventually be in charge of having some custom buttons and can be switched
 * for people that are left handed or right handed.  That will be in settings, once I program
 * a little settings action.
 */
public class BlumpKeySideView extends View {

    public BlumpKeySideView(Context context) {
        super(context);
        init(null, 0);
    }
    public BlumpKeySideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }
    public BlumpKeySideView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private void init(AttributeSet attrs, int defStyle) { }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), (int)getResources().getDimension(R.dimen.blumpkey_keyboard_height));
    }

}
