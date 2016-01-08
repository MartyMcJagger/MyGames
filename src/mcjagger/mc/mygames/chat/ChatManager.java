package mcjagger.mc.mygames.chat;

import java.util.Collection;

import org.bukkit.entity.Player;

import mcjagger.mc.mygames.game.Game;

public interface ChatManager {
	
	String joinServer(Player player);
	String leaveServer(Player player);

	String playerDeath(Player player);
	String playerDeath(Player player, Player killer);
	
	String joinLobbySuccess(Game gm);
	String leaveLobby(Game gm);
	
	String joinLobbyAlreadyJoined(Game gm);
	String joinLobbyFull(Game gm);
	String joinLobbyIngame(Game gm);
	String joinLobbyInProgress(Game gameManager);

	String gameNotFound(String gameName);
	String gameHasNoWorlds(Game gm);
	String gameStart(Game gm);
	String gameOver(Game gm, Collection<String> winners);
	
	String commandNotRegistered();
	String actionNotAllowed();
	String errorOccurred();
}
