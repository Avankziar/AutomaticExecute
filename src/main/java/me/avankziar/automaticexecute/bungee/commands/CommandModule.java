package main.java.me.avankziar.automaticexecute.bungee.commands;

import java.util.HashMap;

import net.md_5.bungee.api.CommandSender;

public abstract class CommandModule
{
	public String argument;
	public String permission;
    public int minArgs;
    public int maxArgs;
    public String alias;
    public String commandSuggest;

	public CommandModule(String argument, String permission, HashMap<String, CommandModule> map, 
    		int minArgs, int maxArgs, String alias, String commandSuggest)
    {
        this.argument = argument;
        this.permission = permission;
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
        this.alias = alias;
        this.commandSuggest = commandSuggest;

		map.put(argument, this);
		map.put(alias, this);
    }
    
    //This method will process the command.
    public abstract void run(CommandSender sender, String[] args);
}