package com.example.will.acidbasemodel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //we back, boys
        final Button LSIbutton = findViewById(R.id.lsiccpp);
        LSIbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //setContentView(R.layout.activity_lsiccpp);
                launchLSICCPP();
            }
        });

        final Button acidbaseButton = findViewById(R.id.acidbase);
        acidbaseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //setContentView(R.layout.activity_acidbase);
                launchAcidbase();
            }
        });

    }
    private void launchAcidbase() {

        Intent intent = new Intent(this, acidbase.class);
        startActivity(intent);
    }
    private void launchLSICCPP() {

        Intent intent = new Intent(this, LSICCPP.class);
        startActivity(intent);
    }
}
