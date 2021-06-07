package primitives;

public class Material {
	public double kD=0, kS=0, kR=0, kT=0;
	
	public Material setkR(double kR) { //hishtakfut
		this.kR = kR;
		return this;
	}
	public Material setkT(double kT) { //shkifut
		this.kT = kT;
		return this;
	}
	public int nShininess=0;
	
	
	public Material setkD(double kD) {
		this.kD = kD;
		return this;
	}
	public Material setkS(double kS) {
		this.kS = kS;
		return this;
	}
	public Material setnShininess(int nShininess) {
		this.nShininess = nShininess;
		return this;
	}
	

}
