package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao;
	private Graph<Player, DefaultWeightedEdge> grafo;
	private Map<Integer, Player> idMap;
	
	public Model() {
		
		this.dao = new PremierLeagueDAO();
		this.idMap = new HashMap<>();
		
	}
	
	public String creaGrafo(Double goals) {
		
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		this.dao.loadPlayer(idMap);
		Graphs.addAllVertices(grafo, this.dao.getPlayers(goals, idMap));
		//for(Player p : grafo.vertexSet())
			//System.out.println(p);
		for(Adiacenza a : this.dao.getAdiacenze(goals, idMap)) {
			Graphs.addEdge(grafo, a.getP1(), a.getP2(), a.getPeso());
		}
		
		return String.format("Grafo creato! %d vertici e %d archi.\n", grafo.vertexSet().size(), grafo.edgeSet().size());
	}

	public String topPlayer() {
		int best = 0;
		Player topPlayer = null;
		List<Player> battuti = new ArrayList<>();
		for(Player p : grafo.vertexSet()) {
			if(grafo.outDegreeOf(p)>best) {
				best = grafo.outDegreeOf(p);
				topPlayer = p;
			}
		}
		String output = "TopPlayer: "+topPlayer.getName() +"\nLista giocatori battuti:\n";
		for(DefaultWeightedEdge d : this.grafo.outgoingEdgesOf(topPlayer)) {
			Player out = this.grafo.getEdgeTarget(d);
			out.resetDelta();
			out.addDelta(this.grafo.getEdgeWeight(d));
			battuti.add(out);
		}
		Collections.sort(battuti);
		for(Player x : battuti) {
			output += x.toString() + "\n";
		}
		
		return output;
	}

}
