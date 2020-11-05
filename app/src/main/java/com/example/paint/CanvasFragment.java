package com.example.paint;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.*;

import yuku.ambilwarna.AmbilWarnaDialog;

import static android.content.res.Configuration.*;

public class CanvasFragment extends Fragment {
    private static final String canvasLinesBundleKey = "0wskkf37ed";
    private float moveThreshold = 1.5f;
    //private float moveThreshold = 7f;

    private PaintCanvas paintCanvas;

    private GestureListener mGestureListener;
    private GestureDetector mGestureDetector;

    private SensorManager mSensorManager;

    private Sensor mAccelerometer;
    private boolean isAccelerometerSensorAvailable, itIsNotFirstTime = false;

    private float lastX, lastY, lastZ;
    private float xDifference, yDifference, zDifference;

    private Vibrator vibrator;

    private SensorEventListener lightEventListener, shakeEventListener;

    private boolean isLightSensorAvailable;
    private Sensor lightSensor;

    private Configuration configuration;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mGestureListener = new GestureListener();
        mGestureDetector = new GestureDetector(getContext(), mGestureListener);
        mGestureDetector.setIsLongpressEnabled(true);
        mGestureDetector.setOnDoubleTapListener(mGestureListener);
        paintCanvas = new PaintCanvas(getContext(), null, mGestureDetector);
        mGestureListener.setCanvas(paintCanvas);

        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null) {
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            isAccelerometerSensorAvailable = true;
            Log.d("Accelerometer", "Accelerometer is initialized!");
        } else {
            isAccelerometerSensorAvailable = false;
            Log.d("Accelerometer", "Accelerometer is not initialized!");
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null) {
            lightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            isLightSensorAvailable = true;
            Log.d("Light", "Light is initialized!");
        } else {
            isLightSensorAvailable = false;
            Log.d("Light", "Light is not initialized!");
        }

        //PaintCanvas(Context context, AttributeSet attrs, GestureDetector mGestureDetector)
        lightEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float value = sensorEvent.values[0];
                Log.d("Light", Float.toString(value));
                int newValue = 0;

                if(value >= 20000 && value < 25000) {
                    newValue = manipulateColor(paintCanvas.getBackgroundColor(), 0.75f);
                }
                if(value >= 25000) {
                    newValue = manipulateColor(paintCanvas.getBackgroundColor(), 1f);
                }
                if(value < 20000 && value >= 15000) {
                    newValue = manipulateColor(paintCanvas.getBackgroundColor(), 0.5f);
                }
                if(value < 15000) {
                    newValue = manipulateColor(paintCanvas.getBackgroundColor(), 0.25f);
                }
                paintCanvas.setBackgroundColor(newValue);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        shakeEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float currentX = sensorEvent.values[0];
                float currentY = sensorEvent.values[1];
                float currentZ = sensorEvent.values[2];
                //Log.d("CurrentX", Float.toString(currentX));
                //Log.d("CurrentY", Float.toString(currentY));
                //Log.d("CurrentZ", Float.toString(currentZ));

                if (itIsNotFirstTime) {
                    xDifference = Math.abs(lastX - currentX);
                    yDifference = Math.abs(lastY - currentY);
                    zDifference = Math.abs(lastZ - currentZ);

                    //Log.d("xDifference", Float.toString(xDifference));
                    //Log.d("yDifference", Float.toString(yDifference));
                    //Log.d("zDifference", Float.toString(zDifference));

                    // if ((Math.abs(xDifference) > moveThreshold)) {

                    // if ((xDifference > moveThreshold && yDifference > moveThreshold) ||
                    // (xDifference > moveThreshold && zDifference > moveThreshold) ||
                    // (yDifference > moveThreshold && zDifference > moveThreshold)) {

                        if ((xDifference > moveThreshold && yDifference > moveThreshold) ||
                        (xDifference > moveThreshold && zDifference > moveThreshold) ||
                         (yDifference > moveThreshold && zDifference > moveThreshold)) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                paintCanvas.canvasErase();
                                paintCanvas.invalidate();

                                Log.d("Done", "Done!");
                                vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
                            } else {
                                vibrator.vibrate(1000);
                            }
                        }
                }

                lastX = currentX;
                lastY = currentY;
                lastZ = currentZ;
                itIsNotFirstTime = true;
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

        return paintCanvas;
    }

    @Override
    public void onActivityCreated(final @Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            paintCanvas.finishedLines.clear();
            final Parcelable[] lines = savedInstanceState.getParcelableArray(canvasLinesBundleKey);
            if (lines == null) {
                throw new IllegalArgumentException("Bundle returned null Lines array.");
            }
            for (final Parcelable line : lines) {
                paintCanvas.finishedLines.addLast((PaintCanvas.Line) line);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isAccelerometerSensorAvailable) {
            mSensorManager.registerListener(shakeEventListener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        mSensorManager.registerListener(lightEventListener, lightSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (!isAccelerometerSensorAvailable) {
            mSensorManager.unregisterListener(shakeEventListener);
        }
        mSensorManager.unregisterListener(lightEventListener);
    }

    @Override
    public void onSaveInstanceState(final @NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        final Parcelable[] canvasLines = paintCanvas.finishedLines.toArray(new PaintCanvas.Line[0]);
        outState.putParcelableArray(canvasLinesBundleKey, canvasLines);
    }

    public void changeCanvasColor(int color) {
        paintCanvas.changeColor(color);
    }

    public void eraseCanvas() {
        paintCanvas.changeColor(paintCanvas.getBackgroundColor());
    }

    public void undo() {
        paintCanvas.undo();
        paintCanvas.invalidate();
    }

    private static class PaintCanvas extends View implements View.OnTouchListener {

        private Deque<Line> finishedLines = new LinkedList<>();
        private Line currentLine;

        private int backGroundColor = Color.WHITE;
        private int currentPaintColor = Color.BLACK;

        private GestureDetector mGestureDetector;

        public PaintCanvas(Context context, AttributeSet attrs, GestureDetector mGestureDetector) {
            super(context, attrs);
            currentLine = new Line(false);
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
            //Log.d("Draw", "Draw!");
            for (final Line line : finishedLines) {
                if (line.isFromEraser()) {
                    line.getPaint().setColor(backGroundColor); // lines from eraser need to be the same color as background
                }
                canvas.drawPath(line.getPath(), line.getPaint()); // draws each path with its paint
            }
            canvas.drawPath(currentLine.getPath(), currentLine.getPaint());
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
                    finishedLines.addLast(currentLine);
                    currentLine = new Line(currentPaintColor == backGroundColor);
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
            currentLine.isFromEraser = backGroundColor == currentPaintColor;
            //switch aqui em principio para as cores
        }

        public void fillBackground() {
            AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(getContext(), Color.WHITE, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                @Override
                public void onCancel(AmbilWarnaDialog dialog) {}

                @Override
                public void onOk(AmbilWarnaDialog dialog, int color) {
                    backGroundColor = color;
                    setBackgroundColor(backGroundColor);
                }
            });
            colorPicker.show();
        }

        public void changeStrokeSize() {
            currentLine.getPaint().setStrokeWidth(10f);
        }

        public void canvasErase() {
            finishedLines.clear();
            backGroundColor = Color.WHITE;
            setBackgroundColor(backGroundColor);
            currentLine = new Line(currentPaintColor == backGroundColor);
        }

        public void undo() {
            finishedLines.pollLast();
        }

        public int getBackgroundColor() {
            return backGroundColor;
        }

        private static class Line implements Parcelable {
            public static final Parcelable.Creator<Line> CREATOR = new Parcelable.Creator<Line>() {
                @Override
                public Line createFromParcel(final Parcel source) {
                    return new Line(source);
                }

                @Override
                public Line[] newArray(final int size) {
                    return new Line[size];
                }
            };
            private final Path path;
            private final Paint paint;
            private boolean isFromEraser;

            public Line(boolean isFromEraser) {
                this.path = new Path();
                this.paint = new Paint();
                this.isFromEraser = isFromEraser;
            }

            public Line(final @NonNull Parcel input) {
                path = (Path) input.readValue(Path.class.getClassLoader());
                paint = (Paint) input.readValue(Paint.class.getClassLoader());
                isFromEraser = input.readInt() != 0; // readBoolean() requires API 29
            }

            @Override
            public void writeToParcel(final Parcel dest, final int flags) {
                dest.writeValue(path);
                dest.writeValue(paint);
                dest.writeInt(isFromEraser ? 1 : 0); // writeBoolean() requires API 29
            }

            @Override
            public int describeContents() {
                return 0;
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
            return false;
        }

    }

    public static int manipulateColor(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r,255),
                Math.min(g,255),
                Math.min(b,255));
    }

}
