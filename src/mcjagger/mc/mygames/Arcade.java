package mcjagger.mc.mygames;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import mcjagger.mc.mygames.chat.ChatManager;
import mcjagger.mc.mygames.world.MapConfigManager;
import mcjagger.mc.mygames.world.MapManager;

public abstract class Arcade extends JavaPlugin {
		
	public abstract Set<String> getGames();

	@Override
	public void onEnable() {
		MyGames.setArcade(this);
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
	public abstract MapManager		getWorldManager();
	public abstract MapConfigManager 	getWorldConfigManager();
	
}
