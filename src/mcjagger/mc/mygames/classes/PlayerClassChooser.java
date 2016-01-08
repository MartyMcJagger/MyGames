package mcjagger.mc.mygames.classes;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import mcjagger.mc.mygames.Module;
import mcjagger.mc.mygames.Weapon;
import mcjagger.mc.mygames.game.Game;
import mcjagger.mc.mygames.inventorymenu.InventoryMenu;

public class PlayerClassChooser extends Weapon {

	@Override
	public String getName() {
		return "Class Chooser";
	}

	@Override
	public void registerNecessaryListeners() {
		
	}

	@Override
	public boolean primary(Game game, Player player) {
		return false;
	}

	@Override
	public boolean secondary(Game game, Player player) {
		
		for (Module gm : game.getModules()) {
			if (gm instanceof PlayerClassModule) {
				InventoryMenu.openMenu(player, ((PlayerClassModule)gm).menu);
			}
		}
		
		return true;
	}

	@Override
	public int melee(Game game, Player player, Player victim) {
		return 2;
	}

	@Override
	public boolean interact(Game game, Player player, Entity target) {
		return false;
	}

	@Override
	public ItemStack getBaseItem() {
		return new ItemStack(Material.BOOK_AND_QUILL);
	}

}
