package main.java.me.avankziar.autoex.general;

public enum Rythmus
{
	ONCE,
	ONCE_A_DAY,
	
	//new ones
	ONCE_ONLY, //SendingDate && SendingTimes
	//ONCE_A_DAY, //SendingTimes
	ONCE_A_WEEK, //DayOfWeek && SendingTimes
	ONCE_A_MONTH, //DayOfMonth && SendingTimes
	ONCE_A_YEAR, //DayOfMonth && MonthNumber && SendingTimes

	
	MULTIPLE_ON_THE_DAY,
	
	ON_TIMES,
	
	//new ones
	ON_TIMES_PER_DAY, //Pro Tag, an x Zeitpunkten
	ON_TIMES_PER_WEEK, //Pro Woche, an WochenTag y an x Zeitpunkten
	ON_TIMES_PER_MONTH, //Pro Monat, an Monatstag y an x Zeitpunkten
	ON_TIMES_PER_YEAR, //Pro Jahr, an Monatstag y, Monat z an x Zeitpunkten.

	INTERVAL,
	
	//new ones
	INTERVAL_ONLY;
}
