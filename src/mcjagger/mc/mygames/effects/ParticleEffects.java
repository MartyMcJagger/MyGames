package mcjagger.mc.mygames.effects;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class ParticleEffects {
	
	public static void line(Effect effect, int data, Location loc1, Location loc2) {
		Location pos = loc1.clone();
		
		double repeats = (10.0*loc2.distance(loc1));
		Location dPos = loc2.subtract(loc1).multiply(1/repeats);
		
		for (int i = 0; i < repeats; i++) {
			loc1.getWorld().playEffect(pos, effect, data);
			//loc1.getWorld().//.spawnParticle("reddust", posX, posY, posZ, 0.0D /*red*/, 1.0D /*green*/, 0.0D /*blue*/);
			pos = pos.add(dPos);
		}		
	}
	
	public static void measuredLine(Effect effect, int data, Location loc1, double falloff) {
		Location pos = loc1.clone();
		
		double repeats = (10.0 * falloff);
		Vector dPos = loc1.getDirection().normalize().multiply(falloff).multiply(1/repeats);
		
		for (int i = 0; i < repeats; i++) {
			loc1.getWorld().playEffect(pos, effect, data);
			//loc1.getWorld().//.spawnParticle("reddust", posX, posY, posZ, 0.0D /*red*/, 1.0D /*green*/, 0.0D /*blue*/);
			pos = pos.add(dPos);
		}		
	}

}
