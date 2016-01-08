package mcjagger.mc.mygames.world.location;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public abstract class MapLocation {
	
	public abstract String configKey();
	public abstract ItemStack icon();
	
	public boolean canHaveMultiple() { return false; }
/*
	private Location location = null;
	
	public final Location getLocation() {
		return location;
	}
	public final void setLocation(Location location) {
		if (canSetAt(location))
			this.location = location;
	}
	
	public boolean isSet() {
		return getLocation() == null;
	}*/
	
	public abstract boolean canSetAt(Location location);

}
