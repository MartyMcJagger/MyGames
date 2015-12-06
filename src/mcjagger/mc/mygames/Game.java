package mcjagger.mc.mygames;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

public abstract class Game extends Playable {
	
	public abstract String[] getAliases();
	
	
	private boolean running = false;
	
	public boolean isRunning() {
		return running;
	}
	
	// TODO: Warmup
	public boolean isWarmup() {
		return false;
	}
	
	public int maxPlayers = 20;
	public int minPlayers = 4;
	
	@Override
	public boolean canAddPlayer(UUID uuid) {
		return playerCount() < maxPlayers;
	}
	

	private Set<GameModule> gameModules = new HashSet<GameModule>();
	
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
	
	
	public void started(){}
	public final boolean start() {
		if (running)
			return false;
		
		running = true;
		
		for (GameModule module : gameModules)
			module.started();
		
		return true;
	}
	
	public void stopped(){}
	public final boolean stop() {
		if (!running)
			return false;
		
		running = false;
		
		for (GameModule module : gameModules)
			module.stopped();
		
		return true;
	}
	
	public void restart() {
		
		tellPlayers("Game is restarting!");
		
		stop();
		start();
	}
}
