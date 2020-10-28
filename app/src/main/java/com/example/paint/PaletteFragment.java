package com.example.paint;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class PaletteFragment extends Fragment {

    private OnPaletteFragmentListener mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_palette, container, false);

        final Button b = v.findViewById(R.id.color_red);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCanvasColor(((ColorDrawable) b.getBackground()).getColor());
            }
        });

        return v;
    }

    public interface OnPaletteFragmentListener {
        void messageCanvas(int color);
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
        mCallback.messageCanvas(color);
    }
}
