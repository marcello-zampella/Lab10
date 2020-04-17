package it.polito.tdp.porto.model;

import org.jgrapht.graph.DefaultEdge;

public class DefaultEdgeMigliorato extends DefaultEdge {

	Paper paperComune;

	public Paper getPaperComune() {
		return paperComune;
	}

	public void setPaperComune(Paper paperComune) {
		this.paperComune = paperComune;
	}
	

}
