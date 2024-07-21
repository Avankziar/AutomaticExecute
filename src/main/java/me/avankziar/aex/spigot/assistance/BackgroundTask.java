package main.java.me.avankziar.aex.spigot.assistance;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import dev.dejvokep.boostedyaml.YamlDocument;
import main.java.me.avankziar.aex.general.Rythmus;
import main.java.me.avankziar.aex.general.database.YamlHandler;
import main.java.me.avankziar.aex.spigot.AEX;
import main.java.me.avankziar.aex.spigot.object.AutoMessage;
import main.java.me.avankziar.aex.spigot.object.Titles;
import net.kyori.adventure.title.Title;

public class BackgroundTask
{
	private AEX plugin;
	public static List<AutoMessage> AutoMessageList = new ArrayList<AutoMessage>();
	
	public BackgroundTask(AEX plugin)
	{
		this.plugin = plugin;
		loadBackgroundTask();
		runTask();
		
	}
	
	public boolean loadBackgroundTask()
	{
		initList();
		return true;
	}
	
	public void initList()
	{
		if(!AutoMessageList.isEmpty())
		{
			AutoMessageList.clear();
		}
		YamlHandler yh = plugin.getYamlHandler();
		YamlDocument auto = yh.getAutoEx();
		String sepb = yh.getConfig().getString("Seperator.BetweenFunction", "~");
		for(String path : yh.getAutoEx().getRoutesAsStrings(false))
		{
			AEX.logger.info("Loading Message: "+path);
			if(auto.getString(path+".Rythmus") == null) continue;
			Rythmus rythmus = Rythmus.valueOf(auto.getString(path+".Rythmus", "ONCE"));
			String permission = null;
			if(auto.getString(path+".Permission") != null) permission = auto.getString(path+".Permission");
			LocalDate date = null;
			if(auto.getString(path+".SendingDate") != null)
			{
				 date = LocalDate.parse((CharSequence)auto.getString(path+".SendingDate"),
							DateTimeFormatter.ofPattern("dd.MM.yyyy"));
			} else
			{
				date = LocalDate.now();
			}
			LocalTime time = null;
			long interval = 0;
			long lasttimesend = System.currentTimeMillis();
			if(auto.get(path+".SendingTime") != null)
			{
				if(auto.getString(path+".SendingTime").split(":").length==3)
				{
					time = LocalTime.parse((CharSequence)auto.getString(path+".SendingTime"),
							DateTimeFormatter.ofPattern("HH:mm:ss"));
					interval = Utility.timeToLong(time);
					lasttimesend += interval;
				}
			}
			if(time == null)
			{
				time = LocalTime.now();
				interval = Utility.timeToLong(time);
				lasttimesend += interval;
			}
			List<LocalTime> timelist = new ArrayList<>();
			if(auto.getStringList(path+".SendingTimes") != null)
			{
				for(String tl : auto.getStringList(path+".SendingTimes"))
				{
					timelist.add(LocalTime.parse((CharSequence)tl,
							DateTimeFormatter.ofPattern("~HH:mm:ss")));
				}
			}
			boolean isRandom = false;
			if(auto.get(path+".IsRandom")!=null)
			{
				isRandom = auto.getBoolean(path+".IsRandom");
			}
			List<List<String>> RandomMessage = new ArrayList<List<String>>();
			if(isRandom)
			{
				for(String otherpath : auto.getStringList(path+".RandomMessage"))
				{
					if(auto.getString(otherpath)!=null)
					{
						if(auto.getStringList(otherpath)!=null)
						{
							List<String> rm = new ArrayList<String>();
							for(String message : auto.getStringList(otherpath))
							{
								rm.add(message);
							}
							RandomMessage.add(rm);
						}
					}
				}
			}
			List<String> Message = new ArrayList<String>();
			for(String message : auto.getStringList(path+".Message"))
			{
				Message.add(message);
			}
			List<String> consoleCommand = new ArrayList<String>();
			boolean doPlayerCommandWithPermission = false;
			List<String> playerCommand = new ArrayList<String>();
			if(auto.get(path+".DoConsoleCommand") != null)
			{
				if(auto.getStringList(path+".ConsoleCommand") != null)
				{
					consoleCommand = auto.getStringList(path+".ConsoleCommand");
				}
			}
			if(auto.get(path+".DoPlayerCommand") != null)
			{
				if(auto.getStringList(path+".PlayerCommand") != null)
				{
					playerCommand = auto.getStringList(path+".PlayerCommand");
				}
			}
			if(auto.get(path+".DoPlayerCommandWithPermission") != null)
			{
				doPlayerCommandWithPermission = auto.getBoolean(path+".DoPlayerCommandWithPermission");
				if(auto.getStringList(path+".PlayerCommand") != null)
				{
					playerCommand = auto.getStringList(path+".PlayerCommand");
				}
			}
			Titles title = null;
			if(auto.get(path+".TitleMessage") != null)
			{
				String[] ti = auto.getString(path+".TitleMessage").split(sepb);
				if(ti.length == 5)
				{
					title = new Titles(ti[0],
							ti[1],
							Integer.parseInt(ti[2]),
							Integer.parseInt(ti[3]),
							Integer.parseInt(ti[4]));
				}
			}
			Titles titleWithPermission = null;
			if(auto.get(path+".TitleMessageWithPermission") != null)
			{
				String[] ti = auto.getString(path+".TitleMessageWithPermission").split(sepb);
				if(ti.length == 5)
				{
					titleWithPermission = new Titles(
							ti[0],
							ti[1],
							Integer.parseInt(ti[2]),
							Integer.parseInt(ti[3]),
							Integer.parseInt(ti[4]));
				}
			}
			ArrayList<Sound> sounds = new ArrayList<Sound>();
			if(auto.get(path+".Sounds") != null)
			{
				for(String s : auto.getStringList(path+".Sounds"))
				{
					try
					{
						Sound sound = Sound.valueOf(s);
						sounds.add(sound);
					} catch(IllegalArgumentException e){}
				}
			}
			ArrayList<Sound> soundsWithPermission = new ArrayList<Sound>();
			if(auto.get(path+".SoundsWithPermission") != null)
			{
				for(String s : auto.getStringList(path+".SoundsWithPermission"))
				{
					try
					{
						Sound sound = Sound.valueOf(s);
						sounds.add(sound);
					} catch(IllegalArgumentException e){}
				}
			}
			AutoMessage am = new AutoMessage(path, rythmus, permission,
					isRandom, RandomMessage, Message, title, titleWithPermission,
					consoleCommand, doPlayerCommandWithPermission, playerCommand,
					date, time, timelist, interval, lasttimesend, sounds, soundsWithPermission);
			AutoMessageList.add(am);
		}
	}
	
