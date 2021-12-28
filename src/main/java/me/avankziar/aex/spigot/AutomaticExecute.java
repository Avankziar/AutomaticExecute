package main.java.me.avankziar.aex.spigot;

import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import main.java.me.avankziar.aex.general.YamlManager;
import main.java.me.avankziar.aex.spigot.assistance.BackgroundTask;
import main.java.me.avankziar.aex.spigot.assistance.Utility;
import main.java.me.avankziar.aex.spigot.database.YamlHandler;

public class AutomaticExecute extends JavaPlugin
{
	public static AutomaticExecute plugin;
	public static Logger log;
	public static String pluginName = "AutomanticExecute";
	private static YamlHandler yamlHandler;
	private static YamlManager yamlManager;
	private static BackgroundTask backgroundTask;
	private static Utility utility;
	
	public void onEnable()
	{
		plugin = this;
		log = getLogger();
		log.info("  █████╗ ███████╗██╗  ██╗ | API-Version: "+plugin.getDescription().getAPIVersion());
		log.info(" ██╔══██╗██╔════╝╚██╗██╔╝ | Author: "+plugin.getDescription().getAuthors().toString());
		log.info(" ███████║█████╗   ╚███╔╝  | Plugin Website: "+plugin.getDescription().getWebsite());
		log.info(" ██╔══██║██╔══╝   ██╔██╗  | Depend Plugins: "+plugin.getDescription().getDepend().toString());
		log.info(" ██║  ██║███████╗██╔╝ ██╗ | SoftDepend Plugins: "+plugin.getDescription().getSoftDepend().toString());
		log.info(" ╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝ | LoadBefore: "+plugin.getDescription().getLoadBefore().toString());
		try
		{
			yamlHandler = new YamlHandler(this);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		utility = new Utility(this);
		backgroundTask = new BackgroundTask(this);
		CommandSetup();
		ListenerSetup();
	}
	
	public void onDisable()
	{
		Bukkit.getScheduler().cancelTasks(this);
		HandlerList.unregisterAll(this);		
		log.info(pluginName + " is disabled!");
	}
	
	public YamlHandler getYamlHandler() 
	{
		return yamlHandler;
	}
	
	public YamlManager getYamlManager()
	{
		return yamlManager;
	}

	public void setYamlManager(YamlManager yamlManager)
	{
		AutomaticExecute.yamlManager = yamlManager;
	}
	
	public BackgroundTask getBackgroundTask()
	{
		return backgroundTask;
	}
	
	public Utility getUtility()
	{
		return utility;
	}
	
	public void CommandSetup()
	{
		/*new ARGBook(this);
		new ARGItem(this);
		new ARGReload(this);
		new ARGSend(plugin);
		getCommand("autoex").setExecutor(new MultipleCommandExecutor(this));
		getCommand("autoex").setTabCompleter(new TABCompletion());*/
	}
	
	
	public void ListenerSetup()
	{
		//PluginManager pm = getServer().getPluginManager();
	}
}