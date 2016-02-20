package mcjagger.mc.mygames.weapon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.FixedMetadataValue;

import mcjagger.mc.mygames.MyGames;
import mcjagger.mc.mygames.game.Game;

public abstract class BlockWeapon implements Listener {

	public abstract Material getType(Game game, Player player);

	public MaterialData applyData(Game game, Player player, BlockState state){return state.getData();}	
	public void placedBlock(Game game, Player player, Block block){}
	
	
	public static final String BLOCK_OWNER = "blockOwner";
	private static HashMap<String, BlockWeapon> registeredWeapons = new HashMap<String, BlockWeapon>();
	
	private Set<Location> locations = new HashSet<Location>();
	public int blockReach = 3;
	
	public void primary(Game game, Player player, BlockBreakEvent event) {
		
		if (event.getBlock() == null || player == null)
			return;
		
		Block broken = event.getBlock();
		
		/*
		if (!placed.isEmpty()) {
			player.sendMessage(MyGames.getChatManager().prefix(game) + ChatColor.RED + " That block is not empty!");
			return;
		}*/
		
		placeBlock(game, player, broken);
		placedBlock(game, player, broken);
	}

	public void secondary(Game game, Player player, BlockPlaceEvent event) {

		if (event.getBlock() == null || player == null)
			return;
		
		Block placed = event.getBlock();
		
		placeBlock(game, player, placed);
		placedBlock(game, player, placed);
	}

	public void melee(Game game, Player player, Player victim, EntityDamageByEntityEvent event) {}
	public void interact(Game game, Player player, Entity target, PlayerInteractEntityEvent event) {}
	public void blockPlaced(Game game, Player player, Block block){}
	
	public final void placeBlock(Game game, Player player, Block block) {
		block.getState().setType(getType(game, player));
		block.getState().setData(applyData(game, player, block.getState()));
		
		block.getState().setMetadata(BLOCK_OWNER, new FixedMetadataValue(MyGames.getArcade(), player.getUniqueId()));
		
		locations.add(block.getLocation());
	}
	
	public final boolean hasLocation(Location location) {
		return locations.contains(location);
	}
	
	public UUID getBlockOwner(Block block) {
		try {
			
			return (UUID) block.getState().getMetadata(BLOCK_OWNER).get(0).value();
			
		} catch (Exception ignored) {
			return null;
		}
	}
	
	@EventHandler
	public final void onBlockPlaceEvent(BlockPlaceEvent event) {
		if (BlockWeapon.isWeapon(event.getItemInHand())) {
		    
		}
	}
	
	

	/**
	 * Get preferred name for weapon. If this name is taken, it will be appended by the first available integer.
	 * @return
	 */
	public abstract String getName();
	public abstract void registerNecessaryListeners();
	
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
	
	public static boolean register(BlockWeapon weapon) {
		
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
	
	public static ItemStack createWeapon(Class<? extends BlockWeapon> w) {
		
		BlockWeapon weapon;
		
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
	
	public static BlockWeapon parseWeapon(ItemStack itemStack) {
		
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
	
	public static GameWeapon[] getRegisteredWeapons() {
		return registeredWeapons.values().toArray(new GameWeapon[registeredWeapons.size()]);
	}
	
}
