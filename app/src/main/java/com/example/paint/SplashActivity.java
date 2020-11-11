package com.example.paint;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SplashActivity extends AppCompatActivity {

    ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Activity is being created
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        layout = (ConstraintLayout) findViewById(R.id.layout); //pk e k isto da null?????

        Button start_button = findViewById(R.id.start_button_btn);

        start_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Muda o user para o ecrã "principal"
                Intent switch_to_main = new Intent(getApplicationContext(), MapActivity.class);
                startActivityForResult(switch_to_main, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 1)
            layout.setBackgroundColor(data.getIntExtra("color", -1));
    }
}