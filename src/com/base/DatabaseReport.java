package com.base;
/*
 *	Output race including handicap to database for truline2012.
 *
 */
import java.util.*;
import java.io.*;
import java.text.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mains.Truline;

public class DatabaseReport
{
 private Connection        connect    = null;
 private Statement         sqlStmt    = null;
 private PreparedStatement psqlStmt   = null;
 private ResultSet         resultSet  = null;
 int                       m_mode;
 boolean                   first_time = true;
 boolean                   first_race = true;
 String                    sql;
 public static String[] names        = { "FS", "SS", "FT", "TT", "CS", "AS",
  "RE", "QP", "EN", "EPS", "TTCS","FSTT","PP","ML"          };
 public int[]           biasPoints   = new int[names.length]; // Bias points Dirt
 public int[]           biasPointsT   = new int[names.length]; // Bias points Turf
 public int[]           biasPointsR  = new int[names.length]; // Bias points
 public int             biasBonus = 0;
 public int             biasTotal = 0;
 public int             biasTotalT = 0;
 public int             biasTotalR = 0;
 public DatabaseReport() {
}
 /**
  * Output the race from DRF input.
  */
 public void generate(String databaseName, Bris bris, int mode)
 {
  for (int i = 0; i < 10; i++) {
   biasPoints[i] = 0;
   biasPointsT[i] = 0;
  }
  biasTotal = 0;
  biasTotalT = 0;
  m_mode = mode;
  Truline.println("Inserting race into database " + databaseName);
  try {
   if (first_time) {
    first_time = false;
    // This will load the MySQL driver, each DB has its own driver
    Class.forName("com.mysql.jdbc.Driver");
    // Setup the connection with the DB
    connect = DriverManager.getConnection("jdbc:mysql://127.0.0.1/"
      + databaseName + "?" + "user=truline&password=4crossPP");
    insertHandicapOptions();
   }
   if (Log.isDebug(Log.TRACE))
    Log.print("Outputing race day " + databaseName + "\n");
   for (Enumeration e = bris.m_races.elements(); e.hasMoreElements();) {
    Race race = (Race) e.nextElement();
    if (race.m_distance >= Lib.atoi(Truline.userProps.getProperty("MinDistance", "0")))
    {
     accumulateBias(race);
     if (m_mode == Truline.DATABASE1 || m_mode == Truline.DATABASE3)
      outputRace(race);
     else
      outputHandicap(race);
    }
   }
   psqlStmt = connect.prepareStatement("commit;");
   psqlStmt.executeUpdate();
   first_race = true;
  } catch (Exception e) {
   Log.print("Exception outputing race to database " + e + "\n" + "Class = "
     + e.getClass() + "  Message = " + e.getMessage() + "\n");
  }
 }
 /**
  * Output the race from MCP input.
  */
 public void generate(String databaseName, BrisMCP brisMCP, int mode)
 {
  for (int i = 0; i < 10; i++) {
   biasPoints[i] = 0;
   biasPointsT[i] = 0;
  }
  biasTotal = 0;
  biasTotalT = 0;
  m_mode = mode;
  Truline.println("Inserting race into database " + databaseName);
  try {
   if (first_time) {
    first_time = false;
    // This will load the MySQL driver, each DB has its own driver
    Class.forName("com.mysql.jdbc.Driver");
    // Setup the connection with the DB
    connect = DriverManager.getConnection("jdbc:mysql://localhost/"
      + databaseName + "?" + "user=truline&password=4crossPP");
    insertHandicapOptions();
   }
   if (Log.isDebug(Log.TRACE))
    Log.print("Outputing race day to database " + databaseName + "\n");
   for (Enumeration e = brisMCP.m_races.elements(); e.hasMoreElements();) {
    Race race = (Race) e.nextElement();
    if (race.m_distance >= Lib.atoi(Truline.userProps.getProperty("MinDistance", "0")))
    {
     accumulateBias(race);
     if (m_mode == Truline.DATABASE1 || m_mode == Truline.DATABASE3)
      outputRace(race);
     else
      outputHandicap(race);
    }
   }
   psqlStmt = connect.prepareStatement("commit;");
   psqlStmt.executeUpdate();
   first_race = true;
  } catch (Exception e) {
   Log.print("Exception outputing race to database " + e + "\n" + "Class = "
     + e.getClass() + "  Message = " + e.getMessage() + "\n");
  }
 }
 /**
  * Output the race from JCP input.
  */
 public void generate(String databaseName, BrisJCP brisJCP, int mode)
 {
  for (int i = 0; i < 10; i++) {
   biasPoints[i] = 0;
   biasPointsT[i] = 0;
  }
  biasTotal = 0;
  biasTotalT = 0;
  m_mode = mode;
  Truline.println("Inserting race into database " + databaseName);
  try {
   if (first_time) {
    first_time = false;
    // This will load the MySQL driver, each DB has its own driver
    Class.forName("com.mysql.jdbc.Driver");
    // Setup the connection with the DB
    connect = DriverManager.getConnection("jdbc:mysql://localhost/"
      + databaseName + "?" + "user=truline&password=4crossPP");
    insertHandicapOptions();
   }
   if (Log.isDebug(Log.TRACE))
    Log.print("Outputing race day to database " + databaseName + "\n");
   for (Enumeration e = brisJCP.m_races.elements(); e.hasMoreElements();) {
    Race race = (Race) e.nextElement();
    if (race.m_distance >= Lib.atoi(Truline.userProps.getProperty("MinDistance", "0")))
    {
     accumulateBias(race);
     if (m_mode == Truline.DATABASE1 || m_mode == Truline.DATABASE3)
      outputRace(race);
     else
      outputHandicap(race);
    }
   }
   psqlStmt = connect.prepareStatement("commit;");
   psqlStmt.executeUpdate();
   first_race = true;
  } catch (Exception e) {
   Log.print("Exception outputing race to database " + e + "\n" + "Class = "
     + e.getClass() + "  Message = " + e.getMessage() + "\n");
  }
 }
 public void outputRace(Race race) throws Exception
 {
  // Remove race date for track from all tables if it is there
  if (first_race) {
   first_race = false;
   if (Log.isDebug(Log.TRACE))
    Log.print("Delete and insert " + race.m_track + "/" + race.m_raceDate + "/"
      + race.m_raceNo + "\n");
   /*
    * Delete from the following tables where TRACK_ABBR and DATE_RACE are equal
    * RACE, RACE_PROPERTIES, RACE_RESULTS RACE_POST, RACE_POST_HANDiCAP,
    * RACE_POST_PAST_PERF_PROPERTIES, RACE_POST_PAST_PERF RACE_POST_PROPERTIES,
    * RACE_POST_TRAINER_JOCKEY_STATS, RACE_POST_WORK
    */
   Properties prop = new Properties();
   prop.setProperty("TRACK_ABBR", race.m_track);
   prop.setProperty("DATE_RACE", race.m_props.getProperty("RACEDATE"));
   sql = makeDelete("RACE", prop);
   psqlStmt = connect.prepareStatement(sql);
   psqlStmt.executeUpdate();
   sql = makeDelete("RACE_BIAS", prop);
   psqlStmt = connect.prepareStatement(sql);
   psqlStmt.executeUpdate();
   sql = makeDelete("RACE_PROPERTIES", prop);
   psqlStmt = connect.prepareStatement(sql);
   psqlStmt.executeUpdate();
   sql = makeDelete("RACE_RESULTS", prop);
   psqlStmt = connect.prepareStatement(sql);
   psqlStmt.executeUpdate();
   sql = makeDelete("RACE_POST", prop);
   psqlStmt = connect.prepareStatement(sql);
   psqlStmt.executeUpdate();
   sql = makeDelete("RACE_POST_HANDICAP", prop);
   psqlStmt = connect.prepareStatement(sql);
   psqlStmt.executeUpdate();
//   if (!race.m_resultsPosted.equals("Y")) {
    sql = makeDelete("RACE_POST_FLOW_BET", prop);
    psqlStmt = connect.prepareStatement(sql);
    psqlStmt.executeUpdate();
//   }
   sql = makeDelete("RACE_FLOW_BET", prop);
   psqlStmt = connect.prepareStatement(sql);
   psqlStmt.executeUpdate();
   sql = makeDelete("RACE_POST_PAST_PERF", prop);
   psqlStmt = connect.prepareStatement(sql);
   psqlStmt.executeUpdate();
   sql = makeDelete("RACE_POST_PAST_PERF_PROPERTIES", prop);
   psqlStmt = connect.prepareStatement(sql);
   psqlStmt.executeUpdate();
   sql = makeDelete("RACE_POST_PROPERTIES", prop);
   psqlStmt = connect.prepareStatement(sql);
   psqlStmt.executeUpdate();
   sql = makeDelete("RACE_POST_TRAINER_JOCKEY_STATS", prop);
   psqlStmt = connect.prepareStatement(sql);
   psqlStmt.executeUpdate();
   sql = makeDelete("RACE_POST_WORK", prop);
   psqlStmt = connect.prepareStatement(sql);
   psqlStmt.executeUpdate();
// Temporary remove JCP original track 
   prop.setProperty("TRACK_ABBR", race.m_props.getProperty("TRACKABBR"));
   prop.setProperty("DATE_RACE", race.m_props.getProperty("RACEDATE"));
   sql = makeDelete("RACE", prop);
   psqlStmt = connect.prepareStatement(sql);
   psqlStmt.executeUpdate();
   sql = makeDelete("RACE_BIAS", prop);
   psqlStmt = connect.prepareStatement(sql);
   psqlStmt.executeUpdate();
   sql = makeDelete("RACE_PROPERTIES", prop);
   psqlStmt = connect.prepareStatement(sql);
   psqlStmt.executeUpdate();
   sql = makeDelete("RACE_RESULTS", prop);
   psqlStmt = connect.prepareStatement(sql);
   psqlStmt.executeUpdate();
   sql = makeDelete("RACE_POST", prop);
   psqlStmt = connect.prepareStatement(sql);
   psqlStmt.executeUpdate();
   sql = makeDelete("RACE_POST_HANDICAP", prop);
   psqlStmt = connect.prepareStatement(sql);
   psqlStmt.executeUpdate();
   if (!race.m_resultsPosted.equals("Y")) {
    sql = makeDelete("RACE_POST_FLOW_BET", prop);
    psqlStmt = connect.prepareStatement(sql);
    psqlStmt.executeUpdate();
   }
   sql = makeDelete("RACE_POST_PAST_PERF", prop);
   psqlStmt = connect.prepareStatement(sql);
   psqlStmt.executeUpdate();
   sql = makeDelete("RACE_POST_PAST_PERF_PROPERTIES", prop);
   psqlStmt = connect.prepareStatement(sql);
   psqlStmt.executeUpdate();
   sql = makeDelete("RACE_POST_PROPERTIES", prop);
   psqlStmt = connect.prepareStatement(sql);
   psqlStmt.executeUpdate();
   sql = makeDelete("RACE_POST_TRAINER_JOCKEY_STATS", prop);
   psqlStmt = connect.prepareStatement(sql);
   psqlStmt.executeUpdate();
   sql = makeDelete("RACE_POST_WORK", prop);
   psqlStmt = connect.prepareStatement(sql);
   psqlStmt.executeUpdate();
}
  // Insert race-level tables
  insertRace(race);
  insertRaceBias(race);
  insertRaceResults(race);
  
  /* ***  Race flow inserts moved to external procedures
  if (Truline.userProps.getProperty("ArtAndKim", "N").equals("Y")) {
   if (race.cntRaceFlowsAK >= 0)
    insertRaceFlowBetAK(race);
  }
  else if (race.cntRaceFlows >= 0)
   insertRaceFlowBet(race);
   **** */
  
  if (m_mode == Truline.DATABASE1) {
   insertRaceProperties(race);
  }
  // Insert post-level tables
  for (Enumeration e = race.m_posts.elements(); e.hasMoreElements();) {
   Post post = (Post) e.nextElement();
   insertRacePost(race, post);
/*   if (post.m_workCnt > 4)  */
       insertRacePostWork(race, post);
   if (m_mode == Truline.DATABASE1) {
    insertRacePostProperties(race, post);
    for (Enumeration e1 = post.m_performances.elements(); e1.hasMoreElements();) {
     Performance p = (Performance) e1.nextElement();
     insertRacePostPastPerf(race, post, p);
     insertRacePostPastPerfProperties(race, post, p);
    }
    for (int i = 0; i < post.m_work.length; i++) {
     if (post.m_work[i].m_workDate != null) {
      insertRacePostWork(race, post);
     }
    }
   }
   if (post.m_handicap == null || post.m_horseName == null)
    continue; // position is empty
   String entry = post.m_props.getProperty("ENTRY", "");
   if (entry.equals("S")) {
    // No handicap for scratched horses
   } else {
    if (Truline.userProps.getProperty("TrackTheBias", "N").equals("1")
      || Truline.userProps.getProperty("TrackTheBias", "N").equals("2")) {
     // See if horse gets any bias points
     biasBonus = 0;
     for (int i = 0; i < 10; i++) {
      if (race.m_surface.equals("D")) {
       if (post.m_handicap.rank[i] == 1 && biasPoints[i] > 0) {
        biasBonus = biasBonus + biasPoints[i];
       }
       if (Truline.userProps.getProperty("TrackTheBias", "N").equals("2")
         && post.m_handicap.rank[i] == 2 && biasPoints[i] > 0) {
        biasBonus = biasBonus + biasPoints[i];
       }
      }
      if (race.m_surface.equals("T")) {
       if (post.m_handicap.rank[i] == 1 && biasPointsT[i] > 0) {
        biasBonus = biasBonus + biasPointsT[i];
       }
       if (Truline.userProps.getProperty("TrackTheBias", "N").equals("2")
         && post.m_handicap.rank[i] == 2 && biasPointsT[i] > 0) {
        biasBonus = biasBonus + biasPointsT[i];
       }
      }
     }
    }
    insertRacePostHandicap(race, post);
    for (Enumeration e1 = post.m_trainerJockeyStats.elements(); e1
      .hasMoreElements();) {
     TrainerJockeyStats tjs = (TrainerJockeyStats) e1.nextElement();
     insertRacePostTrainerJockeyStats(race, post, tjs);
    }
   }
  }
 }
 public void outputHandicap(Race race) throws Exception
 {
  // If handicap only, remove handicap and bias for this race and re-insert
  if (m_mode == Truline.DATABASE2) {
   if (first_race) {
    first_race = false;
    if (Log.isDebug(Log.TRACE))
     Log.print("Delete and insert handicap for " + race.m_track + "/"
       + race.m_raceDate + "/" + race.m_raceNo + "/"
       + Truline.m_handicapVersion + "\n");
    // Remove from RACE_POST_HANDICAP table for all races for TRACK_ABBR,
    // DATE_RACE, HANDICAP_VERSION
    Properties prop = new Properties();
    prop.setProperty("TRACK_ABBR", race.m_track);
    prop.setProperty("DATE_RACE", race.m_props.getProperty("RACEDATE"));
    prop.setProperty("RACE_NO", race.m_props.getProperty("RACENO"));
    prop.setProperty("HANDICAP_VERSION", Truline.m_handicapVersion);
    prop.setProperty("TRULINE_VERSION", Truline.m_trulineVersion);
    sql = makeDelete("RACE_POST_HANDICAP", prop);
    psqlStmt = connect.prepareStatement(sql);
    psqlStmt.executeUpdate();
    sql = makeDelete("RACE_BIAS", prop);
    psqlStmt = connect.prepareStatement(sql);
    psqlStmt.executeUpdate();
   }
  }
  // Insert race-level Bias table
  insertRaceBias(race);
  // Insert post-level Handicap tables
  for (Enumeration e = race.m_posts.elements(); e.hasMoreElements();) {
   Post post = (Post) e.nextElement();
   if (Log.isDebug(Log.TRACE))
    Log.print("Insert " + race.m_track + "/" + race.m_raceDate + "/"
      + race.m_raceNo + "/" + post.m_postPosition + "\n");
   if (post.m_handicap == null || post.m_horseName == null)
    continue; // position is empty
   String entry = post.m_props.getProperty("ENTRY", "");
   if (entry.equals("S")) {
    // No handicap for scratched horses
   } else {
    insertRacePostHandicap(race, post);
   }
  }
 }
 private void insertHandicapOptions() throws Exception
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("Outputing HANDICAP_OPTIONS table for "
     + Truline.m_handicapVersion);
  Properties prop = new Properties();
  prop.setProperty("HANDICAP_VERSION", Truline.m_handicapVersion);
  prop.setProperty("TRULINE_VERSION", Truline.m_trulineVersion);
  sql = makeDelete("HANDICAP_OPTIONS", prop);
  psqlStmt = connect.prepareStatement(sql);
  psqlStmt.executeUpdate();
  prop.setProperty("MAX_VARIANT", Truline.userProps.getProperty("MaxVariant"));
  prop
    .setProperty("RECENCY_DAYS", Truline.userProps.getProperty("RecencyDays"));
  prop.setProperty("MAX_DAYS", Truline.userProps.getProperty("MaxDays"));
  prop.setProperty("USE_MAIDEN", Truline.userProps.getProperty("UseMaiden"));
  prop.setProperty("IGNORE_SURFACE",
    Truline.userProps.getProperty("IgnoreSurface"));
  prop.setProperty("BETTING_FACTOR_VERSION",
    Truline.userProps.getProperty("BetFactorVersion"));
  sql = makeInsert("HANDICAP_OPTIONS", prop);
  psqlStmt = connect.prepareStatement(sql);
  psqlStmt.executeUpdate();
 }
 private void insertRace(Race race) throws Exception
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("Outputing RACE table for " + race.m_props.getProperty("RACENO")
     + "\n");
  Properties prop = new Properties();
  prop.setProperty("TRACK_ABBR", race.m_track);
  prop.setProperty("DATE_RACE", race.m_props.getProperty("RACEDATE"));
  prop.setProperty("RACE_NO", race.m_props.getProperty("RACENO"));
  prop.setProperty("RACE_DISTANCE", Lib.ftoa((int) race.m_distance, 0));
  prop.setProperty("PURSE_CLASS", Lib.ftoa((double) race.m_purseClass, 0));
  prop.setProperty("TRACK_CLASS", Lib.ftoa((double) race.m_trackClass, 0));
  prop.setProperty("AMT_PURSE", Lib.ftoa((int) race.m_purse, 0));
  prop.setProperty("AMT_CLAIMING_PRICE", Lib.ftoa((int) race.m_claim, 0));
  prop.setProperty("RACE_SURFACE", race.m_surfaceLC);
  prop.setProperty("SURFACE_FLAG", race.m_allweather);
  if (race.m_allweather.equals("A"))
   prop.setProperty("RACE_SURFACE", race.m_allweather);   
  prop.setProperty("RACE_TYPE", race.m_raceType);
  prop.setProperty("AGE_SEX_CODE", race.m_age);
  prop.setProperty("BETTABLE_1", race.m_bettable1);
  prop.setProperty("BETTABLE_2", race.m_bettable2);
  prop.setProperty("BETTABLE_3", race.m_bettable3);
  prop.setProperty("IGNORE_RUN_LINE", race.m_ignoreRunLine);
  prop.setProperty("RESULTS_POSTED", race.m_resultsPosted);
  prop.setProperty("CNT_HORSES", Lib.ftoa((int) race.m_cnthorses, 0));
  prop.setProperty("CNT_HORSE_NRL", Lib.ftoa((int) race.m_cntnrl, 0));
  prop.setProperty("PCT_HORSE_NRL", Lib.ftoa(race.m_pctNRL, 0));
  prop.setProperty("CNT_HORSE_NRLML", Lib.ftoa((int) race.m_cntnrlML, 0));
  String raceCond = race.m_props.getProperty("COMMENTS");
  if (raceCond.length() > 500)
   raceCond = raceCond.substring(0,499);
  prop.setProperty("RACE_CONDITIONS", raceCond);
  prop.setProperty("CNT_TRNOWN", Lib.ftoa((int) race.m_cnttrnown, 0));
  prop.setProperty("CNT_TSDS", Lib.ftoa((int) race.m_cnt$d, 0));
  prop.setProperty("CNT_1ST_TIME", Lib.ftoa((int) race.m_cnt1st, 0));
  sql = makeInsert("RACE", prop);
  psqlStmt = connect.prepareStatement(sql);
  psqlStmt.executeUpdate();
 }
 private void insertRaceBias(Race race) throws Exception
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("Outputing RACE_BIAS table for "
     + race.m_props.getProperty("RACENO") + "\n");
  Properties prop = new Properties();
  prop.setProperty("TRACK_ABBR", race.m_track);
  prop.setProperty("DATE_RACE", race.m_props.getProperty("RACEDATE"));
  prop.setProperty("RACE_NO", race.m_props.getProperty("RACENO"));
  prop.setProperty("RACE_DISTANCE", Lib.ftoa((int) race.m_distance, 0));
  prop.setProperty("RACE_SURFACE", race.m_surfaceLC);
  if (!race.m_trackCondResult.equals(""))
   prop.setProperty("TRACK_CONDITION", race.m_trackCondResult);
  else 
   prop.setProperty("TRACK_CONDITION", race.m_trackCond);
  prop.setProperty("HANDICAP_VERSION", Truline.m_handicapVersion);
  prop.setProperty("TRULINE_VERSION", Truline.m_trulineVersion);
  prop.setProperty("EPS_BIAS",
    Lib.ftoa((int) biasPointsR[Handicap.EPS], 0));
  prop.setProperty("EN_BIAS",
    Lib.ftoa((int) biasPointsR[Handicap.EN], 0));
  prop.setProperty("FS_BIAS",
    Lib.ftoa((int) biasPointsR[Handicap.FS], 0));
  prop.setProperty("TT_BIAS",
    Lib.ftoa((int) biasPointsR[Handicap.TT], 0));
  prop.setProperty("SS_BIAS",
    Lib.ftoa((int) biasPointsR[Handicap.SS], 0));
  prop.setProperty("CS_BIAS",
    Lib.ftoa((int) biasPointsR[Handicap.CS], 0));
  prop.setProperty("FT_BIAS",
    Lib.ftoa((int) biasPointsR[Handicap.FT], 0));
  prop.setProperty("AS_BIAS",
    Lib.ftoa((int) biasPointsR[Handicap.AS], 0));
  prop.setProperty("RE_BIAS",
    Lib.ftoa((int) biasPointsR[Handicap.RE], 0));
  prop.setProperty("QP_BIAS",
    Lib.ftoa((int) biasPointsR[Handicap.QP], 0));
  prop.setProperty("TOTAL_BIAS", Lib.ftoa((int) biasTotalR, 0));
  if (race.m_surface.equals("D")) {
   prop.setProperty("EPS_BIAS_T",
     Lib.ftoa((int) biasPoints[Handicap.EPS], 0));
   prop.setProperty("EN_BIAS_T",
     Lib.ftoa((int) biasPoints[Handicap.EN], 0));
   prop.setProperty("FS_BIAS_T",
     Lib.ftoa((int) biasPoints[Handicap.FS], 0));
   prop.setProperty("TT_BIAS_T",
     Lib.ftoa((int) biasPoints[Handicap.TT], 0));
   prop.setProperty("SS_BIAS_T",
     Lib.ftoa((int) biasPoints[Handicap.SS], 0));
   prop.setProperty("CS_BIAS_T",
     Lib.ftoa((int) biasPoints[Handicap.CS], 0));
   prop.setProperty("FT_BIAS_T",
     Lib.ftoa((int) biasPoints[Handicap.FT], 0));
   prop.setProperty("AS_BIAS_T",
     Lib.ftoa((int) biasPoints[Handicap.AS], 0));
   prop.setProperty("RE_BIAS_T",
     Lib.ftoa((int) biasPoints[Handicap.RE], 0));
   prop.setProperty("QP_BIAS_T",
     Lib.ftoa((int) biasPoints[Handicap.QP], 0));
   prop.setProperty("TOTAL_BIAS_T", Lib.ftoa((int) biasTotal, 0));
  }
  else {
   prop.setProperty("EPS_BIAS_T",
     Lib.ftoa((int) biasPointsT[Handicap.EPS], 0));
   prop.setProperty("EN_BIAS_T",
     Lib.ftoa((int) biasPointsT[Handicap.EN], 0));
   prop.setProperty("FS_BIAS_T",
     Lib.ftoa((int) biasPointsT[Handicap.FS], 0));
   prop.setProperty("TT_BIAS_T",
     Lib.ftoa((int) biasPointsT[Handicap.TT], 0));
   prop.setProperty("SS_BIAS_T",
     Lib.ftoa((int) biasPointsT[Handicap.SS], 0));
   prop.setProperty("CS_BIAS_T",
     Lib.ftoa((int) biasPointsT[Handicap.CS], 0));
   prop.setProperty("FT_BIAS_T",
     Lib.ftoa((int) biasPointsT[Handicap.FT], 0));
   prop.setProperty("AS_BIAS_T",
     Lib.ftoa((int) biasPointsT[Handicap.AS], 0));
   prop.setProperty("RE_BIAS_T",
     Lib.ftoa((int) biasPointsT[Handicap.RE], 0));
   prop.setProperty("QP_BIAS_T",
     Lib.ftoa((int) biasPointsT[Handicap.QP], 0));
   prop.setProperty("TOTAL_BIAS_T", Lib.ftoa((int) biasTotalT, 0));
  }
  sql = makeInsert("RACE_BIAS", prop);
  psqlStmt = connect.prepareStatement(sql);
  psqlStmt.executeUpdate();
 }
 private void insertRaceProperties(Race race) throws Exception
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("Outputing RACE_PROPERTIES table for "
     + race.m_props.getProperty("RACENO") + "\n");
  Properties prop = new Properties();
  prop.setProperty("TRACK_ABBR", race.m_track);
  prop.setProperty("DATE_RACE", race.m_props.getProperty("RACEDATE"));
  prop.setProperty("RACE_NO", race.m_props.getProperty("RACENO"));
  for (Enumeration e = race.m_props.propertyNames(); e.hasMoreElements();) {
   String name = (String) e.nextElement();
   String value = race.m_props.getProperty(name);
   if (value != null && value.length() > 0) {
    prop.setProperty("PROPERTY", name);
    prop.setProperty("PVALUE", race.m_props.getProperty(name));
    sql = makeInsert("RACE_PROPERTIES", prop);
    psqlStmt = connect.prepareStatement(sql);
    psqlStmt.executeUpdate();
   }
  }
 }
 private void insertRaceResults(Race race) throws Exception
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("Outputing RACE_RESULTS table for "
     + race.m_props.getProperty("RACENO") + "\n");
  Properties prop = new Properties();
  prop.setProperty("TRACK_ABBR", race.m_track);
  prop.setProperty("DATE_RACE", race.m_props.getProperty("RACEDATE"));
  prop.setProperty("RACE_NO", race.m_props.getProperty("RACENO"));
  prop.setProperty("SADDLE_CLOTH1", race.m_cloth1);
  prop.setProperty("AMT_WIN1", race.m_win1);
  prop.setProperty("AMT_PLACE1", race.m_place1);
  prop.setProperty("AMT_SHOW1", race.m_show1);
  prop.setProperty("SADDLE_CLOTH2", race.m_cloth2);
  prop.setProperty("AMT_WIN2", race.m_win2);
  prop.setProperty("AMT_PLACE2", race.m_place2);
  prop.setProperty("AMT_SHOW2", race.m_show2);
  prop.setProperty("SADDLE_CLOTH3", race.m_cloth3);
  prop.setProperty("AMT_WIN3", race.m_win3);
  prop.setProperty("AMT_PLACE3", race.m_place3);
  prop.setProperty("AMT_SHOW3", race.m_show3);
  prop.setProperty("AMT_EXACTA", race.m_exactaPayoff);
  prop.setProperty("AMT_TRIFECTA", race.m_trifectaPayoff);
  prop.setProperty("AMT_SUPERFECTA", race.m_superPayoff);
  prop.setProperty("AMT_PICK3", race.m_pick3Payoff);
  prop.setProperty("AMT_PICK4", race.m_pick4Payoff);
  prop.setProperty("AMT_PICK6", race.m_pick6Payoff);
  prop.setProperty("AMT_DOUBLE", race.m_doublePayoff);
  prop.setProperty("SADDLE_CLOTH4", race.m_cloth4);
  prop.setProperty("TRACK_CONDITION", race.m_trackCondResult);
  sql = makeInsert("RACE_RESULTS", prop);
  psqlStmt = connect.prepareStatement(sql);
  psqlStmt.executeUpdate();
 }
 private void insertRacePost(Race race, Post post) throws Exception
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("Outputing RACE_POST table for "
     + race.m_props.getProperty("RACENO") + "/"
     + Lib.ftoa((int) post.m_postPosition, 0) + "\n");
  Properties prop = new Properties();
  prop.setProperty("TRACK_ABBR", race.m_track);
  prop.setProperty("DATE_RACE", race.m_props.getProperty("RACEDATE"));
  prop.setProperty("RACE_NO", race.m_props.getProperty("RACENO"));
  prop.setProperty("SADDLE_CLOTH", post.cloth);
  prop.setProperty("POST_POS", Lib.ftoa((int) post.m_postPosition, 0));
  prop.setProperty("HORSE_NAME", post.m_horseName);
  prop.setProperty("HORSE_NAME_P", post.m_horseNameP);
  prop.setProperty("WEIGHT", Lib.ftoa((int) post.m_weight, 0));
  prop.setProperty("AGE", Lib.ftoa((int) post.m_age, 0));
  prop.setProperty("SEX", post.m_sex);
  prop.setProperty("MORNING_LINE", post.m_props.getProperty("MORNINGLINE", ""));
  prop.setProperty("MORNING_LINE_P", post.m_morningLine);
  String entry = post.m_props.getProperty("ENTRY", "");
  if (entry.equals("S")) 
   prop.setProperty("ML_RANK","99");
  else
   prop.setProperty("ML_RANK",Lib.ftoa((int) post.m_handicap.rank[Handicap.ML], 0));
  if (entry.equals("S")) 
   prop.setProperty("FO_RANK","99");
  else
   prop.setProperty("FO_RANK",Lib.ftoa((int) post.m_handicap.rank[Handicap.FO], 0));
  prop.setProperty("TRAINER_NAME", post.m_trainerName);
  prop.setProperty("TRAINER_WIN_PCT", Lib.ftoa((int) post.m_trnPct, 0));
  prop.setProperty("JOCKEY_NAME", post.m_jockeyName);
  prop.setProperty("JOCKEY_WIN_PCT", Lib.ftoa((int) post.m_jkyPct, 0));
  prop.setProperty("DIST_WINS", post.m_props.getProperty("LRDWINS", "").trim());
  prop.setProperty("DIST_PLACES", post.m_props.getProperty("LRDPLACES", "").trim());
  prop.setProperty("DIST_RACES", post.m_props.getProperty("LRDSTARTS", "").trim());
  prop.setProperty("LIFE_WINS", post.m_props.getProperty("LIFETIMEWINS", "").trim());
