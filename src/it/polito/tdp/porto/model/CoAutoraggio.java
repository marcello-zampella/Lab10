package it.polito.tdp.porto.model;

public class CoAutoraggio {
	private int autore1;
	private int autore2;
	public int getAutore1() {
		return autore1;
	}
	public void setAutore1(int autore1) {
		this.autore1 = autore1;
	}
	public int getAutore2() {
		return autore2;
	}
	public void setAutore2(int autore2) {
		this.autore2 = autore2;
	}
	public CoAutoraggio(int i, int j) {
		super();
		this.autore1 = i;
		this.autore2 = j;
	}

}
