package com.example.paint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button settings_button = findViewById(R.id.settings_button_btn);
        Button about_button = findViewById(R.id.about_button_btn);

        settings_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent switch_to_settings = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(switch_to_settings);
            }
        });

        about_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent switch_to_about = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(switch_to_about);
            }
        });


    }


}