package main.java.me.avankziar.aex.general;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import main.java.me.avankziar.aex.general.Language.ISO639_2B;

public class YamlManager
{
	private ISO639_2B languageType = ISO639_2B.GER;
	private ISO639_2B defaultLanguageType = ISO639_2B.GER;
	private static LinkedHashMap<String, Language> configKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> commandsKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> languageKeys = new LinkedHashMap<>();
	
	private static LinkedHashMap<String, Language> configAutomaticExecuteKeys = new LinkedHashMap<>();
	
	public YamlManager(boolean spigot) //INFO
	{
		initConfig();
		//initCommands();
		//initLanguage();	
	}
	
	public ISO639_2B getLanguageType()
	{
		return languageType;
	}

	public void setLanguageType(ISO639_2B languageType)
	{
		this.languageType = languageType;
	}
	
	public ISO639_2B getDefaultLanguageType()
	{
		return defaultLanguageType;
	}
	
	public LinkedHashMap<String, Language> getConfigKey()
	{
		return configKeys;
	}
	
	public LinkedHashMap<String, Language> getCommandsKey()
	{
		return commandsKeys;
	}
	
	public LinkedHashMap<String, Language> getLanguageKey()
	{
		return languageKeys;
	}
	
	public LinkedHashMap<String, Language> getAutomaticExecuteKey()
	{
		return configAutomaticExecuteKeys;
	}
	
	/*
	 * The main methode to set all paths in the yamls.
	 */
	public void setFileInputBungee(net.md_5.bungee.config.Configuration yml,
			LinkedHashMap<String, Language> keyMap, String key, ISO639_2B languageType)
	{
		if(!keyMap.containsKey(key))
		{
			return;
		}
		if(yml.get(key) != null)
		{
			return;
		}
		if(keyMap.get(key).languageValues.get(languageType).length == 1)
		{
			if(keyMap.get(key).languageValues.get(languageType)[0] instanceof String)
			{
				yml.set(key, ((String) keyMap.get(key).languageValues.get(languageType)[0]).replace("\r\n", ""));
			} else
			{
				yml.set(key, keyMap.get(key).languageValues.get(languageType)[0]);
			}
		} else
		{
			List<Object> list = Arrays.asList(keyMap.get(key).languageValues.get(languageType));
			ArrayList<String> stringList = new ArrayList<>();
			if(list instanceof List<?>)
			{
				for(Object o : list)
				{
					if(o instanceof String)
					{
						stringList.add(((String) o).replace("\r\n", ""));
					} else
					{
						stringList.add(o.toString().replace("\r\n", ""));
					}
				}
			}
			yml.set(key, (List<String>) stringList);
		}
	}
	
	public void setFileInputBukkit(org.bukkit.configuration.file.YamlConfiguration yml,
			LinkedHashMap<String, Language> keyMap, String key, ISO639_2B languageType)
	{
		if(!keyMap.containsKey(key))
		{
			return;
		}
		if(yml.get(key) != null)
		{
			return;
		}
		if(keyMap.get(key).languageValues.get(languageType).length == 1)
		{
			if(keyMap.get(key).languageValues.get(languageType)[0] instanceof String)
			{
				yml.set(key, ((String) keyMap.get(key).languageValues.get(languageType)[0]).replace("\r\n", ""));
			} else
			{
				yml.set(key, keyMap.get(key).languageValues.get(languageType)[0]);
			}
		} else
		{
			List<Object> list = Arrays.asList(keyMap.get(key).languageValues.get(languageType));
			ArrayList<String> stringList = new ArrayList<>();
			if(list instanceof List<?>)
			{
				for(Object o : list)
				{
					if(o instanceof String)
					{
						stringList.add(((String) o).replace("\r\n", ""));
					} else
					{
						stringList.add(o.toString().replace("\r\n", ""));
					}
				}
			}
			yml.set(key, (List<String>) stringList);
		}
	}
	
	@SuppressWarnings("unused")
	public void initConfig() //INFO:Config
	{
		Base:
		{
			configKeys.put("Language"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"ENG"}));
			configKeys.put("DocumentExecuteInLog"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
		}
		Generator:
		{
			configKeys.put("Identifier.Click"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"click"}));
			configKeys.put("Identifier.Hover"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"hover"}));
			configKeys.put("Seperator.BetweenFunction"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"~"}));
			configKeys.put("Seperator.WhithinFuction"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"@"}));
			configKeys.put("Seperator.Space"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"+"}));
			configKeys.put("Seperator.HoverNewLine"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"~!~"}));
		}
	}
	
	public void initLanguage() //INFO:Languages
	{
		languageKeys.put("InputIsWrong",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDeine Eingabe ist fehlerhaft! Klicke hier auf den Text, um weitere Infos zu bekommen!",
						"&cYour input is incorrect! Click here on the text to get more information!"}));
		languageKeys.put("NoPermission",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast dafür keine Rechte!",
						"&cYou don not have the rights!"}));
		languageKeys.put("NoPlayerExist",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Spieler ist nicht online oder existiert nicht!",
						"&cThe player is not online or does not exist!"}));
		languageKeys.put("PlayerDontExist",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Spieler existiert nicht! (Groß- und Kleinschreibung nicht richtig?)",
						"&cThe player does not exist! (Upper and lower case not correct?)"}));
		languageKeys.put("NoNumber",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas Argument &f%arg% &cmuss eine ganze Zahl sein.",
						"&cThe argument &f%arg% &must be an integer."}));
		languageKeys.put("NoNumberII",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEiner der Argumente muss eine Zahl sein.",
						"&cOne of the argument must be an number."}));
		languageKeys.put("NoDouble",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas Argument &f%arg% &cmuss eine Gleitpunktzahl sein!",
						"&cThe argument &f%arg% &must be a floating point number!"}));
		languageKeys.put("IsNegativ",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas Argument &f%arg% &cmuss eine positive Zahl sein!",
						"&cThe argument &f%arg% &must be a positive number!"}));
		languageKeys.put("ToHigh",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDie angegebene Zahl ist zu hoch! Erlaubtes Maximum: %max% %currency%",
						"&cThe specified number is too high! Maximum allowed: %max% %currency%"}));
		languageKeys.put("GeneralHover",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKlick mich!",
						"&eClick me!"}));
		languageKeys.put("NotEnumValue",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cNicht korrekter Wert! Erlaubte Werte sind: %enum%",
						"&cNot correct value! Permitted values are: %enum%"}));	
		lang();
		/*languageKeys.put(""
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"",
				""}))*/
	}
	
	private void lang()
	{
		/*String path = "CmdAutoE.";
		languageKeys.put(path+"Info.Headline",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e==========&7[&6AutomaticExecute&7]&e==========",
						""}));*/
	}
}