	public void runTask()
	{
		new BukkitRunnable()
		{
			
			@Override
			public void run()
			{
				LocalDateTime now = LocalDateTime.now();
				if((now.getSecond() == 0 || now.getSecond() == 5 || now.getSecond() == 10 || now.getSecond() == 15
						 || now.getSecond() == 20 || now.getSecond() == 25 || now.getSecond() == 30 || now.getSecond() == 35
						 || now.getSecond() == 40 || now.getSecond() == 45 || now.getSecond() == 50 || now.getSecond() == 55)
						&& (now.getMinute() == 0 || now.getMinute() == 5 || now.getMinute() == 10 || now.getMinute() == 15
							|| now.getMinute() == 20 || now.getMinute() == 25 || now.getMinute() == 30 
							|| now.getMinute() == 35 || now.getMinute() == 40 || now.getMinute() == 50
							|| now.getMinute() == 55))
				{
					autoSendMessage(now);
					cancel();
				}
			}
		}.runTaskTimer(plugin, 20*5L, 20L);
	}
	
	public void autoSendMessage(LocalDateTime now)
	{
		AEX.logger.info("AutomaticExecute starts Scheduler at "+Utility.serialisedDateTime(now));
		new BukkitRunnable()
		{
			
			@Override
			public void run()
			{
				LocalDate ld = LocalDate.now();
				int day = ld.getDayOfMonth();
				int month = ld.getMonthValue();
				int year = ld.getYear();
				LocalTime lt = LocalTime.now();
				int hour = lt.getHour();
				int min = lt.getMinute();
				int sec = lt.getSecond();
				for(AutoMessage am : AutoMessageList)
				{
					if(am.getRythmus() == Rythmus.ONCE)
					{
						doONCE(am, day, month, year, hour, min, sec);
					} else if(am.getRythmus() == Rythmus.ONCE_A_DAY)
					{
						doONCE_A_DAY(am, hour, min, sec);
					} else if(am.getRythmus() == Rythmus.ON_TIMES)
					{
						doON_TIMES(am, hour, min, sec);
					} else if(am.getRythmus() == Rythmus.MULTIPLE_ON_THE_DAY)
					{
						doMULTIPLE_ON_THE_DAY(am, day, month, year, hour, min, sec);
					} else if(am.getRythmus() == Rythmus.INTERVAL)
					{
						doINTERVAL(am, day, month, year, hour, min, sec);
					}
				}
			}
		}.runTaskTimer(plugin, 0L, 1*20L);
	}
	
