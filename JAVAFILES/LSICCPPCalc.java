import java.text.DecimalFormat;
public class LSICCPPCalc {

	// Chemistry Values
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

	double CCPP;

	// Other variables
	/* X */

	public static void main(String args[]) {
		// initialize manual entry table
		SetPh(8.08);
		SetAlkalinity(190);
		SetPortalCalcium(61.2);
		SetCalcCalcium();
		SetTDS(364);
		SetTemp(18);
		initCCPP();
		//PrintFormulas();

		// CALCULATED VALUES
		// double saturationIndex = Math.log(calcLSI());
		
		// double aggressiveIndex = calcAI();
		// double ryznarIndex = calcRI();
		// double dissolvedOrganicCarbon = calcCtCO3()*12*1000;

		// System.out.println(CCPPpH(12));
		// System.out.println(CCPPhOld(12));
		// System.out.println(CCPPHCO3(12));

		// System.out.println("Saturation Index: " + saturationIndex);
		System.out.println("CCPP: " + calcCCPP());
		// System.out.println("Aggressive Index: " + aggressiveIndex);
		// System.out.println("Ryznar Index: " + ryznarIndex);
		// System.out.println("Dissolved Organic Carbon: " + dissolvedOrganicCarbon);
		System.out.println("----------------------------------------");
	}

