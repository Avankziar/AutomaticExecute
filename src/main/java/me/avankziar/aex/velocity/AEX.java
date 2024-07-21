package main.java.me.avankziar.aex.velocity;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import main.java.me.avankziar.aex.general.database.YamlHandler;
import main.java.me.avankziar.aex.general.database.YamlManager;
import me.avankziar.ifh.velocity.IFH;
import me.avankziar.ifh.velocity.administration.Administration;
import me.avankziar.ifh.velocity.plugin.RegisteredServiceProvider;

@Plugin(
		id = "automaticexecute", 
		name = "AutomaticExecute", 
		version = "3-6-0",
		url = "https://www.spigotmc.org/resources/authors/avankziar.332028/",
		dependencies = {},
		description = "Afk Tracker Plugin",
		authors = {"Avankziar"}
)
public class AEX
{
	private static AEX plugin;
    private final ProxyServer server;
    private Logger logger = null;
    private Path dataDirectory;
	public static String pluginname = "AEX";
	private YamlHandler yamlHandler;
	private YamlManager yamlManager;
	private static Administration administrationConsumer;
    
    
    @Inject
    public AEX(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) 
    {
    	AEX.plugin = this;
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }
    
    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) 
    {
    	logger = Logger.getLogger("AEX");
    	PluginDescription pd = server.getPluginManager().getPlugin(pluginname.toLowerCase()).get().getDescription();
        List<String> dependencies = new ArrayList<>();
        pd.getDependencies().stream().allMatch(x -> dependencies.add(x.toString()));
        //https://patorjk.com/software/taag/#p=display&f=ANSI%20Shadow&t=SCC
		logger.info("  █████╗ ███████╗██╗  ██╗ | Id: "+pd.getId());
		logger.info(" ██╔══██╗██╔════╝╚██╗██╔╝ | Version: "+pd.getVersion().get());
		logger.info(" ███████║█████╗   ╚███╔╝  | Author: ["+String.join(", ", pd.getAuthors())+"]");
		logger.info(" ██╔══██║██╔══╝   ██╔██╗  | Description: "+(pd.getDescription().isPresent() ? pd.getDescription().get() : "/"));
		logger.info(" ██║  ██║███████╗██╔╝ ██╗ | Plugin Website:"+pd.getUrl().get().toString());
		logger.info(" ╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝ | Dependencies Plugins: ["+String.join(", ", dependencies)+"]");
        
		setupIFHAdministration();
		
		yamlHandler = new YamlHandler(YamlManager.Type.VELO, pluginname, logger, dataDirectory,
        		(plugin.getAdministration() == null ? null : plugin.getAdministration().getLanguage()));
        setYamlManager(yamlHandler.getYamlManager());
    }
    
    public static AEX getPlugin()
    {
    	return AEX.plugin;
    }
    
    public ProxyServer getServer()
    {
    	return server;
    }
    
    public Logger getLogger()
    {
    	return logger;
    }
    
    public Path getDataDirectory()
    {
    	return dataDirectory;
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
    	this.yamlManager = yamlManager;
    }
	
	private void setupIFHAdministration()
	{ 
		Optional<PluginContainer> ifhp = plugin.getServer().getPluginManager().getPlugin("interfacehub");
        if (ifhp.isEmpty()) 
        {
        	logger.info(pluginname + " dont find InterfaceHub!");
            return;
        }
        me.avankziar.ifh.velocity.IFH ifh = IFH.getPlugin();
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
    		logger.info(pluginname + " detected InterfaceHub >>> Administration.class is consumed!");
        }
        return;
	}
	
	public Administration getAdministration()
	{
		return administrationConsumer;
	}
}