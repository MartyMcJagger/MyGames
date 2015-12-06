package mcjagger.mc.mygames.command;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import mcjagger.mc.mygames.MyGames;

public abstract class CommandMap implements CommandExecutor {
	
	private HashMap<String, Command> knownCommands = new HashMap<String, Command>();
	
	public CommandMap() {
		register("help", new HelpCommand(this));
	}
	
	public abstract void defaultCommand(CommandSender sender);
	
	public void register(String label, Command command) {
		if (knownCommands.containsKey(label)) {
			MyGames.getLogger().warning("[MyGames] Command already registered with name: " + label);
			return;
		}
		
		knownCommands.put(label, command);
	}
	
	public Command getCommand(String key) {
		return knownCommands.get(key);
	}
	
	public Set<String> commandNames() {
		return knownCommands.keySet();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		
		if (args.length == 0) {
			/*if (knownCommands.containsKey("help"))
				return this.onCommand(sender, command, label, new String[]{"help"});
			else
				return false;*/
			defaultCommand(sender);
			return true;
		}
		
		label = args[0];
		command = knownCommands.get(label);
		
		if (command == null) {
			sender.sendMessage(MyGames.getChatManager().commandNotRegistered());
			return false;
		}
		else if ((command.getPermission() != null)&&(!sender.hasPermission(command.getPermission()))) {
			sender.sendMessage(command.getPermissionMessage());
			return true;
		}
		
		String[] args2 = new String[args.length - 1];
		for (int i = 0; i < args2.length; i++)
			args2[i] = args[i+1];
		
		return command.execute(sender, label, args2);
	//	return false;
	}
	
}
