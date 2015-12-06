package mcjagger.mc.mygames;

import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import mcjagger.mc.mygames.chat.ChatManager;
import mcjagger.mc.mygames.command.CommandMap;
import mcjagger.mc.mygames.command.MyGamesCommandMap;
import mcjagger.mc.mygames.world.MapConfigManager;
import mcjagger.mc.mygames.world.MapManager;

public class MyGames {

	private static CommandMap commandMap = new MyGamesCommandMap();
	private static Arcade arcade = null;
	private static Logger logger = null;
	
	
	
	public static void setArcade(Arcade arcade) {
		if (MyGames.arcade != null)
			throw new UnsupportedOperationException("Arcade has already been set!");
		
		MyGames.arcade = arcade;
		PluginCommand mygamesCommand = arcade.getCommand("mygames");
		if (mygamesCommand == null)
			getLogger().severe("MyGames command not found. Until added to plugin.yml, you will not be able to use /mygames.");
		else
			mygamesCommand.setExecutor(commandMap);
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
	public static MapManager getWorldManager() {
		return getArcade().getWorldManager();
	}
	public static MapConfigManager getMapConfigManager() {
		return getArcade().getWorldConfigManager();
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

	
	
	
}
