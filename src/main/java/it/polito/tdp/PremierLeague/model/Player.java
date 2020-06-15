package it.polito.tdp.PremierLeague.model;

public class Player implements Comparable<Player>{
	Integer playerID;
	String name;
	private Double delta;
	
	public Player(Integer playerID, String name) {
		super();
		this.playerID = playerID;
		this.name = name;
		this.delta = 0.0;
	}
	
	public Integer getPlayerID() {
		return playerID;
	}
	public void setPlayerID(Integer playerID) {
		this.playerID = playerID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void addDelta(Double delta) {
		this.delta += delta;
	}
	public void resetDelta() {
		this.delta = 0.0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((playerID == null) ? 0 : playerID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (playerID == null) {
			if (other.playerID != null)
				return false;
		} else if (!playerID.equals(other.playerID))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return playerID + " - " + name + " | " + delta;
	}

	@Override
	public int compareTo(Player other) {
		return -this.delta.compareTo(other.delta);
	}
	
	
	
}
