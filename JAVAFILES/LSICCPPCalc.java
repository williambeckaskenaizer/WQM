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
	//double saturationIndex = Math.log(calcLSI());
	double CCPP = calcCCPP();
	//double aggressiveIndex = calcAI();
	//double ryznarIndex = calcRI();
	//double dissolvedOrganicCarbon = calcCtCO3()*12*1000;

	// System.out.println(CCPPpH(12));
	// System.out.println(CCPPhOld(12));
	// System.out.println(CCPPHCO3(12));

	//System.out.println("Saturation Index: " + saturationIndex);
	System.out.println("CCPP: " + CCPP);
	//System.out.println("Aggressive Index: " + aggressiveIndex);
	//System.out.println("Ryznar Index: " + ryznarIndex);
	//System.out.println("Dissolved Organic Carbon: " + dissolvedOrganicCarbon);
	System.out.println("----------------------------------------");
	System.out.println("mister: " + masterCCPP());

// 	CCPPhOld();
// 	CCPPHCO3();
// 	CCPPCO3();
// 	CCPPCa();
// 	CCPPOH();
// 	CCPPFH();
// 	CCPPdHCO3dH();
// 	CCPPdCO3dH();
// 	CCPPdCadH();
// 	CCPPdOHdH();
// 	CCPPdFdH();
	//CCPPHnew(0);
 }

public static double masterCCPP(){
	double ccpppH = 0;
		double ccpphOld = 0;
		double ccpphco3;
		double ccppco3;
		double ccppCa = 0;
		double ccppOh;
		double ccppFh = 0;
		double ccppdhco3dh;
		double ccppdco3dh;
		double ccppdcadh ;
		double ccppdohdh;
		double ccppdfdh = 0;
		double ccppHnew;
	for(int i = 0; i < 37; i++){
		System.out.println("loup :3");
		 ccpppH = -(Math.log(CCPPHnew(ccpphOld, ccppFh, ccppdfdh) * calcGamma1()));
		 ccpphOld = (Math.pow(10, ccpppH)/calcGamma1());
		 ccpphco3 = (calcTotalAcidity() + calcKw()/Math.pow(calcGamma1(), 2)/  ccpphOld - ccpphOld/(1 + Math.pow(2, Math.pow(calcGamma1(), 2 )/calcK1()*ccpphOld)));
		 ccppco3 = (calcK2()/calcGamma2())*( ccpphco3/ccpphOld);
		 ccppCa = (calcKso()/Math.pow(calcGamma2(), 2)/ccppco3);
		 ccppOh = (calcKw()/(Math.pow(calcGamma1(),2)/ccpphOld));
		 ccppFh = (calcTotalAlk() - (2*ccppco3 - ccpphco3 - ccppOh + ccpphOld + 2*ccppCa) );
		 ccppdhco3dh = ((-calcKw()/(Math.pow(calcGamma1(),2)/Math.pow(ccpphOld,2)-1)
		*(1+2*Math.pow(calcGamma1(), 2)*ccpphOld/calcK1())-(2*Math.pow(calcGamma1(), 2)
		/calcK1())*calcTotalAcidity()+calcKw()/Math.pow(calcGamma1(), 2)/ccpphOld - ccpphOld)
		/(1+2*Math.pow(calcGamma1(), 2)/Math.pow(calcK1() * ccpphOld, 2)));
		 ccppdco3dh = (calcK2()/calcGamma2()*(ccppdhco3dh*ccpphOld-ccpphco3)/Math.pow(ccpphOld, 2));
		 ccppdcadh = calcKso()/Math.pow(calcGamma2(), 2)*(-ccppdco3dh)/ccppco3;
		 ccppdohdh = -calcKw()/Math.pow(calcKw(), 2)/Math.pow(ccpphOld, 2);
		 ccppdfdh = -2*ccppdco3dh - ccppdhco3dh - ccppdohdh + 1 + 2* ccppdcadh;
		 ccppHnew = CCPPHnew(ccpphOld, ccppFh, ccppdfdh);
		 
	}
	return ccppCa;
}
public static double CCPPHnew(double hOld, double fh, double dfdh){
	//FORM: IF((K12-P12/U12)<0,K12/10,K12-P12/U12)
	double x;
		if(hOld-fh/dfdh < 0){
			x = hOld/10;
		}else{
			x = (hOld-fh)/(dfdh);
		}
	return x;
}


