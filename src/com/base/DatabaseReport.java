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
  "RE", "QP", "EN", "EPS"          };
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
    connect = DriverManager.getConnection("jdbc:mysql://localhost/"
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
   sql = makeDelete("RACE_POST_FLOW_BET", prop);
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
   sql = makeDelete("RACE_POST_FLOW_BET", prop);
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
}
  // Insert race-level tables
  insertRace(race);
  insertRaceBias(race);
  insertRaceResults(race);
  if (m_mode == Truline.DATABASE1) {
   insertRaceProperties(race);
  }
  // Insert post-level tables
  for (Enumeration e = race.m_posts.elements(); e.hasMoreElements();) {
   Post post = (Post) e.nextElement();
   insertRacePost(race, post);
   if (m_mode == Truline.DATABASE1) {
    insertRacePostProperties(race, post);
    for (Enumeration e1 = post.m_performances.elements(); e1.hasMoreElements();) {
     Performance p = (Performance) e1.nextElement();
     insertRacePostPastPerf(race, post, p);
     insertRacePostPastPerfProperties(race, post, p);
    }
    for (int i = 0; i < post.m_work.length; i++) {
     if (post.m_work[i].m_workDate != null) {
      insertRacePostWork(race, post, post.m_work[i]);
     }
    }
    for (Enumeration e1 = post.m_trainerJockeyStats.elements(); e1
      .hasMoreElements();) {
     TrainerJockeyStats tjs = (TrainerJockeyStats) e1.nextElement();
     insertRacePostTrainerJockeyStats(race, post, tjs);
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
      if (post.m_handicap.rank[i] == 1 && biasPoints[i] > 0) {
       biasBonus = biasBonus + biasPoints[i];
      }
      if (Truline.userProps.getProperty("TrackTheBias", "N").equals("2")
        && post.m_handicap.rank[i] == 2 && biasPoints[i] > 0) {
       biasBonus = biasBonus + biasPoints[i];
      }
     }
    }
    insertRacePostHandicap(race, post);
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
  prop.setProperty("TRAINER_NAME", post.m_trainerName);
  prop.setProperty("JOCKEY_NAME", post.m_jockeyName);
  prop.setProperty("RUNNING_STYLE", post.m_props.getProperty("RUNSTYLE", "").trim());
  String ownerName = post.m_props.getProperty("OWNER");
  if (ownerName.length() > 100)
   ownerName = ownerName.substring(0,99);
  prop.setProperty("OWNER_NAME", ownerName);
  prop.setProperty("SIRE_NAME", post.m_sireName);
  prop.setProperty("SIRE_AWD", post.m_sireAWD);
  prop.setProperty("DAM_NAME", post.m_damName);
  prop.setProperty("DAM_AWD", post.m_damAWD);
  prop.setProperty("DAM_SIRE_NAME", post.m_damSireName);
  prop.setProperty("DAM_SIRE_AWD", post.m_damSireAWD);
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
 private void insertRacePostWork(Race race, Post post, Workout w)
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
  prop.setProperty("DATE_WORK", Lib.datetoa(w.m_workDate));
  for (Enumeration e = w.m_props.propertyNames(); e.hasMoreElements();) {
   String name = (String) e.nextElement();
   String value = w.m_props.getProperty(name);
   if (value != null && value.length() > 0) {
    prop.setProperty("PROPERTY", name);
    prop.setProperty("PVALUE", w.m_props.getProperty(name));
    sql = makeInsert("RACE_POST_WORK", prop);
    psqlStmt = connect.prepareStatement(sql);
    psqlStmt.executeUpdate();
   }
  }
 }
 private void insertRacePostTrainerJockeyStats(Race race, Post post,
   TrainerJockeyStats tjs) throws Exception
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("Outputing RACE_POST_TRAINER_JOCKEY_STATS table for "
     + race.m_props.getProperty("RACENO") + "/"
     + Lib.ftoa((int) post.m_postPosition, 0) + "\n");
  Properties prop = new Properties();
  prop.setProperty("TRACK_ABBR", race.m_track);
  prop.setProperty("DATE_RACE", race.m_props.getProperty("RACEDATE"));
  prop.setProperty("RACE_NO", race.m_props.getProperty("RACENO"));
  prop.setProperty("SADDLE_CLOTH", post.cloth);
  prop.setProperty("POST_POS", Lib.ftoa((int) post.m_postPosition, 0));
  for (Enumeration e = tjs.m_props.propertyNames(); e.hasMoreElements();) {
   String name = (String) e.nextElement();
   String value = tjs.m_props.getProperty(name);
   if (value != null && value.length() > 0) {
    prop.setProperty("PROPERTY", name);
    prop.setProperty("PVALUE", tjs.m_props.getProperty(name));
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
  prop.setProperty("POINTS", Lib.ftoa((int) post.m_handicap.points, 0));
  prop.setProperty("BONUS", Lib.ftoa((int) post.m_handicap.bonus, 0));
  prop.setProperty("BONUS_RANK", Lib.ftoa((int) post.m_handicap.bonusRank, 0));
  prop.setProperty("MBEN_FLAG", post.m_handicap.extraFlg ? "#" : " ");
  prop.setProperty("TRAINER_PT", post.m_trainerNamePT);
  prop.setProperty("SIRE_TS", post.m_sireTS);
  prop.setProperty("SIRE_TS2", post.m_sireTS2);
  prop.setProperty("OWNER_TRAINER", post.m_ownerTrn);
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
  prop.setProperty("ODDS", post.m_odds);
  prop.setProperty("POINTS_ADV", Lib.ftoa((int) post.m_pointsAdv, 0));
  prop.setProperty("BF_FACTORS", Lib.ftoa((int) post.m_betfactors, 0));
  prop.setProperty("TF_FACTORS", Lib.ftoa((int) post.m_trnfactors, 0));
  prop.setProperty("FLOW_BETS", Lib.ftoa((int) post.cntHorseFlows, 0));
  sql = makeInsert("RACE_POST_HANDICAP", prop);
  psqlStmt = connect.prepareStatement(sql);
  psqlStmt.executeUpdate();
//  if (post.cntHorseFlows >= 0)
//   insertRacePostFlowBet(race, post);
 }
 private void insertRacePostFlowBet(Race race, Post post) throws Exception
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("Outputing RACE_POST_FLOW_BET table for "
     + race.m_props.getProperty("RACENO") + "/"
     + Lib.ftoa((int) post.m_postPosition, 0) + "\n");
  Properties prop = new Properties();
  prop.setProperty("TRACK_ABBR", race.m_track);
  prop.setProperty("DATE_RACE", race.m_props.getProperty("RACEDATE"));
  prop.setProperty("RACE_NO", race.m_props.getProperty("RACENO"));
  prop.setProperty("SADDLE_CLOTH", post.cloth);
  prop.setProperty("POST_POS", Lib.ftoa((int) post.m_postPosition, 0));
  prop.setProperty("FLOW_BETS", Lib.ftoa((int) post.cntHorseFlows, 0));
  prop.setProperty("FLOW_BET_1", post.horseFlows[0]);
  prop.setProperty("FLOW_BET_2", post.horseFlows[1]);
  prop.setProperty("FLOW_BET_3", post.horseFlows[2]);
  prop.setProperty("FLOW_BET_4", post.horseFlows[3]);
  prop.setProperty("FLOW_BET_5", post.horseFlows[4]);
  prop.setProperty("FLOW_BET_6", post.horseFlows[5]);
  prop.setProperty("FLOW_BET_7", post.horseFlows[6]);
  prop.setProperty("FLOW_BET_8", post.horseFlows[7]);
  prop.setProperty("FLOW_BET_9", post.horseFlows[8]);
  prop.setProperty("FLOW_BET_10", post.horseFlows[9]);
  sql = makeInsert("RACE_POST_FLOW_BET", prop);
  psqlStmt = connect.prepareStatement(sql);
  psqlStmt.executeUpdate();
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
