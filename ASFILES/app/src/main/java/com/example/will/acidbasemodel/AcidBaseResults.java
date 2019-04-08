package com.example.will.acidbasemodel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AcidBaseResults extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acid_base_results);

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

        double alpha1num = Math.pow(10,(-0.0025*Math.sqrt(tdsD)));
        String alpha1Final = Double.toString(alpha1num);

        double alpha2num = Math.pow(10,((-0.01*Math.sqrt(tdsD))));
        String alpha2Final = Double.toString(alpha2num);

        alpha1.setText(alpha1Final);
        alpha2.setText(alpha2Final);

        double k1num = Math.pow(10,(-1*(356.309-21834.4/(273+tempD)-126.834*Math.log10(273+tempD)+0.06092*(273+tempD)+1684915/Math.pow((273.0+tempD),2))));
        String k1Final = Double.toString(k1num);

        double k2num = Math.pow(10,(-1*(107.887-5151.8/(273+tempD)-38.926*Math.log10(273+tempD)+0.032528*(273+tempD)+563713.9/Math.pow((273+tempD),2))));
        String k2Final = Double.toString(k2num);

        double kwnum = Math.pow(10,(-1*(-6.088+4471/(273+tempD)+0.01706*(273+tempD))));
        String kwFinal = Double.toString(kwnum);

        k1.setText(k1Final);
        k2.setText(k2Final);
        kw.setText(kwFinal);

        double ctnum = ((1+alpha1num*Math.pow(10,(-phD))/k1num + alpha1num*k2num/alpha2num/Math.pow(10, (-phD)))*((alkD/50000-kwnum/alpha1num/Math.pow(10,(-phD))+Math.pow(10, (-phD))/alpha1num)/(1+2*k2num*alpha1num/alpha2num/Math.pow(10,(-phD)))));
        String ctFinal = Double.toString(ctnum);

        double finalAlkNum = (50000*(ctnum*(1+2*k2num*alpha1num/alpha2num/Math.pow(10,(-tphD)))/(1+alpha1num*Math.pow(10,(-tphD))/k1num +alpha1num*k2num/alpha2num/Math.pow(10,(-tphD)))+kwnum/alpha1num/Math.pow(10,(-tphD))-Math.pow(10,(-tphD))/alpha1num));
        String finalAlkFinal = Double.toString(finalAlkNum);

        ct.setText(ctFinal);
        finalAlk.setText(finalAlkFinal);

        double k1pknum = -Math.log10(k1num);
        String k1pkFinal = Double.toString(k1pknum);

        double k2pknum = -Math.log10(k2num);
        String k2pkFinal = Double.toString(k2pknum);

        double kwpknum = -Math.log10(kwnum);
        String kwpkFinal = Double.toString(kwpknum);

        k1pk.setText(k1pkFinal);
        k2pk.setText(k2pkFinal);
        kwpk.setText(kwpkFinal);

        double h2so4num = sulfuricAcidCalc(phD, tphD, alkD, finalAlkNum);
        String h2so4Final = Double.toString(h2so4num);

        h2so4.setText(h2so4Final);



    }
    public static double sulfuricAcidCalc(double pH, double phTarget, double alkalinity, double finalAlkalinity){
        if(phTarget < pH) {
            return (49.0/50.0)*(alkalinity - finalAlkalinity);
        }else{
            return 0.0;
        }
    }
}
