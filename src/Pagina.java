
public class Pagina {
	private int numPag = -1;
	private boolean r = false;
	private boolean w = false;
	private int utilizado = 0;
	
	public int getNumPag() {
		return numPag;
	}
	public void setNumPag(int numPag) {
		this.numPag = numPag;
	}
	public boolean isR() {
		return r;
	}
	public void setR(boolean r) {
		this.r = r;
	}
	public boolean isW() {
		return w;
	}
	public void setW(boolean w) {
		this.w = w;
	}
	public int getUtilizado() {
		return utilizado;
	}
	public void setUtilizado(int utilizado) {
		this.utilizado = utilizado;
	}
	public String toString() {
		return "N:" + this.numPag + " R:" + this.r + " W:" + this.w;
	}
	
}
