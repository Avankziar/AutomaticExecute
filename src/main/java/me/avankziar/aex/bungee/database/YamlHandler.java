package main.java.me.avankziar.aex.bungee.database;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;

import main.java.me.avankziar.aex.bungee.AutomaticExecute;
import main.java.me.avankziar.aex.general.Language;
import main.java.me.avankziar.aex.general.YamlManager;
import main.java.me.avankziar.aex.general.Language.ISO639_2B;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class YamlHandler
{
	private AutomaticExecute plugin;
	private File config = null;
	private Configuration cfg = new Configuration();
	/*private File commands = null;
	private Configuration com = new Configuration();*/
	private File automatic = null;
	private Configuration aum = new Configuration();

	/*private File language = null;
	private Configuration lang = new Configuration();*/
	private String languages;
	
	public YamlHandler(AutomaticExecute plugin)
	{
		this.plugin = plugin;
		loadYamlHandler();
	}
	
	public boolean loadYamlHandler()
	{
		plugin.setYamlManager(new YamlManager(false));
		if(!mkdirStaticFiles())
		{
			return false;
		}
		if(!mkdirDynamicFiles())
		{
			return false;
		}
		return true;
	}
	 
	public Configuration getConfig()
	{
		return cfg;
	}
	
	/*public Configuration getCommands()
	{
		return com;
	}
	
	public Configuration getLang()
	{
		return lang;
	}*/
	
	public Configuration getAutoEx()
	{
		return aum;
	}
	
	public boolean mkdirStaticFiles()
	{
		File directory = new File(plugin.getDataFolder()+"");
		if(!directory.exists())
		{
			directory.mkdir();
		}
		config = new File(plugin.getDataFolder(), "config.yml");
		if(!config.exists()) 
		{
			AutomaticExecute.log.info("Create config.yml...");
			 try (InputStream in = plugin.getResourceAsStream("default.yml")) 
	    	 {       
	    		 Files.copy(in, config.toPath());
	         } catch (IOException e) 
	    	 {
	        	 e.printStackTrace();
	        	 return false;
	         }
		}
		cfg = loadYamlTask(config, cfg);
		writeFile(config, cfg, plugin.getYamlManager().getConfigKey());
		
		languages = cfg.getString("Language", "ENG").toUpperCase();
		
		/*commands = new File(plugin.getDataFolder(), "commands.yml");
		if(!commands.exists()) 
		{
			AutomaticExecute.log.info("Create commands.yml...");
			 try (InputStream in = plugin.getResourceAsStream("default.yml")) 
	    	 {       
	    		 Files.copy(in, commands.toPath());
	         } catch (IOException e) 
	    	 {
	        	 e.printStackTrace();
	        	 return false;
	         }
		}
		com = loadYamlTask(commands, com);
		writeFile(commands, com, plugin.getYamlManager().getCommandsKey());*/
		
		automatic = new File(plugin.getDataFolder(), "automaticexecute.yml");
		if(!automatic.exists()) 
		{
			AutomaticExecute.log.info("Create automaticexecute.yml...");
			 try (InputStream in = plugin.getResourceAsStream("automaticexecute.yml")) 
	    	 {       
	    		 Files.copy(in, automatic.toPath());
	         } catch (IOException e) 
	    	 {
	        	 e.printStackTrace();
	        	 return false;
	         }
		}
		aum = loadYamlTask(automatic, aum);
		//writeFile(automatic, cti, plugin.getYamlManager().getChatTitleKey());
		return true;
	}
	
	private boolean mkdirDynamicFiles()
	{
		List<Language.ISO639_2B> types = new ArrayList<Language.ISO639_2B>(EnumSet.allOf(Language.ISO639_2B.class));
		ISO639_2B languageType = ISO639_2B.ENG;
		for(ISO639_2B type : types)
		{
			if(type.toString().equals(languages))
			{
				languageType = type;
				break;
			}
		}
		plugin.getYamlManager().setLanguageType(languageType);
		/*if(!mkdirLanguage())
		{
			return false;
		}*/
		return true;
	}
	
	/*private boolean mkdirLanguage()
	{
		String languageString = plugin.getYamlManager().getLanguageType().toString().toLowerCase();
		File directory = new File(plugin.getDataFolder()+"/Languages/");
		if(!directory.exists())
		{
			directory.mkdir();
		}
		language = new File(directory.getPath(), languageString+".yml");
		if(!language.exists()) 
		{
			AutomaticExecute.log.info("Create %lang%.yml...".replace("%lang%", languageString));
			 try (InputStream in = plugin.getResourceAsStream("default.yml")) 
	    	 {       
	    		 Files.copy(in, language.toPath());
	         } catch (IOException e) 
	    	 {
	        	 e.printStackTrace();
	        	 return false;
	         }
		}
		lang = loadYamlTask(language, lang);
		writeFile(language, lang, plugin.getYamlManager().getLanguageKey());
		return true;
	}*/
	
	private Configuration loadYamlTask(File file, Configuration yaml)
	{
		Configuration y = null;
		try 
		{
			yaml = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
		} catch (IOException e) 
		{
			AutomaticExecute.log.severe(
					"Could not load the %file% file! You need to regenerate the %file%! Error: ".replace("%file%", file.getName())
					+ e.getMessage());
			e.printStackTrace();
		}
		y = yaml;
		return y;
	}
	
	private boolean writeFile(File file, Configuration yml, LinkedHashMap<String, Language> keyMap) 
	{
		for(String key : keyMap.keySet())
		{
			Language languageObject = keyMap.get(key);
			if(languageObject.languageValues.containsKey(plugin.getYamlManager().getLanguageType()) == true)
			{
				plugin.getYamlManager().setFileInputBungee(yml, keyMap, key, plugin.getYamlManager().getLanguageType());
			} else if(languageObject.languageValues.containsKey(plugin.getYamlManager().getDefaultLanguageType()) == true)
			{
				plugin.getYamlManager().setFileInputBungee(yml, keyMap, key, plugin.getYamlManager().getDefaultLanguageType());
			}
		}
		try
		{
			 ConfigurationProvider.getProvider(YamlConfiguration.class).save(yml, file);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return true;
	}
}