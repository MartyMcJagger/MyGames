package mcjagger.mc.mygames;

import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class LobbyManager implements Listener {
	
	public abstract void joinedGame(Player player, Game game);
	public abstract void leftGame(Player player, Game game);
	
	public abstract String getCurrentGame(UUID uuid);
	public abstract String getCurrentGame(Player player);
	
	public abstract boolean addPlayer(Player player, String gameName);
	public abstract boolean removePlayer(Player player, String gameName);

	public abstract void sendPlayers(Game game);
	
	public abstract Game getGame(String game);
	
	public abstract boolean addGame(Game game);
	public abstract boolean removeGame(Game gm);
	
	public abstract boolean startGame(String gameName);
	public abstract boolean stopGame(String gameName);
	public abstract boolean restartGame(String gameName);
	
	public abstract boolean hasGame(String game);
	
	public abstract Set<String> getGames();
	public abstract Set<String> getGameNames();

	public abstract void stopAll();
}
