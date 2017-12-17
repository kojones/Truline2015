package com.base;
/*
*	Rich Text Report for displaying handicap for truline2015.
*
*/
import com.base.Bris;
import com.base.BrisMCP;
import com.base.BrisJCP;
import com.base.Handicap;
import com.base.Launch;
import com.base.Lib;
import com.base.Log;
import com.base.Post;
import com.base.Race;
import com.base.TrainerJockeyStats;

import java.util.*;
import java.io.*;
import java.text.*;

import com.mains.Truline;

public class RichTextReport
{
 public int             fontsize;
 // public int pagesize;
 public static String[] names        = { "FS", "SS", "FT", "TT", "CS", "AS",
  "RE", "QP", "EN", "EPS"          };
 public int[]           biasPoints    = new int[names.length]; // Bias points Dirt
 public int[]           biasPointsT   = new int[names.length]; // Bias points Turf
 public int[]           biasPointsSP  = new int[names.length]; // Bias points Dirt Sprints
 public int[]           biasPointsRT  = new int[names.length]; // Bias points Dirt Routes
 public int[]           biasPointsTSP  = new int[names.length]; // Bias points Turf Sprints
 public int[]           biasPointsTRT  = new int[names.length]; // Bias points Turf Routes
 public int[]           biasPointsRank  = new int[30]; // Bias points rank
 private String         finishPosPrt = "";
 private String         raceSurface = "";
 public RichTextReport() {
}
/**
 * Generate the report - all races - Bris DRF input
 */
public void generate(String filename, Bris bris, boolean print)
{
 for (int i = 0; i < 10; i++) {
  biasPoints[i] = 0;
  biasPointsT[i] = 0;
  biasPointsSP[i] = 0;
  biasPointsRT[i] = 0;
  biasPointsTSP[i] = 0;
  biasPointsTRT[i] = 0;
 }
 boolean generated = false;
 String name = "tmp.rtf";
 if (!print) {
  name = filename + ".rtf";
  Truline.println("Generating Text Report to " + name);
 }
 try {
  if (Log.isDebug(Log.TRACE))
   Log.print("Writing text report to " + name + "\n");
  PrintWriter out = new PrintWriter(new FileWriter(name), true);
  fontSetup(out);
  for (Enumeration e = bris.m_races.elements(); e.hasMoreElements();) {
   Race race = (Race) e.nextElement();
   if (race.m_distance >= Lib.atoi(Truline.userProps.getProperty("MinDistance", "0")))
   {
    writeReport(out, race);
    accumulateBias(race);
   }
   generated = true;
  }
  out.println("}");
  out.close();
 } catch (Exception e) {
  Log.print("Exception opening output file " + e + "\n");
 }
 if (generated && print) {
  // Send tmp.rpt to printer
  StringBuffer results = new StringBuffer();
  try {
   String printProgram = Truline.userProps.getProperty("PrintProgram",
     "Wordpad.exe /p");
   String[] command = Launch.fixArgs(printProgram + " " + name);
   Launch launcher = new Launch(command, null, null, results);
   launcher.exec();
   launcher.waitfor(); // wait until it completes.
  } catch (Exception e) {
   results.append(e.toString());
   results.append("\n");
  }
  if (results.length() > 0)
   Log.println(results.toString());
 }
}
/**
 * Generate the report - all races - Bris MCP input
 */
public void generate(String filename, BrisMCP brisMCP, boolean print)
{
 for (int i = 0; i < 10; i++) {
  biasPoints[i] = 0;
  biasPointsT[i] = 0;
  biasPointsSP[i] = 0;
  biasPointsRT[i] = 0;
  biasPointsTSP[i] = 0;
  biasPointsTRT[i] = 0;
 }
 boolean generated = false;
 String name = "tmp.rtf";
 if (!print) {
  name = filename + ".rtf";
  Truline.println("Generating Text Report to " + name);
 }
 try {
  if (Log.isDebug(Log.TRACE))
   Log.print("Writing text report to " + name + "\n");
  PrintWriter out = new PrintWriter(new FileWriter(name), true);
  fontSetup(out);
  for (Enumeration e = brisMCP.m_races.elements(); e.hasMoreElements();) {
   Race race = (Race) e.nextElement();
   if (race.m_distance >= Lib.atoi(Truline.userProps.getProperty("MinDistance", "0")))
   {
    writeReport(out, race);
    accumulateBias(race);
   }
   generated = true;
  }
  out.println("}");
  out.close();
 } catch (Exception e) {
  Log.print("Exception opening output file " + e + "\n");
 }
 if (generated && print) {
  // Send tmp.rpt to printer
  StringBuffer results = new StringBuffer();
  try {
   String printProgram = Truline.userProps.getProperty("PrintProgram",
     "Wordpad.exe /p");
   String[] command = Launch.fixArgs(printProgram + " " + name);
   Launch launcher = new Launch(command, null, null, results);
   launcher.exec();
   launcher.waitfor(); // wait until it completes.
  } catch (Exception e) {
   results.append(e.toString());
   results.append("\n");
  }
  if (results.length() > 0)
   Log.println(results.toString());
 }
}
/**
 * Generate the report - all races - Bris JCP input
 */
public void generate(String filename, BrisJCP brisJCP, boolean print)
{
 for (int i = 0; i < 10; i++) {
  biasPoints[i] = 0;
  biasPointsT[i] = 0;
  biasPointsSP[i] = 0;
  biasPointsRT[i] = 0;
  biasPointsTSP[i] = 0;
  biasPointsTRT[i] = 0;
}
 boolean generated = false;
 String name = "tmp.rtf";
 if (!print) {
  name = filename + ".rtf";
  Truline.println("Generating Text Report to " + name);
 }
 try {
  if (Log.isDebug(Log.TRACE))
   Log.print("Writing text report to " + name + "\n");
  PrintWriter out = new PrintWriter(new FileWriter(name), true);
  fontSetup(out);
  for (Enumeration e = brisJCP.m_races.elements(); e.hasMoreElements();) {
   Race race = (Race) e.nextElement();
   if (race.m_distance >= Lib.atoi(Truline.userProps.getProperty("MinDistance", "0")))
   {
    writeReport(out, race);
    accumulateBias(race);
   }
   generated = true;
  }
  out.println("}");
  out.close();
 } catch (Exception e) {
  Log.print("Exception opening output file " + e + "\n");
 }
 if (generated && print) {
  // Send tmp.rpt to printer
  StringBuffer results = new StringBuffer();
  try {
   String printProgram = Truline.userProps.getProperty("PrintProgram",
     "Wordpad.exe /p");
   String[] command = Launch.fixArgs(printProgram + " " + name);
   Launch launcher = new Launch(command, null, null, results);
   launcher.exec();
   launcher.waitfor(); // wait until it completes.
  } catch (Exception e) {
   results.append(e.toString());
   results.append("\n");
  }
  if (results.length() > 0)
   Log.println(results.toString());
 }
}
/**
 * Generate the report.
 * 
 * @param filename
 *         - display name of the BRIS file
 * @param bris
 *         - handicap structure
 * @param raceNbr
 *         - The requested race number, (-1 for all races)
 * @param print
 *         - flag indicating wether to print or just generate file
 */
public void generate(String filename, Bris bris, int raceNbr, boolean print)
{
 String name = "truline.rtf";
 if (!print) {
  if (raceNbr > 0)
   name = filename + "_" + raceNbr + ".rtf";
  else
   name = filename + ".rtf";
  Truline.println("Generating Text Report to " + name);
 }
 for (int i = 0; i < 10; i++) {
  biasPoints[i] = 0;
  biasPointsT[i] = 0;
  biasPointsSP[i] = 0;
  biasPointsRT[i] = 0;
  biasPointsTSP[i] = 0;
  biasPointsTRT[i] = 0;
}
 boolean generated = false;
 PrintWriter out = null;
 try {
  out = new PrintWriter(new FileWriter(name));
  fontSetup(out);
  for (int idx = 0; idx < bris.m_races.size(); idx++) {
   Race race = (Race) bris.m_races.elementAt(idx);
   if (race.m_raceNo == raceNbr) {
    raceSurface = race.m_surface;
   }
  }
  for (int idx = 0; idx < bris.m_races.size(); idx++) {
   Race race = (Race) bris.m_races.elementAt(idx);
   if (raceNbr > 0) {
    // just one race requested.
    if (generated)
     break;
    if (race.m_raceNo != raceNbr) {
     if (race.m_surface.equals(raceSurface) && (race.m_distance >= Lib.atoi(Truline.userProps.getProperty("MinDistance", "0"))))
      accumulateBias(race);
     continue;
    }
   }
   if (race.m_distance >= Lib.atoi(Truline.userProps.getProperty("MinDistance", "0")))
   {
    writeReport(out, race);
    accumulateBias(race);
   }
   generated = true;
  }
  out.println("}");
  out.close();
 } catch (Exception e) {
  Log.print("Exception generating report " + e + "\n");
  Truline.println("Exception generating report " + e);
  if (out != null)
   out.close();
 }
 if (generated && print) {
  // Send tmp.rpt to printer
  StringBuffer results = new StringBuffer();
  try {
   String printProgram = Truline.userProps.getProperty("PrintProgram",
     "Wordpad.exe /p");
   String[] command = Launch.fixArgs(printProgram + " " + name);
   Launch launcher = new Launch(command, null, null, results);
   launcher.exec();
   // launcher.waitfor(); // wait until it completes.
  } catch (Exception e) {
   results.append(e.toString());
   results.append("\n");
  }
  if (results.length() > 0) {
   Log.println(results.toString());
   Truline.println(results.toString());
  }
 }
}
/**
 * Generate the report.
 * 
 * @param filename
 *         - display name of the BRIS MCP file
 * @param brisMCP
 *         - handicap structure
 * @param raceNbr
 *         - The requested race number, (-1 for all races)
 * @param print
 *         - flag indicating wether to print or just generate file
 */
public void generate(String filename, BrisMCP brisMCP, int raceNbr,
  boolean print)
{
 String name = "truline.rtf";
 if (!print) {
  if (raceNbr > 0)
   name = filename + "_" + raceNbr + ".rtf";
  else
   name = filename + ".rtf";
  Truline.println("Generating Text Report to " + name);
 }
 for (int i = 0; i < 10; i++) {
  biasPoints[i] = 0;
  biasPointsT[i] = 0;
  biasPointsSP[i] = 0;
  biasPointsRT[i] = 0;
  biasPointsTSP[i] = 0;
  biasPointsTRT[i] = 0;
}
 boolean generated = false;
 PrintWriter out = null;
 try {
  out = new PrintWriter(new FileWriter(name));
  fontSetup(out);
  for (int idx = 0; idx < brisMCP.m_races.size(); idx++) {
   Race race = (Race) brisMCP.m_races.elementAt(idx);
   if (race.m_raceNo == raceNbr) {
    raceSurface = race.m_surface;
   }
  }
  for (int idx = 0; idx < brisMCP.m_races.size(); idx++) {
   Race race = (Race) brisMCP.m_races.elementAt(idx);
   if (raceNbr > 0) {
    // just one race requested.
    if (generated)
     break;
    if (race.m_raceNo != raceNbr) {
     if (race.m_surface.equals(raceSurface) && (race.m_distance >= Lib.atoi(Truline.userProps.getProperty("MinDistance", "0"))))
      accumulateBias(race);
     continue;
    }
   }
   if (race.m_distance >= Lib.atoi(Truline.userProps.getProperty("MinDistance", "0")))
   {
    writeReport(out, race);
    accumulateBias(race);
   }
   generated = true;
  }
  out.println("}");
  out.close();
 } catch (Exception e) {
  Log.print("Exception generating report " + e + "\n");
  Truline.println("Exception generating report " + e);
  if (out != null)
   out.close();
 }
 if (generated && print) {
  // Send tmp.rpt to printer
  StringBuffer results = new StringBuffer();
  try {
   String printProgram = Truline.userProps.getProperty("PrintProgram",
     "Wordpad.exe /p");
   String[] command = Launch.fixArgs(printProgram + " " + name);
   Launch launcher = new Launch(command, null, null, results);
   launcher.exec();
   // launcher.waitfor(); // wait until it completes.
  } catch (Exception e) {
   results.append(e.toString());
   results.append("\n");
  }
  if (results.length() > 0) {
   Log.println(results.toString());
   Truline.println(results.toString());
  }
 }
}
/**
 * Generate the report.
 * 
 * @param filename
 *         - display name of the BRIS JCP file
 * @param brisJCP
 *         - handicap structure
 * @param raceNbr
 *         - The requested race number, (-1 for all races)
 * @param print
 *         - flag indicating wether to print or just generate file
 */
public void generate(String filename, BrisJCP brisJCP, int raceNbr,
  boolean print)
{
 String name = "truline.rtf";
 // if (!print) {
  if (raceNbr > 0)
   name = filename + "_" + raceNbr + ".rtf";
  else
   name = filename + ".rtf";
  Truline.println("Generating Text Report to " + name);
 // }
 for (int i = 0; i < 10; i++) {
  biasPoints[i] = 0;
  biasPointsT[i] = 0;
  biasPointsSP[i] = 0;
  biasPointsRT[i] = 0;
  biasPointsTSP[i] = 0;
  biasPointsTRT[i] = 0;
}
 boolean generated = false;
 PrintWriter out = null;
 try {
  out = new PrintWriter(new FileWriter(name));
  fontSetup(out);
  for (int idx = 0; idx < brisJCP.m_races.size(); idx++) {
   Race race = (Race) brisJCP.m_races.elementAt(idx);
   if (race.m_raceNo == raceNbr) {
    raceSurface = race.m_surface;
   }
  }
  for (int idx = 0; idx < brisJCP.m_races.size(); idx++) {
   Race race = (Race) brisJCP.m_races.elementAt(idx);
   if (raceNbr > 0) {
    // just one race requested.
    if (generated)
     break;
    if (race.m_raceNo != raceNbr) {
     if (race.m_surface.equals(raceSurface) && (race.m_distance >= Lib.atoi(Truline.userProps.getProperty("MinDistance", "0"))))
      accumulateBias(race);
     continue;
    }
   }
   if (race.m_distance >= Lib.atoi(Truline.userProps.getProperty("MinDistance", "0")))
   {
    writeReport(out, race);
    accumulateBias(race);
   }
   generated = true;
  }
  out.println("}");
  out.close();
 } catch (Exception e) {
  Log.print("Exception generating report " + e + "\n");
  Truline.println("Exception generating report " + e);
  if (out != null)
   out.close();
 }
 if (generated && print) {
  // Send tmp.rpt to printer
  StringBuffer results = new StringBuffer();
  try {
   String printProgram = Truline.userProps.getProperty("PrintProgram",
     "Wordpad.exe /p");
   String[] command = Launch.fixArgs(printProgram + " " + name);
   Launch launcher = new Launch(command, null, null, results);
   launcher.exec();
   // launcher.waitfor(); // wait until it completes.
  } catch (Exception e) {
   results.append(e.toString());
   results.append("\n");
  }
  if (results.length() > 0) {
   Log.println(results.toString());
   Truline.println(results.toString());
  }
 }
}
/**
 * Generate the PI report - all races - Bris DRF input
 */
public void generateHF(String filename, Bris bris, boolean print)
{
 for (int i = 0; i < 10; i++) {
  biasPoints[i] = 0;
  biasPointsT[i] = 0;
  biasPointsSP[i] = 0;
  biasPointsRT[i] = 0;
  biasPointsTSP[i] = 0;
  biasPointsTRT[i] = 0;
}
 boolean generated = false;
 String name = filename + "PI.rtf";
 if (!print) {
  name = filename + "PI.rtf";
  Truline.println("Generating Horse Peripheral Information Report to " + name);
 }
 try {
  if (Log.isDebug(Log.TRACE))
   Log.print("Writing text report to " + name + "\n");
  PrintWriter out = new PrintWriter(new FileWriter(name), true);
  fontSetup(out);
  for (Enumeration e = bris.m_races.elements(); e.hasMoreElements();) {
   Race race = (Race) e.nextElement();
   if (race.m_distance >= Lib.atoi(Truline.userProps.getProperty("MinDistance", "0"))) {
    // if (race.cntHorseFlows >= 0) {
     writeHFReport(out, race);
     accumulateBias(race);
     generated = true;
    // }
   }
  }
  if (!generated) {
   generated = true;
   out.println("\\par                          " + Truline.title
     + "                ");
   out.println("\\par [" + Truline.version + "] " + Truline.copyright);
   out
     .println("\\par ===============================================================================================");
   out.println("\\par TrackDate " + filename + "    No Peripheral Information Indicators today");
  }
  out.println("}");
  out.close();
 } catch (Exception e) {
  Log.print("Exception opening output file " + e + "\n");
 }
 if (generated && print) {
  // Send tmp.rpt to printer
  StringBuffer results = new StringBuffer();
  try {
   String printProgram = Truline.userProps.getProperty("PrintProgram",
     "Wordpad.exe /p");
   String[] command = Launch.fixArgs(printProgram + " " + name);
   Launch launcher = new Launch(command, null, null, results);
   launcher.exec();
   launcher.waitfor(); // wait until it completes.
  } catch (Exception e) {
   results.append(e.toString());
   results.append("\n");
  }
  if (results.length() > 0)
   Log.println(results.toString());
 }
}
/**
 * Generate the PI report - all races - Bris MCP input
 */
public void generateHF(String filename, BrisMCP brisMCP, boolean print)
{
 for (int i = 0; i < 10; i++) {
  biasPoints[i] = 0;
  biasPointsT[i] = 0;
  biasPointsSP[i] = 0;
  biasPointsRT[i] = 0;
  biasPointsTSP[i] = 0;
  biasPointsTRT[i] = 0;
}
 boolean generated = false;
 String name = filename + "PI.rtf";
 if (!print) {
  name = filename + "PI.rtf";
  Truline.println("Generating Horse Peripheral Information Report to " + name);
 }
 try {
  if (Log.isDebug(Log.TRACE))
   Log.print("Writing text report to " + name + "\n");
  PrintWriter out = new PrintWriter(new FileWriter(name), true);
  fontSetup(out);
  for (Enumeration e = brisMCP.m_races.elements(); e.hasMoreElements();) {
   Race race = (Race) e.nextElement();
   if (race.m_distance >= Lib.atoi(Truline.userProps.getProperty("MinDistance", "0"))) {
    // if (race.cntHorseFlows >= 0) {
     writeHFReport(out, race);
     accumulateBias(race);
     generated = true;
    // }
   }
  }
  if (!generated) {
   generated = true;
   out.println("\\par                          " + Truline.title
     + "                ");
   out.println("\\par [" + Truline.version + "] " + Truline.copyright);
   out
     .println("\\par ============================================================================================");
   out.println("\\par TrackDate " + filename + "    No Peripheral Information Indicators today");
  }
  out.println("}");
  out.close();
 } catch (Exception e) {
  Log.print("Exception opening output file " + e + "\n");
 }
 if (generated && print) {
  // Send tmp.rpt to printer
  StringBuffer results = new StringBuffer();
  try {
   String printProgram = Truline.userProps.getProperty("PrintProgram",
     "Wordpad.exe /p");
   String[] command = Launch.fixArgs(printProgram + " " + name);
   Launch launcher = new Launch(command, null, null, results);
   launcher.exec();
   launcher.waitfor(); // wait until it completes.
  } catch (Exception e) {
   results.append(e.toString());
   results.append("\n");
  }
  if (results.length() > 0)
   Log.println(results.toString());
 }
}
/**
 * Generate the PI report - all races - Bris JCP input
 */
public void generateHF(String filename, BrisJCP brisJCP, boolean print)
{
 for (int i = 0; i < 10; i++) {
  biasPoints[i] = 0;
  biasPointsT[i] = 0;
  biasPointsSP[i] = 0;
  biasPointsRT[i] = 0;
  biasPointsTSP[i] = 0;
  biasPointsTRT[i] = 0;
}
 boolean generated = false;
 String name = filename + "PI.rtf";
 if (!print) {
  name = filename + "PI.rtf";
  Truline.println("Generating Horse Peripheral Information Report to " + name);
 }
 try {
  if (Log.isDebug(Log.TRACE))
   Log.print("Writing text report to " + name + "\n");
  PrintWriter out = new PrintWriter(new FileWriter(name), true);
  fontSetup(out);
  for (Enumeration e = brisJCP.m_races.elements(); e.hasMoreElements();) {
   Race race = (Race) e.nextElement();
   if (race.m_distance >= Lib.atoi(Truline.userProps.getProperty("MinDistance", "0"))) {
    // if (race.cntHorseFlows >= 0) {
     writeHFReport(out, race);
     accumulateBias(race);
     generated = true;
    // }
   }
  }
  if (!generated) {
   generated = true;
   out.println("\\par                          " + Truline.title
     + "                ");
   out.println("\\par [" + Truline.version + "] " + Truline.copyright);
   out
     .println("\\par ============================================================================================");
   out.println("\\par TrackDate " + filename + "    No Peripheral Information Indicators today");
  }
  out.println("}");
  out.close();
 } catch (Exception e) {
  Log.print("Exception opening output file " + e + "\n");
 }
 if (generated && print) {
  // Send tmp.rpt to printer
  StringBuffer results = new StringBuffer();
  try {
   String printProgram = Truline.userProps.getProperty("PrintProgram",
     "Wordpad.exe /p");
   String[] command = Launch.fixArgs(printProgram + " " + name);
   Launch launcher = new Launch(command, null, null, results);
   launcher.exec();
   launcher.waitfor(); // wait until it completes.
  } catch (Exception e) {
   results.append(e.toString());
   results.append("\n");
  }
  if (results.length() > 0)
   Log.println(results.toString());
 }
}
/**
 * Generate the PI report.
 * 
 * @param filename
 *         - display name of the BRIS file
 * @param bris
 *         - handicap structure
 * @param raceNbr
 *         - The requested race number, (-1 for all races)
 * @param print
 *         - flag indicating wether to print or just generate file
 */
public void generateHF(String filename, Bris bris, int raceNbr, boolean print)
{
 String name = "trulinePI.rtf";
 // if (!print) {
  if (raceNbr > 0)
   name = filename + "PI_" + raceNbr + ".rtf";
  else
   name = filename + "PI.rtf";
  Truline.println("Generating Text Report to " + name);
 // }
 for (int i = 0; i < 10; i++) {
  biasPoints[i] = 0;
  biasPointsT[i] = 0;
  biasPointsSP[i] = 0;
  biasPointsRT[i] = 0;
  biasPointsTSP[i] = 0;
  biasPointsTRT[i] = 0;
}
 boolean generated = false;
 PrintWriter out = null;
 try {
  out = new PrintWriter(new FileWriter(name));
  fontSetup(out);
  for (int idx = 0; idx < bris.m_races.size(); idx++) {
   Race race = (Race) bris.m_races.elementAt(idx);
   if (race.m_raceNo == raceNbr) {
    raceSurface = race.m_surface;
   }
  }
  for (int idx = 0; idx < bris.m_races.size(); idx++) {
   Race race = (Race) bris.m_races.elementAt(idx);
   if (raceNbr > 0) {
    // just one race requested.
    if (generated)
     break;
    if (race.m_raceNo != raceNbr) {
     if (race.m_surface.equals(raceSurface) && (race.m_distance >= Lib.atoi(Truline.userProps.getProperty("MinDistance", "0"))))
      accumulateBias(race);
     continue;
    }
   }
   if (race.m_distance >= Lib.atoi(Truline.userProps.getProperty("MinDistance", "0")))
   {
    writeHFReport(out, race);
    accumulateBias(race);
   }
   generated = true;
  }
  out.println("}");
  out.close();
 } catch (Exception e) {
  Log.print("Exception generating report " + e + "\n");
  Truline.println("Exception generating report " + e);
  if (out != null)
   out.close();
 }
 if (generated && print) {
  // Send tmp.rpt to printer
  StringBuffer results = new StringBuffer();
  try {
   String printProgram = Truline.userProps.getProperty("PrintProgram",
     "Wordpad.exe /p");
   String[] command = Launch.fixArgs(printProgram + " " + name);
   Launch launcher = new Launch(command, null, null, results);
   launcher.exec();
   // launcher.waitfor(); // wait until it completes.
  } catch (Exception e) {
   results.append(e.toString());
   results.append("\n");
  }
  if (results.length() > 0) {
   Log.println(results.toString());
   Truline.println(results.toString());
  }
 }
}
/**
 * Generate the PI report.
 * 
 * @param filename
 *         - display name of the BRIS MCP file
 * @param brisMCP
 *         - handicap structure
 * @param raceNbr
 *         - The requested race number, (-1 for all races)
 * @param print
 *         - flag indicating wether to print or just generate file
 */
public void generateHF(String filename, BrisMCP brisMCP, int raceNbr,
  boolean print)
{
 String name = "trulinePI.rtf";
 // if (!print) {
  if (raceNbr > 0)
   name = filename + "PI_" + raceNbr + ".rtf";
  else
   name = filename + "PI.rtf";
  Truline.println("Generating Text Report to " + name);
 // }
 for (int i = 0; i < 10; i++) {
  biasPoints[i] = 0;
  biasPointsT[i] = 0;
  biasPointsSP[i] = 0;
  biasPointsRT[i] = 0;
  biasPointsTSP[i] = 0;
  biasPointsTRT[i] = 0;
}
 boolean generated = false;
 PrintWriter out = null;
 try {
  out = new PrintWriter(new FileWriter(name));
  fontSetup(out);
  for (int idx = 0; idx < brisMCP.m_races.size(); idx++) {
   Race race = (Race) brisMCP.m_races.elementAt(idx);
   if (race.m_raceNo == raceNbr) {
    raceSurface = race.m_surface;
   }
  }
  for (int idx = 0; idx < brisMCP.m_races.size(); idx++) {
   Race race = (Race) brisMCP.m_races.elementAt(idx);
   if (raceNbr > 0) {
    // just one race requested.
    if (generated)
     break;
    if (race.m_raceNo != raceNbr) {
     if (race.m_surface.equals(raceSurface) && (race.m_distance >= Lib.atoi(Truline.userProps.getProperty("MinDistance", "0"))))
      accumulateBias(race);
     continue;
    }
   }
   if (race.m_distance >= Lib.atoi(Truline.userProps.getProperty("MinDistance", "0")))
   {
    writeHFReport(out, race);
    accumulateBias(race);
   }
   generated = true;
  }
  out.println("}");
  out.close();
 } catch (Exception e) {
  Log.print("Exception generating report " + e + "\n");
  Truline.println("Exception generating report " + e);
  if (out != null)
   out.close();
 }
 if (generated && print) {
  // Send tmp.rpt to printer
  StringBuffer results = new StringBuffer();
  try {
   String printProgram = Truline.userProps.getProperty("PrintProgram",
     "Wordpad.exe /p");
   String[] command = Launch.fixArgs(printProgram + " " + name);
   Launch launcher = new Launch(command, null, null, results);
   launcher.exec();
   // launcher.waitfor(); // wait until it completes.
  } catch (Exception e) {
   results.append(e.toString());
   results.append("\n");
  }
  if (results.length() > 0) {
   Log.println(results.toString());
   Truline.println(results.toString());
  }
 }
}
/**
 * Generate the PI report.
 * 
 * @param filename
 *         - display name of the BRIS JCP file
 * @param brisJCP
 *         - handicap structure
 * @param raceNbr
 *         - The requested race number, (-1 for all races)
 * @param print
 *         - flag indicating wether to print or just generate file
 */
public void generateHF(String filename, BrisJCP brisJCP, int raceNbr,
  boolean print)
{
 String name = "trulinePI.rtf";
 // if (!print) {
  if (raceNbr > 0)
   name = filename + "PI_" + raceNbr + ".rtf";
  else
   name = filename + "PI.rtf";
  Truline.println("Generating Text Report to " + name);
 // }
 for (int i = 0; i < 10; i++) {
  biasPoints[i] = 0;
  biasPointsT[i] = 0;
  biasPointsSP[i] = 0;
  biasPointsRT[i] = 0;
  biasPointsTSP[i] = 0;
  biasPointsTRT[i] = 0;
}
 boolean generated = false;
 PrintWriter out = null;
 try {
  out = new PrintWriter(new FileWriter(name));
  fontSetup(out);
  for (int idx = 0; idx < brisJCP.m_races.size(); idx++) {
   Race race = (Race) brisJCP.m_races.elementAt(idx);
   if (race.m_raceNo == raceNbr) {
    raceSurface = race.m_surface;
   }
  }
  for (int idx = 0; idx < brisJCP.m_races.size(); idx++) {
   Race race = (Race) brisJCP.m_races.elementAt(idx);
   if (raceNbr > 0) {
    // just one race requested.
    if (generated)
     break;
    if (race.m_raceNo != raceNbr) {
     if (race.m_surface.equals(raceSurface) && (race.m_distance >= Lib.atoi(Truline.userProps.getProperty("MinDistance", "0"))))
      accumulateBias(race);
     continue;
    }
   }
   if (race.m_distance >= Lib.atoi(Truline.userProps.getProperty("MinDistance", "0")))
   {
    writeHFReport(out, race);
    accumulateBias(race);
   }
   generated = true;
  }
  out.println("}");
  out.close();
 } catch (Exception e) {
  Log.print("Exception generating report " + e + "\n");
  Truline.println("Exception generating report " + e);
  if (out != null)
   out.close();
 }
 if (generated && print) {
  // Send tmp.rpt to printer
  StringBuffer results = new StringBuffer();
  try {
   String printProgram = Truline.userProps.getProperty("PrintProgram",
     "Wordpad.exe /p");
   String[] command = Launch.fixArgs(printProgram + " " + name);
   Launch launcher = new Launch(command, null, null, results);
   launcher.exec();
   // launcher.waitfor(); // wait until it completes.
  } catch (Exception e) {
   results.append(e.toString());
   results.append("\n");
  }
  if (results.length() > 0) {
   Log.println(results.toString());
   Truline.println(results.toString());
  }
 }
}
/**
 * Accumulate Bias
 */
public void accumulateBias(Race race)
{
 for (Enumeration e = race.m_posts.elements(); e.hasMoreElements();) {
  Post post = (Post) e.nextElement();
  String entry = post.m_props.getProperty("ENTRY", "");
  if (entry.equals("S")) {
   continue;
  }
  if (post.m_handicap == null || post.m_horseName == null)
   continue; // position is empty
  String finishPos = post.m_finishPos;
  if (finishPos.equals("1")) {
   for (int i = 0; i < 10; i++) {
    if ((Truline.userProps.getProperty("TrackTheBias", "N").equals("1") && post.m_handicap.rank[i] == 1)
      || (Truline.userProps.getProperty("TrackTheBias", "N").equals("2") && post.m_handicap.rank[i] < 3)) {
     if (race.m_surface.equals("D") || race.m_surface.equals("A")) {
      biasPoints[i] = biasPoints[i] + 2;
      if (race.m_distance < 1760)
       biasPointsSP[i] = biasPointsSP[i] + 2;
      else
       biasPointsRT[i] = biasPointsRT[i] + 2;
     }
     else {
      biasPointsT[i] = biasPointsT[i] + 2;
      if (race.m_distance < 1760)
       biasPointsTSP[i] = biasPointsTSP[i] + 2;
      else
       biasPointsTRT[i] = biasPointsTRT[i] + 2;
     }
    }
    if ((Truline.userProps.getProperty("TrackTheBias", "N").equals("1") && post.m_handicap.rank[i] == 2)) {
     if (race.m_surface.equals("D") || race.m_surface.equals("A")) {
      biasPoints[i] = biasPoints[i] + 1;
      if (race.m_distance < 1760)
       biasPointsSP[i] = biasPointsSP[i] + 1;
      else
       biasPointsRT[i] = biasPointsRT[i] + 1;
     }
     else {
      biasPointsT[i] = biasPointsT[i] + 1;
      if (race.m_distance < 1760)
       biasPointsTSP[i] = biasPointsTSP[i] + 1;
      else
       biasPointsTRT[i] = biasPointsTRT[i] + 1;
     }
    }
  }
  }
  if (finishPos.equals("2")) {
   for (int i = 0; i < 10; i++) {
    if ((Truline.userProps.getProperty("TrackTheBias", "N").equals("1") && post.m_handicap.rank[i] == 1)
      || (Truline.userProps.getProperty("TrackTheBias", "N").equals("2") && post.m_handicap.rank[i] < 3))
     if (race.m_surface.equals("D") || race.m_surface.equals("A")) {
      biasPoints[i] = biasPoints[i] + 1;
      if (race.m_distance < 1760)
       biasPointsSP[i] = biasPointsSP[i] + 1;
      else
       biasPointsRT[i] = biasPointsRT[i] + 1;
     }
     else {
      biasPointsT[i] = biasPointsT[i] + 1;
      if (race.m_distance < 1760)
       biasPointsTSP[i] = biasPointsTSP[i] + 1;
      else
       biasPointsTRT[i] = biasPointsTRT[i] + 1;
     }
   }
  }
 }
}
private void fontSetup(PrintWriter out)
{
 out
   .print("{\\rtf1\\ansi\\deff0\\deftab720{\\fonttbl{\\f0\\fswiss MS Sans Serif;}{\\f1\\froman\\fcharset2 Symbol;}{\\f2\\fmodern Courier New;}{\\f3\fmodern\\fprq1 Courier New;}}");
 out.print("{\\colortbl\\red0\\green0\\blue0;}");
 out
   .print("\\deflang1033\\pard\\plain");
 fontsize = Lib.atoi(Truline.userProps.getProperty("FontSize", "8"));
 switch (fontsize) {
  case 7:
   out.print("\\margl720\\margr720\\margt720\\margb720\\f2\\fs14\\cf0");
   // pagesize = 70;
   break;
  case 9:
   out.print("\\margl720\\margr720\\margt720\\margb720\\f2\\fs18\\cf0");
   // pagesize = 61;
   break; 
  default:
  case 8:
   out.print("\\margl720\\margr720\\margt720\\margb720\\f2\\fs16\\cf0");
   // pagesize = 67;
   break;
  case 12:
   out.print("\\landscape\\paperw15840\\paperh12240\\margl720\\margr720\\margt720\\margb720\\f2\\fs24\\cf0");
   // pagesize = 67;
   break;
 }
 // pagesize = Lib.atoi(Truline.userProps.getProperty("PageSize",
 // ""+pagesize));
 // Truline.println("Font size="+fontsize+" Page size="+pagesize);
}
public void writeReport(PrintWriter out, Race race)
{
 DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
 DateFormat dtf = DateFormat.getDateTimeInstance(DateFormat.SHORT,
   DateFormat.SHORT);
 String datestr = dtf.format(new Date());
 int line = 0;
 String biasPts[] = { " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ",
 " " }; // Bias points for each horse
 String biasPtsD[] = { " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ",
 " " }; // Bias points for each horse
 int biasBonus = 0;
 int biasBonusD = 0;
 try {
  out.println("\\par                          " + Truline.title
    + "                " + datestr);
  out.println("\\par [" + Truline.version + "] " + Truline.copyright);
  out
    .println("\\par ===================================================================================================");
  out.println("\\par Track "
    + Lib.pad(race.m_track, 3)
    + "    "
    + Lib.datetoa(race.m_raceDate)
    + "    Race#"
    + race.m_raceNo
    + "   Distance "
    + Lib.ftoa(((double) race.m_distance) / Handicap.YdPerF, 1)
    + "F"
    + "  Type "
    + ((race.m_raceType.equals("G1")) ? "G1-Stake I" : (race.m_raceType
      .equals("G2")) ? "G2-Stake II"
      : (race.m_raceType.equals("G3")) ? "G3-Stake III" : (race.m_raceType
        .equals("N")) ? "N-nongraded stake"
        : (race.m_raceType.equals("A")) ? "A-allowance" : (race.m_raceType
          .equals("R")) ? "R-Starter Alw"
          : (race.m_raceType.equals("T")) ? "T-Starter Hcp" : (race.m_raceType
            .equals("C")) ? "C-claiming"
            : (race.m_raceType.equals("S")) ? "S-mdn sp wt" : (race.m_raceType
              .equals("M")) ? "M-mdn claimer" : race.m_raceType));
  NumberFormat fmt = NumberFormat.getCurrencyInstance();
  out.println("\\par "
    + ((race.m_claim != 0) ? ("Claim " + fmt.format(race.m_claim) + "   Purse " + fmt.format(race.m_purse))
      : (race.m_purse != 0) ? ("Purse " + fmt.format(race.m_purse))
        : "                ") + "  Surface "
    + ((race.m_surface.equals("D")) ? "Dirt" :
       (race.m_surface.equals("A")) ? "All Weather" :"Turf")
    + "  Handicapped for "+race.m_trackCond+" track");
  String sexAge = race.m_props.getProperty("AGESEX", "");
  out.print("\\par AGE/SEX (" + sexAge + ")");
  switch (sexAge.charAt(0)) {
   case 'A':
    out.print(" 2 year olds");
    break;
   case 'B':
    out.print(" 3 year olds");
    break;
   case 'C':
    out.print(" 4 year olds");
    break;
   case 'D':
    out.print(" 5 year olds");
    break;
   case 'E':
    out.print(" 3 & 4 year olds");
    break;
   case 'F':
    out.print(" 4 & 5 year olds");
    break;
   case 'G':
    out.print(" 3, 4, and 5 year olds");
    break;
   case 'H':
    out.print(" all ages");
    break;
  }
  switch (sexAge.charAt(1)) {
   case 'O':
    out.print(", That age Only");
    break;
   case 'U':
    out.print(", That age and Up");
    break;
  }
  switch (sexAge.charAt(2)) {
   case 'N':
    out.print(", No Sex Restrictions");
    break;
   case 'M':
    out.print(", Mares and Fillies");
    break;
   case 'C':
    out.print(", Colts and/or Geldings");
    break;
   case 'F':
    out.print(", Fillies Only");
    break;
  }
  out.println();
  out.println("\\par          Run Style Profile  " + race.m_runStyleProfile);
  if (Truline.userProps.getProperty("Experimental", "N").equals("Yes")) {
   if (race.cntRaceFlows >= 0 || race.cntRaceFlowsAK >= 0)
    out
    .println("\\par =================================================================================================");
   if (Truline.userProps.getProperty("ArtAndKim", "N").equals("Y")) {
    int cnt = 20-race.cntRaceFlowsAK;
    while (cnt <= 20) {
     out.println(race.raceFlowsAK[cnt]);
     cnt++;
    }
   }
   else {
    int cnt = 20-race.cntRaceFlows;
    while (cnt <= 20) {
     out.println(race.raceFlows[cnt]);
     cnt++;
    }
   }
  }
  out
    .println("\\par ===================================================================================================");
  out
    .println("\\par       #  Horse              RS RR      EPS  EN    FS   TT    SS   CS    FT  AS  RE QP TP  ML  ODDS");
  if (Truline.userProps.getProperty("TrackTheBias", "N").equals("1")
    || Truline.userProps.getProperty("TrackTheBias", "N").equals("2")) {
   biasBonus = 0;
   biasBonusD = 0;
   for (int i = 0; i < 10; i++) {
    if (race.m_surface.equals("D") || race.m_surface.equals("A")) {
      biasPts[i] = "" + biasPoints[i];
      biasBonus = biasBonus + biasPoints[i];
      if (race.m_distance < 1760) {
       biasPtsD[i] = "" + biasPointsSP[i];
       biasBonusD = biasBonusD + biasPointsSP[i];
      }
      else {
       biasPtsD[i] = "" + biasPointsRT[i];
       biasBonusD = biasBonusD + biasPointsRT[i];
      }
     }
    else {
      biasPts[i] = "" + biasPointsT[i];
      biasBonus = biasBonus + biasPointsT[i];
      if (race.m_distance < 1760) {
       biasPtsD[i] = "" + biasPointsTSP[i];
       biasBonusD = biasBonusD + biasPointsTSP[i];
      }
      else {
       biasPtsD[i] = "" + biasPointsTRT[i];
       biasBonusD = biasBonusD + biasPointsTRT[i];
      }
     }
   }
   biasPts[10] = "" + biasBonus;
   biasPtsD[10] = "" + biasBonusD;
   out
   .println("\\par          Total Bias                    "
     + Lib.rjust(biasPts[Handicap.EPS], 3)
     + Lib.rjust(biasPts[Handicap.EN], 4) + Lib.rjust(biasPts[Handicap.FS], 6)
     + Lib.rjust(biasPts[Handicap.TT], 5) + Lib.rjust(biasPts[Handicap.SS], 6)
     + Lib.rjust(biasPts[Handicap.CS], 5) + Lib.rjust(biasPts[Handicap.FT], 6)
     + Lib.rjust(biasPts[Handicap.AS], 4) + Lib.rjust(biasPts[Handicap.RE], 4)
     + Lib.rjust(biasPts[Handicap.QP], 3) + Lib.rjust(biasPts[10], 3)
     );
   out
   .println("\\par          Total Bias (distance)         "
     + Lib.rjust(biasPtsD[Handicap.EPS], 3)
     + Lib.rjust(biasPtsD[Handicap.EN], 4) + Lib.rjust(biasPtsD[Handicap.FS], 6)
     + Lib.rjust(biasPtsD[Handicap.TT], 5) + Lib.rjust(biasPtsD[Handicap.SS], 6)
     + Lib.rjust(biasPtsD[Handicap.CS], 5) + Lib.rjust(biasPtsD[Handicap.FT], 6)
     + Lib.rjust(biasPtsD[Handicap.AS], 4) + Lib.rjust(biasPtsD[Handicap.RE], 4)
     + Lib.rjust(biasPtsD[Handicap.QP], 3) + Lib.rjust(biasPtsD[10], 3)
     );
  }
  out.println("\\par ");
  line += 9;
  // Display each horse in the race.
  for (Enumeration e = race.m_posts.elements(); e.hasMoreElements();) {
   biasBonus = 0;
   biasPts[10] = "";
   biasBonusD = 0;
   biasPtsD[10] = "";
   Post post = (Post) e.nextElement();
   String bold_cloth = "\\b"+post.cloth+"\\b0";
   String entry = post.m_props.getProperty("ENTRY", "");
   if (entry.equals("S")) {
    out.println("\\par      " + Lib.pad(post.cloth, 4)
      + Lib.pad(post.m_horseName, 16) + "      SCRATCHED");
    line++;
    continue;
   }
   if (post.m_handicap == null || post.m_horseName == null)
    continue; // position is empty
   String finishPos = post.m_finishPos;
   if (finishPos.equals("1") || finishPos.equals("2") || finishPos.equals("3")
     || finishPos.equals("4"))
    finishPosPrt = "(" + finishPos + ")";
   else
    finishPosPrt = "";
   if (Truline.userProps.getProperty("TrackTheBias", "N").equals("1")
     || Truline.userProps.getProperty("TrackTheBias", "N").equals("2")) {
    // See if horse gets any bias points
    biasBonus = 0;
    biasBonusD = 0;
    for (int i = 0; i < 10; i++) {
     if (race.m_surface.equals("D") || race.m_surface.equals("A")) {
      biasPts[i] = "";
      biasPtsD[i] = "";
      if (post.m_handicap.rank[i] == 1 && biasPoints[i] > 0) {
       biasPts[i] = "" + biasPoints[i];
       biasBonus = biasBonus + biasPoints[i];
       if (race.m_distance < 1760) {
        biasPtsD[i] = "" + biasPointsSP[i];
        biasBonusD = biasBonusD + biasPointsSP[i];
       }
       else {
        biasPtsD[i] = "" + biasPointsRT[i];
        biasBonusD = biasBonusD + biasPointsRT[i];
       }
      }
      if (Truline.userProps.getProperty("TrackTheBias", "N").equals("2")
        && post.m_handicap.rank[i] == 2 && biasPoints[i] > 0) {
       biasPts[i] = "" + biasPoints[i];
       biasBonus = biasBonus + biasPoints[i];
       if (race.m_distance < 1760) {
        biasPtsD[i] = "" + biasPointsSP[i];
        biasBonusD = biasBonusD + biasPointsSP[i];
       }
       else {
        biasPtsD[i] = "" + biasPointsRT[i];
        biasBonusD = biasBonusD + biasPointsRT[i];
       }
      }
     }
     else {
      biasPts[i] = "";
      biasPtsD[i] = "";
      if (post.m_handicap.rank[i] == 1 && biasPointsT[i] > 0) {
       biasPts[i] = "" + biasPointsT[i];
       biasBonus = biasBonus + biasPointsT[i];
       if (race.m_distance < 1760) {
        biasPtsD[i] = "" + biasPointsTSP[i];
        biasBonusD = biasBonusD + biasPointsTSP[i];
       }
       else {
        biasPtsD[i] = "" + biasPointsTRT[i];
        biasBonusD = biasBonusD + biasPointsTRT[i];
       }
      }
      if (Truline.userProps.getProperty("TrackTheBias", "N").equals("2")
        && post.m_handicap.rank[i] == 2 && biasPointsT[i] > 0) {
       biasPts[i] = "" + biasPointsT[i];
       biasBonus = biasBonus + biasPointsT[i];
       if (race.m_distance < 1760) {
        biasPtsD[i] = "" + biasPointsTSP[i];
        biasBonusD = biasBonusD + biasPointsTSP[i];
       }
       else {
        biasPtsD[i] = "" + biasPointsTRT[i];
        biasBonusD = biasBonusD + biasPointsTRT[i];
       }
}
     }
    }
    if (biasBonus > 0) {
     biasPts[10] = "Bs=" + biasBonus + "/" + biasBonusD;
     biasPts[11] = "Bias";
     post.m_bias = "Bias=" + biasBonus + "/" + biasBonusD;
     post.m_biasN = biasBonus;
    }
    else {
     biasPts[10] = "";
     biasPts[11] = "";
     post.m_bias = "";
     post.m_biasN = 0;
    }
     
   }
   String repRaceDate;
   if (post.m_handicap.m_repRace != null)
    repRaceDate = Lib.datetoa(post.m_handicap.m_repRace.ppRaceDate);
   else
    repRaceDate = "00/00";
   out.println("\\par " + Lib.pad(post.m_sireTSp, 1)
     + Lib.pad(post.m_sireTS2, 1) + " " 
     + Lib.pad(post.m_5furlongBullet, 1)
     + Lib.pad(post.m_ownerTrn, 1)
     + Lib.pad(post.cloth, 3) + Lib.pad(post.m_ownerBrd, 1) + Lib.pad(post.m_horseNameP, 19)
     + Lib.pad(post.m_runStyle, 3) + Lib.pad(repRaceDate, 5)
     + Lib.rjust(post.m_handicap.value[Handicap.EPS], 6)
     + Lib.rjust(post.m_handicap.value[Handicap.EN], 4, 1)
     + ((post.m_handicap.extraFlg) ? "#" : " ")
     + Lib.rjust(post.m_handicap.value[Handicap.FS], 5, 1)
     + Lib.rjust(post.m_handicap.value[Handicap.TT], 5, 1)
     + Lib.rjust(post.m_handicap.value[Handicap.SS], 6, 1)
     + Lib.rjust(post.m_handicap.value[Handicap.CS], 5, 1)
     + Lib.rjust(post.m_handicap.value[Handicap.FT], 6, 1)
     + Lib.rjust(post.m_handicap.value[Handicap.AS], 4)
     + Lib.rjust(post.m_handicap.value[Handicap.RE], 4)
     + Lib.rjust(post.m_handicap.value[Handicap.QP], 3)
     + Lib.rjust(post.m_handicap.bonus + post.m_handicap.points, 3)
     + Lib.rjust(post.m_morningLine, 5) + Lib.rjust(post.m_odds, 6));
   line++;
   int tstart, tplace, twin, jstart, jplace, jwin;
   tstart = Lib.atoi(post.m_props.getProperty("TRAINERSTARTS"));
   tplace = Lib.atoi(post.m_props.getProperty("TRAINERPLACES"));
   twin = Lib.atoi(post.m_props.getProperty("TRAINERWINS"));
   jstart = Lib.atoi(post.m_props.getProperty("JOCKEYSTARTS"));
   jplace = Lib.atoi(post.m_props.getProperty("JOCKEYPLACES"));
   jwin = Lib.atoi(post.m_props.getProperty("JOCKEYWINS"));
/*
   int jpcnt = (jstart > 0) ? (jwin + jplace) * 100 / jstart : 0;
   int tpcnt = (tstart > 0) ? (twin + tplace) * 100 / tstart : 0;
*/   
   int jpcnt = (jstart > 0) ? (jwin) * 100 / jstart : 0;
   int tpcnt = (tstart > 0) ? (twin) * 100 / tstart : 0;
   out.println("\\par " + Lib.pad(post.m_sireTSPI3, 2) +  Lib.pad(post.m_betfactorsPR, 3) + "  "
     + Lib.pad(post.m_trainerNamePT, 2)
     + Lib.pad(post.m_props.getProperty("TRAINER", "").toLowerCase(), 14)
     + Lib.pad(post.m_trnfactorsPR, 8)
     + Lib.rjust((double) tstart, 3) + "/"
     + Lib.pad(Lib.ftoa((double) tpcnt, 0) + "%", 4)
     + Lib.rjust(post.m_handicap.rank[Handicap.EPS], 3)
     + Lib.rjust(post.m_handicap.rank[Handicap.EN], 4)
     + Lib.rjust(post.m_handicap.rank[Handicap.FS], 6)
     + Lib.rjust(post.m_handicap.rank[Handicap.TT], 5)
     + Lib.rjust(post.m_handicap.rank[Handicap.SS], 6)
     + Lib.rjust(post.m_handicap.rank[Handicap.CS], 5)
     + Lib.rjust(post.m_handicap.rank[Handicap.FT], 6)
     + Lib.rjust(post.m_handicap.rank[Handicap.AS], 4)
     + Lib.rjust(post.m_handicap.rank[Handicap.RE], 4)
     + Lib.rjust(post.m_handicap.rank[Handicap.QP], 3)
     + Lib.rjust(post.m_handicap.bonusRank, 3) + Lib.rjust(finishPosPrt, 5));
   out.println("\\par " + Lib.pad(post.m_trnJkyPct+post.m_trnJky, 8) + " "
     + Lib.pad(post.m_props.getProperty("JOCKEY", "").toLowerCase(), 14)
     + Lib.pad(post.m_jkyfactorsPR, 8)
     + Lib.rjust((double) jstart, 3) + "/"
     + Lib.pad(Lib.ftoa((double) jpcnt, 0) + "%", 4)
     + Lib.rjust(biasPts[Handicap.EPS], 3)
     + Lib.rjust(biasPts[Handicap.EN], 4) + Lib.rjust(biasPts[Handicap.FS], 6)
     + Lib.rjust(biasPts[Handicap.TT], 5) + Lib.rjust(biasPts[Handicap.SS], 6)
     + Lib.rjust(biasPts[Handicap.CS], 5) + Lib.rjust(biasPts[Handicap.FT], 6)
     + Lib.rjust(biasPts[Handicap.AS], 4) + Lib.rjust(biasPts[Handicap.RE], 4)
     + Lib.rjust(biasPts[Handicap.QP], 3) + Lib.rjust(biasPts[10], 8));
     // + Lib.rjust(biasPts[11], 5));
   line++;
  }
  out
    .println("\\par  # = Must Bet Energy or Power Trainer / $ = Turf Sire / d = Dam Sire / * = Trainer-Owner");
  out.println("\\par ");
  out
    .println("\\par ==================================== Recap of Top Ranked Horses ===================================");
  if (Truline.userProps.getProperty("ShowTidbits", "N").equals("Y")) {
   if (race.m_bettable1 == "N")
    out.println("\\par *** NON-BETTABLE RACE ***");
   else if (race.m_bettable2 == "N")
   {
    out.println("\\par *** LOW PROBABILITY RACE - " + race.m_cntnrl
      + " horses have no running lines - (" + Lib.ftoa(race.m_pctNRL, 0) + "%) ***");
    if (race.m_cnt1st > 0 && race.m_cntnrl > 0)
     out.println("\\par *** CAUTION - " + race.m_cnt1st + " first time starter(s)");
    }
   // else if (race.m_surface.equals("T"))
   // out.println("\\par *** CAUTION - Turf and "+race.m_cntnrl+" horses have no running lines ***");
   else if (race.m_cnthorses < 8)
   {
    out.println("\\par *** Double overlay betting only - " + race.m_cnthorses
      + " horses in race - " + race.m_cntnrl + " have no running line - (" + Lib.ftoa(race.m_pctNRL, 0) + "%)");
    if (race.m_cnt1st > 0 && race.m_cntnrl > 0)
     out.println("\\par *** CAUTION - " + race.m_cnt1st + " first time starter(s)");
   }
   else if (race.m_cntnrl > 2)
   {
    out.println("\\par *** CAUTION - " + race.m_cntnrl + " horses have no running line - (" + Lib.ftoa(race.m_pctNRL, 0) + "%)");
    if (race.m_cnt1st > 0 && race.m_cntnrl > 0)
     out.println("\\par *** CAUTION - " + race.m_cnt1st + " first time starter(s)");
   }
   else if (race.m_cnt1st > 0)
    out.println("\\par *** CAUTION - " + race.m_cnt1st + " first time starter(s)");
   else
    out.println("\\par *** PRIME BETTING RACE ***");
  }
  // Display body language hints
  if (Truline.userProps.getProperty("ShowBodyLanguage", "N").equals("Y")) {
   switch (sexAge.charAt(0)) {
    case 'A':
     out.print("\\par 2 year old ");
     switch (sexAge.charAt(2)) {
      case 'N':
       out.println("Males B/L = Calm / Fillies = Calm and Fat");
       break;
      case 'C':
       out.println("Males B/L = Calm");
       break;
      case 'F':
       out.println("Fillies B/L = Calm and Fat");
       break;
     }
     break;
    case 'B':
    case 'E':
    case 'G':
    case 'H':
     out.print("\\par 3 year old ");
     switch (sexAge.charAt(2)) {
      case 'N':
       out.println("Males B/L = Calm / Fillies = Calm and Fat");
       break;
      case 'C':
       out.println("Males B/L = Calm");
       break;
      case 'M':
      case 'F':
       out.println("Fillies B/L = Calm and Fat");
       break;
     }
     switch (sexAge.charAt(1)) {
      case 'O':
       break;
      case 'U':
       out.print("\\par 4 year old and up ");
       switch (sexAge.charAt(2)) {
        case 'N':
         out.println("Males B/L = Prancing / Mares B/L = Prancing and Fat");
         break;
        case 'C':
         out.println("Males B/L = Prancing");
         break;
        case 'M':
        case 'F':
         out.println("Mares B/L = Prancing and Fat");
         break;
       }
       break;
     }
     break;
    case 'C':
    case 'D':
    case 'F':
     out.print("\\par 4 year old and up ");
     switch (sexAge.charAt(2)) {
      case 'N':
       out.println("Males B/L = Prancing / Mares B/L = Prancing and Fat");
       break;
      case 'C':
       out.println("Males B/L = Prancing");
       break;
      case 'M':
      case 'F':
       out.println("Mares B/L = Prancing and Fat");
       break;
     }
     break;
   }
  }
  line += 3;
  String[] odds = { "8-5 ", "5-2 ", "6-1 ", "9-1 ", "20-1", "30-1" };
  int i = 0;
  int j = 0;
  int pts = 999;
  int ml;
  String DO;
  String Adv20;
  for (Enumeration e = race.ranking.elements(); e.hasMoreElements();) {
   if (i >= 25) // show only the first 6
    break;
   Post post = (Post) e.nextElement();
   if (i < 6 && post.m_handicap.bonus + post.m_handicap.points < pts) {
    pts = post.m_handicap.bonus + post.m_handicap.points;
    j = i;
   }
   ml = Lib.atoi(post.m_props.getProperty("MORNINGLINE"));
   DO = "";
   Adv20 = "";
   switch (j) {
    case 0:
     if (ml >= 4)
      DO = " DO";
     if (post.m_pointsAdv > 19 && race.m_cnthorses > 6 && race.m_cntnrl < 4
       && race.m_cntnrlML == 0)
      Adv20 = "**";
     break;
    case 1:
     if (ml >= 6)
      DO = " DO";
     break;
    case 2:
     if (ml >= 12)
      DO = " DO";
     break;
    case 3:
     if (ml >= 30)
      DO = " DO";
     break;
    case 4:
     if (ml >= 30)
      DO = " DO";
     break;
    case 5:
     if (ml >= 30)
      DO = " DO";
     break;
   }
   if (i >= 5 && post.m_biasN == 0 && post.m_handicap.bonus + post.m_handicap.points < pts)
    continue;
   else {
   // if (Truline.userProps.getProperty("Experimental", "N").equals("Yes")) {
    if (i == 1) {
     out.print("\\par Track Condition __________ ");
     line++;
    } else if (i == 4) {
     out.print("\\par Final Fraction  __________ ");
     line++;
    } else {
     out.print("\\par                            ");
     line++;
    }
    out.println(Lib.pad(post.m_sireTSp, 1) + Lib.pad(post.m_sireTS2, 1)
      + Lib.pad(post.m_trainerNamePT.substring(1), 1)
      + Lib.pad(post.m_5furlongBullet, 1) + Lib.pad(post.m_ownerTrn, 1)
      + Lib.pad(post.cloth, 3) + Lib.pad(post.m_ownerBrd, 1) + Lib.pad(post.m_horseName, 16)
      + Lib.rjust(post.m_handicap.bonus + post.m_handicap.points, 5) + " " + Lib.pad(post.m_runStyle, 3)
      + Lib.rjust(post.m_truLine, 6)
      + Lib.pad("(" + post.m_morningLine + post.m_truLineDO + ")" + Adv20, 15)
      + Lib.pad(post.m_bias, 10));
   /*
   }
   else {
    if (i == 1) {
     out.print("\\par Track Condition ___________ ");
     line++;
    } else if (i == 4) {
     out.print("\\par Final Fraction  ___________ ");
     line++;
    } else {
     out.print("\\par                             ");
     line++;
    }
    out.println(Lib.pad(post.m_sireTS, 1) + Lib.pad(post.m_sireTS2, 1)
      + Lib.pad(post.m_trainerNamePT.substring(1), 1) + Lib.pad(post.m_ownerTrn, 1)
      + Lib.pad(post.cloth, 3) + Lib.pad(post.m_ownerBrd, 1) + Lib.pad(post.m_horseName, 16)
      + Lib.rjust(post.m_handicap.bonus + post.m_handicap.points, 5) + " " + Lib.pad(post.m_runStyle, 3)
      + Lib.rjust(((j < 6) ? odds[j] : ""), 6)
      + Lib.pad("(" + post.m_morningLine + DO + ")" + Adv20, 15)
      + Lib.pad(post.m_bias, 10));
   }
   */
   }
   i++;
  }
  // Display body language hints
  if (Truline.userProps.getProperty("ShowStats", "N").equals("Y"))
   try {
    out.println("\\par ");
    out
      .println("\\par ========================================== Trainer / Jockey Stats =================================");
    out.println("\\par ");
    // Display stats for each horse.
    out.println("\\par                    " + Lib.pad("Category", 18)
      + Lib.pad("STS", 6) + Lib.pad("Win%", 6) + Lib.pad("ITM%", 6)
      + Lib.pad("ROI", 7));
    for (Enumeration e = race.m_posts.elements(); e.hasMoreElements();) {
     Post post = (Post) e.nextElement();
     String cat = "";
     int sts = 0;
     int win = 0;
     int itm = 0;
     String roi = "";
     String entry = post.m_props.getProperty("ENTRY", "");
     if (!entry.equals("S")) {
      out.println("\\par " + Lib.pad(post.cloth, 4)
        + Lib.pad(post.m_horseNameP, 18) + " Trainer / Jockey Statistics");
      for (Enumeration e1 = post.m_trainerJockeyStats.elements(); e1
        .hasMoreElements();) {
       TrainerJockeyStats tjs = (TrainerJockeyStats) e1.nextElement();
       
       for (int k = 1; k < 7; k++) {
        cat = tjs.m_props.getProperty("TRAINERCAT"+k, "N/A");
        win = Lib.atoi(tjs.m_props.getProperty("TRAINERWIN"+k, "0"));
        itm = Lib.atoi(tjs.m_props.getProperty("TRAINERITM"+k, "0"));
        roi = tjs.m_props.getProperty("TRAINERROI"+k, "XXX");
        if ((!cat.equals("N/A") && (!roi.substring(0, 1).equals("-"))
          && ((win > 19) || (itm > 49) || (Lib.atoi(roi.substring(0, 1)) > 2)))) {
         out.println("\\par  "
           + Lib.pad(post.m_props.getProperty("TRAINER", "").toLowerCase(), 17)
           + " " + Lib.pad(tjs.m_props.getProperty("TRAINERCAT"+k, "Ns/A"), 18)
           + Lib.pad(tjs.m_props.getProperty("TRAINERSTS"+k, " "), 6)
           + Lib.pad(tjs.m_props.getProperty("TRAINERWIN"+k, " "), 6)
           + Lib.pad(tjs.m_props.getProperty("TRAINERITM"+k, " "), 6)
           + Lib.pad(tjs.m_props.getProperty("TRAINERROI"+k, " "), 7));
        }
       }
              
       sts = Lib.atoi(tjs.m_props.getProperty("JOCKEYDISTSTS", "0"));
       win = Lib.atoi(tjs.m_props.getProperty("JOCKEYDISTWIN", "0"));
       itm = Lib.atoi(tjs.m_props.getProperty("JOCKEYDISTWIN", "0"))
         + Lib.atoi(tjs.m_props.getProperty("JOCKEYDISTPLC", "0"))
         + Lib.atoi(tjs.m_props.getProperty("JOCKEYDISTSHW", "0"));
       if (sts > 0) {
        win = (sts > 0) ? (win * 100) / sts : 0;
        itm = (sts > 0) ? (itm * 100) / sts : 0;
       }
       out.println("\\par  "
         + Lib.pad(post.m_props.getProperty("JOCKEY", "").toLowerCase(), 17)
         + " " + Lib.pad(tjs.m_props.getProperty("JOCKEYDISTCAT", "N/A"), 18)
         + Lib.pad(tjs.m_props.getProperty("JOCKEYDISTSTS", " "), 6)
         + Lib.pad(win, 6) + Lib.pad(itm, 6)
         + Lib.pad(tjs.m_props.getProperty("JOCKEYDISTROI", ""), 7));
      }
     }
    }
   } catch (Exception e1) {
    Log.print("Exception Writing Statistics: " + e1 + "\n");
   }
  out.println("\\par ");
  out
    .println("\\par ============================================== Race Payoffs =======================================");
  out.println("\\par ");
  if (race.m_resultsPosted.equals("Y")) {
   out.println("\\par            1st  " + Lib.rjust(race.m_cloth1, 3)
     + Lib.rjust(race.m_win1, 8) + Lib.rjust(race.m_place1, 8)
     + Lib.rjust(race.m_show1, 8));
   out.println("\\par ");
   out.println("\\par            2nd  " + Lib.rjust(race.m_cloth2, 3)
     + Lib.rjust(race.m_win2, 8) + Lib.rjust(race.m_place2, 8)
     + Lib.rjust(race.m_show2, 8));
   out.println("\\par ");
   out.println("\\par            3rd  " + Lib.rjust(race.m_cloth3, 3)
     + Lib.rjust(race.m_win3, 8) + Lib.rjust(race.m_place3, 8)
     + Lib.rjust(race.m_show3, 8));
   out.println("\\par ");
   out.println("\\par            EX " + Lib.rjust(race.m_exactaPayoff, 17)
     + "  PICK3 " + Lib.rjust(race.m_pick3Payoff, 11) + "  SFCTA "
     + Lib.rjust(race.m_superPayoff, 11));
   out.println("\\par ");
   out.println("\\par            TRIFECTA "
     + Lib.rjust(race.m_trifectaPayoff, 11) + "  PICK4 "
     + Lib.rjust(race.m_pick4Payoff, 11) + "  DD "
     + Lib.rjust(race.m_doublePayoff, 14));
   out.println("\\par ");
   out.println("\\par            PICK5    "
     + Lib.rjust(race.m_pick5Payoff, 11) + "  PICK6 "
     + Lib.rjust(race.m_pick6Payoff, 11));
  } else {
   out
     .println("\\par            1st  ____   ______________   ______________  _______________");
   out.println("\\par ");
   out
     .println("\\par            2nd  ____                    ______________  _______________");
   out.println("\\par ");
   out
     .println("\\par            3rd  ____                                    _______________");
   out.println("\\par ");
   out
     .println("\\par            EX ________________  QU_______________  PICK3_______________");
   out.println("\\par ");
   out
     .println("\\par            TRIFECTA __________  DD ______________  OTHER ______________");
  }
  out
    .println("\\par ===================================================================================================");
  out.println("\\par ");
  line += 14;
  out.println("\\page "); // Next page
  // line = line % pagesize;
  // while(line < pagesize)
  // {
  // out.println("\\par ");
  // line++;
  // }
 } catch (Exception e) {
  Log.print("Exception Writing report: " + e + "\n");
 }
}
/* Print Horse Flow Report */
public void writeHFReport(PrintWriter out, Race race)
{
 DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
 DateFormat dtf = DateFormat.getDateTimeInstance(DateFormat.SHORT,
   DateFormat.SHORT);
 String datestr = dtf.format(new Date());
 int line = 0;
 String FB1;
 String FB2;
 try {
  out.println("\\par                          " + Truline.title
    + "                " + datestr);
  out.println("\\par [" + Truline.version + "] " + Truline.copyright);
  out
    .println("\\par ===============================================================================================");
  out.println("\\par Track "
    + Lib.pad(race.m_track, 3)
    + "    "
    + Lib.datetoa(race.m_raceDate)
    + "    Race#"
    + race.m_raceNo
    + "   Distance "
    + Lib.ftoa(((double) race.m_distance) / Handicap.YdPerF, 1)
    + "F"
    + "  Type "
    + ((race.m_raceType.equals("G1")) ? "G1-Stake I" : (race.m_raceType
      .equals("G2")) ? "G2-Stake II"
      : (race.m_raceType.equals("G3")) ? "G3-Stake III" : (race.m_raceType
        .equals("N")) ? "N-nongraded stake"
        : (race.m_raceType.equals("A")) ? "A-allowance" : (race.m_raceType
          .equals("R")) ? "R-Starter Alw"
          : (race.m_raceType.equals("T")) ? "T-Starter Hcp" : (race.m_raceType
            .equals("C")) ? "C-claiming"
            : (race.m_raceType.equals("S")) ? "S-mdn sp wt" : (race.m_raceType
              .equals("M")) ? "M-mdn claimer" : race.m_raceType));
  NumberFormat fmt = NumberFormat.getCurrencyInstance();
  out.println("\\par "
    + ((race.m_claim != 0) ? ("Claim " + fmt.format(race.m_claim) + "   Purse " + fmt.format(race.m_purse))
      : (race.m_purse != 0) ? ("Purse " + fmt.format(race.m_purse))
        : "                ") + "  Surface "
    + ((race.m_surface.equals("D")) ? "Dirt" :
       (race.m_surface.equals("A")) ? "All Weather" :"Turf")
    + "  Handicapped for "+race.m_trackCond+" track");
  String sexAge = race.m_props.getProperty("AGESEX", "");
  out.print("\\par AGE/SEX (" + sexAge + ")");
  switch (sexAge.charAt(0)) {
   case 'A':
    out.print(" 2 year olds");
    break;
   case 'B':
    out.print(" 3 year olds");
    break;
   case 'C':
    out.print(" 4 year olds");
    break;
   case 'D':
    out.print(" 5 year olds");
    break;
   case 'E':
    out.print(" 3 & 4 year olds");
    break;
   case 'F':
    out.print(" 4 & 5 year olds");
    break;
   case 'G':
    out.print(" 3, 4, and 5 year olds");
    break;
   case 'H':
    out.print(" all ages");
    break;
  }
  switch (sexAge.charAt(1)) {
   case 'O':
    out.print(", That age Only");
    break;
   case 'U':
    out.print(", That age and Up");
    break;
  }
  switch (sexAge.charAt(2)) {
   case 'N':
    out.print(", No Sex Restrictions");
    break;
   case 'M':
    out.print(", Mares and Fillies");
    break;
   case 'C':
    out.print(", Colts and/or Geldings");
    break;
   case 'F':
    out.print(", Fillies Only");
    break;
  }
  out.println();
  out.println("\\par          Run Style Profile  " + race.m_runStyleProfile);
  if (Truline.userProps.getProperty("Experimental", "N").equals("Yes")) {
   if (race.cntRaceFlows >= 0)
    out
    .println("\\par =================================================================================================");
   if (Truline.userProps.getProperty("ArtAndKim", "N").equals("Y")) {
    int cnt = 20-race.cntRaceFlowsAK;
    while (cnt <= 20) {
     out.println(race.raceFlowsAK[cnt]);
     cnt++;
    }
   }
   else {
    int cnt = 20-race.cntRaceFlows;
    while (cnt <= 20) {
     out.println(race.raceFlows[cnt]);
     cnt++;
    }
   }
  }
  out
  .println("\\par =================================================================================================");
  if (race.m_cntnrl > 0) {
    out.println("\\par *** CAUTION - " + race.m_cntnrl + " horses have no running line - (" + Lib.ftoa(race.m_pctNRL, 0) + "%)");
   if (race.m_cnt1st > 0)
    out.println("\\par *** CAUTION - " + race.m_cnt1st + " first time starter(s)");
   out
   .println("\\par =============================================================================================");
  }
  out.println("\\par    #   Horse               PI INDICATORS                     Truline     ML     BIAS  FP   ODDS");
  out.println("\\par ");
  line += 9;

  rankBiasPoints(race);

  // Display each horse in the race.
  for (Enumeration e = race.m_posts.elements(); e.hasMoreElements();) {
   Post post = (Post) e.nextElement();
   String entry = post.m_props.getProperty("ENTRY", "");
   if (entry.equals("S"))
    continue;
   if (post.m_handicap == null || post.m_horseName == null)
    continue; // position is empty
   if (post.cntHorseFlows < 0) {
     //  No flows for this horse
    post.cntHorseFlows = 0;
    post.horseFlows[0] = "                                ";
   }
   String finishPos = post.m_finishPos;
   if (finishPos.equals("1") || finishPos.equals("2") || finishPos.equals("3")
     || finishPos.equals("4"))
    finishPosPrt = finishPos;
   else
    finishPosPrt = "";

   setKimsTrainerFlags(post);
   
   String kimsSummary = "TL"+post.m_handicap.bonusRank+
     (post.m_biasRank == 0 ? "" : "+BR"+post.m_biasRank)+
     (post.m_5furlongBullet.equals("") ? "" : "+"+post.m_5furlongBullet)+
     "+RS-"+post.m_runStyle.trim()+
     (post.m_kimsEPS.equals("") ? "" : "+"+post.m_kimsEPS)+
     (post.m_kimsPT.equals("") ? "" : "+"+post.m_kimsPT)+
     (post.m_kimsT1.equals("") ? "" : "+"+post.m_kimsT1)+
     (post.m_kimsT2.equals("") ? "" : "+"+post.m_kimsT2)+
     (post.m_kimsT3.equals("") ? "" : "+"+post.m_kimsT3)+
     (post.m_kimsT4.equals("") ? "" : "+"+post.m_kimsT4)+
     (post.m_kimsTOB.equals("") ? "" : "+"+post.m_kimsTOB)+
     (post.m_kimsJ1.equals("") ? "" : "+"+post.m_kimsJ1)+
     (post.m_kimsTT.equals("") ? "" : "+"+post.m_kimsTT)+
     (post.m_kimsCS.equals("") ? "" : "+"+post.m_kimsCS)+
     (post.m_kimsTTCS.equals("") ? "" : "+"+post.m_kimsTTCS)+
     (post.m_kimsPP.equals("") ? "" : "+"+post.m_kimsPP)+
     (post.m_sireTSp.equals(" ") && (post.m_sireTS2.equals(" ") | post.m_sireTS2.equals("-")) && post.m_sireTSPI3.equals(" ") ? "" 
       : "+\\b Breeding="+post.m_sireTSp+post.m_sireTS2+post.m_sireTSPI3+" \\b0");
   
   String horseRank = "" + post.m_handicap.bonusRank; 
   String horsePoints = "" + (post.m_handicap.bonus + post.m_handicap.points);
   if (post.m_whereBred.length() > 2) {
    FB1 = "\\b ";
    FB2 = "\\b0 ";
   }
   else {
    FB1 = "";
    FB2 = "";
   }
   String DO1 = "", DO2 = "   ", PAD1 = "";
   if (post.m_truLineDO.equals("DO")) {
    DO1 = "\\b ";
    DO2 = "\\b0 ";
   }
    else {
     DO1 = "\\b \\b0 ";
     DO2 = "  ";
    }
//   if (post.horseFlows[0].substring(0,2).equals("\\b")) {
//    PAD1 = "       ";
//   }
   out.println("\\par " + Lib.pad(post.m_sireTSp, 1)
     + Lib.pad(post.m_sireTS2, 1) + Lib.pad(post.m_5furlongBullet, 1) + Lib.pad(post.m_ownerTrn, 1)
     + Lib.pad(post.cloth, 3) + Lib.pad(post.m_ownerBrd, 1) + FB1 + Lib.pad(post.m_horseNameP, 19) + FB2
     // + (post.m_repRaceDtl+"                                  ").substring(0, 34)
     + "  RS=" + Lib.pad(post.m_runStyle, 3) + "                          "
     + Lib.pad(horseRank+"/"+horsePoints+"/"+post.m_truLine, 10) 
     + Lib.rjust(DO1+post.m_morningLine+post.m_truLineDO+DO2,14) 
     + Lib.rjust(post.m_bias, 6) 
     + Lib.rjust(finishPosPrt, 4)
     + Lib.rjust(post.m_odds, 7));
   line++;
   
   if (Truline.userProps.getProperty("Experimental", "N").equals("Yes")) {
   /*  Print Kims Summary */
    out.println("\\par             "
       + "Kims Summary="+kimsSummary
       );
   }
   
   /*  Print Fresh Horse Bonus Factor */
   if (post.m_freshHorse && Truline.userProps.getProperty("ArtAndKim", "N").equals("Y"))
    out.println("\\par             "
       + "\\b *** Fresh Horse Plus BONUS FACTOR *** \\b0 "
       );
    
   /*  Print Rep Race */
   out.println("\\par             "
      + post.m_repRaceDtl
      );
   
   /*  Print Purse Change */
   if (!post.m_repRacePurseComp.equals(""))
    out.println("\\par             "
      + "\\b "+post.m_repRacePurseComp+ "\\b0 "
      );
   if (!post.m_lastRacePurseComp.equals("")) {
    if (post.m_lastRacePurseComp.indexOf("CLASS DOWN") >= 0 && Truline.userProps.getProperty("ArtAndKim", "N").equals("Y") && post.m_trainerClsChgDownOK && post.m_trainerClsChgDownROI > 1.99)
     post.m_lastRacePurseComp = post.m_lastRacePurseComp + "- Trainer ROI is "+Lib.ftoa(post.m_trainerClsChgDownROI, 2)+" *** BONUS FACTOR *** \\b0"; 
    out.println("\\par             "
      + "\\b "+post.m_lastRacePurseComp+ "\\b0 "
      );
   }
   
   /*  Print Claim Change */
   if (!post.m_lastRaceClaimComp.equals("")) {
    out.println("\\par             "
      + "\\b "+post.m_lastRaceClaimComp+ "\\b0 "
      );
   }
   
   /*  Print Track Class Change */
   if (!post.m_lastRaceTrackClass.equals(""))
    out.println("\\par             "
      + "\\b "+post.m_lastRaceTrackClass+ "\\b0 "
      );
   
   /*  Print if more than 45 days since last race */
   if (post.m_daysSinceLast > 45)
    if (post.m_trainer45LayoffOK)
     out.println("\\par             "
       + "\\b Caution - More than 45 days since last race - "+post.m_daysSinceLast+" Days - but Trainer ROI is "+Lib.ftoa(post.m_trainer45LayoffROI, 2)+" \\b0"
       );
    else
     out.println("\\par             "
      + "\\b Caution - More than 45 days since last race - "+post.m_daysSinceLast+" Days \\b0"
      );
   
   /*  Print Form Cycle */
   out.println("\\par             "
     + post.m_formCycle
     );
   if (!post.m_formCycle2.equals("       "))
    out.println("\\par             "
      + post.m_formCycle2
      );
   if (!post.m_formCycle3.equals("       "))
    out.println("\\par             "
      + post.m_formCycle3
      );
   if (!post.m_formCycle4.equals("       "))
    out.println("\\par             "
      + post.m_formCycle4
      );
   if (!post.m_formCycle5.equals("       "))
    out.println("\\par             "
      + post.m_formCycle5
      );
   
   /*  Print Record at Today's Distance */
   int starts = Lib.atoi(post.m_props.getProperty("LRDSTARTS", "0"));
   int earnings = Lib.atoi(post.m_props.getProperty("LRDEARNINGS", "0"));
   int EPS = 0;
   if (starts > 0)
    EPS = earnings / starts;
   if (!post.m_props.getProperty("LRDWINS", "0").equals("0"))    
    out.println("\\b \\par             Lifetime at Today's Distance="
      + post.m_props.getProperty("LRDSTARTS", "0")+"/"
      + post.m_props.getProperty("LRDWINS", "0")+"/"
      + post.m_props.getProperty("LRDPLACES", "0")+"/"
      + post.m_props.getProperty("LRDSHOWS", "0")+"/"
      + post.m_props.getProperty("LRDEARNINGS", "0")
      + "/EPS=" + EPS + " \\b0"
       );
   else
    out.println("\\par             Lifetime at Today's Distance="
      + post.m_props.getProperty("LRDSTARTS", "0")+"/"
      + post.m_props.getProperty("LRDWINS", "0")+"/"
      + post.m_props.getProperty("LRDPLACES", "0")+"/"
      + post.m_props.getProperty("LRDSHOWS", "0")+"/"
      + post.m_props.getProperty("LRDEARNINGS", "0")
      + "/EPS=" + EPS
       );
    
   
   /*  Print Record on Turf */
   if (race.m_surface.equals("T") || race.m_surface.equals("A"))
   {
    starts = Lib.atoi(post.m_props.getProperty("LRTURFSTARTS", "0"));
    earnings = Lib.atoi(post.m_props.getProperty("LRTURFEARNINGS", "0"));
    EPS = 0;
    if (starts > 0)
     EPS = earnings / starts;
    out.println("\\par             Lifetime on Turf="
      + post.m_props.getProperty("LRTURFSTARTS", "0")+"/"
      + post.m_props.getProperty("LRTURFWINS", "0")+"/"
      + post.m_props.getProperty("LRTURFPLACES", "0")+"/"
      + post.m_props.getProperty("LRTURFSHOWS", "0")+"/"
      + post.m_props.getProperty("LRTURFEARNINGS", "0")
      + "/EPS=" + EPS
       );
   }
   
   /*  Print Record on All Weather Tracks */
   if (race.m_surface.equals("A"))
   {
    starts = Lib.atoi(post.m_props.getProperty("LRAWESTARTS", "0"));
    earnings = Lib.atoi(post.m_props.getProperty("LRAWEEARNINGS", "0"));
    EPS = 0;
    if (starts > 0)
     EPS = earnings / starts;
    out.println("\\par             Lifetime on All Weather Tracks="
      + post.m_props.getProperty("LRAWESTARTS", "0")+"/"
      + post.m_props.getProperty("LRAWEWINS", "0")+"/"
      + post.m_props.getProperty("LRAWEPLACES", "0")+"/"
      + post.m_props.getProperty("LRAWESHOWS", "0")+"/"
      + post.m_props.getProperty("LRAWEEARNINGS", "0")
      + "/EPS=" + EPS
       );
   }
   
   /*  Print Record on Fast Dirt Tracks */
   if (race.m_surface.equals("D") || race.m_surface.equals("A"))
   {
    starts = Lib.atoi(post.m_props.getProperty("LRDIRTSTARTS", "0"));
    earnings = Lib.atoi(post.m_props.getProperty("LRDIRTEARNINGS", "0"));
    EPS = 0;
    if (starts > 0)
     EPS = earnings / starts;
    out.println("\\par             Lifetime on Fast Dirt Tracks="
      + post.m_props.getProperty("LRDIRTSTARTS", "0")+"/"
      + post.m_props.getProperty("LRDIRTWINS", "0")+"/"
      + post.m_props.getProperty("LRDIRTPLACES", "0")+"/"
      + post.m_props.getProperty("LRDIRTSHOWS", "0")+"/"
      + post.m_props.getProperty("LRDIRTEARNINGS", "0")
      + "/EPS=" + EPS
       );
   }
   
   /*  Print Record on Wet Tracks */
   if (race.m_trackCond.equals("Off") && !race.m_surface.equals("A"))
   {
    starts = Lib.atoi(post.m_props.getProperty("LRWETSTARTS", "0"));
    earnings = Lib.atoi(post.m_props.getProperty("LRWETEARNINGS", "0"));
    EPS = 0;
    if (starts > 0)
     EPS = earnings / starts;
    out.println("\\par             Lifetime on Wet Tracks="
      + post.m_props.getProperty("LRWETSTARTS", "0")+"/"
      + post.m_props.getProperty("LRWETWINS", "0")+"/"
      + post.m_props.getProperty("LRWETPLACES", "0")+"/"
      + post.m_props.getProperty("LRWETSHOWS", "0")+"/"
      + post.m_props.getProperty("LRWETEARNINGS", "0")
      + "/EPS=" + EPS
       );
   }
    
   // Print other factors if present
   if (!post.m_otherFactors.equals(""))
    out.println("\\par             "
      + post.m_otherFactors
      );

   // Print Sire AWD data if present
   if (!post.m_sireTSPI2.equals(""))
    out.println("\\par                  "
      + post.m_sireTSPI2
      );

   /*  Print Trainer and Jockey and Base Stats */
   out.println("\\par             "
     + "T-J=" + post.m_props.getProperty("TRAINER", "").trim() + " / " + post.m_props.getProperty("JOCKEY", "").trim()
     );
   if (Truline.userProps.getProperty("ArtAndKim", "N").equals("Y")) {
    if (post.m_trnJkyPctF > post.m_trnPctF+5 && post.m_trnJkyPctF > post.m_jkyPctF+5
      && post.m_trnPctF > 20 && post.m_jkyPctF > 15)
     out.println("\\par             "
       + "    Top Trainer-Top Jockey Team - T-J="+post.m_trnJkyPctF+"% / T="+post.m_trnPctF+"% / J="+post.m_jkyPctF+"%   *** BONUS FACTOR ***"
       );
    if (post.m_jockeyTodayPrevWin)
     out.println("\\par             "
       + "    Jockey Change - Today's Jockey won previously on horse   *** BONUS FACTOR ***"
       );
   }
   if (!post.m_trnSurfaceStat.equals(""))
    out.println("\\par             "
      + "    Trainer Surface Stats Last year="
      + post.m_trnSurfaceStat
      );
   if (!post.m_trnMeetStatD.equals("")) {
    out.println("\\par             "
      + "    Trainer 60-day Meet Stats Dist="
      + post.m_trnMeetStatD
      );
    if (!post.m_trnMeetStatS.equals("")) {
     out.println("\\par             "
       + "                              Surf="
       + post.m_trnMeetStatS
       );
    }
   }
   else {
    if (!post.m_trnMeetStatS.equals("")) {
     out.println("\\par             "
       + "    Trainer 60-day Meet Stats Surf="
       + post.m_trnMeetStatS
       );
    }
   }
   if (!post.m_jkyMeetStatD.equals("")) {
    out.println("\\par             "
      + "    Jockey 60-day Meet Stats Dist="
      + post.m_jkyMeetStatD
      );
    if (!post.m_jkyMeetStatS.equals("")) {
     out.println("\\par             "
       + "                             Surf="
       + post.m_jkyMeetStatS
       );
    }
   }
   else {
    if (!post.m_jkyMeetStatS.equals("")) {
     out.println("\\par             "
       + "    Jockey 60-day Meet Stats Surf="
       + post.m_jkyMeetStatS
       );
    }
   }
   if (!post.m_trnJkyStat.equals(""))
    out.println("\\par             "
      + "    \\b T-J 1-Year Stats="
      + post.m_trnJkyStat + " \\b0"
      );
   if (Truline.userProps.getProperty("Experimental", "N").equals("Yes")) {
    if (!post.m_trnJkyTrkStat.equals(""))
     out.println("\\par             "
       + "    T-J Stats Last 12 Months="
       + post.m_trnJkyTrkStat
       );
    if (Truline.userProps.getProperty("ArtAndKim", "N").equals("Y")) {
     if (!post.m_trnJkyTrkL25Stat5.equals(""))
      if (post.m_last25)
       out.println("\\par             "
         + "    T-J Stats Last 5 Races="
         + post.m_trnJkyTrkL25Stat5
         );
      else
       out.println("\\par             "
         + "    T-J Stats Last 5 Races="
         + post.m_trnJkyTrkL25Stat5
         );
     if (!post.m_trnJkyTrkL25Stat10.equals(""))
      out.println("\\par             "
        + "    T-J Stats Last 10 Races="
        + post.m_trnJkyTrkL25Stat10
        );
    }
    if (!post.m_trnOwnStat.equals(""))
     out.println("\\par             "
       + "    T-O Stats Last 2 years="
       + post.m_trnOwnStat
       );
   }

   String cat = "";
   int sts = 0;
   int win = 0;
   int itm = 0;
   double roi = 0;
   for (Enumeration e1 = post.m_trainerJockeyStats.elements(); e1
     .hasMoreElements();) {
    TrainerJockeyStats tjs = (TrainerJockeyStats) e1.nextElement();
    
    for (int k = 1; k < 7; k++) {
     cat = tjs.m_props.getProperty("TRAINERCAT"+k, "N/A");
     win = Lib.atoi(tjs.m_props.getProperty("TRAINERWIN"+k, "0"));
     itm = Lib.atoi(tjs.m_props.getProperty("TRAINERITM"+k, "0"));
     roi = Lib.atof(tjs.m_props.getProperty("TRAINERROI"+k, "0"));
     if ((!cat.equals("N/A") && (roi > 0)
       && ((win > 24) || (itm > 59) || (roi > 1.99))))
      if (((win > 24 && roi > 1.99) || win > 29) && (Truline.userProps.getProperty("ArtAndKim", "N").equals("Y")) &&
         (cat.indexOf("Dwn 20Pct") >= 0 || cat.indexOf("3rdStart45") >= 0 ||
         cat.indexOf("Claimed 1bk") >= 0)) 
       out.println("\\par                            \\b "
         + Lib.pad(cat, 18)
         + Lib.pad(tjs.m_props.getProperty("TRAINERSTS"+k, " "), 6)
         + Lib.pad(Lib.ftoa((int) win,0)+'%', 6)
         + Lib.pad(Lib.ftoa((int) itm,0)+'%', 6)
         + Lib.pad("$"+Lib.ftoa((double) roi, 2), 7)+" *** BONUS FACTOR *** \\b0");
      else if (roi > 1.99)
       out.println("\\par                            \\b "
         + Lib.pad(cat, 18)
         + Lib.pad(tjs.m_props.getProperty("TRAINERSTS"+k, " "), 6)
         + Lib.pad(Lib.ftoa((int) win,0)+'%', 6)
         + Lib.pad(Lib.ftoa((int) itm,0)+'%', 6)
         + Lib.pad("$"+Lib.ftoa((double) roi, 2), 7)+" \\b0");
      else
       out.println("\\par                            "
         + Lib.pad(cat, 18)
         + Lib.pad(tjs.m_props.getProperty("TRAINERSTS"+k, " "), 6)
         + Lib.pad(Lib.ftoa((int) win,0)+'%', 6)
         + Lib.pad(Lib.ftoa((int) itm,0)+'%', 6)
         + Lib.pad("$"+Lib.ftoa((double) roi, 2), 7));
    }
   }

   
   
   String lastFlow = "";
   int cnt = 0;
   while (cnt <= post.cntHorseFlows) {
    // if (!lastFlow.equals(post.horseFlows[cnt])) {
     out.println("\\par                            "
       + post.horseFlows[cnt]
       );
     line++;
    // }
    lastFlow = post.horseFlows[cnt];
    cnt++;
   }
  }
  out
  .println("\\par ===============================================================================================");
  out.println("\\par ");
  line += 14;
  out.println("\\page "); // Next page
  // line = line % pagesize;
  // while(line < pagesize)
  // {
  // out.println("\\par ");
  // line++;
  // }
 } catch (Exception e) {
  Log.print("Exception Writing report: " + e + "\n");
 }
}
/**
 * Rank Bias
 */
public void rankBiasPoints(Race race)
{
 String biasPts[] = { " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ",
 " " }; // Bias points for each horse
 String biasPtsD[] = { " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ",
 " " }; // Bias points for each horse
 int biasBonus = 0;
 int biasBonusD = 0;

 for (int i = 0; i < 30; i++) 
  biasPointsRank[i] = 0;
 
 // Set each horses bias points
 for (Enumeration e = race.m_posts.elements(); e.hasMoreElements();) {
  Post post = (Post) e.nextElement();
  String entry = post.m_props.getProperty("ENTRY", "");
  if (entry.equals("S"))
   continue;
  if (post.m_handicap == null || post.m_horseName == null)
   continue; // position is empty
  String finishPos = post.m_finishPos;
  if (Truline.userProps.getProperty("TrackTheBias", "N").equals("1")
    || Truline.userProps.getProperty("TrackTheBias", "N").equals("2")) {
   // See if horse gets any bias points
   biasBonus = 0;
   biasBonusD = 0;
   for (int i = 0; i < 10; i++) {
    if (race.m_surface.equals("D") || race.m_surface.equals("A")) {
     biasPts[i] = "";
     biasPtsD[i] = "";
     if (post.m_handicap.rank[i] == 1 && biasPoints[i] > 0) {
      biasPts[i] = "" + biasPoints[i];
      biasBonus = biasBonus + biasPoints[i];
      if (race.m_distance < 1760) {
       biasPtsD[i] = "" + biasPointsSP[i];
       biasBonusD = biasBonusD + biasPointsSP[i];
      }
      else {
       biasPtsD[i] = "" + biasPointsRT[i];
       biasBonusD = biasBonusD + biasPointsRT[i];
      }
     }
     if (Truline.userProps.getProperty("TrackTheBias", "N").equals("2")
       && post.m_handicap.rank[i] == 2 && biasPoints[i] > 0) {
      biasPts[i] = "" + biasPoints[i];
      biasBonus = biasBonus + biasPoints[i];
      if (race.m_distance < 1760) {
       biasPtsD[i] = "" + biasPointsSP[i];
       biasBonusD = biasBonusD + biasPointsSP[i];
      }
      else {
       biasPtsD[i] = "" + biasPointsRT[i];
       biasBonusD = biasBonusD + biasPointsRT[i];
      }
     }
    }
    else {
     biasPts[i] = "";
     if (post.m_handicap.rank[i] == 1 && biasPointsT[i] > 0) {
      biasPts[i] = "" + biasPointsT[i];
      biasBonus = biasBonus + biasPointsT[i];
      if (race.m_distance < 1760) {
       biasPtsD[i] = "" + biasPointsTSP[i];
       biasBonusD = biasBonusD + biasPointsTSP[i];
      }
      else {
       biasPtsD[i] = "" + biasPointsTRT[i];
       biasBonusD = biasBonusD + biasPointsTRT[i];
      }
     }
     if (Truline.userProps.getProperty("TrackTheBias", "N").equals("2")
       && post.m_handicap.rank[i] == 2 && biasPointsT[i] > 0) {
      biasPts[i] = "" + biasPointsT[i];
      biasBonus = biasBonus + biasPointsT[i];
      if (race.m_distance < 1760) {
       biasPtsD[i] = "" + biasPointsTSP[i];
       biasBonusD = biasBonusD + biasPointsTSP[i];
      }
      else {
       biasPtsD[i] = "" + biasPointsTRT[i];
       biasBonusD = biasBonusD + biasPointsTRT[i];
      }
     }
    }
   }
   if (biasBonus > 0) {
    biasPts[10] = biasBonus + "/" + biasBonusD;
    biasPts[11] = "Bias";
    post.m_bias = biasBonus + "/" + biasBonusD;
    post.m_biasN = biasBonus;
   }
   else {
    biasPts[10] = "";
    biasPts[11] = "";
    post.m_bias = "";
    post.m_biasN = 0;
   }    
  }
  
  // Rank the bias points
  for (Enumeration e1 = race.m_posts.elements(); e1.hasMoreElements();) {
   Post post1 = (Post) e1.nextElement();
   for (int i = 0; i < 30; i++) {
    if (biasPointsRank[i] == 0) {
     biasPointsRank[i] = post1.m_biasN;
     break;
    }
    else if (post1.m_biasN > biasPointsRank[i]) {
     int j = 29;
     while (j > i) {
      biasPointsRank[j] = biasPointsRank[j-1];
      j = j-1;
     }
     biasPointsRank[i] = post1.m_biasN;
     break;
    }
    else if (post1.m_biasN == biasPointsRank[i]) 
     break;
   }
  }
 }

 // Set each horses bias points rank
 for (Enumeration e2 = race.m_posts.elements(); e2.hasMoreElements();) {
  Post post2 = (Post) e2.nextElement();
  for (int i = 0; i < 30; i++) {
   if (post2.m_biasN > 0 && post2.m_biasN == biasPointsRank[i]) {
    post2.m_biasRank = i+1;
    break;
   }
  }
 }
 }

