package mcjagger.mc.mygames;

import java.io.File;
import java.util.Arrays;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import mcjagger.mc.mygames.chat.ChatManager;
import mcjagger.mc.mygames.game.Game;
import mcjagger.mc.mygames.inventorymenu.InventoryMenuListener;
import mcjagger.mc.mygames.world.MapConfigManager;
import mcjagger.mc.mygames.world.MapManager;

public abstract class Arcade extends JavaPlugin {

	public abstract Set<String> getGames();
	
	private Plugin[] plugins;
	public final Plugin[] getPlugins(){return this.plugins;}
	
	@Override
	public void onLoad() {
		MyGames.setArcade(this);
		plugins = MyGames.loadSubplugins(new File(this.getDataFolder().getAbsolutePath() + File.separator + "plugins"));
		MyGames.getLogger().info("Found sub-plugins: " + Arrays.toString(plugins));
	}
	
	@Override
	public void onEnable() {		
		PluginCommand mygamesCommand = getCommand("mygames");
		if (mygamesCommand == null)
			getLogger().severe("MyGames command not found. Until added to plugin.yml, you will not be able to use /mygames.");
		else
			mygamesCommand.setExecutor(MyGames.getCommandMap());
		
		Bukkit.getPluginManager().registerEvents(new InventoryMenuListener(), this);
		
		MyGames.enableSubplugins();
	}
	
	@Override
	public void onDisable() {
		getLobbyManager().stopAll();
		
		MyGames.disableSubplugins(getPlugins());
	}
	
	public void setEnabled(Game game, boolean enabled) {
		
		if (!isLoaded(game))
			MyGames.getLogger().warning(String.format("Tried to change enabled state of unknown game '%s'", game.getName()));
		
		if (enabled)
			enable(game);
		else
			disable(game);
	}
	
	public abstract boolean load(Game game);
	public abstract boolean enable(Game game);
	public abstract boolean disable(Game game);

	public abstract Game getGame(String gameName);

	public abstract boolean isLoaded(Game game);
	public abstract boolean isEnabled(Game game);
	
	public abstract Game getCurrentGame(Player player);
	public abstract boolean joinGame(Player player, Game game);

	public abstract boolean toLobby(Player player);
	public abstract boolean toSetup(Player player);
	
	public abstract Location getSpawnLocation();
	
	public abstract ChatManager  		getChatManager();
	public abstract ConfigManager		getConfigManager();
	public abstract LobbyManager		getLobbyManager();
	public abstract MetadataManager		getMetadataManager();
	public abstract MapManager   		getMapManager();
	public abstract MapConfigManager 	getMapConfigManager();
	public abstract ScoreboardSwitcher	getScoreboardSwitcher();

	public abstract Scoreboard getDefaultScoreboard();
	
}
