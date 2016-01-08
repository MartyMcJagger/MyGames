package mcjagger.mc.mygames.world.location;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;

public class TeamSpawnLocation extends MapLocation {

	private final DyeColor color;
		
	public TeamSpawnLocation(DyeColor color){
		this.color = color;
	}
	
	@Override
	public String configKey() {
		return TeamSpawnLocation.configKey(color);
	}
	
	public static String configKey(DyeColor color) {
		return "teamspawn."+ color.name();
	}
	
	@Override
	public boolean canHaveMultiple() {
		return true;
	}

	@Override
	public ItemStack icon() {
		Wool w = new Wool();
		w.setColor(color);
		return w.toItemStack();
	}

	@Override
	public boolean canSetAt(Location location) {
		try {
			boolean a = location.getBlock().getType() == Material.AIR;
			boolean b = location.add(0, 1, 0).getBlock().getType() == Material.AIR;
			return a && b;
		} catch (Exception e) {
			// Block clicked was too high in world?
			return false;
		}
	}

}
