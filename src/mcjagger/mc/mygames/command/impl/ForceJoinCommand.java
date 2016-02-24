package mcjagger.mc.mygames.command.impl;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mcjagger.mc.mygames.MyGames;
import mcjagger.mc.mygames.command.MyGamesCommand;
import mcjagger.mc.mygames.game.Game;
import mcjagger.mc.mygames.game.JoinResult;
import net.md_5.bungee.api.ChatColor;

public class ForceJoinCommand extends MyGamesCommand {
	
	public ForceJoinCommand() {
		super("forcejoin");
		
		this.setPermission("mygames.command.forcejoin");
		this.setUsage("forcejoin [game name] [player name]");
		this.setDescription("Forces another player to join game of provided name.");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel,
			String[] args) {

		if (args.length < 2)
			return false;
		
		Player player = Bukkit.getPlayer(args[1]);

		if (player == null) {
			sender.sendMessage(ChatColor.RED + "Couldn't find player: " + args[1]);
			return true;
		}
		
		Game gm = getGame(player, args, 0);
		if (gm == null)
			return true;
		
		JoinResult joinResult = gm.canAddPlayer(player.getUniqueId());
		
		if (joinResult == JoinResult.SUCCESS) {
			MyGames.getLobbyManager().addPlayer(player, gm.getName());
			sender.sendMessage(ChatColor.GREEN + "Success");
		} else {
			sender.sendMessage(joinResult.prefixedMessage(gm));
		}
		
		
		
		return true;
	}

}
