package mcjagger.mc.mygames.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mcjagger.mc.mygames.Game;
import mcjagger.mc.mygames.command.MyGamesCommand;

public class LeaveCommand extends MyGamesCommand {
	
	public LeaveCommand() {
		super("leave");
		
		this.setPermission("mygames.command.leave");
		this.setUsage("leave [game name]");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel,
			String[] args) {
		
		Player player = getPlayerFromSender(sender);
		if (player == null)
			return true;
		
		Game gm = getGame(player, args, 0);
		if (gm == null)
			return true;
		
		gm.removePlayer(player.getUniqueId());
		
		return true;
	}

}