package mcjagger.mc.mygames.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mcjagger.mc.mygames.Game;
import mcjagger.mc.mygames.command.MyGamesCommand;

public class JoinCommand extends MyGamesCommand {
	
	public JoinCommand() {
		super("join");
		
		this.setPermission("mygames.command.join");
		this.setUsage("join [game name]");
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
		
		if (gm.canAddPlayer(player.getUniqueId()))
			gm.addPlayer(player.getUniqueId());
		
		return true;
	}

}
