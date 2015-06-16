package org.gestern.gringotts.commands;

import static org.gestern.gringotts.Language.LANG;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class ReloadCommand implements CommandExecutor {
	private final Plugin plugin;

	public ReloadCommand(Plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {

		if (args.length >= 1 && "reload".equalsIgnoreCase(args[0])) {
			plugin.reloadConfig();
			sender.sendMessage(LANG.reload);
			return true;
		}

		return false;
	}

}
