package mcjagger.mc.mygames.chat;

import java.util.Collection;
import java.util.List;

import org.bukkit.entity.Player;

import mcjagger.mc.mygames.game.Game;

public interface ChatManager {
	
	String prefix();
	String prefix(Game game);
	
	String joinServer(Player player);
	String leaveServer(Player player);

	String playerDeath(Player player);
	String playerDeath(Player player, Player killer);
	
	String joinLobbySuccess(Game gm);
	String leaveLobby(Game gm);

	String joinLobbySuccess();
	String leaveLobby();
	
	String joinLobbyAlreadyJoined(Game gm);
	String joinLobbyFull(Game gm);
	String joinLobbyIngame(Game gm);
	String joinLobbyInProgress(Game gm);
	String joinLobbyMaxPlayers(Game gm);
	
	String joinLobbyAlreadyJoined();
	String joinLobbyFull();
	String joinLobbyIngame();
	String joinLobbyInProgress();
	String joinLobbyMaxPlayers();

	String gameNotFound(String gameName);
	String gameHasNoMaps(Game gm);
	String gameStart(Game gm);
	String gameOver(Game gm, List<Collection<String>> list);
	
	String commandNotRegistered();
	String actionNotAllowed();
	String errorOccurred();
	String changeMap(Game game, String mapName);
}
