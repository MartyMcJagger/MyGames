package mcjagger.mc.mygames;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public abstract class Game extends Playable {

	public abstract String[] getAliases();
	public Location getSpawnLocation(Player player){return MyGames.getWorldManager().getRandomSpawn(this);}
	
	
	private GameListener gameListener = new GameListener(this);
	private Set<GameModule> gameModules = new HashSet<GameModule>();
	
	private boolean running = false;
	private boolean warmingUp = false;
	
	public int maxPlayers = 20;
	public int minPlayers = 4;
	
	
	
	public void started(){}
	public final boolean start() {
		if (running)
			return false;
		
		running = true;
		
		MyGames.getLobbyManager().sendPlayers(this);
		Bukkit.getPluginManager().registerEvents(gameListener, MyGames.getArcade());
		
		for (GameModule module : gameModules)
			module.started();
		
		return true;
	}
	
	public void stopped(){}
	public final boolean stop() {
		if (!running)
			return false;
		
		running = false;
		
		
		HandlerList.unregisterAll(gameListener);
		
		for (GameModule module : gameModules)
			module.stopped();
		
		return true;
	}
	
	public void restart() {
		
		tellPlayers("Game is restarting!");
		
		stop();
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
		return playerCount() < maxPlayers;
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
}
