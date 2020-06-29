package main.java.me.avankziar.automaticexecute.spigot;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import main.java.me.avankziar.automaticexecute.spigot.assistance.BackgroundTask;
import main.java.me.avankziar.automaticexecute.spigot.assistance.ItemStackParser;
import main.java.me.avankziar.automaticexecute.spigot.assistance.Utility;
import main.java.me.avankziar.automaticexecute.spigot.commands.CommandHelper;
import main.java.me.avankziar.automaticexecute.spigot.commands.CommandModule;
import main.java.me.avankziar.automaticexecute.spigot.commands.MultipleCommandExecutor;
import main.java.me.avankziar.automaticexecute.spigot.commands.TABCompletion;
import main.java.me.avankziar.automaticexecute.spigot.commands.automaticexecute.ARGBook;
import main.java.me.avankziar.automaticexecute.spigot.commands.automaticexecute.ARGItem;
import main.java.me.avankziar.automaticexecute.spigot.commands.automaticexecute.ARGReload;
import main.java.me.avankziar.automaticexecute.spigot.commands.automaticexecute.ARGSend;
import main.java.me.avankziar.automaticexecute.spigot.database.MysqlHandler;
import main.java.me.avankziar.automaticexecute.spigot.database.MysqlSetup;
import main.java.me.avankziar.automaticexecute.spigot.database.YamlHandler;

public class AutomaticExecute extends JavaPlugin
{
	public static AutomaticExecute plugin;
	public static Logger log;
	public static String pluginName = "AutomanticExecute";
	private static YamlHandler yamlHandler;
	private static MysqlSetup mysqlSetup;
	private static MysqlHandler mysqlHandler;
	private static BackgroundTask backgroundTask;
	private static CommandHelper commandHelper;
	private static Utility utility;
	public static HashMap<String, CommandModule> automarguments;
	private ItemStackParser itemStackParser;
	
	public void onEnable()
	{
		plugin = this;
		log = getLogger();
		automarguments = new HashMap<String, CommandModule>();
		yamlHandler = new YamlHandler(this);
		commandHelper = new CommandHelper(this);
		itemStackParser = new ItemStackParser();
		utility = new Utility(this);
		if(yamlHandler.get().getBoolean("Mysql.Status", false))
		{
			mysqlHandler = new MysqlHandler(this);
			mysqlSetup = new MysqlSetup(this);
		} else
		{
			log.severe("MySQL is not set in the Plugin "+pluginName+"!");
			Bukkit.getPluginManager().getPlugin("AutomaticExecute").getPluginLoader().disablePlugin(this);
			return;
		}
		backgroundTask = new BackgroundTask(this);
		CommandSetup();
		ListenerSetup();
	}
	
	public void onDisable()
	{
		Bukkit.getScheduler().cancelTasks(this);
		HandlerList.unregisterAll(this);
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
	
	public BackgroundTask getBackgroundTask()
	{
		return backgroundTask;
	}
	
	public CommandHelper getCommandHelper()
	{
		return commandHelper;
	}
	
	public Utility getUtility()
	{
		return utility;
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
	
	public void CommandSetup()
	{
		new ARGBook(this);
		new ARGItem(this);
		new ARGReload(this);
		new ARGSend(plugin);
		getCommand("autoex").setExecutor(new MultipleCommandExecutor(this));
		getCommand("autoex").setTabCompleter(new TABCompletion());
	}
	
	
	public void ListenerSetup()
	{
		//PluginManager pm = getServer().getPluginManager();
	}

	public ItemStackParser getItemStackParser()
	{
		return itemStackParser;
	}
}