package mcjagger.mc.mygames;

import org.bukkit.scoreboard.Scoreboard;

public interface ScoreboardProvider {
	
	public String getName();
	public Scoreboard getScoreboard();

}
