package com.mains;
/**
 *	Truline2014
 *
 *	Written by David Keeney and upgraded by Kim Jones with lots of input from 
 *	Jim and Trillis Selvidge.
 *
 * V 1.0  01/15/14 jones
 *     Initial version of Truline 2014
 * V 1.1  02/23/12 jones
 *     Initial version containing Scratch option in File menu.
 *
 *	Spinning Electrons, LLC
 *	201 W Stassney Lane  #522
 *	Austin, TX  78745
 *	kim.jones@spinningelectrons.com
 *	(415) 935-4546
 */
/**
 *	Truline2000
 *
 *	Written by David Keeney with lots of input from 
 *	Jim and Trillis Selvidge and Michael Gray.
 *
 *	V 2.1.0  01/13/01 keeney
 *			 Added the printing using Wordpad.
 *	V 2.0.4  03/05/00 keeney
 *			 Corrected spelling. Fixed gender display.
 *	V 2.0.3  03/04/00 keeney
 *			 do not allow reports unless all three BIRS
 *			 files exist.  Added install program.
 *	V 2.0-2  02/28/00 keeney
 *			 Added configuation and printer Setup.
 *	V 2.0-1  02/22/00 keeney 
 *			 Initial version containing the GUI.
 *
 *	Keeney Software Inc.
 *	24 Tuckaway Shores Rd
 *	Nottingham NH 03290
 *	keeney@scguild.com
 *	(603)895-9975
 */

import java.awt.Frame;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.Properties;

import com.base.Bris;
import com.base.BrisMCP;
import com.base.BrisJCP;
import com.base.BrisXRD;
import com.base.CommaDelimited;
import com.base.DatabaseReport;
import com.base.GUI;
import com.base.Handicap;
import com.base.HtmlFolder;
import com.base.HtmlReport;
import com.base.Log;
import com.base.Race;
import com.base.ResultsOnly;
import com.base.RichTextReport;
import com.base.SQLDefinition;
import com.base.SQLReport;
import com.base.ScratchFile;
import com.base.TextPrint;
import com.base.TextReport;

public class Truline
{
 public static String         title        = "Truline 2015";
 public static String         version      = "Version 2.1 5/1/2021";
 public static String         copyright    = "Copyright(c) 2001,2021 Spinning Electrons, LLC";
 public static String         m_handicapVersion = "hf2015v1";
 public static String         m_trulineVersion  = "2.1.0";
 public static Properties     userProps    = new Properties();
 public static CommaDelimited co           = new CommaDelimited();                            // Correlation
 public static CommaDelimited pc           = new CommaDelimited();                            // track
 public static CommaDelimited ex           = new CommaDelimited();                            // exclude
 public static CommaDelimited pt           = new CommaDelimited();                            // power
 public static CommaDelimited ts           = new CommaDelimited();                            // turf sires
 public static CommaDelimited bf           = new CommaDelimited();                            // track strengths
 public static CommaDelimited jf           = new CommaDelimited();                            // jockey strengths
 public static CommaDelimited jm           = new CommaDelimited();                            // jockey meet stats
 public static CommaDelimited tf           = new CommaDelimited();                            // trainer strengths
 public static CommaDelimited tm           = new CommaDelimited();                            // trainer meet stats
 public static CommaDelimited rf           = new CommaDelimited();                            // exotic bets
 public static CommaDelimited fn           = new CommaDelimited();                            // input file names
 public static CommaDelimited tt           = new CommaDelimited();                            // truline trainer stats
 public static CommaDelimited jt           = new CommaDelimited();                            // truline jockey stats
 public static CommaDelimited tj           = new CommaDelimited();                            // trainer jockey stats
 public static CommaDelimited t2           = new CommaDelimited();                            // trainer jockey track stats
 public static CommaDelimited t3           = new CommaDelimited();                            // trainer surface stats
 public static CommaDelimited t4           = new CommaDelimited();                            // trainer jockey strengths
 public static CommaDelimited t5           = new CommaDelimited();                            // trainer jockey last 25
 public static CommaDelimited to           = new CommaDelimited();                            // trainer owner stats
 public static CommaDelimited st           = new CommaDelimited();                            // sire stats
 public static CommaDelimited dt           = new CommaDelimited();                            // dam stats
 public static CommaDelimited ds           = new CommaDelimited();                            // dam sire stats
 public static CommaDelimited hf           = new CommaDelimited();                            // handicapping factors
 public static CommaDelimited rs           = new CommaDelimited();                            // run style profile
 public static CommaDelimited sc           = new CommaDelimited();                            // scratches
                                                                                               
