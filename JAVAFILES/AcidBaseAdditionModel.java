public class AcidBaseAdditionModel {
public static void main(String args[]){
								System.out.println("Testing equations in java:");

								double temperature;
								double tds;
								double pH;
								double alkalinity;
								double phTarget;
								double finalAlkalinity;
								double ct;
								double alpha1;
								double alpha2;

								temperature = 10.0;
								tds = 57;
								pH = 8.5;
								alkalinity = 35;
								phTarget = 10.3;

								alpha1 = Math.pow(10,(-0.0025*Math.sqrt(tds)));
								alpha2 = Math.pow(10,((-0.01*Math.sqrt(tds))));

								double k1 = Math.pow(10,(-1*(356.309-21834.4/(273+temperature)-126.834*Log10(273+temperature)+0.06092*(273+temperature)+1684915/Math.pow((273.0+temperature),2))));
								double k2 = Math.pow(10,(-1*(107.887-5151.8/(273+temperature)-38.926*Log10(273+temperature)+0.032528*(273+temperature)+563713.9/Math.pow((273+temperature),2))));
								double kw = Math.pow(10,(-1*(-6.088+4471/(273+temperature)+0.01706*(273+temperature))));

								ct = ((1+alpha1*Math.pow(10,(-pH))/k1 + alpha1*k2/alpha2/Math.pow(10, (-pH)))*((alkalinity/50000-kw/alpha1/Math.pow(10,(-pH))+Math.pow(10, (-pH))/alpha1)/(1+2*k2*alpha1/alpha2/Math.pow(10,(-pH)))));
								finalAlkalinity = (50000*(ct*(1+2*k2*alpha1/alpha2/Math.pow(10,(-phTarget)))/(1+alpha1*Math.pow(10,(-phTarget))/k1 +alpha1*k2/alpha2/Math.pow(10,(-phTarget)))+kw/alpha1/Math.pow(10,(-phTarget))-Math.pow(10,(-phTarget))/alpha1));

								double k1pk = -Log10(k1);
								double k2pk = -Log10(k2);
								double kwpk = -Log10(kw);

								double h2so4 = sulfuricAcidCalc(pH, phTarget, alkalinity, finalAlkalinity);

								System.out.println("K1 = " + k1);
								System.out.println("K2 = " + k2);
								System.out.println("Kw = " + kw);

								System.out.println("k1 pK = " + k1pk);
								System.out.println("k2 pK = " + k2pk);
								System.out.println("kw pK = " + kwpk);

								System.out.println("Alpha1 = " + alpha1);
								System.out.println("Alpha2 = " + alpha2);

								System.out.println("Final Alkalinity = " + Math.round(finalAlkalinity));
								System.out.println("CT = " + ct);

								System.out.println("H2SO4 = " + h2so4);
}

public static double Log10(double x){
								return  Math.log10(x);
}
public static double sulfuricAcidCalc(double pH, double phTarget, double alkalinity, double finalAlkalinity){
								if(phTarget < pH) {
																return (49.0/50.0)*(alkalinity - finalAlkalinity);
								}else{
																return 0.0;
								}
}
}
