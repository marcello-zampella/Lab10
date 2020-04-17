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
	private Graph<Author, DefaultEdge> grafo;
	private List<Author> coautori;
	HashMap<Integer,Author> idMap;
	HashMap<Integer,Author> mappaNonCollegati;

	public void creaGrafo() {
		PortoDAO dao= new PortoDAO();
		idMap= new HashMap<Integer, Author>();
		for(Author a: dao.getAllAutori()) {
			idMap.put(a.getId(), a);
		}
		this.grafo= new SimpleGraph<Author, DefaultEdge>(DefaultEdge.class);
		Graphs.addAllVertices(grafo, idMap.values());
		for(CoAutoraggio c: dao.getAutoraggio()) {
			grafo.addEdge(idMap.get(c.getAutore1()),idMap.get(c.getAutore2()));
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
		DijkstraShortestPath<Author, DefaultEdge> dijkstra = new DijkstraShortestPath<Author, DefaultEdge>(this.grafo);
		GraphPath<Author, DefaultEdge> path= dijkstra.getPath(partenza,arrivo);
		if(path==null)
			return null;
		this.collegaAutoriArticoli(getAllPaper(), this.idMap);
		LinkedList<Author> cammino=new LinkedList<Author>(path.getVertexList());
		ArrayList<Paper> articolicomuni=new ArrayList<Paper>();
		for(int i=0;i<cammino.size()-1;i++) {
			articolicomuni.add(this.articoloComune(cammino.get(i),cammino.get(i+1)));
		}
		return articolicomuni;
	}
	
	private Paper articoloComune(Author author, Author author2) {
		//System.out.println(author.getPapers());
		for (Paper p: author.getPapers()) {
			if(author2.contieneArticolo(p))
				return p;
		}
		return null;
	}

	private HashMap<Integer, Paper> getAllPaper() {
		PortoDAO dao= new PortoDAO();
		HashMap<Integer,Paper> paperMap=new HashMap<Integer, Paper>();
		for(Paper p:dao.getAllPaper()) {
			paperMap.put(p.getEprintid(), p);
		}
		return paperMap;
	}
	
	private void collegaAutoriArticoli(HashMap<Integer,Paper>paperMap, HashMap<Integer,Author>autorimap) {
		PortoDAO dao= new PortoDAO();
		for(Author a: autorimap.values()) {
			for(Integer id:dao.getAllPaperByAuthor(a)) {
				Paper p=paperMap.get(id);
				p.aggiungiAutore(a);
				a.aggiungiPaper(p);
			}
		}
	}
}
