package mcjagger.mc.mygames.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mcjagger.mc.mygames.command.MyGamesCommand;
import mcjagger.mc.mygames.game.Game;
import mcjagger.mc.mygames.game.JoinResult;

public class JoinCommand extends MyGamesCommand {
	
	public JoinCommand() {
		super("join");
		
		this.setPermission("mygames.command.join");
		this.setUsage("join [game name]");
		this.setDescription("Join game of provided name.");
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
		
		JoinResult joinResult = gm.canAddPlayer(player.getUniqueId());
		
		if (joinResult == JoinResult.SUCCESS)
			gm.addPlayer(player.getUniqueId());
		else
			player.sendMessage(joinResult.prefixedMessage(gm));
		
		return true;
	}

}
