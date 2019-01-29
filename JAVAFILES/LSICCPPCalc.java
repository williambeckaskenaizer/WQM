import java.util.Arrays;

public class LSICCPPCalc {
public static void main(String args[]){

								float[][] CCPPArr = GenerateCCPPTable();
								DisplayArray(CCPPArr);

								float temperature;
								float tds;
								float pH;
								float alkalinity;
								float phTarget;
								float finalAlkalinity;
								float ct;
								float alpha1;
								float alpha2;
								float portalCalcium;
								float calcCalcium;

								portalCalcium = 61.5f;
								pH = 8.5f; //C6
								alkalinity = 35f; //C7
								calcCalcium = portalCalcium*2.5f; //C8
								tds = 57f; //C9
								temperature = 10.0f;//C10
								phTarget = 10.3f;

								//This next section will contain an insane number of variables. I'll do my best to keep them organized.
								//I'll try and stick to a naming convention. "Calc" means they're from the calc page, Portal means they're from the portal page.

								float calcI = tds*0.000025f; //C12
								float calcE = 60954f/(temperature + 273.15f+116f)-68.937f; //C13
								float calcA = (float)Math.pow(1820000f*(calcE*(temperature+273.15f)), -1.5f); //C14

								float calcLogGamma1 = -1*calcA*(float)Math.pow(1, 2*(Math.sqrt(calcI)/(1+Math.sqrt(calcI))-0.3*calcI)); //C15
								float calcLogGamma2 = -1*calcA*(float)Math.pow(2, 2*(Math.sqrt(calcI)/(1+Math.sqrt(calcI))-0.3*calcI)); //C16

								float calcGamma1 = (float)Math.pow(10, calcLogGamma1); //C18
								float calcGamma2 = (float)Math.pow(10, calcLogGamma2); //C19

								float calcK1 = (float)Math.pow(10, (-1*(356.309-21834.4/(273+temperature)-126.834*Math.log(273+temperature)+0.06092*(273+temperature)+1684915/(273+Math.pow(temperature,2))))); //C20
								float calcK2 = (float)Math.pow(10, (-1*(107.887-5151.8/(273+temperature)-38.926*Math.log(273+temperature)+0.032528*(273+temperature)+563713.9/(273+Math.pow(temperature,2))))); //C21
								float calcKw = (float)Math.pow(10, (-1*(-6.088+4471/(273+temperature)+0.01706*(273+temperature)))); //C22
								float calcKso = (float)Math.pow(10,(-1*(171.9065+0.077993*(temperature+273)-2839.319/(temperature+273)-71.595*Math.log10((temperature+273))))); //C23

								float calcpK1 = (float)-Math.log(calcK1); //C24
								float calcpK2 = (float)-Math.log(calcK2); //C25
								float calcpKw = (float)-Math.log(calcKw); //C26
								float calcpKso = (float)-Math.log(calcKso); //C27

								float calcHpositive = (float)Math.pow(10, pH)/calcGamma1; //C28
								float calcOHnegative = calcKw/calcHpositive/(float)Math.pow(calcGamma1,2); //C29

								float calcH2CO3 = (float)Math.pow(calcGamma1, 2*calcHpositive/calcK1*(alkalinity/50000-calcKw/Math.pow((calcGamma1),2)/calcHpositive+calcHpositive)/(1+2*calcK2/calcGamma2/calcHpositive)); //C33
								float calcHCO3 = (alkalinity/50000-calcKw/(float)Math.pow((calcGamma1), 2)/calcHpositive+calcHpositive)/(1+2*calcK2/calcGamma2/calcHpositive); //C34
								float calcCO32negative = calcK2/calcGamma2/calcHpositive*(alkalinity/50000-calcKw/(float)Math.pow(calcGamma1, 2)/calcHpositive+calcHpositive)/(1+2*calcK2/calcGamma2/calcHpositive); //C35
								float calcTotalAcidity = 2*calcH2CO3+calcHCO3+calcHpositive-calcKw/calcHpositive/(float)Math.pow(calcGamma1, 2); //C36
								float calcCtC03 = (calcH2CO3 + calcHCO3 + calcCO32negative); //C32

								float calcTotalAlk = alkalinity/50000-2*calcCalcium/100000; //C37
								float calcAlpha0 = calcHpositive/(calcHpositive+calcK1); //C39
								float calcAlpha1 = 1-calcAlpha0; //C40
								float calcAlpha2 = 1-calcHpositive/(calcHpositive+calcK2);//C41

								float calcAlkalinityCheck = 50000*(calcHCO3+2*calcCO32negative+calcOHnegative-calcHpositive); //C43
								float calcSaturationRatio = (float)Math.pow(calcGamma2, 2)*calcCalcium/2.5f/40000f*calcCO32negative/calcKso; //C44

								float calcLSI = (float)Math.log(calcSaturationRatio); //C46
								float calcpHs = pH-calcLSI; //C47
								float calcCCPP = calcCalcium-100000 /* * */ /*N48*/; //C48
								float calcAI = pH + (float)Math.log(calcCalcium * tds);//C49
								float calcRI = 2*calcpHs-pH; //C50

								// 37 x 13

}
public static float[][] GenerateCCPPTable(){
								System.out.println("Generating CCPP Table");
								float[][] arr = new float[37][13];

								for(int i = 0; i < arr.length; i++) {
																for(int j = 0; j < arr[i].length; j++) {
																								arr[i][j] = 0f;
																}
								}
								System.out.println("End of Generate method");
								return arr;
}
public static void DisplayArray(float[][] arr) {
								System.out.println("\nDisplaying CCPP Array");
								for (int i = 0; i < arr.length; i++) {
																System.out.print("\t");
																System.out.print("\n");
																for(int j = 0; j < arr[i].length; j++){
																	System.out.print(arr[i][j] + " ");
																}
								}
								System.out.println("\nEnd of array");
}


//create a 2D array, 13x37.
//Columns will be as follows:
/*0 - flags
   1 - pH
   2 - [H]old
   3 - HCO3
   4 - CO3
   5 - Ca
   6 - [OH]
   7 - F([H])
   8 - d[HCO3]/d[H]
   9 - d[CO3]/d[H]
   10 - d[Ca]/d[H]
   11 - d[OH]/d[H]
   12 - dF/d[H]
   13 - [H]new

 */

}
