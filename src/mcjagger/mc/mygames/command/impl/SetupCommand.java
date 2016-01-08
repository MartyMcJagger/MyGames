package mcjagger.mc.mygames.command.impl;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mcjagger.mc.mygames.MetadataManager;
import mcjagger.mc.mygames.MyGames;
import mcjagger.mc.mygames.command.MyGamesCommand;

public class SetupCommand extends MyGamesCommand {
	
	public SetupCommand() {
		super("setup");
		
		this.setPermission("mygames.command.setup");
		this.setUsage("setup");
		this.setDescription("Changes your playmode to 'setup' and adds game world configuration tool to your inventory.");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel,
			String[] args) {
		
		Player player = getPlayerFromSender(sender);
		if (player == null)
			return true;
		
		MetadataManager mm = MyGames.getMetadataManager();
		if (mm.getMode(player) == MetadataManager.OTHER_GAME) {
			player.sendMessage(ChatColor.RED + "Sorry dude, I'm trying to be nice to other plugins;"
					+ " I can't let you use that command right now."
					+ " Leave the game you are currently attached to in order to play."
					+ "(One way to achieve this could be reconnecting? :/)");
			return true;
		}
		
		MyGames.toSetup(player);
		mm.setInSetup(player);
		
		return true;
	}

}