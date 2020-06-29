package main.java.me.avankziar.automaticexecute.bungee.object;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;

public class AutoMessage
{
	private String pathName;
	private Rythmus rythmus;
	private String permission;
	private List<TextComponent> message;
	private boolean doPlayerCommandWithPermission;
	private List<String> consoleCommand;
	private List<String> playerCommand;
	private boolean random;
	private List<List<TextComponent>> randomlist;
	private Title title;
	private Title titleWithPermission;
	
	private LocalDate date; //For Once and OnceADay
	private LocalTime time; //For Once and OnceADay
	private List<LocalTime> timeList; //ON_TIME
	
	private long interval; //For interval
	private long lastTimeSend;

	public AutoMessage(String pathName, Rythmus rythmus, String permission, 
			boolean random, List<List<TextComponent>> randomlist, List<TextComponent> message,
			Title title, Title titleWithPermission,
			List<String> consoleCommand,
			boolean doPlayerCommandWithPermission, List<String> playerCommand,
			LocalDate date, LocalTime time, List<LocalTime> timeList, long interval, long lastTimeSend)
	{
		setPathName(pathName);
		setRythmus(rythmus);
		setPermission(permission);
		setMessage(message);
		setTitle(title);
		setTitleWithPermission(titleWithPermission);
		setDoPlayerCommandWithPermission(doPlayerCommandWithPermission);
		setConsoleCommand(consoleCommand);
		setPlayerCommand(playerCommand);
		setDate(date);
		setTime(time);
		setTimeList(timeList);
		setInterval(interval);
		setLastTimeSend(lastTimeSend);
	}
	
	public String getPathName()
	{
		return pathName;
	}

	public void setPathName(String pathName)
	{
		this.pathName = pathName;
	}

	public Rythmus getRythmus()
	{
		return rythmus;
	}

	public void setRythmus(Rythmus rythmus)
	{
		this.rythmus = rythmus;
	}

	public String getPermission()
	{
		return permission;
	}

	public void setPermission(String permission)
	{
		this.permission = permission;
	}

	public List<TextComponent> getMessage()
	{
		return message;
	}

	public void setMessage(List<TextComponent> message)
	{
		this.message = message;
	}

	public boolean isDoPlayerCommandWithPermission()
	{
		return doPlayerCommandWithPermission;
	}

	public void setDoPlayerCommandWithPermission(boolean doPlayerCommandWithPermission)
	{
		this.doPlayerCommandWithPermission = doPlayerCommandWithPermission;
	}

	public List<String> getConsoleCommand()
	{
		return consoleCommand;
	}

	public void setConsoleCommand(List<String> consoleCommand)
	{
		this.consoleCommand = consoleCommand;
	}

	public List<String> getPlayerCommand()
	{
		return playerCommand;
	}

	public void setPlayerCommand(List<String> playerCommand)
	{
		this.playerCommand = playerCommand;
	}

	public boolean isRandom()
	{
		return random;
	}

	public void setRandom(boolean random)
	{
		this.random = random;
	}

	public List<List<TextComponent>> getRandomlist()
	{
		return randomlist;
	}

	public void setRandomlist(List<List<TextComponent>> randomlist)
	{
		this.randomlist = randomlist;
	}

	public LocalDate getDate()
	{
		return date;
	}

	public void setDate(LocalDate date)
	{
		this.date = date;
	}

	public LocalTime getTime()
	{
		return time;
	}

	public void setTime(LocalTime time)
	{
		this.time = time;
	}

	public long getInterval()
	{
		return interval;
	}

	public void setInterval(long interval)
	{
		this.interval = interval;
	}

	public long getLastTimeSend()
	{
		return lastTimeSend;
	}

	public void setLastTimeSend(long lastTimeSend)
	{
		this.lastTimeSend = lastTimeSend;
	}

	public List<LocalTime> getTimeList()
	{
		return timeList;
	}

	public void setTimeList(List<LocalTime> timeList)
	{
		this.timeList = timeList;
	}

	public Title getTitle()
	{
		return title;
	}

	public void setTitle(Title title)
	{
		this.title = title;
	}

	public Title getTitleWithPermission()
	{
		return titleWithPermission;
	}

	public void setTitleWithPermission(Title titleWithPermission)
	{
		this.titleWithPermission = titleWithPermission;
	}

	public enum Rythmus
	{
		ONCE,
		ONCE_A_DAY,
		MULTIPLE_ON_THE_DAY,
		ON_TIMES,
		INTERVAL;
	}
}
