package main.java.me.avankziar.aex.velocity.assistance;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.velocitypowered.api.proxy.Player;

import dev.dejvokep.boostedyaml.YamlDocument;
import main.java.me.avankziar.aex.general.Rythmus;
import main.java.me.avankziar.aex.general.database.YamlHandler;
import main.java.me.avankziar.aex.velocity.AEX;
import main.java.me.avankziar.aex.velocity.object.AutoMessage;

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
		//String sepb = yh.getConfig().getString("Seperator.BetweenFunction", "~");
		for(String path : yh.getAutoEx().getRoutesAsStrings(false))
		{
			if(auto.getString(path+".Rythmus") == null) continue;
			plugin.getLogger().info("Loading Message: "+path);
			Rythmus rythmus = Rythmus.valueOf(auto.getString(path+".Rythmus", "ONCE"));
			String permission = null;
			if(auto.getString(path+".Permission", null) != null) permission = auto.getString(path+".Permission");
			LocalDate date = null;
			if(auto.getString(path+".SendingDate", null) != null)
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
			if(auto.getString(path+".SendingTime", null) != null)
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
			if(auto.get(path+".SendingTimes", null) != null)
			{
				for(String tl : auto.getStringList(path+".SendingTimes"))
				{
					timelist.add(LocalTime.parse((CharSequence)tl,
							DateTimeFormatter.ofPattern("~HH:mm:ss")));
				}
			}
			boolean isRandom = false;
			if(auto.get(path+".IsRandom", null) != null)
			{
				isRandom = auto.getBoolean(path+".IsRandom");
			}
			List<List<String>> RandomMessage = new ArrayList<List<String>>();
			if(isRandom)
			{
				for(String otherpath : auto.getStringList(path+".RandomMessage"))
				{
					if(auto.getString(otherpath, null) != null)
					{
						if(auto.getStringList(otherpath) != null)
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
			if(auto.get(path+".ConsoleCommand", null) != null)
			{
				consoleCommand = auto.getStringList(path+".ConsoleCommand");
			}
			if(auto.get(path+".DoPlayerCommand", null) != null)
			{
				if(auto.get(path+".PlayerCommand", null) != null)
				{
					playerCommand = auto.getStringList(path+".PlayerCommand");
				}
			}
			if(auto.get(path+".DoPlayerCommandWithPermission") != null)
			{
				doPlayerCommandWithPermission = auto.getBoolean(path+".DoPlayerCommandWithPermission", false);
				if(auto.get(path+".PlayerCommand", null) != null)
				{
					playerCommand = auto.getStringList(path+".PlayerCommand");
				}
			}
			/*Title title = null;
			if(auto.getString(path+".TitleMessage", null) != null)
			{
				String[] ti = auto.getString(path+".TitleMessage").split(sepb);
				if(ti.length == 5)
				{
					title = ProxyServer.getInstance().createTitle();
					title.title(ChatApi.tctl(ti[0]));
					title.subTitle(ChatApi.tctl(ti[1]));
					title.fadeIn(Integer.parseInt(ti[2]));
					title.stay(Integer.parseInt(ti[3]));
					title.fadeOut(Integer.parseInt(ti[4]));
				}
			}
			Title titleWithPermission = null;
			if(auto.getString(path+".TitleMessageWithPermission", null) != null)
			{
				String[] ti = auto.getString(path+".TitleMessageWithPermission").split(sepb);
				if(ti.length == 5)
				{
					titleWithPermission = ProxyServer.getInstance().createTitle();
					titleWithPermission.title(ChatApi.tctl(ti[0]));
					titleWithPermission.subTitle(ChatApi.tctl(ti[1]));
					titleWithPermission.fadeIn(Integer.parseInt(ti[2]));
					titleWithPermission.stay(Integer.parseInt(ti[3]));
					titleWithPermission.fadeOut(Integer.parseInt(ti[4]));
				}
			}*/
			AutoMessage am = new AutoMessage(path, rythmus, permission,
					isRandom, RandomMessage, Message, //title, titleWithPermission,
					consoleCommand, doPlayerCommandWithPermission, playerCommand,
					date, time, timelist, interval, lasttimesend);
			AutoMessageList.add(am);
		}
	}
	
	public void runTask()
	{
		plugin.getServer().getScheduler().buildTask(plugin, (task) ->
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
				task.cancel();
			}
		}).delay(1L, TimeUnit.SECONDS).repeat(5L, TimeUnit.SECONDS).schedule();
	}
	
	public void autoSendMessage(LocalDateTime now)
	{
		plugin.getLogger().info("AutomaticExecute starts Scheduler at "+Utility.serialisedDateTime(now));
		plugin.getServer().getScheduler().buildTask(plugin, (task) ->
		{
			
			LocalTime lt = LocalTime.now();
			int min = lt.getMinute();
			int sec = lt.getSecond();
			int hour = lt.getHour();
			LocalDate ld = LocalDate.now();
			int day = ld.getDayOfMonth();
			int month = ld.getMonthValue();
			int year = ld.getYear();
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
		}).delay(0L, TimeUnit.SECONDS).repeat(1L, TimeUnit.SECONDS).schedule();
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
			plugin.getLogger().info(am.getPathName()+": ONCE at "
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
			plugin.getLogger().info(am.getPathName()+": ONCE_A_DAY at "
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
			plugin.getLogger().info(am.getPathName()+": ON_TIMES at "
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
			plugin.getLogger().info(am.getPathName()+": MULTIPLE_ON_THE_DAY at "
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
	
	public void doINTERVAL(AutoMessage am, int day, int month, int year, int hour, int min, int sec)
	{
		long now = System.currentTimeMillis();
		if(am == null)
		{
			return;
		}
		if(now >= am.getLastTimeSend())
		{
			plugin.getLogger().info(am.getPathName()+": INTERVAL at "
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
		for(Player all : plugin.getServer().getAllPlayers())
		{
			if(am.getPermission() != null)
			{
				if(all.hasPermission(am.getPermission()))
				{
					//sendTitle(true, am, all);
					for(String tc : am.getMessage())
					{
						all.sendMessage(ChatApi.tl(tc));
					}
					playerCommand(true, am, all);
				}
			} else
			{
				//sendTitle(false, am, all);
				for(String tc : am.getMessage())
				{
					all.sendMessage(ChatApi.tl(tc));
				}
				playerCommand(false, am, all);
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
		for(Player all : plugin.getServer().getAllPlayers())
		{
			if(am.getPermission() != null)
			{
				if(all.hasPermission(am.getPermission()))
				{
					//sendTitle(true, am, all);
					List<String> list = am.getRandomlist().get(value);
					for(String tc : list)
					{
						all.sendMessage(ChatApi.tl(tc));											
					}
					playerCommand(true, am, all);
				}
			} else
			{
				//sendTitle(false, am, all);
				List<String> list = am.getRandomlist().get(value);
				for(String tc : list)
				{
					all.sendMessage(ChatApi.tl(tc));											
				}
				playerCommand(false, am, all);
			}
		}
		consoleCommand(am);
	}
	
	private void consoleCommand(AutoMessage am)
	{
		for(String cmd : am.getConsoleCommand())
		{
			AEX.getPlugin().getServer().getCommandManager().executeImmediatelyAsync(plugin.getServer().getConsoleCommandSource(), cmd);
		}
		return;
	}
	
	private void playerCommand(boolean permission, AutoMessage am, Player player)
	{
		if(permission)
		{
			if(am.isDoPlayerCommandWithPermission() == true)
			{
				for(String cmd : am.getPlayerCommand())
				{
					AEX.getPlugin().getServer().getCommandManager().executeImmediatelyAsync(player, cmd);
				}
			}
		} else
		{
			if(am.isDoPlayerCommandWithPermission() == false)
			{
				for(String cmd : am.getPlayerCommand())
				{
					AEX.getPlugin().getServer().getCommandManager().executeImmediatelyAsync(player, cmd);
				}
			}
		}
	}
	
	/*private void sendTitle(boolean permission, AutoMessage am, Player player)
	{
		if(permission)
		{
			if(am.getTitleWithPermission()!=null)
			{
				player.sendTitle(am.getTitleWithPermission());
			}
		} else
		{
			if(am.getTitle() != null)
			{
				player.sendTitle(am.getTitle());
			}
		}
	}*/
}