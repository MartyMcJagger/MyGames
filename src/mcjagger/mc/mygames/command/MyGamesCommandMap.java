package mcjagger.mc.mygames.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import mcjagger.mc.mygames.command.impl.JoinCommand;
import mcjagger.mc.mygames.command.impl.LeaveCommand;
import mcjagger.mc.mygames.command.impl.ListCommand;
import mcjagger.mc.mygames.command.impl.PlayCommand;
import mcjagger.mc.mygames.command.impl.SetSpawnCommand;
import mcjagger.mc.mygames.command.impl.SetupCommand;
import mcjagger.mc.mygames.command.impl.StartCommand;
import mcjagger.mc.mygames.command.impl.StopCommand;

public class MyGamesCommandMap extends CommandMap {

	public MyGamesCommandMap() {
		
		register("join", new JoinCommand());
		register("leave", new LeaveCommand());
		register("start", new StartCommand());
		register("stop", new StopCommand());
		register("play", new PlayCommand());
		register("setup", new SetupCommand());
		register("list", new ListCommand());
		register("setspawn", new SetSpawnCommand());
		
	}

	@Override
	public void defaultCommand(CommandSender sender) {
		sender.sendMessage(ChatColor.GRAY + "New to MyGames? Type \"/mygames help\" for a list of commands!");
	}

}
