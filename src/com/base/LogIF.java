package com.base;
/////////////////////////////////////////////////////////////////////////////
//
//  FILE:           LogIF.java
//
//  AUTHOR:          David Keeney
//
//  DATE:           March 15 2000
//
//  DESCRIPTION:    This defines the interface to the Logging facilities.
//                  
//
/////////////////////////////////////////////////////////////////////////////

import java.util.*;
import java.io.*;

/**
*   Handles all processing of debug logic.
*
*   @author David Keeney, 03/15/2000
*/
public interface LogIF
{
    // Standard Debug settings
    public static final int MINIMUM =    0x0001;   // Echo to screen
	public static final int STATS   =    0x0002;   // Show load statistics periodically
    public static final int SEIZURE =    0x0004;   // Show incoming connections
    public static final int SWITCH  =    0x0008;   // Show Switch actions
    public static final int TRACE   =    0x0010;   // Show traffic trace (and MIS events)
	public static final int ROUTEPLAN =  0x0020;   // Show Route plan execution
    public static final int CONNECTION = 0x0040;   // Show all connection polling and keepalives
    public static final int SERVLET  =   0x0080;   // Show servlet processing activity
	public static final int PROTOCOL =   0x0100;   // Show all chat protocol info
	public static final int MSGQUE   =   0x0200;   // Show local message queues (insert, remove)
	public static final int SCRIPT   =   0x0400;   // Show script processing
    public static final int CMCMSG 	 =   0x0800;   // Show all messages exchanged with CMC email server
	public static final int SENDEMAIL=   0x1000;   // Show steps in sending email within netvu
	public static final int MISEVENTS=   0x2000;   // Show MIS event stream
	public static final int DBTRACE  =   0x4000;   // Show Database activity
								// Everything on is 8191   
								// without CONNECTION or MIS is 4031
								// without CONNECTION or PROTOCOL or MIS 3775
    
    
    
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
    public boolean isDebug(int level);

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
    /* public void assert(boolean cond, String errText); */
	
	/**
	*	The method to call to output debug statements.
	*	It is assumed that this function will be overridden
	*	so that it goes into the application's debug log.
	*
	*	In the main application these are overloaded with methods
	*	that send the output to the appropreate log file.
	*
	*	NOTE: this does not check the debug level.
	*	@param text  The text to be output.
	*/
	public void println(String text);
	public void print(String text);



}

