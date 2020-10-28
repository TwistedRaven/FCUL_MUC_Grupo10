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
        return inflater.inflate(R.layout.fragment_palette, container, false);
    }

    public interface OnPaletteFragmentListener {
        void messageCanvas(int color);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnPaletteFragmentListener) {
            OnPaletteFragmentListener onPaletteFragmentListener = (OnPaletteFragmentListener) context;
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
}
