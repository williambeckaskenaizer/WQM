public class beep{
	public static void main(String args[]){
	System.out.println("Testing equations in java:");

	float temperature;
	float tds; 
	float pH;
	float alkalinity;
	float phTarget;
	float finalAlkalinity;
	float ct;
	float alpha1;
	float alpha2;

	temperature = 15.0f;
	tds = 57f;
	pH = 8.5f;
	alkalinity = 35f;
	phTarget = 10.3f;

	alpha1 = (float)Math.pow(10,(-0.0025*Math.sqrt(tds)));
	alpha2 = (float)Math.pow(10,((-0.01*Math.sqrt(tds))));

	float k1 = (float)Math.pow(10f,(-1*(356.309-21834.4/(273+temperature)-126.834*Log10(273f+temperature)+0.06092*(273+temperature)+1684915/Math.pow((273.0f+temperature),2))));
	float k2 = (float)Math.pow(10,(-1*(107.887-5151.8/(273+temperature)-38.926*Log10(273+temperature)+0.032528*(273+temperature)+563713.9/Math.pow((273+temperature),2))));
	float kw = (float)Math.pow(10,(-1*(-6.088+4471/(273+temperature)+0.01706*(273+temperature))));

	ct = (float)((1+alpha1*Math.pow(10,(-pH))/k1 + alpha1*k2/alpha2/Math.pow(10, (-pH)))*((alkalinity/50000-kw/alpha1/Math.pow(10,(-pH))+Math.pow(10, (-pH))/alpha1)/(1+2*k2*alpha1/alpha2/Math.pow(10,(-pH)))));
				//(1+gamma1*10^(-pHi)/K_1+gamma1*K_2/gamma2/10^(-pHi))*((alki/50000-Kw/gamma1/10^(-pHi)+10^(-pHi)/gamma1)/(1+2*K_2*gamma1/gamma2/10^(-pHi)))

	finalAlkalinity = (float)(50000*(ct*(1+2*k2*alpha1/alpha2/Math.pow(10,(-phTarget)))/(1+alpha1*Math.pow(10,(-phTarget))/k1 +alpha1*k2/alpha2/Math.pow(10,(-phTarget)))+kw/alpha1/Math.pow(10,(-phTarget))-Math.pow(10,(-phTarget))/alpha1));

	float k1pk = -Log10(k1);
	float k2pk = -Log10(k2);
	float kwpk = -Log10(kw);

	//float h2so4 = sulfuricAcidCalc(phTarget, pH, );

	System.out.println("K1 = " + k1);
	System.out.println("K2 = " + k2);
	System.out.println("Kw = " + kw);

	System.out.println("k1 pK = " + k1pk);
	System.out.println("k2 pK = " + k2pk);
	System.out.println("kw pK = " + kwpk);

	System.out.println("Alpha1 = " + alpha1);
	System.out.println("Alpha2 = " + alpha2);

	System.out.println("Final Alkalinity = " + finalAlkalinity);
	System.out.println("CT = " + ct);


	System.out.println("H2SO4 = " + "");
	}

	public static float Log10(float x){
		return (float) Math.log10(x);
	}
	public static float sulfuricAcidCalc(){
		// if(pH > phTarget){
		// 	return (49/50)*
		// }
		return 0.0f;
	}
}