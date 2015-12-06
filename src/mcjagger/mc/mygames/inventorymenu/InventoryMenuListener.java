package mcjagger.mc.mygames.inventorymenu;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

// TODO: Is this even referenced???
public class InventoryMenuListener implements Listener {
	
	public InventoryMenuListener() {
		
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		InventoryMenu.inventoryClosed(event.getPlayer().getUniqueId());
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		event.setCancelled(InventoryMenu.inventoryClicked(event.getWhoClicked().getUniqueId(), event.getSlot()));
	}

}
