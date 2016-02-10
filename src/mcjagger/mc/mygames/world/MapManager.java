package mcjagger.mc.mygames.world;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;

import mcjagger.mc.mygames.game.Game;
import mcjagger.mc.mygames.world.location.MapLocation;

public abstract class MapManager {

	public abstract void unloadAll();
	
	public abstract void addToList(String string);
	public abstract void removeFromList(String string);
	
	public abstract boolean setMapName(Game game, String map);
	public abstract String getMapName(Game game);
	public abstract World getWorld(Game game);
	public abstract boolean unloadMap(Game game, boolean removePlayers);
	
	//public abstract String getRandomMap(Game game);
	public abstract boolean isMapCompatible(String map, Game game);

	public static Location listToLoc(List<Double> list, World world) {
		if (list == null)
			return null;
		return new Location(world, list.get(0), list.get(1), list.get(2));
	}
	
	public abstract Location getRandomSpawn(Game game);
	public abstract Location getRandomLocation(Game game, MapLocation... locations);
	public abstract Location getLocation(Game game, String locationKey);

}