	public static double initCCPP() {
		for (int i = 0; i < 37; i++) {
			if (i == 0) {
				ccpppH = GetPh();
				System.out.println("ph: " + ccpppH);

				ccpphOld = Math.round(((Math.pow(10, -ccpppH) / calcGamma1()) )*100000000000d)/100000000000d;
				System.out.println("hOld: " + ccpphOld);

				ccpphco3 = Math.round(( (calcTotalAcidity() + calcKw() / Math.pow(calcGamma1(), 2) / ccpphOld
						- ccpphOld / (1 + Math.pow(2, Math.pow(calcGamma1(), 2) / calcK1() * ccpphOld))) )*10000d)/10000d;
				System.out.println("HCO3: " + (ccpphco3));
					
				ccppco3 = Math.round(( (calcK2() / calcGamma2()) * (ccpphco3 / ccpphOld) )*10000000d)/10000000d;
				System.out.println("CO3: " + ccppco3);
				
				//close 
				ccppCa = Math.round(( (calcKso() / Math.pow(calcGamma2(), 2) / ccppco3) )*1000000d)/1000000d;
				System.out.println("Ca: " + ccppCa);
				ccppOh = Math.round(( (calcKw() / Math.pow(calcGamma1(), 2) / ccpphOld) )*1000000000d)/1000000000d;
				System.out.println("OH: " + ccppOh);
				
				ccppFh = Math.round(( (calcTotalAlk() - 2 * ccppco3 - ccpphco3 - ccppOh + ccpphOld + 2 * ccppCa) )*10000000000000000d)/10000000000000000d;
				System.out.println("Fh: " + (ccppFh));

				ccppdhco3dh = Math.round(( ((-calcKw() / Math.pow(calcGamma1(), 2) / Math.pow(ccpphOld, 2) - 1)
						* (1 + 2 * Math.pow(calcGamma1(), 2) * ccpphOld / calcK1())
						- (2 * Math.pow(calcGamma1(), 2) / calcK1())
								* (calcTotalAcidity() + calcKw() / Math.pow(calcGamma1(), 2) / ccpphOld - ccpphOld))
						/ Math.pow((1 + 2 * Math.pow(calcGamma1(), 2) / calcK1() * ccpphOld), 2) )*100d)/100d;
				System.out.println("dHCO3dH: " + (ccppdhco3dh));

				ccppdco3dh = Math.round(( (calcK2() / calcGamma2() * (ccppdhco3dh * ccpphOld - ccpphco3) / Math.pow(ccpphOld, 2)) )*100d)/100d;
				System.out.println("dCO3dH: " + (ccppdco3dh));

				ccppdcadh = Math.round(( calcKso() / Math.pow(calcGamma2(), 2) * (-ccppdco3dh) / Math.pow(ccppco3, 2) )*100d)/100d;
				System.out.println("dCadH: " + (ccppdcadh));

				ccppdohdh = Math.round((-calcKw() / Math.pow(calcGamma1(), 2) / Math.pow(ccpphOld, 2) )*100d)/100d;
				System.out.println("dOHdH: " + ccppdohdh);

				ccppdfdh = Math.round((-2 * ccppdco3dh - ccppdhco3dh - ccppdohdh + 1 + 2 * ccppdcadh )*1000d)/1000d;
				System.out.println("dFdH: " + (ccppdfdh));

				if (ccpphOld - ccppFh / ccppdfdh < 0) {
					ccppHnew = Math.round((ccpphOld / 10 )*10000000000000d)/10000000000000d;
				} else {
					ccppHnew = Math.round((ccpphOld - ccppFh / ccppdfdh )*10000000000000d)/10000000000000d;
				}
				System.out.println("Hnew: " + ccppHnew);
				System.out.println("First Round Complete");
			} else {

				ccpppH = Math.round(( (-Math.log10(ccppHnew * calcGamma1())) )*100000d)/100000d;

				ccpphOld = Math.round(((Math.pow(10, -ccpppH) / calcGamma1()) )*100000000000d)/100000000000d;
				ccpphco3 = Math.round(( (calcTotalAcidity() + calcKw() / Math.pow(calcGamma1(), 2) / ccpphOld
						- ccpphOld / (1 + Math.pow(2, Math.pow(calcGamma1(), 2) / calcK1() * ccpphOld))) )*10000d)/10000d;
				ccppco3 = Math.round(( (calcK2() / calcGamma2()) * (ccpphco3 / ccpphOld) )*10000000d)/10000000d;
				
				//close 
				ccppCa = Math.round(( (calcKso() / Math.pow(calcGamma2(), 2) / ccppco3) )*100000d)/100000d;

				//$C$22/$C$18^2/K12
				ccppOh = Math.round(( (calcKw() / Math.pow(calcGamma1(), 2) / ccpphOld) )*1000000000d)/1000000000d;

				
				ccppFh = Math.round(( (calcTotalAlk() - 2 * ccppco3 - ccpphco3 - ccppOh + ccpphOld + 2 * ccppCa) )*10000000000000000d)/10000000000000000d;
				ccppdhco3dh = Math.round(( ((-calcKw() / Math.pow(calcGamma1(), 2) / Math.pow(ccpphOld, 2) - 1)
						* (1 + 2 * Math.pow(calcGamma1(), 2) * ccpphOld / calcK1())
						- (2 * Math.pow(calcGamma1(), 2) / calcK1())
								* (calcTotalAcidity() + calcKw() / Math.pow(calcGamma1(), 2) / ccpphOld - ccpphOld))
						/ Math.pow((1 + 2 * Math.pow(calcGamma1(), 2) / calcK1() * ccpphOld), 2) )*100d)/100d;

				ccppdco3dh = Math.round(( (calcK2() / calcGamma2() * (ccppdhco3dh * ccpphOld - ccpphco3) / Math.pow(ccpphOld, 2)) )*100d)/100d;
				ccppdcadh = Math.round(( calcKso() / Math.pow(calcGamma2(), 2) * (-ccppdco3dh) / Math.pow(ccppco3, 2) )*100d)/100d;

				//-$C$22/$C$18^2/K12^2
				ccppdohdh = Math.round((-calcKw() / Math.pow(calcGamma1(), 2) / Math.pow(ccpphOld, 2) )*100d)/100d;
				ccppdfdh = Math.round((-2 * ccppdco3dh - ccppdhco3dh - ccppdohdh + 1 + 2 * ccppdcadh )*1000d)/1000d;
				if (ccpphOld - ccppFh / ccppdfdh < 0) {
					ccppHnew = Math.round((ccpphOld / 10 )*10000000000000d)/10000000000000d;
				} else {
					ccppHnew = Math.round((ccpphOld - ccppFh / ccppdfdh )*10000000000000d)/10000000000000d;
				}
			}
		}
		System.out.println("-------------------------------------");
		System.out.println("ph: " + ccpppH);
		System.out.println("Hold: " + ccpphOld);
		System.out.println("hco3: " + ccpphco3);
		System.out.println("co3: " + ccppco3);
		System.out.println("Ca: " + ccppCa);
		System.out.println("OH: " + ccppOh);
		System.out.println("Fh: " + ccppFh);
		System.out.println("dhco3dh: " + ccppdhco3dh);
		System.out.println("dco3dh: " + ccppdco3dh);
		System.out.println("dcadh: " + ccppdcadh);
		System.out.println("dohdh: " + ccppdohdh);
		System.out.println("dfdh: " + ccppdfdh);
		System.out.println("hnew: " + ccppHnew);
		return ccppCa;
	}

