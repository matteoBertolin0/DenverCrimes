package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	private Graph<String,DefaultWeightedEdge> grafo;
	private EventsDao dao;
	
	private List<String> best;
	
	public Model() {
		this.dao = new EventsDao();
	}
	
	public void creaGrafo(String categoria, int mese) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//aggiunta vertici
		Graphs.addAllVertices(this.grafo, dao.getVertici(categoria, mese));
		//aggiunta archi
		for(Adiacenza a : dao.getArchi(categoria, mese)) {
			Graphs.addEdgeWithVertices(this.grafo, a.getV1(), a.getV2(),a.getPeso());
		}
		
		System.out.println("Grafo creato:");
		System.out.println("#VERTICI: " + this.grafo.vertexSet().size());
		System.out.println("#ARCHI: " + this.grafo.edgeSet().size());
	}
	
	public List<Adiacenza> getArchiMaggioriPesoMedio(){
		double avg = 0.0;
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			avg += this.grafo.getEdgeWeight(e);
		}
		List<Adiacenza> res = new ArrayList<>();
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(e)>(avg/this.grafo.edgeSet().size())) {
				res.add(new Adiacenza(this.grafo.getEdgeSource(e),this.grafo.getEdgeTarget(e), (int) this.grafo.getEdgeWeight(e)));
			}
		}
		System.out.println("Peso medio: "+ avg/this.grafo.edgeSet().size());
		return res;
	}
	
	public List<String> calcolaPercorso(String sorgente, String destinazione){
		best = new LinkedList<>();
		List<String> parziale = new LinkedList<>();
		parziale.add(sorgente);
		cerca(parziale, destinazione);
		return best;
	}

	private void cerca(List<String> parziale, String destinazione) {
		//condizione di terminazione
		if(parziale.get(parziale.size()-1).equals(destinazione)) {
			if(parziale.size()>best.size()) {
				best = new LinkedList<>(parziale);
			}
			return;
		}
		
		for(String v : Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))) {
			if(!parziale.contains(v)) {
				parziale.add(v);
				cerca(parziale, destinazione);
				parziale.remove(parziale.size()-1);				
			}
			System.out.println(v);
		}
	}
	
}
