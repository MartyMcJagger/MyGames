package mcjagger.mc.mygames.game;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import mcjagger.mc.mygames.MyGames;
import mcjagger.mc.mygames.weapon.BlockWeapon;
import mcjagger.mc.mygames.weapon.GameWeapon;

public class GameListener implements Listener {
	
	private Game game;
	
	public GameListener(Game game) {
		this.game = game;
	}
	
	@EventHandler
	public void onLogout(PlayerQuitEvent event) {
		Game gm = MyGames.getLobbyManager().getGame(MyGames.getLobbyManager().getCurrentGame(event.getPlayer().getUniqueId()));
		if (gm != null && game.equals(gm))
			gm.removePlayer(event.getPlayer().getUniqueId());
	}
	
	@EventHandler
	public void onHungerChange(FoodLevelChangeEvent event) {
		Player player = Bukkit.getPlayer(event.getEntity().getUniqueId());
		if (game.hasPlayer(player.getUniqueId())) {
			event.setFoodLevel(20);
		}
	}
	
	@EventHandler
	public final void onItemPickupEvent(PlayerPickupItemEvent event) {
		Item item = event.getItem();
		if (item instanceof Arrow) {
			event.setCancelled(true);
			item.remove();
		}
	}

	@SuppressWarnings("deprecation")
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

			if (game.hasPlayer(damager.getUniqueId()) && game.hasPlayer(victim.getUniqueId())) {
				
				if (game.canDamage(damager, victim)) {
					GameWeapon weapon = GameWeapon.parseWeapon(damager.getItemInHand());
					if (weapon != null) {
						weapon.melee(game, damager, victim, event);
					}
				} else {
					event.setCancelled(true);
				}
			}
		} else if (event.getDamager() instanceof TNTPrimed
				&& event.getEntity() instanceof Player) {
			
			TNTPrimed tnt = (TNTPrimed) event.getDamager();
			Player victim = (Player) event.getEntity();
			
			if (tnt.getMetadata("Owner").size() >= 1) {
				String owner = tnt.getMetadata("Owner").get(0).asString();
				Player player = Bukkit.getPlayer(owner);
				
				if (player != null) {
					if (game.hasPlayer(player.getUniqueId())) {
						if (game.canDamage(player, victim)) {
							double damage = event.getDamage();
							victim.setLastDamageCause(new EntityDamageEvent(victim, DamageCause.ENTITY_EXPLOSION, damage));
							victim.damage(damage, player);
						}
						event.setCancelled(true);
					}
				}				
			}
			
		}
	}
	
	@EventHandler
	public final void onPlayerClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (game.hasPlayer(player.getUniqueId())
				&& GameWeapon.isWeapon(event.getItem())) {
			
			GameWeapon weapon = (GameWeapon) GameWeapon.parseWeapon(event.getItem());
			Action action = event.getAction();

			if (action == Action.LEFT_CLICK_AIR
					|| action == Action.LEFT_CLICK_BLOCK) {
				weapon.primary(game, player, event);
			} else if (action == Action.RIGHT_CLICK_AIR
					|| (action == Action.RIGHT_CLICK_BLOCK && player.isSneaking())) {
				weapon.secondary(game, player, event);
			}
		}
	}

	@EventHandler
	public final void onPlayerInteract(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		if (game.hasPlayer(player.getUniqueId())) {
			
			GameWeapon weapon = GameWeapon.parseWeapon(player.getItemInHand());
			if (weapon != null) {
				weapon.interact(game, player, event.getRightClicked(), event);
			}
			
			/*
			if (player.getItemInHand().getData() instanceof WeaponData) {
				WeaponData weapon = (WeaponData) player.getItemInHand().getData();
				event.setCancelled(!weapon.interact(this, player,
						event.getRightClicked()));
			}*/
		}
	}
