package main.java.me.avankziar.automaticexecute.spigot.assistance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.automaticexecute.spigot.AutomaticExecute;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class ChatApi
{
	public static String tl(String s)
	{
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	public static TextComponent tc(String s)
	{
		return new TextComponent(TextComponent.fromLegacyText(s));
	}
	
	public static TextComponent tctl(String s)
	{
		return new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', s)));
	}
	
	public static TextComponent TextWithExtra(String s, List<BaseComponent> list)
	{
		TextComponent tc = tctl(s);
		tc.setExtra(list);
		return tc;
	}
	
	public static TextComponent clickEvent(TextComponent msg, ClickEvent.Action caction, String cmd)
	{
		msg.setClickEvent( new ClickEvent(caction, cmd));
		return msg;
	}
	
	public static TextComponent clickEvent(String text, ClickEvent.Action caction, String cmd)
	{
		TextComponent msg = null;
		msg = tctl(text);
		msg.setClickEvent( new ClickEvent(caction, cmd));
		return msg;
	}
	
	public static TextComponent hoverEvent(TextComponent msg, HoverEvent.Action haction, String[] hover)
	{
		ArrayList<BaseComponent> components = new ArrayList<>();
		TextComponent hoverMessage = new TextComponent(new ComponentBuilder().create());
		TextComponent newLine = new TextComponent(ComponentSerializer.parse("{text: \"\n\"}"));
		int i = 0; 
		for(String h : hover)
		{
			if(i == 0)
			{
				hoverMessage.addExtra(new TextComponent(new ComponentBuilder(tl(h)).create()));
			} else
			{
				hoverMessage.addExtra(newLine);
				hoverMessage.addExtra(new TextComponent(new ComponentBuilder(tl(h)).create()));
			}
			i++;
		}
		components.add(hoverMessage);
		BaseComponent[] hoverToSend = (BaseComponent[])components.toArray(new BaseComponent[components.size()]); 
		msg.setHoverEvent( new HoverEvent(haction, hoverToSend));
		return msg;
	}
	
	public static TextComponent hoverEvent(String text, HoverEvent.Action haction, String[] hover)
	{
		TextComponent msg = null;
		ArrayList<BaseComponent> components = new ArrayList<>();
		msg = tctl(text);
		TextComponent hoverMessage = new TextComponent(new ComponentBuilder().create());
		TextComponent newLine = new TextComponent(ComponentSerializer.parse("{text: \"\n\"}"));
		int i = 0; 
		for(String h : hover)
		{
			if(i == 0)
			{
				hoverMessage.addExtra(new TextComponent(new ComponentBuilder(tl(h)).create()));
			} else
			{
				hoverMessage.addExtra(newLine);
				hoverMessage.addExtra(new TextComponent(new ComponentBuilder(tl(h)).create()));
			}
			i++;
		}
		components.add(hoverMessage);
		BaseComponent[] hoverToSend = (BaseComponent[])components.toArray(new BaseComponent[components.size()]); 
		msg.setHoverEvent( new HoverEvent(haction, hoverToSend));
		return msg;
	}
	
	public static TextComponent apiChat(String text, ClickEvent.Action caction, String cmd,
			HoverEvent.Action haction, String[] hover)
	{
		TextComponent msg = null;
		msg = tctl(text);
		if(caction != null)
		{
			msg.setClickEvent( new ClickEvent(caction, cmd));
		}
		if(haction != null)
		{
			hoverEvent(msg, haction, hover);
		}
		return msg;
	}
	
	public static TextComponent apiChatItem(String text, ClickEvent.Action caction, String cmd,
			String itemStringFromReflection)
	{
		TextComponent msg = tctl(text);
		if(caction != null && cmd != null)
		{
			msg.setClickEvent( new ClickEvent(caction, cmd));
		}
		msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, 
				new BaseComponent[]{new TextComponent(itemStringFromReflection)}));
		return msg;
	}
	
	public static TextComponent generateTextComponent(String message)
	{
		AutomaticExecute plugin = AutomaticExecute.plugin;
		String[] array = message.split(" ");
		YamlConfiguration cfg = plugin.getYamlHandler().get();
		String idclick = cfg.getString("Identifier.Click");
		String idhover = cfg.getString("Identifier.Hover");
		String idbook = cfg.getString("Identifier.Book");
		String sepb = cfg.getString("Seperator.BetweenFunction");
		String sepw = cfg.getString("Seperator.WhithinFuction");
		String sepspace = cfg.getString("Seperator.Space");
		String sepnewline = cfg.getString("Seperator.HoverNewLine");
		List<BaseComponent> list = new ArrayList<BaseComponent>();
		TextComponent textcomponent = tc("");
		String lastColor = null;
		for(int i = 0; i < array.length; i++)
		{
			String word = array[i];
			lastColor = getLastColor(lastColor, word);
			//Word enthält Funktion
			if(word.contains(idclick) || word.contains(idhover) || word.contains(idbook))
			{
				if(word.contains(sepb))
				{
					String[] functionarray = word.split(sepb);
					String originmessage = null;
					if(i+1 == array.length)
					{
						//Letzter Value
						originmessage = functionarray[0].replace(sepspace, " ");
					} else
					{
						originmessage = functionarray[0].replace(sepspace, " ")+" ";
					}
					//Eine Funktion muss mehr als einen wert haben
					if(functionarray.length<2)
					{
						continue;
					}
					//Word enthält ein buch, ist gesondert zu behandlen
					if(word.contains(idbook))
					{
						for(String f : functionarray)
						{
							//es wird nach der funktion gesucht
							if(f.contains(idbook))
							{
								
								String[] function = f.split(sepw);
								if(function.length!=2)
								{
									continue;
								}
								String bookpath = function[1];
								String book_json = null;
								try
								{
									String base64 = (String)plugin.getMysqlHandler().getDataI(
											bookpath, "itemstack_json", "item_name");
									ItemStack book = ItemStackParser.FromBase64ItemStack(base64);
									book_json = plugin.getItemStackParser().convertItemStackToJson(book);
								} catch (IOException e)
								{
									e.printStackTrace();
								}
								if(book_json == null)
								{
									list.add(tctl(originmessage));
									break;
								}
								TextComponent tc = apiChatItem(originmessage,
										ClickEvent.Action.RUN_COMMAND, "/autoex book "+bookpath, book_json);
								list.add(tc);
								break;
							}
						}
					} else
					{
						TextComponent tc = tctl(lastColor+originmessage);
						for(String f : functionarray)
						{
							if(f.contains(idclick))
							{
								String[] function = f.split(sepw);
								if(function.length!=3)
								{
									continue;
								}
								String clickaction = function[1];
								String clickstring = function[2].replace(sepspace, " ");
								clickEvent(tc, ClickEvent.Action.valueOf(clickaction), clickstring);
							} else if(f.contains(idhover))
							{
								String[] function = f.split(sepw);
								if(function.length!=3)
								{
									continue;
								}
								if(function[1].equals("SHOW_ITEM"))
								{
									String base64 = (String)plugin.getMysqlHandler().getDataI(
											function[2], "itemstack_json", "item_name");
									ItemStack item = null;
									String item_json = null;
									try
									{
										item = ItemStackParser.FromBase64ItemStack(base64);
										item_json = plugin.getItemStackParser().convertItemStackToJson(item);
									} catch (IOException e)
									{
										e.printStackTrace();
									}
									
									
									if(item_json != null)
									{
										hoverEvent(tc, HoverEvent.Action.SHOW_ITEM,
												item_json.split(sepnewline));
									}
								} else
								{
									String hoveraction = function[1];
									String hoverstringpath = function[2];
									String hoverstring = tl(
											plugin.getYamlHandler().getA().getString(hoverstringpath));
									hoverEvent(tc, HoverEvent.Action.valueOf(hoveraction),
											hoverstring.split(sepnewline));
								}
							}
						}
						list.add(tc);
					}
				}
			} else
			{
				//Beinhalten keine Funktion
				if(i+1 == array.length)
				{
					list.add(tctl(lastColor+word));
				} else
				{
					list.add(tctl(lastColor+word+" "));
				}
			}
		}
		textcomponent.setExtra(list);
		return textcomponent;
	}
	
	private static String getLastColor(String lastColor, String s)
	{
		String color = lastColor;
		for(int i = s.length()-1; i >= 0; i--)
		{
			char c = s.charAt(i);
			   if(c == '&' || c == '§')
			   {
				   if(i+1<s.length())
				   {
					   char d = s.charAt(i+1);
					   if(d == '0' || d == '1' || d == '2' || d == '3' || d == '4' || d == '5' || d == '6'
							   || d == '7' || d == '8' || d == '9' || d == 'a' || d == 'A' || d == 'b' || d == 'B'
							   || d == 'c' || d == 'C' || d == 'd' || d == 'D' || d == 'e' || d == 'E'
							   || d == 'f' || d == 'F' || d == 'k' || d == 'K' || d == 'm' || d == 'M'
							   || d == 'n' || d == 'N' || d == 'l' || d == 'L' || d == 'o' || d == 'O'
							   || d == 'r' || d == 'R')
					   {
						   color = "§"+Character.toString(d);
					   }
				   }
			   }
		}
		return color;
	}
}
