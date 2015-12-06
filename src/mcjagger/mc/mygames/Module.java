package mcjagger.mc.mygames;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public abstract class Module {
	
	protected final Playable playable;
	public final boolean removable;
	
	public Module(Playable playable) {
		this.playable = playable;
		this.removable = true;
	}
	public Module(Playable playable, boolean removable) {
		this.playable = playable;
		this.removable = removable;
	}
	
	
	protected final Playable getPlayable() {
		return playable;
	}

	
	/**
	 * Prepare for start. This occurs before any players are in the Playable.
	 */
	public void loaded(){}
	
	public void addedPlayer(Player player){}
	public void removedPlayer(Player player){}

	public void pointScored(UUID uuid, Location location){}
	public void playerDied(UUID uuid) {}
	public void playerDamaged(EntityDamageEvent event){}
	public void playerDamagedPlayer(EntityDamageByEntityEvent event) {}
}