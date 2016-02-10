package mcjagger.mc.mygames;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scoreboard.Objective;

import com.google.common.collect.Lists;

public class Utils {
	
	public static String list(Collection<? extends Object> elements, ChatColor baseColor, ChatColor itemColor) {
		String ret = "";
		
		ArrayList<Object> list = Lists.newArrayList(elements);
		
		if (list.size() == 2) {
			ret = itemColor + list.get(0).toString() + baseColor + " and "
					+ itemColor + list.get(1).toString();
		} else if (list.size() >= 1) {
			for (int i = 0; i < list.size(); i++) {
				ret +=baseColor + ((i==0)?"":", ")
						+ (((i==list.size()-1) && (i != 0))?"and ":"") 
						+ itemColor + list.get(i).toString();
			}
		}
		
		return ret;
	}
	
	public static String list(Collection<? extends Object> elements, ChatColor baseColor) {
		return list(elements, baseColor, ChatColor.RESET);
	}
	
	public static String list(Collection<? extends Object> elements) {
		return list(elements, ChatColor.RESET, ChatColor.RESET);
	}
	
	public static boolean hasMetadataValue(List<MetadataValue> metas, Object value) {
		for (MetadataValue meta : metas) {
			try {
				Object metaValue = meta.value();
				if (metaValue == value || metaValue.equals(value)) {
					return true;
				}
			} catch (Exception e) {
				continue;
			}
		}
		return false;
	}
	
	public static void fakeDeath(Location loc) {
		Villager villager = (Villager) loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);
		villager.playEffect(EntityEffect.HURT);
		villager.playEffect(EntityEffect.DEATH);
		villager.setHealth(0);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getRandomItem(Collection<T> items) {
		if (items == null || items.isEmpty())
			return null;
		Random r = new Random();
		return (T) items.toArray()[r.nextInt(items.size())];
	}
	
	public static ItemStack namedStack(Material mat, String name, int amount) {
		ItemStack is = namedStack(mat, name);
		is.setAmount(amount);
		return is;
	}
	
	public static ItemStack namedStack(Material mat, String name) {
		ItemStack is = new ItemStack(mat);
		return name(is, name);
	}
	
	public static ItemStack name(ItemStack is, String name) {
		ItemMeta meta = is.getItemMeta();
		
		meta.setDisplayName(name);
		is.setItemMeta(meta);
		
		return is;
	}
	
	/**
	 *  Returns rankings based on the
	 *  objective parameter. The contents
	 *  of the returned list is such that
	 *  the 'n'th item in the list is a
	 *  collection containing the 'n'th highest
	 *  scoring players.
	 *
	 * @param entries entries to consider for scoring
	 * @param objective the criteria by which to rank entries
	 * @return Rankings as described above.
	 */
	public static List<Collection<String>> winnersHighest(Collection<String> entries, Objective objective) {
		Map<Integer, Set<String>> map = new HashMap<Integer, Set<String>>();
		for (String entry : entries) {
			int score = objective.getScore(entry).getScore();
			Set<String> set = map.get(score);
			
			if (set == null)
				set = new HashSet<String>();
			
			set.add(entry);
			map.put(score, set);
		}
		
		List<Entry<Integer, Set<String>>> list = new ArrayList<Entry<Integer, Set<String>>>();
		list.addAll(map.entrySet());
		
		list.sort(new Comparator<Entry<Integer, Set<String>>>(){

			@Override
			public int compare(Entry<Integer, Set<String>> arg0, Entry<Integer, Set<String>> arg1) {
				return arg1.getKey() - arg0.getKey();
			}
			
		});
		
		List<Collection<String>> ret = new ArrayList<Collection<String>>();
		for (Entry<Integer, Set<String>> entry : list) {
			ret.add(entry.getValue());
		}
		
		return ret;
	}
	
	public static List<Collection<String>> winnersLowest(Collection<String> entries, Objective objective) {
		Map<Integer, Set<String>> map = new HashMap<Integer, Set<String>>();
		for (String entry : entries) {
			int score = objective.getScore(entry).getScore();
			Set<String> set = map.get(score);
			
			if (set == null)
				set = new HashSet<String>();
			
			set.add(entry);
			map.put(score, set);
		}
		
		List<Entry<Integer, Set<String>>> list = new ArrayList<Entry<Integer, Set<String>>>();
		list.addAll(map.entrySet());
		
		list.sort(new Comparator<Entry<Integer, Set<String>>>(){

			@Override
			public int compare(Entry<Integer, Set<String>> arg0, Entry<Integer, Set<String>> arg1) {
				return arg0.getKey() - arg1.getKey();
			}
			
		});
		
		List<Collection<String>> ret = new ArrayList<Collection<String>>();
		for (Entry<Integer, Set<String>> entry : list) {
			ret.add(entry.getValue());
		}
		
		return ret;
	}
}
