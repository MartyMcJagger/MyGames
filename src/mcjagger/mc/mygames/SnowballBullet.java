package mcjagger.mc.mygames;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Snowball;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;

public class SnowballBullet implements Listener {

	public static final String SNOWBALL_BULLET_ID = "mygamesSnowballBulletId";
	public static final String SNOWBALL_BULLET_DMG = "mygamesSnowballBulletDmg";
	public static final String SNOWBALL_BULLET_OWNER = "mygamesSnowballBulletOwner";
	
	private static int nextId = 0;
	private final int id = getId();
	
	private synchronized int getId() {
		 return nextId++;
	}
	
	private Location loc;
	
	private UUID shooter;
	private double damage;
	
	
	public SnowballBullet(UUID shooter, Location loc, double damage) {
		this.shooter = shooter;
		this.loc = loc;
		this.damage = damage;
	}
	
	public void launch() {
		Snowball snowball = loc.getWorld().spawn(loc.add(loc.getDirection().normalize().multiply(2)), Snowball.class);
		
		snowball.setMetadata(SNOWBALL_BULLET_ID, new FixedMetadataValue(MyGames.getArcade(), id));
		snowball.setMetadata(SNOWBALL_BULLET_DMG, new FixedMetadataValue(MyGames.getArcade(), damage));
		snowball.setMetadata(SNOWBALL_BULLET_OWNER, new FixedMetadataValue(MyGames.getArcade(), (shooter).toString()));
		
		snowball.setBounce(false);
		snowball.setVelocity(loc.getDirection().normalize().multiply(20));
	}

}
