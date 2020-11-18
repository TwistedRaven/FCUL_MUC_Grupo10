package com.example.paint;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;


public class SettingsFragment extends Fragment {
    private static final String storageLocation = "lines.txt";

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_settings, container, false);
        final Button button = view.findViewById(R.id.settings_save_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                try {
                    final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                    final MainActivity mainActivity = (MainActivity) requireActivity();
                    objectOutputStream.writeObject(mainActivity.getCanvasFragment().getPaintCanvas().getLines());

                    final StorageReference storageReference = FirebaseStorage.getInstance().getReference(storageLocation);
                    storageReference.putBytes(byteArrayOutputStream.toByteArray());

                    Toast.makeText(getContext(), "Click!", Toast.LENGTH_SHORT).show();
                } catch (final IOException e) {
                    Log.e("Settings", e.getMessage(), e);
                }
            }
        });

        return view;
    }
}