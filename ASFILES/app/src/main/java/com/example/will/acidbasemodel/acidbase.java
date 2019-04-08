package com.example.will.acidbasemodel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

        EditText temp = (EditText)findViewById(R.id.tempIn);
        EditText tds = (EditText)findViewById(R.id.tdsIn);
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


        startActivity(intent);
    }
}
