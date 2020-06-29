package main.java.me.avankziar.automaticexecute.bungee.commands.autoexecute;

import main.java.me.avankziar.automaticexecute.bungee.AutomaticExecute;
import main.java.me.avankziar.automaticexecute.bungee.assistance.ChatApi;
import main.java.me.avankziar.automaticexecute.bungee.commands.CommandModule;
import main.java.me.avankziar.automaticexecute.general.StringValues;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGReload extends CommandModule
{
	private AutomaticExecute plugin;
	
	public ARGReload(AutomaticExecute plugin)
	{
		super("bungeereload",StringValues.PERM_CMD_AUTOE_RELOAD,AutomaticExecute.automarguments,1,1,
				"bungeeneuladen", StringValues.AUTOE_SUGGEST_RELOAD_B);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String autom = "CmdAutoE.";
		if(plugin.reload())
		{
			///Yaml Datein wurden neugeladen.
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(autom+"Reload.Success")));
			return;
		} else
		{
			///Es wurde ein Fehler gefunden! Siehe Konsole!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(autom+"Reload.Error")));
			return;
		}
	}
}
