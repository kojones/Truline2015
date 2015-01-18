package com.base;
/**
 *	BRIS Exotic Results data parser
 */
import com.base.Lib;
import com.base.Log;
import com.base.Post;
import com.base.Race;
import com.base.Result;

import java.util.*;
import java.util.zip.*;
import java.io.*;

import com.mains.Truline;


public class BrisXRD
{
 Vector m_races = new Vector();
 /**
  * Constructor
  */
 public BrisXRD() {
 }
 public boolean load(String file, String filename, Vector m_racesIn)
 {
  m_races = m_racesIn;
  if (file.indexOf('\\') == -1) {
   // no path given, use the base address if known.
   String base = Truline.userProps.getProperty("DATADIR");
   if (base != null) {
    file = base + file;
   }
  }
  if (!parseXrd(file, filename))
   return false;
  return true;
 }
 public String[] names1 = {
                        // EXOTIC RESULTS DATAFILE : Priced at only 25 cents per
                        // racing card, this
                        // comma-delimited results file offers basic results
                        // along with win/place/show and exotic payoffs.
                        // Described below is the record layout of this
                        // comma-delimited results file. The Exotic Results
                        // Datafile contains one record for each horse which
                        // raced at the selected track and date.
                        //
                        //
                        // 6/2005 Added 5 additional WagerTypes (#32-#36,
                        // #55-74),
                        // Expanded file definition to maximum 254 fields.
                        //
                        //
                        //
                        // 1. TRACK CHARACTER XXX 3
   "TRACKABBR",
   // 2. DATE CHARACTER XXXXXXXX 8 CYMD
   "RACEDATE",
   // 3. RACE NUMBER NUMERIC 99 2
   "RACENO",
   // 4. (reserved for future use)
   "",
   // 5. SURFACE CHARACTER X 1 D-dirt
   // d-inner dirt
   // T-turf
   // t-inner turf
   "SURFACE",
   // 6 - 15 (reserved for future use)
   "", "", "", "", "", "", "", "", "", "",
   // 16. FINAL TIME NUMERIC 999.99 6
   "FINALTIME",
   // 17. TRACK CONDITION CHARACTER XX 2
   "CONDITION",
   // 18. Horse's POST POSITION NUMERIC 99 2
   "POSTPOSITION",
   // 19. Horse's ENTRY/COUPLING FLAG CHARACTER X 1 e-entry/coupled
   "ENTRY",
   // 20. Horse's NAME CHARACTER 25
   "HORSENAME",
   // 21- 29 (reserved for future use)
   "", "", "", "", "", "", "", "", "",
   // 30. Horse's Finish Position NUMERIC 99 2 92=DidNotFinish
   "FINISHPOS",
   // 31- (reserved for future use)
   // 32. WagerType10_NAME CHARACTER 45
   // 33. WagerType11_NAME CHARACTER 45
   // 34. WagerType12_NAME CHARACTER 45
   // 35. WagerType13_NAME CHARACTER 45
   // 36. WagerType14_NAME CHARACTER 45
   "", "", "", "", "", "",
   // 37. Horse's Odds to $1.00 NUMERIC 999.99 6
   "ODDS",
   // 38- 47 (reserved for future use)
   // 48. # starters/field size NUMERIC 99 2
   // 49. Horse's Official Finish Pos NUMERIC 99 2
   "", "", "", "", "", "", "", "", "", "", "", "",
   // 50. Horse's Official PROGRAM # CHARACTER XXX 3
   "PROGRAMNUMBER",
   // 51- 54 (reserved for future use)
   // 55. WagerType10_WinningNums CHARACTER 45 (ex. "3-7")
   // 56. WagerType11_WinningNums CHARACTER 45 (ex. "3-7")
   // 57. WagerType12_WinningNums CHARACTER 45 (ex. "3-7")
   // 58. WagerType13_WinningNums CHARACTER 45 (ex. "3-7")
   // 59. WagerType14_WinningNums CHARACTER 45 (ex. "3-7")
   // 60. WagerType10_$2Payoff NUMERIC 999999999.99 12 ("45.80" payoff)
   "", "", "", "", "", "", "", "", "", "",
   // 61. WagerType11_$2Payoff NUMERIC 999999999.99 12 ("45.80" payoff)
   // 62. WagerType12_$2Payoff NUMERIC 999999999.99 12 ("45.80" payoff)
   // 63. WagerType13_$2Payoff NUMERIC 999999999.99 12 ("45.80" payoff)
   // 64. WagerType14_$2Payoff NUMERIC 999999999.99 12 ("45.80" payoff)
   // 65. WagerType10_Pool NUMERIC 999999999.99 12
   // 66. WagerType11_Pool NUMERIC 999999999.99 12
   // 67. WagerType12_Pool NUMERIC 999999999.99 12
   // 68. WagerType13_Pool NUMERIC 999999999.99 12
   // 69. WagerType14_Pool NUMERIC 999999999.99 12
   // 70. WagerType10_BetAmt NUMERIC 99.99 5
   "", "", "", "", "", "", "", "", "", "",
   // 71. WagerType11_BetAmt NUMERIC 99.99 5
   // 72. WagerType12_BetAmt NUMERIC 99.99 5
   // 73. WagerType13_BetAmt NUMERIC 99.99 5
   // 74. WagerType14_BetAmt NUMERIC 99.99 5
   "", "", "", "",
   // 75. Horse's $2 Win Payoff - if any 9999.99 7
   "WINPAYOFF",
   // 76. Horse's $2 Place Payoff - if any 9999.99 7
   "PLACEPAYOFF",
   // 77. Horse's $2 Show Payoff - if any 9999.99 7
   "SHOWPAYOFF",
   // 78. WagerType1_NAME CHARACTER 45 (ex. "EXACTA")
   "WAGER1",
   // 79. WagerType2_NAME CHARACTER 45
   "WAGER2",
   // 80. WagerType3_NAME CHARACTER 45
   "WAGER3",
   // 81. WagerType4_NAME CHARACTER 45
   "WAGER4",
   // 82. WagerType5_NAME CHARACTER 45
   "WAGER5",
   // 83. WagerType6_NAME CHARACTER 45
   "WAGER6",
   // 84. WagerType7_NAME CHARACTER 45
   "WAGER7",
   // 85. WagerType8_NAME CHARACTER 45
   "WAGER8",
   // 86. WagerType9_NAME CHARACTER 45
   "WAGER9",
   // 87. WagerType1_WinningNums CHARACTER 45 (ex. "3-7")
   // 88. WagerType2_WinningNums CHARACTER 45
   // 89. WagerType3_WinningNums CHARACTER 45
   // 90. WagerType4_WinningNums CHARACTER 45
   // 91. WagerType5_WinningNums CHARACTER 45
   // 92. WagerType6_WinningNums CHARACTER 45
   // 93. WagerType7_WinningNums CHARACTER 45
   // 94. WagerType8_WinningNums CHARACTER 45
   // 95. WagerType9_WinningNums CHARACTER 45
   "", "", "", "", "", "", "", "", "",
   // 96. WagerType1_$2Payoff NUMERIC 9999999.99 10 (ex. "45.80")
   "WAGERPAYOFF1",
   // 97. WagerType2_$2Payoff NUMERIC 9999999.99 10
   "WAGERPAYOFF2",
   // 98. Wa3erType3_$2Payoff NUMERIC 9999999.99 10
   "WAGERPAYOFF3",
   // 99. WagerType4_$2Payoff NUMERIC 9999999.99 10
   "WAGERPAYOFF4",
   // 100. WagerType5_$2Payoff NUMERIC 9999999.99 10
   "WAGERPAYOFF5",
   // 101. WagerType6_$2Payoff NUMERIC 9999999.99 10
   "WAGERPAYOFF6",
   // 102. WagerType7_$2Payoff NUMERIC 9999999.99 10
   "WAGERPAYOFF7",
   // 103. WagerType8_$2Payoff NUMERIC 9999999.99 10
   "WAGERPAYOFF8",
   // 104. WagerType9_$2Payoff NUMERIC 9999999.99 10
   "WAGERPAYOFF9",
   // 105. WagerType1_Pool NUMERIC 999999999.99 12
   // 106. WagerType2_Pool NUMERIC 999999999.99 12
   // 107. WagerType3_Pool NUMERIC 999999999.99 12
   // 108. WagerType4_Pool NUMERIC 999999999.99 12
   // 109. WagerType5_Pool NUMERIC 999999999.99 12
   // 110. WagerType6_Pool NUMERIC 999999999.99 12
   // 111. WagerType7_Pool NUMERIC 999999999.99 12
   // 112. WagerType8_Pool NUMERIC 999999999.99 12
   // 113. WagerType9_Pool NUMERIC 999999999.99 12
   "", "", "", "", "", "", "", "", "",
   // 114. WagerType1_BetAmt NUMERIC 99.99 5
   "WAGERBET1",
   // 115. WagerType2_BetAmt NUMERIC 99.99 5
   "WAGERBET2",
   // 116. WagerType3_BetAmt NUMERIC 99.99 5
   "WAGERBET3",
   // 117. WagerType4_BetAmt NUMERIC 99.99 5
   "WAGERBET4",
   // 118. WagerType5_BetAmt NUMERIC 99.99 5
   "WAGERBET5",
   // 119. WagerType6_BetAmt NUMERIC 99.99 5
   "WAGERBET6",
   // 120. WagerType7_BetAmt NUMERIC 99.99 5
   "WAGERBET7",
   // 121. WagerType8_BetAmt NUMERIC 99.99 5
   "WAGERBET8",
   // 122. WagerType9_BetAmt NUMERIC 99.99 5
   "WAGERBET9"
                        // 123-254 (reserved for future use)
                        //
                        };
 /**
  * Parse the exotic results file
  */
 public boolean parseXrd(String file, String filename)
 {
  boolean status = true;
  String buffer;
  BufferedReader in = null;
  StreamTokenizer parser = null;
  try {
   in = openFile(file, ".xrd");
  } catch (Exception e) {
   int i = file.indexOf('.');
   if (i > 0)
    file = file.substring(0, i);
   Truline.println("Could not open file - " + file + ".xrd\n   "
     + e.getMessage());
   Log
     .print("Could not open file - " + file + ".xrd\n   " + e.getMessage());
   return false;
  }
  Truline.println("Parsing " + filename);
  try {
   parser = new StreamTokenizer(in);
   parser.resetSyntax();
   parser.wordChars(33, 255);
   parser.whitespaceChars(0, 32);
   parser.eolIsSignificant(true);
   parser.commentChar('#');
   parser.quoteChar('"');
   parser.ordinaryChar(',');
  } catch (Exception e) {
   Truline.println("Could not read file - " + filename + "\n   "
     + e.getMessage());
   Log.print("Could not read file - " + filename + "\n   " + e.getMessage());
   return false;
  }
  String value;
  int fld = 0;
  int c = StreamTokenizer.TT_EOL;
  Result result = new Result();
  boolean running = true;
  while (running) {
   try {
    c = parser.nextToken();
    switch (c) {
     case '"':
      parser.sval = parser.sval.trim();
      // fall through
     case StreamTokenizer.TT_WORD:
      if (fld < names1.length) {
       String name = names1[fld].trim();
       if (name.length() > 0) {
        if (Log.isDebug(Log.PARSE1))
         Log.print(name + "=" + parser.sval + "\n");
        result.m_props.put(name, parser.sval);
       }
      }
      break;
     case ',':
      fld++;
      break;
     case StreamTokenizer.TT_EOF:
      running = false;
      // fall through
     case StreamTokenizer.TT_EOL:
      if (result.m_props.size() > 0) {
       result.m_track = result.m_props.getProperty("TRACKABBR","   ");
       if (result.m_track.equals("HOL"))
        result.m_track = "BHP";
       if (!result.m_track.equals("AJX") && !result.m_track.equals("FPX") && !result.m_track.equals("PRX"))
        if (result.m_track.substring(2).equals("X") || result.m_track.equals("FPK"))
         result.m_track = result.m_track.substring(0, 2);
       value = result.m_props.getProperty("RACEDATE");
       result.m_raceDate = Lib.atoDate(value);
       value = result.m_props.getProperty("RACENO");
       result.m_raceNo = Lib.atoi(value);
       result.m_surface = result.m_props.getProperty("SURFACE");
       value = result.m_props.getProperty("POSTPOSITION");
       result.m_postPosition = Lib.atoi(value);
       result.m_programNumber = result.m_props.getProperty("PROGRAMNUMBER");
       result.m_horseName = result.m_props.getProperty("HORSENAME");
       result.m_trackCond = result.m_props.getProperty("CONDITION");
       Race race = findRace(result.m_raceNo);
       race.m_trackCondResult = result.m_trackCond;
       race.m_surfaceResult = result.m_surface;
       Post post = findPost(race, result.m_horseName, result.m_programNumber);
       if (post != null) {
        if (result.m_track.trim().equals(post.m_track.trim())
          && result.m_raceDate.equals(post.m_raceDate)) {
         String finishPos = result.m_props.getProperty("FINISHPOS");
         if (finishPos != null)
          post.m_finishPos = finishPos;
         // Save odds and payoffs for horse
         double payoffWork = Lib.atof(result.m_props.getProperty("ODDS"));
         post.m_odds = Lib.ftoa((double) payoffWork, 2);
         if (post.m_finishPos.equals("1")) {
          race.m_cloth1 = result.m_programNumber;
          payoffWork = Lib.atof(result.m_props.getProperty("WINPAYOFF"));
          race.m_win1 = Lib.ftoa((double) payoffWork, 2);
          payoffWork = Lib.atof(result.m_props.getProperty("PLACEPAYOFF"));
          race.m_place1 = Lib.ftoa((double) payoffWork, 2);
          payoffWork = Lib.atof(result.m_props.getProperty("SHOWPAYOFF"));
          race.m_show1 = Lib.ftoa((double) payoffWork, 2);
          post.m_winPayoff = race.m_win1;
          post.m_placePayoff = race.m_place1;
          post.m_showPayoff = race.m_show1;
         } else if (post.m_finishPos.equals("2")) {
          race.m_cloth2 = result.m_programNumber;
          value = result.m_props.getProperty("WINPAYOFF");
          if (!(result.m_props.getProperty("WINPAYOFF").equals(".00") || result.m_props.getProperty("WINPAYOFF").equals("0"))) {
           payoffWork = Lib.atof(result.m_props.getProperty("WINPAYOFF"));
           race.m_win2 = Lib.ftoa((double) payoffWork, 2);
          }
          payoffWork = Lib.atof(result.m_props.getProperty("PLACEPAYOFF"));
          race.m_place2 = Lib.ftoa((double) payoffWork, 2);
          payoffWork = Lib.atof(result.m_props.getProperty("SHOWPAYOFF"));
          race.m_show2 = Lib.ftoa((double) payoffWork, 2);
          post.m_winPayoff = race.m_win2;
          post.m_placePayoff = race.m_place2;
          post.m_showPayoff = race.m_show2;
         } else if (post.m_finishPos.equals("3")) {
          race.m_cloth3 = result.m_programNumber;
          if (!(result.m_props.getProperty("WINPAYOFF").equals(".00") || result.m_props.getProperty("WINPAYOFF").equals("0"))) {
           payoffWork = Lib.atof(result.m_props.getProperty("WINPAYOFF"));
           race.m_win3 = Lib.ftoa((double) payoffWork, 2);
          }
          if (!(result.m_props.getProperty("PLACEPAYOFF").equals(".00") || result.m_props.getProperty("PLACEPAYOFF").equals("0"))) {
           payoffWork = Lib.atof(result.m_props.getProperty("PLACEPAYOFF"));
           race.m_place3 = Lib.ftoa((double) payoffWork, 2);
          }
          payoffWork = Lib.atof(result.m_props.getProperty("SHOWPAYOFF"));
          race.m_show3 = Lib.ftoa((double) payoffWork, 2);
          post.m_winPayoff = race.m_win3;
          post.m_placePayoff = race.m_place3;
          post.m_showPayoff = race.m_show3;
         } else if (post.m_finishPos.equals("4")) {
          race.m_cloth4 = result.m_programNumber;
         }
         if (result.m_props.getProperty("WAGER1") != null)
          postPayoff(race, result, "WAGER1", "WAGERPAYOFF1", "WAGERBET1");
         if (result.m_props.getProperty("WAGER2") != null)
          postPayoff(race, result, "WAGER2", "WAGERPAYOFF2", "WAGERBET2");
         if (result.m_props.getProperty("WAGER3") != null)
          postPayoff(race, result, "WAGER3", "WAGERPAYOFF3", "WAGERBET3");
         if (result.m_props.getProperty("WAGER4") != null)
          postPayoff(race, result, "WAGER4", "WAGERPAYOFF4", "WAGERBET4");
         if (result.m_props.getProperty("WAGER5") != null)
          postPayoff(race, result, "WAGER5", "WAGERPAYOFF5", "WAGERBET5");
         if (result.m_props.getProperty("WAGER6") != null)
          postPayoff(race, result, "WAGER6", "WAGERPAYOFF6", "WAGERBET6");
         if (result.m_props.getProperty("WAGER7") != null)
          postPayoff(race, result, "WAGER7", "WAGERPAYOFF7", "WAGERBET7");
         if (result.m_props.getProperty("WAGER8") != null)
          postPayoff(race, result, "WAGER8", "WAGERPAYOFF8", "WAGERBET8");
         if (result.m_props.getProperty("WAGER9") != null)
          postPayoff(race, result, "WAGER9", "WAGERPAYOFF9", "WAGERBET9");
        } else {
         System.out.println("Error " + file + ":  Race=" + result.m_track + "/"
           + post.m_track + " date=" + result.m_raceDate + "/"
           + post.m_raceDate + "; Track/Date Not found");
         running = false;
         status = false;
         break;
        }
       } else {
        System.out.println("Error " + file + ": Race=" + result.m_raceNo
          + " Pgmnum=" + result.m_programNumber + "; Program number Not found");
        running = false;
        status = false;
       }
      }
      // Set up for the next result
      if (running) {
       result = new Result();
       fld = 0;
      }
      break;
    }
   } catch (Exception e) {
    // an error condition...
    System.out.println("Error in " + filename + ": line=" + parser.lineno()
      + " fld=" + fld + "\n   " + e);
    e.printStackTrace();
    running = false;
    status = false;
   }
  }
  try {
   in.close();
  } catch (Exception e) {
  }
  return status;
 }
 /**
  * Locate the race record.
  */
 public Race findRace(int raceNo)
 {
  for (Enumeration e = m_races.elements(); e.hasMoreElements();) {
   Race race = (Race) e.nextElement();
   if (race.m_raceNo == raceNo)
    return race;
  }
  return null;
 }
 /**
  * Locate the post record.
  */
 public Post findPost(Race race, String horseName, String programNumber)
 {
  race.m_resultsPosted = "Y";
  if (race != null) {
   for (Enumeration e = race.m_posts.elements(); e.hasMoreElements();) {
    Post post = (Post) e.nextElement();
    if (post.cloth.equals(programNumber))
     return post;
    if (post.cloth.equals("") && post.m_horseName.equals(horseName)) {
     post.cloth = programNumber;
     return post;
    }
   }
  }
  return null;
 }
 /**
  * Post wager type payoff
  */
 public boolean postPayoff(Race race, Result result, String wagerType,
   String wagerPayoff, String wagerBet)
 {
  double payoffWork = 0;
  double payoffBet = 2;
  if (result.m_props.getProperty(wagerType).equals("EXACTA")) {
   payoffWork = Lib.atof(result.m_props.getProperty(wagerPayoff));
   // payoffBet = Lib.atof(result.m_props.getProperty(wagerBet));
   race.m_exactaPayoff = Lib.ftoa((double) (payoffWork / payoffBet), 2);
  }
  if (result.m_props.getProperty(wagerType).equals("TRIFECTA")) {
   payoffWork = Lib.atof(result.m_props.getProperty(wagerPayoff));
   // payoffBet = Lib.atof(result.m_props.getProperty(wagerBet));
   race.m_trifectaPayoff = Lib.ftoa((double) (payoffWork / payoffBet), 2);
  }
  if (result.m_props.getProperty(wagerType).equals("SUPERFECTA")) {
   payoffWork = Lib.atof(result.m_props.getProperty(wagerPayoff));
   // payoffBet = Lib.atof(result.m_props.getProperty(wagerBet));
   race.m_superPayoff = Lib.ftoa((double) (payoffWork / payoffBet), 2);
  }
  if (result.m_props.getProperty(wagerType).equals("PICK THREE")) {
   payoffWork = Lib.atof(result.m_props.getProperty(wagerPayoff));
   // payoffBet = Lib.atof(result.m_props.getProperty(wagerBet));
   race.m_pick3Payoff = Lib.ftoa((double) (payoffWork / payoffBet), 2);
  }
  if (result.m_props.getProperty(wagerType).equals("PICK FOUR")) {
   payoffWork = Lib.atof(result.m_props.getProperty(wagerPayoff));
   // payoffBet = Lib.atof(result.m_props.getProperty(wagerBet));
   race.m_pick4Payoff = Lib.ftoa((double) (payoffWork / payoffBet), 2);
  }
  if (result.m_props.getProperty(wagerType).equals("PICK FIVE")) {
   payoffWork = Lib.atof(result.m_props.getProperty(wagerPayoff));
   // payoffBet = Lib.atof(result.m_props.getProperty(wagerBet));
   race.m_pick5Payoff = Lib.ftoa((double) (payoffWork / payoffBet), 2);
  }
  if (result.m_props.getProperty(wagerType).equals("PICK SIX")) {
   payoffWork = Lib.atof(result.m_props.getProperty(wagerPayoff));
   // payoffBet = Lib.atof(result.m_props.getProperty(wagerBet));
   race.m_pick6Payoff = Lib.ftoa((double) (payoffWork / payoffBet), 2);
  }
  if (result.m_props.getProperty(wagerType).equals("DAILY DOUBLE") || result.m_props.getProperty(wagerType).equals("DOUBLE")) {
   payoffWork = Lib.atof(result.m_props.getProperty(wagerPayoff));
   // payoffBet = Lib.atof(result.m_props.getProperty(wagerBet));
   race.m_doublePayoff = Lib.ftoa((double) (payoffWork / payoffBet), 2);
  }
  return true;
 }
 /**
  * Open file as an individual file or as a zip file.
  */
 public BufferedReader openFile(String file, String ext) throws Exception
 {
  BufferedReader in;
  int i = file.lastIndexOf('.');
  if (i > 0) {
   if (file.substring(i).equalsIgnoreCase(".zip")) {
    ZipFile z = new ZipFile(file);
    for (Enumeration e = z.entries(); e.hasMoreElements();) {
     ZipEntry ent = (ZipEntry) e.nextElement();
     String name = ent.getName();
     i = name.lastIndexOf('.');
     if (i > 0 && name.substring(i).equalsIgnoreCase(ext)) {
      in = new BufferedReader(new InputStreamReader(z.getInputStream(ent)));
      return in;
     }
    }
    throw new Exception("Extension \"" + ext + "\" not found in zip file");
   }
   file = file.substring(0, i);
  }
  in = new BufferedReader(new FileReader(file + ext));
  return in;
 }
 /**
  * Get the largest race number. Call this AFTER bris has been loaded.
  */
 public int getMaxRaceNbr()
 {
  int raceNbr = 0;
  for (Enumeration e = m_races.elements(); e.hasMoreElements();) {
   Race race = (Race) e.nextElement();
   if (race.m_raceNo > raceNbr)
    raceNbr = race.m_raceNo;
  }
  return raceNbr;
 }
}
