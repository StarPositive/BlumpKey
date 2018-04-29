package com.containsnojuice.davidjensen.blumpkey;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;


public class BlumpKey extends RelativeLayout implements OnTouchListener {

    DisplayMetrics metrics;

    private BlumpKeyScopeView bkScopeView;

    //scope is scaled down so divide slop by that
    private static float DRAGSLOP = 10.0f / 2.0f;//distance required to recognize
    private float DRAGLIMIT = 0.0f;
    private static float EDGEZOOMIN = 5.0f;//the additional amount of zoom the edge will have (1.0f will give double magnification at edge)
    private int viewHeight = 0;

    private float[] touchPoint = new float[2];
    private float[] downPoint = new float[2];
    private float[] dragLine = new float[2];
    private float dragDistance = 0;

    private boolean dragging = false;
    private boolean touchDown = false;

    //float[] values = new float[9];

    // Matrices used to move and zoom image.
    private Matrix matrix = new Matrix();
    private Matrix matrixInverse = new Matrix();
    private Matrix savedMatrix = new Matrix();

    public BlumpKey(Context context) {
        super(context);
        init(null, 0);
    }

    public BlumpKey(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public BlumpKey(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    @Override
    protected void onCreateContextMenu(ContextMenu menu) {
        super.onCreateContextMenu(menu);
    }

    @Override
    protected void onFinishInflate() {

        super.onFinishInflate();
    }

    private void init(AttributeSet attrs, int defStyle) {

        viewHeight = (int) getResources().getDimension(R.dimen.blumpkey_keyboard_height);
        DRAGLIMIT = viewHeight / 4.0f;//minus the distance from edge of scope to the max distance the touch is allowed to go

        metrics = Resources.getSystem().getDisplayMetrics();

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return true;//do not pass to child views
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionIndex = event.getActionIndex();
        int action = event.getActionMasked();
        int pointerId = event.getPointerId(0);//first touch

        touchPoint[0] = event.getX();
        touchPoint[1] = event.getY();

        int pointerIndex = event.getActionIndex();
        int maskedAction = event.getActionMasked();

        matrix.reset();//matrix always reset
        bkScopeView.setAlpha(1.0f);

        switch (maskedAction) {

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                touchDown = true;
                downPoint[0] = touchPoint[0];
                downPoint[1] = touchPoint[1];
                break;
            }
            case MotionEvent.ACTION_MOVE: { // a pointer was moved
                dragging = true;
                dragLine[0] = touchPoint[0] - downPoint[0];
                dragLine[1] = touchPoint[1] - downPoint[1];
                dragDistance = distance(dragLine[0], dragLine[1]);

                if (dragDistance >= DRAGSLOP) {

                    //clamp it so it doesn't get crazy
                    //Log.d("blumpkey","drag distance: " + dragDistance + " DRAGLIMIT: " + DRAGLIMIT);
                    if(dragDistance > DRAGLIMIT){
                        dragLine[0] *= DRAGLIMIT / dragDistance;
                        dragLine[1] *= DRAGLIMIT / dragDistance;
                        dragDistance = DRAGLIMIT;
                    }

                    float dragX = -dragLine[0] * 2.0f;
                    float dragY = -dragLine[1] * 2.0f;

                    float scaleValue = 1.0f + EDGEZOOMIN * ((float)dragDistance / (float)viewHeight);


                    matrix.preScale(scaleValue, scaleValue, viewHeight/4.0f, viewHeight/4.0f);
                    //matrix.postTranslate(dragX, dragY);

                    //will also trigger a redraw
                    bkScopeView.setTransform(matrix);
                    bkScopeView.setAlpha(Math.max(0.0f,1.5f - (scaleValue / 2.0f)));

                }

                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {
                touchDown = false;
                dragging = false;

                //reset and redraw the scope
                bkScopeView.setTransform(matrix);

                break;
            }
        }

        //boolean handled = super.onTouchEvent(event);
        return true;
    }


    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onCapturedPointerEvent(MotionEvent motionEvent) {
        float verticalOffset = motionEvent.getY();
        return false;
    }

//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        int childCount = getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            View child = getChildAt(i);
//            if (child.getVisibility() != GONE) {
//                child.layout(left, top, left + child.getMeasuredWidth(), top + child.getMeasuredHeight());
//            }
//        }
//    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                measureChild(child, widthMeasureSpec, viewHeight);
            }
        }
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), viewHeight);

        //I can't find the event for knowing when a ViewGroup has all its Views inflated and ready
        //  so this might work.  Or it might not.
        bkScopeView = (BlumpKeyScopeView) this.findViewById(R.id.blumpkey_scope_view);

        Matrix newMatrix = new Matrix();
//        newMatrix.postScale(4.0f, 4.0f,
//                getResources().getDimension(R.dimen.blumpkey_keyboard_height) / 2,
//                getResources().getDimension(R.dimen.blumpkey_keyboard_height) / 2
//        );
        bkScopeView.setTransform(newMatrix);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    private float[] scaledPointsToScreenPoints(float[] a) {
        matrix.mapPoints(a);
        return a;
    }

    private float[] screenPointsToScaledPoints(float[] a) {
        matrixInverse.mapPoints(a);
        return a;
    }

    private float distance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private float distance(float width, float height) {
        return (float) Math.sqrt(width * width + height * height);
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

}
