package mcjagger.mc.mygames;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public abstract class ConfigManager {
	
	public static final String GAME_PREFIX = "game.";
	public static final String SUFFIX_ENABLED = ".enabled";
	
	public static final String LISTENER_LOBBY = "listener.lobby.enabled";
	public static final String LISTENER_SIGN = "listener.sign.enabled";
	
	public static final String LOCATION_SIGN = "signs.locations";
	
	public static final String SPAWN_WORLD = "customSpawn.world.name";
	public static final String SPAWN_WORLD_ENABLED = "customSpawn.world.enabled";
	public static final String SPAWN_LOCATION = "customSpawn.location.loc";
	public static final String SPAWN_LOCATION_ENABLED = "customSpawn.location.enabled";
	
	public abstract ConfigurationSection loadConfig();
	public abstract void saveConfig();
	
	public abstract boolean isLobbyListenerEnabled();
	public abstract boolean isSignListenerEnabled();
	
	public abstract boolean isGameEnabled(String gameName);
	public abstract String getGameEnabledPath(String gameName);
	public abstract Location getSpawnLocation();
}
