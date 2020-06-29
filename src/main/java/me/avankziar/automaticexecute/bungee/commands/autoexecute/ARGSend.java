package main.java.me.avankziar.automaticexecute.bungee.commands.autoexecute;

import main.java.me.avankziar.automaticexecute.bungee.AutomaticExecute;
import main.java.me.avankziar.automaticexecute.bungee.assistance.BackgroundTask;
import main.java.me.avankziar.automaticexecute.bungee.assistance.ChatApi;
import main.java.me.avankziar.automaticexecute.bungee.commands.CommandModule;
import main.java.me.avankziar.automaticexecute.bungee.object.AutoMessage;
import main.java.me.avankziar.automaticexecute.general.StringValues;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
		ProxiedPlayer player = (ProxiedPlayer) sender;
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
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(path+"Send.PathNotExist")));
			return;
		}
		player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(path+"Send.Sending")
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
