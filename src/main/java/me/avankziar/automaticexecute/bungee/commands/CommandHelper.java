package main.java.me.avankziar.automaticexecute.bungee.commands;

import main.java.me.avankziar.automaticexecute.bungee.AutomaticExecute;
import main.java.me.avankziar.automaticexecute.bungee.assistance.ChatApi;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class CommandHelper
{
	private AutomaticExecute plugin;
	private String path = ".CmdAutoE.";

	public CommandHelper(AutomaticExecute plugin)
	{
		this.plugin = plugin;
	}

	public void autoe(ProxiedPlayer player)
	{
		player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(path+"Info.Headline")));
		for(String argument : AutomaticExecute.automarguments.keySet())
		{
			if(argument.equals(AutomaticExecute.automarguments.get(argument).argument))
			{
				sendInfo(player, AutomaticExecute.automarguments.get(argument));
			}			
		}
	}
	
	private void sendInfo(ProxiedPlayer player, CommandModule module)
	{
		if(player.hasPermission(module.permission))
		{
			player.sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("CmdRules.Info."+module.argument),
					ClickEvent.Action.SUGGEST_COMMAND, module.commandSuggest));
		}
	}
}
