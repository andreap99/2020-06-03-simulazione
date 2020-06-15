package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
	public List<Player> listAllPlayers(){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void loadPlayer(Map<Integer, Player> idMap) {
		String sql = "SELECT * FROM Players";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				idMap.put(player.getPlayerID(), player);
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Player> getPlayers(Double goals, Map<Integer, Player> idMap) {
		
		final String sql = "SELECT PlayerID " + 
				"FROM actions " + 
				"GROUP BY PlayerID " + 
				"HAVING AVG(Goals) > ?";
		List<Player> result = new ArrayList<>();
		
		try {
			
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, goals);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				result.add(idMap.get(rs.getInt("PlayerID")));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public List<Adiacenza> getAdiacenze(Double goals, Map<Integer, Player> idMap) {
		
		final String sql = "SELECT a1.PlayerID AS p1, a2.PlayerID AS p2, MAX(a1.TimePlayed - a2.TimePlayed) AS delta " + 
				"FROM actions AS a1, actions AS a2 " + 
				"WHERE a1.PlayerID <> a2.PlayerID AND " + 
				"a1.MatchID = a2.MatchID AND " + 
				"a1.`Starts` = 1 AND a2.`Starts` = 1 AND " + 
				"a1.TimePlayed > a2.TimePlayed AND a1.TeamID <> a2.TeamID AND " + 
				"a1.PlayerID IN (SELECT PlayerID " + 
				"FROM actions " + 
				"GROUP BY PlayerID " + 
				"HAVING AVG(Goals) > ?) AND " + 
				"a2.PlayerID IN (SELECT PlayerID " + 
				"FROM actions " + 
				"GROUP BY PlayerID " + 
				"HAVING AVG(Goals) > ?) " + 
				"GROUP BY a1.PlayerID, a2.PlayerID";
		List<Adiacenza> adiacenze = new ArrayList<>();
		
		try {
			
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, goals);
			st.setDouble(2, goals);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				Player primo = idMap.get(rs.getInt("p1"));
				Player secondo = idMap.get(rs.getInt("p2"));
				adiacenze.add(new Adiacenza(primo, secondo,(double) rs.getInt("delta")));
			}
			conn.close();
			return adiacenze;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	
	}
}
