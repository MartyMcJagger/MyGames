package mcjagger.mc.mygames;

import org.bukkit.entity.Player;

public abstract class MetadataManager {

	public static final String KEY = "mygames";
	public static final String MODE = "mygames.mode";
	public static final String TEAM = "mygames.team";
	public static final String CLASS = "mygames.class";
	
	public static final String GAME = "game";
	
	public static final int OTHER_GAME = -1;
	public static final int INGAME = 0;
	public static final int LOBBY = 1;
	public static final int SETUP = 2;

	public abstract boolean setInGame(Player player, String gameName);
	public abstract boolean setInSetup(Player player);
	public abstract boolean setInLobby(Player player);
	public abstract void setInOther(Player player);
	
	public abstract int getMode(Player player);
	public abstract String getGame(Player player);
	public abstract boolean inOtherGame(Player player);
	public abstract void remove(Player player);

}
