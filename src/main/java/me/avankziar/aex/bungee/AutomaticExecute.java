package main.java.me.avankziar.aex.bungee;

import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.me.avankziar.aex.bungee.assistance.BackgroundTask;
import main.java.me.avankziar.aex.bungee.assistance.ChatApi;
import main.java.me.avankziar.aex.bungee.assistance.Utility;
import main.java.me.avankziar.aex.bungee.database.YamlHandler;
import main.java.me.avankziar.aex.general.YamlManager;
import main.java.me.avankziar.ifh.bungee.InterfaceHub;
import main.java.me.avankziar.ifh.bungee.administration.Administration;
import main.java.me.avankziar.ifh.bungee.plugin.RegisteredServiceProvider;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class AutomaticExecute extends Plugin
{
	public static Logger log;
	public static String pluginName = "AutomaticExecute";
	private static AutomaticExecute plugin;
	private static YamlHandler yamlHandler;
	private static YamlManager yamlManager;
	private static Utility utility;
	private static BackgroundTask backgroundTask;
	
	private static ChatApi chatApi;
	
	private static Administration administrationConsumer;
	
	private ScheduledTask administrationRun;
	
	public void onEnable() 
	{
		plugin = this;
		log = getLogger();
		//https://patorjk.com/software/taag/#p=display&f=ANSI%20Shadow&t=AEX
		log.info("  █████╗ ███████╗██╗  ██╗ | API-Version: "+plugin.getDescription().getVersion());
		log.info(" ██╔══██╗██╔════╝╚██╗██╔╝ | Author: "+plugin.getDescription().getAuthor());
		log.info(" ███████║█████╗   ╚███╔╝  | Plugin Website: Comming soon");
		log.info(" ██╔══██║██╔══╝   ██╔██╗  | Depend Plugins: "+plugin.getDescription().getDepends().toString());
		log.info(" ██║  ██║███████╗██╔╝ ██╗ | SoftDepend Plugins: "+plugin.getDescription().getSoftDepends().toString());
		log.info(" ╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝ | Have Fun^^");
		
		setupIFHAdministration();
		
		yamlHandler = new YamlHandler(this);
		utility = new Utility(this);
		backgroundTask = new BackgroundTask(this);
		
		CommandSetup();
		ListenerSetup();
	}
	
	public void onDisable()
	{
		getProxy().getScheduler().cancel(this);
		
		log.info(pluginName + " is disabled!");
	}
	
	public static AutomaticExecute getPlugin()
	{
		return plugin;
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
	
	public Utility getUtility()
	{
		return utility;
	}
	
	public ChatApi getChatApi()
	{
		return chatApi;
	}
	
	public BackgroundTask getBackgroundTask()
	{
		return backgroundTask;
	}
	
	public boolean reload()
	{
		if(!yamlHandler.loadYamlHandler())
		{
			return false;
		}
		if(!backgroundTask.loadBackgroundTask())
		{
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public void disablePlugin()
	{
		Plugin plugin = (Plugin) ProxyServer.getInstance().getPluginManager().getPlugin(pluginName);
	       
		try
		{
			plugin.onDisable();
			for (Handler handler : plugin.getLogger().getHandlers())
			{
				handler.close();
			}
		}
		catch (Throwable t) 
		{
			getLogger().log(Level.SEVERE, "Exception disabling plugin " + plugin.getDescription().getName(), t);
		}
		ProxyServer.getInstance().getPluginManager().unregisterCommands(plugin);
		ProxyServer.getInstance().getPluginManager().unregisterListeners(plugin);
		ProxyServer.getInstance().getScheduler().cancel(plugin);
		plugin.getExecutorService().shutdownNow();
	}
	
	public void CommandSetup()
	{
		/*PluginManager pm = getProxy().getPluginManager();
		new ARGReload(this);
		new ARGInfo(this);
		new ARGSend(plugin);
		pm.registerCommand(this, new CommandExecutorAutomaticExecute(this));*/
	}
	
	public void ListenerSetup()
	{
		//PluginManager pm = getProxy().getPluginManager();
		//pm.registerListener(this, new EventName(this));
	}
	
	private void setupIFHAdministration()
	{ 
		Plugin plugin = BungeeCord.getInstance().getPluginManager().getPlugin("InterfaceHub");
        if (plugin == null) 
        {
            return;
        }
        InterfaceHub ifh = (InterfaceHub) plugin;
        administrationRun = plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					RegisteredServiceProvider<Administration> rsp = ifh
			        		.getServicesManager()
			        		.getRegistration(Administration.class);
			        if (rsp == null) 
			        {
			            return;
			        }
			        administrationConsumer = rsp.getProvider();
			        if(administrationConsumer != null)
			        {
			    		log.info(pluginName + " detected InterfaceHub >>> Administration.class is consumed!");
			    		administrationRun.cancel();
			        }
				} catch(NoClassDefFoundError e)
				{
					administrationRun.cancel();
				}
			}
		}, 15L*1000, 25L, TimeUnit.MILLISECONDS);
        return;
	}
	
	public Administration getAdministration()
	{
		return administrationConsumer;
	}
}
