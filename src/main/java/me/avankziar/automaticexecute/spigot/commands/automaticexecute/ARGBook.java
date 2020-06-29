package main.java.me.avankziar.automaticexecute.spigot.commands.automaticexecute;

import java.io.IOException;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.automaticexecute.general.StringValues;
import main.java.me.avankziar.automaticexecute.spigot.AutomaticExecute;
import main.java.me.avankziar.automaticexecute.spigot.assistance.ChatApi;
import main.java.me.avankziar.automaticexecute.spigot.assistance.ItemStackParser;
import main.java.me.avankziar.automaticexecute.spigot.commands.CommandModule;

public class ARGBook extends CommandModule
{
	private AutomaticExecute plugin;
	
	public ARGBook(AutomaticExecute plugin)
	{
		super(StringValues.ARG_AUTOE_BOOK,StringValues.PERM_CMD_AUTOE_BOOK,
				AutomaticExecute.automarguments,2,2, StringValues.ARG_AUTOE_BOOK_ALIAS,
				StringValues.AUTOE_SUGGEST_BOOK);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String path = StringValues.PATH_AUTOE;
		String itemname = args[1];
		if(plugin.getMysqlHandler().existItem(itemname) == false)
		{
			///Das Buch &f%bookname% &cexistiert nicht.
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(path+"Book.BookNotExist")
					.replace("%bookname%", itemname)));
			return;
		}
		ItemStack item = null;
		try
		{
			item = ItemStackParser.FromBase64ItemStack(
					(String)plugin.getMysqlHandler().getDataI(itemname, "itemstack_json", "item_name"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		if(item == null)
		{
			///Das Buch &f%bookname% &chat beinhaltet einen Fehler. Es kann nicht geöffnet werden!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(path+"Book.BookNotExist")
					.replace("%bookname%", itemname)));
			return;
		}
		if(item.getType() != Material.WRITABLE_BOOK && item.getType() != Material.WRITTEN_BOOK)
		{
			///Das Item &f%bookname% &cist kein Buch!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(path+"Book.ItemIsNotABook")
					.replace("%bookname%", itemname)));
			return;
		}
		player.openBook(item);
		/*if(plugin.getUtility().existMethod(BookUtil.class, "openPlayer"))
		{
			
			//BookUtil.openPlayer(player, item);
		} else
		{
			///Das Plugin BookUtil ist nicht geladen worden oder fehlt!
			player.spigot().sendMessage(ChatApi.tctl(aum.getString(path+"Book.BookUtilIsMissing")));
			return;
		}*/
		return;
	}
}