package mcjagger.mc.mygames.command;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import mcjagger.mc.mygames.Utils;

public class HelpCommand extends MyGamesCommand {
	
	CommandMap cmdMap;
	
	public HelpCommand(CommandMap cmdMap) {
		super("help");
		
		this.cmdMap = cmdMap;
		this.setPermission("mygames.command.help");
		this.setUsage("help (\"all\")");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel,
			String[] args) {
		
		if (args.length >= 1) {
			
			Command cmd = cmdMap.getCommand(args[0]);
			if (cmd != null) {
				
				StringBuilder sb = new StringBuilder();

				sb.append(ChatColor.GRAY + "~~~~~~~~~~\n");
				
				// Name: Green if player has permission, Red otherwise.
				{
					String name = ChatColor.GREEN + "";
					if (!cmd.testPermissionSilent(sender)) {
						name = ChatColor.RED + "";
					}
					
					name += ChatColor.stripColor(cmd.getName());
					sb.append("Name: " + name + "\n");
				}
				
				sb.append(ChatColor.GRAY + "Usage: " + cmd.getUsage() + "\n");
				sb.append(ChatColor.GRAY + "Description: " + cmd.getDescription());				
				
				sender.sendMessage(sb.toString());
				
				return true;
			} else if (!args[0].equals("all")) {
				sender.sendMessage("Command not found \"" + args[0] + "\".");
			}
		}
		
		ArrayList<String> commands = new ArrayList<String>(cmdMap.commandNames());
		String prefix = "Registered Commands: ";
		boolean a = false;
		
		if (!((args.length > 0) && (args[0].equalsIgnoreCase("all")))) {
		
			// We only want to see which commands the user has access to.
			// Finds which commands the user does not have access to and removes them.
			
			prefix = "Commands available to you: ";
			
			for (String string : cmdMap.commandNames()) {
				String perm = cmdMap.getCommand(string).getPermission();
				if ((perm != null)&&(!sender.hasPermission(perm))) {
					commands.remove(string);
					a = true;
				}
			}
		} 
		
		Collections.sort(commands);
		sender.sendMessage(ChatColor.GRAY + prefix + Utils.list(commands, ChatColor.GRAY, ChatColor.BLUE));
		if (a)
			sender.sendMessage(ChatColor.GRAY + "To see all commands, use the argument \""
					+ ChatColor.BLUE + "all" + ChatColor.GRAY +"\".");
		
		return true;
	}

}
