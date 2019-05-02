package com.capstone.will.waterqualitymodel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LSICCPPResults extends AppCompatActivity {
    private static double temperature;
    private static double tds;
    private static double pH;
    private static double alkalinity;
    private static double phTarget;
    private static double finalAlkalinity;
    private static double ct;
    private static double alpha1;
    private static double alpha2;
    private static double portalCalcium;
    private static double calcCalcium;

    private static double ccpppH = GetPh();
    private static double ccpphOld;
    private static double ccpphco3;
    private static double ccppco3;
    private static double ccppCa;
    private static double ccppOh;
    private static double ccppFh;
    private static double ccppdhco3dh;
    private static double ccppdco3dh;
    private static double ccppdcadh;
    private static double ccppdohdh;
    private static double ccppdfdh;
    private static double ccppHnew;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LSICCPPResults.this, LSICCPP.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);

        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lsiccppresults);

        View view = findViewById(R.id.lsiccppresLayout);
        Context context = this.getApplicationContext();
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.mainColor));



        final Button doneButton = findViewById(R.id.ccppDone);
        doneButton.setBackgroundColor(Color.parseColor("#a67884"));
        doneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(LSICCPPResults.this, LSICCPP.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);

                finish();
            }
        });





        // Get reference of widgets from XML layout
        final ListView lv = (ListView) findViewById(R.id.complexCCPPValues);
        // Initializing a new String Array
        String[] vals = new String[]{};
        // Create a List from String Array elements
        final List<String> vals_list = new ArrayList<String>(Arrays.asList(vals));
        // Create an ArrayAdapter from List
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, vals_list);

        // DataBind ListView with items from ArrayAdapter
        lv.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        String ph = getIntent().getStringExtra("cPH");
        String tds = getIntent().getStringExtra("cTDS");
        String cal = getIntent().getStringExtra("cCAL");
        String temp = getIntent().getStringExtra("cTEMP");
        String alk = getIntent().getStringExtra("cALK");
        int complex = getIntent().getIntExtra("COMP", 0);
        //int complex = 1;

        double tdsD = Double.parseDouble(tds);
        double tempD = Double.parseDouble(temp);
        double phD = Double.parseDouble(ph);
        double calD = Double.parseDouble(cal);
        double alkD = Double.parseDouble(alk);

        SetPh(phD);
        SetAlkalinity(alkD);
        SetPortalCalcium(calD);
        SetCalcCalcium();
        SetTDS(tdsD);
        SetTemp(tempD);

        initCCPP();


        int numSigDig = 2;
        int numDir = 0;

        if(complex == 0) {

            String saturationIndexS = "Saturation Index:  " + (Double.toString(sigDigRounder(calcLSI(), numSigDig, numDir)));
            vals_list.add(saturationIndexS);
            String CCPPoutS = "CCPP:  " + Double.toString(sigDigRounder(calcCCPP(), numSigDig, numDir));
            vals_list.add(CCPPoutS);
            String aggressiveIndexS = "Aggressive Index:  " + Double.toString(sigDigRounder(calcAI(), 3, numDir));
            vals_list.add(aggressiveIndexS);
            String ryznarIndexS = "Ryznar Index:  " + Double.toString(sigDigRounder(calcRI(), numSigDig, numDir));
            vals_list.add(ryznarIndexS);
            String dissolvedOrganicCarbonS = "Dissolved Organic Carbon:  " + Double.toString((sigDigRounder(((calcCtCO3() * 12 * 1000)), numSigDig, numDir)));
            vals_list.add(dissolvedOrganicCarbonS);
        }else{
            String saturationIndexS = "Saturation Index:  " + (Double.toString(sigDigRounder(calcLSI(), numSigDig, numDir)));
            vals_list.add(saturationIndexS);
            String CCPPoutS = "CCPP:  " + Double.toString(sigDigRounder(calcCCPP(), numSigDig, numDir));
            vals_list.add(CCPPoutS);
            String aggressiveIndexS = "Aggressive Index:  " + Double.toString(sigDigRounder(calcAI(), 3, numDir));
            vals_list.add(aggressiveIndexS);
            String ryznarIndexS = "Ryznar Index:  " + Double.toString(sigDigRounder(calcRI(), numSigDig, numDir));
            vals_list.add(ryznarIndexS);
            String dissolvedOrganicCarbonS = "Dissolved Organic Carbon:  " + Double.toString((sigDigRounder(((calcCtCO3() * 12 * 1000)), numSigDig, numDir)));
            vals_list.add(dissolvedOrganicCarbonS);
            vals_list.add("");
            vals_list.add("-ADDITIONAL VALUES BELOW-");
            vals_list.add("I:  " + Double.toString(calcI()));
            vals_list.add("A:  " + Double.toString(calcA()));
            vals_list.add("E:  " + Double.toString(calcE()));
            vals_list.add("LOG(g1):  " + Double.toString(calcLogGamma1()));
            vals_list.add("LOG(g2):  " + Double.toString(calcLogGamma2()));
            vals_list.add("g1:  " + Double.toString(calcGamma1()));
            vals_list.add("g2:  " + Double.toString(calcGamma2()));
            vals_list.add("K1:  " + Double.toString(calcK1()));
            vals_list.add("K2:  " + Double.toString(calcK2()));
            vals_list.add("Kw:  " + Double.toString(calcKw()));
            vals_list.add("Kso:  " + Double.toString(calcKso()));
            vals_list.add("pK1:  " + Double.toString(calcpK1()));
            vals_list.add("pK2:  " + Double.toString(calcpK2()));
            vals_list.add("pKw:  " + Double.toString(calcpKw()));
            vals_list.add("pKso:  " + Double.toString(calcpKso()));
            vals_list.add("H+:  " + Double.toString(calcHpositive()));
            vals_list.add("OH-:  " + Double.toString(calcOHnegative()));
            vals_list.add("CtCO3:  " + Double.toString(calcCtCO3()));
            vals_list.add("H2CO3:  " + Double.toString(calcH2CO3()));
            vals_list.add("HCO3-:  " + Double.toString(calcHCO3()));
            vals_list.add("CO32-:  " + Double.toString(calcCO32negative()));
            vals_list.add("Total Acidity:  " + Double.toString(calcTotalAcidity()));
            vals_list.add("Tot. Alk - 2 Ca:  " + Double.toString(calcTotalAlk()));
            vals_list.add("a0:  " + Double.toString(calcAlpha0()));
            vals_list.add("a1:  " + Double.toString(calcAlpha1()));
            vals_list.add("a2:  " + Double.toString(calcAlpha2()));
            vals_list.add("Alk. Check:  " + Double.toString(calcAlkalinityCheck()));
            vals_list.add("Saturation Ratio:  " + Double.toString(calcSaturationRatio()));
            vals_list.add("pHs:  " + Double.toString(calcpHs()));
        }



    }

    public static double initCCPP() {
        for (int i = 0; i < 37; i++) {
            if (i == 0) {
                ccpppH = sigDigRounder(( GetPh() ), 15, -1);

                ccpphOld = sigDigRounder(((Math.pow(10, -ccpppH) / calcGamma1()) ), 15, -1);

                ccpphco3 = sigDigRounder((calcTotalAcidity() + calcKw() / Math.pow(calcGamma1(), 2) / ccpphOld
                        - ccpphOld / (1 + Math.pow(2, Math.pow(calcGamma1(), 2) / calcK1() * ccpphOld))), 15, -1);

                ccppco3 = sigDigRounder((calcK2() / calcGamma2()) * (ccpphco3 / ccpphOld), 15, -1);

                //close
                ccppCa = sigDigRounder(( (calcKso() / Math.pow(calcGamma2(), 2) / ccppco3) ), 15, -1 );

                ccppOh = sigDigRounder( ( (calcKw() / Math.pow(calcGamma1(), 2) / ccpphOld) ), 15, -1 );

                ccppFh = sigDigRounder(( (calcTotalAlk() - 2 * ccppco3 - ccpphco3 - ccppOh + ccpphOld + 2 * ccppCa) ), 15, -1);

                ccppdhco3dh = sigDigRounder( ((-calcKw() / Math.pow(calcGamma1(), 2) / Math.pow(ccpphOld, 2) - 1)
                        * (1 + 2 * Math.pow(calcGamma1(), 2) * ccpphOld / calcK1())
                        - (2 * Math.pow(calcGamma1(), 2) / calcK1())
                        * (calcTotalAcidity() + calcKw() / Math.pow(calcGamma1(), 2) / ccpphOld - ccpphOld))
                        / Math.pow((1 + 2 * Math.pow(calcGamma1(), 2) / calcK1() * ccpphOld), 2), 15, -1 );

                ccppdco3dh = sigDigRounder(( (calcK2() / calcGamma2() * (ccppdhco3dh * ccpphOld - ccpphco3) / Math.pow(ccpphOld, 2)) ), 15, -1 );

                ccppdcadh = sigDigRounder(( calcKso() / Math.pow(calcGamma2(), 2) * (-ccppdco3dh) / Math.pow(ccppco3, 2) ), 15, -1 );

                ccppdohdh = sigDigRounder((-calcKw() / Math.pow(calcGamma1(), 2) / Math.pow(ccpphOld, 2) ), 15, -1 );

                ccppdfdh = sigDigRounder((-2 * ccppdco3dh - ccppdhco3dh - ccppdohdh + 1 + 2 * ccppdcadh ), 15, -1 );

                if (ccpphOld - ccppFh / ccppdfdh < 0) {
                    ccppHnew = sigDigRounder((ccpphOld / 10 ), 15, -1 );
                } else {
                    ccppHnew = sigDigRounder((ccpphOld - ccppFh / ccppdfdh ), 15, -1 );
                }
            } else {

                ccpppH = sigDigRounder(( (-Math.log10(ccppHnew * calcGamma1())) ), 15, -1);

                ccpphOld = sigDigRounder(((Math.pow(10, -ccpppH) / calcGamma1()) ), 15, -1);

                ccpphco3 = sigDigRounder((calcTotalAcidity() + calcKw() / Math.pow(calcGamma1(), 2) / ccpphOld
                        - ccpphOld / (1 + Math.pow(2, Math.pow(calcGamma1(), 2) / calcK1() * ccpphOld))), 15, -1);

                ccppco3 = sigDigRounder((calcK2() / calcGamma2()) * (ccpphco3 / ccpphOld), 15, -1);

                //close
                ccppCa = sigDigRounder(( (calcKso() / Math.pow(calcGamma2(), 2) / ccppco3) ), 15, -1 );

                ccppOh = sigDigRounder( ( (calcKw() / Math.pow(calcGamma1(), 2) / ccpphOld) ), 15, -1 );

                ccppFh = sigDigRounder(( (calcTotalAlk() - 2 * ccppco3 - ccpphco3 - ccppOh + ccpphOld + 2 * ccppCa) ), 15, -1);

                ccppdhco3dh = sigDigRounder( ((-calcKw() / Math.pow(calcGamma1(), 2) / Math.pow(ccpphOld, 2) - 1)
                        * (1 + 2 * Math.pow(calcGamma1(), 2) * ccpphOld / calcK1())
                        - (2 * Math.pow(calcGamma1(), 2) / calcK1())
                        * (calcTotalAcidity() + calcKw() / Math.pow(calcGamma1(), 2) / ccpphOld - ccpphOld))
                        / Math.pow((1 + 2 * Math.pow(calcGamma1(), 2) / calcK1() * ccpphOld), 2), 15, -1 );


                ccppdco3dh = sigDigRounder(( (calcK2() / calcGamma2() * (ccppdhco3dh * ccpphOld - ccpphco3) / Math.pow(ccpphOld, 2)) ), 15, -1 );


                ccppdcadh = sigDigRounder(( calcKso() / Math.pow(calcGamma2(), 2) * (-ccppdco3dh) / Math.pow(ccppco3, 2) ), 15, -1 );


                ccppdohdh = sigDigRounder((-calcKw() / Math.pow(calcGamma1(), 2) / Math.pow(ccpphOld, 2) ), 15, -1 );


                ccppdfdh = sigDigRounder((-2 * ccppdco3dh - ccppdhco3dh - ccppdohdh + 1 + 2 * ccppdcadh ), 15, -1 );


                if (ccpphOld - ccppFh / ccppdfdh < 0) {
                    ccppHnew = sigDigRounder((ccpphOld / 10 ), 15, -1 );
                } else {
                    ccppHnew = sigDigRounder((ccpphOld - ccppFh / ccppdfdh ), 15, -1 );
                }
            }
        }
        return ccppCa;
    }
    public static double calcI() {
        //should be 0.0091
        return sigDigRounder(GetTDS() * 0.000025, 15, -1);
    }

    public static double calcE() {
        //should be 83.7725077038707
        return sigDigRounder((60954.0 / (GetTemp() + 273.15 + 116.0) - 68.937), 15, -1 );
    }

    public static double calcA() {
        //should be 0.498189150027024
        return sigDigRounder((1820000 * Math.pow((calcE() * (GetTemp() + 273.15)), (-1.5) ) ), 15, -1); // C14
    }

    public static double calcLogGamma1() {
        //should be -0.0420254464238279
        return sigDigRounder((-1.0 * calcA() * Math.pow(1, 2)
                * (Math.sqrt(calcI()) / (1 + Math.sqrt(calcI())) - 0.3 * calcI())), 15, -1);
    }

    public static double calcLogGamma2() {
        //should be -0.168101785695312

        //-1*C14*2^2*(SQRT(C12)/(1+SQRT(C12))-0.3*C12)
        return sigDigRounder( -1.0 * calcA() * Math.pow(2, 2)
                * (Math.sqrt(calcI()) / (1 + Math.sqrt(calcI())) - 0.3 * calcI()), 15, -1 );
    }

    public static double calcGamma1() {
        //should be 0.907767340211057
        return sigDigRounder(Math.pow(10, calcLogGamma1()), 15, -1);
    }

    public static double calcGamma2() {
        //should be 0.679044466161855
        return sigDigRounder((Math.pow(10, calcLogGamma2())), 15, 1);
    }

    public static double calcK1() {
        return sigDigRounder((Math.pow(10, (-1 * (356.309 - 21834.4 / (273 + GetTemp()) - 126.834 * Math.log10(273 + GetTemp())
                + 0.06092 * (273 + GetTemp()) + 1684915 / Math.pow((273 + GetTemp()), 2))))), 15, -1 );
    }

    public static double calcK2() {
        return sigDigRounder((Math.pow(10, (-1 * (107.887 - 5151.8 / (273 + GetTemp()) - 38.926 * Math.log10(273 + GetTemp())
                + 0.032528 * (273 + GetTemp()) + 563713.9 / Math.pow(273 + GetTemp(), 2))))), 15, -1 );
    }

    public static double calcKw() {
        return sigDigRounder((Math.pow(10, (-1 * (-6.088 + 4471
                / (273 + GetTemp()) + 0.01706 * (273 + GetTemp()))))), 16, 1); // C22
    }

    public static double calcKso() {
        return sigDigRounder((Math.pow(10, (-1f * (171.9065 + 0.077993 * (GetTemp() + 273f) - 2839.319 / (GetTemp() + 273f)
                - 71.595 * Math.log10((GetTemp() + 273f))))) ), 15, -1 ); // C23
    }

    public static double calcpK1() {
        return sigDigRounder((-Math.log10(calcK1()) ), 15, -1);
    }

    public static double calcpK2() {
        return sigDigRounder((-Math.log10(calcK2()) ), 15, -1 );
    }

    public static double calcpKw() {
        return sigDigRounder((-Math.log10(calcKw()) ), 15, -1 );
    }

    public static double calcpKso() {
        return sigDigRounder((-Math.log10(calcKso()) ), 15, -1 );
    }

    public static double calcHpositive() {
        return sigDigRounder((Math.pow(10, pH * -1.0) / calcGamma1() ), 15, -1);
    }

    public static double calcOHnegative() {
        return sigDigRounder((calcKw() / calcHpositive() / Math.pow(calcGamma1(), 2) ), 15, -1);
    }

    public static double calcCtCO3() {
        // SUM(C33:C35)
        return sigDigRounder(((calcH2CO3() + calcHCO3() + calcCO32negative()) ), 15, -1 );
    }

    public static double calcH2CO3() {
        return sigDigRounder((Math.pow(calcGamma1(), 2) * calcHpositive() / calcK1()
                * (GetAlkalinity() / 50000.0 - calcKw() / Math.pow(calcGamma1(), 2) / calcHpositive() + calcHpositive())
                / (1.0 + 2.0 * calcK2() / calcGamma2() / calcHpositive()) ), 15, -1 );
    }

    public static double calcHCO3() {
        return sigDigRounder(((GetAlkalinity() / 50000 - calcKw() / Math.pow(calcGamma1(), 2) / calcHpositive() + calcHpositive())
                / (1 + 2 * calcK2() / calcGamma2() / calcHpositive()) ), 15, -1 );
    }

    public static double calcCO32negative() {
        return sigDigRounder((calcK2() / calcGamma2() / calcHpositive()
                * (alkalinity / 50000 - calcKw() / Math.pow(calcGamma1(), 2) / calcHpositive() + calcHpositive())
                / (1 + 2 * calcK2() / calcGamma2() / calcHpositive()) ), 15, -1 );
    }

    public static double calcTotalAcidity() {
        return sigDigRounder(( 2 * calcH2CO3() + calcHCO3() + calcHpositive() - calcKw() / calcHpositive() / Math.pow(calcGamma1(), 2) ), 15, -1 );
    }

    public static double calcTotalAlk() {
        return sigDigRounder(alkalinity / 50000 - (2 * calcCalcium / 100000), 15, -1 );
    }

    public static double calcAlpha0() {
        return sigDigRounder((calcHpositive() / (calcHpositive() + calcK1()) ), 15, -1 );
    }

    public static double calcAlpha1() {
        return sigDigRounder(( 1 - calcAlpha0() ), 15, -1 );
    }

    public static double calcAlpha2() {
        return sigDigRounder((1 - calcHpositive() / (calcHpositive() + calcK2()) ), 15, -1 );
    }

    public static double calcAlkalinityCheck() {
        return sigDigRounder(( 50000 * (calcHCO3() + 2 * calcCO32negative() + calcOHnegative() - calcHpositive()) ), 15, -1 );
    }

    public static double calcSaturationRatio() {
        return sigDigRounder(((Math.pow(calcGamma2(), 2.0) * calcCalcium / 2.5) / 40000.0 * (calcCO32negative() / calcKso()) ), 15, 0 );
    }

    public static double calcLSI() {
        return sigDigRounder((Math.log10(calcSaturationRatio())), 15, -1 );
    }

    public static double calcpHs() {
        return sigDigRounder((GetPh() - calcLSI()), 15, -1 );
    }

    public static double calcAI() {
        return sigDigRounder( (GetPh() + Math.log10(GetCalcCalcium() * GetTDS())), 15, -1 );
    }

    public static double calcRI() {
        return sigDigRounder( (2 * calcpHs() - pH), 15, -1 );
    }

    public static double calcCCPP() {
        return sigDigRounder((GetCalcCalcium()-100000*ccppCa), 15, -1 );
    }



    public static void SetTemp(double d) {
        temperature = d;
    }

    public static double GetTemp() {
        return temperature;
    }

    public static void SetPh(double d) {
        pH = d;
    }

    public static double GetPh() {
        return pH;
    }

    public static void SetTDS(double d) {
        tds = d;
    }

    public static double GetTDS() {
        return tds;
    }

    public static void SetAlkalinity(double d) {
        alkalinity = d;
    }

    public static double GetAlkalinity() {
        return alkalinity;
    }

    public static void SetPhTarget(double d) {
        phTarget = d;
    }

    public static double GetPhTarget() {
        return phTarget;
    }

    public static void SetFinalAlkalinity(double d) {
        finalAlkalinity = d;
    }

    public static double GetFinalAlkalinity() {
        return finalAlkalinity;
    }

    public static void SetCT(double d) {
        ct = d;
    }

    public static double GetCT() {
        return ct;
    }

    public static void SetAlpha1(double d) {
        alpha1 = d;
    }

    public static double GetAlpha1() {
        return alpha1;
    }

    public static void SetAlpha2(double d) {
        alpha2 = d;
    }

    public static double GetAlpha2() {
        return alpha2;
    }

    public static void SetPortalCalcium(double d) {
        portalCalcium = d;
    }

    public static double GetPortalCalcium() {
        return portalCalcium;
    }

    public static void SetCalcCalcium() {
        calcCalcium = GetPortalCalcium() * 2.5f;
    }

    public static double GetCalcCalcium() {
        return calcCalcium;
    }

    public static double sigDigRounder(double value, int nSigDig, int dir) {

        double intermediate = value/Math.pow(10,Math.floor(Math.log10(Math.abs(value)))-(nSigDig-1));

        if(dir > 0)      intermediate = Math.ceil(intermediate);
        else if (dir< 0) intermediate = Math.floor(intermediate);
        else             intermediate = Math.round(intermediate);

        double result = intermediate * Math.pow(10,Math.floor(Math.log10(Math.abs(value)))-(nSigDig-1));

        return result;

    }
}


