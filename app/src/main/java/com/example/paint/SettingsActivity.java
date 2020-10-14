package com.example.paint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import yuku.ambilwarna.AmbilWarnaDialog;

public class SettingsActivity extends AppCompatActivity {
    ConstraintLayout layout;
    int layoutColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        layout = findViewById(R.id.settings_layout);
        Button colorPickerButton = findViewById(R.id.settings_color_picker_button);
        colorPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });
    }

    private void openColorPicker() {

        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, layoutColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {}

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                layout.setBackgroundColor(color);
                layoutColor = color;
            }
        });
        colorPicker.show();
    }
}
