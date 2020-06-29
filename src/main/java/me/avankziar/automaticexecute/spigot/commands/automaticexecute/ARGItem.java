package main.java.me.avankziar.automaticexecute.spigot.commands.automaticexecute;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.automaticexecute.general.StringValues;
import main.java.me.avankziar.automaticexecute.spigot.AutomaticExecute;
import main.java.me.avankziar.automaticexecute.spigot.assistance.ChatApi;
import main.java.me.avankziar.automaticexecute.spigot.commands.CommandModule;
import net.md_5.bungee.api.chat.ClickEvent;

public class ARGItem extends CommandModule
{
	private AutomaticExecute plugin;
	
	public ARGItem(AutomaticExecute plugin)
	{
		super(StringValues.ARG_AUTOE_ITEM,StringValues.ARG_AUTOE_ITEM_ALIAS,
				AutomaticExecute.automarguments,1,3,StringValues.ARG_AUTOE_ITEM_ALIAS,
				StringValues.AUTOE_SUGGEST_ITEM);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String path = StringValues.PATH_AUTOE;
		if(player.getInventory().getItemInMainHand()==null)
		{
			///Du hast kein Item in der Hand!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(path+"Item.NoItemInHand")));
			return;
		}
		if(player.getInventory().getItemInMainHand().getType()==Material.AIR)
		{
			///Du hast kein Item in der Hand!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(path+"Item.NoItemInHand")));
			return;
		}
		ItemStack item = player.getInventory().getItemInMainHand().clone();
		String itemname = "test";
		if(args.length >= 2)
		{
			itemname = args[1];
		} else if(item.getItemMeta() != null && item.getItemMeta().hasDisplayName())
		{
			itemname = item.getItemMeta().getDisplayName();
		} else
		{
			itemname = item.getType().toString();
		}
		if(args.length == 3)
		{
			if(args[2].equalsIgnoreCase("delete") || args[2].equalsIgnoreCase("löschen"))
			{
				if(!plugin.getMysqlHandler().existItem(itemname))
				{
					player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(path+"Item.ItemNameNotExist")
							.replace("%itemname%", itemname)));
					return;
				}
				plugin.getMysqlHandler().deleteDataI(itemname, "item_name");
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(path+"Item.ItemDelete")
						.replace("%itemname%", itemname)));
				return;
			}
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("InputIsWrong")));
			return;
		} else
		{
			if(plugin.getMysqlHandler().existItem(itemname))
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(path+"Item.ItemNameAlreadyExist")
						.replace("%itemname%", itemname)));
				return;
			}
			plugin.getMysqlHandler().createItem(item, itemname);
			///Das Item &f%itemname% wurde erstellt. Klicken Sie auf die Nachricht um den Namen durch die URL Abfrage zu kopieren.
			player.spigot().sendMessage(ChatApi.clickEvent(plugin.getYamlHandler().getL().getString(path+"Item.ItemCreate")
					.replace("%itemname%", itemname), ClickEvent.Action.COPY_TO_CLIPBOARD , itemname));
		}
		return;
	}
}