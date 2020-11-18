package com.example.paint;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Deque;


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
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference(storageLocation);
        final MainActivity mainActivity = (MainActivity) requireActivity();
        final View view = inflater.inflate(R.layout.fragment_settings, container, false);
        final Button saveButton = view.findViewById(R.id.settings_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                try (final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
                    final Deque<CanvasFragment.PaintCanvas.Line> lines = mainActivity.getCanvasFragment().getPaintCanvas().getLines();
                    objectOutputStream.writeObject(lines);
                } catch (final IOException e) {
                    Log.e("Settings", e.getMessage(), e);
                    System.exit(1);
                }
                final UploadTask uploadTask = storageReference.putBytes(byteArrayOutputStream.toByteArray());
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(), "Drawing saved with success.", Toast.LENGTH_SHORT).show();
                    }
                });
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(final @NonNull Exception e) {
                        Log.e("Settings", e.getMessage(), e);
                        Toast.makeText(getContext(), "Error while Saving!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        final Button restoreButton = view.findViewById(R.id.settings_restore_button);
        restoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(storageReference.getBytes(9));
                final Task<byte[]> task = storageReference.getBytes(Long.MAX_VALUE);
                task.addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(final byte[] bytes) {
                        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                        try (final ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
                            @SuppressWarnings("unchecked") final Deque<CanvasFragment.PaintCanvas.Line> lines = (Deque<CanvasFragment.PaintCanvas.Line>) objectInputStream.readObject();
                            mainActivity.getCanvasFragment().getPaintCanvas().setLines(lines);
                            Toast.makeText(getContext(), "Drawing restored with success", Toast.LENGTH_SHORT).show();
                        } catch (final IOException | ClassNotFoundException e) {
                            Log.e("Settings", e.getMessage(), e);
                            System.exit(1);
                        }
                    }
                });
                task.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(final @NonNull Exception e) {
                        Log.e("Settings", e.getMessage(), e);
                        Toast.makeText(getContext(), "Error while Restoring!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return view;
    }
}