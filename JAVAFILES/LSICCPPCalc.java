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
private static double tolerance;
private static int numRecurrance;

public static void main(String args[]){
	//initialize manual entry table
	SetPh(8.08f);
	SetAlkalinity(190.f);
	SetPortalCalcium(61.2f);
	SetTDS(364.f);
	SetTemp(18.f);
	SetTolerance(.0000000000001);
	SetNumRecurrance(37);
	System.out.println("running newton on calchco3: " +Newton(GetFormula("calcHCO3")));


}

public static double Newton(double x){
	// for(int i = 1; Math.abs(f(x)) > GetTolerance() && i < GetNumRecurrance(); i++){
	// 	x = x-f(x)/fPrime(x);
	// 	System.out.println("Step " + i + " x: " + x + " Value: " + f(x));
	// }
	// if(Math.abs(f(x)) <= tolerance){
	// 	System.out.println("Zero found at " + x);
	// }else{
	// 	System.out.println("Failed to find zero within " + numRecurrance + " iterations");
	// }
	// return 0.0f;
	double del = 1e-5,xx = 0 ;
 	double dx =0/*, x=Math.PI/2*/;
	int k = 0;
	while (Math.abs(xx-x) > del && k<10 && f(x)!=0) {
		dx = f(x)/fPrime(x);
		xx=x;
		x = x - dx;
		k++;

System.out.println("Iteration number: " + k);
System.out.println("Root obtained: " + x);
System.out.println("Estimated error: " + Math.abs(xx-x));

}
return x;
}

static double f(double x){
	return Math.sin(x);
}

static double fPrime(double x ){
	//function to take the derivative required for Newton's method
	return 1.0f/2f*x-(x*Math.cos(x)+Math.sin(x))+Math.sin(2f*x);

	//return 0.0;
}

public static void SetTolerance(double f){
	tolerance = f;
}

static double GetTolerance(){
	return tolerance;
}

static void SetNumRecurrance(int n){
	numRecurrance = n;
}

static double GetNumRecurrance(){
	return numRecurrance;
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

//Print method

public static void print(double f){
								System.out.println(f);
}
public static double GetFormula(String formId){

								//This next section will contain an insane number of variables. I'll do my best to keep them organized.
								//I'll try and stick to a naming convention. "Calc" means they're from the calc page, Portal means they're from the portal page.

								//rounding example
								//Math.round(value * 100000d) / 100000d


								double calcI = Math.round(tds*0.000025f * 10000d)/10000d; //C12
								System.out.println("calcI is "+ calcI);
								double calcE = 60954f/(temperature + 273.15f+116f)-68.937f; //C13
								System.out.println("calcE is " + calcE);
								double calcA = 1820000f*Math.pow((calcE*(temperature+273.15f)),(-1.5f)); //C14
								System.out.println("CalcA is " + Math.round(calcA * 1000000000d)/1000000000d);

								double calcLogGamma1 = -1.0f*calcA*Math.pow(1f,2f)*(Math.sqrt(calcI)/(1f+Math.sqrt(calcI)))-0.3f*calcI;  //C15
								System.out.println("calcloggamma1 is "+Math.round(calcLogGamma1 * 1000000000d)/1000000000d);
								double calcLogGamma2 = -1.0f*calcA*Math.pow(2f, 2f)*(Math.sqrt(calcI)/(1f+Math.sqrt(calcI))-0.3f*calcI); //C16

								//Rounding test for calcGamma


								double calcGamma1 = Math.round(Math.pow(10, calcLogGamma1)*10000d)/10000d; //C18
								System.out.println("calcGamma1 is "+ calcGamma1);
								double calcGamma2 = Math.pow(10, calcLogGamma2); //C19

								double calcK1 = Math.pow(10f, (-1f*(356.309-21834.4/(273f+GetTemp())-126.834*Math.log10(273f+GetTemp())+0.06092*(273f+GetTemp())+1684915f/Math.pow(273f+GetTemp(),2)))); //C20
								double calcK2 = Math.pow(10, (-1f*(107.887-5151.8/(273+temperature)-38.926*Math.log(273+temperature)+0.032528*(273f+temperature)+563713.9/(273f+Math.pow(temperature,2))))); //C21
								double calcKw = Math.pow(10, (-1f*(-6.088+4471/(273+temperature)+0.01706*(273+temperature)))); //C22
								double calcKso = Math.pow(10,(-1f*(171.9065+0.077993*(temperature+273f)-2839.319/(temperature+273f)-71.595*Math.log10((temperature+273f))))); //C23


								double calcpK1 = -Math.log10(calcK1); //C24
								double calcpK2 = -Math.log10(calcK2); //C25
								double calcpKw = -Math.log10(calcKw); //C26
								double calcpKso = -Math.log10(calcKso); //C27

								double calcHpositive = Math.pow(10, pH*-1)/calcGamma1; //C28
								double calcOHnegative = calcKw/calcHpositive/Math.pow(calcGamma1,2); //C29
								System.out.println("calchpositive "+calcHpositive);
								System.out.println("calcOHnegative is "+ calcOHnegative);

								double calcH2CO3 = Math.pow(calcGamma1, 2f*calcHpositive/calcK1*(alkalinity/50000f-calcKw/Math.pow((calcGamma1),2)/calcHpositive+calcHpositive)/(1f+2f*calcK2/calcGamma2/calcHpositive)); //C33
								double calcHCO3 = (alkalinity/50000f-calcKw/Math.pow((calcGamma1), 2)/calcHpositive+calcHpositive)/(1f+2f*calcK2/calcGamma2/calcHpositive); //C34
								System.out.println("calchco3 " + calcHCO3);
								double calcCO32negative = calcK2/calcGamma2/calcHpositive*(alkalinity/50000-calcKw/Math.pow(calcGamma1, 2)/calcHpositive+calcHpositive)/(1f+2f*calcK2/calcGamma2/calcHpositive); //C35
								double calcTotalAcidity = 2*calcH2CO3+calcHCO3+calcHpositive-calcKw/calcHpositive/Math.pow(calcGamma1, 2); //C36
								System.out.println("calctotalalkalinity "+calcTotalAcidity);
								double calcCtCO3 = (calcH2CO3 + calcHCO3 + calcCO32negative); //C32
								System.out.println("calcctco3 is"+ calcCtCO3);

								double calcTotalAlk = alkalinity/50000f-2f*calcCalcium/100000f; //C37
								double calcAlpha0 = calcHpositive/(calcHpositive+calcK1); //C39
								double calcAlpha1 = 1f-calcAlpha0; //C40
								double calcAlpha2 = 1f-calcHpositive/(calcHpositive+calcK2);//C41

								double calcAlkalinityCheck = 50000f*(calcHCO3+2f*calcCO32negative+calcOHnegative-calcHpositive); //C43
								double calcSaturationRatio = Math.pow(calcGamma2, 2)*calcCalcium/2.5f/40000f*calcCO32negative/calcKso; //C44

								double calcLSI = Math.log(calcSaturationRatio); //C46
								double calcpHs = pH-calcLSI; //C47
								double calcCCPP = calcCalcium-100000f /* * */ /*N48*/; //C48
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
								case "calcHC03": return calcHCO3;
								default: return 0.0f;
								}
}
}
