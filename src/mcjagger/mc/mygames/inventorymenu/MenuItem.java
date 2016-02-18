package mcjagger.mc.mygames.inventorymenu;

import org.bukkit.inventory.ItemStack;

public class MenuItem {
	
	private ItemStack icon;
	private MenuItemListener listener;
	private String actionCommand = null;
	private boolean closeAfterClick = true;
	
	private String data = "";
	
	@Override
	public String toString() {
		return icon.getType() + ((actionCommand == null) ? "" : (":" + actionCommand));
	}
	
	public MenuItem(ItemStack icon, MenuItemListener listener) {
		this.icon = icon;
		this.listener = listener;
	}
	
	public MenuItem(ItemStack icon, MenuItemListener listener, boolean closeAfterClick) {
		this.icon = icon;
		this.listener = listener;
		this.closeAfterClick = closeAfterClick;
	}

	public MenuItem(ItemStack icon, MenuItemListener listener, String actionCommand) {
		this.icon = icon;
		this.listener = listener;
		this.actionCommand = actionCommand;
	}
	
	public MenuItem(ItemStack icon, MenuItemListener listener, String actionCommand, boolean closeAfterClick) {
		this.icon = icon;
		this.listener = listener;
		this.actionCommand = actionCommand;
		this.closeAfterClick = closeAfterClick;
	}
	
	public ItemStack getIcon(){
		return icon;
	}
	
	public String getActionCommand() {
		return actionCommand;
	}
	
	public void setCloseAfterClick(boolean closeAfterClick) {
		this.closeAfterClick = closeAfterClick;
	}
	public boolean closeAfterClick() {
		return closeAfterClick;
	}
	
	public void setListenerData(String data) {
		this.data = data;
	}
	public String getListenerData() {
		return data;
	}
	
	public MenuItemListener getListener() {
		return listener;
	}
}
