package mcjagger.mc.mygames.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mcjagger.mc.mygames.MyGames;
import mcjagger.mc.mygames.command.MyGamesCommand;
import mcjagger.mc.mygames.game.Game;

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
		
		if (gm.canAddPlayer(player.getUniqueId()))
			gm.addPlayer(player.getUniqueId());
		else
			player.sendMessage(MyGames.getChatManager().joinLobbyFull(gm));
		
		return true;
	}

}