//CCPP CALCULATION FORMULAS
//what if i just jammed this all into one giant method.
//wait. tail recursion, duh...
//wait, tail recursion is super complicated. not duh...
//wait maybe it's not.

//CCPPHnew(double hOld, double fh, double dfdh, int index)
//CCPPdCO3dH(double dHCO3dH, double hOld, double hCO3, int index)


// return (CCPPHnew(CCPPhOld(CCPPpH(index-1)), CCPPFH(CCPPCO3(CCPPHCO3(CCPPhOld(CCPPpH(index-1))), CCPPhOld(CCPPpH(index-1))),CCPPHCO3(CCPPhOld(CCPPpH(index-1))),CCPPOH(CCPPhOld(CCPPpH(index-1))),CCPPhOld(CCPPpH(index-1)),
// CCPPCa(CCPPCO3(CCPPHCO3(CCPPhOld(CCPPpH(index-1))), CCPPhOld(CCPPpH(index-1))))), CCPPdFdH(CCPPdCO3dH(CCPPhOld(CCPPpH(index-1)), CCPPhOld(CCPPpH(index-1)), CCPPhOld(CCPPpH(index-1))),
//  CCPPdHCO3dH(CCPPhOld(CCPPpH(index-1))), CCPPdOHdH(CCPPhOld(CCPPpH(index-1))), CCPPdCadH(CCPPCO3(CCPPHCO3(CCPPhOld(CCPPpH(index-1))), CCPPhOld(CCPPpH(index-1))),
// 	CCPPCO3(CCPPHCO3(CCPPhOld(CCPPpH(index-1))), CCPPhOld(CCPPpH(index-1)))))))*calcGamma1();



