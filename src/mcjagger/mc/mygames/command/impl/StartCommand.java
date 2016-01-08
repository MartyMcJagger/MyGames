package mcjagger.mc.mygames.command.impl;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import mcjagger.mc.mygames.command.MyGamesCommand;
import mcjagger.mc.mygames.game.Game;

public class StartCommand extends MyGamesCommand {
	
	public StartCommand() {
		super("start");
		
		this.setPermission("mygames.command.start");
		this.setUsage("start [game name]");
		this.setDescription("Force a game to start.");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel,
			String[] args) {
		
		Game gm = getGame(sender, args, 0);
		if (gm == null)
			return true;
		
		if (gm.isRunning()) {
			sender.sendMessage(ChatColor.RED + gm.getName() + " is already running!");
		} else {
			gm.start();
		}
		
		return true;
	}

}