	private void doONCE(AutoMessage am, int day, int month, int year, int hour, int min, int sec)
	{
		if(am.getDate() == null || am.getTime() == null)
		{
			return;
		}
		if(am.getDate().getYear() == year &&
				am.getDate().getMonthValue() == month &&
				am.getDate().getDayOfMonth() == day &&
				am.getTime().getHour() == hour &&
				am.getTime().getMinute() == min &&
				am.getTime().getSecond() == sec)
		{
			AEX.logger.info(am.getPathName()+": ONCE at "
					+day+"."+month+"."+year+" "+hour+":"+min+":"+sec);
			if(am.isRandom())
			{
				sendToPlayersRandom(am);
			} else
			{
				sendToPlayers(am);
			}
		}
	}
	
	private void doONCE_A_DAY(AutoMessage am, int hour, int min, int sec)
	{
		if(am.getTime() == null)
		{
			return;
		}
		if(am.getTime().getHour() == hour &&
				am.getTime().getMinute() == min &&
				am.getTime().getSecond() == sec)
		{
			AEX.logger.info(am.getPathName()+": ONCE_A_DAY at "
					+hour+":"+min+":"+sec);
			if(am.isRandom())
			{
				sendToPlayersRandom(am);
			} else
			{
				sendToPlayers(am);
			}
		}
	}
	
	private void doON_TIMES(AutoMessage am, int hour, int min, int sec)
	{
		if(am.getTimeList() == null)
		{
			return;
		}
		Boolean check = false;
		for(LocalTime times : am.getTimeList())
		{
			if(times.getHour() == hour &&
					times.getMinute() == min &&
					times.getSecond() == sec)
			{
				check = true;
				break;
			}
		}
		if(check == true)
		{
			AEX.logger.info(am.getPathName()+": ON_TIMES at "
					+hour+":"+min+":"+sec);
			if(am.isRandom())
			{
				sendToPlayersRandom(am);
			} else
			{
				sendToPlayers(am);
			}
		}
	}
	
	private void doMULTIPLE_ON_THE_DAY(AutoMessage am, int day, int month, int year, int hour, int min, int sec)
	{
		long now = System.currentTimeMillis();
		if(am == null)
		{
			return;
		}
		if(am.getDate() == null)
		{
			return;
		}
		if(am.getDate().getYear() == year &&
				am.getDate().getMonthValue() == month &&
				am.getDate().getDayOfMonth() == day &&
				now >= am.getLastTimeSend())
		{
			AEX.logger.info(am.getPathName()+": MULTIPLE_ON_THE_DAY at "
					+day+"."+month+"."+year+" "+hour+":"+min+":"+sec);
			if(am.isRandom())
			{
				
				sendToPlayersRandom(am);
				am.setLastTimeSend(now+Utility.timeToLong(am.getTime()));
			} else
			{
				sendToPlayers(am);
				am.setLastTimeSend(now+Utility.timeToLong(am.getTime()));
			}
		}
	}
	
	private void doINTERVAL(AutoMessage am, int day, int month, int year, int hour, int min, int sec)
	{
		long now = System.currentTimeMillis();
		if(am == null)
		{
			return;
		}
		if(now >= am.getLastTimeSend())
		{
			AEX.logger.info(am.getPathName()+": INTERVAL at "
					+day+"."+month+"."+year+" "+hour+":"+min+":"+sec);
			if(am.isRandom())
			{
				sendToPlayersRandom(am);
				am.setLastTimeSend(now+am.getInterval());
			} else
			{
				sendToPlayers(am);
				am.setLastTimeSend(now+am.getInterval());
			}
		}
	}
	
