package mcjagger.mc.mygames.inventorymenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import mcjagger.mc.mygames.MyGames;

public class InventoryMenu {

	private static HashMap<UUID, InventoryMenu> currentInventories = new HashMap<UUID, InventoryMenu>();
	
	// 012345678
	private static final Integer[][] noodlePositions = new Integer[][]{
		
		new Integer[]{ }, 							// 0 items
		new Integer[]{ 4 },							// 1 item
		new Integer[]{ 2, 6 },						// 2 items
		new Integer[]{ 2, 4, 6 },					// 3 items
		new Integer[]{ 1, 3, 5, 7 },				// 4 items
		new Integer[]{ 1, 2, 4, 6, 7 },				// 5 items
		new Integer[]{ 1, 2, 3, 5, 6, 7 },			// 6 items
		new Integer[]{ 1, 2, 3, 4, 5, 6, 7 },		// 7 items
		new Integer[]{ 0, 1, 2, 3, 5, 6, 7, 8}, 	// 8 items
		new Integer[]{ 0, 1, 2, 3, 4, 5, 6, 7, 8,}	// 9 items
		
	};
	
	private String name;
	private ArrayList<MenuItem> items = new ArrayList<MenuItem>();
	
	private HashMap<Integer, MenuItem> layout = null;
	private Inventory inventory = null;
	
	public InventoryMenu(String name, ArrayList<MenuItem> items) {
		this.name = name;
		this.items = items;
	}
	
	private Inventory constructInventory(){
		
		if (inventory != null)
			return inventory;

		layout = new HashMap<Integer, MenuItem>();
		
		int itemRows = (int)(Math.ceil(items.size() / 7.0));
		
		boolean needsScroll = itemRows > 4;
		boolean useFull = needsScroll && (items.size() <= 54);	
		
		if (!needsScroll) {
			inventory = Bukkit.createInventory(null, itemRows * 9, name);
			
			final int rowLength = (useFull)?9:7;
			MyGames.debug("rowLength:" + rowLength);
			MyGames.debug("itemRows:" + itemRows);
			MyGames.debug("needsScroll:" + needsScroll);
			
			for (int i = 0; i < items.size(); i++) {
				
				/*
				 * 
				 *   o0123456o
				 *   o7890123o
				 *   o4567890o
				 * 
				 * 
				 */
				
				int rowIndex = i/rowLength;
				int posInRow = i%rowLength;
				
				int sideBuffer = (9 - rowLength)/2;
				
				boolean noodlyRow = rowIndex + 1 == itemRows;
				if (noodlyRow) {
					List<MenuItem> subList = items.subList(rowIndex*rowLength, items.size());
					MyGames.debug(rowIndex + " is noodle: " + subList);
					Integer[] positions = noodlePositions[subList.size()];
					
					MyGames.debug(rowIndex + " is noodle: " + subList);
					
					for (i = 0; i < subList.size(); i++) {
						int slot = (rowIndex * 9) + positions[i];
						inventory.setItem(slot, subList.get(i).getIcon());
						layout.put(slot, subList.get(i));
						MyGames.debug("adding to slot " + slot + ", row " + rowIndex + ", pos " + posInRow + ", buffer " + sideBuffer);
						
					}
					
					break;
				}
				
				int slot = (rowIndex * 9) + (posInRow) + (sideBuffer);
				MyGames.debug("adding to slot " + slot + ", row " + rowIndex + ", pos " + posInRow + ", buffer " + sideBuffer);
				
				/*
				try {
					MyGames.debug("put " + items.get(i).getIcon().getItemMeta().getDisplayName() + " in slot " + (rowIndex * 9) +"+"+ (posInRow) + "+" +  (sideBuffer)  +": " + slot);
				} catch (Exception e){
					MyGames.debug("put " + items.get(i).getIcon().getType().name() + " in slot " + slot);
					
				}*/
				try {
				inventory.setItem(slot, items.get(i).getIcon());
				layout.put(slot, items.get(i));
				} catch (Exception e) {}
			}
		}
		
		return inventory;
	}
	
	public static void openMenu(Player player, InventoryMenu menu) {
		Inventory inventory = menu.constructInventory();
		
		player.openInventory(inventory);
		currentInventories.put(player.getUniqueId(), menu);
	}
	
	public static boolean inventoryClicked(UUID uniqueId, int slot) {
		if (!currentInventories.containsKey(uniqueId))
			return false;
		
		InventoryMenu menu = currentInventories.get(uniqueId);
		MenuItem item = menu.layout.get(slot);
		
		if (item == null)
			return true;
		
		inventoryClosed(uniqueId);
		
		item.getListener().onClick(Bukkit.getPlayer(uniqueId), menu, item);
		
		if (item.closeAfterClick())
			Bukkit.getPlayer(uniqueId).closeInventory();
		
		return true;
	}

	public static void inventoryClosed(UUID uniqueId) {
		//Player player = Bukkit.getPlayer(uniqueId);
		//if (player != null)
		//	player.closeInventory();
		
		currentInventories.remove(uniqueId);
	}
	
	

}
