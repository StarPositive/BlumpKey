package com.containsnojuice.davidjensen.blumpkey;

import android.inputmethodservice.InputMethodService;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

public class BlumpKeyIME extends InputMethodService {

    //private InputConnection mInputConnection;

    private BlumpKey blumpkey;

    @Override
    public View onCreateInputView() {
        blumpkey = (BlumpKey) getLayoutInflater().inflate(R.layout.blumpkey_layout, null);
        return blumpkey;
    }

}

/*
    @Override
    public View onCreateInputView() {
        blumpkey = (BlumpKey) getLayoutInflater().inflate(R.layout.blumpkey_layout, null);

        blumpkey.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //some code....
                        Touch(v,event);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.performClick();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        blumpkey.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event){
                return Drag(v,event);
            }
        });

        return blumpkey;
    }

    private boolean Touch(View v,MotionEvent event){
        Log.e("Touchin ma Blumpkin", "getX=" + event.getX());
        //if you don't want to call getCurrentInputConnection then you need to get override onBindInput so you can change it when it changes
        getCurrentInputConnection().commitText("x",1);
        return true;
    }

    private boolean Drag(View v,DragEvent event){
        Log.e("Draggin ma Blumpkin", "getX=" + event.getX());
        return true;
    }
*/