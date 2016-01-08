package mcjagger.mc.mygames.game;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.scoreboard.Scoreboard;

import mcjagger.mc.mygames.Module;
import mcjagger.mc.mygames.MyGames;
import mcjagger.mc.mygames.Playable;
import mcjagger.mc.mygames.ScoreboardProvider;

public abstract class Game extends Playable implements ScoreboardProvider {

	public abstract String[] getAliases();
	//public abstract PointManager getPointManager();
	
	public void preparePlayer(Player player){}
	public void respawnPlayer(Player player){preparePlayer(player);player.teleport(getSpawnLocation(player));}
	public Location getSpawnLocation(Player player){return MyGames.getMapManager().getRandomSpawn(this);}
	public boolean allowInventory(){return false;}
	
	private GameListener gameListener = new GameListener(this);
	private Set<GameModule> gameModules = new HashSet<GameModule>();
	
	private Queue<Scoreboard> scoreboards = new LinkedList<Scoreboard>();
	private Scoreboard scoreboard = null;
	
	private boolean running = false;
	private boolean warmingUp = false;
	
	public int maxPlayers = 20;
	public int minPlayers = 4;
	
	public boolean allowBlockBreak = false;
	public boolean allowBlockPlace = false;
	public boolean allowInventory = false;
	public boolean allowJoinInProgress = true;
	
	public Game() {
		scoreboards.add(getScoreboard());
	}
	
	public void started(){}
	public final boolean start() {
		if (running)
			return false;
		
		running = true;
		
		MyGames.getLobbyManager().sendPlayers(this);
		Bukkit.getPluginManager().registerEvents(gameListener, MyGames.getArcade());
		
		for (GameModule module : gameModules)
			module.started();
		
		started();
		
		return true;
	}
	
	public void stopped(){}
	public final boolean stop() {
		return stop(true);
	}
	public final boolean stop(boolean clearPlayers) {
		if (!running)
			return false;
		
		running = false;
		
		MyGames.getLobbyManager().retrievePlayers(this);
		HandlerList.unregisterAll(gameListener);
		
		for (GameModule module : gameModules)
			module.stopped();
		
		for (Scoreboard scoreboard : scoreboards) {
			for (String entry : scoreboard.getEntries())
				scoreboard.resetScores(entry);
		}
		
		MyGames.getMapManager().unloadMap(this, true);
		
		stopped();
		
		return true;
	}
	
	public void restart() {
		
		tellPlayers("Game is restarting!");
		
		stop(false);
		start();
	}
	
	
	public boolean isRunning() {
		return running;
	}
	public boolean isWarmup() {
		return warmingUp;
	}
	
	@Override
	public boolean canAddPlayer(UUID uuid) {
		return playerCount() < maxPlayers && (!isRunning() || allowJoinInProgress);
	}
	
	@Override
	public boolean addModule(Module module) {
		if (super.addModule(module) && (module instanceof GameModule)) {
			gameModules.add((GameModule) module);
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean removeModule(Module module) {
		if (super.removeModule(module) && (module instanceof GameModule)) {
			gameModules.remove(module);
			return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * @return Main Scoreboard for scores and teams 
	 */
	public Scoreboard getScoreboard() {
		if (scoreboard == null)
			scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		
		return scoreboard;
	}
	
	@Override
	public Scoreboard nextScoreboard() {
		Scoreboard scoreboard = scoreboards.remove();
		scoreboards.add(scoreboard);
		
		MyGames.debug("Returning 1 of " + scoreboards.size() + " scoreboards.");
		
		return scoreboard;
	}
	
	@Override
	public boolean hasScoreboard() {
		return scoreboards.size() > 0;
	}
	
	public void addScoreboard(Scoreboard scoreboard) {
		scoreboards.add(scoreboard);
	}
}