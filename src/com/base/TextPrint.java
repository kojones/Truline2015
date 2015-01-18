package com.base;
/*
 *	Text Report for Printing handicap for truline2000.
 *	NOTE:   This is using the older printJob rather than printerJob
 *			because printerJob did not work properly on Win95.
 *
 */
import com.base.Bris;
import com.base.BrisMCP;
import com.base.Handicap;
import com.base.Lib;
import com.base.Log;
import com.base.Post;
import com.base.Race;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.PrintJob;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import com.mains.Truline;
public class TextPrint
{
 Vector<String> out = new Vector<String>();
 Font           m_font;
 public TextPrint() {
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
  * @param f
  *         - Frame associated with this.
  */
 public void generate(String filename, Bris bris, int raceNbr, Frame f)
 {
  // Truline.add("Printing Text Report");
  boolean generated = false;
  for (int idx = 0; idx < bris.m_races.size(); idx++) {
   Race race = (Race) bris.m_races.elementAt(idx);
   if (raceNbr > 0) {
    // just one race requested.
    if (generated)
     break;
    if (race.m_raceNo != raceNbr)
     continue;
   }
   writeReport(race);
   generated = true;
  }
  String fontstring = Truline.userProps.getProperty("PrinterFont", "Courier-8");
  m_font = Font.decode(fontstring);
  PrintJob pjob = f.getToolkit().getPrintJob(f, "Truline report", null);
  if (pjob != null) {
   int pageNo = 0;
   while (true) {
    Graphics pg = pjob.getGraphics();
    if (pg != null) {
     boolean more = paint(pjob, pg, pageNo);
     pg.dispose();
     if (!more)
      break;
     pageNo++;
    } else
     break;
   }
   pjob.end();
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
  * @param f
  *         - Frame associated with this.
  */
 public void generate(String filename, BrisMCP brisMCP, int raceNbr, Frame f)
 {
  // Truline.add("Printing Text Report");
  boolean generated = false;
  for (int idx = 0; idx < brisMCP.m_races.size(); idx++) {
   Race race = (Race) brisMCP.m_races.elementAt(idx);
   if (raceNbr > 0) {
    // just one race requested.
    if (generated)
     break;
    if (race.m_raceNo != raceNbr)
     continue;
   }
   writeReport(race);
   generated = true;
  }
  String fontstring = Truline.userProps.getProperty("PrinterFont", "Courier-8");
  m_font = Font.decode(fontstring);
  PrintJob pjob = f.getToolkit().getPrintJob(f, "Truline report", null);
  if (pjob != null) {
   int pageNo = 0;
   while (true) {
    Graphics pg = pjob.getGraphics();
    if (pg != null) {
     boolean more = paint(pjob, pg, pageNo);
     pg.dispose();
     if (!more)
      break;
     pageNo++;
    } else
     break;
   }
   pjob.end();
  }
 }
 public void writeReport(Race race)
 {
  DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
  DateFormat dtf = DateFormat.getDateTimeInstance(DateFormat.SHORT,
    DateFormat.SHORT);
  String datestr = dtf.format(new Date());
  try {
   out.add("                        " + Truline.title + "                "
     + datestr);
   out.add("[" + Truline.version + "] " + Truline.copyright);
   out
     .add("======================================================================================");
   out.add("Track "
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
   out.add(((race.m_claim != 0) ? ("Claim " + fmt.format(race.m_claim))
     : (race.m_purse != 0) ? ("Purse " + fmt.format(race.m_purse))
       : "                ")
     + "  Surface " + ((race.m_surface.equals("D")) ? "Dirt" : "Turf"));
   String sexAge = race.m_props.getProperty("AGESEX", "");
   String str = "AGE/SEX (" + sexAge + ")";
   switch (sexAge.charAt(0)) {
    case 'A':
     str += " 2 year olds";
     break;
    case 'B':
     str += " 3 year olds";
     break;
    case 'C':
     str += " 4 year olds";
     break;
    case 'D':
     str += " 5 year olds";
     break;
    case 'E':
     str += " 3 & 4 year olds";
     break;
    case 'F':
     str += " 4 & 5 year olds";
     break;
    case 'G':
     str += " 3, 4, and 5 year olds";
     break;
    case 'H':
     str += " all ages";
     break;
   }
   switch (sexAge.charAt(1)) {
    case 'O':
     str += ", That age Only";
     break;
    case 'U':
     str += ", That age and Up";
     break;
   }
   switch (sexAge.charAt(2)) {
    case 'N':
     str += ", No Sex Restrictions";
     break;
    case 'M':
     str += ", Mares and Fillies";
     break;
    case 'C':
     str += ", Colts and/or Geldings";
     break;
    case 'F':
     str += ", Fillies Only";
     break;
   }
   out.add(str);
   out
     .add("======================================================================================");
   out
     .add("   #  Horse             RR      EPS  EN    FS   TT    SS   CS    FT  AS  RE QP TP  ML");
   out.add("");
   // Display each horse in the race.
   for (Enumeration e = race.m_posts.elements(); e.hasMoreElements();) {
    Post post = (Post) e.nextElement();
    String entry = post.m_props.getProperty("ENTRY", "");
    if (entry.equals("S")) {
     out.add("  " + Lib.pad(post.cloth, 4) + Lib.pad(post.m_horseName, 16)
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
    out.add(Lib.pad(post.m_sireTS, 1) + Lib.pad(post.m_sireTS2, 1)
      + Lib.pad(post.m_ownerTrn, 1) + Lib.pad(post.cloth, 3)
      + Lib.pad(post.m_horseName, 18) + Lib.pad(repRaceDate, 5)
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
    out.add(" " + Lib.pad(post.m_betfactorsPR, 4)
      + Lib.pad(post.m_trainerNamePT, 1)
      + Lib.pad(post.m_props.getProperty("TRAINER", "").toLowerCase(), 15)
      + Lib.rjust((double) tstart, 4) + "/"
      + Lib.pad(Lib.ftoa((double) tpcnt, 0) + "%", 6)
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
    out.add("      "
      + Lib.pad(post.m_props.getProperty("JOCKEY", "").toLowerCase(), 15)
      + Lib.rjust((double) jstart, 4) + "/"
      + Lib.pad(Lib.ftoa((double) jpcnt, 0) + "%", 4));
   }
   out
     .add(" # = Must Bet Energy or Power Trainer / $ = Turf Sire / d = Dam Sire / * = Trainer-Owner");
   out.add("");
   out
     .add("======================= Recap of Top Ranked Horses ===================================");
   if (Truline.userProps.getProperty("ShowTidbits", "N").equals("Y")) {
    if (race.m_bettable1 == "N")
     out.add("*** NON-BETTABLE RACE ***");
    else if (race.m_surface.equals("T"))
     out.add("*** CAUTION - Turf and " + race.m_cntnrl
       + " horses have no running lines ***");
    else if (race.m_cnthorses < 8)
     out.add("*** Double overlay betting only - " + race.m_cnthorses
       + " horses in race - " + race.m_cntnrl + " have no running line");
    else if (race.m_cntnrl > 2)
     out.add("*** CAUTION - " + race.m_cntnrl + " out of " + race.m_cnthorses
       + " horses have no running line");
    else
     out.add("*** PRIME BETTING RACE ***");
   }
   // Display body language hints
   if (Truline.userProps.getProperty("ShowBodyLanguage", "N").equals("Y")) {
    switch (sexAge.charAt(0)) {
     case 'A':
      switch (sexAge.charAt(2)) {
       case 'C':
        out.add("2 year old Males B/L = Calm");
        break;
       case 'F':
        out.add("2 year old Fillies B/L = Calm and Fat");
        break;
      }
      break;
     case 'B':
     case 'E':
     case 'G':
     case 'H':
      switch (sexAge.charAt(2)) {
       case 'N':
        out.add("3 year old Males or Fillies B/L = Calm");
        break;
       case 'C':
        out.add("3 year old Males B/L = Calm");
        break;
       case 'M':
       case 'F':
        out.add("3 year old Fillies B/L = Calm");
        break;
      }
      switch (sexAge.charAt(1)) {
       case 'O':
        break;
       case 'U':
        switch (sexAge.charAt(2)) {
         case 'N':
          out
            .add("4 year old and up Males B/L = Prancing / Mares B/L = Prancing and Fat");
          break;
         case 'C':
          out.add("4 year old and up Males B/L = Prancing");
          break;
         case 'M':
         case 'F':
          out.add("4 year old and up Mares B/L = Prancing and Fat");
          break;
        }
        break;
      }
      break;
     case 'C':
     case 'D':
     case 'F':
      switch (sexAge.charAt(2)) {
       case 'N':
        out
          .add("4 year old and up Males B/L = Prancing / Mares B/L = Prancing and Fat");
        break;
       case 'C':
        out.add("4 year old and up Males B/L = Prancing");
        break;
       case 'M':
       case 'F':
        out.add("4 year old and up Mares B/L = Prancing and Fat");
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
    out.add(Lib.pad(post.m_sireTS, 1) + Lib.pad(post.m_sireTS2, 1)
      + Lib.pad(post.m_trainerNamePT, 1) + Lib.pad(post.cloth, 4)
      + Lib.pad(post.m_horseName, 16)
      + Lib.rjust(post.m_handicap.bonus + post.m_handicap.points, 5) + "  "
      + ((j < 6) ? odds[j] : "") + "  (" + post.m_morningLine + DO + ")");
    i++;
   }
   out.add("");
   out
     .add("================================== Race Payoffs ==================================");
   out.add("");
   out
     .add("           1st  ____   ______________   ______________  _______________");
   out.add("");
   out
     .add("           2nd  ____                    ______________  _______________");
   out.add("");
   out
     .add("           3rd  ____                                    _______________");
   out.add("");
   out
     .add("           EX ________________  QU_______________  PICK3_______________");
   out.add("");
   out
     .add("           TRIFECTA __________  DD ______________  OTHER ______________");
   out
     .add("=================================================================================");
   out.add("");
   out.add("\f"); // Next page
  } catch (Exception e) {
   Log.print("Exception Writing report: " + e + "\n");
   return;
  }
 }
 /**
  * The function called to render the page.
  */
 public boolean paint(PrintJob pjob, Graphics pg, int pageIndex)
 {
  int margin = 50;
  int wPage = pjob.getPageDimension().width - (margin * 2);
  int hPage = pjob.getPageDimension().height - (margin * 2);
  pg.translate(margin, margin);
  // pg.setClip(0,0,wPage - margin, hPage - margin);
  pg.setFont(m_font);
  pg.setColor(Color.black);
  FontMetrics fm = pg.getFontMetrics();
  int h = fm.getHeight();
  int x = 0;
  int y = fm.getAscent() + h;
  int lineNo = 0;
  int page = 0;
  int bottom = 2 * h; // save room at bottom for two more lines.
  while (lineNo < out.size()) {
   String line = (String) out.elementAt(lineNo);
   if (pageIndex == page) {
    while (lineNo < out.size()) {
     if (line.equals("\f") || y + bottom > hPage)
      break;
     if (line.length() > 0)
      pg.drawString(line, x, y);
     y += h;
     lineNo++;
     line = (String) out.elementAt(lineNo);
    }
    line = "Page " + (page + 1);
    pg.drawString(line, wPage / 2 - line.length() / 2, hPage - fm.getAscent());
    if (lineNo < out.size())
     return true;
    return false;
   }
   y += h;
   if (line.equals("\f") || y + bottom > hPage) {
    // page boundary
    y = fm.getAscent() + h;
    page++;
   }
   lineNo++;
  }
  return false;
 }
}
