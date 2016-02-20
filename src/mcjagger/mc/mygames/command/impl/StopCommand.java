package mcjagger.mc.mygames.command.impl;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import mcjagger.mc.mygames.MyGames;
import mcjagger.mc.mygames.command.MyGamesCommand;
import mcjagger.mc.mygames.game.Game;

public class StopCommand extends MyGamesCommand {
	
	public StopCommand() {
		super("start");
		
		this.setPermission("mygames.command.stop");
		this.setUsage("stop [game name]");
		this.setDescription("Force a game to stop.");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel,
			String[] args) {
		
		Game gm = getGame(sender, args, 0);
		if (gm == null)
			return true;
		
		if (!gm.isRunning()) {
			sender.sendMessage(ChatColor.RED + gm.getName() + " is not running!");
		} else {
			gm.stop(true, true);
		}
		
		return true;
	}

}