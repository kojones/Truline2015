package com.base;
/**
 *	Library methods
 */
import java.util.*;
import java.text.*;

class Lib
{
	/**
	 *   String to integer conversion routine.
	 *   The regular Integer.parseInt() function will not 
	 *   convert fields that start with white space or that
	 *   are followed by non-numeric characters.  This routine
	 *   trys to emulate the actions of the C atoi() routine.
	 *
	 *   Conversion starts at the first non-space character
	 *   and ends with the first non-digit after that.
	 *	If the next non-digit character is a 'k' then multiply
	 *	the value by 1000.
	 *
	 *   @argument string A string containing numeric digits
	 *   @return   The converted integer.  0 if string is not numeric.
	 */
	public static int atoi(String string) 
	{
		int value = 0;
		if (string == null || string.length() == 0)
			return 0;
		string = string.trim();
		
		char buf[] = string.toCharArray();
		boolean neg = false;
		int i,j;
		
		i = 0;
		if (buf[i] == '-')
		{
			neg = true;
			i++;
		}
		for (;i < buf.length; i++) 
		{
			j = Character.digit(buf[i], 10);
			if (j == -1) 
			{
				if (buf[i] == 'k' || buf[i] == 'K')
					value *=1000;
				break;
			}
			value = value*10 + j;
		}
		// Log.print("atoi string="+string+"ret="+((neg)?(-value):value));
		return ((neg)?(-value):value);
	}
	
	/**
	 *	Parse a floating point value.
	 */
	public static double atof(String string) 
	{
		double f = 0;
		if (string != null)
		{
			try
			{
				string.trim();
				f = Double.parseDouble(string);
			}
			catch(Exception e)
			{
				f = 0;
			}
		}
		return f;
	}
	
	/**
	 *	Parse a date of the form YYYYMMDD
	 */
	public static Date atoDate(String value)
	{
		Date date = null;
		if (value != null)
		{
			String string = value.substring(4,6)  // Month
				+"/"+value.substring(6,8)   // Day
				+"/"+value.substring(0,4);  // Year (4 digit)
			try
			{
				//  MM/DD/YYYY format
				DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
				date = df.parse(string);
			}
			catch(Exception e)
			{
				if (Log.isDebug(Log.PARSE))
					Log.print("Error parsing date "+string+" - "+e+"\n");
			}
		}
		return date;
	}
	
	/**
	 *	Format a date for display  MM/DD/YYYY
	 */
	public static String datetoa(Date date)
	{
		if (date == null)
			return "null";
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
		String x =  df.format(date);
		int i1 = x.indexOf('/');
		int i2 = x.indexOf('/',i1+1);
		String m = "00"+x.substring(0,i1);
		m = m.substring(m.length()-2, m.length());
		String d = "00"+x.substring(i1+1,i2);
		d = d.substring(d.length()-2);
		return m+"/"+d+"/"+x.substring(i2+1);
	}
	
	/**
	 *	Compute the difference between two dates in days.
	 *  NOTE:  This is not very accurate for long periods of time
	 *         but should work for periods within a year or so.
	 *	@param date1, date2 - The dates to compare
	 *	@returns An integer number of days.
	 */
	public static int dateDiff(Date date1, Date date2)
	{
		if (date1 == null || date2 == null)
			return 0;
		// add half day to round to nearest day.
		long days1 = (date1.getTime()+43200)/86400000;
		long days2 = (date2.getTime()+43200)/86400000;
		//Log.print("dateDiff "+date1+" and "+date2+" = "+(days2-days1)+"\n");
		return (int)(days2 - days1);
	}
	
	/**
	 *	Round number to given number of decimal digits.
	 */
	public static String ftoa(double nbr, int digits)
	{
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(digits);
		nf.setMinimumFractionDigits(digits);
		nf.setGroupingUsed(false);
		return nf.format(nbr).trim();
	}
	public static String pad(String str, int len)
	{
		return (str+"                ").substring(0,len);
	}
	public static String pad(double r, int len)
	{
		String str = ftoa(r,0);
		return (str+"                ").substring(0,len);
	}
	public static String rjust(String str, int len)
	{
		str = "                   "+str;
		return str.substring(str.length()-len,str.length());
	}
	public static String rjust(double r, int len)
	{
		return rjust(r,len,0);
	}
	public static String rjust(double r, int len, int digits)
	{
		String str = ftoa(r,digits);
		return rjust(str, len);
	}
	public static String rjust5th(double r, int len)
	{
		double b = Math.floor(r);
		r = b + (r - b)/2;
		return rjust(r,len,1);		
	}
	
}

