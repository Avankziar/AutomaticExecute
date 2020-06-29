package main.java.me.avankziar.automaticexecute.spigot.commands.automaticexecute;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.automaticexecute.spigot.object.AutoMessage;
import main.java.me.avankziar.automaticexecute.general.StringValues;
import main.java.me.avankziar.automaticexecute.spigot.AutomaticExecute;
import main.java.me.avankziar.automaticexecute.spigot.assistance.BackgroundTask;
import main.java.me.avankziar.automaticexecute.spigot.assistance.ChatApi;
import main.java.me.avankziar.automaticexecute.spigot.commands.CommandModule;

public class ARGSend extends CommandModule
{
	private AutomaticExecute plugin;
	
	public ARGSend(AutomaticExecute plugin)
	{
		super(StringValues.ARG_AUTOE_SEND,StringValues.ARG_AUTOE_SEND_ALIAS,
				AutomaticExecute.automarguments,2,2,StringValues.ARG_AUTOE_SEND_ALIAS,
				StringValues.AUTOE_SUGGEST_SEND);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String path = StringValues.PATH_AUTOE;
		String autopath = args[1];
		AutoMessage am = null;
		for(AutoMessage message : BackgroundTask.AutoMessageList)
		{
			if(message.getPathName().equals(autopath))
			{
				am = message;
				break;
			}
		}
		if(am == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(path+"Send.PathNotExist")));
			return;
		}
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(path+"Send.Sending")
				.replace("%path%", autopath)));
		if(am.isRandom())
		{
			plugin.getBackgroundTask().sendToPlayersRandom(am);
		} else
		{
			plugin.getBackgroundTask().sendToPlayers(am);
		}
		return;
	}
}
