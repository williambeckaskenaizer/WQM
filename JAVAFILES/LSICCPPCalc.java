import java.util.Arrays;
import java.math.*;

public class LSICCPPCalc {

//Chemistry Values
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

//Other variables
/* X */

public static void main(String args[]){
	//initialize manual entry table
	SetPh(8.08);
	SetAlkalinity(190);
	SetPortalCalcium(61.2);
	SetCalcCalcium();
	SetTDS(364);
	SetTemp(18);

//CALCULATED VALUES
	double saturationIndex = GetFormula("calcLSI");
	double CCPP = GetFormula("calcCCPP");
	double aggressiveIndex = GetFormula("calcAI");
	double ryznarIndex = GetFormula("calcRI");
	double dissolvedOrganicCarbon = GetFormula("calcCtCO3")*12*1000;

	// System.out.println(CCPPpH(12));
	// System.out.println(CCPPhOld(12));
	// System.out.println(CCPPHCO3(12));

	System.out.println("Saturation Index: " + saturationIndex);
	System.out.println("CCPP: " + CCPP);
	System.out.println("Aggressive Index: " + aggressiveIndex);
	System.out.println("Ryznar Index: " + ryznarIndex);
	System.out.println("Dissolved Organic Carbon: " + dissolvedOrganicCarbon);
	System.out.println("----------------------------------------");
	System.out.println("Calc Saturation Ratio: " + GetFormula("calcSaturationRatio"));
	System.out.println("calcCO32negative: " + GetFormula("calcCO32negative"));
	System.out.println("calcHpositive: " + GetFormula("calcHpositive"));
	System.out.println("calcK2: " + GetFormula("calcK2"));
	System.out.println("calcGamma2: " + GetFormula("calcGamma2"));
	System.out.println("calcKso: " + GetFormula("calcKso"));
	System.out.println("calcCalcium: " + GetFormula("calcCalcium"));


}



//CCPP CALCULATION FORMULAS

public static double CCPPpH(int index){
	//FORM: -LOG(V12-47*C18)
	double x = 0;
	if(index == 0){
		for(int i = 0; i < 37; i++){
			x = -(Math.log10(CCPPHnew() * GetFormula("calcGamma1")));
		}
	}else{
			for(int i = 0; i < index; i++){
				x = -(Math.log10(CCPPHnew() * GetFormula("calcGamma1")));
			}
		}
	return x;
}
public static double CCPPhOld(int index){
	//FORM: (10^(-J12-49))/C18
	double x = 0;
	if(index == 0){
		for(int i = 0; i < 37; i++){
			x = (Math.pow(10, CCPPpH(i))/GetFormula("calcGamma1"));
		}
		}else{
			for(int i = 0; i < index; i++){
				x = (Math.pow(10, CCPPpH(i))/GetFormula("calcGamma1"));
			}
		}

	return x;
}

public static double CCPPHCO3(int index){
	//FORM: (C36 + C22 / C18^2 / K12-49 - K12-49)/(1 + 2^C18^2/C20*K12-49) ... oof
	double x = 0;
		for(int i = 0; i < 37; i++){
			x = (GetFormula("calcTotalAcidity") + GetFormula("calcKw")/Math.pow(GetFormula("calcGamma1"), 2)/  CCPPhOld(i) - CCPPhOld(i)/(1 + Math.pow(2, Math.pow(GetFormula("calcGamma1"), 2 )/GetFormula("calcK1")*CCPPhOld(i))));
		}
	return x;
}
public static double CCPPCO3(int index){
	//FORM: $C$21/$C$19*L12-49/K12-49
	double x = 0;
	for(int i = 0; i < 37; i++){
		x = (GetFormula("calcK2")/GetFormula("calcGamma2"))*( CCPPHCO3(i)/CCPPhOld(i));
	}
	return x;
}
public static double CCPPCa(int index){
	//FORM: $C$23/$C$19^2/M13-49
	double x = 0;
		for(int i = 0; i < 37; i++){
			x = (GetFormula("calcKso")/Math.pow(GetFormula("calcGamma2"), 2)/CCPPCO3(i));
		}
	return x;
}
public static double CCPPOH(int index){
	//FORM: $C$22/$C$18^2/K12
	double x = 0;
		for(int i = 0; i < 37; i++){
			x = (GetFormula("calcKw")/(Math.pow(GetFormula("calcGamma1"),2)/CCPPhOld(i)));
		}
	return x;
}
public static double CCPPFH(int index){
	//FORM: $C$37-2*M12-L12-O12+K12+2*N12
	double x = 0;

		for(int i = 0; i < 37; i++){
			x = (GetFormula("calcTotalAlk") - (2*CCPPCO3(i) - CCPPHCO3(i) - CCPPOH(i) + CCPPhOld(i) + 2*CCPPCa(i)) );
		}
	return x;
}
public static double CCPPdHCO3dH(int index){
	//FORM: ((-$C$22/$C$18^2/K12^2-1)*(1+2*$C$18^2*K12/$C$20)-(2*$C$18^2/$C$20)*($C$36+$C$22/$C$18^2/K12-K12))/(1+2*$C$18^2/$C$20*K12)^2\
	double x = 0;
		for(int i = 0; i < 37; i++){
			x = (-GetFormula("calcKw")/(Math.pow(GetFormula("calcGamma1"),2)/Math.pow(CCPPhOld(i),2)-1)
			*(1+2*Math.pow(GetFormula("calcGamma1"), 2)*CCPPhOld(i)/GetFormula("calcK1"))-(2*Math.pow(GetFormula("calcGamma1"), 2)
			/GetFormula("calcK1"))*(GetFormula("calcTotalAcidity")+GetFormula("calcKw")/Math.pow(GetFormula("calcGamma1"), 2)/CCPPhOld(i) - CCPPhOld(i))
			/(1+2*Math.pow(GetFormula("calcGamma1"), 2)/Math.pow(GetFormula("calcK1" ) * CCPPhOld(i), 2)));
		}
	return x;
}
public static double CCPPdCO3dH(int index){
	//FORM: $C$21/$C$19*(Q12*K12-L12)/K12^2
	double x = 0.0;
		for(int i = 0; i < 37; i++){
			x = (GetFormula("calcK2")/GetFormula("calcGamma2")*(CCPPdHCO3dH(i)*CCPPhOld(i)-CCPPHCO3(i))/Math.pow(CCPPhOld(i), 2));
		}
	return x;
}
public static double CCPPdCadH(int index){
	//FORM: $C$23/$C$19^2*(-R12)/M12^2
	double x = 0.0;
		for(int i = 0; i < 37; i++){
			x = GetFormula("calcKso")/Math.pow(GetFormula("calcGamma2"), 2)*(-CCPPdCO3dH(i))/CCPPCO3(i);
		}
	return x;
}
public static double CCPPdOHdH(int index){
	//FORM: -$C$22/$C$18^2/K12^2
	double x = 0.0;
		for(int i = 0; i < 37; i++){
			x = -GetFormula("calcKw")/Math.pow(GetFormula("calcKw"), 2)/Math.pow(CCPPhOld(i), 2);
		}
	return x;
}
public static double CCPPdFdH(int index){
	//FORM: -2*R12-Q12-T12+1+2*S12
	double x = 0;
		for(int i = 0; i < 37; i++){
			x = -2*CCPPdCO3dH(i) - CCPPdHCO3dH(i) - CCPPdOHdH(i) + 1 + 2* CCPPdCadH(i);
		}
	return x;
}
public static double CCPPHnew(){
	//FORM: IF((K12-P12/U12)<0,K12/10,K12-P12/U12)
	double x = 0;
	for(int i = 0; i < 37; i++){
		if(CCPPhOld(i)-CCPPFH(i)/CCPPdFdH(i) < 0){
			x = CCPPhOld(i)/10;
		}else{
			x = (CCPPhOld(i)-CCPPFH(i))/(CCPPdFdH(i));
		}
	}
	return x;
}
public static double CCPPFlag(){
//FORM: IF(ABS((J13-J12)/J12)>$J$8,1,0)
double x = 0;
for(int i = 12; i < 34; i++){
	if(Math.abs((CCPPpH(i+1) - CCPPpH(i)/CCPPpH(i))) > 0.000001){
		x = 1;
	}else{
		x = 0;
	}

}
return x;
}

public static void SetTemp(double f){
								temperature = f;
}
public static double GetTemp(){
								return temperature;
}

public static void SetPh(double f){
								pH = f;
}
public static double GetPh(){
								return pH;
}

public static void SetTDS(double f){
								tds = f;
}
public static double GetTDS(){
								return tds;
}

public static void SetAlkalinity(double f){
								alkalinity = f;
}
public static double GetAlkalinity(){
								return alkalinity;
}

public static void SetPhTarget(double f){
								phTarget = f;
}
public static double GetPhTarget(){
								return phTarget;
}

public static void SetFinalAlkalinity(double f){
								finalAlkalinity = f;
}
public static double GetFinalAlkalinity(){
								return finalAlkalinity;
}

public static void SetCT(double f){
								ct = f;
}
public static double GetCT(){
								return ct;
}

public static void SetAlpha1(double f){
								alpha1 = f;
}
public static double GetAlpha1(){
								return alpha1;
}

public static void SetAlpha2(double f){
								alpha2 = f;
}
public static double GetAlpha2(){
								return alpha2;
}

public static void SetPortalCalcium(double f){
								portalCalcium = f;
}
public static double GetPortalCalcium(){
								return portalCalcium;
}
public static void SetCalcCalcium(){
								calcCalcium = GetPortalCalcium()*2.5f;
}
public static double GetCalcCalcium(){
								return calcCalcium;
}




public static double GetFormula(String formId){

								//This next section will contain an insane number of variables. I'll do my best to keep them organized.
								//I'll try and stick to a naming convention. "Calc" means they're from the calc page, Portal means they're from the portal page.

								//rounding example
								//Math.round(value * 100000d) / 100000d\


								//double Ca = CCPPCa(0);


								double calcI = Math.round(GetTDS()*0.000025f * 10000d)/10000d; //C12
								double calcE = 60954f/(GetTemp() + 273.15f+116f)-68.937f; //C13
								double calcA = 1820000f*Math.pow((calcE*(GetTemp()+273.15f)),(-1.5f)); //C14

								double calcLogGamma1 = -1.0f*calcA*Math.pow(1f,2f)*(Math.sqrt(calcI)/(1f+Math.sqrt(calcI)))-0.3f*calcI;  //C15
								double calcLogGamma2 = -1.0f*calcA*Math.pow(2f, 2f)*(Math.sqrt(calcI)/(1f+Math.sqrt(calcI))-0.3f*calcI); //C16

								double calcGamma1 = Math.round(Math.pow(10, calcLogGamma1)*10000d)/10000d; //C18
								double calcGamma2 = Math.pow(10, calcLogGamma2); //C19

								double calcK1 = Math.pow(10f, (-1f* (356.309-21834.4/(273f+GetTemp()) - 126.834 * Math.log10(273f+GetTemp()) + 0.06092 * (273+GetTemp()) +1684915/Math.pow((273f+GetTemp()),2)))); //C20
								double calcK2 = Math.pow(10, (-1*(107.887-5151.8/(273+GetTemp())-38.926*Math.log10(273+GetTemp())+0.032528*(273+GetTemp())+563713.9/Math.pow(273+GetTemp(), 2)))); //C21
								double calcKw = Math.pow(10, (-1f*(-6.088+4471/(273+GetTemp())+0.01706*(273+GetTemp())))); //C22
								double calcKso = Math.pow(10,(-1f*(171.9065+0.077993*(GetTemp()+273f)-2839.319/(GetTemp()+273f)-71.595*Math.log10((GetTemp()+273f))))); //C23

								double calcpK1 = -Math.log10(calcK1); //C24
								double calcpK2 = -Math.log10(calcK2); //C25
								double calcpKw = -Math.log10(calcKw); //C26
								double calcpKso = -Math.log10(calcKso); //C27

								double calcHpositive = Math.pow(10, pH*-1)/calcGamma1; //C28
								double calcOHnegative = calcKw/calcHpositive/Math.pow(calcGamma1,2); //C29
								double calcH2CO3 = Math.pow(calcGamma1, 2f*calcHpositive/calcK1*(alkalinity/50000f-calcKw/Math.pow((calcGamma1),2)/calcHpositive+calcHpositive)/(1f+2f*calcK2/calcGamma2/calcHpositive)); //C33
								double calcHCO3 = (alkalinity/50000f-calcKw/Math.pow((calcGamma1), 2)/calcHpositive+calcHpositive)/(1f+2f*calcK2/calcGamma2/calcHpositive); //C34
								double calcCO32negative = calcK2/calcGamma2/calcHpositive*(alkalinity/50000-calcKw/Math.pow(calcGamma1, 2)/calcHpositive+calcHpositive)/(1+2*calcK2/calcGamma2/calcHpositive); //C35
								double calcTotalAcidity = 2*calcH2CO3+calcHCO3+calcHpositive-calcKw/calcHpositive/Math.pow(calcGamma1, 2); //C36
								double calcCtCO3 = (calcH2CO3 + calcHCO3 + calcCO32negative); //C32
								double calcTotalAlk = alkalinity/50000-2*calcCalcium/100000; //C37
								double calcAlpha0 = calcHpositive/(calcHpositive+calcK1); //C39
								double calcAlpha1 = 1f-calcAlpha0; //C40
								double calcAlpha2 = 1f-calcHpositive/(calcHpositive+calcK2);//C41
								double calcAlkalinityCheck = 50000f*(calcHCO3+2f*calcCO32negative+calcOHnegative-calcHpositive); //C43

								double calcSaturationRatio = (Math.pow(calcGamma2, 2.0)*calcCalcium/2.5)/40000.0*(calcCO32negative/calcKso); //C44
								double calcLSI = Math.log(calcSaturationRatio); //C46
								double calcpHs = pH-calcLSI; //C47
								double calcCCPP = calcCalcium-100000f * CCPPCa(37);/* * Ca*/; //C48
								double calcAI = pH + Math.log(calcCalcium * tds);//C49
								double calcRI = 2*calcpHs-pH; //C50

								switch(formId) {
								case "calcI": return calcI;
								case "calcE": return calcE;
								case "calcA": return calcE;
								case "calcLogGamma1": return calcLogGamma1;
								case "calcLogGamma2": return calcLogGamma2;
								case "calcGamma1": return calcGamma1;
								case "calcGamma2": return calcGamma2;
								case "calcK1": return calcK1;
								case "calcK2": return calcK2;
								case "calcKw": return calcKw;
								case "calcKso": return calcKso;
								case "calcpK1": return calcpK1;
								case "calcpK2": return calcpK2;
								case "calcpKw": return calcpKw;
								case "calcpKso": return calcpKso;
								case "calcHpositive": return calcHpositive;
								case "calcOHnegative": return calcOHnegative;
								case "calcH2CO3": return calcH2CO3;
								case "calcHCO3": return calcHCO3;
								case "calcCO32negative": return calcCO32negative;
								case "calcTotalAcidity": return calcTotalAcidity;
								case "calcCtCO3": return calcCtCO3;
								case "calcTotalAlk": return calcTotalAlk;
								case "calcAlpha0": return calcAlpha0;
								case "calcAlpha1": return calcAlpha1;
								case "calcAlpha2": return calcAlpha2;
								case "calcAlkalinityCheck": return calcAlkalinityCheck;
								case "calcSaturationRatio": return calcSaturationRatio;
								case "calcHC03": return calcHCO3;
								case "calcLSI": return calcLSI;
								case "calcpHs": return calcpHs;
								case "calcCCPP": return calcCCPP;
								case "calcAI": return calcAI;
								case "calcRI": return calcRI;
								default: return 0.0f;
								}
}


}
