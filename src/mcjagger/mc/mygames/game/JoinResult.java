package mcjagger.mc.mygames.game;

import mcjagger.mc.mygames.MyGames;

public enum JoinResult {
	
	SUCCESS(MyGames.getChatManager().joinLobbySuccess()),
	ALREADY_JOINED(MyGames.getChatManager().joinLobbyAlreadyJoined()),
	
	MAX_PLAYERS(MyGames.getChatManager().joinLobbyMaxPlayers()),
	IN_PROGRESS(MyGames.getChatManager().joinLobbyInProgress()),
	IN_OTHER_GAME(MyGames.getChatManager().joinLobbyIngame());
	
	private final String message;
	
	JoinResult(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String prefixedMessage(Game gm) {
		return MyGames.getChatManager().prefix(gm) + message;
	}
	
}
