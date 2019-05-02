package com.capstone.will.waterqualitymodel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LSICCPP extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lsiccpp);
        if (getIntent().getBooleanExtra("EXIT", false)) { finish(); }

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

        if (phContent.matches("")) {
            Toast.makeText(this, "Please enter a valid pH value", Toast.LENGTH_SHORT).show();
            return;
        }else if (alkContent.matches("")) {
            Toast.makeText(this, "Please enter a valid Alkalinity value", Toast.LENGTH_SHORT).show();
            return;
        }else if (calciumContent.matches("")) {
            Toast.makeText(this, "Please enter a valid Calcium value", Toast.LENGTH_SHORT).show();
            return;
        }else if (tdsContent.matches("")) {
            Toast.makeText(this, "Please enter a valid Total Dissolved Solids value", Toast.LENGTH_SHORT).show();
            return;
        }else if (tempContent.matches("")) {
            Toast.makeText(this, "Please enter a valid Temperature value", Toast.LENGTH_SHORT).show();
            return;
        }else{
            startActivity(intent);
        }




        intent.putExtra("cTEMP", tempContent);
        intent.putExtra("cTDS", tdsContent);
        intent.putExtra("cALK", alkContent);
        intent.putExtra("cPH", phContent);
        intent.putExtra("cCAL", calciumContent);

        CheckBox complexOption = (CheckBox)findViewById(R.id.ccppTableOption);
        if(complexOption.isChecked()){
            intent.putExtra("COMP", 1);
        }else{
            intent.putExtra("COMP", 0);
        }


        Bundle extras = intent.getExtras();
        if (extras == null) {
            finish();
        }


        startActivity(intent);
    }
}