	// public static double masterCCPP() {
	// System.out.println("-------------------------------------");
	// System.out.println("Starting Master");

	// }

	public static double CCPPHnew(double hOld, double fh, double dfdh) {
		// FORM: IF((K12-P12/U12)<0,K12/10,K12-P12/U12)
		double x;
		if (hOld - fh / dfdh < 0) {
			x = hOld / 10;
		} else {
			x = (hOld - fh) / (dfdh);
		}
		return x;
	}

	// CCPP CALCULATION FORMULAS
	// what if i just jammed this all into one giant method.
	// wait. tail recursion, duh...
	// wait, tail recursion is super complicated. not duh...
	// wait maybe it's not.

	/* END CCPP METHODS */

	// The guts of the late GetFormula method will go here.
	public static double calcI() {
		return Math.round(GetTDS() * 0.000025f * 10000d) / 10000d;
	}

	public static double calcE() {
		return Math.round((60954.0 / (GetTemp() + 273.15 + 116.0) - 68.937) *100d ) / 100d;
	}

	public static double calcA() {
		return Math.round( ( 1820000 * Math.pow((calcE() * (GetTemp() + 273.15)), (-1.5) ) ) * 1000000000d ) /1000000000d; // C14
	}

	public static double calcLogGamma1() {
		//-1*C14*1^2*(SQRT(C12)/(1+SQRT(C12))-0.3*C12)
		return Math.round((-1.0 * calcA() * Math.pow(1, 2) * (Math.sqrt(calcI()) / (1 + Math.sqrt(calcI())) - 0.3 * calcI()))* 1000000000d)/1000000000d;
	}

	public static double calcLogGamma2() {
		return Math.round((-1.0f * calcA() * Math.pow(2f, 2f) * (Math.sqrt(calcI()) / (1f + Math.sqrt(calcI())) - 0.3f * calcI()))*1000000000d)/1000000000d;
	}

	public static double calcGamma1() {
		return Math.round(Math.pow(10, calcLogGamma1()) * 10000d) / 10000d;
	}

	public static double calcGamma2() {
		return Math.round((Math.pow(10, calcLogGamma2()))*10000d)/10000d;
	}

	public static double calcK1() {
		return Math.round((Math.pow(10, (-1 * (356.309 - 21834.4 / (273 + GetTemp()) - 126.834 * Math.log10(273 + GetTemp())
				+ 0.06092 * (273 + GetTemp()) + 1684915 / Math.pow((273 + GetTemp()), 2)))))*1000000000d)/1000000000d;
	}

	public static double calcK2() {
		return Math.round((Math.pow(10, (-1 * (107.887 - 5151.8 / (273 + GetTemp()) - 38.926 * Math.log10(273 + GetTemp())
				+ 0.032528 * (273 + GetTemp()) + 563713.9 / Math.pow(273 + GetTemp(), 2)))))*10000000000000d)/10000000000000d;
	}

