package mcjagger.mc.mygames.game;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;

import mcjagger.mc.mygames.Module;
import mcjagger.mc.mygames.MyGames;
import mcjagger.mc.mygames.Playable;
import mcjagger.mc.mygames.ScoreboardProvider;

public abstract class Game extends Playable implements ScoreboardProvider {
	
	public abstract String[] getAliases();
	//public abstract PointManager getPointManager();
	public abstract List<Collection<String>> getWinners();
	
	public void preparePlayer(Player player){}
	public void respawnPlayer(Player player){
		if (isRunning()) {
			preparePlayer(player);player.teleport(getSpawnLocation(player));
		}
		else {
			MyGames.setSpectating(player, this);
		}
	}
	
	public Location getSpawnLocation(Player player){return MyGames.getMapManager().getRandomSpawn(this);}
	public boolean allowInventory(){return false;}
	
	private GameListener gameListener = new GameListener(this);
	private Set<GameModule> gameModules = new HashSet<GameModule>();
	
	private Queue<Scoreboard> scoreboards = new LinkedList<Scoreboard>();
	private Scoreboard scoreboard = null;
	
	public GameState state = GameState.STOPPED;
	
	public int maxPlayers = 20;
	public int minPlayers = 4;
	
	public boolean allowBlockBreak = false;
	public boolean allowBlockPlace = false;
	public boolean allowInventory = false;
	public boolean allowJoinInProgress = true;
	
	public Game() {
		scoreboards.add(getScoreboard());
	}
	
	@Override
	public final void addedPlayer(UUID uuid) {
	    MyGames.getLobbyManager().joinedGame(Bukkit.getPlayer(uuid), this);
	}
	
	public void started(){}
	public final boolean start() {
		if (!state.canStart())
			return false;
		
		state = GameState.RUNNING;
		
		MyGames.getLobbyManager().sendPlayers(this);
		Bukkit.getPluginManager().registerEvents(gameListener, MyGames.getArcade());
		
		for (GameModule module : gameModules)
			module.started();
		
		started();
		
		tellPlayers(MyGames.getChatManager().gameStart(this));
		
		return true;
	}
	
	public void stopped(){}
	public final boolean stop() {
		return stop(true, true);
	}
	
	private BukkitTask cooldownTask = null;
	public final boolean announceWinnersAndStop() {
		if (!state.canStop())
			return false;
		
		if (state == GameState.COOLING_DOWN)
			return false;
		
		sendTitle("Game Over!", "See Chat for Winners!");
		tellWorld(MyGames.getChatManager().gameOver(this, getWinners()));
		
		for (Module module : getModules())
			if (module instanceof GameModule)
				((GameModule)module).stopping();
		
		cooldownTask = Bukkit.getScheduler().runTaskLater(MyGames.getArcade(), new Runnable(){
			
			@Override
			public void run() {
				stop(true, false);
			}
			
		}, 15 * 20);
		
		return true;
	}
	
	public final boolean stop(boolean unloadMap, boolean announceWinners) {
		if (!state.canStop())
			return false;
		
		state = GameState.STOPPED;
		
		if (cooldownTask != null && Bukkit.getScheduler().isQueued((cooldownTask.getTaskId()))) {
			cooldownTask.cancel();
			cooldownTask = null;
		}
		
		MyGames.getLobbyManager().retrievePlayers(this);
		HandlerList.unregisterAll(gameListener);
		
		for (GameModule module : gameModules)
		module.stopped();
		
		if (announceWinners) {
			tellWorld(MyGames.getChatManager().gameOver(this, getWinners()));
		}
		
		for (Scoreboard scoreboard : scoreboards) {
			for (String entry : scoreboard.getEntries())
				scoreboard.resetScores(entry);
		}
		
		if (unloadMap) {
			MyGames.getMapManager().unloadMap(this, true);
		}
		
		stopped();
		
		return true;
	}
	
	public void restart() {
		
		tellPlayers("Game is restarting!");
		
		stop(false, false);
		start();
	}
	
	
	public boolean isRunning() {
		return state.isRunning();
	}
	
	@Override
	public JoinResult canAddPlayer(UUID uuid) {
		
		if (playerCount() >= maxPlayers) {
			return JoinResult.MAX_PLAYERS;
		} else if (isRunning() && !allowJoinInProgress) {
			return JoinResult.IN_PROGRESS;
		}
		return JoinResult.SUCCESS;
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
