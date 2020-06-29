package main.java.me.avankziar.automaticexecute.spigot.commands;

import org.bukkit.entity.Player;

import main.java.me.avankziar.automaticexecute.general.StringValues;
import main.java.me.avankziar.automaticexecute.spigot.AutomaticExecute;
import main.java.me.avankziar.automaticexecute.spigot.assistance.ChatApi;
import net.md_5.bungee.api.chat.ClickEvent;

public class CommandHelper
{
	private AutomaticExecute plugin;
	private String path = StringValues.PATH_AUTOE;

	public CommandHelper(AutomaticExecute plugin)
	{
		this.plugin = plugin;
	}

	public void autom(Player player)
	{
		for(String argument : AutomaticExecute.automarguments.keySet())
		{
			if(argument.equals(AutomaticExecute.automarguments.get(argument).argument))
			{
				sendInfo(player, AutomaticExecute.automarguments.get(argument));
			}			
		}
	}
	
	private void sendInfo(Player player, CommandModule module)
	{
		if(player.hasPermission(module.permission))
		{
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString(path+"Info."+module.argument),
					ClickEvent.Action.SUGGEST_COMMAND, module.commandSuggest));
		}
	}
}
