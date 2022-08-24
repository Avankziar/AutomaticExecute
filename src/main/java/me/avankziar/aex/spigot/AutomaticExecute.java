package main.java.me.avankziar.aex.spigot;

import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.aex.general.YamlManager;
import main.java.me.avankziar.aex.spigot.assistance.BackgroundTask;
import main.java.me.avankziar.aex.spigot.assistance.Utility;
import main.java.me.avankziar.aex.spigot.database.YamlHandler;
import main.java.me.avankziar.ifh.spigot.administration.Administration;

public class AutomaticExecute extends JavaPlugin
{
	public static AutomaticExecute plugin;
	public static Logger log;
	public static String pluginName = "AutomanticExecute";
	private static YamlHandler yamlHandler;
	private static YamlManager yamlManager;
	private static BackgroundTask backgroundTask;
	private static Utility utility;
	
	private static Administration administrationConsumer;
	
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
		
		setupIFHAdministration();
		
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
	
	private void setupIFHAdministration()
	{ 
		if(!plugin.getServer().getPluginManager().isPluginEnabled("InterfaceHub")) 
	    {
	    	return;
	    }
		new BukkitRunnable()
        {
        	int i = 0;
			@Override
			public void run()
			{
			    if(i == 20)
			    {
				cancel();
				return;
			    }
			    try
			    {
			    	RegisteredServiceProvider<main.java.me.avankziar.ifh.spigot.administration.Administration> rsp = 
	                         getServer().getServicesManager().getRegistration(Administration.class);
				    if (rsp == null) 
				    {
				    	i++;
				        return;
				    }
				    administrationConsumer = rsp.getProvider();
				    log.info(pluginName + " detected InterfaceHub >>> Administration.class is consumed!");
			    } catch(NoClassDefFoundError e) 
			    {
			    	cancel();
			    }		    
			    cancel();
			}
        }.runTaskTimer(plugin,  0L, 20*2);
	}
	
	public Administration getAdministration()
	{
		return administrationConsumer;
	}
}