//prop.setProperty("LIFE_PLACES", post.m_props.getProperty("LRTPLACES", "").trim());
  prop.setProperty("LIFE_RACES", post.m_props.getProperty("LIFETIMESTARTS", "").trim());
  prop.setProperty("TRACK_WINS", post.m_props.getProperty("LRTWINS", "").trim());
//prop.setProperty("TRACK_PLACES", post.m_props.getProperty("LRTPLACES", "").trim());
  prop.setProperty("TRACK_RACES", post.m_props.getProperty("LRTSTARTS", "").trim());
  if (race.m_surface.equals("T")) {
   prop.setProperty("SURFACE_WINS", post.m_props.getProperty("LRTURFWINS", "0").trim());
//   prop.setProperty("SURFACE_PLACES", post.m_props.getProperty("LRTURFPLACES", "0").trim());
   prop.setProperty("SURFACE_RACES", post.m_props.getProperty("LRTURFSTARTS", "0").trim());
  } else if (race.m_surface.equals("A")) {
   prop.setProperty("SURFACE_WINS", post.m_props.getProperty("LRAWEWINS", "0").trim());
//   prop.setProperty("SURFACE_PLACES", post.m_props.getProperty("LRAWEPLACES", "0").trim());
   prop.setProperty("SURFACE_RACES", post.m_props.getProperty("LRAWESTARTS", "0").trim());
  } else {
   int surf = Lib.atoi(post.m_props.getProperty("LIFETIMEWINS", "0"))
     - Lib.atoi(post.m_props.getProperty("LRTURFWINS", "0"))
     - Lib.atoi(post.m_props.getProperty("LRAWEWINS", "0"));
   prop.setProperty("SURFACE_WINS", Lib.ftoa((int) surf, 0));
   surf = Lib.atoi(post.m_props.getProperty("LIFETIMEPLACES", "0"))
     - Lib.atoi(post.m_props.getProperty("LRTURFPLACES", "0"))
     - Lib.atoi(post.m_props.getProperty("LRAWEPLACES", "0"));
//   prop.setProperty("SURFACE_PLACES", Lib.ftoa((int) surf, 0));
   surf = Lib.atoi(post.m_props.getProperty("LIFETIMESTARTS", "0"))
     - Lib.atoi(post.m_props.getProperty("LRTURFSTARTS", "0"))
     - Lib.atoi(post.m_props.getProperty("LRAWESTARTS", "0"));
   prop.setProperty("SURFACE_RACES", Lib.ftoa((int) surf, 0));
  }
  prop.setProperty("RUNNING_STYLE", post.m_props.getProperty("RUNSTYLE", "").trim());
  String ownerName = post.m_props.getProperty("OWNER");
  if (ownerName.length() > 100)
   ownerName = ownerName.substring(0,99);
  prop.setProperty("OWNER_NAME", ownerName);
  prop.setProperty("SIRE_NAME", post.m_sireName);
  prop.setProperty("SIRE_AWD", post.m_sireAWD);
  prop.setProperty("SIRE_APRS", Lib.ftoa((int) post.m_sireAPRS, 0));
  prop.setProperty("DAM_NAME", post.m_damName);
  prop.setProperty("DAM_AWD", post.m_damAWD);
  prop.setProperty("DAM_APRS", Lib.ftoa((int) post.m_damAPRS, 0));
  prop.setProperty("DAM_SIRE_NAME", post.m_damSireName);
  prop.setProperty("DAM_SIRE_AWD", post.m_damSireAWD);
  prop.setProperty("DAM_SIRE_APRS", Lib.ftoa((int) post.m_damSireAPRS, 0));
  prop.setProperty("SIRE_SIRE_NAME", post.m_sireSireName);
  prop.setProperty("FINISH_POS", post.m_finishPos);
  prop.setProperty("ODDS", post.m_odds);
  // prop.setProperty("AMT_WIN", post.m_winPayoff);
  // prop.setProperty("AMT_PLACE", post.m_placePayoff);
  // prop.setProperty("AMT_SHOW", post.m_showPayoff);
  prop.setProperty("OWNER_TRAINER", post.m_ownerTrn);
  prop.setProperty("RUN_STYLE", post.m_runStyle);
  sql = makeInsert("RACE_POST", prop);
  psqlStmt = connect.prepareStatement(sql);
  psqlStmt.executeUpdate();
 }
 private void insertRacePostProperties(Race race, Post post) throws Exception
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("Outputing RACE_POST_PROPERTIES table for "
     + race.m_props.getProperty("RACENO") + "/"
     + Lib.ftoa((int) post.m_postPosition, 0) + "\n");
  Properties prop = new Properties();
  prop.setProperty("TRACK_ABBR", race.m_track);
  prop.setProperty("DATE_RACE", race.m_props.getProperty("RACEDATE"));
  prop.setProperty("RACE_NO", race.m_props.getProperty("RACENO"));
  prop.setProperty("SADDLE_CLOTH", post.cloth);
  prop.setProperty("POST_POS", Lib.ftoa((int) post.m_postPosition, 0));
  for (Enumeration e = post.m_props.propertyNames(); e.hasMoreElements();) {
   String name = (String) e.nextElement();
   String value = post.m_props.getProperty(name);
   if (value != null && value.length() > 0 && name != null && name.length() > 0) {
    prop.setProperty("PROPERTY", name);
    prop.setProperty("PVALUE", post.m_props.getProperty(name));
    sql = makeInsert("RACE_POST_PROPERTIES", prop);
    psqlStmt = connect.prepareStatement(sql);
    psqlStmt.executeUpdate();
   }
  }
 }
 private void insertRacePostPastPerf(Race race, Post post, Performance p)
   throws Exception
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("Outputing RACE_POST_PAST_PERF table for "
     + race.m_props.getProperty("RACENO") + "/"
     + Lib.ftoa((int) post.m_postPosition, 0) + "\n");
  Properties prop = new Properties();
  prop.setProperty("TRACK_ABBR", race.m_track);
  prop.setProperty("DATE_RACE", race.m_props.getProperty("RACEDATE"));
  prop.setProperty("RACE_NO", race.m_props.getProperty("RACENO"));
  prop.setProperty("SADDLE_CLOTH", post.cloth);
  prop.setProperty("POST_POS", Lib.ftoa((int) post.m_postPosition, 0));
  prop.setProperty("TRACK_ABBR_PP", p.ppTrack);
  prop.setProperty("DATE_RACE_PP", Lib.datetoa(p.ppRaceDate));
  prop.setProperty("RACE_NO_PP", Lib.ftoa((int) p.ppRaceNo, 0));
  prop.setProperty("POST_POS_PP", Lib.ftoa((int) p.ppPostPosition, 0));
  String s = new Boolean(p.isExcluded).toString();
  prop.setProperty("IS_EXCLUDED_PP", s.substring(0, 1));
  s = new Boolean(p.isRoute).toString();
  prop.setProperty("IS_ROUTE_PP", s.substring(0, 1));
  prop.setProperty("DRF_SPEED_PP", Lib.ftoa((double) p.drfSpeed, 0));
  prop.setProperty("VARIANT_PP", Lib.ftoa((double) p.variant, 0));
  prop.setProperty("FS_PP", Lib.ftoa((double) p.fs, 0));
  prop.setProperty("SS_PP", Lib.ftoa((double) p.ss, 0));
  prop.setProperty("FT_PP", Lib.ftoa((double) p.ft, 0));
  prop.setProperty("TT_PP", Lib.ftoa((double) p.tt, 0));
  prop.setProperty("CS_PP", Lib.ftoa((double) p.cs, 0));
  prop.setProperty("AS_PP", Lib.ftoa((double) p.as, 0));
  sql = makeInsert("RACE_POST_PAST_PERF", prop);
  psqlStmt = connect.prepareStatement(sql);
  psqlStmt.executeUpdate();
 }
 private void insertRacePostPastPerfProperties(Race race, Post post,
   Performance p) throws Exception
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("Outputing RACE_POST_PAST_PERF_PROPERTIES table for "
     + race.m_props.getProperty("RACENO") + "/"
     + Lib.ftoa((int) post.m_postPosition, 0) + "\n");
  Properties prop = new Properties();
  prop.setProperty("TRACK_ABBR", race.m_track);
  prop.setProperty("DATE_RACE", race.m_props.getProperty("RACEDATE"));
  prop.setProperty("RACE_NO", race.m_props.getProperty("RACENO"));
  prop.setProperty("SADDLE_CLOTH", post.cloth);
  prop.setProperty("POST_POS", Lib.ftoa((int) post.m_postPosition, 0));
  for (Enumeration e = p.m_props.propertyNames(); e.hasMoreElements();) {
   String name = (String) e.nextElement();
   String value = p.m_props.getProperty(name);
   if (value != null && value.length() > 0) {
    prop.setProperty("PROPERTY", name);
    prop.setProperty("PVALUE", p.m_props.getProperty(name));
    sql = makeInsert("RACE_POST_PAST_PERF_PROPERTIES", prop);
    psqlStmt = connect.prepareStatement(sql);
    psqlStmt.executeUpdate();
   }
  }
 }
 private void insertRacePostWork(Race race, Post post)
   throws Exception
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("Outputing RACE_POST_WORK table for "
     + race.m_props.getProperty("RACENO") + "/"
     + Lib.ftoa((int) post.m_postPosition, 0) + "\n");
  Properties prop = new Properties();
  prop.setProperty("TRACK_ABBR", race.m_track);
  prop.setProperty("DATE_RACE", race.m_props.getProperty("RACEDATE"));
  prop.setProperty("RACE_NO", race.m_props.getProperty("RACENO"));
  prop.setProperty("SADDLE_CLOTH", post.cloth);
  prop.setProperty("POST_POS", Lib.ftoa((int) post.m_postPosition, 0));
  prop.setProperty("WORK_COUNT", Lib.ftoa((int) post.m_workCnt, 0));
  for (int i = 0; i < post.m_workCnt; i++) {
   Properties props = post.m_work[i].m_props;
   String workdate = Lib.datetoa(post.m_work[i].m_workDate);
   String dist = props.getProperty("WORKDISTANCE");
   String rank = props.getProperty("WORKRANK", "");
   String workQty = props.getProperty("WORKQTY");
   if (i == 0) {
    prop.setProperty("DATE_WORK_1", workdate);
    prop.setProperty("DISTANCE_1", dist);
    prop.setProperty("RANK_1", rank);
    prop.setProperty("QUANTITY_1", workQty);
   }
   if (i == 1) {
    prop.setProperty("DATE_WORK_2", workdate);
    prop.setProperty("DISTANCE_2", dist);
    prop.setProperty("RANK_2", rank);
    prop.setProperty("QUANTITY_2", workQty);
   }
   if (i == 2) {
    prop.setProperty("DATE_WORK_3", workdate);
    prop.setProperty("DISTANCE_3", dist);
    prop.setProperty("RANK_3", rank);
    prop.setProperty("QUANTITY_3", workQty);
   }
   if (i == 3) {
    prop.setProperty("DATE_WORK_4", workdate);
    prop.setProperty("DISTANCE_4", dist);
    prop.setProperty("RANK_4", rank);
    prop.setProperty("QUANTITY_4", workQty);
   }
   if (i == 4) {
    prop.setProperty("DATE_WORK_5", workdate);
    prop.setProperty("DISTANCE_5", dist);
    prop.setProperty("RANK_5", rank);
    prop.setProperty("QUANTITY_5", workQty);
   }
   if (i == 5) {
    prop.setProperty("DATE_WORK_6", workdate);
    prop.setProperty("DISTANCE_6", dist);
    prop.setProperty("RANK_6", rank);
    prop.setProperty("QUANTITY_6", workQty);
   }
  }
  sql = makeInsert("RACE_POST_WORK", prop);
  psqlStmt = connect.prepareStatement(sql);
  psqlStmt.executeUpdate();
 }
 private void insertRacePostTrainerJockeyStats(Race race, Post post,
   TrainerJockeyStats tjs) throws Exception
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("Outputing RACE_POST_TRAINER_JOCKEY_STATS table for "
     + race.m_props.getProperty("RACENO") + "/"
     + Lib.ftoa((int) post.m_postPosition, 0) + "\n");
  String cat = "";
  int sts = 0;
  int win = 0;
  int itm = 0;
  double roi = 0;
  Properties prop = new Properties();
  prop.setProperty("TRACK_ABBR", race.m_track);
  prop.setProperty("DATE_RACE", race.m_props.getProperty("RACEDATE"));
  prop.setProperty("RACE_NO", race.m_props.getProperty("RACENO"));
  prop.setProperty("SADDLE_CLOTH", post.cloth);
  prop.setProperty("POST_POS", Lib.ftoa((int) post.m_postPosition, 0));
  /*  new method is specific
  for (Enumeration e = tjs.m_props.propertyNames(); e.hasMoreElements();) {
   String name = (String) e.nextElement();
   String value = tjs.m_props.getProperty(name);
   if (value != null && value.length() > 0) {
    prop.setProperty("PROPERTY", name);
    prop.setProperty("PVALUE", tjs.m_props.getProperty(name));
    */
   for (int k = 1; k < 7; k++) {
    cat = tjs.m_props.getProperty("TRAINERCAT"+k, "N/A");
    win = Lib.atoi(tjs.m_props.getProperty("TRAINERWIN"+k, "0"));
    itm = Lib.atoi(tjs.m_props.getProperty("TRAINERITM"+k, "0"));
    roi = Lib.atof(tjs.m_props.getProperty("TRAINERROI"+k, "0"));
    if (cat.indexOf("Claimed") >= 0 || cat.indexOf("FTS") >= 0) {
       prop.setProperty("CATEGORY", cat); 
       prop.setProperty("WIN_PCT", Lib.ftoa((int) win,0));
       prop.setProperty("ROI", Lib.ftoa((double) roi, 2));
       sql = makeInsert("RACE_POST_TRAINER_JOCKEY_STATS", prop);
       psqlStmt = connect.prepareStatement(sql);
       psqlStmt.executeUpdate();
    }
  }
 }
 private void insertRacePostHandicap(Race race, Post post) throws Exception
 {
  String repRaceDate;
  if (Log.isDebug(Log.TRACE))
   Log.print("Outputing RACE_POST_HANDICAP table for "
     + race.m_props.getProperty("RACENO") + "/"
     + Lib.ftoa((int) post.m_postPosition, 0) + "\n");
  Properties prop = new Properties();
  prop.setProperty("TRACK_ABBR", race.m_track);
  prop.setProperty("DATE_RACE", race.m_props.getProperty("RACEDATE"));
  prop.setProperty("RACE_NO", race.m_props.getProperty("RACENO"));
  prop.setProperty("SADDLE_CLOTH", post.cloth);
  prop.setProperty("POST_POS", Lib.ftoa((int) post.m_postPosition, 0));
  prop.setProperty("HANDICAP_VERSION", Truline.m_handicapVersion);
  prop.setProperty("TRULINE_VERSION", Truline.m_trulineVersion);
  prop.setProperty("EPS_RANK",
    Lib.ftoa((int) post.m_handicap.rank[Handicap.EPS], 3));
  prop.setProperty("EPS_VALUE",
    Lib.ftoa((double) post.m_handicap.value[Handicap.EPS], 0));
  prop.setProperty("EN_RANK",
    Lib.ftoa((int) post.m_handicap.rank[Handicap.EN], 0));
  prop.setProperty("EN_VALUE",
    Lib.ftoa((double) post.m_handicap.value[Handicap.EN], 1));
  prop.setProperty("FS_RANK",
    Lib.ftoa((int) post.m_handicap.rank[Handicap.FS], 0));
  prop.setProperty("FS_VALUE",
    Lib.ftoa((double) post.m_handicap.value[Handicap.FS], 1));
  prop.setProperty("TT_RANK",
    Lib.ftoa((int) post.m_handicap.rank[Handicap.TT], 0));
  prop.setProperty("TT_VALUE",
    Lib.ftoa((double) post.m_handicap.value[Handicap.TT], 1));
  prop.setProperty("SS_RANK",
    Lib.ftoa((int) post.m_handicap.rank[Handicap.SS], 0));
  prop.setProperty("SS_VALUE",
    Lib.ftoa((double) post.m_handicap.value[Handicap.SS], 1));
  prop.setProperty("CS_RANK",
    Lib.ftoa((int) post.m_handicap.rank[Handicap.CS], 0));
  prop.setProperty("CS_VALUE",
    Lib.ftoa((double) post.m_handicap.value[Handicap.CS], 1));
  prop.setProperty("TTCS_RANK",
    Lib.ftoa((int) post.m_handicap.rank[Handicap.TTCS], 0));
  prop.setProperty("TTCS_VALUE",
    Lib.ftoa((double) post.m_handicap.value[Handicap.TTCS], 1));
  prop.setProperty("FT_RANK",
    Lib.ftoa((int) post.m_handicap.rank[Handicap.FT], 0));
  prop.setProperty("FT_VALUE",
    Lib.ftoa((double) post.m_handicap.value[Handicap.FT], 1));
  prop.setProperty("AS_RANK",
    Lib.ftoa((int) post.m_handicap.rank[Handicap.AS], 0));
  prop.setProperty("AS_VALUE",
    Lib.ftoa((double) post.m_handicap.value[Handicap.AS], 0));
  prop.setProperty("RE_RANK",
    Lib.ftoa((int) post.m_handicap.rank[Handicap.RE], 0));
  prop.setProperty("RE_VALUE",
    Lib.ftoa((double) post.m_handicap.value[Handicap.RE], 0));
  prop.setProperty("QP_RANK",
    Lib.ftoa((int) post.m_handicap.rank[Handicap.QP], 0));
  prop.setProperty("QP_VALUE",
    Lib.ftoa((double) post.m_handicap.value[Handicap.QP], 0));
  prop.setProperty("PP_RANK",
    Lib.ftoa((int) post.m_handicap.rank[Handicap.PP], 0));
  prop.setProperty("PP_VALUE",
    Lib.ftoa((double) post.m_handicap.value[Handicap.PP], 0));
  prop.setProperty("POINTS", Lib.ftoa((int) post.m_handicap.points, 0));
  prop.setProperty("BONUS", Lib.ftoa((int) post.m_handicap.bonus, 0));
  prop.setProperty("BONUS_RANK", Lib.ftoa((int) post.m_handicap.bonusRank, 0));
  prop.setProperty("MBEN_FLAG", post.m_handicap.extraFlg ? "#" : " ");
  prop.setProperty("TRAINER_PT", post.m_trainerNamePT);
  prop.setProperty("SIRE_TS", post.m_sireTSp);
  prop.setProperty("SIRE_TS2", post.m_sireTS2);
  prop.setProperty("OWNER_TRAINER", post.m_ownerTrn);
  prop.setProperty("OWNER_BREEDER", post.m_ownerBrd);
  prop.setProperty("TRAINER_JOCKEY", post.m_trnJky);
  prop.setProperty("TRAINER_JOCKEY_HOT", post.m_trnJkyHot);
  prop.setProperty("BULLET_WORK", post.m_5furlongBullet);
  prop.setProperty("BIAS_POINTS", Lib.ftoa((int) biasBonus,0));
  if (post.m_handicap.m_repRace != null)
   repRaceDate = Lib.datetoa(post.m_handicap.m_repRace.ppRaceDate);
  else
   repRaceDate = "";
  prop.setProperty("REP_RACE_DATE", repRaceDate);
  if (post.m_handicap.m_recency != null)
   repRaceDate = Lib.datetoa(post.m_handicap.m_recency.ppRaceDate);
  else
   repRaceDate = "";
  prop.setProperty("RE_RACE_DATE", repRaceDate);
  prop.setProperty("CNT_TOPRANKS", Lib.ftoa((int) post.m_topRanks, 0));
  prop.setProperty("ODDS", Lib.ftoa((double) post.m_truLineD, 1));
  prop.setProperty("POINTS_ADV", Lib.ftoa((int) post.m_pointsAdv, 0));
  prop.setProperty("BF_FACTORS", Lib.ftoa((int) post.m_betfactors, 0));
  prop.setProperty("TF_FACTORS", Lib.ftoa((int) post.m_trnfactors, 0));
  prop.setProperty("FLOW_BETS", Lib.ftoa((int) post.cntHorseFlows, 0));
  prop.setProperty("DAYS_SINCE_LAST", Lib.ftoa((int) post.m_daysSinceLast, 0));
  prop.setProperty("DAYS_SINCE_WORK1", Lib.ftoa((int) post.m_daysSinceWork1, 0));
  prop.setProperty("DAYS_SINCE_WORK2", Lib.ftoa((int) post.m_daysSinceWork2, 0));
  prop.setProperty("DAYS_SINCE_WORK3", Lib.ftoa((int) post.m_daysSinceWork3, 0));
  prop.setProperty("LAYOFF_OK_TRN", post.m_trainer45LayoffOK ? "T" : "F");
  if (post.m_lastRaceTrackClass.equals(""))  
   prop.setProperty("TRACK_CLASS_CHG", "");
  else
   prop.setProperty("TRACK_CLASS_CHG", post.m_lastRaceTrackClass.substring(6, 7));
  prop.setProperty("LAST_RACE_PURSE_CHG", Lib.ftoa(post.m_lastRaceClassChg, 0));
  prop.setProperty("LAST_RACE_CLAIM_CHG", Lib.ftoa(post.m_lastRaceClaimChg, 0));
  prop.setProperty("FIRST_CLM_RACE", post.m_firstClmRace ? "T" : "F");
  prop.setProperty("LOWEST_CLM_PRICE", post.m_lowestClmPrice ? "T" : "F");
  prop.setProperty("BIG_WIN_LAST", post.m_bigWinLast ? "T" : "F");
  prop.setProperty("MDN_CLM_WIN_LAST", post.m_lastMdnClmWin ? "T" : "F");
  prop.setProperty("JOCKEY_CHG", post.m_jockeyChgToday ? "T" : "F");
  prop.setProperty("TRAINER_CHG", post.m_trainerChgToday ? "T" : "F");
  sql = makeInsert("RACE_POST_HANDICAP", prop);
  psqlStmt = connect.prepareStatement(sql);
  psqlStmt.executeUpdate();
  if (post.m_trnJkyfactorsSD.equals("Y") || post.m_trnJkyfactorsTYP.equals("Y")
    || post.m_trnJkyfactorsFAV.equals("Y") || post.m_trnJkyfactorsTRN.equals("Y")
    || post.m_trnJkyfactorsWP.equals("Y"))
     insertRacePostFlowBet(race, post);
  // if (post.cntHorseFlows >= 0)
  //  insertRacePostFlowBet(race, post);
 }
 private void insertRacePostFlowBet(Race race, Post post) throws Exception
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("Outputing RACE_POST_FLOW_BET table for "
     + race.m_props.getProperty("RACENO") + "/"
     + Lib.ftoa((int) post.m_postPosition, 0) + "\n");
  Properties prop = new Properties();
  prop.setProperty("DATE_RACE", race.m_props.getProperty("RACEDATE"));
  prop.setProperty("TRACK_ABBR", race.m_track);
  prop.setProperty("RACE_NO", race.m_props.getProperty("RACENO"));
  prop.setProperty("SADDLE_CLOTH", post.cloth);
  prop.setProperty("HORSE_NAME", post.m_horseName);
  prop.setProperty("DAYS_SINCE_LAST", Lib.ftoa((int) post.m_daysSinceLast, 0));
  if (post.m_lastRaceTrackClass.equals(""))  
   prop.setProperty("TRACK_CLASS_CHG", "");
  else
   prop.setProperty("TRACK_CLASS_CHG", post.m_lastRaceTrackClass.substring(6, 7));
  prop.setProperty("CNT_FTS", Lib.ftoa((int) race.m_cnt1st, 0));
  prop.setProperty("RACE_TYPE", race.m_raceType);
  prop.setProperty("RACE_SURFACE", race.m_surfaceLC);
  if (race.m_allweather.equals("A"))
   prop.setProperty("RACE_SURFACE", race.m_allweather);   
  prop.setProperty("DIST", Lib.ftoa(((double) race.m_distance) / Handicap.YdPerF, 1) + "F");
  prop.setProperty("SD", post.m_trnJkyfactorsSD);
  prop.setProperty("TYP", post.m_trnJkyfactorsTYP);
  prop.setProperty("SEX", post.m_trnJkyfactorsSEX);
  prop.setProperty("AGE", post.m_trnJkyfactorsAGE);
  prop.setProperty("FTS", post.m_trnJkyfactorsWP);
  prop.setProperty("LAYOFF", post.m_trnJkyfactorsLAY);
  prop.setProperty("FAV", post.m_trnJkyfactorsFAV);
  prop.setProperty("SOURCE", post.m_trnJkyfactorsSOURCE);
  prop.setProperty("TRAINER_NAME", post.m_trainerName);
  prop.setProperty("TRAINER_WIN_PCT", Lib.ftoa((int) post.m_trnPct, 0));
  prop.setProperty("JOCKEY_NAME", post.m_jockeyName);
  prop.setProperty("JOCKEY_WIN_PCT", Lib.ftoa((int) post.m_jkyPct, 0));
  prop.setProperty("BONUS_RANK", Lib.ftoa((int) post.m_handicap.bonusRank, 0));
  prop.setProperty("MORNING_LINE", Lib.ftoa((double) post.m_morningLineD,2));
  prop.setProperty("ODDS", post.m_odds);
  prop.setProperty("FINISH_POS", post.m_finishPos);
  prop.setProperty("WIN_PAY", post.m_winPayoff);
  prop.setProperty("PLC_PAY", post.m_placePayoff);
  prop.setProperty("SHw_PAY", post.m_showPayoff);
  sql = makeInsert("RACE_POST_FLOW_BET", prop);
  psqlStmt = connect.prepareStatement(sql);
  psqlStmt.executeUpdate();
 }
 private void insertRaceFlowBetAK(Race race) throws Exception
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("Outputing RACE_FLOW_BET_AK table for "
     + race.m_props.getProperty("RACENO") + "\n");
  Properties prop = new Properties();
  prop.setProperty("TRACK_ABBR", race.m_track);
  prop.setProperty("DATE_RACE", race.m_props.getProperty("RACEDATE"));
  prop.setProperty("RACE_NO", race.m_props.getProperty("RACENO"));
  prop.setProperty("FLOW_BETS", Lib.ftoa((int) race.cntRaceFlowsAK+1, 0));
  int i = 20-race.cntRaceFlowsAK;
  while (i <= 20) {
   prop.setProperty("BET_NUMBER", Lib.ftoa((int) i+1, 0));
   prop.setProperty("POSSIBLE_BET", race.raceFlowsAK[i]);
   if (race.raceFlowsAK[i] != null) {
    sql = makeInsert("RACE_FLOW_BET", prop);
    psqlStmt = connect.prepareStatement(sql);
    psqlStmt.executeUpdate();
    // insert flow bet for horse if no results yet
    if (!race.m_resultsPosted.equals("Y")) {
      if (i == 0 && Log.isDebug(Log.TRACE))
       Log.print("Outputing RACE_POST_FLOW_BET_AK table for "
         + race.m_props.getProperty("RACENO") + race.raceFlowsAK[i] + "\n");
      Properties prop2 = new Properties();
      prop2.setProperty("TRACK_ABBR", race.m_track);
      prop2.setProperty("DATE_RACE", race.m_props.getProperty("RACEDATE"));
      prop2.setProperty("RACE_NO", race.m_props.getProperty("RACENO"));
      prop2.setProperty("SADDLE_CLOTH", race.raceFlowsAK[i].substring(0,race.raceFlowsAK[i].indexOf(" ")));
      prop2.setProperty("BET_NUMBER", "1");
      prop2.setProperty("FLOW_BET", race.raceFlowsAK[i]);
      sql = makeInsert("RACE_POST_FLOW_BET", prop2);
      psqlStmt = connect.prepareStatement(sql);
      psqlStmt.executeUpdate();
     }
    }   
   i++;
  }

 }
 private void insertRaceFlowBet(Race race) throws Exception
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("Outputing RACE_FLOW_BET table for "
     + race.m_props.getProperty("RACENO") + "\n");
  Properties prop = new Properties();
  prop.setProperty("TRACK_ABBR", race.m_track);
  prop.setProperty("DATE_RACE", race.m_props.getProperty("RACEDATE"));
  prop.setProperty("RACE_NO", race.m_props.getProperty("RACENO"));
  prop.setProperty("FLOW_BETS", Lib.ftoa((int) race.cntRaceFlows+1, 0));
  for (int i = 0; i <= race.cntRaceFlows; i++) {
   prop.setProperty("BET_NUMBER", Lib.ftoa((int) i+1, 0));
   prop.setProperty("POSSIBLE_BET", race.raceFlows[i].substring(race.raceFlows[i].indexOf(" ")+1));
   if (race.raceFlows[i] != null) {
    sql = makeInsert("RACE_FLOW_BET", prop);
    psqlStmt = connect.prepareStatement(sql);
    psqlStmt.executeUpdate();
    // insert flow bet for horse if no results yet
    if (!race.m_resultsPosted.equals("Y")) {
     if (race.raceFlows[i].indexOf("FB") > 0) {
      if (i == 0 && Log.isDebug(Log.TRACE))
       Log.print("Outputing RACE_POST_FLOW_BET table for "
         + race.m_props.getProperty("RACENO") + race.raceFlows[i].substring(race.raceFlows[i].indexOf(" ")+1) + "\n");
      Properties prop2 = new Properties();
      prop2.setProperty("TRACK_ABBR", race.m_track);
      prop2.setProperty("DATE_RACE", race.m_props.getProperty("RACEDATE"));
      prop2.setProperty("RACE_NO", race.m_props.getProperty("RACENO"));
      prop2.setProperty("SADDLE_CLOTH", race.raceFlows[i].substring(0,race.raceFlows[i].indexOf(" ")));
      prop2.setProperty("BET_NUMBER", "1");
      prop2.setProperty("FLOW_BET", race.raceFlows[i].substring(race.raceFlows[i].indexOf(" ")+1));
      sql = makeInsert("RACE_POST_FLOW_BET", prop2);
      psqlStmt = connect.prepareStatement(sql);
      psqlStmt.executeUpdate();
     }
    }
   }
  }
 }
 /**
  * Build an insert SQL command.
  * 
  * @param tableName
  *         - The name of the table.
  * @param prop
  *         - The property list
  */
 private String makeInsert(String tableName, Properties prop)
 {
  String sqlNames = "";
  String sqlValues = "";
  for (Enumeration e = prop.propertyNames(); e.hasMoreElements();) {
   String name = (String) e.nextElement();
   String value = prop.getProperty(name);
   if (value != null && value.length() > 0) {
    sqlNames += name + ", ";
    // Replace all (')s with ('')
    int j = 0;
    int k;
    while ((k = value.indexOf('\'', j)) > -1) {
     value = value.substring(0, k + 1) + value.substring(k);
     j = k + 2;
    }
    sqlValues += "'" + value + "', ";
   }
  }
  int idx = sqlNames.lastIndexOf(",");
  sqlNames = sqlNames.substring(0, idx); // remove the last ", "
  idx = sqlValues.lastIndexOf(",");
  sqlValues = sqlValues.substring(0, idx); // remove the last ", "
  String sql = "INSERT INTO " + tableName + " (" + sqlNames + ") VALUES ( "
    + sqlValues + ");";
  return sql;
 }
 /**
  * Build a delete SQL command
  * 
  * @param tableName
  *         - The name of the table.
  * @param keys
  *         - an array of key names for the where clause.
  */
 private String makeDelete(String tableName, Properties prop)
 {
  String sql = "DELETE FROM " + tableName + " WHERE ";
  for (Enumeration e = prop.propertyNames(); e.hasMoreElements();) {
   String name = (String) e.nextElement();
   String value = prop.getProperty(name);
   if (value != null && value.length() > 0) {
    // Replace all (')s with ('')
    int j = 0;
    int k;
    while ((k = value.indexOf('\'', j)) > -1) {
     value = value.substring(0, k + 1) + value.substring(k);
     j = k + 2;
    }
    sql += name + " = '" + value + "' AND ";
   } else
    sql += name + " = NULL AND ";
  }
  int idx = sql.lastIndexOf("AND");
  sql = sql.substring(0, idx); // remove the last "AND"
  sql += ";";
  return sql;
 }
 public void accumulateBias(Race race)
 {
  for (int i = 0; i < 10; i++)
   biasPointsR[i] = 0;
  biasTotalR = 0;
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
      if (race.m_surface.equals("D")) {
       biasPoints[i] = biasPoints[i] + 2;
       biasTotal = biasTotal + 2;
      }
      else {
       biasPointsT[i] = biasPointsT[i] + 2;
       biasTotalT = biasTotalT + 2;
      }
      biasPointsR[i] = biasPointsR[i] + 2;
      biasTotalR = biasTotalR + 2;
     }
     if ((Truline.userProps.getProperty("TrackTheBias", "N").equals("1") && post.m_handicap.rank[i] == 2)) {
      if (race.m_surface.equals("D")) {
       biasPoints[i] = biasPoints[i] + 1;
       biasTotal = biasTotal + 1;
      }
      else {
       biasPointsT[i] = biasPointsT[i] + 1;
       biasTotalT = biasTotalT + 1;
      }
      biasPointsR[i] = biasPointsR[i] + 1;
      biasTotalR = biasTotalR + 1;
     }
    }
   }
   if (finishPos.equals("2")) {
    for (int i = 0; i < 10; i++) {
     if ((Truline.userProps.getProperty("TrackTheBias", "N").equals("1") && post.m_handicap.rank[i] == 1)
       || (Truline.userProps.getProperty("TrackTheBias", "N").equals("2") && post.m_handicap.rank[i] < 3)) {
      if (race.m_surface.equals("D")) {
       biasPoints[i] = biasPoints[i] + 1;
       biasTotal = biasTotal + 1;
      }
      else {
       biasPointsT[i] = biasPointsT[i] + 1;
       biasTotalT = biasTotalT + 1;
      }
      biasPointsR[i] = biasPointsR[i] + 1;
      biasTotalR = biasTotalR + 1;
     }
    }
   }
  }
 }
 }
