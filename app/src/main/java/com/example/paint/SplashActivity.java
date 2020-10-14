package com.example.paint;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Activity is being created
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Button start_button = findViewById(R.id.start_button_btn);

        start_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Muda o user para o ecrã "principal"
                Intent switch_to_main = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(switch_to_main, 1);
            }
        });
    }
}