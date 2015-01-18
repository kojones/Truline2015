package com.base;
/*
 *	Text Report for displaying handicap for truline2000.
 *
 */
import com.base.BrisMCP;
import com.base.BrisJCP;
import com.base.Handicap;
import com.base.Lib;
import com.base.Log;
import com.base.Post;
import com.base.Race;

import java.util.*;
import java.io.*;
import java.text.*;

import com.mains.Truline;
public class HtmlReport
{
 public static String[] names        = { "FS", "SS", "FT", "TT", "CS", "AS",
   "RE", "QP", "EN", "EPS"          };
 public int[]           biasPoints   = new int[names.length]; // Bias points Dirt
 public int[]           biasPointsT   = new int[names.length]; // Bias points Turf
 private String         finishPosPrt = "";
public HtmlReport() {
 }
 public void generate(String filename, Bris bris)
 {
  for (int i = 0; i < 10; i++) {
   biasPoints[i] = 0;
   biasPointsT[i] = 0;
  }
  Truline.println("Generating HTML Report to " + filename + ".html");
  try {
   if (Log.isDebug(Log.TRACE))
    Log.print("Writing text report to " + filename + ".html\n");
   PrintWriter out = new PrintWriter(new FileWriter(filename + ".html"));
   heading(out);
   for (Enumeration<?> e = bris.m_races.elements(); e.hasMoreElements();) {
    Race race = (Race) e.nextElement();
    if (race.m_distance >= Lib.atoi(Truline.userProps.getProperty("MinDistance", "0")))
    {
     writeReport(out, race);
     accumulateBias(race);
    }
   }
   footing(out);
   out.close();
  } catch (Exception e) {
   Log.print("Exception Writing report: " + e + "\n");
  }
 }
 public void generate(String filename, BrisMCP brisMCP)
 {
  for (int i = 0; i < 10; i++) {
   biasPoints[i] = 0;
   biasPointsT[i] = 0;
  }
  Truline.println("Generating HTML Report to " + filename + ".html");
  try {
   if (Log.isDebug(Log.TRACE))
    Log.print("Writing text report to " + filename + ".html\n");
   PrintWriter out = new PrintWriter(new FileWriter(filename + ".html"));
   heading(out);
   for (Enumeration e = brisMCP.m_races.elements(); e.hasMoreElements();) {
    Race race = (Race) e.nextElement();
    if (race.m_distance >= Lib.atoi(Truline.userProps.getProperty("MinDistance", "0")))
    {
     writeReport(out, race);
     accumulateBias(race);
    }
   }
   footing(out);
   out.close();
  } catch (Exception e) {
   Log.print("Exception Writing report: " + e + "\n");
  }
 }
 public void generate(String filename, BrisJCP brisJCP)
 {
  for (int i = 0; i < 10; i++) {
   biasPoints[i] = 0;
   biasPointsT[i] = 0;
  }
  Truline.println("Generating HTML Report to " + filename + ".html");
  try {
   if (Log.isDebug(Log.TRACE))
    Log.print("Writing text report to " + filename + ".html\n");
   PrintWriter out = new PrintWriter(new FileWriter(filename + ".html"));
   heading(out);
   for (Enumeration e = brisJCP.m_races.elements(); e.hasMoreElements();) {
    Race race = (Race) e.nextElement();
    if (race.m_distance >= Lib.atoi(Truline.userProps.getProperty("MinDistance", "0")))
    {
     writeReport(out, race);
     accumulateBias(race);
    }
   }
   footing(out);
   out.close();
  } catch (Exception e) {
   Log.print("Exception Writing report: " + e + "\n");
  }
 }
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
      if (race.m_surface.equals("D") || race.m_surface.equals("A"))
       biasPoints[i] = biasPoints[i] + 2;
      else
       biasPointsT[i] = biasPointsT[i] + 2;
     }
     if ((Truline.userProps.getProperty("TrackTheBias", "N").equals("1") && post.m_handicap.rank[i] == 2))
      if (race.m_surface.equals("D") || race.m_surface.equals("A"))
       biasPoints[i] = biasPoints[i] + 1;
      else
       biasPointsT[i] = biasPointsT[i] + 1;
    }
   }
   if (finishPos.equals("2")) {
    for (int i = 0; i < 10; i++) {
     if ((Truline.userProps.getProperty("TrackTheBias", "N").equals("1") && post.m_handicap.rank[i] == 1)
       || (Truline.userProps.getProperty("TrackTheBias", "N").equals("2") && post.m_handicap.rank[i] < 3))
      if (race.m_surface.equals("D") || race.m_surface.equals("A"))
       biasPoints[i] = biasPoints[i] + 1;
      else
       biasPointsT[i] = biasPointsT[i] + 1;
    }
   }
  }
 }
 public void heading(PrintWriter out)
 {
  out.println("<HTML><HEAD><TITLE>" + Truline.title + "</TITLE><BODY>");
  out.println("<TABLE SIZE=600><TR><TD>");
  out.println("<FONT FACE=\"Comic Sans MS\">");
 }
 public void writeReport(PrintWriter out, Race race)
 {
  DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
  DateFormat dtf = DateFormat.getDateTimeInstance(DateFormat.SHORT,
    DateFormat.SHORT);
  String datestr = dtf.format(new Date());
  String biasPts[] = { " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ",
  " " }; // Bias points for each horse
  int biasBonus = 0;
  try {
   out.println("<CENTER><H3>" + Truline.title + "</H3>");
   out.println("<FONT SIZE=1>" + Truline.version + " - " + Truline.copyright
     + "   &nbsp; &nbsp;    " + datestr + "</FONT>");
   out.println("</CENTER>");
   out.println("<HR><FONT SIZE=3>");
   out.println("Track " + Lib.pad(race.m_track, 3) + "  &nbsp;  "
     + Lib.datetoa(race.m_raceDate) + "  &nbsp;  Race#" + race.m_raceNo
     + "</FONT>");
   out.println("<FONT SIZE=2><dir>   Distance "
     + Lib.ftoa(((double) race.m_distance) / Handicap.YdPerF, 1) + "F"
     + " &nbsp; Surface " + ((race.m_surface.equals("D")) ? "Dirt" :
                             (race.m_surface.equals("A")) ? "All Weather" :"Turf")
     + "  Handicapped for "+race.m_trackCond+" track"
     + "<br>");
   NumberFormat fmt = NumberFormat.getCurrencyInstance();
   out.println(((race.m_claim != 0) ? (" Claim " + fmt.format(race.m_claim))
     : (race.m_purse != 0) ? (" Purse " + fmt.format(race.m_purse))
       : " &nbsp &nbsp ")
     + " &nbsp;  Type "
     + ((race.m_raceType.equals("G1")) ? "G1-Stake I" : (race.m_raceType
       .equals("G2")) ? "G2-Stake II"
       : (race.m_raceType.equals("G3")) ? "G3-Stake III" : (race.m_raceType
         .equals("N")) ? "N-nongraded stake"
         : (race.m_raceType.equals("A")) ? "A-allowance" : (race.m_raceType
           .equals("R")) ? "R-Starter Alw"
           : (race.m_raceType.equals("T")) ? "T-Starter Hcp" : (race.m_raceType
             .equals("C")) ? "C-claiming"
             : (race.m_raceType.equals("S")) ? "S-mdn sp wt" : (race.m_raceType
               .equals("M")) ? "M-mdn claimer" : race.m_raceType) + "<br>");
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
   out.println("</dir></FONT>");
   out.println("<hr>");
   out.println("<table cellpadding=5>");
   out.println("<tr>");
   out.println("  <td>&nbsp;</td>");
   out.println("  <td><b>Horse</b></td>");
   out.println("  <td><b>RR</b></td>");
   out.println("  <td align=right><b>EPS</b></td>");
   out.println("  <td align=right><b>EN</b></td>");
   out.println("  <td align=right><b>FS</b></td>");
   out.println("  <td align=right><b>TT</b></td>");
   out.println("  <td align=right><b>SS</b></td>");
   out.println("  <td align=right><b>CS</b></td>");
   out.println("  <td align=right><b>FT</b></td>");
   out.println("  <td align=right><b>AS</b></td>");
   out.println("  <td align=right><b>RE</b></td>");
   out.println("  <td align=right><b>QP</b></td>");
   out.println("  <td align=right><b>TP</b></td>");
   out.println("  <td align=right><b>ML</b></td>");
   out.println("</tr>");
  
   if (Truline.userProps.getProperty("TrackTheBias", "N").equals("1")
     || Truline.userProps.getProperty("TrackTheBias", "N").equals("2")) {
    biasBonus = 0;
    for (int i = 0; i < 10; i++) {
     if (race.m_surface.equals("D") || race.m_surface.equals("A")) {
       biasPts[i] = "" + biasPoints[i];
       biasBonus = biasBonus + biasPoints[i];
      }
     else {
       biasPts[i] = "" + biasPointsT[i];
       biasBonus = biasBonus + biasPointsT[i];
      }
    }
    biasPts[10] = "" + biasBonus;
    out.println("<tr>");
    out.println("  <td>&nbsp;</td>");
    out.println("  <td><b>Total Bias</b></td>");
    out.println("  <td>&nbsp;</td>");
    out.println("  <td align=right>" + biasPts[Handicap.EPS] + "</td>");
    out.println("  <td align=right>" + biasPts[Handicap.EN] + "</td>");
    out.println("  <td align=right>" + biasPts[Handicap.FS] + "</td>");
    out.println("  <td align=right>" + biasPts[Handicap.TT] + "</td>");
    out.println("  <td align=right>" + biasPts[Handicap.SS] + "</td>");
    out.println("  <td align=right>" + biasPts[Handicap.CS] + "</td>");
    out.println("  <td align=right>" + biasPts[Handicap.FT] + "</td>");
    out.println("  <td align=right>" + biasPts[Handicap.AS] + "</td>");
    out.println("  <td align=right>" + biasPts[Handicap.RE] + "</td>");
    out.println("  <td align=right>" + biasPts[Handicap.QP] + "</td>");
    out.println("  <td align=right>" + biasPts[10] + "</td>");
    out.println("</tr>");
   }
   
   // Display each horse in the race.
   for (Enumeration e = race.m_posts.elements(); e.hasMoreElements();) {
    Post post = (Post) e.nextElement();
    String entry = post.m_props.getProperty("ENTRY", "");
    if (entry.equals("S")) {
     out.println("<tr>");
     out.println("  <td valign=top><FONT SIZE=3>&nbsp" + post.cloth + "</td>");
     // out.println("  <td valign=top>__ </td>");
     out.println("  <td valign=top><FONT SIZE=3 COLOR=\"red\"><b>"
       + post.m_horseName + "</b>");
     out.println("  </FONT></td>");
     out
       .println("  <td valign=top colspan=10><FONT SIZE=3><b>SCRATCHED</b></FONT>");
     out.println("</tr>");
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
     for (int i = 0; i < 10; i++) {
      if (race.m_surface.equals("D") || race.m_surface.equals("A")) {
       biasPts[i] = "";
       if (post.m_handicap.rank[i] == 1 && biasPoints[i] > 0) {
        biasPts[i] = "" + biasPoints[i];
        biasBonus = biasBonus + biasPoints[i];
       }
       if (Truline.userProps.getProperty("TrackTheBias", "N").equals("2")
         && post.m_handicap.rank[i] == 2 && biasPoints[i] > 0) {
        biasPts[i] = "" + biasPoints[i];
        biasBonus = biasBonus + biasPoints[i];
       }
      }
      else {
       biasPts[i] = "";
       if (post.m_handicap.rank[i] == 1 && biasPointsT[i] > 0) {
        biasPts[i] = "" + biasPointsT[i];
        biasBonus = biasBonus + biasPointsT[i];
       }
       if (Truline.userProps.getProperty("TrackTheBias", "N").equals("2")
         && post.m_handicap.rank[i] == 2 && biasPoints[i] > 0) {
        biasPts[i] = "" + biasPointsT[i];
        biasBonus = biasBonus + biasPointsT[i];
       }
      }
     }
     if (biasBonus > 0) {
      biasPts[10] = "" + biasBonus;
      biasPts[11] = "Bias";
      post.m_bias = "Bias=" + biasBonus;
     }
     else {
      biasPts[10] = "";
      biasPts[11] = "";
      post.m_bias = "";
     }      
    }
    
    String repRaceDate;
    if (post.m_handicap.m_repRace != null)
     repRaceDate = Lib.datetoa(post.m_handicap.m_repRace.ppRaceDate);
    else
     repRaceDate = "00/00";
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
    out.println("<tr>");
    out.println("  <td valign=top><FONT SIZE=3>" + post.cloth + post.m_sireTS
      + post.m_sireTS2 + post.m_ownerTrn + "<br></FONT><FONT SIZE=2>");
    out.println("                     " + post.m_betfactorsPR + "<br>");
    out.println("                     " + Lib.pad(post.m_trnJkyPct, 7) + "<br></FONT>");
    out.println("  </td>");
    // out.println("  <td valign=top>__ </td>");
    out.println("  <td valign=top><FONT SIZE=3 COLOR=\"red\"><b>"
      + post.m_horseNameP + "</b><br>");
    out.println("                     </FONT><FONT SIZE=2>");
    out.println(post.m_trainerNamePT + post.m_props.getProperty("TRAINER", "")
      + "<br>");
    out.println(" " + post.m_props.getProperty("JOCKEY", "") + "<br></FONT>");
    out.println("  </td>");
    out.println("  <td valign=top><FONT SIZE=2>" + repRaceDate + "<br>");
    out.println(Lib.ftoa((double) tstart, 0) + "/"
      + Lib.ftoa((double) tpcnt, 0) + "%" + "<br>");
    out.println(Lib.ftoa((double) jstart, 0) + "/"
      + Lib.ftoa((double) jpcnt, 0) + "%" + "<br></FONT>");
    out.println("  </td>");
    out.println("  <td valign=top align=right><FONT SIZE=2>"
      + Lib.ftoa(post.m_handicap.value[Handicap.EPS], 0) + "<br>");
    out.println(Lib.ftoa(post.m_handicap.rank[Handicap.EPS], 0) + "<br>");
    out.println(biasPts[Handicap.EPS] + "<br></FONT>");
    out.println("  </td>");
    out.println("  <td valign=top align=right><FONT SIZE=2>"
      + Lib.ftoa(post.m_handicap.value[Handicap.EN], 1)
      + ((post.m_handicap.extraFlg) ? "#" : "&nbsp;") + "<br>");
    out.println(Lib.ftoa(post.m_handicap.rank[Handicap.EN], 0) + "<br>");
    out.println(biasPts[Handicap.EN] + "<br></FONT>");
    out.println("  </td>");
    out.println("  <td valign=top align=right><FONT SIZE=2>"
      + Lib.ftoa(post.m_handicap.value[Handicap.FS], 1) + "<br>");
    out.println(Lib.ftoa(post.m_handicap.rank[Handicap.FS], 0) + "<br>");
    out.println(biasPts[Handicap.FS] + "<br></FONT>");
    out.println("  </td>");
    out.println("  <td valign=top align=right><FONT SIZE=2>"
      + Lib.ftoa(post.m_handicap.value[Handicap.TT], 1) + "<br>");
    out.println(Lib.ftoa(post.m_handicap.rank[Handicap.TT], 0) + "<br>");
    out.println(biasPts[Handicap.TT] + "<br></FONT>");
    out.println("  </td>");
    out.println("  <td valign=top align=right><FONT SIZE=2>"
      + Lib.ftoa(post.m_handicap.value[Handicap.SS], 1) + "<br>");
    out.println(Lib.ftoa(post.m_handicap.rank[Handicap.SS], 0) + "<br>");
    out.println(biasPts[Handicap.SS] + "<br></FONT>");
    out.println("  </td>");
    out.println("  <td valign=top align=right><FONT SIZE=2>"
      + Lib.ftoa(post.m_handicap.value[Handicap.CS], 1) + "<br>");
    out.println(Lib.ftoa(post.m_handicap.rank[Handicap.CS], 0) + "<br>");
    out.println(biasPts[Handicap.CS] + "<br></FONT>");
    out.println("  </td>");
    out.println("  <td valign=top align=right><FONT SIZE=2>"
      + Lib.ftoa(post.m_handicap.value[Handicap.FT], 1) + "<br>");
    out.println(Lib.ftoa(post.m_handicap.rank[Handicap.FT], 0) + "<br>");
    out.println(biasPts[Handicap.FT] + "<br></FONT>");
    out.println("  </td>");
    out.println("  <td valign=top align=right><FONT SIZE=2>"
      + Lib.ftoa(post.m_handicap.value[Handicap.AS], 0) + "<br>");
    out.println(Lib.ftoa(post.m_handicap.rank[Handicap.AS], 0) + "<br>");
    out.println(biasPts[Handicap.AS] + "<br></FONT>");
    out.println("  </td>");
    out.println("  <td valign=top align=right><FONT SIZE=2>"
      + Lib.ftoa(post.m_handicap.value[Handicap.RE], 0) + "<br>");
    out.println(Lib.ftoa(post.m_handicap.rank[Handicap.RE], 0) + "<br>");
    out.println(biasPts[Handicap.RE] + "<br></FONT>");
    out.println("  </td>");
    out.println("  <td valign=top align=right><FONT SIZE=2>"
      + Lib.ftoa(post.m_handicap.value[Handicap.QP], 0) + "<br>");
    out.println(Lib.ftoa(post.m_handicap.rank[Handicap.QP], 0) + "<br>");
    out.println(biasPts[Handicap.QP] + "<br></FONT>");
    out.println("  </td>");
    out.println("  <td valign=top align=right><FONT SIZE=2>"
      + (post.m_handicap.bonus + post.m_handicap.points) + "<br>");
    out.println(Lib.ftoa(post.m_handicap.bonusRank, 0) + "<br>");
    out.println(biasPts[10] + "<br></FONT>");
    out.println("  </td>");
    out.println("  <td valign=top align=right><FONT SIZE=2>"
      + (post.m_morningLine) + "<br>");
    out.println(finishPosPrt + "<br>");
    out.println(biasPts[11] + "<br></FONT>");
    out.println("  </td>");
    out.println("</tr>");
   }
   out.println("</table>");
   out
     .println("<FONT SIZE=1> # = Must Bet Energy or Power Trainer / $ = Turf Sire / d = Dam Sire / * = Trainer-Owner</FONT><p>");
   out.println("<hr>");
   out.println("<center>Recap of Top Ranked Horses</center>");
   if (Truline.userProps.getProperty("ShowTidbits", "N").equals("Y")) {
    if (race.m_bettable1 == "N")
     out.println("*** NON-BETTABLE RACE ***");
    else if (race.m_bettable2 == "N")
    {
     out.println("*** LOW PROBABILITY RACE - " + race.m_cntnrl
       + " horses have no running lines ***");
     if (race.m_cnt1st > 0)
      out.println("*** CAUTION - " + race.m_cnt1st + " first time starter(s)");    
     }
    // else if (race.m_surface.equals("T"))
    // out.println("*** CAUTION - Turf and "+race.m_cntnrl+" horses have no running lines ***");
    else if (race.m_cnthorses < 8)
    {
     out.println("*** Double overlay betting only - " + race.m_cnthorses
       + " horses in race - " + race.m_cntnrl + " have no running line");
     if (race.m_cnt1st > 0)
      out.println("*** CAUTION - " + race.m_cnt1st + " first time starter(s)");    
    }
    else if (race.m_cntnrl > 2)
    {
     out.println("*** CAUTION - " + race.m_cntnrl + " horses have no running line");
     if (race.m_cnt1st > 0)
      out.println("*** CAUTION - " + race.m_cnt1st + " first time starter(s)");    
    }
    else if (race.m_cnt1st > 0)
    {
     out.println("*** CAUTION - " + race.m_cnt1st + " first time starter(s)");
    }
    else
     out.println("*** PRIME BETTING RACE ***");
   }
   out.println("<br>");
   // Display body language hints
   if (Truline.userProps.getProperty("ShowBodyLanguage", "N").equals("Y")) {
    switch (sexAge.charAt(0)) {
     case 'A':
      out.print("2 year old ");
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
      out.print("3 year old ");
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
        out.print(" / 4 year old and up ");
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
   out.println("<table>");
   String[] odds = { "8-5", "5-2", "6-1", "9-1", "20-1", "30-1" };
   int i = 0;
   int j = 0;
   int pts = 999;
   int ml;
   String DO;
   String Adv20;
   for (Enumeration e = race.ranking.elements(); e.hasMoreElements();) {
    if (i >= 6) // show only the first 6
     break;
    if (i == 1)
     out.print("<tr><td>Track Condition ___________  </td>");
    else if (i == 4)
     out.print("<tr><td>Final Fraction  ___________  </td>");
    else
     out.print("<tr><td>&nbsp;</td>");
    Post post = (Post) e.nextElement();
    if (post.m_handicap.bonus + post.m_handicap.points < pts) {
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
    if (Truline.userProps.getProperty("Experimental", "N").equals("Y")) {
     out.println("<td>" + post.m_sireTS + post.m_sireTS2 + post.m_trainerNamePT
       + post.m_ownerTrn + "</td><td>" + post.cloth + "</td><td>"
       + post.m_horseName + "</td><td>"
       + Lib.ftoa(post.m_handicap.bonus + post.m_handicap.points, 0)
       + "</td><td>" + post.m_truLine + "</td><td>("
       + post.m_morningLine + post.m_truLineDO + ")" + Adv20 + "</td><td>" + post.m_bias
       + "</td></tr>");
    }
    else {
     out.println("<td>" + post.m_sireTS + post.m_sireTS2 + post.m_trainerNamePT
       + post.m_ownerTrn + "</td><td>" + post.cloth + "</td><td>"
       + post.m_horseName + "</td><td>"
       + Lib.ftoa(post.m_handicap.bonus + post.m_handicap.points, 0)
       + "</td><td>" + ((j < 6) ? odds[j] : "") + "</td><td>("
       + post.m_morningLine + DO + ")" + Adv20 + "</td><td>" + post.m_bias
       + "</td></tr>");
    }
    i++;
   }
   out.println("</table>");
   out.println("<pre>");
   out
     .println("============================= Race Payoffs =================================");
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
     .println("============================================================================");
   out.println("</pre>");
   out.write("<p><br><p>"); // Next page
  } catch (Exception e) {
   Log.print("Exception Writing report: " + e + "\n");
  }
 }
 public void footing(PrintWriter out)
 {
  out.println("</FONT>");
  out.println("</TD></TR></TABLE>");
  out.println("</body></HTML>");
 }
}
