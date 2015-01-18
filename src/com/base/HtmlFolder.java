package com.base;
/*
 *	Text Report for displaying handicap for truline2000.
 *
 */
import java.util.*;
import java.io.*;
import java.text.*;

import com.mains.Truline;
public class HtmlFolder
{
 public HtmlFolder() {
 }
 public void generate(String filename, Bris bris)
 {
  Truline.println("Generating HTML Folder Report to " + filename + ".");
  File fp = new File(filename);
  if (!fp.isDirectory()) {
   fp.mkdirs();
  }
  try {
   if (Log.isDebug(Log.TRACE))
    Log.print("Writing html to " + filename + ".html\n");
   PrintWriter out = new PrintWriter(new FileWriter(new File(fp, filename
     + ".html")));
   heading(out);
   writeIndex(filename, out, bris);
   footing(out);
   out.close();
   for (Enumeration e = bris.m_races.elements(); e.hasMoreElements();) {
    Race race = (Race) e.nextElement();
    String htmlfile = filename + "_" + race.m_raceNo + ".html";
    out = new PrintWriter(new FileWriter(new File(fp, htmlfile)));
    heading(out);
    writeReport(out, race);
    footing(out);
    out.close();
   }
  } catch (Exception e) {
   Log.print("Exception Writing report index: " + e + "\n");
  }
 }
 public void heading(PrintWriter out)
 {
  out.println("<HTML><HEAD><TITLE>" + Truline.title + "</TITLE><BODY>");
  out.println("<TABLE SIZE=600><TR><TD>");
  out.println("<FONT FACE=\"Comic Sans MS\">");
 }
 /**
  * Generates the report index.
  */
 public void writeIndex(String name, PrintWriter out, Bris bris)
 {
  NumberFormat fmt = NumberFormat.getCurrencyInstance();
  DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
  DateFormat dtf = DateFormat.getDateTimeInstance(DateFormat.SHORT,
    DateFormat.SHORT);
  String datestr = dtf.format(new Date());
  try {
   Race race = (Race) bris.m_races.elementAt(0);
   out.println("<CENTER><FONT SIZE=2><B>" + Truline.title + "</B></FONT><BR>");
   out.println("<FONT SIZE=1>" + Truline.version + " - " + Truline.copyright
     + "   &nbsp; &nbsp;    " + datestr + "</FONT><BR>");
   out.println("Track " + Lib.pad(race.m_track, 3) + "  &nbsp;  "
     + Lib.datetoa(race.m_raceDate));
   out.println("</CENTER>");
   out.println("<HR><table width=600>");
   for (Enumeration e = bris.m_races.elements(); e.hasMoreElements();) {
    race = (Race) e.nextElement();
    out.println("<tr><td valign=top>");
    out.println("<FONT SIZE=3><A HREF=\"" + name + "_" + race.m_raceNo
      + ".html\">");
    out.println("Race#" + race.m_raceNo + "</A></FONT>");
    out.println("</td><td valign=top>");
    out.println("<FONT SIZE=1>   Distance "
      + Lib.ftoa(((double) race.m_distance) / Handicap.YdPerF, 1) + "F"
      + " &nbsp; Surface " + ((race.m_surface.equals("D")) ? "Dirt" : "Turf"));
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
            : (race.m_raceType.equals("T")) ? "T-Starter Hcp"
              : (race.m_raceType.equals("C")) ? "C-claiming" : (race.m_raceType
                .equals("S")) ? "S-mdn sp wt"
                : (race.m_raceType.equals("M")) ? "M-mdn claimer"
                  : race.m_raceType));
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
    out.println("</FONT></td></tr>");
   }
   out.println("</TABLE>");
  } catch (Exception e) {
   Log.print("Exception Writing report index: " + e + "\n");
  }
 }
 public void writeReport(PrintWriter out, Race race)
 {
  DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
  DateFormat dtf = DateFormat.getDateTimeInstance(DateFormat.SHORT,
    DateFormat.SHORT);
  String datestr = dtf.format(new Date());
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
     + " &nbsp; Surface " + ((race.m_surface.equals("D")) ? "Dirt" : "Turf")
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
   // out.println("  <td align=right><b>TP</b></td>");
   out.println("</tr>");
   // Display each horse in the race.
   for (Enumeration e = race.m_posts.elements(); e.hasMoreElements();) {
    Post post = (Post) e.nextElement();
    String entry = post.m_props.getProperty("ENTRY", "");
    if (entry.equals("S")) {
     out.println("<tr>");
     // out.println("  <td valign=top><FONT SIZE=3>" + post.cloth
     // +"</font></td>");
     out.println("  <td valign=top>__ </td>");
     out.println("  <td valign=top><FONT SIZE=3 COLOR=\"red\"><b>"
       + post.m_horseName + "</b>");
     out.println("  </font></td>");
     out
       .println("  <td valign=top colspan=10><FONT SIZE=3><b>SCRATCHED</b></FONT>");
     out.println("</tr>");
     continue;
    }
    if (post.m_handicap == null || post.m_horseName == null)
     continue; // position is empty
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
    int jpcnt = (jstart > 0) ? (jwin + jplace) * 100 / jstart : 0;
    int tpcnt = (tstart > 0) ? (twin + tplace) * 100 / tstart : 0;
    out.println("<tr>");
    // out.println("  <td valign=top><FONT SIZE=3>" + post.cloth
    // +"</FONT></td>");
    out.println("  <td valign=top>__ </td>");
    out.println("  <td valign=top><FONT SIZE=3 COLOR=\"red\"><b>"
      + post.m_horseName + "</b><br>");
    out.println("                     </FONT><FONT SIZE=2>");
    out.println(post.m_props.getProperty("TRAINER", "") + "<br>");
    out.println(post.m_props.getProperty("JOCKEY", "") + "<br></FONT>");
    out.println("  </td>");
    out.println("  <td valign=top><FONT SIZE=2>" + repRaceDate + "<br>");
    out.println(Lib.ftoa((double) tstart, 0) + "/"
      + Lib.ftoa((double) tpcnt, 0) + "%" + "<br>");
    out.println(Lib.ftoa((double) jstart, 0) + "/"
      + Lib.ftoa((double) jpcnt, 0) + "%" + "<br></FONT>");
    out.println("  </td>");
    out.println("  <td valign=top align=right><FONT SIZE=2>"
      + Lib.ftoa(post.m_handicap.value[Handicap.EPS], 0) + "<br>");
    out
      .println(Lib.ftoa(post.m_handicap.rank[Handicap.EPS], 0) + "<br></FONT>");
    out.println("  </td>");
    out.println("  <td valign=top align=right><FONT SIZE=2>"
      + Lib.ftoa(post.m_handicap.value[Handicap.EN], 1)
      + ((post.m_handicap.extraFlg) ? "#" : "&nbsp;") + "<br>");
    out.println(Lib.ftoa(post.m_handicap.rank[Handicap.EN], 0) + "<br></FONT>");
    out.println("  </td>");
    out.println("  <td valign=top align=right><FONT SIZE=2>"
      + Lib.ftoa(post.m_handicap.value[Handicap.FS], 1) + "<br>");
    out.println(Lib.ftoa(post.m_handicap.rank[Handicap.FS], 0) + "<br></FONT>");
    out.println("  </td>");
    out.println("  <td valign=top align=right><FONT SIZE=2>"
      + Lib.ftoa(post.m_handicap.value[Handicap.TT], 1) + "<br>");
    out.println(Lib.ftoa(post.m_handicap.rank[Handicap.TT], 0) + "<br></FONT>");
    out.println("  </td>");
    out.println("  <td valign=top align=right><FONT SIZE=2>"
      + Lib.ftoa(post.m_handicap.value[Handicap.SS], 1) + "<br>");
    out.println(Lib.ftoa(post.m_handicap.rank[Handicap.SS], 0) + "<br></FONT>");
    out.println("  </td>");
    out.println("  <td valign=top align=right><FONT SIZE=2>"
      + Lib.ftoa(post.m_handicap.value[Handicap.CS], 1) + "<br>");
    out.println(Lib.ftoa(post.m_handicap.rank[Handicap.CS], 0) + "<br></FONT>");
    out.println("  </td>");
    out.println("  <td valign=top align=right><FONT SIZE=2>"
      + Lib.ftoa(post.m_handicap.value[Handicap.FT], 1) + "<br>");
    out.println(Lib.ftoa(post.m_handicap.rank[Handicap.FT], 0) + "<br></FONT>");
    out.println("  </td>");
    out.println("  <td valign=top align=right><FONT SIZE=2>"
      + Lib.ftoa(post.m_handicap.value[Handicap.AS], 0) + "<br>");
    out.println(Lib.ftoa(post.m_handicap.rank[Handicap.AS], 0) + "<br></FONT>");
    out.println("  </td>");
    out.println("  <td valign=top align=right><FONT SIZE=2>"
      + Lib.ftoa(post.m_handicap.value[Handicap.RE], 0) + "<br>");
    out.println(Lib.ftoa(post.m_handicap.rank[Handicap.RE], 0) + "<br></FONT>");
    out.println("  </td>");
    out.println("  <td valign=top align=right><FONT SIZE=2>"
      + Lib.ftoa(post.m_handicap.value[Handicap.QP], 0) + "<br>");
    out.println(Lib.ftoa(post.m_handicap.rank[Handicap.QP], 0) + "<br></FONT>");
    // out.println("  </td>");
    // out.println("  <td valign=top align=right><FONT SIZE=2>" +
    // (post.m_handicap.bonus + post.m_handicap.points) + "<br>");
    // out.println( Lib.ftoa(post.m_handicap.bonusRank,0) + "<br>");
    // out.println("  </td>");
    out.println("</tr>");
   }
   out.println("</table>");
   out
     .println("<FONT SIZE=1> # = Must Bet Energy or Power Trainer / $ = Turf Sire / d = Dam Sire / * = Trainer-Owner</FONT><p>");
   out.println("<hr>");
   out.println("<center>Recap of Top Ranked Horses</center>");
   out.println("<table>");
   String[] odds = { "8-5", "5-2", "6-1", "9-1", "20-1", "30-1" };
   int i = 0;
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
    out.println("<td>" + post.m_horseName + "</td><td>"
      + Lib.ftoa(post.m_handicap.bonus + post.m_handicap.points, 0)
      + "</td><td>" + ((i < 6) ? odds[i] : "") + "</td></tr>");
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
