package mcjagger.mc.mygames.classes;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import mcjagger.mc.mygames.Game;
import mcjagger.mc.mygames.GameModule;
import mcjagger.mc.mygames.inventorymenu.InventoryMenu;
import mcjagger.mc.mygames.inventorymenu.MenuItem;
import mcjagger.mc.mygames.inventorymenu.MenuItemListener;

public class PlayerClassModule extends GameModule implements MenuItemListener {

	private SelectablePlayerClass[] classes;
	
	public InventoryMenu menu = null;
	
	public PlayerClassModule(Game game, SelectablePlayerClass[] classes) {
		super(game);
		
		this.classes = classes;
		
		ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
		
		for (SelectablePlayerClass playerClass : classes) {
			MenuItem mi = new MenuItem(playerClass.menuIcon(), this, true);
			mi.setListenerData(playerClass.getName());
			menuItems.add(mi);
		}
		
		menu = new InventoryMenu("Select a Class!", menuItems);
	}
	
	private void openMenu(UUID uuid) {
		Player player = Bukkit.getPlayer(uuid);
		if (player == null)
			return;
		
		Bukkit.getLogger().log(Level.FINEST, "Opened Menu For " + player.getDisplayName());
		InventoryMenu.openMenu(player, menu);
	}

	@Override
	public void onClick(Player player, InventoryMenu menu, MenuItem item) {
		
		String data = item.getListenerData();
		
		for (SelectablePlayerClass playerClass : classes) {
			if (playerClass.getName().equals(data)) {
				PlayerClass.setClass(player, playerClass);
				break;
			}
		}
	}

	@Override
	public void addedPlayer(Player player) {
		openMenu(player.getUniqueId());
	}

}
