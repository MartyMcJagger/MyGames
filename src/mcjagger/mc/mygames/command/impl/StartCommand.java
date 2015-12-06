package mcjagger.mc.mygames.command.impl;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import mcjagger.mc.mygames.Game;
import mcjagger.mc.mygames.command.MyGamesCommand;

public class StartCommand extends MyGamesCommand {
	
	public StartCommand() {
		super("start");
		
		this.setPermission("mygames.command.start");
		this.setUsage("start [game name]");
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