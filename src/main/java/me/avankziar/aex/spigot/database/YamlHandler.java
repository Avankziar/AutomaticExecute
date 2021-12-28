package main.java.me.avankziar.aex.spigot.database;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import main.java.me.avankziar.aex.general.Language;
import main.java.me.avankziar.aex.general.YamlManager;
import main.java.me.avankziar.aex.general.Language.ISO639_2B;
import main.java.me.avankziar.aex.spigot.AutomaticExecute;

public class YamlHandler
{
	private AutomaticExecute plugin;
	private File config = null;
	private YamlConfiguration cfg = new YamlConfiguration();
	
	/*private File commands = null;
	private YamlConfiguration com = new YamlConfiguration();*/
	
	private File automaticexecute = null;
	private YamlConfiguration aum = new YamlConfiguration();
	
	private String languages;
	/*private File language = null;
	private YamlConfiguration lang = new YamlConfiguration();*/
	
	public YamlHandler(AutomaticExecute plugin) throws IOException 
	{
		this.plugin = plugin;
		loadYamlHandler();
	}
	
	public boolean loadYamlHandler() throws IOException
	{
		if(!mkdirStaticFiles())
		{
			return false;
		}
		
		if(!mkdirDynamicFiles()) //Per language one file
		{
			return false;
		}
		return true;
	}
	
	public YamlConfiguration getConfig()
	{
		return cfg;
	}
	
	/*public YamlConfiguration getLang()
	{
		return lang;
	}*/
	
	public YamlConfiguration getAutoEx()
	{
		return aum;
	}
	
	/*public YamlConfiguration getCom()
	{
		return com;
	}*/
	
	public boolean mkdirStaticFiles() throws IOException
	{
		//Erstellen aller Werte FÜR die Config.yml
		plugin.setYamlManager(new YamlManager(true));
		
		File directory = new File(plugin.getDataFolder()+"");
		if(!directory.exists())
		{
			directory.mkdir();
		}
		
		//Initialisierung der config.yml
		config = new File(plugin.getDataFolder(), "config.yml");
		if(!config.exists()) 
		{
			AutomaticExecute.log.info("Create config.yml...");
			try(InputStream in = plugin.getResource("default.yml"))
			{
				//Erstellung einer "leere" config.yml
				//FileUtils.copyToFile(plugin.getResource("default.yml"), config);
				Files.copy(in, config.toPath());
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		//Laden der config.yml
		cfg = loadYamlTask(config, cfg);
        if(cfg == null)
        {
        	return false;
        }
		//Niederschreiben aller Werte für die Datei
		writeFile(config, cfg, plugin.getYamlManager().getConfigKey());
		
		languages = cfg.getString("Language", "ENG").toUpperCase();
		
		/*commands = new File(plugin.getDataFolder(), "commands.yml");
		if(!commands.exists()) 
		{
			AutomaticExecute.log.info("Create commands.yml...");
			try(InputStream in = plugin.getResource("default.yml"))
			{
				//Erstellung einer "leere" config.yml
				Files.copy(in, commands.toPath());
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		com = loadYamlTask(commands, com);
        if(com == null)
        {
        	return false;
        }
		writeFile(commands, com, plugin.getYamlManager().getCommandsKey());*/
		
		//Hier wird eine Ausnahme gemacht
		automaticexecute = new File(plugin.getDataFolder(), "automaticexecute.yml");
		if(!automaticexecute.exists()) 
		{
			AutomaticExecute.log.info("Create automaticexecute.yml...");
			try(InputStream in = plugin.getResource("automaticexecute.yml")) //<= Ausnahme
			{
				//Erstellung einer "leere" config.yml
				Files.copy(in, automaticexecute.toPath());
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		aum = loadYamlTask(automaticexecute, aum);
        if(aum == null)
        {
        	return false;
        }
		//writeFile(automaticexecute, aum, plugin.getYamlManager().getAutomaticExecuteKey());
		return true;
	}
	
	private boolean mkdirDynamicFiles() throws IOException
	{
		//Vergleich der Sprachen
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
		//Setzen der Sprache
		plugin.getYamlManager().setLanguageType(languageType);
		
		/*if(!mkdirLanguage())
		{
			return false;
		}*/
		return true;
	}
	
	/*private boolean mkdirLanguage() throws IOException
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
			try(InputStream in = plugin.getResource("default.yml"))
			{
				Files.copy(in, language.toPath());
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		//Laden der Datei
		lang = loadYamlTask(language, lang);
        if(lang == null)
        {
        	return false;
        }
		//Niederschreiben aller Werte in die Datei
		writeFile(language, lang, plugin.getYamlManager().getLanguageKey());
		return true;
	}*/
	
	private YamlConfiguration loadYamlTask(File file, YamlConfiguration yaml)
	{
		try 
		{
			yaml.load(file);
		} catch (IOException | InvalidConfigurationException e) 
		{
			AutomaticExecute.log.severe(
					"Could not load the %file% file! You need to regenerate the %file%! Error: ".replace("%file%", file.getName())
					+ e.getMessage());
			e.printStackTrace();
		}
		return yaml;
	}
	
	private boolean writeFile(File file, YamlConfiguration yml, LinkedHashMap<String, Language> keyMap) throws IOException
	{
		yml.options().header("For more explanation see \n https://www.spigotmc.org");
		for(String key : keyMap.keySet())
		{
			Language languageObject = keyMap.get(key);
			if(languageObject.languageValues.containsKey(plugin.getYamlManager().getLanguageType()) == true)
			{
				plugin.getYamlManager().setFileInputBukkit(yml, keyMap, key, plugin.getYamlManager().getLanguageType());
			} else if(languageObject.languageValues.containsKey(plugin.getYamlManager().getDefaultLanguageType()) == true)
			{
				plugin.getYamlManager().setFileInputBukkit(yml, keyMap, key, plugin.getYamlManager().getDefaultLanguageType());
			}
		}
		yml.save(file);
		return true;
	}
}