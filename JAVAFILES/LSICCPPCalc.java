public class LSICCPPCalc {
public static void main(String args[]){

								float temperature;
								float tds;
								float pH;
								float alkalinity;
								float phTarget;
								float finalAlkalinity;
								float ct;
								float alpha1;
								float alpha2;

								temperature = 10.0f;
								tds = 57f;
								pH = 8.5f;
								alkalinity = 35f;
								phTarget = 10.3f;

								//This next section will contain an insane number of variables. I'll do my best to keep them organized.
								//I'll try and stick to a naming convention. "Calc" means they're from the calc page, Portal means they're from the portal page.

								float calcI = tds*0.000025f;
								float calcE = 60954f/(temperature + 273.15f+116f)-68.937f;
								float calcA = (float)Math.pow(1820000f*(calcE*(temperature+273.15f)), -1.5f);

								float calcLogGamma1 = -1*calcA*(float)Math.pow(1, 2*(Math.sqrt(calcI)/(1+Math.sqrt(calcI))-0.3*calcI));
								float calcLogGamma2 = -1*calcA*(float)Math.pow(2, 2*(Math.sqrt(calcI)/(1+Math.sqrt(calcI))-0.3*calcI));
								float calcGamma1 = (float)Math.pow(10, calcLogGamma1);
								float calcGamma2 = (float)Math.pow(10, calcLogGamma2);

								float calcK1 = (float)Math.pow(10, (-1*(356.309-21834.4/(273+temperature)-126.834*Math.log(273+temperature)+0.06092*(273+temperature)+1684915/(273+Math.pow(temperature,2)))));
								float calcK2 = (float)Math.pow(10, (-1*(107.887-5151.8/(273+temperature)-38.926*Math.log(273+temperature)+0.032528*(273+temperature)+563713.9/(273+Math.pow(temperature,2)))));
								float calcKw = (float)Math.pow(10, (-1*(-6.088+4471/(273+temperature)+0.01706*(273+temperature))));
								float calcKso = (float)Math.pow(10,(-1*(171.9065+0.077993*(temperature+273)-2839.319/(temperature+273)-71.595*Math.log10((temperature+273)))));

								float calcpK1 = (float)-Math.log(calcK1);
								float calcpK2 = (float)-Math.log(calcK2);
								float calcpKw = (float)-Math.log(calcKw);
								float calcpKso = (float)-Math.log(calcKso);

								float calcHpositive = (float)Math.pow(10, pH)/calcGamma1;
								float calcHOnegative = calcKw/calcHpositive/(float)Math.pow(calcGamma1,2);

								//float calcCtC03 = (/* h2CO3 */ + /* hCO3 */ + /* CO32 */);
								float calcH2CO3 = (float)Math.pow(calcGamma1, 2*calcHpositive/calcK1*(alkalinity/50000-calcKw/Math.pow((calcGamma1),2)/calcHpositive+calcHpositive)/(1+2*calcK2/calcGamma2/calcHpositive));
								float calcHCO3 = (alkalinity/50000-calcKw/(float)Math.pow((calcGamma1), 2)/calcHpositive+calcHpositive)/(1+2*calcK2/calcGamma2/calcHpositive);
								float calcCO32negative = calcK2/calcGamma2/calcHpositive*(alkalinity/50000-calcKw/(float)Math.pow(calcGamma1, 2)/calcHpositive+calcHpositive)/(1+2*calcK2/calcGamma2/calcHpositive);
								float calcTotalAcidity = 2*calcH2CO3+calcHCO3+calcHpositive-calcKw/calcHpositive/(float)Math.pow(calcGamma1, 2);
								

}
}
