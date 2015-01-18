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
 * Handles all processing of output SCR file entires.
 * 
 * @author Kim Jones, 02/14/2012
 */
public class ScratchFile
{
 // Standard Debug levels
 public static ScratchFile m_scr      = new ScratchFile();
 // Log object member variables
 public String            m_filename = null;
 private PrintWriter      m_fp       = null;
 /**
  * Constructor.
  */
 public ScratchFile() {
 }
 /**
  * Initialize the scratch file creation
  */
 public static void init(String file)
 {
  m_scr.m_filename = file + ".xrd";
 }
 /**
  * Prepare for writing the scratch file
  */
 public static void init2()
 {

  File f = new File(m_scr.m_filename); if (f.exists()) f.delete();
  
 }
 /**
  * Add a new Scratch entry
  * 
  * NOTE: This does not check the debug level. This must be done prior to
  * calling this routine. That way we avoid the overhead of building the text
  * string when debug is not enabled at that level.
  * 
  * @see isDebug
  * @param text
  *         Text to be placed in the log file.
  * 
  */
 public static void println(String text)
 {
  print(text + "\n");
 }
 public static void print(String text)
 {
  m_scr.flatWrite(text);
 }
 /**
  * Put an entry in a flat log file. It can contain \n characters but does not
  * need to be terminated with a \n character. The text will be appended to the
  * file as-is.
  * 
  * @param text
  *         Text to be placed in the log file.
  */
 private void flatWrite(String line)
 {
  if (m_filename == null)
   return;
  if (m_fp == null) {
   try {
    m_fp = new PrintWriter(new FileOutputStream(m_filename, true));
   } catch (IOException e) {
    Truline.println("cannot open scratch file " + m_filename + "\n" + e);
    return;
   }
  }
  int i;
  int j = 0;
  while (j < line.length()) {
   i = line.indexOf('\n', j);
   if (i == -1) {
    m_fp.print(line.substring(j));
    break;
   }
   m_fp.println(line.substring(j, i));
   j = i + 1;
  }
  m_fp.flush();
  // fp.close();
 }
 public static void close()
 {
  if (m_scr.m_fp != null) {
   m_scr.m_fp.close();
  }
  m_scr.m_fp = null;
 }
 /**
  * Tests to see if the specified level has been enabled for debug. This is used
  * to determine if a debug print should be called.
  * 
  * This can be implemented as a bitmask or as a level (or even some
  * combination). That is the reason the m_debug variable is private. You must
  * call this routine to tell if debug applies.
  * 
  * @param level
  *         The level you want to test for.
  * @return true if debugging is active at this level.
  */
}
