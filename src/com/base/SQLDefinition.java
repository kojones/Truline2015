package com.base;
/*
 *	Generates a file containing SQL data table creation.
 *
 */
import java.io.FileWriter;
import java.io.PrintWriter;

import com.mains.Truline;
public class SQLDefinition
{
 public SQLDefinition() {
 }
 /**
  * Generate the report.
  */
 public boolean generate(String filename)
 {
  Bris bris = new Bris();
  try {
   Truline.println("Generating SQL Definition script to " + filename);
   if (Log.isDebug(Log.TRACE))
    Log.print("Writing SQL Definition report to " + filename + "\n");
   PrintWriter out = new PrintWriter(new FileWriter(filename));
   WriteDrf(out, bris);
   WriteDr2(out, bris);
   WriteDr3(out, bris);
   WriteDr4(out, bris);
   WriteHandicap(out);
   out.close();
  } catch (Exception e) {
   Log.print("Exception opening output file " + e + "\n");
   return false;
  }
  return true;
 }
 /**
  * Inspect the fields in the first file.
  */
 public boolean WriteDrf(PrintWriter out, Bris bris)
 {
  out.println("create table race (");
  for (int i = 0; i < bris.names1.length; i++) {
   if (bris.names1[i].equals("COMMENTS")) {
    out.println("COMMENTS   VARCHAR2(500)");
   } else if (bris.names1[i].length() > 1) {
    out.println(bris.names1[i] + "  VARCHAR2(50),");
    if (bris.names1[i].equals("DISTANCE"))
     out.println("ABOUT  VARCHAR2(2),");
   }
  }
  out.println(");");
  return true;
 }
 /**
  * Inspect the fields in the second file.
  */
 public boolean WriteDr2(PrintWriter out, Bris bris)
 {
  out.println("create table post (");
  for (int i = 0; i < bris.names2.length; i++) {
   if (i > 22 && i < 36) {
    // Horse related fields
    continue;
   }
   if (bris.names2[i].equals("COMMENTS")) {
    out.println("COMMENTS   VARCHAR2(500)");
   } else if (bris.names2[i].length() > 1 && !bris.names2[i].startsWith("WORK")) {
    out.println(bris.names2[i] + "  VARCHAR2(50),");
   }
  }
  out.println(");");
  out.println("create table workout (");
  out.println("HORSENAME  VARCHAR2(50");
  for (int i = 0; i < bris.names2.length; i++) {
   if (i > 22 && i < 36) {
    // Horse related fields
    continue;
   }
   if (bris.names2[i].equals("WORKRANK")) {
    out.println("WORKRANK   VARCHAR2(50)");
   } else if (bris.names2[i].startsWith("WORK")) {
    out.println(bris.names2[i] + "  VARCHAR2(50),");
   }
  }
  out.println(");");
  out.println("create table horse (");
  for (int i = 0; i < bris.names2.length; i++) {
   if ((i > 22 && i < 35) && !bris.names2[i].equals("")) {
    out.println(bris.names2[i] + "  VARCHAR2(100),");
   }
   if (i == 35) {
    out.println(bris.names2[i] + "  VARCHAR2(100)");
   }
  }
  out.println(");");
  return true;
 }
 /**
  * Inspect the fields in the third file.
  */
 public boolean WriteDr3(PrintWriter out, Bris bris)
 {
  out.println("create table performance (");
  for (int i = 0; i < bris.names3.length; i++) {
   if (bris.names3[i].length() > 1) {
    out.println(bris.names3[i] + "  VARCHAR2(50),");
    if (bris.names3[i].equals("DISTANCE"))
     out.println("ABOUT  VARCHAR2(2),");
   }
  }
  out.println("PURSE  VARCHAR2(20)");
  out.println(");");
  return true;
 }
 /**
  * Inspect the fields in the fourth file.
  */
 public boolean WriteDr4(PrintWriter out, Bris bris)
 {
  out.println("create table trainer_stats (");
  for (int i = 0; i < bris.names4.length; i++) {
   if (bris.names4[i].length() > 1) {
    out.println(bris.names4[i] + "  VARCHAR2(50),");
    if (bris.names4[i].equals("DISTANCE"))
     out.println("ABOUT  VARCHAR2(2),");
   }
  }
  out.println("PURSE  VARCHAR2(20)");
  out.println(");");
  return true;
 }
 /**
  * Include the fields computed as part of the handicap.
  */
 public boolean WriteHandicap(PrintWriter out)
 {
  out.println("create table handicap (");
  out.println("TRACKABBR  	VARCHAR2(20),");
  out.println("RACEDATE   	VARCHAR2(20),");
  out.println("RACENO  		VARCHAR2(20),");
  out.println("POSTPOSTION  	VARCHAR2(20),");
  out.println("HORSENAME  	VARCHAR2(100),");
  out.println("CLOTH  		VARCHAR2(20),");
  out.println("SCRATCHED  	VARCHAR2(2),");
  out.println("REPRACE  		VARCHAR2(30),");
  out.println("RECENCY  		VARCHAR2(30),");
  for (int i = 0; i < Handicap.names.length; i++) {
   out.println(Handicap.names[i] + "     		VARCHAR2(20),");
   out.println(Handicap.names[i] + "RANK" + "     VARCHAR2(20),");
  }
  out.println("MUSTBET  		VARCHAR2(2),");
  out.println("POINTS  		VARCHAR2(20),");
  out.println("BONUS  		VARCHAR2(20),");
  out.println("RANK  			VARCHAR2(20),");
  out.println("TRAINER  		VARCHAR2(100),");
  out.println("TRAINERSTARTS  VARCHAR2(20),");
  out.println("TPCNT  		VARCHAR2(20),");
  out.println("JOCKEY  		VARCHAR2(100),");
  out.println("JOCKEYSTARTS   VARCHAR2(20),");
  out.println("JPCNT  		VARCHAR2(20)");
  out.println(");");
  return true;
 }
}
