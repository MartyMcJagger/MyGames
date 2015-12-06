package mcjagger.mc.mygames;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import mcjagger.mc.mygames.world.location.MapLocation;

public abstract class Playable implements Listener {
	
	public abstract String	getName();
	public abstract boolean	canAddPlayer(UUID uuid);
	public 			void 	addedPlayer(UUID uuid){}
	public 			void 	removedPlayer(UUID uuid){}
	
	public void pointScored(UUID uuid, Location location){}
	public void playerDied(UUID uuid) {}
	public void playerDamaged(EntityDamageEvent event){}
	public void playerDamagedPlayer(EntityDamageByEntityEvent event) {}
	public void playerRespawned(PlayerRespawnEvent event){}
	
	public boolean canDamage(Player attacker, Player victim){return true;}
	public boolean doFallDamage(){return false;}
	public boolean doDrownDamage(){return false;}
	
	public DyeColor[] getSpawnColors(){return null;}
	public MapLocation[] getLocationTypes(){return new MapLocation[0];}
	
	private Set<UUID> players = new HashSet<UUID>();
	private Set<Module> modules = new HashSet<Module>();
	
	
	
	public Playable() {
		
	}
	
	
	
	

	public Set<Module> getModules() {
		return new HashSet<Module>(modules);
	}
	public boolean addModule(Module module) {
		return modules.add(module);
	}
	public boolean removeModule(Module module) {
		if (module.removable)
			return modules.remove(module);
		
		return false;
	}
	
	
	
	public final Set<UUID> getPlayers() {
		return new HashSet<UUID>(players);
	}
	public final int playerCount() {
		return players.size();
	}
	
	public final void tellPlayers(String message) {
		for (UUID uuid : getPlayers()) {
			Player player = Bukkit.getPlayer(uuid);
			if (player != null)
				player.sendMessage(message);
		}
	}
	
	
	
	/**
	 * 
	 * @param uuid Player to check for
	 * @return true if the player set contains this player
	 */
	public final boolean hasPlayer(UUID uuid) {
		return players.contains(uuid);
	}
	
	/**
	 * 
	 * @param player Player to check for
	 * @return true if the player set contains this player
	 */
	public final boolean hasPlayer(Player player) {
		return hasPlayer(player.getUniqueId());
	}
	/**
	 * 
	 * @param uuid Player to add
	 * @return true if the player set changed as a result of this call
	 */
	public final boolean addPlayer(UUID uuid) {
		if (canAddPlayer(uuid) && players.add(uuid)) {
			addedPlayer(uuid);
			return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param uuid Player to remove
	 * @return true if the player set changed as a result of this call
	 */
	public final boolean removePlayer(UUID uuid) {
		if (players.remove(uuid)) {
			removedPlayer(uuid);
			return true;
		}
		
		return false;
	}

}
