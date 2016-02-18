package mcjagger.mc.mygames.world;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import mcjagger.mc.mygames.inventorymenu.InventoryMenu;
import mcjagger.mc.mygames.inventorymenu.MenuItem;
import mcjagger.mc.mygames.world.location.MapLocation;

public abstract class MapConfigManager {

	public static final String MAP_LIST_PATH = "map.game.list";

	public abstract void addToList(String string);
	public abstract void removeFromList(String string);
	public abstract List<String> getList();

	public abstract World setWorldOptions(World world);
	public abstract boolean saveMapToReferences(String map);
	
	public abstract void loadConfigs();
	public abstract void saveConfigs();

	/*
	 * Methods for configuration reloading, getting, and saving.
	 */

	public abstract void reloadConfig(String key);
	public abstract void saveConfig(String key);
	public abstract FileConfiguration getConfig(String key);
	
	public abstract boolean hasLocation(String mapKey, String locationKey);
	public abstract List<Double> getLocation(String mapKey, String locationKey);
	public abstract void setLocation(String key, String locationKey, Location loc);
	
	/*
	 * Methods for world config items
	 */
	
	//public abstract ItemStack getSpawnTool(DyeColor color);
	//public abstract ItemStack getBoundsTool(int i);
	public abstract InventoryMenu getConfigMenu();
	
	public abstract MenuItem getMenuItem(MapLocation ml);
	
	public abstract ItemStack getTool(MapLocation ml);
	public abstract String getKeyFromTool(ItemStack itemStack);
	
	public abstract ItemStack getConfigMenuItem();
	public abstract boolean isConfigMenuItem(ItemStack is);
	
	public abstract boolean isMapLocation(Location loc);
	public abstract void markMapLocation(MapLocation mapLocation, Location location);
	public abstract void removeLocation(String key, String locationKey, Location loc);
	public abstract boolean canHaveMultiple(ItemStack itemStack);
	public abstract void addLocation(String key, String locationKey, Location loc);
	public abstract void markMapLocation(String locationKey, boolean multiple, Location location);
	public abstract List<Location> getLocations(World world, String locationKey);
}