  /**
   * Rank Set Kim's Trainer Flags
   */
  public void setKimsTrainerFlags(Post post)
  {

   post.m_kimsTOB = "";
   if (post.m_ownerTrn.equals("$") && post.m_ownerBrd.equals("+"))
    post.m_kimsTOB = "TO-OB";
   
   post.m_kimsT3 = "";
   for (Enumeration e1 = post.m_trainerJockeyStats.elements(); e1
     .hasMoreElements();) {
    TrainerJockeyStats tjs = (TrainerJockeyStats) e1.nextElement();
    
    for (int k = 1; k < 7; k++) {
     String cat = tjs.m_props.getProperty("TRAINERCAT"+k, "N/A");
     int win = Lib.atoi(tjs.m_props.getProperty("TRAINERWIN"+k, "0"));
     int itm = Lib.atoi(tjs.m_props.getProperty("TRAINERITM"+k, "0"));
     double roi = Lib.atof(tjs.m_props.getProperty("TRAINERROI"+k, "0"));
     if ((!cat.equals("N/A") && (roi > 0)
       && ((win > 19) || (itm > 49) || (roi > 1.99)))) {
      if (roi > 1.99)
       post.m_kimsT3 = "\\b T3\\b0";
      else
       post.m_kimsT3 = "T3";
     }
    }
   }

  }  

}
