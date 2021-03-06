package com.example.paint;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import yuku.ambilwarna.AmbilWarnaDialog;

public class PaletteFragment extends Fragment {

    //Changed to public because of
    // public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PaletteFragment.OnPaletteFragmentListener {
    public OnPaletteFragmentListener mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_palette, container, false);

        final Button b1 = v.findViewById(R.id.color_black);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCanvasColor(((ColorDrawable) b1.getBackground()).getColor());
            }
        });
        final Button b2 = v.findViewById(R.id.color_purple);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCanvasColor(((ColorDrawable) b2.getBackground()).getColor());
            }
        });
        final Button b3 = v.findViewById(R.id.color_orange);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCanvasColor(((ColorDrawable) b3.getBackground()).getColor());
            }
        });
        final Button b4 = v.findViewById(R.id.color_yellow);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCanvasColor(((ColorDrawable) b4.getBackground()).getColor());
            }
        });
        final Button b5 = v.findViewById(R.id.color_grey);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCanvasColor(((ColorDrawable) b5.getBackground()).getColor());
            }
        });
        final Button b6 = v.findViewById(R.id.color_red);
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCanvasColor(((ColorDrawable) b6.getBackground()).getColor());
            }
        });
        final Button b7 = v.findViewById(R.id.color_blue);
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCanvasColor(((ColorDrawable) b7.getBackground()).getColor());
            }
        });
        final Button b8 = v.findViewById(R.id.color_green);
        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCanvasColor(((ColorDrawable) b8.getBackground()).getColor());
            }
        });


        final ImageButton customColor = v.findViewById(R.id.customColor);
        customColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(getContext(), Color.BLACK, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {}

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        changeCanvasColor(color);
                    }
                });
                colorPicker.show();
            }
        });

        final ImageButton eraser = v.findViewById(R.id.eraser);
        eraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               eraserCanvas();
            }
        });

        final ImageButton undo = v.findViewById(R.id.undo);
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undo();
            }
        });
        return v;
    }

    public interface OnPaletteFragmentListener {
        void messageCanvas(int color);
        void eraserCanvas();
        void undo();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnPaletteFragmentListener) {
            mCallback = (OnPaletteFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPaletteFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    public void changeCanvasColor(View v){
        Button b = (Button) v;
        mCallback.messageCanvas(((ColorDrawable) b.getBackground()).getColor());
    }

    public void changeCanvasColor(int color){
        Log.d("change", "gonna change color");
        mCallback.messageCanvas(color);
    }
    public void eraserCanvas(){mCallback.eraserCanvas();}
    public void undo(){mCallback.undo();}

}
