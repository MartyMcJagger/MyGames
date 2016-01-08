package mcjagger.mc.mygames.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mcjagger.mc.mygames.MyGames;
import mcjagger.mc.mygames.command.MyGamesCommand;

public class DebugCommand extends MyGamesCommand {
	
	public DebugCommand() {
		super("debug");
		
		this.setPermission("mygames.command.debug");
		this.setUsage("debug [true|false]");
		this.setDescription("Decides whether you receive debugging messages.");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel,
			String[] args) {
		
		Player player = getPlayerFromSender(sender);
		if (player == null)
			return true;
		
		if (args.length == 0)
			args = new String[]{"true"};
		
		MyGames.setDebugging(player.getUniqueId(), Boolean.parseBoolean(args[0]));
		
		return true;
	}

}