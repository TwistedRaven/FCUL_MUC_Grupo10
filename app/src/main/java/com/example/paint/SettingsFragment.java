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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;


public class SettingsFragment extends Fragment {
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        final MainActivity mainActivity = (MainActivity) requireActivity();
        final View view = inflater.inflate(R.layout.fragment_settings, container, false);
        final Button saveButton = view.findViewById(R.id.settings_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                databaseReference.setValue("");
                // vai criar uma child na database CM-Paint com o nome backgroundColor. ex: backgroundColor: -2875103
                databaseReference.child("backgroundColor").setValue(mainActivity.getCanvasFragment().getPaintCanvas().getBackgroundColor());
                final Deque<CanvasFragment.PaintCanvas.Line> lines = mainActivity.getCanvasFragment().getPaintCanvas().getLines();

                // por cada linha vai criar uma child com os conteúdos da linha
                int i = 0;
                for (final CanvasFragment.PaintCanvas.Line line : lines) {
                    databaseReference.child(Integer.toString(i++)).setValue(line);
                }
                Toast.makeText(getContext(), "Saved with success.", Toast.LENGTH_SHORT).show();
            }
        });
        final Button restoreButton = view.findViewById(R.id.settings_restore_button);
        restoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public void onDataChange(final @NonNull DataSnapshot snapshot) {
                        final Deque<CanvasFragment.PaintCanvas.Line> lines = new LinkedList<>();
                        // vai iterar todos os childs (0, 1, ..., backgroundColor)
                        for (final DataSnapshot innerSnapshot : snapshot.getChildren()) {
                            if (!Objects.requireNonNull(innerSnapshot.getKey()).equals("backgroundColor")) {
                                Log.d("Settings", innerSnapshot.toString());
                                final HashMap<String, Object> lineAsMap = (HashMap<String, Object>) innerSnapshot.getValue();
                                if (lineAsMap == null) {
                                    throw new IllegalStateException("InnerSnapShot was null.");
                                }

                                final int lineColor = ((Long) Objects.requireNonNull(lineAsMap.get("color"))).intValue();
                                final boolean lineFromEraser = (Boolean) Objects.requireNonNull(lineAsMap.get("fromEraser"));
                                final ArrayList<Object> linePointsAsList = (ArrayList<Object>) Objects.requireNonNull(lineAsMap.get("points"));

                                final Deque<Float> linePoints = new LinkedList<>();
                                for (final Object pointAsObject : linePointsAsList) {
                                    linePoints.addLast(((Double) pointAsObject).floatValue());
                                }
                                final CanvasFragment.PaintCanvas.Line line = new CanvasFragment.PaintCanvas.Line(linePoints, lineColor, lineFromEraser);
                                lines.addLast(line);
                            } else {
                                // nota a database guarda inteiros como Long
                                final int color = ((Long) Objects.requireNonNull(snapshot.child("backgroundColor").getValue())).intValue();
                                mainActivity.getCanvasFragment().getPaintCanvas().changeBackgroundColor(color);
                            }
                        }
                        mainActivity.getCanvasFragment().getPaintCanvas().setAndInitLines(lines);
                        Toast.makeText(getContext(), "Restore succeeded.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(final @NonNull DatabaseError error) {
                        Log.e("Settings", error.getMessage(), error.toException());
                        Toast.makeText(getContext(), "Failed to read value.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        return view;
    }
}