	public void sendToPlayers(AutoMessage am)
	{
		if(am.getMessage() == null)
		{
			return;
		}
		for(Player all : plugin.getServer().getOnlinePlayers())
		{
			if(am.getPermission() != null)
			{
				sendTitle(true, am, all);
				playSound(true, am, all);
				if(all.hasPermission(am.getPermission()))
				{
					for(String tc : am.getMessage())
					{
						all.sendMessage(ChatApi.tl(tc));
					}
					playerCommandWithPermission(am, all);
				}
			} else
			{
				sendTitle(false, am, all);
				playSound(false, am, all);
				for(String tc : am.getMessage())
				{
					all.sendMessage(ChatApi.tl(tc));
				}
				playerCommand(am, all);
			}
		}
		consoleCommand(am);
	}
	
	public void sendToPlayersRandom(AutoMessage am)
	{
		if(am.getRandomlist() == null)
		{
			return;
		}
		int size = am.getRandomlist().size()-1;
		Random r = new Random();
		int value = r.nextInt(size);
		for(Player all : plugin.getServer().getOnlinePlayers())
		{
			if(am.getPermission() != null)
			{
				if(all.hasPermission(am.getPermission()))
				{
					sendTitle(true, am, all);
					playSound(true, am, all);
					List<String> list = am.getRandomlist().get(value);
					for(String tc : list)
					{
						all.sendMessage(ChatApi.tl(tc));											
					}
					playerCommandWithPermission(am, all);
				}
			} else
			{
				sendTitle(false, am, all);
				playSound(false, am, all);
				List<String> list = am.getRandomlist().get(value);
				for(String tc : list)
				{
					all.sendMessage(ChatApi.tl(tc));											
				}
				playerCommand(am, all);
			}
		}
		consoleCommand(am);
	}
	
	private void playSound(boolean permission, AutoMessage am, Player player)
	{
		if(permission)
		{
			if(am.getSoundsWithPermission().isEmpty())
			{
				return;
			}
			for(Sound sound : am.getSoundsWithPermission())
			{
				player.playSound(player.getLocation(), sound, 0.6f, 0.6f);
			}
		} else
		{
			if(am.getSounds().isEmpty())
			{
				return;
			}
			for(Sound sound : am.getSounds())
			{
				player.playSound(player.getLocation(), sound, 0.6f, 0.6f);
			}
		}
		return;
	}
	
	private void consoleCommand(AutoMessage am)
	{
		for(String cmd : am.getConsoleCommand())
		{
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
		}
		return;
	}
	
	private void playerCommand(AutoMessage am, Player player)
	{
		if(am.isDoPlayerCommandWithPermission() == false)
		{
			for(String cmd : am.getPlayerCommand())
			{
				Bukkit.dispatchCommand(player, cmd);
			}
		}
	}
	
	private void playerCommandWithPermission(AutoMessage am, Player player)
	{
		if(am.isDoPlayerCommandWithPermission() == true)
		{
			for(String cmd : am.getPlayerCommand())
			{
				Bukkit.dispatchCommand(player, cmd);
			}
		}
	}
	
	private void sendTitle(boolean permission, AutoMessage am, Player player)
	{
		if(permission)
		{
			if(am.getTitleWithPermission()!=null)
			{
				Titles t = am.getTitleWithPermission();
				Title.Times times = Title.Times.times(Duration.ofMillis(t.getFadeIn()*50L),
						Duration.ofMillis(t.getStay()*50L), Duration.ofMillis(t.getFadeOut()*50L));
				
				player.showTitle(Title.title(ChatApi.tl(t.getTitle()), ChatApi.tl(t.getSubTitle()), times));
			}
		} else
		{
			if(am.getTitle() != null)
			{
				
				Titles t = am.getTitle();
				Title.Times times = Title.Times.times(Duration.ofMillis(t.getFadeIn()*50L),
						Duration.ofMillis(t.getStay()*50L), Duration.ofMillis(t.getFadeOut()*50L));
				
				player.showTitle(Title.title(ChatApi.tl(t.getTitle()), ChatApi.tl(t.getSubTitle()), times));
			}
		}
	}
}