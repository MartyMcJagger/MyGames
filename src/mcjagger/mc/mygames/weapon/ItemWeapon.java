package mcjagger.mc.mygames.weapon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import mcjagger.mc.mygames.MyGames;

public abstract class ItemWeapon implements Listener {

	private static HashMap<String, ItemWeapon> registeredWeapons = new HashMap<String, ItemWeapon>();
	
	public int blockReach = 3;
	
	
	/**
	 * Get preferred name for weapon. If this name is taken, it will be appended by the first available integer.
	 * @return
	 */
	public abstract String getName();
	public abstract void registerNecessaryListeners();
	
	/**
	 * Triggered by a Primary Attack, default Left Click.
	 * @param game = Running Game
	 * @param player = Who Clicked
	 * @param event = relevant event
	 */
	public abstract void primary(Player player, PlayerInteractEvent event);
	
	/**
	 * Triggered by a Secondary Attack, default Right Click.
	 * @param game = Running Game
	 * @param player = Who Clicked
	 * @param event = relevant event
	 */
	public abstract void secondary(Player player, PlayerInteractEvent event);

	/**
	 * Called when a player uses this weapon in a melee attack on someone else.
	 * 
	 * @param game
	 * @param player
	 * @param victim
	 * @param event = relevant event
	 */
	public abstract void melee(Player player, Player victim, EntityDamageByEntityEvent event);
	
	/**
	 * Triggered by an interaction with an entity.
	 * @param game = Running Game
	 * @param player = Who Clicked
	 * @param event = relevant event
	 */
	public abstract void interact(Player player, Entity target, PlayerInteractEntityEvent event);
	
	public abstract ItemStack getBaseItem();
	
	/**
	 *  Override this if you need to add lores to store data.
	 *
	 */
	public void setCustomData(ItemStack is) {}
	
	public final boolean isRegistered() {
		return isRegistered(getName());
	}
	
	public void generateExplosion(Location loc, float power, Player player) {
		TNTPrimed tnt = (TNTPrimed) loc.getWorld().spawnEntity(loc, EntityType.PRIMED_TNT);
		tnt.setFuseTicks(1);
		tnt.setYield(power);
		tnt.setIsIncendiary(false);
		tnt.setMetadata("Owner", new FixedMetadataValue(MyGames.getArcade(), player.getName()));
	}
	
	public static final boolean isRegistered(String name) {
		return registeredWeapons.containsKey(name); 
	}
	
	public static boolean register(ItemWeapon weapon) {
		
		if (weapon.isRegistered())
			return true;
		
		//try{
			registeredWeapons.put(weapon.getName(), weapon);
			weapon.registerNecessaryListeners();
			return true;
		//} catch (Exception e) {
		//	e.printStackTrace();
		//	Bukkit.getLogger().severe("Error while registering Weapon: " + weapon.getName());
		//	return false;
		//}
	}
	
	public static ItemStack createWeapon(Class<? extends ItemWeapon> w) {
		
		ItemWeapon weapon;
		
		try {
			weapon = w.newInstance();
			
			if (!weapon.isRegistered()) {
				if (!register(weapon))
					return null;
			}
			
			ItemStack is = weapon.getBaseItem();
			
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(ChatColor.RED + weapon.getName());
			
			List<String> lores = new ArrayList<String>();
			lores.add("Weapon");
			lores.add(weapon.getName());
			
			im.setLore(lores);
			is.setItemMeta(im);

			weapon.setCustomData(is);
			
			return is;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	public static ItemWeapon parseWeapon(ItemStack itemStack) {
		
		if (itemStack == null)
			return null;
		
		if (isWeapon(itemStack)) {
			return registeredWeapons.get(getWeaponName(itemStack));
		}
		
		return null;
	}
	
	public static String getWeaponName(ItemStack itemStack) {
		
		try {
			List<String> lore = itemStack.getItemMeta().getLore();
			
			if (lore.get(0).equalsIgnoreCase("Weapon")) {
				return lore.get(1);
			}
		} catch(Exception e) {
			return null;
		}
		
		return null;
	}
	
	public static boolean isWeapon(ItemStack itemStack) {
		
		if (itemStack == null)
			return false;
		
		String name = getWeaponName(itemStack);
		
		return (name != null) && (isRegistered(name));
	}
	
	public static ItemWeapon[] getRegisteredWeapons() {
		return registeredWeapons.values().toArray(new ItemWeapon[registeredWeapons.size()]);
	}
	
}
