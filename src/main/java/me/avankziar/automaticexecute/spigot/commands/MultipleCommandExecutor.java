package main.java.me.avankziar.automaticexecute.spigot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.automaticexecute.spigot.AutomaticExecute;
import main.java.me.avankziar.automaticexecute.spigot.assistance.ChatApi;
import net.md_5.bungee.api.chat.ClickEvent;

public class MultipleCommandExecutor implements CommandExecutor 
{
	private AutomaticExecute plugin;
	
	public MultipleCommandExecutor(AutomaticExecute plugin)
	{
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		// Checks if the label is one of yours.
		if (cmd.getName().equalsIgnoreCase("autoex") || cmd.getName().equalsIgnoreCase("automaticexecutespigot")) 
		{		
			if (!(sender instanceof Player)) 
			{
				AutomaticExecute.log.info("/autoex is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if (args.length == 0) 
			{
				plugin.getCommandHelper().autom(player); //Info Command
				return false;
			}
			if (AutomaticExecute.automarguments.containsKey(args[0])) 
			{
				CommandModule mod = AutomaticExecute.automarguments.get(args[0]);
				//Abfrage ob der Spieler die Permission hat
				if (player.hasPermission(mod.permission)) 
				{
					//Abfrage, ob der Spieler in den min und max Argumenten Bereich ist.
					if(args.length >= mod.minArgs && args.length <= mod.maxArgs)
					{
						mod.run(sender, args);
					} else
					{
						///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
						player.spigot().sendMessage(ChatApi.clickEvent(
								plugin.getYamlHandler().getL().getString("InputIsWrong"),
								ClickEvent.Action.RUN_COMMAND, "/autoex"));
						return false;
					}
				} else 
				{
					///Du hast dafür keine Rechte!
					player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("NoPermission")));
					return false;
				}
			} else 
			{
				///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
				player.spigot().sendMessage(ChatApi.clickEvent(
						plugin.getYamlHandler().getL().getString("InputIsWrong"),
						ClickEvent.Action.RUN_COMMAND, "/autoex"));
				return false;
			}
		}
		return false;
	}
}
