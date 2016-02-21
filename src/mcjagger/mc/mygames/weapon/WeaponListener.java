package mcjagger.mc.mygames.weapon;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class WeaponListener implements Listener {

	@EventHandler
	public final void onPlayerMelee(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player
				&& event.getEntity() instanceof Player) {
			
			if (event.getDamager().getUniqueId() == event.getEntity().getUniqueId())
				return;

			//MyGames.debug(event.getCause().name());
			if (event.getCause() == DamageCause.CUSTOM)
				return;
			
			Player damager = (Player) event.getDamager();
			Player victim = (Player) event.getEntity();

			ItemWeapon weapon = ItemWeapon.parseWeapon(damager.getItemInHand());
			if (weapon != null) {
				weapon.melee(damager, victim, event);
			}
		}
	}
	
	@EventHandler
	public final void onPlayerClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
			
		ItemWeapon weapon = ItemWeapon.parseWeapon(event.getItem());
		Action action = event.getAction();

		if (weapon == null)
			return;
		
		if (action == Action.LEFT_CLICK_AIR
				|| action == Action.LEFT_CLICK_BLOCK) {
			weapon.primary(player, event);
		} else if (action == Action.RIGHT_CLICK_AIR
				|| (action == Action.RIGHT_CLICK_BLOCK && player.isSneaking())) {
			weapon.secondary(player, event);
		}
	}

	@EventHandler
	public final void onPlayerInteract(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		ItemWeapon weapon = ItemWeapon.parseWeapon(player.getItemInHand());
		if (weapon != null) {
			weapon.interact(player, event.getRightClicked(), event);
		}
	}
}
