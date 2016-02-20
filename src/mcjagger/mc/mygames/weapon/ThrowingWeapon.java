
package mcjagger.mc.mygames.weapon;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;

import mcjagger.mc.mygames.MyGames;
import mcjagger.mc.mygames.game.Game;

public abstract class ThrowingWeapon extends GameWeapon implements Listener {
	
	private final Class<? extends ThrowingWeapon> clazz;
	
	public ThrowingWeapon(Class<? extends ThrowingWeapon> clazz) {
		this.clazz = clazz;
	}
	
	@Override
	public void primary(Game game, Player player, PlayerInteractEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void secondary(Game game, Player player, PlayerInteractEvent event) {
		Arrow arrow = (Arrow) player.launchProjectile(Arrow.class, player.getLocation().getDirection().normalize().multiply(5));
		arrow.setMetadata(getName(), new FixedMetadataValue(MyGames.getArcade(), true));
		arrow.setCritical(true);
		arrow.setShooter(player);
		
		ItemStack is = (player.getItemInHand());
		if (is != null && GameWeapon.isWeapon(is)) {
			if (GameWeapon.getWeaponName(is).equalsIgnoreCase(getName())) {
				//Bukkit.broadcastMessage(is.getAmount() + " left");
				if (is.getAmount() < 2) {
					player.setItemInHand(null);
					//player.getInventory().clear(player.getInventory().getHeldItemSlot());
					//player.
				}
				else {
					is.setAmount(is.getAmount()-1);
				}
				
				player.updateInventory();
				
				return;
			}
		}
	}

	@Override
	public void interact(Game game, Player player, Entity target, PlayerInteractEntityEvent event) {
		
	}
	
	@Override
	public void registerNecessaryListeners() {
		Bukkit.getPluginManager().registerEvents(this, MyGames.getArcade());
	}
	
	@EventHandler
	public void onItemHit(ProjectileHitEvent event) {
		if (event.getEntity() instanceof Arrow) {
			Arrow arrow = (Arrow)event.getEntity();
			
			if ((arrow.hasMetadata(getName()))) {
				Location loc = arrow.getLocation();
				ItemStack drop = GameWeapon.createWeapon(clazz);
				drop.setAmount(1);
				
				arrow.getWorld().dropItem(loc, drop);
				arrow.remove();
			}
		}
	}
	
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onItemDamagePlayer(EntityDamageByEntityEvent event) {
		//Bukkit.broadcastMessage(event.getDamager().getType().toString());
		//Bukkit.broadcastMessage(event.getDamage() + "");
		if (event.getDamager() instanceof Arrow) {
			Arrow arrow = (Arrow)event.getDamager();
			
			if ((arrow.hasMetadata(getName()))) {
				//Location loc = arrow.getLocation();
				//ItemStack drop = Weapon.createWeapon(Knife.class);
				//drop.setAmount(1);
				
				//arrow.getWorld().dropItem(loc, drop);
				//arrow.remove();
				
				//ProjectileSource source = arrow.getShooter();
				/*
				if (!(source instanceof Player)) {
				 
					//Bukkit.getLogger().info("source != shooter!");
					return;
				}//*/
				
				
				//event.setDamage(0);
				//event.setCancelled(true);
				/*if (event.getEntity() instanceof LivingEntity) {
					double dmg = ((LivingEntity)event.getEntity()).getMaxHealth();
					Player killer = (Player) source;
					//((Damageable)event.getEntity()).damage(dmg, killer);
				//	((Damageable)event.getEntity()).damage(10.0, player);
					event.getEntity().setLastDamageCause(new EntityDamageEvent(killer, DamageCause.PROJECTILE, dmg));
				}//*/
			}
		}
	}//*/
	
	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent event) {
		
		ItemStack knife = event.getItem().getItemStack();
		if (GameWeapon.isWeapon(knife) && GameWeapon.getWeaponName(knife).equalsIgnoreCase(getName())) {
		
			PlayerInventory pi = event.getPlayer().getInventory();
			ItemStack[] items = pi.getContents();
			
			//if (pi.contains(getBaseItem(), 1)) {
				for (ItemStack is : items) {
					if (is != null) {
						//Bukkit.broadcastMessage(is.toString());
						if (GameWeapon.isWeapon(is) && GameWeapon.getWeaponName(is).equalsIgnoreCase(getName())) {
							is.setAmount(is.getAmount()+1);
							event.getItem().remove();
							event.setCancelled(true);
							return;
						}
					}
				}
			//}
		}
	}
}
