package mcjagger.mc.mygames.classes;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import mcjagger.mc.mygames.MyGames;
import mcjagger.mc.mygames.game.Game;
import mcjagger.mc.mygames.game.GameModule;
import mcjagger.mc.mygames.inventorymenu.InventoryMenu;
import mcjagger.mc.mygames.inventorymenu.MenuItem;
import mcjagger.mc.mygames.inventorymenu.MenuItemListener;

public class PlayerClassModule extends GameModule implements MenuItemListener {

	private SelectablePlayerClass[] classes;
	
	public InventoryMenu menu = null;
	
	public PlayerClassModule(Game game, SelectablePlayerClass[] classes) {
		super(game);
		
		if (classes.length == 0)
			throw new IllegalArgumentException("Must have at least one argument!");
		
		this.classes = classes;
		
		ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
		
		for (SelectablePlayerClass playerClass : classes) {
			MenuItem mi = new MenuItem(playerClass.menuIcon(), this, true);
			mi.setListenerData(playerClass.getName());
			menuItems.add(mi);
		}
		
		menu = new InventoryMenu("Select a Class!", menuItems);
	}
	
	public void openMenu(Player player) {
		if (player == null)
			return;
		
		MyGames.debug("Opened Menu For " + player.getDisplayName());
		InventoryMenu.openMenu(player, menu);
	}

	@Override
	public void onClick(Player player, InventoryMenu menu, MenuItem item) {
		
		String data = item.getListenerData();
		
		for (SelectablePlayerClass playerClass : classes) {
			if (playerClass.getName().equals(data)) {
				PlayerClass.setClass(player, playerClass);
				return;
			}
		}
		
		MyGames.debug("PlayerClass from menu not found: " + data);
	}

	@Override
	public void addedPlayer(Player player) {
		PlayerClass.setClass(player, classes[0]);
		openMenu(player);
	}

}
