package mcjagger.mc.mygames.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import mcjagger.mc.mygames.Utils;
import mcjagger.mc.mygames.weapon.ItemWeapon;

public abstract class SelectablePlayerClass extends PlayerClass {

	public abstract Material menuIconMaterial();
	public abstract String shortDescription();
	
	public abstract ItemStack[] getPlayerWeapons();
	
	@Override
	public final ItemStack[] getWeapons() {
		ItemStack[] weaps = getPlayerWeapons();
		
		ItemStack[] ret = Arrays.copyOf(weaps, weaps.length +1);
		ret[weaps.length] = ItemWeapon.createWeapon(PlayerClassChooser.class);
		
		return weaps;//ret;
	}
	
	public final ItemStack menuIcon() {
		ItemStack is = Utils.namedStack(menuIconMaterial(), getName());
		ItemMeta im = is.getItemMeta();
		
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GREEN+""+ChatColor.BOLD+"Weapons:");
		lore.addAll(Arrays.asList(weaponNames()));
		
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}
	
	public final String[] weaponNames() {
		ItemStack[] weapons = getWeapons();
		String[] names = new String[weapons.length];
		
		for (int i = 0; i < weapons.length; i++) {
			ItemStack is = weapons[i];
			
			if (is == null)
				continue;
			
			ItemMeta im = is.getItemMeta();
			
			if (im != null && im.hasDisplayName())
				names[i] = im.getDisplayName();
			else
				names[i] = is.getType().toString();
		}
		
		return names;
	}
}
