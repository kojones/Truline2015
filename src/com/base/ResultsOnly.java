package com.base;
/*
 *	Output race results to database for truline2012.
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
public class ResultsOnly
{
 private Connection        connect    = null;
 private Statement         sqlStmt    = null;
 private PreparedStatement psqlStmt   = null;
 private ResultSet         resultSet  = null;
 int                       m_mode;
 boolean                   first_time = true;
 boolean                   first_race = true;
 String                    sql;
 public ResultsOnly() {
 }
 /**
  * Output the race from DRF input.
  */
 public void generate(String databaseName, Bris bris, int mode)
 {
  m_mode = mode;
  Truline.println("Inserting race results into database " + databaseName);
  try {
   if (first_time) {
    first_time = false;
    // This will load the MySQL driver, each DB has its own driver
    Class.forName("com.mysql.jdbc.Driver");
    // Setup the connection with the DB
    connect = DriverManager.getConnection("jdbc:mysql://localhost/"
      + databaseName + "?" + "user=truline&password=4crossPP");
   }
   if (Log.isDebug(Log.TRACE))
    Log.print("Outputing race results to " + databaseName + "\n");
   for (Enumeration e = bris.m_races.elements(); e.hasMoreElements();) {
    Race race = (Race) e.nextElement();
    outputRace(race);
   }
   psqlStmt = connect.prepareStatement("commit;");
   psqlStmt.executeUpdate();
   first_race = true;
  } catch (Exception e) {
   Log.print("Exception outputing race results to database " + e + "\n"
     + "Class = " + e.getClass() + "  Message = " + e.getMessage() + "\n");
  }
 }
 /**
  * Output the race from MCP input.
  */
 public void generate(String databaseName, BrisMCP brisMCP, int mode)
 {
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
   }
   if (Log.isDebug(Log.TRACE))
    Log.print("Outputing race results to database " + databaseName + "\n");
   for (Enumeration e = brisMCP.m_races.elements(); e.hasMoreElements();) {
    Race race = (Race) e.nextElement();
    outputRace(race);
   }
   psqlStmt = connect.prepareStatement("commit;");
   psqlStmt.executeUpdate();
   first_race = true;
  } catch (Exception e) {
   Log.print("Exception outputing race results to database " + e + "\n"
     + "Class = " + e.getClass() + "  Message = " + e.getMessage() + "\n");
  }
 }
 /**
  * Output the race from JCP input.
  */
 public void generate(String databaseName, BrisJCP brisJCP, int mode)
 {
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
   }
   if (Log.isDebug(Log.TRACE))
    Log.print("Outputing race results to database " + databaseName + "\n");
   for (Enumeration e = brisJCP.m_races.elements(); e.hasMoreElements();) {
    Race race = (Race) e.nextElement();
    outputRace(race);
   }
   psqlStmt = connect.prepareStatement("commit;");
   psqlStmt.executeUpdate();
   first_race = true;
  } catch (Exception e) {
   Log.print("Exception outputing race results to database " + e + "\n"
     + "Class = " + e.getClass() + "  Message = " + e.getMessage() + "\n");
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
    * RACE_RESULTS
    */
   Properties prop = new Properties();
   prop.setProperty("TRACK_ABBR", race.m_props.getProperty("TRACKABBR"));
   prop.setProperty("DATE_RACE", race.m_props.getProperty("RACEDATE"));
   sql = makeDelete("RACE_RESULTS", prop);
   psqlStmt = connect.prepareStatement(sql);
   psqlStmt.executeUpdate();
  }
  // Insert results table
  insertRaceResults(race);
 }
 private void insertRaceResults(Race race) throws Exception
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("Outputing RACE_RESULTS table for "
     + race.m_props.getProperty("RACENO") + "\n");
  Properties prop = new Properties();
  prop.setProperty("TRACK_ABBR", race.m_props.getProperty("TRACKABBR"));
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
  sql = makeInsert("RACE_RESULTS", prop);
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
}
