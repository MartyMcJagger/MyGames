package mcjagger.mc.mygames.game;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class SurvivalPointManager extends DefaultPointManager {

	private Set<String> remaining = new HashSet<String>();
	private Set<String> eliminated = new HashSet<String>();
	
	private final Scoreboard scoreboard;
	
	public SurvivalPointManager(Ranking ranking) {
		super(ranking);
		
		scoreboard = null;
	}
	
	public SurvivalPointManager() {
		scoreboard = null;
	}
	
	public SurvivalPointManager(Ranking ranking, Scoreboard scoreboard) {
		super(ranking);
		this.scoreboard = scoreboard;
	}
	
	public SurvivalPointManager(Scoreboard scoreboard) {
		this.scoreboard = scoreboard;
	}
	
	public void eliminate(UUID uuid) {
		OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
		eliminated.add(op.getName());
		if (scoreboard != null) {
			
			// WE ARE USING TEAMS
			Team team = scoreboard.getEntryTeam(op.getName());
			Set<String> entries = team.getEntries();
			
			if (eliminated.containsAll(entries)) {
				remaining.remove(team.getName());
			}
			
		} else {
			remaining.remove(op.getName());
		}
	}
	
	public void revive(UUID uuid) {
		OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
		eliminated.remove(op.getName());
		if (scoreboard != null) {
			
			// WE ARE USING TEAMS
			Team team = scoreboard.getEntryTeam(op.getName());
			Set<String> entries = team.getEntries();
			
			if (!eliminated.containsAll(entries)) {
				remaining.add(team.getName());
			}
			
		} else {
			remaining.add(op.getName());
		}
	}
	
	public Collection<String> getRemaining() {
		return remaining;
	}
	
}
