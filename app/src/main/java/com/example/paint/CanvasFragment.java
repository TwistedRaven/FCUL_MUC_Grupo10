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

import java.util.Deque;
import java.util.LinkedList;
import java.util.Random;

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

        private Deque<Line> lines = new LinkedList<>();
        private Line currentLine;

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
            currentLine = new Line(false);
            lines.add(currentLine);
            this.mGestureDetector = mGestureDetector;
            setOnTouchListener(this);
            setBackgroundColor(backGroundColor);
            initPaint();
        }

        //Inicializa o stroke da imagem
        private void initPaint() {
            currentLine.getPaint().setAntiAlias(true);
            currentLine.getPaint().setStrokeWidth(20f);
            currentLine.getPaint().setColor(currentPaintColor);
            currentLine.getPaint().setStyle(Paint.Style.STROKE);
            currentLine.getPaint().setStrokeJoin(Paint.Join.ROUND);
        }

        @Override
        protected void onDraw(final Canvas canvas) {
            for (final Line line : lines) {
                if(line.isFromEraser()) {
                    line.getPaint().setColor(backGroundColor); // lines from eraser need to be the same color as background
                }
                canvas.drawPath(line.getPath(), line.getPaint()); // draws each path with its paint
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
                    currentLine.getPath().moveTo(eventX, eventY); // updates the path initial point
                    return true;
                case MotionEvent.ACTION_MOVE:
                    currentLine.getPath().lineTo(eventX, eventY); // makes a line to the point each time this event is fired
                    break;
                case MotionEvent.ACTION_UP: // when you lift your finger
                    currentLine = new Line(currentPaintColor == backGroundColor);
                    lines.add(currentLine);
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
            currentLine.getPaint().setColor(currentPaintColor);
            //switch aqui em principio para as cores
        }

        public void fillBackground() {
            Random r = new Random();
            backGroundColor = Color.rgb(r.nextInt(256), r.nextInt(256), r.nextInt(256));
            setBackgroundColor(backGroundColor);
        }

        public void changeStrokeSize() {
            currentLine.getPaint().setStrokeWidth(50f);
        }

        public void canvasErase() {
            lines.clear();
            lines.add(new Line(currentPaintColor == backGroundColor));
            backGroundColor = Color.WHITE;
            setBackgroundColor(backGroundColor);
        }

        public void eraserCanvas() {
            currentPaintColor = backGroundColor;
            currentLine.getPaint().setColor(currentPaintColor);
        }

        public static class Line {
            private final Path path;
            private final Paint paint;
            private final boolean isFromEraser;

            public Line(boolean isFromEraser) {
                this.path = new Path();
                this.paint = new Paint();
                this.isFromEraser = isFromEraser;
            }

            public Path getPath() {
                return path;
            }

            public Paint getPaint() {
                return paint;
            }

            public boolean isFromEraser() {
                return isFromEraser;
            }
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

    }

}
