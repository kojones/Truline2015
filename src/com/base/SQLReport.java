package com.base;
/*
 *	Text Report for displaying handicap for truline2000.
 *
 */
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Properties;

import com.mains.Truline;

public class SQLReport
{
 int m_mode;
 public SQLReport() {
 }
 /**
  * Generate the report.
  */
 public void generate(String filename, Bris bris, int mode)
 {
  m_mode = mode;
  Truline.println("Generating SQL import script to " + filename + ".sql");
  try {
   if (Log.isDebug(Log.TRACE))
    Log.print("Writing text report to " + filename + ".sql\n");
   PrintWriter out = new PrintWriter(new FileWriter(filename + ".sql"));
   for (Enumeration<?> e = bris.m_races.elements(); e.hasMoreElements();) {
    Race race = (Race) e.nextElement();
    writeReport(out, race);
   }
   out.close();
  } catch (Exception e) {
   Log.print("Exception opening output file " + e + "\n");
  }
 }
 public void writeReport(PrintWriter out, Race race)
 {
  // Populate the Race entries.
  out.println();
  out.println("-------- Race=" + race.m_raceNo);
  String[] raceKeys = { "TRACKABBR", "RACEDATE", "RACENO" };
  makeSQL(out, "race", race.m_props, raceKeys);
  // Display each post in the race.
  for (Enumeration e = race.m_posts.elements(); e.hasMoreElements();) {
   Post post = (Post) e.nextElement();
   out.println("-------- Race=" + race.m_raceNo + "  Post="
     + post.m_postPosition);
   String[] postKeys = { "TRACKABBR", "RACEDATE", "RACENO", "POSTPOSTION" };
   makeSQL(out, "post", post.m_props, postKeys);
   String[] horseKeys = { "HORSENAME" };
   makeSQL(out, "horse", post.m_horse.m_props, horseKeys);
   for (Enumeration e1 = post.m_performances.elements(); e1.hasMoreElements();) {
    Performance p = (Performance) e1.nextElement();
    String[] performanceKeys = { "PPTRACKABBR", "PPRACEDATE", "PPRACENO",
      "PPPOSTPOSITION" };
    makeSQL(out, "performance", p.m_props, performanceKeys);
   }
   for (int i = 0; i < post.m_work.length; i++) {
    if (post.m_work[i].m_workDate != null) {
     post.m_work[i].m_props.setProperty("HORSENAME", post.m_horseName);
     String[] workoutKeys = { "HORSENAME", "WORKDATE" };
     makeSQL(out, "workout", post.m_work[i].m_props, workoutKeys);
    }
   }
   if (post.m_handicap == null || post.m_horseName == null)
    continue; // position is empty
   Properties handicapProps = new Properties();
   handicapProps
     .setProperty("TRACKABBR", post.m_props.getProperty("TRACKABBR"));
   handicapProps.setProperty("RACEDATE", post.m_props.getProperty("RACEDATE"));
   handicapProps.setProperty("RACENO", post.m_props.getProperty("RACENO"));
   handicapProps.setProperty("POSTPOSTION",
     post.m_props.getProperty("POSTPOSTION"));
   handicapProps.setProperty("HORSENAME", post.m_horseName);
   handicapProps.setProperty("CLOTH", post.cloth);
   String entry = post.m_props.getProperty("ENTRY", "");
   if (entry.equals("S")) {
    handicapProps.setProperty("SCRATCHED", "Y");
   } else {
    if (post.m_handicap.m_repRace != null)
     handicapProps.setProperty("REPRACE",
       Lib.datetoa(post.m_handicap.m_repRace.ppRaceDate));
    if (post.m_handicap.m_recency != null)
     handicapProps.setProperty("RECENCY",
       Lib.datetoa(post.m_handicap.m_recency.ppRaceDate));
    // Include each column
    for (int i = 0; i < post.m_handicap.names.length; i++) {
     handicapProps.setProperty(post.m_handicap.names[i],
       Lib.ftoa(post.m_handicap.value[i], 2));
     handicapProps.setProperty(post.m_handicap.names[i] + "RANK", ""
       + post.m_handicap.rank[i]);
    }
    if (post.m_handicap.extraFlg)
     handicapProps.setProperty("MUSTBET", "Y");
    handicapProps.setProperty("POINTS", "" + post.m_handicap.points);
    handicapProps.setProperty("BONUS", "" + post.m_handicap.bonus);
    handicapProps.setProperty("RANK", "" + post.m_handicap.bonusRank);
    int tstart, tplace, twin, jstart, jplace, jwin;
    tstart = Lib.atoi(post.m_props.getProperty("TRAINERSTARTS"));
    tplace = Lib.atoi(post.m_props.getProperty("TRAINERPLACES"));
    twin = Lib.atoi(post.m_props.getProperty("TRAINERWINS"));
    jstart = Lib.atoi(post.m_props.getProperty("JOCKEYSTARTS"));
    jplace = Lib.atoi(post.m_props.getProperty("JOCKEYPLACES"));
    jwin = Lib.atoi(post.m_props.getProperty("JOCKEYWINS"));
    int jpcnt = (jstart > 0) ? (jwin + jplace) * 100 / jstart : 0;
    int tpcnt = (tstart > 0) ? (twin + tplace) * 100 / tstart : 0;
    handicapProps.setProperty("TRAINER",
      post.m_props.getProperty("TRAINER", ""));
    handicapProps.setProperty("TRAINERSTARTS", Lib.ftoa((double) tstart, 0));
    handicapProps.setProperty("TPCNT", Lib.ftoa((double) tpcnt, 2));
    handicapProps.setProperty("JOCKEY", post.m_props.getProperty("JOCKEY", ""));
    handicapProps.setProperty("JOCKEYSTARTS", Lib.ftoa((double) jstart, 0));
    handicapProps.setProperty("JPCNT", Lib.ftoa((double) jpcnt, 2));
   }
   makeSQL(out, "handicap", handicapProps, postKeys);
  }
 }
 private void makeSQL(PrintWriter out, String tableName, Properties prop,
   String[] keys)
 {
  String sql;
  if (m_mode == Truline.SQLMODE)
   sql = makeInsert(tableName, prop);
  else
   sql = makeUpdate(tableName, prop, keys);
  out.println(sql);
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
  * Build an update SQL command
  * 
  * @param tableName
  *         - The name of the table.
  * @param prop
  *         - The property list
  * @param keys
  *         - an array of key names for the where clause.
  */
 private String makeUpdate(String tableName, Properties prop, String[] keys)
 {
  String sql = "UPDATE " + tableName + " SET ";
  for (Enumeration e = prop.propertyNames(); e.hasMoreElements();) {
   String name = (String) e.nextElement();
   boolean isKey = false;
   for (int i = 0; i < keys.length; i++) {
    if (name.equals(keys[i])) {
     isKey = true;
     break;
    }
   }
   if (isKey)
    continue;
   String value = prop.getProperty(name);
   if (value != null && value.length() > 0) {
    // Replace all (')s with ('')
    int j = 0;
    int k;
    while ((k = value.indexOf('\'', j)) > -1) {
     value = value.substring(0, k + 1) + value.substring(k);
     j = k + 2;
    }
    sql += name + " = '" + value + "', ";
   } else
    sql += name + " = NULL, ";
  }
  int idx = sql.lastIndexOf(",");
  sql = sql.substring(0, idx); // remove the last ", "
  sql += " WHERE ";
  for (int i = 0; i < keys.length; i++) {
   String value = prop.getProperty(keys[i]);
   if (value != null && value.length() > 0) {
    // Replace all (')s with ('')
    int j = 0;
    int k;
    while ((k = value.indexOf('\'', j)) > -1) {
     value = value.substring(0, k + 1) + value.substring(k);
     j = k + 2;
    }
    sql += keys[i] + " = '" + value + "' AND ";
   } else
    sql += keys[i] + " = NULL AND ";
  }
  idx = sql.lastIndexOf("AND");
  sql = sql.substring(0, idx); // remove the last "AND"
  sql += ";";
  return sql;
 }
}
