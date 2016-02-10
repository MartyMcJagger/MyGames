package mcjagger.mc.mygames.classes;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import mcjagger.mc.mygames.Module;
import mcjagger.mc.mygames.game.Game;
import mcjagger.mc.mygames.inventorymenu.InventoryMenu;
import mcjagger.mc.mygames.weapon.Weapon;

public class PlayerClassChooser extends Weapon {

	@Override
	public String getName() {
		return "Class Chooser";
	}

	@Override
	public void registerNecessaryListeners() {
		
	}

	@Override
	public void primary(Game game, Player player, PlayerInteractEvent event) {}

	@Override
	public void secondary(Game game, Player player, PlayerInteractEvent event) {
		for (Module gm : game.getModules()) {
			if (gm instanceof PlayerClassModule) {
				InventoryMenu.openMenu(player, ((PlayerClassModule)gm).menu);
			}
		}
	}

	@Override
	public void melee(Game game, Player player, Player victim, EntityDamageByEntityEvent event) {}

	@Override
	public void interact(Game game, Player player, Entity target, PlayerInteractEntityEvent event) {}

	@Override
	public ItemStack getBaseItem() {
		return new ItemStack(Material.BOOK_AND_QUILL);
	}

}
