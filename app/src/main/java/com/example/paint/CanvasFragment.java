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

import java.util.ArrayList;
import java.util.List;

public class CanvasFragment extends Fragment {

    PaintCanvas paintCanvas;

    GestureListener mGestureListener;
    GestureDetector mGestureDetector;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mGestureListener = new GestureListener();
        mGestureDetector = new GestureDetector(getContext(), mGestureListener);
        mGestureDetector.setIsLongpressEnabled(true);
        mGestureDetector.setOnDoubleTapListener(mGestureListener);
        paintCanvas = new PaintCanvas(getContext(), null, mGestureDetector);
        mGestureListener.setCanvas(paintCanvas);

        return paintCanvas;
        //PaintCanvas(Context context, AttributeSet attrs, GestureDetector mGestureDetector)
    }

    public void changeCanvasColor(int color) {
        paintCanvas.changeColor(color);
    }


    public static class PaintCanvas extends View implements View.OnTouchListener {

        private List<Path> paths = new ArrayList<>();
        private List<Paint> paints = new ArrayList<>();

        private Paint currentPaint = new Paint();
        private Path currentPath = new Path();

        private int backGroundColor = Color.WHITE;
        private int currentPaintColor = Color.BLACK;

        private GestureDetector mGestureDetector;

        /*public PaintCanvas(Context context) {
            super(context);
            setOnTouchListener(this);
            setBackgroundColor(backGroundColor);
            initPaint();
        }*/

        public PaintCanvas(Context context, AttributeSet attrs, GestureDetector mGestureDetector) {
            super(context, attrs);
            paths.add(currentPath);
            paints.add(currentPaint);
            this.mGestureDetector = mGestureDetector;
            setOnTouchListener(this);
            setBackgroundColor(backGroundColor);
            initPaint();
        }

        //Inicializa o stroke da imagem
        private void initPaint() {
            currentPaint.setAntiAlias(true);
            currentPaint.setStrokeWidth(20f);
            currentPaint.setColor(currentPaintColor);
            currentPaint.setStyle(Paint.Style.STROKE);
            currentPaint.setStrokeJoin(Paint.Join.ROUND);
        }

        @Override
        protected void onDraw(final Canvas canvas) {
            for (int i = 0; i < paths.size(); i++) {
                canvas.drawPath(paths.get(i), paints.get(i)); // draws each path with its paint
            }
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
                    currentPath.moveTo(eventX, eventY);// updates the path initial point
                    return true;
                case MotionEvent.ACTION_MOVE:
                    currentPath.lineTo(eventX, eventY);// makes a line to the point each time this event is fired
                    break;
                case MotionEvent.ACTION_UP:// when you lift your finger
                    paths.add(currentPath = new Path());
                    paints.add(currentPaint = new Paint());
                    initPaint();
                    performClick();
                    break;
                default:
                    return false;
            }
            // Schedules a repaint.
            invalidate();
            return true;
        }

        public void changeColor(int color) {
            currentPaintColor = color;
            currentPaint.setColor(currentPaintColor);
            //switch aqui em principio para as cores
        }

        public void fillBackground() {
            setBackgroundColor(backGroundColor);
        }

        public void changeStrokeSize() {
            currentPaint.setStrokeWidth(50f);
        }

        public void canvasErase() {
            paths.clear();
            paints.clear();
            paths.add(currentPath = new Path());
            paints.add(currentPaint = new Paint());
            backGroundColor = Color.WHITE;
            setBackgroundColor(backGroundColor);
        }
    }

    public static class GestureListener extends GestureDetector.SimpleOnGestureListener implements GestureDetector.OnDoubleTapListener {

        private PaintCanvas canvas;

        void setCanvas(PaintCanvas canvas) {
            this.canvas = canvas;
        }

        // On Hold Gesture
        @Override
        public void onLongPress(MotionEvent motionEvent) {
            canvas.fillBackground();
            Log.d("LongPress", "Long Press!");
        }

        // On Double Tap
        @Override
        public boolean onDoubleTap(MotionEvent motionEvent) {
            canvas.canvasErase();
            Log.d("DoubleTap", "Double Click!");
            return false;
        }

        // On Single Tap
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.i("SingleTap", "Single Tap!");
            //canvas.changeStrokeSize();
            return false;
        }

        public void changeCanvasColor(int color) {
            canvas.changeColor(color);
        }
    }

}