/*
	@EventHandler
	public final void onPlayerReload(final PlayerDropItemEvent event) {
		
		event.getItemDrop().setPickupDelay(20);
		System.out.println("Reloading A");
		final Player player = event.getPlayer();
		
		if (game.hasPlayer(player)) {//players.contains(player.getUniqueId())) {
			
			final int itemSlot = player.getInventory().getHeldItemSlot();
			
			new BukkitRunnable() {

				@Override
				public void run() {
					event.getItemDrop().getItemStack();
					
					System.out.println("Reloading B");
					//&& player.getItemInHand().getData() instanceof WeaponData) {
					//((WeaponData) player.getItemInHand().getData()).reload(this, player);
					Weapon weapon = Weapon.parseWeapon(event.getItemDrop().getItemStack());
					if (weapon != null) {

						System.out.println("Reloading C");
						weapon.reload(game, player);
						//event.setCancelled(true);
						
						player.getInventory().setItem(itemSlot, event.getItemDrop().getItemStack());
					}
				}	
			}.runTaskLater(mygames, 1);
		}
	}
*/
	@EventHandler
	public final void onPlayerDamage(EntityDamageEvent event) {

		if (!(event.getEntity() instanceof Player))
			return;

		Player player = (Player) event.getEntity();

		if (!game.hasPlayer(player.getUniqueId()))
			return;
		
		if (event.getCause() == DamageCause.VOID) {
			event.setDamage(20d);
			
			if (player.getGameMode() == GameMode.SPECTATOR) {
				MyGames.getLobbyManager().addSpectator(player, game);
				return;
			}
		}
		
		boolean cancel = false;
		
		cancel |= (!game.doFallDamage()) && (event.getCause() == DamageCause.FALL);
		// cancel |= game.state != GameState.RUNNING;
		
		if (cancel) {
			event.setDamage(0.0);
			event.setCancelled(true);
			return;
		}

		/*
		if (player.getHealth() - event.getDamage() < 1) {
			event.setDamage(0);
			event.setCancelled(true);
			
			if (game.isRunning()) {
				game.respawnPlayer(player);
			} else {
				MyGames.setSpectating(player, game);
			}
		}//*/
	}
	
	@EventHandler
	public final void onPlayerDeath(PlayerDeathEvent event) {
		
		if (!game.hasPlayer(event.getEntity()))
			return;
		
		event.getDrops().clear();
		event.setDroppedExp(0);
		event.setKeepLevel(true);
		event.setDeathMessage(null);
		
		event.getEntity().setHealth(20d);

		event.setKeepInventory(true);
		event.getEntity().getInventory().clear();
		
		if (game.isRunning()) {
			game.playerDied(event.getEntity().getUniqueId());
			game.respawnPlayer(event.getEntity());
		} else {
			MyGames.setSpectating(event.getEntity(), game);
		}
	}

	@EventHandler
	public final void onPlayerRespawn(PlayerRespawnEvent event) {
		if (!game.hasPlayer(event.getPlayer()))
			return;
		
		game.playerRespawned(event);
	}
	
	@EventHandler
	public void onInvOpen(InventoryOpenEvent event) {
		Player player = Bukkit.getPlayer(event.getPlayer().getUniqueId());
		if (game.hasPlayer(player) && !game.allowInventory) {

			event.getPlayer().closeInventory();
			event.setCancelled(true);
			if (player != null)
				player.sendMessage(MyGames.getChatManager().actionNotAllowed());
		}
	}

	@EventHandler
	public final void onInvClick(InventoryClickEvent event) {
		Player player = Bukkit.getPlayer(event.getWhoClicked().getUniqueId());
		if (game.hasPlayer(player) && !game.allowInventory) {

			event.getWhoClicked().closeInventory();
			event.setCancelled(true);
			if (player != null)
				player.sendMessage(MyGames.getChatManager().actionNotAllowed());
		}
	}

	@EventHandler
	public final void onBlockPlaceEvent(BlockPlaceEvent event) {
	    System.out.println(event.getItemInHand());
		if (BlockWeapon.isWeapon(event.getItemInHand())) {
			BlockWeapon.parseWeapon(event.getItemInHand()).secondary(game, event.getPlayer(), event);
		} else if (game.hasPlayer(event.getPlayer()) && !game.allowBlockPlace) {

			event.setCancelled(true);
			//event.getPlayer().sendMessage(MyGames.getChatManager().actionNotAllowed());
		}
	}

	/*
	 * @EventHandler public void onBlockDamageEvent(BlockDamageEvent event) { if
	 * (mm.getMode(event.getPlayer()) != mm.SETUP) {
	 * 
	 * event.setCancelled(true); sendErrorMessage(event.getPlayer()); } }
	 */

	@EventHandler
	public final void onBlockBreakEvent(BlockBreakEvent event) {
		if (game.hasPlayer(event.getPlayer()) && !game.allowBlockBreak) {

			event.setCancelled(true);
			event.getPlayer().sendMessage(MyGames.getChatManager().actionNotAllowed());
		}
	}
	
	@EventHandler
	public final void onExplosion(EntityExplodeEvent event){
		if (MyGames.getMapManager().getWorld(game).getName().equals(event.getEntity().getWorld().getName()) && !game.allowBlockBreak)
			event.blockList().clear();
	}

}
