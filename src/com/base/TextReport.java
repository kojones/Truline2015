package com.base;
/*
 *	Text Report for displaying handicap for truline2000.
 *
 */
import com.base.Bris;
import com.base.BrisMCP;
import com.base.Handicap;
import com.base.Launch;
import com.base.Lib;
import com.base.Log;
import com.base.Post;
import com.base.Race;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Enumeration;

import com.mains.Truline;
public class TextReport
{
 public TextReport() {
 }
 /**
  * Generate the report - all races - Bris DRF input
  */
 public void generate(String filename, Bris bris, boolean print)
 {
  boolean generated = false;
  String name = "tmp.rpt";
  if (!print) {
   name = filename + ".rpt";
   Truline.println("Generating Text Report to " + name);
  }
  try {
   if (Log.isDebug(Log.TRACE))
    Log.print("Writing text report to " + name + "\n");
   PrintWriter out = new PrintWriter(new FileWriter(name));
   for (Enumeration e = bris.m_races.elements(); e.hasMoreElements();) {
    Race race = (Race) e.nextElement();
    writeReport(out, race);
    generated = true;
   }
   out.close();
  } catch (Exception e) {
   Log.print("Exception opening output file " + e + "\n");
  }
  if (generated && print) {
   // Send tmp.rpt to printer
   StringBuffer results = new StringBuffer();
   try {
    String[] command = Launch.fixArgs("Notepad /p " + name);
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
  boolean generated = false;
  String name = "tmp.rpt";
  if (!print) {
   name = filename + ".rpt";
   Truline.println("Generating Text Report to " + name);
  }
  try {
   if (Log.isDebug(Log.TRACE))
    Log.print("Writing text report to " + name + "\n");
   PrintWriter out = new PrintWriter(new FileWriter(name));
   for (Enumeration e = brisMCP.m_races.elements(); e.hasMoreElements();) {
    Race race = (Race) e.nextElement();
    writeReport(out, race);
    generated = true;
   }
   out.close();
  } catch (Exception e) {
   Log.print("Exception opening output file " + e + "\n");
  }
  if (generated && print) {
   // Send tmp.rpt to printer
   StringBuffer results = new StringBuffer();
   try {
    String[] command = Launch.fixArgs("Notepad /p " + name);
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
  *         - display name of the BRIS DRF file
  * @param bris
  *         - handicap structure
  * @param raceNbr
  *         - The requested race number, (-1 for all races)
  * @param print
  *         - flag indicating wether to print or just generate file
  */
 public void generate(String filename, Bris bris, int raceNbr, boolean print)
 {
  String name = "truline.rpt";
  if (!print) {
   if (raceNbr > 0)
    name = filename + "_" + raceNbr + ".rpt";
   else
    name = filename + ".rpt";
   Truline.println("Generating Text Report to " + name);
  }
  boolean generated = false;
  PrintWriter out = null;
  try {
   out = new PrintWriter(new FileWriter(name));
   for (int idx = 0; idx < bris.m_races.size(); idx++) {
    Race race = (Race) bris.m_races.elementAt(idx);
    if (raceNbr > 0) {
     // just one race requested.
     if (generated)
      break;
     if (race.m_raceNo != raceNbr)
      continue;
    }
    writeReport(out, race);
    generated = true;
   }
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
    String[] command = Launch.fixArgs("Notepad /p " + name);
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
  String name = "truline.rpt";
  if (!print) {
   if (raceNbr > 0)
    name = filename + "_" + raceNbr + ".rpt";
   else
    name = filename + ".rpt";
   Truline.println("Generating Text Report to " + name);
  }
  boolean generated = false;
  PrintWriter out = null;
  try {
   out = new PrintWriter(new FileWriter(name));
   for (int idx = 0; idx < brisMCP.m_races.size(); idx++) {
    Race race = (Race) brisMCP.m_races.elementAt(idx);
    if (raceNbr > 0) {
     // just one race requested.
     if (generated)
      break;
     if (race.m_raceNo != raceNbr)
      continue;
    }
    writeReport(out, race);
    generated = true;
   }
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
    String[] command = Launch.fixArgs("Notepad /p " + name);
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
 public void writeReport(PrintWriter out, Race race)
 {
  DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
  DateFormat dtf = DateFormat.getDateTimeInstance(DateFormat.SHORT,
    DateFormat.SHORT);
  String datestr = dtf.format(new Date());
  try {
   out.println("                             " + Truline.title
     + "                " + datestr);
   out.println("[" + Truline.version + "] " + Truline.copyright);
   out
     .println("======================================================================================");
   out.println("Track "
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
   out.println(((race.m_claim != 0) ? ("Claim " + fmt.format(race.m_claim))
     : (race.m_purse != 0) ? ("Purse " + fmt.format(race.m_purse))
       : "                ")
     + "  Surface " + ((race.m_surface.equals("D")) ? "Dirt" : "Turf"));
   String sexAge = race.m_props.getProperty("AGESEX", "");
   out.print("AGE/SEX (" + sexAge + ")");
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
   out
     .println("======================================================================================");
   out
     .println("   #  Horse             RR      EPS  EN    FS   TT    SS   CS    FT  AS  RE QP TP  ML");
   out.println();
   // Display each horse in the race.
   for (Enumeration e = race.m_posts.elements(); e.hasMoreElements();) {
    Post post = (Post) e.nextElement();
    String entry = post.m_props.getProperty("ENTRY", "");
    if (entry.equals("S")) {
     out.println("  " + Lib.pad(post.cloth, 4) + Lib.pad(post.m_horseName, 16)
       + "  SCRATCHED");
     continue;
    }
    if (post.m_handicap == null || post.m_horseName == null)
     continue; // position is empty
    String repRaceDate;
    if (post.m_handicap.m_repRace != null)
     repRaceDate = Lib.datetoa(post.m_handicap.m_repRace.ppRaceDate);
    else
     repRaceDate = "00/00";
    out.println(Lib.pad(post.m_sireTS, 1) + Lib.pad(post.m_sireTS2, 1)
      + Lib.pad(post.m_ownerTrn, 1) + Lib.pad(post.cloth, 3)
      + Lib.pad(post.m_horseNameP, 18) + Lib.pad(repRaceDate, 5)
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
      + Lib.rjust(post.m_morningLine, 5));
    int tstart, tplace, twin, jstart, jplace, jwin;
    tstart = Lib.atoi(post.m_props.getProperty("TRAINERSTARTS"));
    tplace = Lib.atoi(post.m_props.getProperty("TRAINERPLACES"));
    twin = Lib.atoi(post.m_props.getProperty("TRAINERWINS"));
    jstart = Lib.atoi(post.m_props.getProperty("JOCKEYSTARTS"));
    jplace = Lib.atoi(post.m_props.getProperty("JOCKEYPLACES"));
    jwin = Lib.atoi(post.m_props.getProperty("JOCKEYWINS"));
    int jpcnt = (jstart > 0) ? (jwin + jplace) * 100 / jstart : 0;
    int tpcnt = (tstart > 0) ? (twin + tplace) * 100 / tstart : 0;
    out.println(" " + Lib.pad(post.m_betfactorsPR, 4)
      + Lib.pad(post.m_trainerNamePT, 1)
      + Lib.pad(post.m_props.getProperty("TRAINER", "").toLowerCase(), 17)
      + Lib.rjust((double) tstart, 4) + "/"
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
      + Lib.rjust(post.m_handicap.bonusRank, 3));
    out.println(" " + Lib.pad(post.m_trnfactorsPR, 5)
      + Lib.pad(post.m_props.getProperty("JOCKEY", "").toLowerCase(), 17)
      + Lib.rjust((double) jstart, 4) + "/"
      + Lib.pad(Lib.ftoa((double) jpcnt, 0) + "%", 4));
   }
   out
     .println(" # = Must Bet Energy or Power Trainer / $ = Turf Sire / d = Dam Sire / * = Trainer-Owner");
   out.println();
   out
     .println("======================= Recap of Top Ranked Horses ===================================");
   if (Truline.userProps.getProperty("ShowTidbits", "N").equals("Y")) {
    if (race.m_bettable1 == "N")
     out.println("*** NON-BETTABLE RACE ***");
    else if (race.m_bettable2 == "N")
     out.println("*** LOW PROBABILITY RACE - " + race.m_cntnrl
       + " horses have no running lines ***");
    // else if (race.m_surface.equals("T"))
    // out.println("\\par *** CAUTION - Turf and "+race.m_cntnrl+" horses have no running lines ***");
    else if (race.m_cnthorses < 8)
     out.println("*** Double overlay betting only - " + race.m_cnthorses
       + " horses in race - " + race.m_cntnrl + " have no running line");
    else if (race.m_cntnrl > 2)
     out.println("*** CAUTION - " + race.m_cntnrl + " out of "
       + race.m_cnthorses + " horses have no running line");
    else
     out.println("*** PRIME BETTING RACE ***");
   }
   // Display body language hints
   if (Truline.userProps.getProperty("ShowBodyLanguage", "N").equals("Y")) {
    switch (sexAge.charAt(0)) {
     case 'A':
      out.print("2 year old ");
      switch (sexAge.charAt(2)) {
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
      out.print("3 year old ");
      switch (sexAge.charAt(2)) {
       case 'N':
        out.println("Males or Fillies B/L = Calm");
        break;
       case 'C':
        out.println("Males B/L = Calm");
        break;
       case 'M':
       case 'F':
        out.println("Fillies B/L = Calm");
        break;
      }
      switch (sexAge.charAt(1)) {
       case 'O':
        break;
       case 'U':
        out.print("4 year old and up ");
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
      out.print("4 year old and up ");
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
   String[] odds = { "8-5 ", "5-2 ", "6-1 ", "9-1 ", "20-1", "30-1" };
   int i = 0;
   int j = 0;
   int pts = 999;
   int ml;
   String DO;
   for (Enumeration e = race.ranking.elements(); e.hasMoreElements();) {
    if (i >= 6) // show only the first 6
     break;
    if (i == 1)
     out.print("Track Condition ___________ ");
    else if (i == 4)
     out.print("Final Fraction  ___________ ");
    else
     out.print("                            ");
    Post post = (Post) e.nextElement();
    if (post.m_handicap.bonus + post.m_handicap.points < pts) {
     pts = post.m_handicap.bonus + post.m_handicap.points;
     j = i;
    }
    ml = Lib.atoi(post.m_props.getProperty("MORNINGLINE"));
    DO = "";
    switch (j) {
     case 0:
      if (ml >= 4)
       DO = " DO";
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
    out.println(Lib.pad(post.m_sireTS, 1) + Lib.pad(post.m_sireTS2, 1)
      + Lib.pad(post.m_trainerNamePT, 1) + Lib.pad(post.cloth, 4)
      + Lib.pad(post.m_horseName, 16)
      + Lib.rjust(post.m_handicap.bonus + post.m_handicap.points, 5) + "  "
      + ((j < 6) ? odds[j] : "") + "  (" + post.m_morningLine + DO + ")");
    i++;
   }
   out.println();
   out
     .println("=================================== Race Payoffs =================================");
   out.println();
   out
     .println("           1st  ____   ______________   ______________  _______________");
   out.println();
   out
     .println("           2nd  ____                    ______________  _______________");
   out.println();
   out
     .println("           3rd  ____                                    _______________");
   out.println();
   out
     .println("           EX ________________  QU_______________  PICK3_______________");
   out.println();
   out
     .println("           TRIFECTA __________  DD ______________  OTHER ______________");
   out
     .println("==================================================================================");
   out.println();
   out.write("\f"); // Next page
  } catch (Exception e) {
   Log.print("Exception Writing report: " + e + "\n");
  }
 }
}
