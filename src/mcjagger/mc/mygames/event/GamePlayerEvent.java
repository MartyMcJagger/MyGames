package mcjagger.mc.mygames.event;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class GamePlayerEvent extends GameUpdateEvent {
	
	private final UUID uuid;
	
	public GamePlayerEvent(String game, GameEventType type, Player player) {
		super(game, type);
		
		this.uuid = player.getUniqueId();
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public OfflinePlayer getPlayer() {
		return Bukkit.getOfflinePlayer(uuid);
	}

}
