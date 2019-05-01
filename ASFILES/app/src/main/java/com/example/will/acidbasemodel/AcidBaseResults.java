package com.example.will.acidbasemodel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AcidBaseResults extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acid_base_results);

        View view = findViewById(R.id.acidbaseresLayout);
        Context context = this.getApplicationContext();
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.mainColor));

        final Button doneButton = findViewById(R.id.abDone);
        doneButton.setBackgroundColor(Color.parseColor("#a67884"));
        doneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(AcidBaseResults.this, acidbase.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);

                finish();
            }
        });

        String ph = getIntent().getStringExtra("PH");
        String tds = getIntent().getStringExtra("TDS");
        String tph = getIntent().getStringExtra("TPH");
        String temp = getIntent().getStringExtra("TEMP");
        String alk = getIntent().getStringExtra("ALK");

        double tdsD = Double.parseDouble(tds);
        double tempD = Double.parseDouble(temp);
        double phD = Double.parseDouble(ph);
        double tphD = Double.parseDouble(tph);
        double alkD = Double.parseDouble(alk);

        TextView alpha1 = findViewById(R.id.alpha1);
        TextView alpha2 = findViewById(R.id.alpha2);

        TextView k1 = findViewById(R.id.k1);
        TextView k2 = findViewById(R.id.k2);
        TextView kw = findViewById(R.id.kw);

        TextView ct = findViewById(R.id.ct);
        TextView finalAlk = findViewById(R.id.finalAlkalinity);

        TextView k1pk = findViewById(R.id.k1pk);
        TextView k2pk = findViewById(R.id.k2pk);
        TextView kwpk = findViewById(R.id.kwpk);

        TextView h2so4 = findViewById(R.id.h2so4);

        TextView HCl = findViewById(R.id.HCl);

        TextView NaOH = findViewById(R.id.NaOH);

        TextView CaOH2 = findViewById(R.id.CaOH2);

        double alpha1num = sigDigRounder(Math.pow(10,(-0.0025*Math.sqrt(tdsD))), 4, 0);
        String alpha1Final = Double.toString(alpha1num);

        double alpha2num = sigDigRounder( Math.pow(10,((-0.01*Math.sqrt(tdsD)))), 3, 0 );
        String alpha2Final = Double.toString(alpha2num);

        alpha1.setText(alpha1Final);
        alpha2.setText(alpha2Final);

        double k1num = sigDigRounder(Math.pow(10,(-1*(356.309-21834.4/(273+tempD)-126.834*Math.log10(273+tempD)+0.06092*(273+tempD)+1684915/Math.pow((273.0+tempD),2)))), 4, 0 );
        String k1Final = Double.toString(k1num);

        double k2num = sigDigRounder(Math.pow(10,(-1*(107.887-5151.8/(273+tempD)-38.926*Math.log10(273+tempD)+0.032528*(273+tempD)+563713.9/Math.pow((273+tempD),2)))), 4, 0);
        String k2Final = Double.toString(k2num);

        double kwnum = sigDigRounder(Math.pow(10,(-1*(-6.088+4471/(273+tempD)+0.01706*(273+tempD)))), 4, 0);
        String kwFinal = Double.toString(kwnum);

        k1.setText(k1Final);
        k2.setText(k2Final);
        kw.setText(kwFinal);

        double ctnum = sigDigRounder( ((1+alpha1num*Math.pow(10,(-phD))/k1num + alpha1num*k2num/alpha2num/Math.pow(10, (-phD)))*((alkD/50000-kwnum/alpha1num/Math.pow(10,(-phD))+Math.pow(10, (-phD))/alpha1num)/(1+2*k2num*alpha1num/alpha2num/Math.pow(10,(-phD))))), 5, 0 );
        String ctFinal = Double.toString(ctnum);

        double finalAlkNum = sigDigRounder (50000*(ctnum*(1+2*k2num*alpha1num/alpha2num/Math.pow(10,(-tphD)))/(1+alpha1num*Math.pow(10,(-tphD))/k1num +alpha1num*k2num/alpha2num/Math.pow(10,(-tphD)))+kwnum/alpha1num/Math.pow(10,(-tphD))-Math.pow(10,(-tphD))/alpha1num), 4, 0);
        String finalAlkFinal = Double.toString(finalAlkNum);

        ct.setText(ctFinal);
        finalAlk.setText(finalAlkFinal);

        double k1pknum = sigDigRounder(-Math.log10(k1num), 4, 0 ) ;
        String k1pkFinal = Double.toString(k1pknum);

        double k2pknum = sigDigRounder(-Math.log10(k2num), 4, 0 );
        String k2pkFinal = Double.toString(k2pknum);

        double kwpknum = sigDigRounder(-Math.log10(kwnum), 4, 0);
        String kwpkFinal = Double.toString(kwpknum);

        k1pk.setText(k1pkFinal);
        k2pk.setText(k2pkFinal);
        kwpk.setText(kwpkFinal);

        double h2so4num = sulfuricAcidCalc(phD, tphD, alkD, finalAlkNum);
        String h2so4Final;
        if(h2so4num == 0){
            h2so4Final = "N/A";
        }else {
            h2so4Final = Double.toString(h2so4num);
        }
        h2so4.setText(h2so4Final);

        //IF(pHf<pHi,36.6/50*(alki-Alkf),"NA")
        double hCLnum;
        if(tphD < phD){
            hCLnum = 36.6/50*(alkD - finalAlkNum);
        }else{
            hCLnum = 0.0;
        }

        String hCLfinal;
        if(hCLnum == 0){
            hCLfinal = "N/A";
        }else{
            hCLfinal = Double.toString(hCLnum);
        }
        HCl.setText(hCLfinal);

        double naohNum;
        if(tphD > phD){
            naohNum = 40./50.*(finalAlkNum - alkD);

        }else{
            naohNum = 0.0;
        }

        naohNum = sigDigRounder(naohNum, 3, 0);

        String naohFinal;

        if(naohNum == 0){
            naohFinal = "N/A";
        }else{
            naohFinal = Double.toString(naohNum);
        }
        NaOH.setText(naohFinal);

        double caoh2Num;
        if(tphD > phD){
            caoh2Num =(40.0+17.0*2)/2.0/50.0*(finalAlkNum - alkD);
        }else{
            caoh2Num = 0.0;
        }
        caoh2Num = sigDigRounder(caoh2Num, 3, 0);
        String caoh2Final;
        if(caoh2Num == 0){
            caoh2Final = "N/A";
        }else{
            caoh2Final = Double.toString(caoh2Num);
        }
        CaOH2.setText(caoh2Final);



    }

    public static double sigDigRounder(double value, int nSigDig, int dir) {

        double intermediate = value/Math.pow(10,Math.floor(Math.log10(Math.abs(value)))-(nSigDig-1));

        if(dir > 0)      intermediate = Math.ceil(intermediate);
        else if (dir< 0) intermediate = Math.floor(intermediate);
        else             intermediate = Math.round(intermediate);

        double result = intermediate * Math.pow(10,Math.floor(Math.log10(Math.abs(value)))-(nSigDig-1));

        return result;

    }
    public static double sulfuricAcidCalc(double pH, double phTarget, double alkalinity, double finalAlkalinity){
        if(phTarget < pH) {
            return sigDigRounder( (49.0/50.0)*(alkalinity - finalAlkalinity), 4, 0);
        }else{
            return 0.0;
        }
    }
}
