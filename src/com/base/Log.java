package com.base;
/////////////////////////////////////////////////////////////////////////////
//                Copyright(c)  1999  
//
//  FILE:           Log.java
//
//  AUTHOR:          David Keeney
//
//  DATE:           July 25, 1999
//
//  DESCRIPTION:    This defines the Log class that handles the logging and debug.
//                  
/////////////////////////////////////////////////////////////////////////////
//
//   DEBUG LEVELS:
//      MINIMUM =  1;   -  
//		STATS   =  3;   -  Show minor statistics
//		SUMMARY = 20;	-  Show a summary of what is happening
//      TRACE   = 30;   -  Also show full trace
//
/////////////////////////////////////////////////////////////////////////////
//  HISTORY:
//
//$Archive:: Log.java                    					$
//$Date:: 5/06/99 7:49p                                     $
//$Revision:: 0                                             $
//$Author:: Dek                                             $
//$History:: Log.java                                       $
//


import java.util.*;
import java.io.*;

import com.mains.Truline;

/**
*   Handles all processing of log file entires.
*
*   @author David Keeney, 07/25/1999
*/
public class Log
{
    // Standard Debug levels
    public static final int MINIMUM  =  1;   // minor stuff
    public static final int STATS    =  3;   // Display statistics
    public static final int SUMMARY  = 20;   // show summary of actions
    public static final int TRACE    = 30;   // Also show full trace
    public static final int PARSE    = 40;   // Show full Parse
    public static final int PARSE1   = 41;	 // Show DR1 file parse only
    public static final int PARSE2   = 42;	 // Show DR2 file parse only
    public static final int PARSE3   = 43;	 // Show DR3 file parse only
    public static final int PARSES   = 44;	 // Show SCR file parse only
    
    private static int m_debug = 0;
    public static Log m_log = new Log();
    
    // Log object member variables
    private String m_filename = null;
    private boolean m_timestamped = false;
    private boolean m_errorlogged = false;
    private boolean m_toscreen = true;
    private PrintWriter m_fp = null;





    /**
    *   Constructor.
    */
    public Log()
    {
    }
    
    /**
    *   Initialize the logging facility
    */
    public static void init(Properties userProps)
    {
        String string;
        m_debug = Lib.atoi(userProps.getProperty("DEBUG", "0"));
        string = userProps.getProperty("LOGFILE");
        if (!string.equals(m_log.m_filename))
        {
            if (m_log.m_fp != null)
                m_log.m_fp.close();
            m_log.m_fp = null;
            m_log.m_filename = string;
            File f = new File(m_log.m_filename);
            if (f.exists())
                f.delete();
        }
        string = userProps.getProperty("TOSCREEN","N");
        m_log.m_toscreen = string.startsWith("Y");
    }

    /**
    *   Make an entry in the log
    *
    *   NOTE: This does not check the debug level.  This must be done
    *         prior to calling this routine.  That way we avoid the
    *         overhead of building the text string when debug is not
    *         enabled at that level.
    *
    *   @see isDebug
    *   @param text   Text to be placed in the log file.
    *
    */
	public static void println(String text)
	{
		print(text+"\n");
	}
    public static void print(String text) 
    {
        if (m_log.m_toscreen)
            System.err.print(text);
        m_log.flatWrite(text);
    }
    public static void errPrint(String text) 
    {
        Truline.println(text);
        m_log.flatWrite(text);
    }
    
    
    /**
    *   Put an entry in a flat log file.  It can contain \n characters
    *   but does not need to be terminated with a \n character.
    *   The text will be appended to the file as-is.
    *
    *   @param text   Text to be placed in the log file.
    */
    private void flatWrite(String line) 
    {
        if (m_filename == null)
            return;
            
        if (m_fp == null)
        {
            try
            {
                m_fp = new PrintWriter(new FileOutputStream(m_filename, true));
            }
            catch(IOException e)
            {
                if (!m_errorlogged)
                    Truline.println("cannot open to logfile "+m_filename+"\n"+e);
                m_errorlogged = true;
                return;
            }
        }
        if (m_timestamped)
        {
            Date d = new Date();
            m_fp.print(d.toString().substring(4,19));
        }
        int i;
        int j = 0;
        while(j < line.length())
        {
            i = line.indexOf('\n', j);
            if (i == -1)
            {
                m_fp.print(line.substring(j));
                break;
            }
            m_fp.println(line.substring(j,i));
            j = i+1;
        }
        m_fp.flush();
        //fp.close();
    }       
    
    public static void close()
    {
        if (m_log.m_fp != null)
        {
            m_log.m_fp.close();
        }
        m_log.m_fp = null;
    }
    
    /**
    *   Tests to see if the specified level has been enabled
    *   for debug.  This is used to determine if a debug
    *   print should be called.
    *
    *   This can be implemented as a bitmask or as a level
    *   (or even some combination).  That is the reason the
    *   m_debug variable is private.  You must call this
    *   routine to tell if debug applies.
    *
    *   @param level  The level you want to test for.
    *   @return true if debugging is active at this level.
    */
    public static boolean isDebug(int level) 
    {
        if (level == PARSE1 || level == PARSE2 || level == PARSE3)
            return (m_debug == level || m_debug == PARSE);
        return (m_debug >= level);
    }

    /**
    *   The assert function will test the condition and
    *   if false it will generate an exception.  Since
    *   an ASSERT should be used to verify that an assumption
    *   in the logic is true, an exception generated from
    *   this type of operation should NOT be cought.  It
    *   should mean that there is a programming error and
    *   that it should crash the program.  Therefore we are
    *   using an unchecked exception RuntimeException rather 
    *   than Exception.
    *
    *   @param cond The condition to be tested.
    *   @param errText The text to be passed in the exception.
    */
    /* public static void assert(boolean cond, String errText) 
    {
        if (!cond) 
        {
            throw new RuntimeException(errText);
        }
    }   */

}
     