 public static final int      TEXTMODE     = 1;
 public static final int      HTMLMODE     = 2;
 public static final int      FOLDERMODE   = 3;
 public static final int      GUIMODE      = 4;
 public static final int      SQLMODE      = 5;
 public static final int      UPDMODE      = 6;
 public static final int      SQLDEFMODE   = 7;
 public static final int      PRINTMODE    = 8;
 public static final int      RTFPRINTMODE = 9;
 public static final int      DATABASE1    = 10;
 public static final int      DATABASE2    = 11;
 public static final int      DATABASE3    = 12;
 public static final int      RESULTS      = 13;
 public static final int      FLOWBETS     = 14;
 public static int            mode         = 0;
 public static GUI            gui          = null;
 public static String         dir         = null;
 public static String         file         = null;
 public static String         fileL             = null;
 private static String        filename          = null;
 public static String         databaseName      = null;
 private static Boolean       loadDatabase      = false;
 // 
 public static boolean servletCalled = false;
 /* ------------------------------------------------------------------------------ */
 public static void main(String[] argv)
 {
  Truline t = new Truline(); 
  t.doMain( argv );
 }
 /* ------------------------------------------------------------------------------ */
 public void doMain(String[] argv)
 {
  try {
   FileInputStream in = new FileInputStream("c:/truline2015/truline.ini");
   // FileInputStream in = new FileInputStream("Truline.ini");
   userProps.load(in);
   in.close();
   Log.init(userProps);
   if (Log.isDebug(Log.MINIMUM))
    Log.errPrint(version + "\n");
   m_handicapVersion = Truline.userProps.getProperty("HandicapVersion",
     "default");
   dir = Truline.userProps.getProperty("HomeDir","");
   ex.load(dir + "Truline.ex"); // exclude list
   co.load(dir + "Truline.co"); // Correlation Table
   // rc.load(dir + "Truline.rc"); // purse class
   // tc.load(dir + "Truline.tc"); // track class
   pc.load(dir + "Truline.pc"); // track purse class
   pt.load(dir + "Truline.pt"); // power trainers
   ts.load(dir + "Truline.ts"); // turf sires
   if (Truline.userProps.getProperty("TL2014","N").equals("Yes"))
     {
     bf.load(dir + "Truline.bf"); // betting factors / flows
     jf.load(dir + "Truline.jf"); // jockey strengths
     tf.load(dir + "Truline.tf"); // trainer strengths
     rf.load(dir + "Truline.rf"); // race flow bets
     tt.load(dir + "Truline.tt"); // truline trainer stats
     jt.load(dir + "Truline.jt"); // truline jockey stats
     tj.load(dir + "Truline.tj"); // trainer jockey stats
     st.load(dir + "Truline.st"); // sire stats
     dt.load(dir + "Truline.dt"); // dam stats
     ds.load(dir + "Truline.ds"); // dam sire stats
     hf.load(dir + "Truline.hf"); // handicapping factor stats
     rs.load(dir + "Truline.rs"); // run style profile
         }
   if (Truline.userProps.getProperty("Experimental","No").equals("Yes"))
   {
    t2.load(dir + "Truline.t2"); // trainer jockey track stats
    to.load(dir + "Truline.to"); // trainer owner stats
    sc.load(dir + "Truline.sc"); // daily scratch file from HDW
    t3.load(dir + "Truline.t3"); // trainer surface 1-year stats
    tm.load(dir + "Truline.tm"); // trainer meet stats
    jm.load(dir + "Truline.jm"); // jockey meet stats
    if (Truline.userProps.getProperty("ArtAndKim", "N").equals("Y")) {
     t4.load(dir + "Truline.t4"); // trainer jockey strengths
     t5.load(dir + "Truline.t5"); // trainer jockey last 25
    }
   }
  } catch (Exception e) {
  }
  // Decode the runtime arguments.
  // -p - print text report on printer
  // -r - RTF print report file
  // -t - text report file
  // -w - html report file
  // -f - folder report file
  // -g - GUI format
  // -s - SQL insert report
  // -u - SQL update report
  // -c - insert all tables to database
  // -d - insert only new handicap values to database
  // -e - insert only new race, post, results and handicap to database
  // -j - insert only results into database
  // -h - help
  for (int i = 0; i < argv.length; i++) {
   if (argv[i].charAt(0) == '-') {
    switch (argv[i].charAt(1)) {
     case 'p':
      mode = PRINTMODE;
      break;
     case 'r':
      mode = RTFPRINTMODE;
      break;
     case 't':
      mode = TEXTMODE;
      break;
     case 'w':
      mode = HTMLMODE;
      break;
     case 'f':
      mode = FOLDERMODE;
      break;
     case 'g':
      mode = GUIMODE;
      break;
     case 's':
      mode = SQLMODE;
      break;
     case 'u':
      mode = UPDMODE;
      break;
     case 'x':
      mode = SQLDEFMODE;
      break;
     case 'c':
      mode = DATABASE1;
      break;
     case 'd':
      mode = DATABASE2;
      break;
     case 'e':
      mode = DATABASE3;
      break;
     case 'j':
      mode = RESULTS;
      break;
     case 'b':
      mode = FLOWBETS;
      break;
     default:
      usage();
    }
   } else {
    file = argv[i];
   }
  }
  if (mode == 0) {
   // No mode was specified, check the truline.ini file for a mode
   // specification.
   if (userProps.getProperty("TextReport", "N").equals("Y"))
    mode = TEXTMODE;
   else if (Truline.userProps.getProperty("HTMLReport", "N").equals("Y"))
    mode = HTMLMODE;
   else if (Truline.userProps.getProperty("HTMLFolder", "N").equals("Y"))
    mode = FOLDERMODE;
   else if (Truline.userProps.getProperty("SQLReport", "N").equals("Y"))
    mode = SQLMODE;
   else if (Truline.userProps.getProperty("SQLDefine", "N").equals("Y"))
    mode = SQLDEFMODE;
   else if (Truline.userProps.getProperty("PrintReport", "N").equals("Y"))
    mode = PRINTMODE;
   else
    mode = GUIMODE;
  }
  // ---------------------------------------------------------------------------------
  // PHR were we called from a servlet
  if (servletCalled) {
   mode = HTMLMODE;
  }
  String filename = null;
  if (mode == GUIMODE && (file == null || !file.substring(0, 4).equals("LOAD"))) {
   // / GUI mode //////////////////////
   gui = new GUI();
   gui.m_filename = file;
   Thread startthread = gui.startGetfile();
   gui.init();
   try {
    startthread.join();
    } catch (Exception e) {
   }
   file = gui.m_filename;
   String base = Truline.userProps.getProperty("DATADIR");
   if (base != null)
    file = base + file;
   ScratchFile.init(file);
   Log.print("Scratch File initialized - " + ScratchFile.m_scr.m_filename + "\n   ");
   gui.doReport();
   return;
  }
  // / batch modes ///////////////////////////
  // If we don't have a filename, ask for one.
  if (file == null) {
   gui = new GUI();
   file = gui.getFile();
   if (file == null)
    System.exit(0);
   gui.dispose();
   gui = null;
  }
  filename = fixupFile(file);
  // ScratchFile.init(file);
  String base = Truline.userProps.getProperty("DATADIR");
  databaseName = Truline.userProps.getProperty("Database");
  if (base != null)
   file = base + filename;
  fileL = file;
  Log.print("File to be processed - " + file + "\n   ");
  if (mode == SQLDEFMODE) {
   file = "truline.sql";
   SQLDefinition rpt7 = new SQLDefinition();
   rpt7.generate(file);
   System.exit(1);
  }
  // If database load, get next file to process
  if ((mode == DATABASE1 || mode == DATABASE2 || mode == DATABASE3 || mode == RESULTS)
    && filename.substring(0, 4).equals("LOAD")) {
   // Load list of files to be loaded
   loadDatabase = true;
   if (file.indexOf('\\') == -1) {
    // no path given, use the base address if known.
    if (base != null)
     file = base + file;
   }
   fn.load(file); // load list of files to be processed
   String fileLoad = fn.getNext("FILENAME");
   while (fileLoad != null) {
    if (base != null)
     fileL = base + fileLoad;
    doBatch(fileL, fileLoad);
    fileLoad = fn.getNext("FILENAME");
   }
  } else
   doBatch(fileL, filename);
  System.out.println("Complete\n");
  if (Log.isDebug(Log.TRACE))
   Log.print("Complete\n");
  Log.close();
  if (!servletCalled) {
   System.exit(1);
  }
 }
 public static void doBatch(String fileL, String fileLoad)
 {
  if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("MCP")) {
   BrisMCP brisMCP = new BrisMCP();
   if (brisMCP.load(fileL, fileLoad)) {
    if (Truline.userProps.getProperty("PostResults", "N").equals("Y")) {
     BrisXRD brisXRD = new BrisXRD();
     if (brisXRD.load(fileL, fileLoad, brisMCP.m_races))
      System.out.println("Results loaded.\n");
     else
      System.out.println("No results file found.\n");
    }
    if (mode == RESULTS)
     System.out.println("No Handicap - results only.\n");
    else {
     for (Enumeration e = brisMCP.m_races.elements(); e.hasMoreElements();) {
      Race race = (Race) e.nextElement();
      Handicap.compute(race);
     }
    }
   } else
    System.exit(0);
   switch (mode) {
    case TEXTMODE:
     TextReport rpt1 = new TextReport();
     rpt1.generate(fileLoad, brisMCP, false);
     break;
    case RTFPRINTMODE:
     RichTextReport rpt1a = new RichTextReport();
     rpt1a.generate(fileLoad, brisMCP, false);
     break;
    case FLOWBETS:
     RichTextReport rpt1b = new RichTextReport();
     rpt1b.generateHF(fileLoad, brisMCP, false);
     break;
    case HTMLMODE:
     HtmlReport rpt2 = new HtmlReport();
     String newFilename = "file:/C:/Truline2012/" + fileLoad;
     newFilename = fileLoad;
     newFilename = dir + fileLoad;
     System.out.println("Generating Report for " + newFilename + "\n");
     rpt2.generate(newFilename, brisMCP);
     break;
    case FOLDERMODE:
     HtmlFolder rpt3 = new HtmlFolder();
     // rpt3.generateMCP(fileLoad, brisMCP);
     break;
    case SQLMODE:
     SQLReport rpt4 = new SQLReport();
     // rpt4.generateMCP(fileLoad, brisMCP, SQLMODE);
     break;
    case UPDMODE:
     SQLReport rpt5 = new SQLReport();
     // rpt5.generateMCP(fileLoad, brisMCP, UPDMODE);
     break;
    case DATABASE1:
     DatabaseReport rpt7a = new DatabaseReport();
     rpt7a.generate(databaseName, brisMCP, DATABASE1);
     break;
    case DATABASE2:
     DatabaseReport rpt8a = new DatabaseReport();
     rpt8a.generate(databaseName, brisMCP, DATABASE2);
     break;
    case DATABASE3:
     DatabaseReport rpt9a = new DatabaseReport();
     rpt9a.generate(databaseName, brisMCP, DATABASE3);
     break;
    case RESULTS:
     ResultsOnly rptRa = new ResultsOnly();
     rptRa.generate(databaseName, brisMCP, RESULTS);
     break;
    case PRINTMODE:
     System.out.println("Generating Report for printer.\n");
     TextPrint rpt6 = new TextPrint();
     Frame tmp = new Frame();
     rpt6.generate(fileLoad, brisMCP, -1, tmp);
     tmp.dispose();
     break;
   }
  } else if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("JCP")) {
    BrisJCP brisJCP = new BrisJCP();
    if (brisJCP.load(fileL, fileLoad)) {
     if (Truline.userProps.getProperty("PostResults", "N").equals("Y")) {
      BrisXRD brisXRD = new BrisXRD();
      if (brisXRD.load(fileL, fileLoad, brisJCP.m_races))
       System.out.println("Results loaded.\n");
      else
       System.out.println("No results file found.\n");
     }
     if (mode == RESULTS)
      System.out.println("No Handicap - results only.\n");
     else {
      for (Enumeration e = brisJCP.m_races.elements(); e.hasMoreElements();) {
       Race race = (Race) e.nextElement();
       Handicap.compute(race);
      }
     }
    } else
     System.exit(0);
    switch (mode) {
     case TEXTMODE:
      TextReport rpt1 = new TextReport();
//      rpt1.generate(fileLoad, brisJCP, false);
      break;
     case RTFPRINTMODE:
      RichTextReport rpt1a = new RichTextReport();
      rpt1a.generate(fileLoad, brisJCP, false);
      break;
     case FLOWBETS:
      RichTextReport rpt1b = new RichTextReport();
      rpt1b.generateHF(fileLoad, brisJCP, false);
      break;
     case HTMLMODE:
      HtmlReport rpt2 = new HtmlReport();
      String newFilename = "file:/C:/Truline2012/" + fileLoad;
      newFilename = fileLoad;
      newFilename = dir + fileLoad;
      System.out.println("Generating Report for " + newFilename + "\n");
      rpt2.generate(newFilename, brisJCP);
      break;
     case FOLDERMODE:
      HtmlFolder rpt3 = new HtmlFolder();
      // rpt3.generateJCP(fileLoad, brisJCP);
      break;
     case SQLMODE:
      SQLReport rpt4 = new SQLReport();
      // rpt4.generateJCP(fileLoad, brisJCP, SQLMODE);
      break;
     case UPDMODE:
      SQLReport rpt5 = new SQLReport();
      // rpt5.generateJCP(fileLoad, brisJCP, UPDMODE);
      break;
     case DATABASE1:
      DatabaseReport rpt7a = new DatabaseReport();
      rpt7a.generate(databaseName, brisJCP, DATABASE1);
      break;
     case DATABASE2:
      DatabaseReport rpt8a = new DatabaseReport();
      rpt8a.generate(databaseName, brisJCP, DATABASE2);
      break;
     case DATABASE3:
      DatabaseReport rpt9a = new DatabaseReport();
      rpt9a.generate(databaseName, brisJCP, DATABASE3);
      break;
     case RESULTS:
      ResultsOnly rptRa = new ResultsOnly();
      rptRa.generate(databaseName, brisJCP, RESULTS);
      break;
     case PRINTMODE:
      System.out.println("Generating Report for printer.\n");
      TextPrint rpt6 = new TextPrint();
      Frame tmp = new Frame();
//      rpt6.generate(fileLoad, brisJCP, -1, tmp);
      tmp.dispose();
      break;
    }
   } else {
   Bris bris = new Bris();
   if (bris.load(fileL, fileLoad)) {
    if (Truline.userProps.getProperty("PostResults", "N").equals("Y")) {
     BrisXRD brisXRD = new BrisXRD();
     if (brisXRD.load(fileL, fileLoad, bris.m_races))
      System.out.println("Results loaded.\n");
     else
      System.out.println("No results file found.\n");
    }
    if (mode == RESULTS)
     System.out.println("No Handicap - results only.\n");
    else {
     for (Enumeration e = bris.m_races.elements(); e.hasMoreElements();) {
      Race race = (Race) e.nextElement();
      Handicap.compute(race);
     }
    }
   } else
    System.exit(0);
   switch (mode) {
    case TEXTMODE:
     TextReport rpt1 = new TextReport();
     rpt1.generate(fileLoad, bris, false);
     break;
    case RTFPRINTMODE:
     RichTextReport rpt1a = new RichTextReport();
     rpt1a.generate(fileLoad, bris, false);
     break;
    case FLOWBETS:
     RichTextReport rpt1b = new RichTextReport();
     rpt1b.generateHF(fileLoad, bris, false);
     break;
    case HTMLMODE:
     HtmlReport rpt2 = new HtmlReport();
     String newFilename = "file:/C:/Truline2012/" + fileLoad;
     newFilename = fileLoad;
     newFilename = dir + fileLoad;
     System.out.println("Generating Report for " + newFilename + "\n");
     rpt2.generate(newFilename, bris);
     break;
    case FOLDERMODE:
     HtmlFolder rpt3 = new HtmlFolder();
     rpt3.generate(fileLoad, bris);
     break;
    case SQLMODE:
     SQLReport rpt4 = new SQLReport();
     rpt4.generate(fileLoad, bris, SQLMODE);
     break;
    case UPDMODE:
     SQLReport rpt5 = new SQLReport();
     rpt5.generate(fileLoad, bris, UPDMODE);
     break;
    case DATABASE1:
     DatabaseReport rpt7 = new DatabaseReport();
     rpt7.generate(databaseName, bris, DATABASE1);
     break;
    case DATABASE2:
     DatabaseReport rpt8 = new DatabaseReport();
     rpt8.generate(databaseName, bris, DATABASE2);
     break;
    case DATABASE3:
     DatabaseReport rpt9 = new DatabaseReport();
     rpt9.generate(databaseName, bris, DATABASE3);
     break;
    case RESULTS:
     ResultsOnly rptR = new ResultsOnly();
     rptR.generate(databaseName, bris, RESULTS);
     break;
    case PRINTMODE:
     System.out.println("Generating Report for printer.\n");
     TextPrint rpt6 = new TextPrint();
     Frame tmp = new Frame();
     rpt6.generate(fileLoad, bris, -1, tmp);
     tmp.dispose();
     break;
   }
  }
 }
 
 public String getFile()
 {
  return file;
 }
 public void setFile(String file)
 {
  Truline.file = file;
 }
 /* end main                                                                         */
 /* -------------------------------------------------------------------------------- */
 public static String fixupFile(String file)
 {
  // System.out.println("file="+file);
  String filename = file;
  int idx = filename.lastIndexOf('\\');
  if (idx != -1)
   filename = filename.substring(idx + 1);
  idx = filename.lastIndexOf('.');
  if (idx != -1)
   filename = filename.substring(0, idx);
  return filename;
 }
 public static String getNextFile(String file)
 {
  boolean first_time = true;
  if (first_time == true) {
   first_time = false;
  }
  // System.out.println("file="+file);
  String filename = file;
  int idx = filename.lastIndexOf('\\');
  if (idx != -1)
   filename = filename.substring(idx + 1);
  idx = filename.lastIndexOf('.');
  if (idx != -1)
   filename = filename.substring(0, idx);
  return filename;
 }
 /**
  * Display to the screen.
  */
 public static void println(String line)
 {
  if (gui != null)
   gui.println(line);
  else
   System.out.println(line);
 }
 /**
  * Runtime usage
  */
 public static void usage()
 {
  System.out
    .println("usage: java -classpath Truline2012.jar com.mains.Truline [options] filename");
  System.out.println("      options:");
  System.out.println("          -t - text display format");
  System.out.println("          -p - Print report");
  System.out.println("          -w - Web page display format");
  System.out.println("          -f - Web folder display format");
  System.out.println("          -s - SQL script file format");
  System.out.println("          -g - GUI display format (default)");
  System.exit(0);
 }
}
