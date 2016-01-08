package mcjagger.mc.mygames.command.impl;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import mcjagger.mc.mygames.MyGames;
import mcjagger.mc.mygames.Utils;
import mcjagger.mc.mygames.command.MyGamesCommand;

public class ListCommand extends MyGamesCommand {
	
	public ListCommand() {
		super("list");
		
		this.setPermission("mygames.command.list");
		this.setUsage("list");
		this.setDescription("List all games currently running on the server.");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel,
			String[] args) {
		
		sender.sendMessage(ChatColor.GRAY + "Games Running: " +
				Utils.list(MyGames.getLobbyManager().getGames(), ChatColor.GRAY));
		
		return true;
	}

}