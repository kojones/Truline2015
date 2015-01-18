package com.base;
/*

 *	GUI Report for displaying handicap for truline2000.
 *
 */
import com.base.Bris;
import com.base.BrisMCP;
import com.base.BrisJCP;
import com.base.GUI;
import com.base.Handicap;
import com.base.Lib;
import com.base.Log;
import com.base.Post;
import com.base.Race;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Enumeration;

import com.mains.Truline;
public class GUIReport
{
 public static String[] names        = { "FS", "SS", "FT", "TT", "CS", "AS",
  "RE", "QP", "EN", "EPS"          };
 public int[]           biasPoints   = new int[names.length]; // Bias points 
 public int[]           biasPointsD  = new int[names.length]; // Bias points 
 private String         finishPosPrt = "";
 private String         raceSurface = "";
 private String         raceDistance = "";
public GUIReport() {
}
/**
 * Generate the report from Bris DRF data
 */
public boolean generate(String filename, Bris bris, GUI gui, int raceNbr)
{
 for (int i = 0; i < 10; i++) {
  biasPoints[i] = 0;
  biasPointsD[i] = 0;
 }
 for (Enumeration e = bris.m_races.elements(); e.hasMoreElements();) {
  Race race = (Race) e.nextElement();
  if (raceNbr == race.m_raceNo) {
   raceSurface = race.m_surface;
   raceDistance = ((race.m_distance < 1760) ? "SP" : "RT");
  }
 }
 for (Enumeration e = bris.m_races.elements(); e.hasMoreElements();) {
  Race race = (Race) e.nextElement();
  if (raceNbr == race.m_raceNo) {
   if (race.m_distance >= Lib.atoi(Truline.userProps.getProperty("MinDistance", "0")))
   {
    gui.clear();
    writeReport(gui, race);
    return true;
   }
   else
    return false;
  } else {
   if (race.m_surface.equals(raceSurface) && (race.m_distance >= Lib.atoi(Truline.userProps.getProperty("MinDistance", "0"))))
    accumulateBias(race);
  }
 }
 return false;
}
/**
 * Generate the report from Bris MCP data
 */
public boolean generate(String filename, BrisMCP bris, GUI gui, int raceNbr)
{
 for (int i = 0; i < 10; i++)
  biasPoints[i] = 0;
 for (Enumeration e = bris.m_races.elements(); e.hasMoreElements();) {
  Race race = (Race) e.nextElement();
  if (raceNbr == race.m_raceNo) {
   raceSurface = race.m_surface;
   raceDistance = ((race.m_distance < 1760) ? "SP" : "RT");
  }
 }
 for (Enumeration e = bris.m_races.elements(); e.hasMoreElements();) {
  Race race = (Race) e.nextElement();
  if (raceNbr == race.m_raceNo) {
   if (race.m_distance >= Lib.atoi(Truline.userProps.getProperty("MinDistance", "0")))
   {
    gui.clear();
    writeReport(gui, race);
    return true;
   }
   else
    return false;
  } else {
   if (race.m_surface.equals(raceSurface) && (race.m_distance >= Lib.atoi(Truline.userProps.getProperty("MinDistance", "0"))))
    accumulateBias(race);
  }
 }
 return false;
}
/**
 * Generate the report from Bris JCP data
 */
public boolean generate(String filename, BrisJCP bris, GUI gui, int raceNbr)
{
 for (int i = 0; i < 10; i++)
  biasPoints[i] = 0;
 for (Enumeration e = bris.m_races.elements(); e.hasMoreElements();) {
  Race race = (Race) e.nextElement();
  if (raceNbr == race.m_raceNo) {
   raceSurface = race.m_surface;
   raceDistance = ((race.m_distance < 1760) ? "SP" : "RT");
  }
 }
 for (Enumeration e = bris.m_races.elements(); e.hasMoreElements();) {
  Race race = (Race) e.nextElement();
  if (raceNbr == race.m_raceNo) {
   if (race.m_distance >= Lib.atoi(Truline.userProps.getProperty("MinDistance", "0")))
   {
    gui.clear();
    writeReport(gui, race);
    return true;
   }
   else
    return false;
  } else {
   if (race.m_surface.equals(raceSurface) && (race.m_distance >= Lib.atoi(Truline.userProps.getProperty("MinDistance", "0"))))
    accumulateBias(race);
  }
 }
 return false;
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
      biasPoints[i] = biasPoints[i] + 2;
      if ((raceDistance.equals("SP") && race.m_distance < 1760) || (raceDistance.equals("RT") && race.m_distance >= 1760))
       biasPointsD[i] = biasPointsD[i] + 2;
    }
    if ((Truline.userProps.getProperty("TrackTheBias", "N").equals("1") && post.m_handicap.rank[i] == 2)) {
      biasPoints[i] = biasPoints[i] + 1;
      if ((raceDistance.equals("SP") && race.m_distance < 1760) || (raceDistance.equals("RT") && race.m_distance >= 1760))
       biasPointsD[i] = biasPointsD[i] + 1;
    }
   }
  }
  if (finishPos.equals("2")) {
   for (int i = 0; i < 10; i++) {
    if ((Truline.userProps.getProperty("TrackTheBias", "N").equals("1") && post.m_handicap.rank[i] == 1)
      || (Truline.userProps.getProperty("TrackTheBias", "N").equals("2") && post.m_handicap.rank[i] < 3)) {
      biasPoints[i] = biasPoints[i] + 1;
      if ((raceDistance.equals("SP") && race.m_distance < 1760) || (raceDistance.equals("RT") && race.m_distance >= 1760))
       biasPointsD[i] = biasPointsD[i] + 1;
    }
   }
  }
 }
}
public void writeReport(GUI out, Race race)
{
 DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
 DateFormat dtf = DateFormat.getDateTimeInstance(DateFormat.SHORT,
   DateFormat.SHORT);
 String datestr = dtf.format(new Date());
 String biasPts[] = { " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ",
 " " }; // Bias points for each horse
 String biasPtsD[] = { " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ",
 " " }; // Bias points for each horse (distance)
 int biasBonus = 0;
 int biasBonusD = 0;
 try {
  /*
   * out.println("       "+Truline.title);
   * out.println(Truline.version+" - "+Truline.copyright+"       "+datestr);
   * out.println(
   * "=============================================================================="
   * );
   */
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
  out.println(((race.m_claim != 0) ? ("Claim " + fmt.format(race.m_claim) + "   Purse " + fmt.format(race.m_purse))
    : (race.m_purse != 0) ? ("Purse " + fmt.format(race.m_purse))
      : "                ")
    + "  Surface " + ((race.m_surface.equals("D")) ? "Dirt" :
                      (race.m_surface.equals("A")) ? "All Weather" :"Turf")
    + "  Handicapped for "+race.m_trackCond+" track");
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
  if (Truline.userProps.getProperty("Experimental", "N").equals("Y")) {
   int cnt = 0;
   while (cnt <= race.cntRaceFlows) {
    out.println(race.raceFlows[cnt]);
    cnt++;
   }
  }
  out
    .println("=======================================================================================");
  out
  .println("    #  Horse             RR      EPS  EN    FS   TT    SS   CS    FT  AS  RE QP TP  ML");
  if (Truline.userProps.getProperty("TrackTheBias", "N").equals("1")
    || Truline.userProps.getProperty("TrackTheBias", "N").equals("2")) {
   biasBonus = 0;
   biasBonusD = 0;
   for (int i = 0; i < 10; i++) {
    if (race.m_surface.equals(raceSurface)) {
     biasPts[i] = "" + biasPoints[i];
     biasBonus = biasBonus + biasPoints[i];
     biasPtsD[i] = "" + biasPointsD[i];
     biasBonusD = biasBonusD + biasPointsD[i];
    }
   }
   biasPts[10] = "" + biasBonus;
   biasPtsD[10] = "" + biasBonusD;
  out
  .println("       Total Bias                "
    + Lib.rjust(biasPts[Handicap.EPS], 3)
    + Lib.rjust(biasPts[Handicap.EN], 4) + Lib.rjust(biasPts[Handicap.FS], 6)
    + Lib.rjust(biasPts[Handicap.TT], 5) + Lib.rjust(biasPts[Handicap.SS], 6)
    + Lib.rjust(biasPts[Handicap.CS], 5) + Lib.rjust(biasPts[Handicap.FT], 6)
    + Lib.rjust(biasPts[Handicap.AS], 4) + Lib.rjust(biasPts[Handicap.RE], 4)
    + Lib.rjust(biasPts[Handicap.QP], 3) + Lib.rjust(biasPts[10], 3)
    );
  out
  .println("       Total Bias (distance)     "
    + Lib.rjust(biasPtsD[Handicap.EPS], 3)
    + Lib.rjust(biasPtsD[Handicap.EN], 4) + Lib.rjust(biasPtsD[Handicap.FS], 6)
    + Lib.rjust(biasPtsD[Handicap.TT], 5) + Lib.rjust(biasPtsD[Handicap.SS], 6)
    + Lib.rjust(biasPtsD[Handicap.CS], 5) + Lib.rjust(biasPtsD[Handicap.FT], 6)
    + Lib.rjust(biasPtsD[Handicap.AS], 4) + Lib.rjust(biasPtsD[Handicap.RE], 4)
    + Lib.rjust(biasPtsD[Handicap.QP], 3) + Lib.rjust(biasPtsD[10], 3)
    );
  }
  out.println();
  // Display each horse in the race.
  for (Enumeration e = race.m_posts.elements(); e.hasMoreElements();) {
   Post post = (Post) e.nextElement();
   String entry = post.m_props.getProperty("ENTRY", "");
   if (entry.equals("S")) {
    out.println("   " + Lib.pad(post.cloth, 4) + Lib.pad(post.m_horseName, 16)
      + "  SCRATCHED");
    continue;
   }
   if (post.m_handicap == null || post.m_horseName == null
     || post.cloth == null)
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
     biasPts[i] = "";
     biasPtsD[i] = "";
     if (post.m_handicap.rank[i] == 1 && biasPoints[i] > 0) {
      biasPts[i] = "" + biasPoints[i];
      biasBonus = biasBonus + biasPoints[i];
     }
     if (post.m_handicap.rank[i] == 1 && biasPointsD[i] > 0) {
      biasPtsD[i] = "" + biasPointsD[i];
      biasBonusD = biasBonusD + biasPointsD[i];
     }
     if (Truline.userProps.getProperty("TrackTheBias", "N").equals("2")
       && post.m_handicap.rank[i] == 2 && biasPoints[i] > 0) {
      biasPts[i] = "" + biasPoints[i];
      biasBonus = biasBonus + biasPoints[i];
     }
     if (Truline.userProps.getProperty("TrackTheBias", "N").equals("2")
       && post.m_handicap.rank[i] == 2 && biasPointsD[i] > 0) {
      biasPtsD[i] = "" + biasPointsD[i];
      biasBonusD = biasBonusD + biasPointsD[i];
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
   out.println(Lib.pad(post.m_sireTSp, 1) + Lib.pad(post.m_sireTS2, 1)
     + Lib.pad(post.m_ownerTrn, 1) + Lib.pad(post.cloth, 3) + Lib.pad(post.m_ownerBrd, 1)
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
/*
   int jpcnt = (jstart > 0) ? (jwin + jplace) * 100 / jstart : 0;
   int tpcnt = (tstart > 0) ? (twin + tplace) * 100 / tstart : 0;
*/
   int jpcnt = (jstart > 0) ? (jwin) * 100 / jstart : 0;
   int tpcnt = (tstart > 0) ? (twin) * 100 / tstart : 0;
   out.println(Lib.pad(post.m_sireTSPI3, 2) +  Lib.pad(post.m_betfactorsPR, 3)
     + Lib.pad(post.m_trainerNamePT, 2)
     + Lib.pad(post.m_props.getProperty("TRAINER", "").toLowerCase(), 14)
     + Lib.pad(post.m_trnfactorsPR, 4)
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
   out.println("" + Lib.pad(post.m_trnJkyPct, 7)
     + Lib.pad(post.m_props.getProperty("JOCKEY", "").toLowerCase(), 14)
     + Lib.pad(post.m_jkyfactorsPR, 4)
     + Lib.rjust((double) jstart, 3) + "/"
     + Lib.pad(Lib.ftoa((double) jpcnt, 0) + "%", 4)
     + Lib.rjust(biasPts[Handicap.EPS], 3)
     + Lib.rjust(biasPts[Handicap.EN], 4) + Lib.rjust(biasPts[Handicap.FS], 6)
     + Lib.rjust(biasPts[Handicap.TT], 5) + Lib.rjust(biasPts[Handicap.SS], 6)
     + Lib.rjust(biasPts[Handicap.CS], 5) + Lib.rjust(biasPts[Handicap.FT], 6)
     + Lib.rjust(biasPts[Handicap.AS], 4) + Lib.rjust(biasPts[Handicap.RE], 4)
     + Lib.rjust(biasPts[Handicap.QP], 3) + Lib.rjust(biasPts[10], 8));
     // + Lib.rjust(biasPts[11], 5));
  }
  out
    .println(" # = Must Bet Energy or Power Trainer / $ = Turf Sire / d = Dam Sire / * = Trainer-Owner");
  out.println();
  out
    .println("========================= Recap of Top Ranked Horses ==================================");
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
  out.println(((race.m_claim != 0) ? ("Claim " + fmt.format(race.m_claim) + "   Purse " + fmt.format(race.m_purse))
    : (race.m_purse != 0) ? ("Purse " + fmt.format(race.m_purse))
      : "                ")
    + "  Surface " + ((race.m_surface.equals("D")) ? "Dirt" :
                      (race.m_surface.equals("A")) ? "All Weather" :"Turf")
    + "  Handicapped for "+race.m_trackCond+" track");
  out.print("SEX: ");
  switch (sexAge.charAt(2)) {
   case 'N':
    out.print(" No Sex Restrictions");
    break;
   case 'M':
    out.print(" Mares and Fillies");
    break;
   case 'C':
    out.print(" Colts and/or Geldings");
    break;
   case 'F':
    out.print(" Fillies Only");
    break;
  }
  out.println();
  if (Truline.userProps.getProperty("ShowTidbits", "N").equals("Y")) {
   if (race.m_bettable1 == "N")
    out.println("*** NON-BETTABLE RACE ***");
   else if (race.m_bettable2 == "N")
   {
    out.println("*** LOW PROBABILITY RACE - " + race.m_cntnrl
      + " horses have no running lines ***");
    if (race.m_cnt1st > 0 && race.m_cntnrl > 0)
     out.println("*** CAUTION - " + race.m_cnt1st + " first time starter(s)");
   }
   // else if (race.m_surface.equals("T"))
   // out.println("*** CAUTION - Turf - "+race.m_cntnrl+" horses have no running lines ***");
   else if (race.m_cnthorses < 8)
   {
    out.println("*** Double overlay betting only - " + race.m_cnthorses
      + " horses in race - " + race.m_cntnrl + " have no running line");
    if (race.m_cnt1st > 0 && race.m_cntnrl > 0)
     out.println("*** CAUTION - " + race.m_cnt1st + " first time starter(s)");
   }
   else if (race.m_cntnrl > 2)
   {
    out.println("*** CAUTION - " + race.m_cntnrl + " horses have no running line");
   if (race.m_cnt1st > 0)
    out.println("*** CAUTION - " + race.m_cnt1st + " first time starter(s)");
   }
   else if (race.m_cnt1st > 0)
    out.println("*** CAUTION - " + race.m_cnt1st + " first time starter(s)");
   else
    out.println("*** PRIME BETTING RACE ***");
  }
  // Display body language hints
  {
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
      case 'N':
       out.println("Males B/L = Calm / Fillies = Calm and Fat");
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
  int biasPtsHigh = 0;
  int ml = 0;
  String DO;
  String Adv20;
  for (Enumeration e = race.ranking.elements(); e.hasMoreElements();) {
   if (i >= 25) // show only the first 6 and ties - or high bias points
    break;
   /*
    * if (i == 1) out.print("Track Condition ___________  "); else if (i == 4)
    * out.print("Final Fraction  ___________  "); else
    * out.print("                             ");
    */
   Post post = (Post) e.nextElement();
   if (i < 6 && post.m_handicap.bonus + post.m_handicap.points < pts) {
    pts = post.m_handicap.bonus + post.m_handicap.points;
    j = i;
   }
   if (i < 6 && post.m_biasN > biasPtsHigh)
    biasPtsHigh = post.m_biasN;
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
   if (Truline.userProps.getProperty("Experimental", "N").equals("Y")) {
    out.print("           ");
    out.println(Lib.pad(post.m_sireTSp, 1) + Lib.pad(post.m_sireTS2, 1)
      + Lib.pad(post.m_trainerNamePT.substring(1), 1) + Lib.pad(post.m_ownerTrn, 1)
      + Lib.pad(post.cloth, 3) + Lib.pad(post.m_ownerBrd, 1) + Lib.pad(post.m_horseName, 16)
      + Lib.rjust(post.m_handicap.bonus + post.m_handicap.points, 5)
      + Lib.rjust(post.m_truLine, 6)
      + Lib.pad(" (" + post.m_morningLine + post.m_truLineDO + ")" + Adv20, 15)
      + Lib.pad(post.m_bias, 10));
   }
   else {
    out.print("           ");
    out.println(Lib.pad(post.m_sireTS, 1) + Lib.pad(post.m_sireTS2, 1)
      + Lib.pad(post.m_trainerNamePT.substring(1), 1) + Lib.pad(post.m_ownerTrn, 1)
      + Lib.pad(post.cloth, 3) + Lib.pad(post.m_ownerBrd, 1) + Lib.pad(post.m_horseName, 16)
      + Lib.rjust(post.m_handicap.bonus + post.m_handicap.points, 5)
      + Lib.rjust(((j < 6) ? odds[j] : ""), 6)
      + Lib.pad("(" + post.m_morningLine + DO + ")" + Adv20, 10)
      + Lib.pad(post.m_bias, 10));
   }
   }
   i++;
  }
  /*
   * out.println(); out.println(
   * "============================= Race Payoffs ================================="
   * ); out.println(); out.println(
   * "           1st  ____   ______________   ______________  _______________"
   * ); out.println(); out.println(
   * "           2nd  ____                    ______________  _______________"
   * ); out.println(); out.println(
   * "           3rd  ____                                    _______________"
   * ); out.println(); out.println(
   * "           EX ________________  QU_______________  PICK3_______________"
   * ); out.println(); out.println(
   * "           TRIFECTA __________  DD ______________  OTHER ______________"
   * ); out.println(
   * "============================================================================"
   * ); out.println();
   */
 } catch (Exception e) {
  Log.print("Exception Writing report: " + e + "\n");
 }
}
}