	public static double calcKw() {
		return Math.round((Math.pow(10, (-1f * (-6.088 + 4471 / (273 + GetTemp()) + 0.01706 * (273 + GetTemp())))))*100000000000000000d)/100000000000000000d; // C22
	}

	public static double calcKso() {
		return Math.round((Math.pow(10, (-1f * (171.9065 + 0.077993 * (GetTemp() + 273f) - 2839.319 / (GetTemp() + 273f)
				- 71.595 * Math.log10((GetTemp() + 273f))))) )*100000000000d)/100000000000d; // C23
	}

	public static double calcpK1() {
		return Math.round((-Math.log10(calcK1()) )*100d)/100d;
	}

	public static double calcpK2() {
		return Math.round((-Math.log10(calcK2()) )*100d)/100d;
	}

	public static double calcpKw() {
		return Math.round((-Math.log10(calcKw()) )*100d)/100d;
	}

	public static double calcpKso() {
		return Math.round((-Math.log10(calcKso()) )*100d)/100d;
	}

	public static double calcHpositive() {
		return Math.round((Math.pow(10, pH * -1) / calcGamma1() )*10000000000000d)/10000000000000d;
	}

	public static double calcOHnegative() {
		return Math.round((calcKw() / calcHpositive() / Math.pow(calcGamma1(), 2) )*1000000000000d)/1000000000000d;
	}

	public static double calcCtCO3() {
		// SUM(C33:C35)
		return Math.round(((calcH2CO3() + calcHCO3() + calcCO32negative()) )*10000000d)/10000000d;
	}

	public static double calcH2CO3() {
		return Math.round((Math.pow(calcGamma1(), 2) * calcHpositive() / calcK1()
				* (GetAlkalinity() / 50000.0 - calcKw() / Math.pow(calcGamma1(), 2) / calcHpositive() + calcHpositive())
				/ (1.0 + 2.0 * calcK2() / calcGamma2() / calcHpositive()) )*10000000d)/10000000d;
	}

	public static double calcHCO3() {
		// (GetAlkalinity()/50000-calcKw()/Math.pow(calcGamma1(),
		// 2)/calcHpositive()+calcHpositive())/(1+2*calcK2/calcGamma2()/calcHpositive());
		return Math.round(((GetAlkalinity() / 50000 - calcKw() / Math.pow(calcGamma1(), 2) / calcHpositive() + calcHpositive())
				/ (1 + 2 * calcK2() / calcGamma2() / calcHpositive()) )*10000000d)/10000000d;
	}

	public static double calcCO32negative() {
		return Math.round((calcK2() / calcGamma2() / calcHpositive()
				* (alkalinity / 50000 - calcKw() / Math.pow(calcGamma1(), 2) / calcHpositive() + calcHpositive())
				/ (1 + 2 * calcK2() / calcGamma2() / calcHpositive()) )*1000000000d)/1000000000d;
	}

	public static double calcTotalAcidity() {
		 return Math.round(( 2 * calcH2CO3() + calcHCO3() + calcHpositive() - calcKw() / calcHpositive() / Math.pow(calcGamma1(), 2) )*10000000d)/10000000d;
	}

	public static double calcTotalAlk() {
		return alkalinity / 50000 - (2 * calcCalcium / 100000);
	}

	public static double calcAlpha0() {
		return Math.round((calcHpositive() / (calcHpositive() + calcK1()) )*10000d)/10000d;
	}

	public static double calcAlpha1() {
		return 1f - calcAlpha0();
	}

	public static double calcAlpha2() {
		return Math.round((1f - calcHpositive() / (calcHpositive() + calcK2()) )*100000d)/100000d;
	}

	public static double calcAlkalinityCheck() {
		return Math.round(( 50000f * (calcHCO3() + 2f * calcCO32negative() + calcOHnegative() - calcHpositive()) )*1000d)/1000d;
	}

