package mcjagger.mc.mygames.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mcjagger.mc.mygames.MyGames;
import mcjagger.mc.mygames.game.Game;

public abstract class MyGamesCommand extends Command {
	
	public MyGamesCommand(String name) {
		super(name);
	}
	
	public Player getPlayerFromSender(CommandSender sender) {
		if (sender instanceof Player)
			return (Player) sender;
		
		sender.sendMessage(ChatColor.RED
				+ "You must be a player to use this command.");
		
		return null;
	}
	
	public Game getGame(CommandSender sender, String[] args, int argIndex) {
		if (argIndex >= args.length) {
			sender.sendMessage(ChatColor.RED + "/mygames " + this.getUsage());
			return null;
		}
		
		return getGame(sender, args[argIndex]);
	}
	
	public Game getGame(CommandSender sender, String gameName) {
		Game gm = MyGames.getArcade().getGame(gameName);
		if (gm == null) {
			sender.sendMessage(MyGames.getChatManager().gameNotFound(gameName));
		}
		
		return gm;
	}

}
