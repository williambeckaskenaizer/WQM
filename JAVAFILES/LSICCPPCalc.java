import java.util.Arrays;

public class LSICCPPCalc {

//Chemistry Values
private static float temperature;
private static float tds;
private static float pH;
private static float alkalinity;
private static float phTarget;
private static float finalAlkalinity;
private static float ct;
private static float alpha1;
private static float alpha2;
private static float portalCalcium;
private static float calcCalcium;

//Other variables
private static int phColumn;
private static int hOldColumn;
private static int HCO3Column;

private static double tolerance;
private static int numRecurrance;

public static void main(String args[]){
	SetTolerance(.0000000000001);
	SetNumRecurrance(37);
	Newton(0.0f);
}

public static float Newton(float x){
	for(int i = 1; Math.abs(f(x)) > GetTolerance() && i < GetNumRecurrance(); i++){
		x = x-f(x)/fPrime(x);
		System.out.println("Step " + i + " x: " + x + " Value: " + f(x));
	}
	if(Math.abs(f(x)) <= tolerance){
		System.out.println("Zero found at " + x);
	}else{
		System.out.println("Failed to find zero within " + numRecurrance + " iterations");
	}
	return 0.0f;
}

static float f(float x){
	return (float)Math.sin(x);
}

static float fPrime(float x){
	return (float)Math.centered(x);
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

static float GetNumRecurrance(){
	return numRecurrance;
}



public static void SetTemp(float f){
								temperature = f;
}
public static float GetTemp(){
								return temperature;
}

public static void SetPh(float f){
								pH = f;
}
public static float GetPh(){
								return pH;
}

public static void SetTDS(float f){
								tds = f;
}
public static float GetTDS(){
								return tds;
}

public static void SetAlkalinity(float f){
								alkalinity = f;
}
public static float GetAlkalinity(){
								return alkalinity;
}

public static void SetPhTarget(float f){
								phTarget = f;
}
public static float GetPhTarget(){
								return phTarget;
}

public static void SetFinalAlkalinity(float f){
								finalAlkalinity = f;
}
public static float GetFinalAlkalinity(){
								return finalAlkalinity;
}

public static void SetCT(float f){
								ct = f;
}
public static float GetCT(){
								return ct;
}

public static void SetAlpha1(float f){
								alpha1 = f;
}
public static float GetAlpha1(){
								return alpha1;
}

public static void SetAlpha2(float f){
								alpha2 = f;
}
public static float GetAlpha2(){
								return alpha2;
}

public static void SetPortalCalcium(float f){
								portalCalcium = f;
}
public static float GetPortalCalcium(){
								return portalCalcium;
}
public static void SetCalcCalcium(){
								calcCalcium = GetPortalCalcium()*2.5f;
}
public static float GetCalcCalcium(){
								return calcCalcium;
}

//Methods to set columns

public static void SetPhColumn(int n){
								phColumn = n;
}

public static void SetHOldColumn(int n){
								hOldColumn = n;
}

public static void SetHCO3Column(int n){
								HCO3Column = n;
}

//Print method

public static void print(float f){
								System.out.println(f);
}
public static float GetFormula(String formId){

								//This next section will contain an insane number of variables. I'll do my best to keep them organized.
								//I'll try and stick to a naming convention. "Calc" means they're from the calc page, Portal means they're from the portal page.

								float calcI = tds*0.000025f; //C12
								float calcE = 60954f/(temperature + 273.15f+116f)-68.937f; //C13
								float calcA = 1820000f*(float)Math.pow((calcE*(temperature+273.15f)),(-1.5f)); //C14

								float calcLogGamma1 = -1.0f*calcA*(float)Math.pow(1f,2f)*((float)Math.sqrt(calcI)/(1f+(float)Math.sqrt(calcI)))-0.3f*calcI;  //C15
								float calcLogGamma2 = -1.0f*calcA*(float)Math.pow(2f, 2f)*((float)Math.sqrt(calcI)/(1f+(float)Math.sqrt(calcI))-0.3f*calcI); //C16

								float calcGamma1 = (float)Math.pow(10, calcLogGamma1); //C18
								float calcGamma2 = (float)Math.pow(10, calcLogGamma2); //C19

								float calcK1 = (float)Math.pow(10, (-1f*(356.309-21834.4/(273+temperature)-126.834*Math.log(273+temperature)+0.06092*(273f+temperature)+1684915/(273f+Math.pow(temperature,2))))); //C20
								float calcK2 = (float)Math.pow(10, (-1f*(107.887-5151.8/(273+temperature)-38.926*Math.log(273+temperature)+0.032528*(273f+temperature)+563713.9/(273f+Math.pow(temperature,2))))); //C21
								float calcKw = (float)Math.pow(10, (-1f*(-6.088+4471/(273+temperature)+0.01706*(273+temperature)))); //C22
								float calcKso = (float)Math.pow(10,(-1f*(171.9065+0.077993*(temperature+273f)-2839.319/(temperature+273f)-71.595*Math.log10((temperature+273f))))); //C23

								float calcpK1 = (float)-Math.log(calcK1); //C24
								float calcpK2 = (float)-Math.log(calcK2); //C25
								float calcpKw = (float)-Math.log(calcKw); //C26
								float calcpKso = (float)-Math.log(calcKso); //C27

								float calcHpositive = (float)Math.pow(10, pH)/calcGamma1; //C28
								float calcOHnegative = calcKw/calcHpositive/(float)Math.pow(calcGamma1,2); //C29

								float calcH2CO3 = (float)Math.pow(calcGamma1, 2f*calcHpositive/calcK1*(alkalinity/50000f-calcKw/Math.pow((calcGamma1),2)/calcHpositive+calcHpositive)/(1f+2f*calcK2/calcGamma2/calcHpositive)); //C33
								float calcHCO3 = (alkalinity/50000f-calcKw/(float)Math.pow((calcGamma1), 2)/calcHpositive+calcHpositive)/(1f+2f*calcK2/calcGamma2/calcHpositive); //C34
								float calcCO32negative = calcK2/calcGamma2/calcHpositive*(alkalinity/50000-calcKw/(float)Math.pow(calcGamma1, 2)/calcHpositive+calcHpositive)/(1f+2f*calcK2/calcGamma2/calcHpositive); //C35
								float calcTotalAcidity = 2*calcH2CO3+calcHCO3+calcHpositive-calcKw/calcHpositive/(float)Math.pow(calcGamma1, 2); //C36
								float calcCtC03 = (calcH2CO3 + calcHCO3 + calcCO32negative); //C32

								float calcTotalAlk = alkalinity/50000f-2f*calcCalcium/100000f; //C37
								float calcAlpha0 = calcHpositive/(calcHpositive+calcK1); //C39
								float calcAlpha1 = 1f-calcAlpha0; //C40
								float calcAlpha2 = 1f-calcHpositive/(calcHpositive+calcK2);//C41

								float calcAlkalinityCheck = 50000f*(calcHCO3+2f*calcCO32negative+calcOHnegative-calcHpositive); //C43
								float calcSaturationRatio = (float)Math.pow(calcGamma2, 2)*calcCalcium/2.5f/40000f*calcCO32negative/calcKso; //C44

								float calcLSI = (float)Math.log(calcSaturationRatio); //C46
								float calcpHs = pH-calcLSI; //C47
								float calcCCPP = calcCalcium-100000f /* * */ /*N48*/; //C48
								float calcAI = pH + (float)Math.log(calcCalcium * tds);//C49
								float calcRI = 2*calcpHs-pH; //C50

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
								default: return 0.0f;
								}
}
}