	public static double calcSaturationRatio() {
		return Math.round(((Math.pow(calcGamma2(), 2.0) * calcCalcium / 2.5) / 40000.0 * (calcCO32negative() / calcKso()) )*100d)/100d;
	}

	public static double calcLSI() {
		return Math.log(calcSaturationRatio());
	}

	public static double calcpHs() {
		return pH - calcLSI();
	}

	public static double calcAI() {
		return pH + Math.log(calcCalcium * tds);
	}

	public static double calcRI() {
		return 2 * calcpHs() - pH;
	}

	public static double calcCCPP() {
		return GetCalcCalcium()-100000*ccppCa;
	}

	/*
	 * 
	 * 
	 * END OF CCPP CALCULATIONS/FORMULAS
	 * 
	 * 
	 */

	public static void SetTemp(double f) {
		temperature = f;
	}

	public static double GetTemp() {
		return temperature;
	}

	public static void SetPh(double f) {
		pH = f;
	}

	public static double GetPh() {
		return pH;
	}

	public static void SetTDS(double f) {
		tds = f;
	}

	public static double GetTDS() {
		return tds;
	}

	public static void SetAlkalinity(double f) {
		alkalinity = f;
	}

	public static double GetAlkalinity() {
		return alkalinity;
	}

	public static void SetPhTarget(double f) {
		phTarget = f;
	}

	public static double GetPhTarget() {
		return phTarget;
	}

	public static void SetFinalAlkalinity(double f) {
		finalAlkalinity = f;
	}

	public static double GetFinalAlkalinity() {
		return finalAlkalinity;
	}

	public static void SetCT(double f) {
		ct = f;
	}

	public static double GetCT() {
		return ct;
	}

	public static void SetAlpha1(double f) {
		alpha1 = f;
	}

	public static double GetAlpha1() {
		return alpha1;
	}

	public static void SetAlpha2(double f) {
		alpha2 = f;
	}

	public static double GetAlpha2() {
		return alpha2;
	}

	public static void SetPortalCalcium(double f) {
		portalCalcium = f;
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

	public static void PrintFormulas() {
		System.out.println("PRINTING FORMULAS\n----------------------------------------------------");
		System.out.println("cg1: " + calcGamma1());
		System.out.println("cg2: " + calcGamma2());
		System.out.println("calcI: " + calcI());
		System.out.println("calcA: " + calcA());
		System.out.println("calcE: " + calcE());
		System.out.println("cLG1: " + calcLogGamma1());
		System.out.println("cLG2: " + calcLogGamma2());
		System.out.println("calcK1: " + calcK1());
		System.out.println("calcK2: " + calcK2());
		System.out.println("calcKw: " + calcKw());
		System.out.println("calcKso: " + calcKso());
		System.out.println("calcpK1: " + calcpK1());
		System.out.println("calcpK2: " + calcpK2());
		System.out.println("calcpKw: " + calcpKw());
		System.out.println("calcpKso: " + calcpKso());
		System.out.println("calcH+: " + calcHpositive());
		System.out.println("calcOH-: " + calcOHnegative());
		System.out.println("calcCtO3: " + calcCtCO3());
		System.out.println("calcH2CO3: " + calcH2CO3());
		System.out.println("calcHCO3: " + calcHCO3());
		System.out.println("calcCO32-: " + calcCO32negative());
		System.out.println("calcTotalAcidity: " + calcTotalAcidity());
		System.out.println("calcTotalAlk: " + calcTotalAlk());
		System.out.println("calcAlpha0: " + calcAlpha0());
		System.out.println("calcAlpha1: " + calcAlpha1());
		System.out.println("calcAlpha2: " + calcAlpha2());
		System.out.println("calcAlkalinityCheck: " + calcAlkalinityCheck());
		System.out.println("calcSaturationRatio: " + calcSaturationRatio());
		System.out.println("------------------------------------------------------------\nPRINTING COMPLETE");
	}
}
