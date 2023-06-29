package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	private Graph<Airport,DefaultWeightedEdge > grafo; 
	private ExtFlightDelaysDAO dao;
	private List<Airport> listaAereoporti;
	private Map<Integer, Airport> airportIdMap;
	
	public Model() {
		dao= new ExtFlightDelaysDAO();
	}

	private void loadAllAirports(){
		this.listaAereoporti= new ArrayList<>(this.dao.loadAllAirports());
		this.airportIdMap= new HashMap<>();
		
		for(Airport a: listaAereoporti) 
			airportIdMap.put(a.getId(),a);

	}
	
	
	public void creaGrafoRotteMaggioriDi(double x) {
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		loadAllAirports();
		
		// Aggiungo i vertici 
		Graphs.addAllVertices(grafo, this.listaAereoporti);
		
		List<Flight> risultato= new ArrayList<>(this.dao.voliConTratteMaggioriDi(x, airportIdMap));
		
		for(Flight f: risultato)
			Graphs.addEdgeWithVertices(this.grafo,f.getPartenza(), f.getArrivo(),f.getMeanRoute());
		
		
	}

	public Graph<Airport, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}
	
	
	
}
