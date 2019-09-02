package com.capstone.will.waterqualitymodel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class acidbase extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acidbase);
        if (getIntent().getBooleanExtra("EXIT", false)) { finish(); }

        View view = findViewById(R.id.acidbase);
        Context context = this.getApplicationContext();
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.mainColor));


        final Button calculateButton = findViewById(R.id.calculateAcidBase);
        calculateButton.setBackgroundColor(Color.parseColor("#a67884"));
        calculateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                launchResults();
            }
        });


    }
    private void launchResults() {

        EditText temp = findViewById(R.id.tempIn);
        EditText tds = findViewById(R.id.tdsIn);
        EditText alk = findViewById(R.id.alkalinityIn);
        EditText ph = findViewById(R.id.pHIn);
        EditText tph = findViewById(R.id.targetPHIn);

        Intent intent = new Intent(this, AcidBaseResults.class);
        String tempContent = temp.getText().toString();
        String tdsContent = tds.getText().toString();
        String alkContent = alk.getText().toString();
        String phContent = ph.getText().toString();
        String tphContent = tph.getText().toString();

        intent.putExtra("TEMP", tempContent);
        intent.putExtra("TDS", tdsContent);
        intent.putExtra("ALK", alkContent);
        intent.putExtra("PH", phContent);
        intent.putExtra("TPH", tphContent);

        if (tempContent.matches("")) {
            Toast.makeText(this, "Please enter a valid Temperature value", Toast.LENGTH_SHORT).show();
        }else if (tdsContent.matches("")) {
            Toast.makeText(this, "Please enter a valid TDS value", Toast.LENGTH_SHORT).show();
        }else if (alkContent.matches("")) {
            Toast.makeText(this, "Please enter a valid Alkalinity value", Toast.LENGTH_SHORT).show();
        }else if (phContent.matches("")) {
            Toast.makeText(this, "Please enter a valid pH value", Toast.LENGTH_SHORT).show();
        }else if (tphContent.matches("")) {
            Toast.makeText(this, "Please enter a valid Target pH value", Toast.LENGTH_SHORT).show();
        }else{
            startActivity(intent);
        }



    }
}
