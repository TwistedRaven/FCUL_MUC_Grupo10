package com.example.paint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends AppCompatActivity {

    int splashColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CanvasFragment cFragment = new CanvasFragment();
        PaletteFragment pFragment = new PaletteFragment();

        fragmentTransaction.add(R.id.fragment_container_canvas, cFragment); //container do canvas fragment
        fragmentTransaction.add(R.id.fragment_container_palette, pFragment); //container do palette fragment
        fragmentTransaction.commit();

        Button settings_button = findViewById(R.id.settings_button_btn);
        Button about_button = findViewById(R.id.about_button_btn);

        settings_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openColorPicker();
            }
        });

        about_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent switch_to_about = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(switch_to_about);
            }
        });


    }

    private void openColorPicker() {

        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, splashColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                splashColor = color;
                Intent switch_to_splash = new Intent();
                switch_to_splash.putExtra("color", splashColor);
                setResult(RESULT_OK, switch_to_splash);
                finish();
            }
        });
        colorPicker.show();
    }
}