package mcjagger.mc.mygames;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public abstract class TravelingVenue extends Venue {

	public TravelingVenue(VenueManager vm) {
		super(vm);
	}
	
	public final void updatePosition() {
		
		Set<UUID> toRemove = new HashSet<UUID>();
		Set<UUID> toAdd = new HashSet<UUID>();
		
		Set<UUID> inRange = new HashSet<UUID>();
		
		Location loc = getLocation();
		double prox = activationProximity();
		Collection<Entity> nearby = loc.getWorld().getNearbyEntities(loc, prox, prox, prox);
		
		for (Entity entity : nearby) {
			if (!Player.class.isInstance(entity))
				continue;
			
			inRange.add(entity.getUniqueId());
		}
		
		toRemove.addAll(getPlayers());
		toRemove.removeAll(inRange);
		
		toAdd.addAll(inRange);
		toAdd.removeAll(getPlayers());
		
		for (UUID uuid : toRemove)
			removePlayer(uuid);
		
		for (UUID uuid : toAdd)
			addPlayer(uuid);
	}

}
