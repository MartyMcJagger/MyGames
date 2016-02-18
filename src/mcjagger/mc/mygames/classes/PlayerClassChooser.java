package mcjagger.mc.mygames.classes;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import mcjagger.mc.mygames.Module;
import mcjagger.mc.mygames.MyGames;
import mcjagger.mc.mygames.game.Game;
import mcjagger.mc.mygames.inventorymenu.InventoryMenu;
import mcjagger.mc.mygames.weapon.ItemWeapon;

public class PlayerClassChooser extends ItemWeapon {

	@Override
	public String getName() {
		return "Class Chooser";
	}

	@Override
	public void registerNecessaryListeners() {
		
	}

	@Override
	public void primary(Player player, PlayerInteractEvent event) {}

	@Override
	public void secondary(Player player, PlayerInteractEvent event) {
		MyGames.debug("Wooo!");
		
		Game game = null;
		
		try {
			game = MyGames.getLobbyManager().getGame(
					MyGames.getLobbyManager().getCurrentGame(player));
			
			for (Module gm : game.getModules()) {
				if (gm instanceof PlayerClassModule) {
					InventoryMenu.openMenu(player, ((PlayerClassModule)gm).menu);
				}
			}	
		} catch (Exception e) {
			player.getInventory().remove(event.getItem());
		}
	}

	@Override
	public void melee(Player player, Player victim, EntityDamageByEntityEvent event) {}

	@Override
	public void interact(Player player, Entity target, PlayerInteractEntityEvent event) {}

	@Override
	public ItemStack getBaseItem() {
		return new ItemStack(Material.BOOK);
	}

}
