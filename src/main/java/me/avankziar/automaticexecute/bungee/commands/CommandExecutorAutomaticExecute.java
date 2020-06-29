package main.java.me.avankziar.automaticexecute.bungee.commands;

import main.java.me.avankziar.automaticexecute.bungee.AutomaticExecute;
import main.java.me.avankziar.automaticexecute.bungee.assistance.ChatApi;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CommandExecutorAutomaticExecute extends Command 
{
	private AutomaticExecute plugin;
	
	public CommandExecutorAutomaticExecute(AutomaticExecute plugin)
	{
		super("autoexb",null,"automaticexecutebungee");
		this.plugin = plugin;
	}

	@Override
	public void execute(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		
		if (!(player instanceof ProxiedPlayer)) 
		{
			AutomaticExecute.log.info("/autoexb is only for Player!");
			return;
		}
		if (args.length == 0) 
		{
			plugin.getCommandHelper().autoe(player); //Info Command
			return;
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
					player.sendMessage(ChatApi.clickEvent(plugin.getYamlHandler().getL().getString("InputIsWrong"),
							ClickEvent.Action.RUN_COMMAND, "/autoexb"));
					return;
				}
			} else 
			{
				///Du hast dafür keine Rechte!
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("NoPermission")));
				return;
			}
		} else 
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.sendMessage(ChatApi.clickEvent(plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, "/autoexb"));
			return;
		}
	}
}