// public static double CCPPpH(int index){
// 	//FORM: -LOG(V12-47*C18)
// 	double x = 0;
// if(index > 0){
// 	System.out.println("loup: " +index);
// 	return -Math.log(CCPPhnew(index)*calcLogGamma1())
// 			 // x = x*calcGamma1();
// 	}else{
// 		return x;
// 	}
// }
// public static double CCPPhOld(double pH){
// 	//FORM: (10^(-J12-49))/C18
// 	double x = 0;
// 			x = (Math.pow(10, pH)/calcGamma1());
// 	return x;
// }
//
// public static double CCPPHCO3(double hOld){
// 	//FORM: (C36 + C22 / C18^2 / K12-49 - K12-49)/(1 + 2^C18^2/C20*K12-49) ... oof
// 	double x = 0;
// 	double fHold = hOld;
// 			x = (calcTotalAcidity() + calcKw()/Math.pow(calcGamma1(), 2)/  hOld - hOld/(1 + Math.pow(2, Math.pow(calcGamma1(), 2 )/calcK1()*hOld)));
// 	return x;
// }
// public static double CCPPCO3(double hCO3, double hOld){
// 	//FORM: $C$21/$C$19*L12-49/K12-49
// 	double x;
// 		x = (calcK2()/calcGamma2())*( hCO3/hOld);
// 	return x;
// }
// public static double CCPPCa(double cO3){
// 	//FORM: $C$23/$C$19^2/M13-49
// 	double x = 0;
// 			x = (calcKso()/Math.pow(calcGamma2(), 2)/cO3);
// 	return x;
// }
// public static double CCPPOH(double hOld){
// 	//FORM: $C$22/$C$18^2/K12
// 	double x = 0;
// 			x = (calcKw()/(Math.pow(calcGamma1(),2)/hOld));
// 	return x;
// }
// public static double CCPPFH(double cO3, double hCO3, double oH, double hOld, double cA){
// 	//FORM: $C$37-2*M12-L12-O12+K12+2*N12
// 	double x = 0;
// 			x = (calcTotalAlk() - (2*cO3 - hCO3 - oH + hOld + 2*cA) );
// 	return x;
// }
// public static double CCPPdHCO3dH(double hOld){
// 	//FORM: ((-$C$22/$C$18^2/K12^2-1)*(1+2*$C$18^2*K12/$C$20)-(2*$C$18^2/$C$20)*($C$36+$C$22/$C$18^2/K12-K12))/(1+2*$C$18^2/$C$20*K12)^2\
// 	double x = 0;
// 			x = ((-calcKw()/(Math.pow(calcGamma1(),2)/Math.pow(hOld,2)-1)
// 			*(1+2*Math.pow(calcGamma1(), 2)*hOld/calcK1())-(2*Math.pow(calcGamma1(), 2)
// 			/calcK1())*calcTotalAcidity()+calcKw()/Math.pow(calcGamma1(), 2)/hOld - hOld)
// 			/(1+2*Math.pow(calcGamma1(), 2)/Math.pow(calcK1() * hOld, 2)));
// 	return x;
// }
// public static double CCPPdCO3dH(double dHCO3dH, double hOld, double hCO3){
// 	//FORM: $C$21/$C$19*(Q12*K12-L12)/K12^2
// 	double x = 0.0;
// 			x = (calcK2()/calcGamma2()*(dHCO3dH*hOld-hCO3)/Math.pow(hOld, 2));
// 	return x;
// }
// public static double CCPPdCadH(double dCO3dH, double cO3){
// 	//FORM: $C$23/$C$19^2*(-R12)/M12^2
// 	double x = 0.0;
// 			x = calcKso()/Math.pow(calcGamma2(), 2)*(-dCO3dH)/cO3;
// 	return x;
// }
// public static double CCPPdOHdH(double hOld){
// 	double x = 0.0;
// 			x = -calcKw()/Math.pow(calcKw(), 2)/Math.pow(hOld, 2);
// 	return x;
// }
// public static double CCPPdFdH(double dCO3dH, double dHCO3dH, double dOdH, double cAdH){
// 	//FORM: -2*R12-Q12-T12+1+2*S12
// 	double x = 0;
// 			x = -2*dCO3dH - dHCO3dH - dOdH + 1 + 2* cAdH;
// 	return x;
// }
// public static double CCPPHnew(double hOld, double fh, double dfdh){
// 	//FORM: IF((K12-P12/U12)<0,K12/10,K12-P12/U12)
// 	double x;
// 		if(hOld-fh/dfdh < 0){
// 			x = hOld/10;
// 		}else{
// 			x = (hOld-fh)/(dfdh);
// 		}
// 	return x;
// }
// public static double CCPPFlag(){
// //FORM: IF(ABS((J13-J12)/J12)>$J$8,1,0)
// double x = 0;
// for(int i = 12; i < 37; i++){
// 	if(Math.abs((CCPPpH(i+1) - CCPPpH(i)/CCPPpH(i))) > 0.000001){
// 		x = 1;
// 	}else{
// 		x = 0;
// 	}
// }
// return x;
// }


/* END CCPP METHODS */


