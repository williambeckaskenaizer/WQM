
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

		// CALCULATED VALUES
		// double saturationIndex = Math.log(calcLSI());
		double CCPP = calcCCPP();
		// double aggressiveIndex = calcAI();
		// double ryznarIndex = calcRI();
		// double dissolvedOrganicCarbon = calcCtCO3()*12*1000;

		// System.out.println(CCPPpH(12));
		// System.out.println(CCPPhOld(12));
		// System.out.println(CCPPHCO3(12));

		// System.out.println("Saturation Index: " + saturationIndex);
		System.out.println("CCPP: " + CCPP);
		// System.out.println("Aggressive Index: " + aggressiveIndex);
		// System.out.println("Ryznar Index: " + ryznarIndex);
		// System.out.println("Dissolved Organic Carbon: " + dissolvedOrganicCarbon);
		System.out.println("----------------------------------------");
		// System.out.println("cg1: " + calcGamma1());
		// System.out.println("cg2: " + calcGamma2());
		// System.out.println("calcI: " + calcI());
		// System.out.println("calcA: " + calcA());
		// System.out.println("calcE: " + calcE());
		// System.out.println("cLG1: " + calcLogGamma1());
		// System.out.println("cLG2: " + calcLogGamma2());
		// System.out.println("calcK1: " + calcK1());
		// System.out.println("calcK2: " + calcK2());
		// System.out.println("calcKw: " + calcKw());
		// System.out.println("calcKso: " + calcKso());
		// System.out.println("calcpK1: " + calcpK1());
		// System.out.println("calcpK2: " + calcpK2());
		// System.out.println("calcpKw: " + calcpKw());
		// System.out.println("calcpKso: " + calcpKso());
		// System.out.println("calcH+: " + calcHpositive());
		// System.out.println("calcOH-: " + calcOHnegative());
		// System.out.println("calcCtO3: " + calcCtCO3());
		// System.out.println("calcH2CO3: " + calcH2CO3());
		// System.out.println("calcHCO3: " + calcHCO3());
		// System.out.println("calcCO32-: " + calcCO32negative());

	}

	public static double masterCCPP() {

		for (int i = 0; i < 37; i++) {
			if (i == 0) {
				ccpppH = GetPh();
				System.out.println("ph: " + ccpppH);
				ccpphOld = (Math.pow(10, -ccpppH) / calcGamma1());
				System.out.println("Hold: " + ccpphOld);

				ccpphco3 = (calcTotalAcidity() + calcKw() / Math.pow(calcGamma1(), 2) / ccpphOld
						- ccpphOld / (1 + Math.pow(2, Math.pow(calcGamma1(), 2) / calcK1() * ccpphOld)));
				System.out.println("hco3: " + ccpphco3);

				ccppco3 = (calcK2() / calcGamma2()) * (ccpphco3 / ccpphOld);
				System.out.println("co3: " + ccppco3);

				ccppCa = (calcKso() / Math.pow(calcGamma2(), 2) / ccppco3);
				System.out.println("Ca: " + ccppCa);

				//$C$22/$C$18^2/K12
				ccppOh = (calcKw()/Math.pow(calcGamma1(), 2) / ccpphOld);
				System.out.println("OH: " + ccppOh);

				ccppFh = (calcTotalAlk() - (2 * ccppco3 - ccpphco3 - ccppOh + ccpphOld + 2 * ccppCa));
				ccppdhco3dh = ((-calcKw() / (Math.pow(calcGamma1(), 2) / Math.pow(ccpphOld, 2) - 1)
						* (1 + 2 * Math.pow(calcGamma1(), 2) * ccpphOld / calcK1())
						- (2 * Math.pow(calcGamma1(), 2) / calcK1()) * calcTotalAcidity()
						+ calcKw() / Math.pow(calcGamma1(), 2) / ccpphOld - ccpphOld)
						/ (1 + 2 * Math.pow(calcGamma1(), 2) / Math.pow(calcK1() * ccpphOld, 2)));
				System.out.println("dhco3dh: " + ccppdhco3dh);

				ccppdco3dh = (calcK2() / calcGamma2() * (ccppdhco3dh * ccpphOld - ccpphco3) / Math.pow(ccpphOld, 2));
				System.out.println("dco3dh: " + ccppdco3dh);

				//$C$23/$C$19^2*(-R12)/M12^2
				ccppdcadh = calcKso()/Math.pow(calcGamma2(), 2)*(-ccppdco3dh) / Math.pow(ccppco3, 2);
				System.out.println("dcadh: " + ccppdcadh);

				//-$C$22/$C$18^2/K12^2
				ccppdohdh = -calcKw()/Math.pow(calcGamma1(), 2)/Math.pow(ccpphOld, 2);
				System.out.println("dohdh: " + ccppdohdh);

				//-2*R12-Q12-T12+1+2*S12
				ccppdfdh = (-2*ccppdco3dh-ccppdhco3dh)-ccppdohdh+(1+2*ccppdcadh);
				System.out.println("dfdh: " + ccppdfdh);

				if(ccpphOld - ccppFh / ccppdfdh < 0){
					ccppHnew = ccpphOld/10;
				}else{
					ccppHnew = ccpphOld - ccppFh/ccppdfdh;
				}
				System.out.println("hnew: " + ccppHnew);
			} else {
				ccpppH = -(Math.log(CCPPHnew(ccpphOld, ccppFh, ccppdfdh) * calcGamma1()));
				ccpphOld = (Math.pow(10, -ccpppH) / calcGamma1());
				ccpphco3 = (calcTotalAcidity() + calcKw() / Math.pow(calcGamma1(), 2) / ccpphOld
						- ccpphOld / (1 + Math.pow(2, Math.pow(calcGamma1(), 2) / calcK1() * ccpphOld)));
				ccppco3 = (calcK2() / calcGamma2()) * (ccpphco3 / ccpphOld);
				ccppCa = (calcKso() / Math.pow(calcGamma2(), 2) / ccppco3);
				ccppOh = (calcKw() / (Math.pow(calcGamma1(), 2) / ccpphOld));
				ccppFh = (calcTotalAlk() - (2 * ccppco3 - ccpphco3 - ccppOh + ccpphOld + 2 * ccppCa));
				ccppdhco3dh = ((-calcKw() / (Math.pow(calcGamma1(), 2) / Math.pow(ccpphOld, 2) - 1)
						* (1 + 2 * Math.pow(calcGamma1(), 2) * ccpphOld / calcK1())
						- (2 * Math.pow(calcGamma1(), 2) / calcK1()) * calcTotalAcidity()
						+ calcKw() / Math.pow(calcGamma1(), 2) / ccpphOld - ccpphOld)
						/ (1 + 2 * Math.pow(calcGamma1(), 2) / Math.pow(calcK1() * ccpphOld, 2)));
				ccppdco3dh = (calcK2() / calcGamma2() * (ccppdhco3dh * ccpphOld - ccpphco3) / Math.pow(ccpphOld, 2));
				ccppdcadh = calcKso() / Math.pow(calcGamma2(), 2) * (-ccppdco3dh) / ccppco3;
				ccppdohdh = -calcKw() / Math.pow(calcKw(), 2) / Math.pow(ccpphOld, 2);
				ccppdfdh = -2 * ccppdco3dh - ccppdhco3dh - ccppdohdh + 1 + 2 * ccppdcadh;
				
				if(ccpphOld - ccppFh / ccppdfdh < 0){
					ccppHnew = ccpphOld/10;
				}else{
					ccppHnew = (ccpphOld - ccppFh)/ccppdfdh;
				}
			}

		}
		return ccppCa;
	}

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
		return 60954.0 / (GetTemp() + 273.15 + 116.0) - 68.937;
	}

	public static double calcA() {
		return 1820000 * Math.pow((calcE() * (GetTemp() + 273.15)), (-1.5)); // C14
	}

	public static double calcLogGamma1() {
		return -1.0f * calcA() * Math.pow(1f, 2f) * (Math.sqrt(calcI()) / (1f + Math.sqrt(calcI()))) - 0.3f * calcI();
	}

	public static double calcLogGamma2() {
		return -1.0f * calcA() * Math.pow(2f, 2f) * (Math.sqrt(calcI()) / (1f + Math.sqrt(calcI())) - 0.3f * calcI());
	}

	public static double calcGamma1() {
		return Math.round(Math.pow(10, calcLogGamma1()) * 10000d) / 10000d;
	}

	public static double calcGamma2() {
		return Math.pow(10, calcLogGamma2());
	}

	public static double calcK1() {
		return Math.pow(10f, (-1f * (356.309 - 21834.4 / (273f + GetTemp()) - 126.834 * Math.log10(273f + GetTemp())
				+ 0.06092 * (273 + GetTemp()) + 1684915 / Math.pow((273f + GetTemp()), 2))));
	}

	public static double calcK2() {
		return Math.pow(10, (-1 * (107.887 - 5151.8 / (273 + GetTemp()) - 38.926 * Math.log10(273 + GetTemp())
				+ 0.032528 * (273 + GetTemp()) + 563713.9 / Math.pow(273 + GetTemp(), 2))));
	}

	public static double calcKw() {
		return Math.pow(10, (-1f * (-6.088 + 4471 / (273 + GetTemp()) + 0.01706 * (273 + GetTemp())))); // C22
	}

	public static double calcKso() {
		return Math.pow(10, (-1f * (171.9065 + 0.077993 * (GetTemp() + 273f) - 2839.319 / (GetTemp() + 273f)
				- 71.595 * Math.log10((GetTemp() + 273f))))); // C23
	}

	public static double calcpK1() {
		return -Math.log10(calcK1());
	}

	public static double calcpK2() {
		return -Math.log10(calcK2());
	}

	public static double calcpKw() {
		return -Math.log10(calcKw());
	}

	public static double calcpKso() {
		return -Math.log10(calcKso());
	}

	public static double calcHpositive() {
		return Math.pow(10, pH * -1) / calcGamma1();
	}

	public static double calcOHnegative() {
		return calcKw() / calcHpositive() / Math.pow(calcGamma1(), 2);
	}

	public static double calcH2CO3() {
		return Math.pow(calcGamma1(),2)*calcHpositive()/calcK1()
		*(GetAlkalinity()/50000-calcKw()/Math.pow(calcGamma1(), 2)
		/calcHpositive()+calcHpositive())/(1+2*calcK2()/calcGamma2()/calcHpositive());
	}

	public static double calcHCO3() {
		//(GetAlkalinity()/50000-calcKw()/Math.pow(calcGamma1(), 2)/calcHpositive()+calcHpositive())/(1+2*calcK2/calcGamma2()/calcHpositive());
		return (GetAlkalinity()/50000-calcKw()/Math.pow(calcGamma1(), 2)/calcHpositive()+calcHpositive())/(1+2*calcK2()/calcGamma2()/calcHpositive());
	}

	public static double calcCO32negative() {
		return calcK2() / calcGamma2() / calcHpositive()
				* (alkalinity / 50000 - calcKw() / Math.pow(calcGamma1(), 2) / calcHpositive() + calcHpositive())
				/ (1 + 2 * calcK2() / calcGamma2() / calcHpositive());
	}

	public static double calcTotalAcidity() {
		return 2 * calcH2CO3() + calcHCO3() + calcHpositive() - calcKw() / calcHpositive() / Math.pow(calcGamma1(), 2);
	}

	public static double calcCtCO3() {
		// SUM(C33:C35)
		return (calcH2CO3() + calcHCO3() + calcCO32negative());
	}

	public static double calcTotalAlk() {
		return alkalinity / 50000 - 2 * calcCalcium / 100000;
	}

	public static double calcAlpha0() {
		return calcHpositive() / (calcHpositive() + calcK1());
	}

	public static double calcAlpha1() {
		return 1f - calcAlpha0();
	}

	public static double calcAlpha2() {
		return 1f - calcHpositive() / (calcHpositive() + calcK2());
	}

	public static double calcAlkalinityCheck() {
		return 50000f * (calcHCO3() + 2f * calcCO32negative() + calcOHnegative() - calcHpositive());
	}

	public static double calcSaturationRatio() {
		return (Math.pow(calcGamma2(), 2.0) * calcCalcium / 2.5) / 40000.0 * (calcCO32negative() / calcKso());
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
		return calcCalcium - (100000.0 * masterCCPP());
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
}
