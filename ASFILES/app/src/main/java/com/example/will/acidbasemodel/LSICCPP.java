package com.example.will.acidbasemodel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LSICCPP extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lsiccpp);

        View view = findViewById(R.id.lsiccppLayout);
        Context context = this.getApplicationContext();
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.mainColor));

        final Button calculateButton = findViewById(R.id.calculateCCPP);
        calculateButton.setBackgroundColor(Color.parseColor("#a67884"));
        calculateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                launchResults();
            }
        });
    }
    private void launchResults() {


        EditText temp = findViewById(R.id.tempCCPPin);
        EditText tds = findViewById(R.id.tdsCCPPin);
        EditText alk = findViewById(R.id.alkalinityCCPPin);
        EditText ph = findViewById(R.id.phCCPPin);
        EditText calcium = findViewById(R.id.calciumCCPPin);

        Intent intent = new Intent(this, LSICCPPResults.class);
        String tempContent = temp.getText().toString();
        String tdsContent = tds.getText().toString();
        String alkContent = alk.getText().toString();
        String phContent = ph.getText().toString();
        String calciumContent = calcium.getText().toString();

        intent.putExtra("cTEMP", tempContent);
        intent.putExtra("cTDS", tdsContent);
        intent.putExtra("cALK", alkContent);
        intent.putExtra("cPH", phContent);
        intent.putExtra("cCAL", calciumContent);


        startActivity(intent);
    }
}
