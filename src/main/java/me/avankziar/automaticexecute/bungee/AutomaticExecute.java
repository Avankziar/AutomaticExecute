package main.java.me.avankziar.automaticexecute.bungee;

import java.util.HashMap;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.me.avankziar.automaticexecute.bungee.assistance.BackgroundTask;
import main.java.me.avankziar.automaticexecute.bungee.assistance.ChatApi;
import main.java.me.avankziar.automaticexecute.bungee.assistance.Utility;
import main.java.me.avankziar.automaticexecute.bungee.commands.CommandExecutorAutomaticExecute;
import main.java.me.avankziar.automaticexecute.bungee.commands.CommandHelper;
import main.java.me.avankziar.automaticexecute.bungee.commands.CommandModule;
import main.java.me.avankziar.automaticexecute.bungee.commands.autoexecute.ARGInfo;
import main.java.me.avankziar.automaticexecute.bungee.commands.autoexecute.ARGReload;
import main.java.me.avankziar.automaticexecute.bungee.commands.autoexecute.ARGSend;
import main.java.me.avankziar.automaticexecute.bungee.database.MysqlHandler;
import main.java.me.avankziar.automaticexecute.bungee.database.MysqlSetup;
import main.java.me.avankziar.automaticexecute.bungee.database.YamlHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class AutomaticExecute extends Plugin
{
	public static Logger log;
	public static String pluginName = "AutomaticExecute";
	private static AutomaticExecute plugin;
	private static YamlHandler yamlHandler;
	private static MysqlHandler mysqlHandler;
	private static MysqlSetup mysqlSetup;
	private static Utility utility;
	private static BackgroundTask backgroundTask;
	private static CommandHelper commandHelper;
	public static HashMap<String, CommandModule> automarguments;
	
	private static ChatApi chatApi;
	
	public void onEnable() 
	{
		plugin = this;
		log = getLogger();
		/*
		 * Initialization of all project important variables
		 */
		automarguments = new HashMap<String, CommandModule>();
		yamlHandler = new YamlHandler(this);
		utility = new Utility(this);
		if(yamlHandler.get().getBoolean("Mysql.Status", false))
		{
			mysqlHandler = new MysqlHandler(this);
			mysqlSetup = new MysqlSetup(this);
		} else
		{
			disablePlugin();
			log.severe("MySQL is not enabled! "+pluginName+" is disabled!");
			return;
		}
		backgroundTask = new BackgroundTask(this);
		commandHelper = new CommandHelper(this);
		
		CommandSetup();
		ListenerSetup();
	}
	
	public void onDisable()
	{
		getProxy().getScheduler().cancel(this);
		if(yamlHandler.get().getBoolean("Mysql.Status", false))
		{
			if (mysqlSetup.getConnection() != null) 
			{
				//backgroundtask.onShutDownDataSave();
				mysqlSetup.closeConnection();
			}
		}
		
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
	
	public MysqlSetup getMysqlSetup() 
	{
		return mysqlSetup;
	}
	
	public MysqlHandler getMysqlHandler()
	{
		return mysqlHandler;
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
		if(!utility.loadUtility())
		{
			return false;
		}
		if(!backgroundTask.loadBackgroundTask())
		{
			return false;
		}
		if(yamlHandler.get().getBoolean("Mysql.Status", false))
		{
			mysqlSetup.closeConnection();
			if(!mysqlHandler.loadMysqlHandler())
			{
				return false;
			}
			if(!mysqlSetup.loadMysqlSetup())
			{
				return false;
			}
		} else 
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
		PluginManager pm = getProxy().getPluginManager();
		new ARGReload(this);
		new ARGInfo(this);
		new ARGSend(plugin);
		pm.registerCommand(this, new CommandExecutorAutomaticExecute(this));
	}
	
	public void ListenerSetup()
	{
		//PluginManager pm = getProxy().getPluginManager();
		//pm.registerListener(this, new EventName(this));
	}

	public CommandHelper getCommandHelper()
	{
		return commandHelper;
	}
}
