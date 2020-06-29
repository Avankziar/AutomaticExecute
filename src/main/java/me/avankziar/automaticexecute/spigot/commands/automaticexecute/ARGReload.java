package main.java.me.avankziar.automaticexecute.spigot.commands.automaticexecute;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.automaticexecute.general.StringValues;
import main.java.me.avankziar.automaticexecute.spigot.AutomaticExecute;
import main.java.me.avankziar.automaticexecute.spigot.assistance.ChatApi;
import main.java.me.avankziar.automaticexecute.spigot.commands.CommandModule;

public class ARGReload extends CommandModule
{
	private AutomaticExecute plugin;
	
	public ARGReload(AutomaticExecute plugin)
	{
		super("spigotreload",StringValues.PERM_CMD_AUTOE_RELOAD,AutomaticExecute.automarguments,1,1,
				"spigotneuladen", StringValues.AUTOE_SUGGEST_RELOAD_S);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String autom = "CmdAutoE.";
		if(plugin.reload())
		{
			///Yaml Datein wurden neugeladen.
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(autom+"Reload.Success")));
			return;
		} else
		{
			///Es wurde ein Fehler gefunden! Siehe Konsole!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(autom+"Reload.Error")));
			return;
		}
	}
}