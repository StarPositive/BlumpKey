package com.containsnojuice.davidjensen.blumpkey;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;


public class BlumpKeyScopeView extends View {
    private String mExampleString;
    private int mExampleColor = Color.RED;
    private float mExampleDimension = 0;
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

    private float[] mOnTouchEventWorkingArray = new float[2];
    float[] values = new float[9];

    // Matrices used to move and zoom image.
    private Matrix matrix = new Matrix();
    private Matrix matrixInverse = new Matrix();
    private Matrix savedMatrix = new Matrix();

    public BlumpKeyScopeView(Context context) {
        super(context);
        init(null, 0);
    }

    public BlumpKeyScopeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public BlumpKeyScopeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.BlumpKeyScopeView, defStyle, 0);

        mExampleString = a.getString(R.styleable.BlumpKeyScopeView_exampleString);
        mExampleColor = a.getColor(R.styleable.BlumpKeyScopeView_exampleColor, mExampleColor);

        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(R.styleable.BlumpKeyScopeView_exampleDimension, mExampleDimension);

        if (a.hasValue(R.styleable.BlumpKeyScopeView_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(R.styleable.BlumpKeyScopeView_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    public void setTransform(Matrix newTransform){
        matrix.set(newTransform);
        this.invalidate();
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);
        mTextWidth = mTextPaint.measureText(mExampleString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        matrix.getValues(values);
        canvas.save();
        canvas.scale(values[Matrix.MSCALE_X], values[Matrix.MSCALE_Y]);
        canvas.translate(values[Matrix.MTRANS_X], values[Matrix.MTRANS_Y]);

        {

            // Draw the example drawable on top of the text.
            if (mExampleDrawable != null) {
                mExampleDrawable.setBounds(0, 0, width, height);
                mExampleDrawable.draw(canvas);
            }

            // Draw the text.
//            canvas.drawText(mExampleString,
//                    paddingLeft + (contentWidth - mTextWidth) / 2,
//                    paddingTop + (contentHeight + mTextHeight) / 2,
//                    mTextPaint);
        }

        canvas.restore();

    }

    public String getExampleString() {
        return mExampleString;
    }

    public void setExampleString(String exampleString) {
        mExampleString = exampleString;
        invalidateTextPaintAndMeasurements();
    }

    public int getExampleColor() {
        return mExampleColor;
    }

    public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }

    public float getExampleDimension() {
        return mExampleDimension;
    }

    public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }

    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }

    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }

}
