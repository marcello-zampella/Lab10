package it.polito.tdp.porto.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.porto.db.PortoDAO;

public class Model {
	private Graph<Author, DefaultEdgeMigliorato> grafo;
	private List<Author> coautori;
	HashMap<Integer,Author> idMap;
	HashMap<Integer,Author> mappaNonCollegati;

	public void creaGrafo() {
		PortoDAO dao= new PortoDAO();
		idMap= new HashMap<Integer, Author>();
		for(Author a: dao.getAllAutori()) {
			idMap.put(a.getId(), a);
		}
		this.grafo= new SimpleGraph<Author, DefaultEdgeMigliorato>(DefaultEdgeMigliorato.class);
		Graphs.addAllVertices(grafo, idMap.values());
		for(CoAutoraggio c: dao.getAutoraggio()) {
			Author a1=idMap.get(c.getAutore1());
			Author a2=idMap.get(c.getAutore2());
			DefaultEdgeMigliorato arco=grafo.addEdge(a1,a2);
			if(arco!=null)
				arco.setPaperComune(dao.getPaperComune(a1, a2));
	}
	}
	
	public List<Author> cercaCoautori(Author a) {
		coautori=Graphs.neighborListOf(this.grafo, a);
		mappaNonCollegati=(HashMap<Integer, Author>) idMap.clone();
		for(Author au: coautori) {
			mappaNonCollegati.remove(au.getId());
		}
		mappaNonCollegati.remove(a.getId());
		return coautori;
	}
	
	public ArrayList<Author> autoriNonCollegati() {
		return new ArrayList(mappaNonCollegati.values());
	}
	
	public Set<Author> getAutori() {
		return this.grafo.vertexSet();
	}

	public ArrayList<Paper> getCammino(Author partenza, Author arrivo) {
		DijkstraShortestPath<Author, DefaultEdgeMigliorato> dijkstra = new DijkstraShortestPath<Author, DefaultEdgeMigliorato>(this.grafo);
		GraphPath<Author, DefaultEdgeMigliorato> path= dijkstra.getPath(partenza,arrivo);
		if(path==null)
			return null;
		LinkedList<Author> cammino=new LinkedList<Author>(path.getVertexList());
		ArrayList<Paper> articoli=new ArrayList<Paper>();
		for(DefaultEdgeMigliorato e: path.getEdgeList()) {
			articoli.add(e.getPaperComune());
		}
		return articoli;
	}
	

}
