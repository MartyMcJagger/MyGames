package mcjagger.mc.mygames.weapon;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import mcjagger.mc.mygames.MyGames;
import mcjagger.mc.mygames.game.Game;

public abstract class PressurePlateWeapon extends BlockWeapon implements Listener {

	public abstract void triggered(Game game, Player player, Block block);

	public abstract Material pressurePlateMaterial();

	@Override
	public final void registerNecessaryListeners() {
		Bukkit.getPluginManager().registerEvents(this, MyGames.getArcade());
	}

	@Override
	public final Material getType(Game game, Player player) {
		return pressurePlateMaterial();
	}

	@Override
	public ItemStack getBaseItem() {
		return new ItemStack(pressurePlateMaterial());
	}

	@EventHandler
	public void pressurePlateEvent(PlayerInteractEvent event) {
		Block block = event.getClickedBlock();

		if (event.getAction() == (Action.PHYSICAL) && event.getClickedBlock().getType() == pressurePlateMaterial()
				&& hasLocation(event.getClickedBlock().getLocation())) {

			Set<String> games = new HashSet<String>();
			for (Entity entity : block.getWorld().getNearbyEntities(block.getLocation(), 3, 3, 3)) {
				if (entity instanceof Player) {
					Player player = (Player) entity;
					String gameName = (MyGames.getLobbyManager().getCurrentGame(player));
					
					if (games.contains(gameName))
						continue;
					else
						games.add(gameName);
					
					Game game = MyGames.getLobbyManager().getGame(gameName);
					triggered(game, event.getPlayer(), event.getClickedBlock());
				}
			}
		}
	}

}
