package main.java.me.avankziar.aex.velocity.assistance;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import main.java.me.avankziar.aex.bungee.AEX;

public class Utility
{
	
	/*final public static String 
	PERMBYPASSCOLOR = "scc.channels.bypass.color",
	PERMBYPASSCOMMAND = "scc.channels.bypass.command";*/
	
	public Utility(AEX plugin)
	{
		
	}
	
	public static LocalDateTime deserialisedDateTime(String datetime)
	{
		LocalDateTime dt = LocalDateTime.parse((CharSequence) datetime,
				DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
		return dt;
	}
	
	public static String serialisedDateTime(LocalDateTime dt)
	{
		String MM = "";
		int month = 0;
		if(dt.getMonthValue()<10)
		{
			MM+=month;
		}
		MM += dt.getMonthValue();
		String dd = "";
		int day = 0;
		if(dt.getDayOfMonth()<10)
		{
			dd+=day;
		}
		dd +=dt.getDayOfMonth();
		String hh = "";
		int hour = 0;
		if(dt.getHour()<10)
		{
			hh+=hour;
		}
		hh += dt.getHour();
		String mm = "";
		int min = 0;
		if(dt.getMinute()<10)
		{
			mm+=min;
		}
		mm += dt.getMinute();
		String ss = "";
		int sec = 0;
		if(dt.getSecond()<10)
		{
			ss+=sec;
		}
		ss += dt.getSecond();
		return dd+"."+MM+"."+dt.getYear()+" "+hh+":"+mm+":"+ss;
	}
	
	public static LocalDate deserialisedDate(String date)
	{
		LocalDate d = LocalDate.parse((CharSequence) date,
				DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		return d;
	}
	
	public static String serialisedDate(LocalDate d)
	{
		String mm = "";
		int month = 0;
		if(d.getMonthValue()<10)
		{
			mm+=month;
		}
		mm += d.getMonthValue();
		String dd = "";
		int day = 0;
		if(d.getDayOfMonth()<10)
		{
			dd+=day;
		}
		dd +=d.getDayOfMonth();
		return dd+"."+mm+"."+d.getYear();
	}
	
	public static LocalTime deserialisedTime(String date)
	{
		LocalTime d = LocalTime.parse((CharSequence) date,
				DateTimeFormatter.ofPattern("HH:mm:ss"));
		return d;
	}
	
	public static String serialiseTime(LocalTime d)
	{
		String hh = "";
		int hour = 0;
		if(d.getHour()<10)
		{
			hh+=hour;
		}
		hh += d.getHour();
		String mm = "";
		int min = 0;
		if(d.getMinute()<10)
		{
			mm+=min;
		}
		mm +=d.getMinute();
		String ss = "";
		int sec = 0;
		if(d.getSecond()<10)
		{
			ss+=sec;
		}
		ss +=d.getSecond();
		return hh+":"+mm+":"+ss;
	}
	
	public static long timeToLong(LocalTime t)
	{
		long x = 1000L;
		long r = t.getHour()*60*60*x+t.getMinute()*60*x+t.getSecond()*x;
		return r;
	}
}
