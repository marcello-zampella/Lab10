package it.polito.tdp.porto.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.porto.db.PortoDAO;

public class Model {
	private Graph<Author, DefaultEdge> grafo;

	public void creaGrafo() {
		PortoDAO dao= new PortoDAO();
		HashMap<Integer,Author> idMap= new HashMap<Integer, Author>();
		for(Author a: dao.getAllAutori()) {
			idMap.put(a.getId(), a);
		}
		this.grafo= new SimpleGraph<Author, DefaultEdge>(DefaultEdge.class);
		Graphs.addAllVertices(grafo, idMap.values());
		for(CoAutoraggio c: dao.getAutoraggio()) {
			grafo.addEdge(idMap.get(c.getAutore1()),idMap.get(c.getAutore2()));
	}
	}
	
	public ArrayList<Author> cercaCoautori(Author a) {
		ArrayList<Author> coautori=new ArrayList<Author>();
		for(DefaultEdge e: grafo.edgesOf(a)) {
			Author autoretemp=null;
			if(!grafo.getEdgeSource(e).equals(a))
				autoretemp=grafo.getEdgeSource(e);
			else
				autoretemp=grafo.getEdgeTarget(e);
			if(autoretemp!=null)
				coautori.add(autoretemp);
		}
		return coautori;
	}
	
	public Set<Author> getAutori() {
		return this.grafo.vertexSet();
	}
	

}
