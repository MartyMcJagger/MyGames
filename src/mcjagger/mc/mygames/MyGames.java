package mcjagger.mc.mygames;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import mcjagger.mc.mygames.chat.ChatManager;
import mcjagger.mc.mygames.command.CommandMap;
import mcjagger.mc.mygames.command.MyGamesCommandMap;
import mcjagger.mc.mygames.world.MapConfigManager;
import mcjagger.mc.mygames.world.MapManager;

public class MyGames {

	private static CommandMap commandMap = new MyGamesCommandMap();
	private static Arcade arcade = null;
	private static Logger logger = null;
	
	private static Set<UUID> debuggers = new HashSet<UUID>();
	
	
	public static void setArcade(Arcade arcade) {
		if (MyGames.arcade != null)
			throw new UnsupportedOperationException("Arcade has already been set!");
		
		MyGames.arcade = arcade;
	}
	
	public static Arcade getArcade() {
		if (arcade == null)
			throw new UnsupportedOperationException("Arcade has not yet been set!");
		
		return MyGames.arcade;
	}
	
	public static Logger getLogger() {
		if (logger == null)
			logger = Logger.getLogger("MyGames");
		
		return logger;
	}
	
	
	public static CommandMap getCommandMap() {
		return commandMap;
	}

	public static boolean registerCommand(String label, Command command) {
		if (commandMap.getCommand(label) == null) {
			commandMap.register(label, command);
			return true;
		}
		
		return false;
	}
	
	
	
	public static ChatManager getChatManager() {
		return getArcade().getChatManager();
	}
	public static ConfigManager getConfigManager() {
		return getArcade().getConfigManager();
	}
	public static LobbyManager getLobbyManager() {
		return getArcade().getLobbyManager();
	}
	public static MetadataManager getMetadataManager() {
		return getArcade().getMetadataManager();
	}
	public static MapManager getMapManager() {
		return getArcade().getMapManager();
	}
	public static MapConfigManager getMapConfigManager() {
		return getArcade().getMapConfigManager();
	}

	
	
	public static boolean toLobby(Player player) {
		return getArcade().toLobby(player);
	}
	public static boolean toSetup(Player player) {
		return getArcade().toSetup(player);
	}
	public static Location getSpawnLocation() {
		return getArcade().getSpawnLocation();
	}

	public static Plugin[] enableSubplugins(File file) {
		if (!file.exists())
			file.mkdirs();
		Plugin[] plugins = Bukkit.getPluginManager().loadPlugins(file);
		for (Plugin plugin : plugins) {
			getArcade().getPluginLoader().enablePlugin(plugin);
		}
		return plugins;
	}

	public static void disableSubplugins(Plugin[] plugins) {
		for (Plugin plugin : plugins)
			getArcade().getPluginLoader().disablePlugin(plugin);
	}
	
	
	public static void setDebugging(UUID uuid, boolean debug) {
		if (debug) {
			debuggers.add(uuid);
		} else {
			debuggers.remove(uuid);
		}
	}
	
	public static void debug(Object o) {
		String msg = o.toString();
		
		for (UUID uuid : debuggers) {
			Player player = Bukkit.getPlayer(uuid);
			if (player == null) {
				debuggers.remove(player);
				continue;
			}
			
			player.sendMessage(msg);
		}
	}
}
