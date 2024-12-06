package main.java.me.avankziar.aex.spigot;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.aex.general.database.YamlHandler;
import main.java.me.avankziar.aex.general.database.YamlManager;
import main.java.me.avankziar.aex.spigot.assistance.BackgroundTask;
import main.java.me.avankziar.aex.spigot.assistance.Utility;
import me.avankziar.ifh.spigot.administration.Administration;

public class AEX extends JavaPlugin
{
	public static AEX plugin;
	public static Logger logger;
	public static String pluginname = "AutomaticExecute";
	private static YamlHandler yamlHandler;
	private static YamlManager yamlManager;
	private static BackgroundTask backgroundTask;
	private static Utility utility;
	
	private static Administration administrationConsumer;
	
	public void onEnable()
	{
		plugin = this;
		logger = getLogger();
		logger.info("  █████╗ ███████╗██╗  ██╗ | API-Version: "+plugin.getDescription().getAPIVersion());
		logger.info(" ██╔══██╗██╔════╝╚██╗██╔╝ | Author: "+plugin.getDescription().getAuthors().toString());
		logger.info(" ███████║█████╗   ╚███╔╝  | Plugin Website: "+plugin.getDescription().getWebsite());
		logger.info(" ██╔══██║██╔══╝   ██╔██╗  | Depend Plugins: "+plugin.getDescription().getDepend().toString());
		logger.info(" ██║  ██║███████╗██╔╝ ██╗ | SoftDepend Plugins: "+plugin.getDescription().getSoftDepend().toString());
		logger.info(" ╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝ | LoadBefore: "+plugin.getDescription().getLoadBefore().toString());
		
		setupIFHAdministration();
		
		yamlHandler = new YamlHandler(YamlManager.Type.SPIGOT, pluginname, logger, plugin.getDataFolder().toPath(),
        		(plugin.getAdministration() == null ? null : plugin.getAdministration().getLanguage()));
        setYamlManager(yamlHandler.getYamlManager());
        
		utility = new Utility(this);
		backgroundTask = new BackgroundTask(this);
		CommandSetup();
		ListenerSetup();
	}
	
	public void onDisable()
	{
		Bukkit.getScheduler().cancelTasks(this);
		HandlerList.unregisterAll(this);		
		logger.info(pluginname + " is disabled!");
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
		AEX.yamlManager = yamlManager;
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
			    	RegisteredServiceProvider<me.avankziar.ifh.spigot.administration.Administration> rsp = 
	                         getServer().getServicesManager().getRegistration(Administration.class);
				    if (rsp == null) 
				    {
				    	i++;
				        return;
				    }
				    administrationConsumer = rsp.getProvider();
				    logger.info(pluginname + " detected InterfaceHub >>> Administration.class is consumed!");
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