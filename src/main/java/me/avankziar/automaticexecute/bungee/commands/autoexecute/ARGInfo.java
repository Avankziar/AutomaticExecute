package main.java.me.avankziar.automaticexecute.bungee.commands.autoexecute;

import java.time.LocalTime;
import java.util.List;

import main.java.me.avankziar.automaticexecute.bungee.AutomaticExecute;
import main.java.me.avankziar.automaticexecute.bungee.assistance.BackgroundTask;
import main.java.me.avankziar.automaticexecute.bungee.assistance.ChatApi;
import main.java.me.avankziar.automaticexecute.bungee.assistance.Utility;
import main.java.me.avankziar.automaticexecute.bungee.commands.CommandModule;
import main.java.me.avankziar.automaticexecute.bungee.object.AutoMessage;
import main.java.me.avankziar.automaticexecute.general.StringValues;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGInfo extends CommandModule
{
	private AutomaticExecute plugin;
	
	public ARGInfo(AutomaticExecute plugin)
	{
		super("info",StringValues.PERM_CMD_AUTOE_INFO,AutomaticExecute.automarguments,2,2,
				"info", StringValues.AUTOE_SUGGEST_INFO);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String autom = "CmdAutoE.";
		AutoMessage aum = null;
		for(AutoMessage am : BackgroundTask.AutoMessageList)
		{
			if(am.getPathName().equals(args[1]))
			{
				aum = am;
				break;
			}
		}
		if(aum == null)
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(autom+"Info.NotExist")));
			return;
		}
		player.sendMessage(ChatApi.tctl("&e=====&7[&bAutoMessage &f%auto%&7]&e=====".replace("%auto%", aum.getPathName())));
		player.sendMessage(ChatApi.tctl("&eRythmus: &r"+aum.getRythmus().toString()));
		player.sendMessage(ChatApi.tctl("&ePermisson: &r"+aum.getPermission()));
		player.sendMessage(ChatApi.tctl("&eMessage: "));
		for(TextComponent tc : aum.getMessage())
		{
			player.sendMessage(tc);
		}
		player.sendMessage(ChatApi.tctl("&eConsoleCommand: "));
		for(String cmd : aum.getConsoleCommand())
		{
			player.sendMessage(ChatApi.tctl("- "+cmd));
		}
		player.sendMessage(ChatApi.tctl("&eDoPlayerCommandWithPermission: &r"+aum.isDoPlayerCommandWithPermission()));
		player.sendMessage(ChatApi.tctl("&ePlayerCommand: "));
		for(String cmd : aum.getPlayerCommand())
		{
			player.sendMessage(ChatApi.tctl("- "+cmd));
		}
		player.sendMessage(ChatApi.tctl("&eRandom: &r"+aum.isRandom()));
		if(aum.isRandom())
		{
			player.sendMessage(ChatApi.tctl("&eRandomPathList: "));
			for(List<TextComponent> listtc : aum.getRandomlist())
			{
				if(listtc != null)
				{
					player.sendMessage(ChatApi.tctl("- &eNew List:"));
					for(TextComponent tc : listtc)
					{
						player.sendMessage(tc);
					}
				}				
			}
		}
		player.sendMessage(ChatApi.tctl("&eTitle: &r"+(aum.getTitle()!=null)));
		player.sendMessage(ChatApi.tctl("&eTitleWithPermission: &r"+(aum.getTitleWithPermission()!=null)));
		if(aum.getDate() != null)
		{
			player.sendMessage(ChatApi.tctl("&eDate: &r"+Utility.serialisedDate(aum.getDate())));
		}
		if(aum.getTime() != null)
		{
			player.sendMessage(ChatApi.tctl("&eTime: &r"+Utility.serialiseTime(aum.getTime())));
		}
		player.sendMessage(ChatApi.tctl("&eTimeList: "));
		for(LocalTime lt : aum.getTimeList())
		{
			player.sendMessage(ChatApi.tctl("- "+Utility.serialiseTime(lt)));
		}
		player.sendMessage(ChatApi.tctl("&eInterval: &r"+aum.getInterval()));
		player.sendMessage(ChatApi.tctl("&eLastTimeSend: &r"+aum.getLastTimeSend()));
	}
}
