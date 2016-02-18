package mcjagger.mc.mygames.weapon;

import static mcjagger.mc.mygames.SnowballBullet.SNOWBALL_BULLET_DMG;
import static mcjagger.mc.mygames.SnowballBullet.SNOWBALL_BULLET_ID;
import static mcjagger.mc.mygames.SnowballBullet.SNOWBALL_BULLET_OWNER;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import mcjagger.mc.mygames.GunListener;
import mcjagger.mc.mygames.MyGames;
import mcjagger.mc.mygames.SnowballBullet;
import mcjagger.mc.mygames.effects.ParticleEffects;
import mcjagger.mc.mygames.game.Game;

public abstract class Gun extends GameWeapon implements Listener {
	
	Runnable collisionTask = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	/**
	 * How many rounds this gun holds before a reload is required.
	 * Note: this is not the number of bursts, rather the number of rounds. Each burst can use multiple rounds.
	 * @return int number of rounds in clip.
	 */
	public abstract int clipSize();
	
	/**
	 * How many rounds this gun has remaining before any reloads. This does not include the rounds already in the clip.
	 */
	public abstract int roundsRemaining();
	
	/**
	 * How much spread to apply if the player is running.
	 * @return float 0-1f. 0 applies no spread, 1f makes spread up to 60 degrees.
	 */
	public abstract float mobility();
	
	/**
	 * Max speed for a player holding this weapon.
	 * @return int speed
	 */
	public double runSpeed() {
		return .7;
	}
	
	/**
	 * The max angle in degrees allowed for the spread calculation. Max value is 60 degrees.
	 * @return
	 */
	public abstract double spreadAngle();
	
	/**
	 * The number of rounds to fire in each burst.
	 * @return int number of rounds in each burst
	 */
	public abstract int burstCount();
	
	/**
	 * How long to wait before firing the next burst.
	 * @return int number of ticks between bursts
	 */
	public abstract int sprayTickDelay();
	
	/**
	 * Speed of bullets in blocks per second. Any non-positive value will create a laser-like instantaneous bullet effect.
	 * @return double bullet speed
	 */
	public double bulletSpeed() {
		return 8;
	}
	
	/**
	 * Amount of health to take away when hit with a bullet.
	 * @return
	 */
	public double bulletDamage() {
		return 1.75;
	}
	
	/**
	 * Whether bullets from this gun can penetrate players and hit more behind them 
	 */
	public boolean bulletPenetrate() {
		return false;
	}
	
	/**
	 * After this point, bullet effects do not play (but damage can still be done)
	 */
	public double falloffDistance() {
		return 16;
	}
	
	String IN_CLIP = "In Clip";
	String MAX_ROUNDS = "Max rounds";
	String REMAINING_ROUNDS = "Remaining";
	String FIRING_COOLDOWN = "FiringCooldown";
	
	public GunClipData getClipData(ItemStack is) {
		GunClipData clip = new GunClipData();
		List<String> lore = is.getItemMeta().getLore();
		
		for (String str : lore) {
			if (str.startsWith(IN_CLIP+":")) {
				clip.roundsInClip = Integer.parseInt(str.substring(str.indexOf(':')+1));
				continue;
			//} //else if (str.startsWith(MAX_ROUNDS+":")) {
				//clip.maxRounds = Integer.parseInt(str.substring(str.indexOf(':')+1));
				//continue;
			} else if (str.startsWith(REMAINING_ROUNDS+":")) {
				clip.roundsLeft = Integer.parseInt(str.substring(str.indexOf(':')+1));
				continue;
			}
		}
		
		return clip;
	}
	
	@Override
	public void setCustomData(ItemStack is) {
		setClipData(is, defaultClipData());
	}
	
	public final void setClipData(ItemStack is, GunClipData clip) {
		ItemMeta im = is.getItemMeta();
		List<String> lore = im.getLore();
		
		for (int i = 0; i < lore.size(); i++) {
			String str = lore.get(i);
			if (str.startsWith(IN_CLIP)) {
				lore.remove(i);
				i--;
			}
			else if (str.startsWith(REMAINING_ROUNDS)) {
				lore.remove(i);
				i--;
			}
		}
		
		lore.add(IN_CLIP + ':' + clip.roundsInClip);
		lore.add(REMAINING_ROUNDS + ':' + clip.roundsLeft);
		
		im.setLore(lore);
		is.setItemMeta(im);
	}
	
