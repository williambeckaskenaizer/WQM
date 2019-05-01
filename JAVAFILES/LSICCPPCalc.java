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
		SetPh(10);
		SetAlkalinity(10);
		SetPortalCalcium(10);
		SetCalcCalcium();
		SetTDS(10);
		SetTemp(10);
		initCCPP();
		PrintFormulas();

		// CALCULATED VALUES
		double saturationIndex = (calcLSI());
		double aggressiveIndex = calcAI();
		double ryznarIndex = calcRI();
		double dissolvedOrganicCarbon = calcCtCO3()*12*1000;
		System.out.println("Saturation Ratio: " + calcSaturationRatio());
		System.out.println("Saturation Index: " + saturationIndex);
		System.out.println("CCPP: " + calcCCPP());
		System.out.println("Aggressive Index: " + aggressiveIndex);
		System.out.println("Ryznar Index: " + ryznarIndex);
		System.out.println("Dissolved Organic Carbon: " + dissolvedOrganicCarbon);
		System.out.println("---------------whatthefuck-------------------------");
	}

	public static double initCCPP() {
		for (int i = 0; i < 37; i++) {
			if (i == 0) {
				ccpppH = sigDigRounder(( GetPh() ), 15, -1);
				System.out.println("ph: " + ccpppH);

				ccpphOld = sigDigRounder(((Math.pow(10, -ccpppH) / calcGamma1()) ), 15, -1);
				System.out.println("hOld: " + ccpphOld);

				ccpphco3 = sigDigRounder((calcTotalAcidity() + calcKw() / Math.pow(calcGamma1(), 2) / ccpphOld
						- ccpphOld / (1 + Math.pow(2, Math.pow(calcGamma1(), 2) / calcK1() * ccpphOld))), 15, -1);
				System.out.println("HCO3: " + (ccpphco3));
					
				ccppco3 = sigDigRounder((calcK2() / calcGamma2()) * (ccpphco3 / ccpphOld), 15, -1);
				System.out.println("CO3: " + ccppco3);
				
				//close 
				ccppCa = sigDigRounder(( (calcKso() / Math.pow(calcGamma2(), 2) / ccppco3) ), 15, -1 );
				System.out.println("Ca: " + ccppCa);

				ccppOh = sigDigRounder( ( (calcKw() / Math.pow(calcGamma1(), 2) / ccpphOld) ), 15, -1 );
				System.out.println("OH: " + ccppOh);
				
				ccppFh = sigDigRounder(( (calcTotalAlk() - 2 * ccppco3 - ccpphco3 - ccppOh + ccpphOld + 2 * ccppCa) ), 15, -1);
				System.out.println("Fh: " + (ccppFh));

				ccppdhco3dh = sigDigRounder( ((-calcKw() / Math.pow(calcGamma1(), 2) / Math.pow(ccpphOld, 2) - 1)
						* (1 + 2 * Math.pow(calcGamma1(), 2) * ccpphOld / calcK1())
						- (2 * Math.pow(calcGamma1(), 2) / calcK1())
						* (calcTotalAcidity() + calcKw() / Math.pow(calcGamma1(), 2) / ccpphOld - ccpphOld))
						/ Math.pow((1 + 2 * Math.pow(calcGamma1(), 2) / calcK1() * ccpphOld), 2), 15, -1 );
				System.out.println("dHCO3dH: " + (ccppdhco3dh));

				ccppdco3dh = sigDigRounder(( (calcK2() / calcGamma2() * (ccppdhco3dh * ccpphOld - ccpphco3) / Math.pow(ccpphOld, 2)) ), 15, -1 );
				System.out.println("dCO3dH: " + (ccppdco3dh));

				ccppdcadh = sigDigRounder(( calcKso() / Math.pow(calcGamma2(), 2) * (-ccppdco3dh) / Math.pow(ccppco3, 2) ), 15, -1 );
				System.out.println("dCadH: " + (ccppdcadh));

				ccppdohdh = sigDigRounder((-calcKw() / Math.pow(calcGamma1(), 2) / Math.pow(ccpphOld, 2) ), 15, -1 );
				System.out.println("dOHdH: " + ccppdohdh);

				ccppdfdh = sigDigRounder((-2 * ccppdco3dh - ccppdhco3dh - ccppdohdh + 1 + 2 * ccppdcadh ), 15, -1 );
				System.out.println("dFdH: " + (ccppdfdh));

				if (ccpphOld - ccppFh / ccppdfdh < 0) {
					ccppHnew = sigDigRounder((ccpphOld / 10 ), 15, -1 );
				} else {
					ccppHnew = sigDigRounder((ccpphOld - ccppFh / ccppdfdh ), 15, -1 );
				}
				System.out.println("Hnew: " + ccppHnew);
				System.out.println("First Round Complete");
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
		System.out.println("-------------------------------------");
		return ccppCa;
	}

	// public static double masterCCPP() {
	// System.out.println("-------------------------------------");
	// System.out.println("Starting Master");

	// }

	// CCPP CALCULATION FORMULAS
	// what if i just jammed this all into one giant method.
	// wait. tail recursion, duh...
	// wait, tail recursion is super complicated. not duh...
	// wait maybe it's not.

	/* END CCPP METHODS */

	// The guts of the late GetFormula method will go here.
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
		// (GetAlkalinity()/50000-calcKw()/Math.pow(calcGamma1(),
		// 2)/calcHpositive()+calcHpositive())/(1+2*calcK2/calcGamma2()/calcHpositive());
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
		System.out.println("calcLSI: " + calcLSI());
		System.out.println("calcpHs: " + calcpHs());
		System.out.println("calcAlkalinityCheck: " + calcAlkalinityCheck());
		System.out.println("calcSaturationRatio: " + calcSaturationRatio());
		System.out.println("------------------------------------------------------------\nPRINTING COMPLETE");
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
