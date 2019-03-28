package com.example.will.acidbasemodel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class acidbase extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acidbase);

        final Button calculateButton = findViewById(R.id.calculateAcidBase);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                launchResults();
            }
        });


    }
    private void launchResults() {

        Intent intent = new Intent(this, AcidBaseResults.class);
        startActivity(intent);
    }
}