	public GunClipData defaultClipData() {
		GunClipData clip = new GunClipData();
		
		clip.fireCooldown = 0;//this.sprayTickDelay();
		clip.roundsInClip = this.clipSize();
		clip.roundsLeft = this.roundsRemaining();
		
		return clip;
	}
	
	@SuppressWarnings("deprecation")
	public void shootBurst(Player player, ItemStack gun) {
		
		// In case you want to slightly randomize traits
		int burstCount = burstCount();
		//double bulletSpeed = bulletSpeed();
		double spreadAngle = spreadAngle();
		double falloff = falloffDistance();
		double damage = bulletDamage();
		
		GunClipData clip = getClipData(gun);
		
		//MyGames mygames = MyGames.getInstance();
		
		if (!clip.hasRounds()) {
			player.playEffect(player.getLocation(), Effect.CLICK1, 0);
			return;
		}
		
		for (int i = 0; i < burstCount && clip.hasRounds(); i++) {
			Location playerLocation = player.getEyeLocation();//player.getLocation();
			//Vector velocity = player.getLocation().getDirection().multiply(bulletSpeed / 20.0);
			playerLocation = spread(playerLocation, spreadAngle);
			
			//FireworkBullet bullet = new FireworkBullet(playerLocation, velocity, player.getUniqueId(), this.bulletDamage(), this.bulletPenetrate());
			SnowballBullet bullet = new SnowballBullet(player.getUniqueId(), playerLocation, damage);
			bullet.launch();
			
			ParticleEffects.measuredLine(Effect.SMALL_SMOKE, 0, playerLocation, falloff);
			
			//firework.setMetadata("MyGamesGun", new FixedMetadataValue(mygames, getName()));
			//firework.setMetadata("MyGamesGunOwner", new FixedMetadataValue(mygames, player.getUniqueId()));
			
			clip.roundsInClip--;
		}
		
		setClipData(gun, clip);
	}
	
	public Location spread(Location loc, double spreadAngle) {
		Random r = new Random();
		
		double pitch = loc.getPitch() + (r.nextGaussian() * (spreadAngle() / 3));
		double yaw = loc.getYaw() + (r.nextGaussian() * (spreadAngle() / 3));
		
		loc.setPitch((float) pitch);
		loc.setYaw((float) yaw);
		
		return loc;
	}
	
	
	GunListener gunListener;
	
	@Override
	public void registerNecessaryListeners() {
		Bukkit.getPluginManager().registerEvents(this, MyGames.getArcade());
	}
	
	@EventHandler
	public void onSnowballDamage(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Snowball && event.getEntity() instanceof Player) {
			Snowball snowball = (Snowball) event.getDamager();
			
			if (snowball.hasMetadata(SNOWBALL_BULLET_ID)) {
				
				UUID shooterUUID = UUID.fromString(snowball.getMetadata(SNOWBALL_BULLET_OWNER).get(0).asString());
				double damage = snowball.getMetadata(SNOWBALL_BULLET_DMG).get(0).asDouble();
				
				Player shooter = Bukkit.getPlayer(shooterUUID);
				Player victim = (Player) event.getEntity();
				
				if (victim.getUniqueId() != shooterUUID) {
					victim.damage(damage, shooter); 
				} else {
					event.setCancelled(true);
					event.setDamage(0);
				}
				
			} else {
				return;
			}
		}
	}


	@Override
	public void primary(Game game, Player player, PlayerInteractEvent event) {
		ItemStack is = player.getItemInHand();
		MyGames.debug("Pew");
		shootBurst(player, is);
	}

	@Override
	public void secondary(Game game, Player player, PlayerInteractEvent event) {
		ItemStack is = player.getItemInHand();
		MyGames.debug("Reloading");
		GunClipData clip = getClipData(is);
		setClipData(is, clip.reload(this.clipSize()));
	}

	@Override
	public void melee(Game game, Player player, Player victim, EntityDamageByEntityEvent event) {
		
	}

	@Override
	public void interact(Game game, Player player, Entity target, PlayerInteractEntityEvent event) {
		
	}

} class GunClipData {
	public int roundsInClip;
	public int roundsLeft;
	
	public int fireCooldown;
	
	public boolean hasRounds() {
		return roundsInClip > 0;
	}
	
	public GunClipData reload(int clipSize) {
		while (roundsInClip < clipSize && roundsLeft > 0) {
			roundsInClip++;
			roundsLeft--;
		}
		return this;
	}
}