//The guts of the late GetFormula method will go here.
public static double calcI(){
	return Math.round(GetTDS()*0.000025f * 10000d)/10000d;
}
public static double calcE(){
	return 60954.0/(GetTemp() + 273.15+116.0)-68.937;
}
public static double calcA(){
	return 1820000*Math.pow((calcE()*(GetTemp()+273.15)),(-1.5)); //C14
}
public static double calcLogGamma1(){
	return -1.0f*calcA()*Math.pow(1f,2f)*(Math.sqrt(calcI())/(1f+Math.sqrt(calcI())))-0.3f*calcI();
}
public static double calcLogGamma2(){
	return -1.0f*calcA()*Math.pow(2f, 2f)*(Math.sqrt(calcI())/(1f+Math.sqrt(calcI()))-0.3f*calcI());
}
public static double calcGamma1(){
	return Math.round(Math.pow(10, calcLogGamma1())*10000d)/10000d;
}
public static double calcGamma2(){
	return Math.pow(10, calcLogGamma2());
}
public static double calcK1(){
	return Math.pow(10f, (-1f* (356.309-21834.4/(273f+GetTemp()) - 126.834 * Math.log10(273f+GetTemp()) + 0.06092 * (273+GetTemp()) +1684915/Math.pow((273f+GetTemp()),2))));
}
public static double calcK2(){
	return Math.pow(10, (-1*(107.887-5151.8/(273+GetTemp())-38.926*Math.log10(273+GetTemp())+0.032528*(273+GetTemp())+563713.9/Math.pow(273+GetTemp(), 2))));
}
public static double calcKw(){
	return Math.pow(10, (-1f*(-6.088+4471/(273+GetTemp())+0.01706*(273+GetTemp())))); //C22
}
public static double calcKso(){
	return Math.pow(10,(-1f*(171.9065+0.077993*(GetTemp()+273f)-2839.319/(GetTemp()+273f)-71.595*Math.log10((GetTemp()+273f))))); //C23
}
public static double calcpK1(){
	return -Math.log10(calcK1());
}
public static double calcpK2(){
	return -Math.log10(calcK2());
}
public static double calcpKw(){
	return -Math.log10(calcKw());
}
public static double calcpKso(){
	return -Math.log10(calcKso());
}
public static double calcHpositive(){
	return Math.pow(10, pH*-1)/calcGamma1();
}
public static double calcOHnegative(){
	return calcKw()/calcHpositive()/Math.pow(calcGamma1(),2);
}
public static double calcH2CO3(){
	return Math.pow(calcGamma1(), 2f*calcHpositive()/calcK1()*(alkalinity/50000f-calcKw()/Math.pow((calcGamma1()),2)/calcHpositive()+calcHpositive())/(1f+2f*calcK2()/calcGamma2()/calcHpositive()));
}
public static double calcHCO3(){
	return (alkalinity/50000f-calcKw()/Math.pow((calcGamma1()), 2)/calcHpositive()+calcHpositive())/(1f+2f*calcK2()/calcGamma2()/calcHpositive());
}
public static double calcCO32negative(){
	return calcK2()/calcGamma2()/calcHpositive()*(alkalinity/50000-calcKw()/Math.pow(calcGamma1(), 2)/calcHpositive()+calcHpositive())/(1+2*calcK2()/calcGamma2()/calcHpositive());
}
public static double calcTotalAcidity(){
	return 2*calcH2CO3()+calcHCO3()+calcHpositive()-calcKw()/calcHpositive()/Math.pow(calcGamma1(), 2);
}
public static double calcCtCO3(){
	return (calcH2CO3() + calcHCO3() + calcCO32negative());
}
public static double calcTotalAlk(){
	return alkalinity/50000-2*calcCalcium/100000;
}
public static double calcAlpha0(){
	return calcHpositive()/(calcHpositive()+calcK1());
}
public static double calcAlpha1(){
	return 1f-calcAlpha0();
}
public static double calcAlpha2(){
	return 1f-calcHpositive()/(calcHpositive()+calcK2());
}
public static double calcAlkalinityCheck(){
	return 50000f*(calcHCO3()+2f*calcCO32negative()+calcOHnegative()-calcHpositive());
}
public static double calcSaturationRatio(){
	return (Math.pow(calcGamma2(), 2.0)*calcCalcium/2.5)/40000.0*(calcCO32negative()/calcKso());
}
public static double calcLSI(){
	return Math.log(calcSaturationRatio());
}
public static double calcpHs(){
	return pH-calcLSI();
}
public static double calcAI(){
	return pH + Math.log(calcCalcium * tds);
}
public static double calcRI(){
	return 2*calcpHs()-pH;
}
public static double calcCCPP(){
	return calcCalcium-(100000.0 * masterCCPP());
}

/*


END OF CCPP CALCULATIONS/FORMULAS


*/


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
}
