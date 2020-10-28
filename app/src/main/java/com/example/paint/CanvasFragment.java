package com.example.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.GestureDetector;
import android.view.MotionEvent;

import java.util.Random;

public class CanvasFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        GestureListener mGestureListener = new GestureListener();
        GestureDetector mGestureDetector = new GestureDetector(getContext(), mGestureListener);
        mGestureDetector.setIsLongpressEnabled(true);
        mGestureDetector.setOnDoubleTapListener(mGestureListener);

        PaintCanvas paintCanvas = new PaintCanvas(getContext(), null, mGestureDetector);
        mGestureListener.setCanvas(paintCanvas);

        return new PaintCanvas(getContext(), null, mGestureDetector);
        //PaintCanvas(Context context, AttributeSet attrs, GestureDetector mGestureDetector)
    }


    public class PaintCanvas extends View implements View.OnTouchListener{

        private Paint paint = new Paint();
        private Path path = new Path();
        private int backGroundColor = Color.WHITE;
        private GestureDetector mGestureDetector;

        /*public PaintCanvas(Context context) {
            super(context);
            setOnTouchListener(this);
            setBackgroundColor(backGroundColor);
            initPaint();
        }*/

        public PaintCanvas(Context context, AttributeSet attrs, GestureDetector mGestureDetector) {
            super(context, attrs);
            this.mGestureDetector = mGestureDetector;
            setOnTouchListener(this);
            setBackgroundColor(backGroundColor);
            initPaint();
        }

        //Inicializa o stroke da imagem
        private void initPaint() {
            paint.setAntiAlias(true);
            paint.setStrokeWidth(20f);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);// draws the path with the paint
        }

        @Override
        public boolean performClick() {
            return super.performClick();
        }

        public boolean onTouch(View v, MotionEvent event) {
            mGestureDetector.onTouchEvent(event);
            return false; // let the event go to the rest of the listeners
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);// updates the path initial point
                    return true;
                case MotionEvent.ACTION_MOVE:
                    path.lineTo(eventX, eventY);// makes a line to the point each time this event is fired
                    break;
                case MotionEvent.ACTION_UP:// when you lift your finger
                    performClick();
                    break;
                default:
                    return false;
            }
            // Schedules a repaint.
            invalidate();
            return true;
        }

        public void changeColor(int color){
            paint.setColor(color);
            //switch aqui em principio para as cores
        }

        public void erase(){
            paint.setColor(backGroundColor);
        }

        public void changeBackground(){
            //Random r = new Random();
            //backGroundColor = Color.rgb(r.nextInt(256), r.nextInt(256), r.nextInt(256));
            //backGroundColor = Color.RED;
            int backGroundColorRed = Color.RED;
            setBackgroundColor(backGroundColorRed);
            //setBackgroundColor(backGroundColor);
        }

        public void changeStrokeSize(){
            paint.setStrokeWidth(50f);
        }
    }

    public class GestureListener extends GestureDetector.SimpleOnGestureListener implements GestureDetector.OnDoubleTapListener {

        private PaintCanvas canvas;

        void setCanvas(PaintCanvas canvas) {
            this.canvas = canvas;
        }

        // On Hold Gesture
        @Override
        public void onLongPress(MotionEvent motionEvent) {
            canvas.changeBackground();
            Log.d("LongPress","Long Press!");
        }

        // On Double Tap
        @Override
        public boolean onDoubleTap(MotionEvent motionEvent) {
            //canvas.erase();
            Log.d("DoubleTap","Double Click!");
            return false;
        }

        // On Single Tap
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.i("SingleTap", "Single Tap!");
            canvas.changeStrokeSize();
            return false;
        }
        public void changeCanvasColor(int color){ canvas.changeColor(color); }
    }

}
