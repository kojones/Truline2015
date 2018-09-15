package com.base;
/*
 Problem:  11/9

 gpx1106 race 2, horse 2

 >Yes, in doing the Energy Numbers, it seems to be giving the 60% bonus to
 >races, and 40% to workouts, as appropriate. In this same second race,
 >all Energy Numbers were right except for YAMIPA. The three efforts that
 >should have been used are: 10/31 workout, 9/26 race, and 9/22 workout.
 >Instead, for a reason I can't explain, it ignored the 10/31 workout, and
 >used (1) the 9/26 race, (2) the 9/22 workout, and (3) a 9/13 workout.
 >This lowered the Energy Number substantially.
 =============================================================================
 */
/*
 *      The handicaping logic for truline.
 *
 *      There should be one Handicap Object created for each horse in a race (each post).
 *
 *SOME BACKGROUND INFORMATION:
 *
 *      NOTE: A furlong is 220 yards.  A 1/5th of a second
 *                is one horse length.  A variant point is considered
 *                to be worth 1/5th of a second.
 Anecdotally, as mentioned, a horse stretched out is 11-12', and that's
 where a fifth of a second cames from. In the 70's, some Ivy League
 mathematicians began arguing that the correct measurement was from the
 point the rear feet pushed off to the point the front feet touched
 ground. This got a little hairy when one compared a small filly (22' in
 this case) vs the great SECRETARIAT (33'). Another group argued that it
 should be the horse while standing, measured from point of tail to nose
 This is usually about 7-8', which would mean 7-8 lengtha per second. To
 tell you the truth, I don't know which is right. Thoroughbred racing
 started in the days of George Washington. They said one-fifth was a
 length. This was the case for 200 years of racing. So that's seemed good
 enough to me. (Plus it is the concept that the Daily Racing Form is
 based on.) This is one place where you can't fight City Hall.
 *
 *      REPRESENTITIVE RACE:
 In order to evaluate a horses chances of winning a race we must find
 some information about the horse on which to base our evaluation.
 For this we look at a horse's past performance.  Among the horses
 previous races we look for one that represents the horses best effort.
 This is the Representitive race.  We then add adjustments to the
 race's running line and speed ratings to normalize it for variations
 in track and other conditions.
 *
 *      TIME, VELOCITY, AND SPEED:
 Basically, we will be talking time and speed rating. The "running line"
 - which is in fractions for the race segments, then final time, is in
 seconds and fractions thereof (fifths of a second as to segments. 6F and
 longer, final time is in minutes, seconds, and fifths.

 But to get "velocity per second", as used in TRULINE, we convert to
 tenths. So,
 22.3       45.4             111:4           becomes
 22.6       45.8             111.8

 The speed figure is a numerical value, rarely below 50 - usually in a
 range of 65-105, that is a relationship to average winning times (par
 times) for the distance. A value of 100 would be the average or par. If
 exceeding 100, it would be a rare and special race, and they'd have to
 re-do their figures. The speed rating is accompanied (column to its
 right) by a value representing the Daily Track Variant, hopefully
 representing the degree that weather or maintenance has changed the
 surface off of "true fast". "0" would be perfectly true.

 We will have a Table for comparitive weight. Today's weight will be
 matched against the weight per rep race. If not identical there will be
 a +/- adjustment, dependent on whether weight is increasing today or
 decreasing. The adjustment number will be added to the Variant. Today's
 race will also be compared based on the Track Class Table. If it is not
 the same, there will be a +/- adjustment, dependent on whether the horse
 is moving up or down in Track Class. This will be added to the Variant.
 Today's race in terms of Purse will be compared to the Purse Class
 Table. Again, a +/- figure derives and is applied to the Variant.

 Once all these adjustments are made, and we have a total figure,
 whatever the figure it represents fifths of a second. Converting to
 tenths, 20% of the total figure is added (or subtracted) from the time
 at the first call (segment); 50% of the total modifies the second
 call/segment, and the total figure is applied to the final time.

 Now, where one can get easily confused, there is a polarity reversal
 involved with this total. The running line says the horse ran the race
 in 1:11:3. The total of the Variant and these other adjustments is +20.
 This means that at least in theory, the track surface and weight and
 Class slowed this horse up by four full seconds. On a "true fast" track,
 the horse should run four seconds faster. So we SUBTRACT the figure, and
 give the horse a "best race" potential time of 1:07:3.

 DRF Running Line               22.4    45.3      111.3
 Modified running line          21.6    43.6      107.6

 BUT, when dealing with AS and RE, we are using the SPEED RATING. For
 this, we must ADD the total value. So, if the DRF speed rating was 77,
 we would add +20 and change this to 97. In turn, a 97 speed rating would
 be consistent to a final time of 1:07:3.

 DISTANCE:
 Distance - 220 yards, yes. For our purposes, sprints are 5F and 5.5F
 (mostly for young horses, two-year-olds, and early in the year for
 three-year-olds. A few for broken down older horses. No major Stakes  at
 these distances. The largest number of races at sprint distance is 6F.
 Some tracks have 6.5F races as well, others 7F.

 Technically, there are a few races at a few tracks at 7.5F. TRULINE
 should ignore these. Short routes are divided between 8F and 8.5F.

 Most of the big Stakes races January through April are at 9F. 9F and up
 are regarded as "classic" distances. The first week May the Kentucky
 Derby is run 1t 10F. Then the Preakness in Maryland drops back slightly
 to 9.5F. The Belmont, third leg of the Triple Crown in June is at 12F.
 That's the longest major Stakes.

 In sprints, there are three running line "calls", the quarter, the half,
 and final time. This gives us the 23.3, 45.4, and 111.1, the final time.
 This is FS, SS, and FT. Subtracting FS from SS gets us TT; and
 substracting SS from FT gets us CS. EXCEPT, that on the Tote Board, and
 sometimes also in the DRF, there is a stretch call. That's when you have
 four calls instead of three. It creates a measurement of the final
 eighth or sixteenth of a mile. If you are watching races on TV, this is
 what the timer up in the corner shows, HOWEVER, this is NOT GIVEN by
 many tracks, and very erratically by the Daily Racing Form, so TRULINE
 doesn't use this break-down, The software, like us, would never know
 where to find it.

 In short routes, 8F to 9F, there are also three. The first is at the
 half mile pole, tthe second is at the six furlong poll, and then the
 final time. So we have 45.4 as the first call, 111.4 as the second call,
 and then the final time.  You are right about beaten lengths.

 */
import com.base.Handicap;
import com.base.Lib;
import com.base.Log;
import com.base.Performance;
import com.base.Post;
import com.base.Race;
import com.base.Workout;

import java.util.*;
import java.text.*;

import com.mains.Truline;
public class Handicap
{
 // Distances in yards
 public static final int F5_0      = 1100;                    // 5.0 Furlongs
 public static final int F5_5      = 1210;                    // 5.5 Furlongs
 public static final int F6_0      = 1320;                    // 6.0 Furlongs
 public static final int F6_5      = 1430;                    // 6.5 Furlongs
 public static final int F7_0      = 1540;                    // 7.0 Furlongs
 public static final int F7_5      = 1650;                    // 7.5 Furlongs
 public static final int F8_0      = 1760;                    // 8.0 Furlongs
 public static final int F8_1      = 1800;                    // 8.1 Furlongs
 public static final int F8_3      = 1830;                    // 8.3 Furlongs
 public static final int F8_5      = 1870;                    // 8.5 Furlongs
 public static final int F9_0      = 1980;                    // 9.0 Furlongs
 public static final int F9_5      = 2090;                    // 9.5 Furlongs
 public static final int F10       = 2200;                    // 10 Furlongs
 public static final int F11       = 2420;                    // 11 Furlongs
 public static final int F12       = 2840;                    // 12 Furlongs
 public static final int YdPerF    = 220;
 // Adjusted values:
 public static final int FS        = 0;                       // First Call
                                                               // (Front Speed)
 // For sprints this will be the time to the quarter-mile pole.
 // This is a guage of early speed, particularly to identify a
 // sole front runner.
 // In routes [8F to 10F] the first call is the half-mile pole.
 // Races longer than 10F usually will have the 6F pole as first call.
 public static final int SS        = 1;                       // Second Call
                                                               // (Sustained
                                                               // Speed)
 // Sprints, this is the half mile point.
 // In Routes it is at 6F pole. Routes over 1-13/16 mile
 // the second call may be the one mile pole.
 public static final int FT        = 2;                       // Final time
 public static final int TT        = 3;                       // Turn time (ss
                                                               // - fs)
 public static final int CS        = 4;                       // Closing time
                                                               // (ft - ss)
 // For races at 6,7,8,9,10F this wil be the final quarter.
 // For races at 5.5, 6.5, 7.5, 8.5, 9.5, and 10.5F the
 // last 1/16th.
 public static final int AS        = 5;                       // Adjusted speed
                                                               // = DRF Speed
                                                               // figure + DRF
                                                               // Variant as
                                                               // modified.
 public static final int RE        = 6;                       // Recency speed
                                                               // (within 28
                                                               // days).
 public static final int QP        = 7;                       // Quirin points
 public static final int EN        = 8;                       // Energy
                                                               // Quotient
 public static final int EPS       = 9;                        // Total Earnings
                                                               // per start
 public static final int TTCS       = 10;                      // TT + CS ranks
 public static final int FSTT       = 11;                      // FS + TT ranks
 public static final int PP         = 12;                      // Prime Power ranks
 public static final int ML         = 13;                      // Morning Line ranks
 public static final int FO          = 14;                      // Final Odds ranks
 public static String[]  names     = { "FS", "SS", "FT", "TT", "CS", "AS",
   "RE", "QP", "EN", "EPS","TTCS","FSTT","PP","ML","FO"        };
 public double[]         value     = new double[names.length]; // parameter
                                                               // values
 public int[]            rank      = new int[names.length];   // Handicap rank
 public int              points    = 0;
 public int              bonus     = 0;
 public int              bonusRank = 999;
 public int              formDaysLast = 0;
 public int              cntPP = 0;
 public int              cntClm = 0;
 public double           lowestClmPrice = 999999;
 public boolean          extraFlg;                             // Extra energy
                                                               // flag
 public Performance      m_repRace = null;                     // Representitive
                                                               // race
 public Performance      m_recency = null;                     // Recency race
 /**
  * Constructor
  */
 public Handicap() {
 }
 /**
  * Main driver for truline logic
  *
  * This is a static routine. It will create one instance of this class for each
  * horse in the race, each attached to the horse's Post instance.
  */
 public static void compute(Race race)
 {
  if (Log.isDebug(Log.MINIMUM)) {
   Log.print("\n================ Handicap for RACE #" + race.m_raceNo
     + "===================\n");
  }
  Truline.println("Handicap for Race #" + race.m_raceNo);
  // Get the track purse class for this race.
  // This is obtained from the truline.pc file
  // Which has fields TRACK, CLASS.
  Properties classData = Truline.pc.get("TRACK", race.m_track);
  if (classData != null) {
   String trackClassStr = classData.getProperty("CLASS");
   if (trackClassStr != null)
    race.m_trackClass = Lib.atof(trackClassStr);
  } else
   Log.print("WARNING: Could not find track " + race.m_track
     + " in truline.pc\n");
  // Get the race class for this race.
  // This is obtained from truline.rc file
  // Which has fields RACETYPE, PURSE, RACECLASS.
  race.m_raceType = race.m_props.getProperty("RACETYPE", "");
  race.m_purse = Lib.atoi(race.m_props.getProperty("PURSE", ""));
  race.m_claim = Lib.atoi(race.m_props.getProperty("CLAIMPRICE", ""));
  // race.m_purseClass = getPurseClass(race.m_raceType, race.m_purse);
  race.m_age = race.m_props.getProperty("AGESEX", "");
  // Maiden Claiming races are not bettable
  // if (race.m_raceType.equals("M"))
  //  race.m_bettable1 = "N";
  // Get the rest of the Race related data
  race.m_distance = Lib.atoi(race.m_props.getProperty("DISTANCE"));
  if (race.m_distance < 0)
   race.m_distance = -race.m_distance;
  if (race.m_surfaceResult.equals("") || race.m_surfaceResult.equals("$")) {
   race.m_surface = race.m_props.getProperty("SURFACE", "").toUpperCase();
   race.m_surfaceLC = race.m_props.getProperty("SURFACE", "");
  } else {
   race.m_surface = race.m_surfaceResult.toUpperCase();
   race.m_surfaceLC = race.m_surfaceResult;
  }
  race.m_allweather = race.m_props.getProperty("ALLWEATHER", "").toUpperCase();
  if (race.m_allweather.equals("A"))
   race.m_surface = race.m_allweather;
  race.cntHorseFlows = -1;
  race.cntRaceFlows = -1;
  race.cntRaceFlowsAK = -1;
  for (int i = 0; i < 21; i++) {
   race.raceFlows[i] = "";
   race.raceFlowsAK[i] = "";
  }
  if (race.m_surface.equals("X"))
   race.m_surface = "T";
  // 2-year old races are not bettable
  String sexAge = race.m_props.getProperty("AGESEX", " ");
  if (sexAge.charAt(0) == 'A')
   race.m_bettable2 = "N";
  // Races less than 6 furlongs or at odd distances are low-probability
  if (race.m_distance < 1320
    || (race.m_distance > 1760 && race.m_distance < 1870))
   race.m_bettable2 = "N";
  // Races at the lowest purses or claiming are also low probability
  if (race.m_trackClass < 2) // Better tracks
  {
   if ((race.m_claim > 0 && race.m_claim < 10000)
     || (race.m_claim == 0 && race.m_purse < 10000))
    race.m_bettable2 = "N";
  } else // lesser tracks
  {
   if ((race.m_claim > 0 && race.m_claim < 5000)
     || (race.m_claim == 0 && race.m_purse < 5000))
    race.m_bettable2 = "N";
  }
  int recencyDays = Lib
    .atoi(Truline.userProps.getProperty("RecencyDays", "28"));
  Calendar cal = Calendar.getInstance();
  cal.setTime(race.m_raceDate);
  cal.add(Calendar.DAY_OF_YEAR, -recencyDays);
  race.m_recencyDate = cal.getTime();
  race.m_maxvariant = Lib.atoi(Truline.userProps
    .getProperty("MaxVariant", "25"));
  race.m_useMaiden = Truline.userProps.getProperty("UseMaiden", "Y")
    .startsWith("Y");
  if (race.m_raceType.equals("M") || race.m_raceType.equals("S"))
   race.m_useMaiden = true;
  if (race.m_surface.equals("T"))
   race.m_maxdays = Lib.atoi(Truline.userProps.getProperty("MaxDaysTurf", "270"));
  else
   race.m_maxdays = Lib.atoi(Truline.userProps.getProperty("MaxDays", "180"));
  cal = Calendar.getInstance();
  cal.setTime(race.m_raceDate);
  cal.add(Calendar.DATE, -race.m_maxdays);
  race.m_cutoff = cal.getTime();
  race.totalPoints = 0;
  if (Log.isDebug(Log.TRACE)) {
   Log.print("  Track=" + race.m_track + ", Track class is "
     + race.m_trackClass + "\n");
   Log.print("  RaceDate=" + Lib.datetoa(race.m_raceDate) + "  Race #"
     + race.m_raceNo + "\n");
   Log.print("  Purse=" + race.m_purse + ", race type=" + race.m_raceType
     + ", Purse class is " + race.m_purseClass + "\n");
   Log.print("  Surface=" + race.m_surface + ", Distance="
     + toF(race.m_distance) + "\n");
   Log.print("  Maxdays days=" + race.m_maxdays + " which is "
     + Lib.datetoa(race.m_cutoff) + "\n");
   Log.print("  Recency days=" + recencyDays + " which is "
     + Lib.datetoa(race.m_recencyDate) + "\n");
   Log.print("  Variant cutoff is set at " + race.m_maxvariant + "\n");
   if (race.m_useMaiden)
    Log.print("  We are using maiden races.\n");
  }
  /*
   * Following routine is no longer necessary since BRIS file contains program
   * number // Determine the saddle cloth numbers on each horse. String[] part =
   * {"", "A", "B", "C", "D", "E", "F"}; int[] mate = {0, 0, 0}; int[] num = {0,
   * 0, 0}; int partnbr = 0; int field = 1; for(Enumeration e =
   * race.m_posts.elements(); e.hasMoreElements();) { Post post =
   * (Post)e.nextElement(); String entry = post.m_props.getProperty("ENTRY","");
   * if (entry.equals("A")) { if (mate[0] == 0) mate[0] = field++; post.cloth =
   * mate[0] + part[num[0]++]; } else if (entry.equals("B")) { if (mate[1] == 0)
   * mate[1] = field++; post.cloth = mate[1] + part[num[1]++]; } else if
   * (entry.equals("C")) { if (mate[2] == 0) mate[2] = field++; post.cloth =
   * mate[2] + part[num[2]++]; } else post.cloth = ""+(field++); }
   */
  // For each horse in the race, determine the representitive race
  // and compute its points.
  for (Enumeration e = race.m_posts.elements(); e.hasMoreElements();) {
   Post post = (Post) e.nextElement();
   post.cntHorseFlows = -1;
   // If results posted and horse did not finish, it was scratched
   if (Truline.userProps.getProperty("PostResults", "N").equals("Y")
     && race.m_resultsPosted.equals("Y") && !race.m_cloth1.equals("") 
     && post.m_finishPos.equals(""))
    post.m_props.setProperty("ENTRY", "S");
   if (horseScratched(race, post) == true)
    post.m_props.setProperty("ENTRY", "S");
   String entry = post.m_props.getProperty("ENTRY", "");
   if (post.m_horseName == null || entry.equals("S")) {
    // Log.errPrint("Scratch RACE #"+race.m_raceNo+" post="+post.m_postPosition+"  Cloth="+post.cloth+"\n");
    continue; // position is empty or scratched
   }
   post.m_handicap = new Handicap();
   for (int i = 0; i < post.m_handicap.value.length; i++) {
    post.m_handicap.value[i] = 0;
    post.m_handicap.rank[i] = 999;
   }
   post.m_handicap.evaluateHorse(race, post);
  }
  rankHorses(race);
  assignPoints(race);
  race.ranking = assignBonusPoints(race);
  if (Truline.userProps.getProperty("TL2014", "N").equals("Yes")) {
   identifyOtherPIFactors(race);
   identifyFlowBets(race);
   identifyRunStyle(race);
   identifyTrnFlowBets(race);
   identifyJkyFlowBets(race);
   identifyTrnJkyFlowBets(race);
   identifyRaceFlowBets(race);
   if (Truline.userProps.getProperty("ArtAndKim", "N").equals("Y"))
    identifyBonusFactors(race);
   
   // Sort top of handicap into sequence by saddle cloth
   Arrays.sort(race.raceFlows);
   Arrays.sort(race.raceFlowsAK);

  }
 }
 /**
  * For a given horse in a race, determine its representative race and then
  * compute its points. The points will establish the horse's ranking.
  */
 public void evaluateHorse(Race race, Post post)
 {
  if (Log.isDebug(Log.TRACE)) {
   Log.print("\nRace #" + race.m_raceNo + ", Post Position "
     + post.m_postPosition + " Horse: " + post.m_horseName + "\n");
   Log.print("                Weight: " + post.m_weight + "\n");
  }
  // Select representative race candidates and evaluate elements of past performances
  Vector candidates = selectCandidates(race, post);
  
  if (cntPP > 8 && cntClm == 0 && race.m_claim > 0)
   post.m_firstClmRace = true;
  else 
   post.m_firstClmRace = false;
  
  if (cntPP > 8 && race.m_claim > 0 && race.m_claim < lowestClmPrice)
   post.m_lowestClmPrice = true;
  else
   post.m_lowestClmPrice = false;
  
  if (candidates == null)
   return;
  // Determine which of the candidates is the best rep race.
  determineRepRace(race, post, candidates);
  if (m_repRace != null) {
   int repRaceDays = getDifferenceDays(m_repRace.ppRaceDate,race.m_raceDate);
   // if (repRaceDays == formDaysLast)
   // post.m_lastRacePurseComp = "";
   post.m_repRaceDtl = "RR="+m_repRace.ppTrack+"/"+m_repRace.m_props.getProperty("RACECLASSIFICATION","")+"/"
     +m_repRace.m_props.getProperty("PPPURSE")+"/"
     +m_repRace.m_props.getProperty("POSITION6", "")
     +(m_repRace.m_props.getProperty("POSITION6", "").equals("1") ? "/" : "+"+m_repRace.m_props.getProperty("LENGTHS4")+"/")
     +repRaceDays+" days";
   int pPurse = Lib.atoi(m_repRace.m_props.getProperty("PPPURSE",Lib.ftoa((double) race.m_purse,0)));
   double pursePct = race.m_purse * 100 / pPurse;
   if (pursePct > 119)
    post.m_repRacePurseComp = "CLASS UP - Purse is "+Lib.ftoa((double) pursePct-100,0)+"%"+" more than Rep Race ";
   if (pursePct < 81)
    post.m_repRacePurseComp = "CLASS DOWN - Purse is "+Lib.ftoa((double) 100-pursePct,0)+"%"+" less than Rep Race ";
   value[FS] = m_repRace.fs;
   value[SS] = m_repRace.ss;
   value[FT] = m_repRace.ft;
   value[TT] = m_repRace.tt;
   value[CS] = m_repRace.cs;
  }
  else
   post.m_repRaceDtl = "RR=none found";
  // Adjusted speed = DRF Speed figure + DRF Variant as modified.
  if (m_repRace != null)
   value[AS] = m_repRace.as; // for Representative race)
  if (m_recency != null)
   value[RE] = m_recency.as; // for Recency race
  // compute points
  value[QP] = quirinPoints(race, post);
  // compute EQ (Energy Quotient)
  value[EN] = computeEQ(race, post);
  // compute EPS (Earnings per start)
  value[EPS] = computeEPS(race, post);
  // compute PP (Prime Power)
  value[PP] = post.m_primePower;
  // store ML (Morning Line)
  double ml = Lib.atof(post.m_props.getProperty("MORNINGLINE"));
  value[ML] = ml;
  // store FO (Final Odds)
  double fo = 99;
  if (post.m_odds != null)
   fo = Lib.atof(post.m_odds);
  if (fo == 0.0)
   fo = 99;
  value[FO] = fo;
 }
 /**
  * Select representitive race candidates: Consider only if:
  *
  * 1) candidate race date is within 120 days (value 120 taken from the
  * BVmaxdays= in user file) 2) Both todays and candidates race are turf or both
  * are dirt. 3) candidate races's variant (DTV) is +20 or less (value +20 taken
  * from BVmaxvariant= in user file) 4) The distance of the canidate race must
  * match todays race according to the following table: 5F for 5F 5.5F for 5.5F
  * 6F or 6.5F for 6F or 6.5F 7F for 7F 7.5F for 7.5F 8F or 8.5F for 8F or 8.5F
  * 9F for 9F 9.5F for 9.5F 10F for 10F 11F for 11F 12F for 12F treat this as a
  * 9F and find a rep race among those.
  *
  *
  * 5) Only use Maiden races if the flag "Maiden in Win" = Y (flag from
  * BVuseMaiden=Y in user file) unless todays race is a maiden race. 6) Stakes
  * G1 - the only races that can be rep races are a finish of first, second, or
  * third in a G2 race or a Win in a G3 or other such race with a Purse of
  * $250,000 or higher. At G1 level, horses successful in Allowance, Claiming
  * and even Maiden Special Weight are not of a G1 quality. Making that jump
  * successfully would be a Cinderella Story. It would be thrilling, but it
  * wouldn't have MY money on it.
  *
  * G2 - A horse must have been first, second or third in a G2 or G3 race (or
  * naturally G1) or other race of $250,000 value or higher.
  *
  * G3 - Must have been first, second, or third in a Graded Stake, or a winner
  * of more than two races in a combination of Allowance and Maiden Special
  * Weight prior races.
  *
  * 7) Position data (lengths) and time data must be available. 8) Do not use
  * race if first two letters of track abbr match with something in 'EX' table
  * (exclude table) in the user file.
  */
 private Vector selectCandidates(Race race, Post post)
 {
  Vector candidates = new Vector();
  if (Log.isDebug(Log.TRACE)) {
   Log.print("  Selecting Candidates for Rep Race:\n");
  }
  int cntForm = -1;
  cntPP = 0;
  post.m_formCycle = "Last 10=";
  post.m_formCycle2 = "       ";
  post.m_formCycle3 = "       ";
  post.m_formCycle4 = "       ";
  post.m_formCycle5 = "       ";
  post.m_freshHorse = false;
  post.m_daysSinceLast = -1;
  post.m_finishPosLast = -1;


  int days1 = 0;
  int finish1 = 0;
  double speed1 = 0;
  int days2 = 0;
  int finish2 = 0;
  double speed2 = 0;
  int days3 = 0;
  int finish3 = 0;
  int days4 = 0;

  for (Enumeration e = post.m_performances.elements(); e.hasMoreElements();) {
   Performance p = (Performance) e.nextElement();
   cntPP++;
   
   // Post the oddball PP data into properties
   String pAllweather = post.m_props.getProperty("ALLWEATHER"+cntPP,"");
   if (!pAllweather.equals(""))
    p.m_props.put("ALLWEATHER",pAllweather);
   String pBarshoe = post.m_props.getProperty("BARSHOE"+cntPP,"");
   if (!pBarshoe.equals(""))
    p.m_props.put("BARSHOE",pBarshoe);
   String pNosepatch = post.m_props.getProperty("NOSEPATCH"+cntPP,"");
   if (!pNosepatch.equals(""))
    p.m_props.put("NOSEPATCH",pNosepatch);
   
   // Check the exclude list
   boolean found = false;
   for (Enumeration exList = Truline.ex.elements(); exList.hasMoreElements();) {
    Properties prop = (Properties) exList.nextElement();
    String exTrack = prop.getProperty("TRACK", "");
    if (exTrack.length() == 2)
     exTrack += "X";
    String track = p.ppTrack;
    if (track.length() == 2)
     track += "X";
    if (track.equals(exTrack)) {
     found = true;
     break;
    }
   }
   if (found) {
    if (Log.isDebug(Log.TRACE)) {
     Log.print("  Canidate: Track: " + p.ppTrack + " "
       + Lib.datetoa(p.ppRaceDate) + ", Race#" + p.ppRaceNo + "\n");
     Log.print("            Excluded track--skipped\n");
    }
    p.isExcluded = true;
    continue;
   }
   p.isExcluded = false;
   String pSurface = p.m_props.getProperty("PPSURFACE", "").toUpperCase();
   String pFavorite = p.m_props.getProperty("FAVORITE", "");
   String pTrkCond = p.m_props.getProperty("CONDITION", "").toUpperCase();
   pAllweather = p.m_props.getProperty("ALLWEATHER", "").toUpperCase();
   if (pAllweather.equals("A"))
    pSurface = pAllweather;
   double distanceAdj = distanceAdjustments(race.m_age, race.m_distance, p);
   p.variant = Lib.atoi(p.m_props.getProperty("VARIENT", "100"));
   p.drfSpeed = Lib.atof(p.m_props.getProperty("DRFSPEED"));
   double formSpeed = p.variant+p.drfSpeed;
   String pracetype = p.m_props.getProperty("PPRACETYPE", "");
   int finishPos = Lib.atoi(p.m_props.getProperty("POSITION6", "99"));
   
   // Find track class for PP
   double pTrackClass = 0;
   Properties classData = Truline.pc.get("TRACK", p.ppTrack);
   if (classData != null) {
    String trackClassStr = classData.getProperty("CLASS");
    if (trackClassStr != null)
     pTrackClass = Lib.atof(trackClassStr);
   }
   
   int purse = race.m_purse;
   int pPurse = Lib.atoi(p.m_props.getProperty("PPPURSE"));
   String pJockey = p.m_props.getProperty("PPJOCKEY");
   if (pJockey == null)
    pJockey = "None";
   String pTrainer = p.m_props.getProperty("PPTRAINER");
   if (pTrainer == null)
    pTrainer = "None";
   int pPurse2 = Lib.atoi(p.m_props.getProperty("PPPURSE",Lib.ftoa((double) race.m_purse,0)));
   int pClaim2 = Lib.atoi(p.m_props.getProperty("PPCLAIMPRICE","0"));
   
   if (pClaim2 > 0) {
    cntClm++;
    if (pClaim2 < lowestClmPrice)
     lowestClmPrice = pClaim2;
   }

   double pursePct = race.m_purse * 100 / pPurse2;
   double pursePctChg = pursePct - 100;
   String pursePctChgS = Lib.ftoa(pursePctChg,  0);
   String purseChgS = "PP " + Lib.ftoa(pPurse2, 0) + " to today " + Lib.ftoa(race.m_purse, 0);
   double claimPct = 100; 
   double claimPctChg = 0;
   if (pClaim2 > 0) {
    claimPct = race.m_claim * 100 / pClaim2;
    claimPctChg = claimPct - 100;
   }
   String claimPctChgS = Lib.ftoa(claimPctChg,  0);
   String claimChgS = "PP " + Lib.ftoa(pClaim2, 0) + " to today " + Lib.ftoa(race.m_claim, 0);
   
   if (cntForm == -1) {
    if (!pJockey.equals(post.m_jockeyName))
     post.m_jockeyChgToday = true;
    if (!pTrainer.equals(post.m_trainerName))
     post.m_trainerChgToday = true;
    post.m_lastRacePurseComp = "";
    post.m_lastRaceClassChg = pursePct-100;
    post.m_lastRaceClaimChg = claimPct-100;
    if (pursePct > 109) {
     post.m_lastRacePurseComp = "CLASS UP - Purse is "+Lib.ftoa((double) pursePct-100,0)+"%"+" more than Last Race ";
    }
    if (pursePct < 91) {
      post.m_lastRacePurseComp = "CLASS DOWN - Purse is "+Lib.ftoa((double) 100-pursePct,0)+"%"+" less than Last Race "; 
    }
    if (claimPct > 109) {
     post.m_lastRaceClaimComp = "CLASS UP - Claim price is "+Lib.ftoa((double) claimPct-100,0)+"%"+" more than Last Race ";
    }
    if (claimPct < 91) {
      post.m_lastRaceClaimComp = "CLASS DOWN - Claim price is "+Lib.ftoa((double) 100-claimPct,0)+"%"+" less than Last Race "; 
    }
    double classChange = pTrackClass - race.m_trackClass;
    if (classChange > .5)
     post.m_lastRaceTrackClass = "CLASS UP - Track class is "+Lib.ftoa((double) race.m_trackClass,0)+".  Last Race was "+Lib.ftoa((double) pTrackClass,0)+ " ";
    else if (classChange < -.5)
     post.m_lastRaceTrackClass = "CLASS DOWN - Track class is "+Lib.ftoa((double) race.m_trackClass,0)+".  Last Race was "+Lib.ftoa((double) pTrackClass,0)+ " ";

    // Set flag if last race MdnClm and was a win
    if (pracetype.equals("M") && finishPos == 1)
     post.m_lastMdnClmWin = true;
    else
     post.m_lastMdnClmWin = false;
   }
   
   int claim = Lib.atoi(p.m_props.getProperty("PPCLAIMPRICE"));
   String ab = p.m_props.getProperty("ABOUT");
   boolean about = (ab != null && ab.equals("Y"));
   String chute = p.m_props.getProperty("CHUTE", "");
   int runstyleJ = Lib.atoi(p.m_props.getProperty("PPRUNSTYLE", "-1"));
   String runstyle = "";
   if (runstyleJ == -1) {
    runstyle = "NA";
    runstyleJ = Lib.atoi(p.m_props.getProperty("RACESHAPE1", "99"));
    if (runstyleJ != 99) 
     runstyle = '"' + p.m_props.getProperty("RACESHAPE1", "") + " " + p.m_props.getProperty("RACESHAPE2", "") + '"'; 
   }
   else if (runstyleJ < 15)
    runstyle = "E";
   else if (runstyleJ < 30)
    runstyle = "EP";
   else if (runstyleJ < 45)
    runstyle = "P";
   else if (runstyleJ < 60)
    runstyle = "PS";
   else if (runstyleJ < 75)
    runstyle = "S";
   else if (runstyleJ < 90)
    runstyle = "SS";
   else 
    runstyle = "U";
    
   int formDays = getDifferenceDays(p.ppRaceDate,race.m_raceDate);
   if (cntForm == -1) {
    days1 = formDays;
    finish1 = finishPos;
    speed1 = formSpeed;
    formDaysLast = formDays;
    post.m_daysSinceLast = formDays;
    post.m_finishPosLast = finishPos;

    // Set flag if last race BIG WIN
    if (!pracetype.equals("M") && finishPos == 1 && (pTrkCond.equals("FT") || pTrkCond.equals("FM"))
         && Lib.atof(p.m_props.getProperty("LENLDRMGN4")) >= 5 
         && (formDays < 30 || (formDays < 45 && (pracetype.equals("G1") || pracetype.equals("G2") 
                                                  || pracetype.equals("G3") || pracetype.equals("N")))) 
         && !post.m_jockeyChgToday && !post.m_trainerChgToday)
     post.m_bigWinLast = true;
    else
     post.m_bigWinLast = false;
   }
   else if (cntForm == 0) {
    days2 = formDays;
    finish2 = finishPos;
    speed2 = formSpeed;
   }
   else if (cntForm == 1) {
    days3 = formDays;
    finish3 = finishPos;
   }
   else if (cntForm == 2)
    days4 = formDays;

   // If jockey change - see if today's jockey won previously with horse
   if (post.m_jockeyChgToday) {
    if (pJockey.equals(post.m_jockeyName) && finishPos == 1)
     post.m_jockeyTodayPrevWin = true;
   }
   
   // Make sure there is running line info
   double pos0 = Lib.atof(p.m_props.getProperty("POSITION1", "")); // Position
                                                                   // at
                                                                   // Starting
                                                                   // Gate
   double pos1 = Lib.atof(p.m_props.getProperty("POSITION2", "")); // Position
                                                                   // at First
                                                                   // Call
   double pos2 = Lib.atof(p.m_props.getProperty("POSITION3", "")); // Position
                                                                   // at Second
                                                                   // Call
   double pos3 = Lib.atof(p.m_props.getProperty("POSITION5", "")); // Position
                                                                   // at Third
                                                                   // Call
   double pos4 = Lib.atof(p.m_props.getProperty("POSITION6", "")); // Position
                                                                   // at Finish
   double f1 = Lib.atof(p.m_props.getProperty("FRACTION1")); // time of the
                                                             // leader
   double len1 = Lib.atof(p.m_props.getProperty("LENGTHS1")); // lengths behind
                                                              // leader
   double f2 = Lib.atof(p.m_props.getProperty("FRACTION2")); // time of the
                                                             // leader
   double len2 = Lib.atof(p.m_props.getProperty("LENGTHS2")); // lengths behind
                                                              // leader
   double f3 = Lib.atof(p.m_props.getProperty("FRACTION3")); // time of the
                                                             // leader
   double len3 = Lib.atof(p.m_props.getProperty("LENGTHS3")); // lengths behind
                                                              // leader
   double f4 = Lib.atof(p.m_props.getProperty("FINALTIME")); // time of the
                                                             // leader at finish
   double len4 = Lib.atof(p.m_props.getProperty("LENGTHS4")); // lengths behind
                                                              // leader at
                                                              // finish
   if (pos1 == 1)
    len1 = 0;
   if (pos2 == 1)
    len2 = 0;
   if (pos3 == 1)
    len3 = 0;
   if (pos4 == 1)
    len4 = 0;
   
   /*  Accumulate last 10 races */
   if (cntForm < 9) {
    cntForm++;
    if (cntForm < 2)
     post.m_formCycle = post.m_formCycle + (cntForm > 0 ? " / " : "") + formDays + "-" + p.m_props.getProperty("PPTRACK") + "-" + pSurface + "-"  + pracetype + "-" + (p.ppDistance < 1540 ? "SP-" : (p.ppDistance <= 1760 ? "MD-" : "RT-")) + finishPos + (finishPos == 1 ? "+"+p.m_props.getProperty("LENLDRMGN4") : "+"+p.m_props.getProperty("LENGTHS4")) + "-" + runstyle + "-" + Lib.ftoa((double) formSpeed, 0) + "-" + Lib.ftoa((int) pPurse, 0);  
    else if (cntForm < 4)
     post.m_formCycle2 = post.m_formCycle2 + (cntForm > 2 ? " / " : "") + formDays + "-" + p.m_props.getProperty("PPTRACK") + "-" + pSurface + "-" + pracetype + "-" + (p.ppDistance < 1540 ? "SP-" : (p.ppDistance <= 1760 ? "MD-" : "RT-")) + finishPos + (finishPos == 1 ? "" : "+"+p.m_props.getProperty("LENGTHS4")) + "-" + runstyle + "-" + Lib.ftoa((double) formSpeed, 0) + "-" + Lib.ftoa((int) pPurse, 0);
    else if (cntForm < 6)
     post.m_formCycle3 = post.m_formCycle3 + (cntForm > 4 ? " / " : "") + formDays + "-" + p.m_props.getProperty("PPTRACK") + "-" + pSurface + "-" + pracetype + "-" + (p.ppDistance < 1540 ? "SP-" : (p.ppDistance <= 1760 ? "MD-" : "RT-")) + finishPos + (finishPos == 1 ? "" : "+"+p.m_props.getProperty("LENGTHS4")) + "-" + runstyle + "-" + Lib.ftoa((double) formSpeed, 0) + "-" + Lib.ftoa((int) pPurse, 0);
    else if (cntForm < 8)
     post.m_formCycle4 = post.m_formCycle4 + (cntForm > 6 ? " / " : "") + formDays + "-" + p.m_props.getProperty("PPTRACK") + "-" + pSurface + "-" + pracetype + "-" + (p.ppDistance < 1540 ? "SP-" : (p.ppDistance <= 1760 ? "MD-" : "RT-")) + finishPos + (finishPos == 1 ? "" : "+"+p.m_props.getProperty("LENGTHS4")) + "-" + runstyle + "-" + Lib.ftoa((double) formSpeed, 0) + "-" + Lib.ftoa((int) pPurse, 0);
    else 
     post.m_formCycle5 = post.m_formCycle5 + (cntForm > 8 ? " / " : "") + formDays + "-" + p.m_props.getProperty("PPTRACK") + "-" + pSurface + "-" + pracetype + "-" + (p.ppDistance < 1540 ? "SP-" : (p.ppDistance <= 1760 ? "MD-" : "RT-")) + finishPos + (finishPos == 1 ? "" : "+"+p.m_props.getProperty("LENGTHS4")) + "-" + runstyle + "-" + Lib.ftoa((double) formSpeed, 0) + "-" + Lib.ftoa((int) pPurse, 0);
   }
   
   
   p.isRoute = isRoute(p);
   boolean stakesRace = false;
   boolean pHighStakesRace = false;
   /*
    * Check the statkes rep race option flag - if not set then old rules apply
    */
   if (Truline.userProps.getProperty("RRStakesCheck", "A").equals("N"))
     pHighStakesRace = true;
   else if (Truline.userProps.getProperty("RRStakesCheck", "A").equals("T") && race.m_surface.equals("D"))
    pHighStakesRace = true;
   else if (Truline.userProps.getProperty("RRStakesCheck", "A").equals("D") && race.m_surface.equals("T"))
    pHighStakesRace = true;
   else if (Truline.userProps.getProperty("RRStakesCheck", "A").equals("T") && race.m_surface.equals("A"))
    pHighStakesRace = true;

   /*
    * For 2-year olds, accept any otherwise qualifying race as a Candidate if
    * today is Turf
    */
   if (race.m_age.startsWith("A")
     && (race.m_raceType.equals("G1") || race.m_raceType.equals("G2"))
     && p.isRoute)
    pHighStakesRace = true;
   if (race.m_raceType.equals("G1") || race.m_raceType.equals("G2")) {
    stakesRace = true;
    if (pracetype.equals("G1")) {
     pHighStakesRace = true;
    } else if (pracetype.equals("G2")) {
     if (pos4 >= 1 && pos4 <= 3) // finished in the top three positions
      pHighStakesRace = true;
    } else if (pracetype.equals("G3")) {
     if (pos4 == 1) // finished first
      pHighStakesRace = true;
    } else if (purse >= 250000) {
     if (pos4 >= 1 && pos4 <= 3) // finished in the top three positions
      pHighStakesRace = true;
    }
   } else if (race.m_raceType.equals("G3") && (purse >= 250000 || purse == 0)) {
    stakesRace = true;
    if (pracetype.equals("G1")) {
     pHighStakesRace = true;
    } else if (pracetype.equals("G2") || pracetype.equals("G3")) {
     if (pos4 >= 1 && pos4 <= 3) // finished in the top three positions
      pHighStakesRace = true;
    } else if ((pracetype.equals("A") || pracetype.equals("S"))
      && purse > 50000) {
     if (pos4 == 1) // finished first
      pHighStakesRace = true;
    }
   }
   if (Log.isDebug(Log.TRACE)) {
    Log.print(" Candidate: Track: " + p.ppTrack + " "
      + Lib.datetoa(p.ppRaceDate) + ", Race#" + p.ppRaceNo + "\n");
    Log.print("            " + ((p.isRoute) ? "ROUTE " : "SPRINT ")
      + " - Race Classification=" + p.m_props.getProperty("RACECLASSIFICATION")
      + "\n");
    Log.print("            " + Lib.dateDiff(p.ppRaceDate, race.m_raceDate)
      + " days back, "
      + (p.ppRaceDate.after(race.m_cutoff) ? "Within " : "Outside ")
      + race.m_maxdays + " days\n");
    Log.print("            Surface=" + pSurface + ", "
      + ((race.m_surface.equals(pSurface)) ? "SAME" : "DIFFERENT")
      + ((!chute.equals("")) ? (" Chute=" + chute) : "") + "\n");
    Log.print("            Distance="
      + toF(p.ppDistance)
      + ", "
      + ((p.ppDistance > F10) ? " Greater than 10F - cannot use"
        : (distanceAdj != 999) ? "SIMILAR" : "DIFFERENT")
      + ((about) ? " (ABOUT)" : "") + "\n");
    Log.print("            variant="
      + ((p.variant == 100) ? "No Variant Given" : p.variant + ", "
        + ((p.variant <= race.m_maxvariant) ? "OK" : "Beyond cutoff"))
      + ", DRF Speed rating=" + p.drfSpeed
      + ((p.drfSpeed == 0) ? " Cannot use" : "") + "\n");
    if (Truline.userProps.getProperty("ArtAndKim", "N").equals("Y")) {
     Log.print("            track class="
       + ((pTrackClass == 0) ? "Track Class Not Found" : pTrackClass + ", "
         + ((pTrackClass+.5 < race.m_trackClass) ? "Lower class not used" : "OK"))
         + "\n");
     Log.print("            purse change="
       + purseChgS + ", " 
       + ((pursePctChg > Lib.atoi(Truline.userProps.getProperty("MaxPurseIncrease", "20"))) ? "Purse increase of "+pursePctChgS+" is too much" : pursePctChgS)
         + "\n");
    }
    Log
      .print("            "
        + ((!pracetype.equals("M") && !pracetype.equals("S")) ? "Not Maiden Race, OK"
          : (race.m_useMaiden) ? "Using Maiden Race"
            : "Maiden Race, cannot use") + "\n");
    Log.print("            Race type=" + pracetype + ", purse=" + purse
      + ", claim=" + claim + "\n");
    Log.print("            "
      + ((!stakesRace) ? "Todays race Not Stakes Race - OK"
        : (pHighStakesRace) ? "Qualifies for high Stakes Race - OK"
          : "Does not qualify for high Stakes Race - Cannot use") + "\n");
    Log.print("            Available Data: Gate Position=" + pos0 + "\n");
    Log.print("                    Call#1: POS=" + pos1 + ", LEN=" + len1
      + " Time=" + f1 + "\n");
    Log.print("                    Call#2: POS=" + pos2 + ", LEN=" + len2
      + " Time=" + f2 + "\n");
    Log.print("                    Call#3: POS=" + pos3 + ", LEN=" + len3
      + " Time=" + f3 + "\n");
    Log.print("                    Finish: POS=" + pos4 + ", LEN=" + len4
      + " Time=" + f4 + "\n");
    String frac = "";
    for (int i = 2; i < 13; i++) {
     String f = p.m_props.getProperty("FRACTION" + i + "F");
     if (f != null) {
      frac += ((!frac.equals("")) ? ", " : "") + i + "F=" + f;
     }
    }
    Log.print("                    " + frac + "\n");
    if (pos2 == 0 || f2 == 0)
     Log.print("                     Not usable, no running line\n");
   }
   if (p.ppRaceDate.after(race.m_cutoff) // within 365 days
     && ((Truline.userProps.getProperty("IgnoreSurface", "N").equals("Y")) || race.m_surface.equals(pSurface) || 
         (Truline.userProps.getProperty("ArtificialToDirt", "N").equals("Y") && race.m_surface.equals("D") && pSurface.equals("A"))) // same surface
     && p.drfSpeed > 0 // Must have DRFSpeed rating
     && p.variant <= race.m_maxvariant // Less than +25 variant (DVT cutoff)
     && (p.ppDistance <= F10 || p.ppDistance == race.m_distance) // Not over 10F
                                                                 // or same
                                                                 // distance as
                                                                 // today
     && distanceAdj != 999 // similar distance
     && (race.m_useMaiden || (!pracetype.equals("M") && !pracetype.equals("S"))) // use
                                                                                 // maiden
     && (!stakesRace || pHighStakesRace) // stakes match
//     && (pTrackClass+.5 <= race.m_trackClass || Truline.userProps.getProperty("ArtAndKim", "N").equals("N"))
//     && ((pursePctChg < Lib.atoi(Truline.userProps.getProperty("MaxPurseIncrease", "20")) || pracetype.equals("N") || pracetype.equals("G1") || pracetype.equals("G2") || pracetype.equals("G3")) 
//       || Truline.userProps.getProperty("ArtAndKim", "N").equals("N"))
     && (pos2 != 0 && f2 != 0)) {
    candidates.addElement(p);
    if (Log.isDebug(Log.TRACE))
     Log.print("            Acceptable candidate\n\n");
   } else {
         if (Log.isDebug(Log.TRACE))
     Log.print("            Candidate not used\n\n");
   }
  }
/**
  if (((days2 < 45 && days3 > 0 && days3 > days2+33 && days3 < days2+181 && finish1 != 1 && finish2 != 1) ||
       (days2 < 45 && days4 > 0 && days4 > days3+33 && days4 < days3+181 && finish1 != 1 && finish2 != 1 && finish3 != 1)) &&
       (speed1 > speed2)) {
   post.m_freshHorse = true;
   if (Truline.userProps.getProperty("ArtAndKim", "N").equals("Y") && race.cntRaceFlows < 20) {
    race.cntRaceFlows++;
    race.raceFlows[race.cntRaceFlows] = String.format("%-22s", post.cloth+" "+post.m_horseName)+" *** Fresh Horse Plus BF ***";
   }
  }
 **/
   
  return candidates;
 }
 /**
  * For each horse in race, determine what would make a good representitive race
  * and a Recency race from among the candidates. --------------------
  * Basically, the representative race refers to the running line that gives the
  * fractional times and final time of the race. We want to use the horse's best
  * effort.
  *
  * In a separate location as to the horse's information, we are given a speed
  * rating (a value hinged to a historical average for the
  * distance/class/age/sex. And we are given a Daily Track Variant, which is to
  * identify that particular day's track surface characteristics vs what would
  * be described as a "true fast" track.
  *
  * Let me give as an example two races in a horse's history:
  *
  *
  * Oct, 12 6F 22.1 45.1 109:4 91 12 Nov. 18 6F 22.4 45.3 110:4 86 20
  *
  * Based on final time, the first race would seemingly be the best. This is not
  * the case. The Daily Track Variant would alter these two running lines to:
  *
  * Oct. 12 6f 21.7 44.0 107.4 Nov. 18 6f 22.0 43.6 106.6
  *
  * (Note: the DRF figures are in minutes and fifths of a second. Our revised
  * running line is in tenths.) At any rate, the second race was the best race.
  * Similarly, if simply adding speed rating and Daily Track Variant (AS), the
  * second race woule be the best race.
  *
  * The program is also supposed to include in this adjustment factors for
  * changes in Track Class, Purse Class, weight and distance.
  *
  * As a totally separate computation, Recency is a measurement of the horse's
  * best effort in the last 28 days. This is the highest total of speed rating
  * and Daily Track Variant in the last 28 days. When this value is lower that
  * AS, then it is telling the User that the rep race came from an earlier time
  * period, and may be an aspect of "Back Class".
  */
 public void determineRepRace(Race race, Post post, Vector candidates)
 {
  double adj;
  if (Log.isDebug(Log.TRACE))
   Log.print("  Select Best Rep Race from Qualified candidates\n");
  // look at each candidate and determine the best match
  for (Enumeration c = candidates.elements(); c.hasMoreElements();) {
   Performance p = (Performance) c.nextElement();
   // Compute speed
   // compute speed rating = BRIS speed rating for this race
   // + adjusted variant
   p.as = p.drfSpeed;
   if (Log.isDebug(Log.TRACE)) {
    Log.print("    Candidate: Track: " + p.ppTrack + " "
      + Lib.datetoa(p.ppRaceDate) + ", Race#" + p.ppRaceNo + "\n");
    Log.print("              DRF speed rating  =" + p.drfSpeed + "\n");
    Log.print("              DRF Track Variant =" + p.variant + "\n");
   }
   // Weight adjustments
   adj = weightAdjustments(post.m_weight, p);
   p.variant += adj;
   // Distance adjustments
   adj = 0;
   if (race.m_distance == F5_0 && p.ppDistance == F6_0)
    adj = 5;
   if (race.m_distance == F5_5 && p.ppDistance == F6_0)
    adj = 7;
   if (race.m_distance == F6_0 && p.ppDistance == F5_5)
    adj = -7;
   if (Log.isDebug(Log.TRACE) && adj != 0) {
    Log.print("              distance adjustment=" + adj + "  ("
      + toF(p.ppDistance) + " to " + toF(race.m_distance) + ")\n");
   }
   p.variant += adj;
   // Class adjustments
   // Track Purse Class Adjustments:
   // lookup rep race track in the class table.
   // Every track has a number #1 thru #8. ONE POINT should be added
   // variant for each level of Class drop, ONE POINT subtracted from
   // each level of upward move.
   if (race.m_trackClass > 0) {
    Properties trackData = Truline.pc.get("TRACK", p.ppTrack);
    if (trackData != null) {
     String trackClassStr = trackData.getProperty("CLASS");
     if (trackClassStr != null) {
      adj = (race.m_trackClass - Lib.atof(trackClassStr));
      if (Log.isDebug(Log.TRACE))
       Log.print("              track class " + trackClassStr + " adjustment="
         + adj + "\n");
      p.variant += adj;
     }
    } else {
     if (Log.isDebug(Log.TRACE))
      Log
        .print("              Track purse class not found in purse class table\n");
    }
   }
   /*
    *  *** The race type / purse class change is no longer used // Purse Class
    * Adjustments: // lookup rep race type and purse in class table. // At lower
    * Class levels, we adjusted a length (a point) // for each $1000 of Purse.
    * At Allowance level, We adjusted // one length (point) for each $3000 of
    * Purse. // The amount of adjustment for each level is set in the // race
    * class table. if (race.m_purseClass > 0) { String raceType =
    * p.m_props.getProperty("PPRACETYPE",""); int purse =
    * Lib.atoi(p.m_props.getProperty("PURSE","")); if
    * (race.m_raceType.equals(raceType) && purse == 0) { if
    * (Log.isDebug(Log.TRACE))
    * Log.print("              purse is 0, assume same class (Purse="
    * +purse+", race type="+raceType+")\n"); } else { double cl =
    * getPurseClass(raceType, purse); if (cl > 0) { adj = cl -
    * race.m_purseClass; if (Log.isDebug(Log.TRACE))
    * Log.print("              Purse class="
    * +cl+", adjustment="+adj+" (Purse="+purse+", race type="+raceType+")\n");
    * p.variant += adj; } else { if (Log.isDebug(Log.TRACE))
    * Log.print("              Race type not found, assuming same. (Purse="
    * +purse+", race type="+raceType+")\n"); } } }
    */
   if (Log.isDebug(Log.TRACE))
    Log.print("              Total adjustment=" + p.variant + " (1/5ths)\n");
   p.as += p.variant;
   if (Log.isDebug(Log.TRACE))
    Log.print("              Computed AS=" + p.as + "\n");
   // Select Running line rep race
   // Pick the race with the best speed rating to be used
   // with Running line and AS.
   // If there is no rep race for a horse, the running
   // line will produce all zeroes.
   if (m_repRace == null || p.as > m_repRace.as) {
    m_repRace = p;
   }
   // Select Recency rep race
   // Pick a second race with the best speed rating within
   // last 28 days to be used with Recency.
   if (!p.ppRaceDate.before(race.m_recencyDate)
     && (m_recency == null || p.as > m_recency.as)) {
    m_recency = p;
   }
  }
  // Adjust Representitive race
  if (m_repRace != null) {
   if (Log.isDebug(Log.TRACE))
    Log.print("    Rep Race: Track: " + m_repRace.ppTrack + " "
      + Lib.datetoa(m_repRace.ppRaceDate) + ", Race#" + m_repRace.ppRaceNo
      + ", AS=" + m_repRace.as + "\n");
   adjRunningLine(race, m_repRace);
  } else {
   if (Log.isDebug(Log.TRACE))
    Log.print("    Rep Race: (nothing found)\n");
  }
  // Adjust Recency
  if (m_recency != null) {
   if (Log.isDebug(Log.TRACE))
    Log.print("    Recency Race: Track: " + m_recency.ppTrack + " "
      + Lib.datetoa(m_recency.ppRaceDate) + ", Race#" + m_recency.ppRaceNo
      + ", AS=" + m_recency.as + "\n");
   adjRunningLine(race, m_recency);
  } else {
   if (Log.isDebug(Log.TRACE))
    Log.print("    Recency Race: (nothing found)\n");
  }
 }
 /**
  * Apply Variant to running line Apply the variant (in units of 1/5 sec) 20%
  * for first time. (FS) 50% for second time, (SS) and 100% to the final
  * time.(FT) The TT and CS are obtained by subtracting.
  */
 private void adjRunningLine(Race race, Performance p)
 {
  double[] fraction = new double[3];
  double[] lengths = new double[3];
  // First Call
  fraction[0] = Lib.atof(p.m_props.getProperty("FRACTION1")); // time of the
                                                              // leader
  lengths[0] = Lib.atof(p.m_props.getProperty("LENGTHS1")); // lengths behind
                                                            // leader
  // Second Call
  fraction[1] = Lib.atof(p.m_props.getProperty("FRACTION2")); // time of the
                                                              // leader
  lengths[1] = Lib.atof(p.m_props.getProperty("LENGTHS2")); // lengths behind
                                                            // leader
  // Third Call
  fraction[2] = Lib.atof(p.m_props.getProperty("FRACTION3")); // time of the
                                                              // leader
  lengths[2] = Lib.atof(p.m_props.getProperty("LENGTHS3")); // lengths behind
                                                            // leader
  // At finish
  double fraction4 = Lib.atof(p.m_props.getProperty("FINALTIME")); // time of
                                                                   // winner
  String valueT = p.m_props.getProperty("LENGTHS4");
  double lengths4 = Lib.atof(p.m_props.getProperty("LENGTHS4")); // lengths
                                                                 // behind
                                                                 // winner
  if (!p.isRoute) {
   // ********* SPRINT ********************
   int firstCall = 0;
   int secondCall = 1;
   // For the first call, use the one that is between 19.0 and 25.0
   if (fraction[0] >= 19.0 && fraction[0] <= 25.0)
    firstCall = 0;
   else if (fraction[1] >= 19.0 && fraction[1] <= 25.0)
    firstCall = 1;
   // For the second call, it will use the fraction that is between 43 and 52.
   if (fraction[1] >= 43.0 && fraction[1] <= 52.0)
    secondCall = 1;
   else if (fraction[2] >= 43.0 && fraction[2] <= 52.0)
    secondCall = 2;
   p.fs = fraction[firstCall] + lengths[firstCall] * 0.2;
   p.ss = fraction[secondCall] + lengths[secondCall] * 0.2;
   p.ft = fraction4 + lengths4 * 0.2;
   // Distance adjustments
   double adj = distanceAdjustments(race.m_age, race.m_distance, p);
   p.ft += adj;
   p.tt = p.ss - p.fs;
   p.cs = p.ft - p.ss;
   if (Log.isDebug(Log.TRACE)) {
    Log.print("           SPRINT\n");
    Log.print("           First  Call:  leader Time=" + fraction[firstCall]
      + ", Lengths behind=" + lengths[firstCall] + "\n");
    Log.print("           Second Call:  leader Time=" + fraction[secondCall]
      + ", Lengths behind=" + lengths[secondCall] + "\n");
    Log.print("           Finish:       leader Time=" + fraction4
      + ", Lengths behind=" + lengths4 + "\n");
    if (adj != 0)
     Log.print("           Final time adjusted by " + adj
       + " sec for distance difference (" + toF(p.ppDistance) + " to "
       + toF(race.m_distance) + ")\n");
    Log.print("           Running line:     FS=" + Lib.ftoa(p.fs, 2) + ", SS="
      + Lib.ftoa(p.ss, 2) + ", FT=" + Lib.ftoa(p.ft, 2) + ", TT="
      + Lib.ftoa(p.tt, 2) + ", CS=" + Lib.ftoa(p.cs, 2) + "\n");
   }
  } else {
   // **********ROUTE**********************
   int firstCall = 1;
   int secondCall = 2;
   // For the first call, use the one that is between 44.0 and 60.0
   if (fraction[0] >= 44.0 && fraction[0] <= 60.0)
    firstCall = 0;
   else if (fraction[1] >= 44.0 && fraction[1] <= 51.0)
    firstCall = 1;
   // For the second call, it will use the fraction that is between 1:08 and
   // 1:20.
   if (fraction[1] >= 67.0 && fraction[1] <= 80.0)
    secondCall = 1;
   else if (fraction[2] >= 67.0 && fraction[2] <= 80.0)
    secondCall = 2;
   p.fs = fraction[firstCall] + lengths[firstCall] * 0.2;
   p.ss = fraction[secondCall] + lengths[secondCall] * 0.2;
   p.ft = fraction4 + lengths4 * 0.2;
   // Distance adjustments
   double adj = distanceAdjustments(race.m_age, race.m_distance, p);
   p.ft += adj;
   p.tt = p.ss - p.fs;
   p.cs = p.ft - p.ss;
   if (Log.isDebug(Log.TRACE)) {
    String text[] = { "First ", "Second", "Third " };
    Log.print("           ROUTE\n");
    Log.print("           " + text[firstCall] + " Call:  leader Time="
      + fraction[firstCall] + ", Lengths behind=" + lengths[firstCall] + "\n");
    Log
      .print("           " + text[secondCall] + " Call:  leader Time="
        + fraction[secondCall] + ", Lengths behind=" + lengths[secondCall]
        + "\n");
    Log.print("           Finish:       leader Time=" + fraction4
      + ", Lengths behind=" + lengths4 + "\n");
    if (adj != 0)
     Log.print("           Final time adjusted by " + adj
       + " sec for distance difference (" + toF(p.ppDistance) + " to "
       + toF(race.m_distance) + ")\n");
    Log.print("           Running line:     FS=" + Lib.ftoa(p.fs, 2) + ", SS="
      + Lib.ftoa(p.ss, 2) + ", FT=" + Lib.ftoa(p.ft, 2) + ", TT="
      + Lib.ftoa(p.tt, 2) + ", CS=" + Lib.ftoa(p.cs, 2) + "\n");
   }
  }
  p.fs = p.fs - (p.variant * 0.20) * 0.20;
  p.ss = p.ss - (p.variant * 0.20) * 0.50;
  p.ft = p.ft - (p.variant * 0.20);
  p.tt = p.ss - p.fs;
  p.cs = p.ft - p.ss;
  if (Log.isDebug(Log.TRACE))
   Log.print("           Adj Running line: FS=" + Lib.ftoa(p.fs, 2) + ", SS="
     + Lib.ftoa(p.ss, 2) + ", FT=" + Lib.ftoa(p.ft, 2) + ", TT="
     + Lib.ftoa(p.tt, 2) + ", CS=" + Lib.ftoa(p.cs, 2) + "\n");
 }
 /**
  * Weight adjustments: The weight difference is weight of todays race minus the
  * weight of the candidate race.
  *
  * For sprints (7.5F and less) if (weight < 119) Add (weight difference / 3) to
  * Variant. else Add (weight difference / 2) to Variant.
  *
  * For routes (8F and up) if (weight < 119) Add (weight difference / 2) to
  * Variant. else Add weight difference to Variant.
  *
  * (Note the weight difference will be negative if todays weight is lighter
  * than the rep race (dropping weight) so when we add the negative number to
  * the Variant it actually subtracts.
  *
  * @param weight
  *         - todays race's weight
  * @param p
  *         - a previous performance for this horse.
  * @return the adjustment to the variance (1/5ths).
  *
  *         When we get to adjustments to be added to the Variant - as related
  *         to Track Class, Purse Class, Weight (and doubtfully distance), all
  *         of these modifiers are in fifths of a second. Using weight as an
  *         example, in a sprint race up to 119# - from 110# to 119# - weight
  *         makes the least difference. Three pounds will slow a horse a fifth
  *         of a second. Most jockeys weigh 114-119. So almost all of this
  *         weight is the jockey, which we call "live weight). But, if a horse
  *         is assigned 122#, and the jockey weighs 114#, the horse must carry
  *         eight pounds in a saddle cloth. This is dead weight, and much more
  *         fatiguing. So, in sprints, if adding weight, we add 1/5 for every
  *         three pounds or fraction thereof up to 119#. Above 119#, we add 1/5
  *         for each additional two pounds or fraction thereof. If dropping
  *         weight, the same principle, just reverse the polarity - a deduction
  *         rather that an addition to the Variant.
  *
  *         In routes, we +/- 1/5 for every two pounds added up to 119#, and 1/5
  *         for every pound over 119#. Yes, this hinges on thirty-plus years of
  *         watching over 100,000 races.
  *
  *         Example: A horse is DROPPING three pounds - say from 118# to 115#.
  *         This should theoretically let the horse run one-fifth FASTER, so the
  *         adjustment should be -1 instead of +1. Anything suggesting the horse
  *         speeds up is SUBTRACTED from the Variant. Anything suggesting that
  *         the horse will run SLOWER is added to the Variant.
  */
 private double weightAdjustments(int weight, Performance p)
 {
  int pWeight = Lib.atoi(p.m_props.getProperty("PPWEIGHT"));
  if (pWeight == 0 || weight == 0)
   return 0;
  int dif1 = 0;
  int dif2 = 0;
  if (pWeight < 120) {
   if (weight < 120)
    dif1 = weight - pWeight;
   else { // weight is above pWeight is below
    dif1 = 120 - pWeight;
    dif2 = weight - 120;
   }
  } else if (weight >= 120)
   dif2 = weight - pWeight;
  else { // pWeight is above and weight is below
   dif1 = 120 - weight;
   dif2 = pWeight - 120;
  }
  double adj = 0;
  if (!isRoute(p)) {
   // For Sprints
   adj = ((double) -dif1) / 3.0;
   adj += ((double) -dif2) / 2.0;
  } else {
   // For Routes
   adj = ((double) -dif1) / 2.0;
   adj += ((double) -dif2);
  }
  if (Log.isDebug(Log.TRACE))
   Log.print("              Weight Adjustment=" + adj + ",  (Weight=" + pWeight
     + " to " + weight + ") " + dif1 + "lb and " + dif2 + "lb\n");
  return adj;
 }
 /**
  * (1) 5f - at present we use 5f, 5.5f, and 6f. This a race that starts on the
  * main track just before the turn. These races are typically a "dash for the
  * cash" - first horse out of the gate can have a big advantage. There is not
  * enough distance for an experienced horse to close a lot of ground. Also,
  * many races at this distance are for young (and/or "cheap") horses that don't
  * really know what they are doing.
  *
  * 5.5f - also starts on the main track (not from a "chute"), and involves many
  * of the characteristics of the 5F race. I am increasingly reluctant to use a
  * representative race that is not also a "short sprint". At present, a rep
  * race for 5.5f can be 5f, 6f, or 6.5f.
  *
  * I am inclined to favor TRULINE 2000 to shift gears - if today's race is 5f
  * or 5.5f, the rep race must be 5f or 5.5f. If today's race was at 5.5F, and
  * the rep race is at 5f, we would add six SECONDS to the final time. If
  * today's race was at 5F, and we used a rep race at 5.5F, we would subtract
  * six seconds.
  *
  * (2) MAJOR CLARIFICATION - in a sprint, the first two calls are quarter mile
  * and half mile. In a route, the second and third calls are the half mile and
  * six furlong points. These distances do not change, regardless of the total
  * distance of the race. The amount of a horse's energy that is expended in
  * these calls is the same regardless of the total distance and final time. So
  * these "distance adjustments" are limited to final time only.
  *
  * (3) If the race is at 6f or 6.5F, at present we use any distance that is
  * within one eighth of a mile. I am inclined for a race at 6F to limit to 6f
  * and 6.5f. The race at 7f is within 1/8 mile. But I find 7f to be a problem
  * distance. Particularly, jockeys don't ride this distance enough, and too
  * many do not know how to rate the horse appropriately. They either give the
  * horse the same cues as for six furlongs, or else they give the cues for 8f -
  * both of which are wrong.
  *
  * To explain, if a horse is to sustain his energy for the entire race, he must
  * "change leads" at specific points. If he led with the same front leg
  * throughout, he would exhaust himself much quicker, always extending the one
  * leg full out and taking the brunt of the surface contact, instead of
  * dividing this between the front legs.
  *
  * IF Michael agreed, then we would deal - 6f vs 6.5f - with a distance
  * adjustment of six and three-fifth seconds (33 fifths). But these final times
  * adjustments would not effect the modified Variant. Variant is limited to the
  * other things that involve the horse's fatigue factor and competitive
  * ability.
  *
  * (4) If today's race is at 7F, I am nervous at using any distance but today's
  * distance. This, again, is a big change from the present program, where we go
  * +/- 1/8 of a mile. THIS, however, is a matter of philosophy, and I would not
  * want to superimpose my reaction arbitrarily. Michael - what do you think?
  * And should we ask other Users for their opinion.
  *
  * (5) If, usually rare occasions, today's race is at 7.5f, punt. Excuse my
  * French, but this is a bastard distance. At most, I would limit to 7F and 8F
  * for the rep race. Would limit to .5 differential.
  *
  * (6) 8f is what is referred to as a "middle distance". Quite a few horses
  * that are sprint-bred can stretch out this far (with the right trainer and
  * conditioning), where rarely can they go farther. Similarly, some route-bred
  * horses can shorten up this much, but do not have enough pure speed to handle
  * shorter distances. Unfortunately, in current time, we see more and more
  * races at 8f, carded to accommodate route horses that are injured, and cannot
  * sustain anymore the longer distances.
  *
  * If today's race is at 8f, I would probably support a rep race from 7f (with
  * a thirteen-second change to final time), as well as a rep race at 8.5 (six
  * and three-fifths second difference) and 9F (also thirteen seconds
  * difference).
  *
  * (7) If today's race is at 8.5f, we now use +/- 1/8. I favor a change to +/-
  * 1/16. Michael - remember, you have an overriding vote in all matters. I rely
  * on your judgement immensely.
  *
  * (8) If today's race is at 9F, we can use +/- 1/8, which can involve either a
  * +/- seven seconds (if using 8.5 or 9.5 races) or +/- thirteen seconds (if
  * using 8f or 10f).
  *
  * (9) If today's race is 10f or longer, there is a major problem in that the
  * entire running line changes. The first call is a half mile, the second call
  * is one mile. Basically, the way I think we should deal with this is - if
  * today's race is 10F or longer, we arbitrarily handicap it as a race at 9.5f.
  * Primarily, we are using as rep races 8.5F and 9F - using "extensions" of
  * seven seconds and thirteen seconds. (Note: if we handicapped the longer
  * routes with the same philosophy, ALL horses entered would have few to none
  * rep races to consider because these longer races are few except for Graded
  * Stakes. The present TRULINE has shown that using 9.5f works.
  *
  * Distance Adjustments: If we are only going to compare same-length races then
  * we don't need to do any length adjustments except: Todays Race Rep Race
  * Final Time DRF (seconds) SPEED RATING 5.0F 5.0F 0 0 5.5F -6 0 6.0F -12 +5
  * (no 2yr olds) 5.5F 5.0F +6 0 5.5F 0 0 6.0F -6.3 +7 (no 2yr olds) 6.0F 5.5F
  * +6.3 -7 6.0F 0 0 6.5F -6.3 0 7.0F -13 0 6.5F 6.0F +6.3 0 6.5F 0 0 7.0F -7 0
  * 7.0F 7.0F 0 7.5F 7.5F 0 8.0F 8.0F 0 8.3F -4 8.5F -6.6 9.0F -13 8.3F 8.0F +4
  * 8.3F 0 8.5F -3 8.5F 8.0F +6.6 8.3F +3 8.5F 0 9.0F -6.6 9.0F 8.0F +13 8.5F +7
  * 9.0F 0 9.5F -7 10.0F -13 9.5F 8.5F +13 9.0F +7 9.5F 0 for 10F and up, treat
  * it as a 9.5F race.
  *
  * @return the final time adjustment in seconds. Return 999 if not allowed.
  *         NOTE: adjustment to DRF SPEED RATING is performed in
  *         determineRepRace()
  */
 private double distanceAdjustments(String age, int distance, Performance p)
 {
  double adj = 999;
  switch (distance) {
   case F5_0:
    switch (p.ppDistance) {
     case F5_0:
      adj = 0;
      break;
     case F5_5:
      adj = -6.0;
      break;
     case F6_0:
      if (!age.startsWith("A")) // not 2 year olds
       adj = -12.0;
      break;
    }
    break;
   case F5_5:
    switch (p.ppDistance) {
     case F5_0:
      adj = +6.0;
      break;
     case F5_5:
      adj = 0;
      break;
     case F6_0:
      if (!age.startsWith("A")) // not 2 year olds
       adj = -6.3;
      break;
    }
    break;
   case F6_0:
    switch (p.ppDistance) {
     case F5_0:
      adj = +12;
      break;
     case F5_5:
      adj = +6.3;
      break;
     case F6_0:
      adj = 0;
      break;
     case F6_5:
      adj = -6.3;
      break;
     case F7_0:
      adj = -13;
      break;
    }
    break;
   case F6_5:
    switch (p.ppDistance) {
     case F5_5:
      adj = +12;
      break;
     case F6_0:
      adj = +6.3;
      break;
     case F6_5:
      adj = 0;
      break;
     case F7_0:
      adj = -7;
      break;
     case F7_5:
      adj = -13;
      break;
    }
    break;
   case F7_0:
    switch (p.ppDistance) {
     case F6_0:
      adj = +12;
      break;
     case F6_5:
      adj = +6.3;
      break;
     case F7_0:
      adj = 0;
      break;
     case F7_5:
      adj = -6.6;
      break;
    }
    break;
   case F7_5:
    switch (p.ppDistance) {
     case F6_5:
      adj = +12;
      break;
     case F7_0:
      adj = +6.3;
      break;
     case F7_5:
      adj = 0;
      break;
    }
    break;
   case F8_0:
    switch (p.ppDistance) {
     case F8_0:
      adj = 0;
      break;
     case F8_1:
      adj = -2.4;
      break;
     case F8_3:
      adj = -4.2;
      break;
     case F8_5:
      adj = -6.6;
      break;
     case F9_0:
      adj = -13.1;
      break;
    }
    break;
   case F8_1:
    switch (p.ppDistance) {
     case F8_0:
      adj = +2.4;
      break;
     case F8_1:
      adj = 0;
      break;
     case F8_3:
      adj = -1.8;
      break;
     case F8_5:
      adj = -4.2;
      break;
     case F9_0:
      adj = -10.8;
      break;
    }
    break;
   case F8_3:
    switch (p.ppDistance) {
     case F8_0:
      adj = +4.2;
      break;
     case F8_1:
      adj = +1.8;
      break;
     case F8_3:
      adj = 0;
      break;
     case F8_5:
      adj = -2.4;
      break;
     case F9_0:
      adj = -9.2;
      break;
    }
    break;
   case F8_5:
    switch (p.ppDistance) {
     case F8_0:
      adj = +6.6;
      break;
     case F8_1:
      adj = +4.2;
      break;
     case F8_3:
      adj = +2.4;
      break;
     case F8_5:
      adj = 0;
      break;
     case F9_0:
      adj = -6.6;
      break;
     case F9_5:
      adj = -13.0;
      break;
    }
    break;
   case F9_0:
    switch (p.ppDistance) {
     case F8_0:
      adj = +13.0;
      break;
     case F8_1:
      adj = +11.5;
      break;
     case F8_3:
      adj = +9.5;
      break;
     case F8_5:
      adj = +7;
      break;
     case F9_0:
      adj = 0;
      break;
     case F9_5:
      adj = -7;
      break;
     case F10:
      adj = -13.0;
      break;
    }
    break;
   case F9_5:
    switch (p.ppDistance) {
     case F8_5:
      adj = +13.0;
      break;
     case F9_0:
      adj = +7;
      break;
     case F9_5:
      adj = 0;
      break;
     case F10:
      adj = -7;
      break;
    }
    break;
   case F10:
    switch (p.ppDistance) {
     case F9_0:
      adj = +13.0;
      break;
     case F9_5:
      adj = +7;
      break;
     case F10:
      adj = 0;
      break;
    }
    break;
   default:
    if (distance > F10) {
     switch (p.ppDistance) {
      case F9_0:
       adj = +13.0;
       break;
      case F9_5:
       adj = +7;
       break;
      case F10:
       adj = 0;
       break;
      default:
       if (p.ppDistance == distance)
        adj = 0;
     }
    }
    break;
  }
  // if (Log.isDebug(Log.TRACE))
  // Log.print("              Distance Adjustment="+((adj==999)?"DIFFERENT":(adj+""))+", this race="+distance+"yards("+toF(distance)+"),  ppdistance="+p.ppDistance+"yards("+toF(p.ppDistance)+")\n");
  return adj;
 }
 /**
  * Search the race class table (truline.rc) and locate the entry with the same
  * race type and the closest purse value, rounding down.
  *
  * @param raceType
  *         - the type of the race.
  * @param purse
  *         - the purse amount.
  * @return race class. private static double getPurseClass(String raceType, int
  *         purse) { int p2; double purseClass = 0;
  *
  *         //Log.print("rc.size()="+Truline.rc.size()+", raceType="+raceType+
  *         " purse="+purse+"\n"); for(Enumeration c = Truline.rc.elements();
  *         c.hasMoreElements();) { Properties prop =
  *         (Properties)c.nextElement(); if
  *         (prop.getProperty("RACETYPE","").equals(raceType)) { p2 =
  *         Lib.atoi(prop.getProperty("PURSE","0")); purseClass =
  *         Lib.atof(prop.getProperty("CLASS","")); if (p2 >= purse) break; } }
  *         return purseClass; }
  */
 /**
  * QP is for Quirin points. The FIRST objective of speed points is to reflect
  * the alertness of a horse in the starting gate, and his readiness to leave
  * the gate when the bell sounds. If he is regularly to the first call either
  * first, or among the first three, this is a significant advantage, as in
  * many, many races - the race is won by the horse that is out of the gate
  * first - and goes "wire to wire". At many tracks and in certain weather and
  * track surface conditions, the first call is an important aspect of
  * prediction.
  *
  * Next of importance is how far he is from the leader. In a sprint race,
  * because the distance they are traveling is limited, it is hard to make up
  * ground. A horse that is within two lengths of the leader is close enough to
  * have a reasonable chance to make up the deficiency and catch the leader.
  * Conversely, if the horse is third by six lengths, that is a lot of ground to
  * make up in the last half mile.
  *
  * A good Thoroughbred at top speed runs at about forty miles per hour. A horse
  * that is six lengths behind would have go of 45 MPH in order to win. Most
  * horses are not capable of that speed.
  *
  * SO, in SPRINTS, we give him ONE POINT if being first, second or third at the
  * First Call. If he is near the leader (within two lengths), we give him a
  * SECOND POINT for proximity to the lead.
  *
  * The next test of the horse in the sprint is his ability to sustain. Think of
  * me in track and field. Fat and seventy, being alert I might be off the
  * blocks with any other runner. But I could not sustain the pace for long. The
  * vast majority of horses that DO start quickly fade between the first and
  * second Call, and are far back at the finish.
  *
  * So if the horse is first, second, or third at the SECOND CALL we give him
  * another POINT. And if he is within two lengths, he gets another POINT for
  * proximity. So, in the most recent race, depending on how he runs, he can
  * earn 0-1-2-3-4 POINTS. The same applies to his second race back, and also to
  * his third race back. This means a perfect score would be 12 - first AND
  * within two lengths at each Call in each of the three races.
  *
  * Oh, yes, if today's race is a dirt sprint, we use the last three dirt sprint
  * races.
  *
  * In the case of a route race, the distance of the race is long enough to
  * allow the horse to make up ground. We are still interested in the horse
  * being alert out of the starting gate, and would prefer him to be towards the
  * front. But the numbers of lengths is less meaningful.
  *
  * So, we give him a point for being first, second, or third at the first Call,
  * and a second point for being first, second, or third at the second Call - a
  * possible two points for his most recent ROUTE race. We do the same for the
  * second route race back, and also for the third back. This is a potential six
  * points. If the horse is first, second, or third at both Calls in all three
  * races, we give him a point for CONSISTENCY, which means a perfect score is
  * SEVEN.
  *
  * If today's race is a dirt route race, we use the last three dirt routes.
  * Whenever the race is a Turf race, sprint or route, we use only the last
  * three Turf races in the appropriate distance range.
  *
  * If we treat routes in this fashion, we will also be less vulnerable to any
  * variations in BRIS format.
  *
  * @param race
  *         - the race object
  * @param post
  *         - The post object for a horse in this race.
  * @return the number of Quirin points
  */
 private int quirinPoints(Race race, Post post)
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("  Quirin Calculation for Post# " + post.m_postPosition
     + ", Horse: " + post.m_horseName + "\n");
  // Sort the past performances by race date.
  int drfQuirin = post.m_quirin;
  Performance.qSort(post.m_performances);
  int points = 0;
  int position;
  String pos0;
  String pos1;
  String pos2;
  double lengths;
  int cnt = 0;
  int rcount = 0;
  int scount = 0;
  for (int i = 0; i < post.m_performances.size(); i++) {
   Performance p = (Performance) post.m_performances.elementAt(i);
   // If todays race is dirt, look only at dirt. If turf, look only at turf.
   String pSurface = p.m_props.getProperty("PPSURFACE", "").toUpperCase();
   String pAllweather = p.m_props.getProperty("ALLWEATHER", "");
   if (pAllweather.equals("A"))
    pSurface = pAllweather;
   if (!race.m_surface.equals(pSurface)
     || p.isRoute != (race.m_distance >= F8_0))
    continue;
   if (!p.isRoute) {
    // For sprint races
    pos0 = p.m_props.getProperty("POSITION1", ""); // Position at Start Gate
    pos1 = p.m_props.getProperty("POSITION2", ""); // Position at First Call
    pos2 = p.m_props.getProperty("POSITION3", ""); // Position at Second Call
    String lenStr1 = p.m_props.getProperty("LENGTHS1", "999");
    String lenStr2 = p.m_props.getProperty("LENGTHS2", "999");
    if (pos1.equals("1"))
     lenStr1 = "0";
    if (pos2.equals("1"))
     lenStr2 = "0";
    if (Log.isDebug(Log.TRACE)) {
     Log.print("     " + p.ppTrack + " " + Lib.datetoa(p.ppRaceDate)
       + ", Race#" + p.ppRaceNo + ", Call#1: POS=" + pos1 + ", LEN=" + lenStr1
       + " Call#2: POS=" + pos2 + ", LEN=" + lenStr2 + " SPRINT\n");
     Log.print("         (Position at starting gate=" + pos0 + ")\n");
    }
    position = Lib.atoi(pos1);
    lengths = Lib.atof(lenStr1);
    if (position >= 1 && position <= 3) {
     points++; // One of the three front-runners
     scount++;
     if (Log.isDebug(Log.TRACE))
      Log
        .print("        One point for being one of three front-runners at call#1\n");
    }
    if (lengths <= 2.0) {
     scount++;
     points++; // within 2 lengths at this call
     if (Log.isDebug(Log.TRACE))
      Log.print("        One point for within 2 lengths at call#1\n");
    }
    position = Lib.atoi(pos2);
    lengths = Lib.atof(lenStr2);
    if (position >= 1 && position <= 3) {
     points++; // One of the three front-runners
     if (Log.isDebug(Log.TRACE))
      Log
        .print("        One point for being one of three front-runners at call#2\n");
    }
    if (lengths <= 2.0) {
     scount++;
     points++; // within 2 lengths at this call
     if (Log.isDebug(Log.TRACE))
      Log.print("        One point for within 2 lengths at call#2\n");
    }
   } else { // for Route races
    String lenStr1, lenStr2;
    lenStr1 = p.m_props.getProperty("LENGTHS1", "999");
    lenStr2 = p.m_props.getProperty("LENGTHS2", "999");
    pos0 = p.m_props.getProperty("POSITION1", ""); // Position at start gate
    pos1 = p.m_props.getProperty("POSITION2", ""); // Position at First Call
    pos2 = p.m_props.getProperty("POSITION3", ""); // Position at Second Call
    if (pos1.equals("1"))
     lenStr1 = "0";
    if (pos2.equals("1"))
     lenStr2 = "0";
    if (Log.isDebug(Log.TRACE)) {
     Log.print("     " + p.ppTrack + " " + Lib.datetoa(p.ppRaceDate)
       + ", Race#" + p.ppRaceNo + ", Call#1: POS=" + pos1 + ", LEN=" + lenStr1
       + " Call#2: POS=" + pos2 + ", LEN=" + lenStr2 + " ROUTE\n");
     Log.print("         (Position at starting gate=" + pos0 + ")\n");
    }
    // First Call
    position = Lib.atoi(pos1);
    if (position >= 1 && position <= 3) {
     rcount++; // leader at this call
     points++;
     if (Log.isDebug(Log.TRACE))
      Log
        .print("        One point for being one of three front-runners at call#1\n");
    }
    // Second Call
    position = Lib.atoi(pos2);
    lengths = Lib.atof(lenStr2);
    if (position >= 1 && position <= 3) {
     rcount++; // leader at this call
     points++;
     if (Log.isDebug(Log.TRACE))
      Log
        .print("        One point for being one of three front-runners at call#2\n");
    }
   }
   cnt++;
   if (cnt >= 3) // only look at the first three races of the same type
    break;
  }
  if (rcount == 6) {
   points++; // was front runner at both calls in all three races (ROUTE only)
   if (Log.isDebug(Log.TRACE))
    Log
      .print("        A bonus point for being 1,2,or 3 at both calls in all races\n");
  }
  // if (scount == 6)
  // points += 6; // was leader at both calls in all three races (Sprint only)
  if (Log.isDebug(Log.TRACE))
   Log.print("     Total Quirin Points=" + points + "/" + drfQuirin + "\n");
  return points;
 }
 /**
  * Compute the EQ (Energy Quotient)
  *
  * Races - if he just runs around the track - last or middle of the pack - he
  * gets credit for the number of furlongs in the race - which is usually
  * 6-6.5-7-8-8.5-9-10. In such a race, there is no sign that he was extended -
  * reacting to the jockey's whip and urgings to run fast.
  *
  * BUT, if he runs in front for most of the race, even though he may not win,
  * he has been extended - he has made a muscle-building effort. Similarly, if
  * he started in the rear, and made up ground and passed other horses - even if
  * he missed the win, he was "all out" and this should bring improvement next
  * time.
  *
  * In both of these scenarios, we give him a 60% Bonus. We are going to look at
  * each of his last three efforts. We subtract the date of the third effort
  * back from today's date. That becomes the denominator. The numerator is the
  * number of lengths credit he is given. Below is "regular race" vs "extended
  * effort" credit that we give as the distance (which becomes the numerator).
  *
  * REGULAR EXTENDED
  *
  * 5.0 8.0 5.5 8.8 6.0 9.6 6.5 10.4 7.0 11.2 7.5 12.0 8.0 12.8 8.5 13.6 9.0
  * 14-4 10.0 16.0
  *
  * WORK-OUTS - For most work-outs the horse is credited with the length of
  * distance worked. The exception is when it is the fastest work-out of the day
  * for that distance/ In this case, we give a 40% Bonus. Such as:
  *
  * REGULAR EXTENDED
  *
  * 2.0 2.8 3.0 4.2 4.0 5.6 5.0 7.0 6.0 8.4 7.0 9.8 8.0 11.2
  *
  *
  * Now let me hypothesize. Today's race date is 8/14. The horse was second in a
  * six furlong race on 7/10. He raced mid-pack for 8.5 furlongs on 8/1. On 8/10
  * he worked five furlongs. There is no "bullet" or "cannonball" to indicate
  * the best work-out of the dat.
  *
  * >From 7/10 to 8/14 is 35 days. He gets 9.6 lengths credit for a competitive
  * six furlongs on 7/10. For 8/1, he loafed along, and gets just the distance
  * of the race, 8.5 furlongs. Likewise he gets five lengths credit for a
  * routine work-out on 8/10. These three efforts total 23.1, which we divide by
  * 35. This gives him a .66 for the first leg of three measurements.
  *
  * Now we look at the three efforts in relationship excluding today's date.
  * (Did he have so much exertion that he needed a rest?) So we subtract 7/10
  * from 8/10, 31 days, and again divide into the 23.1 lengths credit. For this
  * he gets .745.
  *
  * Now we look at recency. Subtracting the date of the last effort from today's
  * date, we get 4 days. This is divided into the 5.0 furlongs worked, which
  * gives him a 1.25. Adding the three values, his ENERGY NUMBER is 2.655, which
  * we round up to 2.7.
  *
  * Now let's look at another horse. On 7/1, he closed ten lenths to be second
  * by two at 8.5 furlongs (bonus). On 7/15 he wins at 6f (bonus). On 8/5, he
  * leads to the head of the stretch, and finishes third by a length (bonus).
  *
  * He receives 13.6 plus 9.6 plus 13.6 lengths credit, a total of 36.8. Divided
  * by 44 days, he receives .836. Taking the efforts in themselves, we divide 35
  * days into 36.8 and get 1.05. Then, for recency, we divide nine days into
  * 13.6 lengths and get 1.51. The three values total toan ENERGY NUMBER of
  * 3.396, which we round up to 3.4.
  *
  *
  * For each horse in a race: { Select EQ Races/workouts =============== for the
  * last 3 races or workouts { If this was a real race if within first 3
  * positions or within 2 lengths of leader at finish multiply distance by 1.6
  * If this is a workout set extra flag to workout type (not sure what this
  * does) if the extra flag is set, multiply distance by 1.4 }
  *
  * Compute EQ =========== EqKey1 = average distance per day over the period of
  * the workouts. EqKey2 = average distance per day from first workout to race
  * day. EqKey3 = average distance per day from last workout to race day.
  * EnergyKeyTotal = EqKey1 + EqKey2 + EqKey3; Energy Ranking is based on this
  * number. }
  *
  */
 private double computeEQ(Race race, Post post)
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("  Energy Calculation for Post# " + post.m_postPosition
     + ", Horse: " + post.m_horseName + "\n");
  // sort the workouts by work date, (most recent first).
  // Assume unused m_work elements contain null dates.
  int len;
  // Set this flag if 5 furlong bullet work in last 14 days
  post.m_5furlongBullet = "";

  for (len = 0; len < post.m_work.length; len++) {
   if (post.m_work[len] == null || post.m_work[len].m_workDate == null)
    break;
  }
  // for (int i = 0; i < 12; i++)
  // {
  // if (post.m_work[i] != null && post.m_work[i].m_workDate != null)
  // Log.print("          presort: "+post.m_work[i].m_props+"\n");
  // }
  for (int i = 0; i < len; i++) {
   for (int j = i + 1; j < len; j++) {
    // Compare keys
    Workout a = post.m_work[i];
    Workout b = post.m_work[j];
    if (a.m_workDate.before(b.m_workDate)) {
     // Swap elements
     post.m_work[i] = b;
     post.m_work[j] = a;
    }
   }
  }
  
  if (len >= 2)
   post.m_work3Short = "Y";
  else
   post.m_work3Short = "N";
   
  if (len >= 3)
   post.m_work4Week = "Y";
  else
   post.m_work4Week = "N";
   
  post.m_daysSinceWork1 = 0;
  post.m_daysSinceWork2 = 0;
  post.m_daysSinceWork3 = 0;
  int work3Last = 0;
  int work4Last = 0;
  
  for (int i = 0; i < len; i++) {
   Properties props = post.m_work[i].m_props;
   double d = Lib.atof(props.getProperty("WORKDISTANCE"));
   boolean bullet = false;
   String rank = props.getProperty("WORKRANK", "");
   double workQty = Lib.atof(props.getProperty("WORKQTY"));
   int daysB = Lib.dateDiff(post.m_work[i].m_workDate, race.m_raceDate); // workout to race
   
   if (i == 0)
    post.m_daysSinceWork1 = daysB;
   else if (i == 1)
    post.m_daysSinceWork2 = daysB;
   else if (i == 2)
    post.m_daysSinceWork3 = daysB;
   
   if (daysB <= 14 && d == 1100 && rank.equals("1") && workQty > 4)
    post.m_5furlongBullet = "@";

   if (i == 0 && daysB > 12)
    post.m_work3Short = "N";
   else {
    if (i == 0)
     work3Last = daysB;
    else if (i == 2 && daysB > work3Last+12)
     post.m_work3Short = "N";
   }

   if (i == 0 && daysB > 12)
    post.m_work4Week = "N";
   else {
    if (i == 0)
     work4Last = daysB;
    else if (i < 4 && daysB > work4Last+8)
     post.m_work4Week = "N";
    else
     work4Last = daysB; 
   }    
   
   if (Log.isDebug(Log.TRACE)) {
    Log.print("              Workdate: "
      + Lib.datetoa(post.m_work[i].m_workDate) + ", distance=" + toF(d)
      + " work rank=" + rank + "Work3=" + post.m_work3Short + "Work4=" + post.m_work4Week + "\n");
   }
  }
  
  if (post.m_work4Week.equals("Y") && post.m_work3Short.equals("Y"))
   post.m_work3Short = "N";

  // We assume performances are already sorted by date (most recent first).
  // Select EQ Races/workouts from the last 3 races or workouts
  //
  double distance = 0; // total distance
  double lastd = 0; // most recent distance
  Date first = null; // First workout or race (oldest)
  Date last = null; // Last workout or race (one just before race)
  int raceIdx = 0; // race index
  int workoutIdx = 0; // workout index
  for (int i = 0; i < 3; i++) {
   Performance p = null;
   if (raceIdx < post.m_performances.size())
    p = (Performance) post.m_performances.elementAt(raceIdx);
   if (workoutIdx < len
     && (raceIdx >= post.m_performances.size() || post.m_work[workoutIdx].m_workDate
       .after(p.ppRaceDate))) {
    // Use the workout.
    if (i == 0)
     last = post.m_work[workoutIdx].m_workDate;
    first = post.m_work[workoutIdx].m_workDate;
    Properties props = post.m_work[workoutIdx].m_props;
    boolean flg = false;
    if (props.getProperty("WORKRANK", "").equals("1"))
     flg = true; // it was a bullet race.
    double d = Lib.atof(props.getProperty("WORKDISTANCE"));
    if (Log.isDebug(Log.TRACE)) {
     Log.print("          EQ" + (i + 1) + " workout " + Lib.datetoa(first)
       + ", distance=" + toF(d) + ", work rank="
       + props.getProperty("WORKRANK", "") + ((flg) ? ", (bullet)" : " ")
       + "\n");
     Log.print("             DATA: " + props + "\n");
    }
    distance += ((flg) ? (d * 1.4) : d);
    if (i == 0)
     lastd = ((flg) ? (d * 1.4) : d);
    workoutIdx++;
   } else if (raceIdx < post.m_performances.size()) {
    // Use Race.
    if (i == 0)
     last = p.ppRaceDate;
    first = p.ppRaceDate;
    boolean flg = false;
    String lenStr = p.m_props.getProperty("LENGTHS4", "999"); // lengths at
                                                              // finish
    int pos = Lib.atoi(p.m_props.getProperty("POSITION6", "")); // Position at
                                                                // Finish
    if (Lib.atof(lenStr) <= 2.0 || (pos >= 1 && pos <= 3))
     flg = true;
    // NOTE: if a horse does not finish, its position will be 0, length=999
    if (Log.isDebug(Log.TRACE)) {
     String comment = p.m_props.getProperty("COMMENT", "");
     Log.print("          EQ" + (i + 1) + " Race " + Lib.datetoa(p.ppRaceDate)
       + ", distance=" + toF(p.ppDistance) + ", lengths="
       + ((lenStr.equals("999")) ? "0" : lenStr) + ", pos=" + pos
       + ((flg) ? ", (Extra Flag) \"" : " \"") + comment + "\"\n");
    }
    distance += ((flg) ? (p.ppDistance * 1.6) : p.ppDistance);
    if (i == 0)
     lastd = ((flg) ? (p.ppDistance * 1.6) : p.ppDistance);
    raceIdx++;
   } else
    break; // no more data.
  }
  double energy = 0;
  if (first != null && last != null) {
   int days1 = Lib.dateDiff(first, last); // days in period of workouts.
   int days2 = Lib.dateDiff(first, race.m_raceDate); // first workout to race
                                                     // date
   int days3 = Lib.dateDiff(last, race.m_raceDate); // last workout to race
                                                    // date.
   double eqkey1 = 0;
   double eqkey2 = 0;
   double eqkey3 = 0;
   if (days1 > 0)
    eqkey1 = distance / (double) days1 / (double) YdPerF;
   if (days2 > 0)
    eqkey2 = distance / (double) days2 / (double) YdPerF;
   if (days3 > 0)
    eqkey3 = lastd / (double) days3 / (double) YdPerF;
   energy = eqkey1 + eqkey2 + eqkey3;
   extraFlg = (energy > 4.6);
   if (Log.isDebug(Log.TRACE)) {
    Log.print("        Resulting distance credit=" + toF(distance) + "\n");
    Log.print("        first workout/race to last workout/race (" + days1
      + " days) distance/day=" + Lib.ftoa(eqkey1, 3) + "\n");
    Log.print("        first workout to race day (" + days2
      + " days) distance/day=" + Lib.ftoa(eqkey2, 3) + "\n");
    Log.print("        last workout to race day (" + days3
      + " days) distance/day=" + Lib.ftoa(eqkey3, 3) + "\n");
    Log.print("        Total energy figure: " + Lib.ftoa(energy, 3) + "\n");
    if (extraFlg)
     Log.print("        This horse gets an (*).\n");
   }
  } else if (Log.isDebug(Log.TRACE))
   Log.print("        Nothing to base energy figure on.\n");
  return energy;
 }
 /********************************************
  * Earnings Computations (EPS) ===================== For each horse in a race:
  * { Earnings Ratio = Total Earnings / Starts; Use the Horse's Lifetime Record:
  * }
  *
  */
 private double computeEPS(Race race, Post post)
 {
  double ratio;
  if (Log.isDebug(Log.TRACE))
   Log.print("  Earnings Ratio for Post# " + post.m_postPosition + ", Horse: "
     + post.m_horseName + "\n");
  int starts;
  double earnings;
  if (race.m_surface.equals("T")) {
   // use only lifetime Turf stats
   starts = Lib.atoi(post.m_props.getProperty("LRTURFSTARTS"));
   earnings = Lib.atof(post.m_props.getProperty("LRTURFEARNINGS"));
  } else if (race.m_surface.equals("A")) {
    // use only lifetime All Weather stats
    starts = Lib.atoi(post.m_props.getProperty("LRAWESTARTS"));
    earnings = Lib.atof(post.m_props.getProperty("LRAWEEARNINGS"));
  } else if (race.m_trackCond.equals("Off")) {
   starts = Lib.atoi(post.m_props.getProperty("LRWETSTARTS"));
   earnings = Lib.atof(post.m_props.getProperty("LRWETEARNINGS"));
  } else {   // use all stats
   starts = Lib.atoi(post.m_props.getProperty("LIFETIMESTARTS"));
   earnings = Lib.atof(post.m_props.getProperty("LIFETIMEEARNINGS"));
  }
  if (starts == 0)
   ratio = 0;
  else
   ratio = earnings / starts;
  if (Log.isDebug(Log.TRACE)) {
   NumberFormat nf = NumberFormat.getCurrencyInstance();
   Log.print("        earnings=" + nf.format(earnings) + "  starts=" + starts
     + "  EPS=" + Lib.ftoa(ratio, 2)
     + ((race.m_surface.equals("T")) ? " (Turf only)" : "")  
     + ((race.m_surface.equals("A")) ? " (All Weather only)" : "") + "\n");
  }
  return ratio;
 }
 /*************************************************************
  * Rank Horses by EPS, EN, FS, TT, SS, CS, FT, AS, RE, QP, PP.
  *
  * I think we are about at the point of settting up the computations. The
  * highest EPS # should be #1 ranked. Each next highest number gets the next
  * ranking. EPS would ultimately be a potential 1-16. If a horse has no EPS, it
  * is tied for last.
  *
  * EN also goes from highest number to lowest number in the ranking. Any horse
  * with an EN over 4.6 should have an asterisk (*).
  *
  * FS, TT, SS, FT, and CS - the fastest fraction is #1 ranked, next fastest #2.
  *
  * AS, RE, QP and PP - the highest number is #1, the next highest #2, etc.
  */
 private static void rankHorses(Race race)
 {
  // Handicap rankings
  if (Log.isDebug(Log.TRACE))
   Log.print("  Rank Horses in race #" + race.m_raceNo + "\n");
  Vector[] ranking = new Vector[names.length];
  for (int i = 0; i < ranking.length; i++)
   ranking[i] = new Vector(); // Vectors contain Post objects for the horse.
  // Rank each horse in the race.
  race.m_cnthorses = 0;
  race.m_cntnrl = 0;
  race.m_cnt1st = 0;
  race.m_cntnrlML = 0;
  race.m_cnttrnown = 0;
  Boolean entryA = false;
  Boolean entryB = false;
  Boolean entryC = false;
  for (Enumeration e = race.m_posts.elements(); e.hasMoreElements();) {
   Post post = (Post) e.nextElement();
   String scratch = post.m_props.getProperty("ENTRY", "");
   if (post.m_horseName == null || scratch.equals("S"))
    continue; // position is empty or scratched
   if (scratch.equals("A")) {
    if (entryA == false) {
     entryA = true;
     race.m_cnthorses++;
    }
   }
   else if (scratch.equals("B")) {
    if (entryB == false) {
     entryB = true;
     race.m_cnthorses++;
    }
   }
   else if (scratch.equals("C")) {
    if (entryC == false) {
     entryC = true;
     race.m_cnthorses++;
    }
   }
   else
    race.m_cnthorses++;

   Boolean noFS = false;
   for (int param = 0; param < names.length; param++) {
    int i;
    boolean found = false;
    if (param == FS & post.m_handicap.value[param] == 0) {
     race.m_cntnrl++;
     noFS = true;
     if (!post.m_morningLine.equals("")) {
      switch (post.m_morningLine.substring(0, 2)) {
       case "1-":
       case "2-":
       case "3-":
       case "4-":
        // case "5-":
        race.m_cntnrlML++;
      }
      switch (post.m_morningLine) {
       case "6-5":
       case "7-5":
       case "8-5":
       case "9-5":
       case "7-2":
        // case "9-2":
        race.m_cntnrlML++;
      }
     }
    }
    if (param == EPS & post.m_daysSinceLast == -1) {
     race.m_cnt1st++;
    }
    if (post.m_handicap.value[param] > 0) {
     if (param < AS | param == ML | param == FO) {
      // Running line and Morning Line and Final Odds - smallest at top (but 0 sorts to bottom)
      for (i = 0; i < ranking[param].size(); i++) {
       Post p = (Post) ranking[param].elementAt(i);
       if (p.m_handicap.value[param] <= 0
         || post.m_handicap.value[param] < p.m_handicap.value[param]) {
        ranking[param].insertElementAt(post, i);
        found = true;
        break;
       }
      }
     } else {
      // Other parameters - largest at top
      for (i = 0; i < ranking[param].size(); i++) {
       Post p = (Post) ranking[param].elementAt(i);
       if (post.m_handicap.value[param] > p.m_handicap.value[param]) {
        ranking[param].insertElementAt(post, i);
        found = true;
        break;
       }
      }
     }
    }
    if (!found && param != TTCS)
     ranking[param].addElement(post);
   }
  }
  
  // ////////////// ASSIGN RANKINGS ///////////////////////////////
  if (race.m_cnthorses > 0) {
   for (int param = 0; param < names.length; param++) {
    if (Log.isDebug(Log.TRACE))
     Log.print("\n  Rankings for " + names[param] + "\n");
    if (param == TTCS)
     computeTTCSRanking(race,ranking[param]);
    Post top = (Post) ranking[param].elementAt(0);
    int lastrank = 1;
    double lastvalue = top.m_handicap.value[param];
    for (int i = 0; i < ranking[param].size(); i++) {
     Post p = (Post) ranking[param].elementAt(i);
     if (p.m_handicap.value[param] == 0)
      p.m_handicap.rank[param] = ranking[param].size();
     else if (p.m_handicap.value[param] == lastvalue)
      p.m_handicap.rank[param] = lastrank; // look for top or ties.
     else
      p.m_handicap.rank[param] = i + 1;
     if (param == TT)
      p.m_handicap.value[TTCS] = p.m_handicap.rank[param];
     if (param == CS)
      p.m_handicap.value[TTCS] = p.m_handicap.value[TTCS] + p.m_handicap.rank[param];
     if (Log.isDebug(Log.TRACE))
      Log.print("    #" + p.m_handicap.rank[param] + " " + names[param] + "="
        + Lib.ftoa(p.m_handicap.value[param], 2) + " Post# " + p.m_postPosition
        + ", Horse: " + p.m_horseName + "\n");
     lastrank = p.m_handicap.rank[param];
     lastvalue = p.m_handicap.value[param];
    }
   }
  }
 }
 private static void computeTTCSRanking(Race race, Vector ranking)
 {
  for (Enumeration e = race.m_posts.elements(); e.hasMoreElements();) {
   Post post = (Post) e.nextElement();
   String scratch = post.m_props.getProperty("ENTRY", "");
   if (post.m_horseName == null || scratch.equals("S"))
    continue; // position is empty or scratched
   int param = TTCS;
   int i;
   boolean found = false;
   if (post.m_handicap.value[param] > 0) {
     // TTCS - smallest at top (but 0 sorts to bottom)
     for (i = 0; i < ranking.size(); i++) {
      Post p = (Post) ranking.elementAt(i);
      if (p.m_handicap.value[param] <= 0
        || post.m_handicap.value[param] < p.m_handicap.value[param]) {
       ranking.insertElementAt(post, i);
       found = true;
       break;
      }
     }
   }
   if (!found)
    ranking.addElement(post);
  }
  
 }
 /***************************
  * Assign Points: Once the rankings are given, we need to assign a point value
  * for #1-2-3-4-etc. in each column. This is subject to negotiation. Right now,
  * #1 EPS is given 8 points, #2 - 4 points, #3 1 point, all the others = 0
  * points. #1 EN gets 4 points, #2 - 2 points, #3 - 1 points, all others are 0.
  * If the number is over 4.6, give another 4 points.
  *
  * For FS, TT, SS, CS, FT, AS, and QP - TRULINE gives 4 points for #1, 2 points
  * for #2, 1 point for #3 and 0 to all others. RE gives 8 points to #1, 4
  * points for #2, 1 point for #3, and 0 for the others. Querying may point us
  * to very different impact values. Even common sense and discussion will
  * probably suggest some changes.
  *
  * @param race
  *         - The race being handicapped.
  * @return list of Posts in order by bonus point rankings.
  */
 private static void assignPoints(Race race)
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("\n  Assign Points in race #" + race.m_raceNo + "\n");

  // Load handicap factor points for track, surface and distance
  Properties prop = null;
  String race_surface = race.m_surface;
  String race_distance = "";
  if (race.m_distance > 1759)
   race_distance = "RT";
  else
   race_distance = "SP";
  int cnt = 0;
  for (Enumeration c = Truline.hf.elements(); c.hasMoreElements();) {
   prop = (Properties) c.nextElement();
   String track = prop.getProperty("TRACK");
   String trackCond = prop.getProperty("TRACKCOND");
   String surface = prop.getProperty("SURFACE");
   String distance = prop.getProperty("DISTANCE");
   if (track.substring(2).equals("X"))
    track = track.substring(0, 2);
   if (track.equals(race.m_track) && trackCond.equals(race.m_trackCond) && surface.equals(race_surface) && distance.equals(race_distance)) {
    cnt++;
    break;
   }
  }

  int EPS1, EN1, FS1, TT1, SS1, CS1, FT1, AS1, RE1, QP1, wPts = 0;
  if (cnt == 0) {
    Log.print("   No track-specific handicap values for track, surface and distance "
        + race.m_track + "/" + race_surface + "/" + race_distance + "\n");
    EPS1 = 8;
    EN1 = 4;
    FS1 = 4;
    TT1 = 4;
    SS1 = 4;
    CS1 = 4;
    FT1 = 4;
    AS1 = 8;
    RE1 = 0;
    QP1 = 8;
  } else {
   EPS1 = Lib.atoi(prop.getProperty("EPS"));
   EN1 = Lib.atoi(prop.getProperty("EN"));
   FS1 = Lib.atoi(prop.getProperty("FS"));
   TT1 = Lib.atoi(prop.getProperty("TT"));
   SS1 = Lib.atoi(prop.getProperty("SS"));
   CS1 = Lib.atoi(prop.getProperty("CS"));
   FT1 = Lib.atoi(prop.getProperty("FT"));
   AS1 = Lib.atoi(prop.getProperty("AS"));
   RE1 = Lib.atoi(prop.getProperty("RE"));
   QP1 = Lib.atoi(prop.getProperty("QP"));
  }

  // Check each horse in the race.
  for (Enumeration e = race.m_posts.elements(); e.hasMoreElements();) {
   Post post = (Post) e.nextElement();
   String scratch = post.m_props.getProperty("ENTRY", "");
   if (post.m_horseName == null || scratch.equals("S"))
    continue; // position is empty or scratched

   // Check for power trainer
   // if (Truline.userProps.getProperty("Experimental", "N").equals("Yes")) {
    int ptPoints = identifyPowerTrainers(race, post);
    if (ptPoints > 0) {
     post.m_handicap.points += ptPoints;
     if (Log.isDebug(Log.TRACE))
      Log.print("     Post# " + post.m_postPosition + ", Horse: "
        + post.m_horseName + " " + ptPoints
        + " points for power trainer\n");
    }

    // Set Track-Specific Trainer & Jockey Percentages / Trainer-Jockey Pairs / Trainer-Owner Pairs
    if (Truline.userProps.getProperty("TL2014", "No").equals("Yes")) {
     String trnJkyPct = setTrainerJockeyPercents(race, post);
     identifyTrainerJockeys(race, post);
     if (Truline.userProps.getProperty("Experimental", "No").equals("Yes")) {
      identifyTrainerOwners(race, post);
      identifyTrainerSurfaceStat(race, post);
      identifyTrainerMeetStat(race, post);
      identifyJockeyMeetStat(race, post);
      identifyEquibaseTrainerStats(race, post);
     }
     post.m_trnJkyPct = trnJkyPct;
    }

  // }
  // else {
  //  Properties trainerData = Truline.pt.get("NAME", post.m_trainerName);
  //  post.m_trainerNamePT = "  ";
  //  Log.print("     Post# " + post.m_postPosition + ", Horse: "
  //    + post.m_horseName + " for trainer " + post.m_trainerName + " "
  //    + trainerData + "\n");
  //  if (trainerData != null) {
  //   String trainerPointsStr = trainerData.getProperty("POINTS");
  //   if (trainerPointsStr != null) {
  //    post.m_handicap.points += Lib.atoi(trainerPointsStr);
  //    post.m_trainerNamePT = " #";
  //    if (Log.isDebug(Log.TRACE))
  //     Log.print("     Post# " + post.m_postPosition + ", Horse: "
  //       + post.m_horseName + " " + trainerPointsStr
  //       + " points for power trainer\n");
  //   }
  //  }
  // }

   for (int i = 0; i < names.length; i++) {
    if (i == EPS) {
     if (post.m_handicap.rank[i] == 1) {
      post.m_handicap.points += EPS1;
      if (Log.isDebug(Log.TRACE))
       Log.print("     Post# " + post.m_postPosition + ", Horse: "
         + post.m_horseName + EPS1 + " points for #1 " + names[i] + "\n");
      if (post.m_handicap.value[FT] == 0) {
       post.m_handicap.points += 6;
       Properties trainerData = Truline.pt.get("NAME", post.m_trainerName);
       if (trainerData != null)
        post.m_handicap.points += 6;
       Log.print("     Post# " + post.m_postPosition + ", Horse: "
         + post.m_horseName + " 6 points for EPS1 and no running line" + "\n");
      }
     } else if (post.m_handicap.rank[i] == 2) {
      post.m_handicap.points += Math.round(EPS1 / 2);
      if (Log.isDebug(Log.TRACE))
       Log.print("     Post# " + post.m_postPosition + ", Horse: "
         + post.m_horseName + Math.round(EPS1 / 2) + " points for #2 " + names[i] + "\n");
      if (post.m_handicap.value[FT] == 0) {
       post.m_handicap.points += 6;
       Properties trainerData = Truline.pt.get("NAME", post.m_trainerName);
       if (trainerData != null)
        post.m_handicap.points += 6;
       Log.print("     Post# " + post.m_postPosition + ", Horse: "
         + post.m_horseName + " 6 points for EPS2 and no running line" + "\n");
      }
     } else if (post.m_handicap.rank[i] == 3) {
      post.m_handicap.points += Math.round(EPS1 / 4);
      if (Log.isDebug(Log.TRACE))
       Log.print("     Post# " + post.m_postPosition + ", Horse: "
         + post.m_horseName + Math.round(EPS1 / 4) + " points for #3 " + names[i] + "\n");
      if (post.m_handicap.value[FT] == 0) {
       post.m_handicap.points += 6;
       Properties trainerData = Truline.pt.get("NAME", post.m_trainerName);
       if (trainerData != null)
        post.m_handicap.points += 6;
       Log.print("     Post# " + post.m_postPosition + ", Horse: "
         + post.m_horseName + " 6 points for EPS3 and no running line" + "\n");
      }
     }
    } else {
     if (race.m_ignoreRunLine.equals("Y") && i < QP)
      continue;
     if (post.m_handicap.rank[i] == 1) {
      switch (i) {
       case EN:
        wPts = EN1;
        break;
       case FS:
        wPts = FS1;
        break;
       case TT:
        wPts = TT1;
        break;
       case SS:
        wPts = SS1;
        break;
       case CS:
        wPts = CS1;
        break;
       case FT:
        wPts = FT1;
        break;
       case AS:
        wPts = AS1;
        break;
       case RE:
        wPts = RE1;
        break;
       case QP:
        wPts = QP1;
        break;
      }
      post.m_handicap.points += wPts;
      if (Log.isDebug(Log.TRACE))
       Log.print("     Post# " + post.m_postPosition + ", Horse: "
         + post.m_horseName + wPts + " for #1 " + names[i] + "\n");
     } else if (post.m_handicap.rank[i] == 2) {
      post.m_handicap.points += Math.round(wPts / 2);
      if (Log.isDebug(Log.TRACE))
       Log.print("     Post# " + post.m_postPosition + ", Horse: "
         + post.m_horseName + Math.round(wPts / 2) + " points for #2 " + names[i] + "\n");
     } else if (post.m_handicap.rank[i] == 3) {
      post.m_handicap.points += Math.round(wPts / 4);
      if (Log.isDebug(Log.TRACE))
       Log.print("     Post# " + post.m_postPosition + ", Horse: "
         + post.m_horseName + Math.round(wPts / 2) + " points for #3 " + names[i] + "\n");
     }
    }
   }
   // Assign 4 points for must-bet EN
   if (post.m_handicap.extraFlg) {
    post.m_handicap.points += 4;
    if (Log.isDebug(Log.TRACE))
     Log.print("     Post# " + post.m_postPosition + ", Horse: "
       + post.m_horseName + " 4 points for energy over 4.6\n");
   }

   // Check for Owner / Trainer connection
   if (post.m_ownerTrn.equals("")) {
    post.m_ownerTrn = " ";
    String trainer1 = post.m_props.getProperty("TRAINER", "$").toUpperCase();
    int trainerI = trainer1.indexOf(" ");
    String trainerLast = trainer1;
    if (trainerI > 1)
     trainerLast = trainer1.substring(0, trainerI);
    String owner1 = post.m_props.getProperty("OWNER", "$").toUpperCase();
    if (trainer1.equals("$") || (owner1.equals("$")))
     post.m_ownerTrn = " ";
    else if (owner1.indexOf(trainerLast) != -1) {
     post.m_ownerTrn = "*";
     race.m_cnttrnown++;
    }
   }

   // Check for Owner / Breeder connection
   post.m_ownerBrd = " ";
   String owner = post.m_props.getProperty("OWNER", "$").toUpperCase();
   String breeder = post.m_horse.m_props.getProperty("BREEDER", "$").toUpperCase();
   if (!owner.equals("$") && !breeder.equals("$")) {
    String delims = "[ ]+";
    String[] ownerWords = owner.split(delims);
    if ((ownerWords.length >= 1 && breeder.indexOf(ownerWords[0]) != -1)
      || (ownerWords.length > 1 && ownerWords[ownerWords.length-1].length() > 3 && breeder.indexOf(ownerWords[ownerWords.length-1]) != -1)) {
     post.m_ownerBrd = "+";
     race.m_cntbrdown++;
    }
   }

   // Check for turf sire when surface is turf
   post.m_sireTS = " ";
   post.m_sireTSp = " ";
   post.m_sireTS2 = " ";
   post.m_sireTSPI = "";
   post.m_sireTSPI2 = "";
   if (race.m_surface.equals("T")) {
    // Check if sire is in top turf sires and add points
    post.m_sireTS2 = "-";
    Properties sireData = Truline.ts.get("NAME", post.m_sireName);
    Log.print("     Post# " + post.m_postPosition + ", Horse: "
      + post.m_horseName + " for sire " + post.m_sireName + " " + sireData
      + "\n");
    if (sireData != null) {
     String sirePointsStr = sireData.getProperty("POINTS");
     if (sirePointsStr != null) {
      int sirePoints = Math.round(Lib.atoi(sirePointsStr));
      // Sire as sire gets sire-level points
      switch (sirePoints) {
       case 3:
        sirePoints = 3;
        break;
       case 6:
        sirePoints = 6;
        break;
       case 9:
        sirePoints = 9;
        break;
      }
      String sireAWDStr = sireData.getProperty("AWD");
      switch (sireAWDStr) {
       case "6":
        sireAWDStr = "-SP ";
        break;
       case "7":
        sireAWDStr = "-AVG";
        break;
       case "9":
        sireAWDStr = "-RT ";
        break;
      }
      // Add in sire points only when option is "Yes"
      if (Truline.userProps.getProperty("SirePoints", "Y").equals("Y")) {
       post.m_handicap.points += sirePoints;
      }
      post.m_sireTS = "$";
      post.m_sireTSp = Lib.ftoa(sirePoints,0);
      if (sirePoints == 9 || sireAWDStr.equals("-RT"))
       post.m_sireTSPI = "\\b Sire-"+post.m_sireName+"-"+sirePoints+sireAWDStr+" \\b0  ";
      else
       post.m_sireTSPI = "Sire-"+post.m_sireName+"-"+sirePoints+sireAWDStr+"  ";
      if (Log.isDebug(Log.TRACE))
       Log.print("     Post# " + post.m_postPosition + ", Horse: "
         + post.m_horseName + " " + sirePoints + " points for turf sire\n");
     }
    }
    // Check if dam sire is in top turf sires and add points
    Properties damSireData = Truline.ts.get("NAME", post.m_damSireName);
    Log.print("     Post# " + post.m_postPosition + ", Horse: "
      + post.m_horseName + " for dam sire " + post.m_damSireName + " "
      + damSireData + "\n");
    if (damSireData != null) {
     String sirePointsStr = damSireData.getProperty("POINTS");
     if (sirePointsStr != null) {
      int sirePoints = Math.round(Lib.atoi(sirePointsStr));
      if (post.m_sireTS != "$") {
       switch (sirePoints) {
        case 3:
         sirePoints = 0;
         break;
        case 6:
         sirePoints = 3;
         break;
        case 9:
         sirePoints = 6;
         break;
       }
      }
      // Add in sire points only when option is "Yes"
      if (Truline.userProps.getProperty("SirePoints", "Y").equals("Y")) {
       post.m_handicap.points += sirePoints;
      }
      String sireAWDStr = damSireData.getProperty("AWD");
      switch (sireAWDStr) {
       case "6":
        sireAWDStr = "-SP ";
        break;
       case "7":
        sireAWDStr = "-AVG";
        break;
       case "9":
        sireAWDStr = "-RT ";
        break;
      }
      post.m_sireTS2 = "d";
      if (sirePoints > 5)
       post.m_sireTS2 = "D";
      if (sirePoints == 9)
       post.m_sireTSPI = post.m_sireTSPI+"\\b Dam Sire-"+post.m_damSireName+"-"+sirePoints+sireAWDStr+" \\b0  ";
      else
       post.m_sireTSPI = post.m_sireTSPI+"Dam Sire-"+post.m_damSireName+"-"+sirePoints+sireAWDStr;
      if (post.m_sireTS.equals("$"))
       race.m_cnt$d++;
      if (Log.isDebug(Log.TRACE))
       Log.print("     Post# " + post.m_postPosition + ", Horse: "
         + post.m_horseName + " " + sirePoints
         + " points for dam's turf sire\n");
     }
    }
    
    /******
     * // Check if sire's sire is in top turf sires and add 1/2 points
     * Properties sireSireData = Truline.ts.get("NAME", post.m_sireSireName);
     * Log.print("     Post# "+post.m_postPosition+", Horse: "+post.m_horseName+
     * " for sire's sire "+post.m_sireSireName+" "+sireSireData+"\n"); if
     * (sireSireData != null) { String sirePointsStr =
     * sireSireData.getProperty("POINTS"); if (sirePointsStr != null) { int
     * sirePoints = Math.round(Lib.atoi(sirePointsStr)/2);
     * post.m_handicap.points += sirePoints; if (post.m_sireTS2.equals(" "))
     * post.m_sireTS2 = "*"; if (Log.isDebug(Log.TRACE))
     * Log.print("     Post# "+
     * post.m_postPosition+", Horse: "+post.m_horseName+" "
     * +sirePoints+" points for sire's turf sire\n"); } }
     */
   }
   // Lookup AWD for sire, dam and dam sire
   String b1 = "", b2 = "";
   double sireAWDn = 0;
   double damAWDn = 0; 
   double damSireAWDn = 0; 
   post.m_sireTSPI3 = " ";
   Properties sireData2 = Truline.st.get("SIRE", post.m_sireName);
   Log.print("     Post# " + post.m_postPosition + ", Horse: "
     + post.m_horseName + " for sire " + post.m_sireName + " "
     + sireData2 + "\n");
   if (sireData2 != null) {
    String sireAWD = sireData2.getProperty("AWD");
    int sireAPRS = Lib.atoi(sireData2.getProperty("APRS"));
    sireAWDn = Lib.atof(sireAWD);
    post.m_sireAWD = sireAWD;
    post.m_sireAPRS = sireAPRS;
    if (sireAWDn >= 7 || sireAPRS >= race.m_purse*.8) {
     b1 = "\\b ";
     b2 = " \\b0";
    }     
    if ((sireAWDn > 6.8 && sireAWDn < 7.1) || sireAPRS >= race.m_purse*.8)
     post.m_sireTSPI3 = "b";
    else if (sireAWDn >= 7.1 || sireAPRS >= race.m_purse)
     post.m_sireTSPI3 = "B";
     post.m_sireTSPI2 = post.m_sireTSPI2+b1+"SireAWD/APRS-"+sireAWD+"/"+sireAPRS+b2;
   }
   
   b1 = "";
   b2 = "";
   Properties damData2 = Truline.dt.get("DAM", post.m_damName);
   Log.print("     Post# " + post.m_postPosition + ", Horse: "
     + post.m_horseName + " for dam " + post.m_damName + " "
     + damData2 + "\n");
   if (damData2 != null) {
    String damAWD = damData2.getProperty("AWD");
    int damAPRS = Lib.atoi(damData2.getProperty("APRS"));
    damAWDn = Lib.atof(damAWD);
    post.m_damAWD = damAWD;
    post.m_damAPRS = damAPRS;
    if (damAWDn >= 7 || damAPRS >= race.m_purse*.8) {
     b1 = "\\b ";
     b2 = " \\b0";
    }     

    if (!post.m_sireTSPI2.equals(""))
     post.m_sireTSPI2 = post.m_sireTSPI2+" / "+b1+"DamAWD/APRS-"+damAWD+"/"+damAPRS+b2;
    else 
     post.m_sireTSPI2 = b1+"DamAWD/APRS-"+damAWD+"/"+damAPRS+b2;
   }
    
   b1 = "";
   b2 = "";
   Properties damSireData2 = Truline.ds.get("DAMSIRE", post.m_damSireName);
   Log.print("     Post# " + post.m_postPosition + ", Horse: "
     + post.m_horseName + " for dam sire " + post.m_damSireName + " "
     + damSireData2 + "\n");
   if (damSireData2 != null) {
    String damSireAWD = damSireData2.getProperty("AWD");
    int damSireAPRS = Lib.atoi(damSireData2.getProperty("APRS"));
    damSireAWDn = Lib.atof(damSireAWD);
    post.m_damSireAWD = damSireAWD;
    post.m_damSireAPRS = damSireAPRS;
    if (damSireAWDn >= 7 || damSireAPRS >= race.m_purse*.8) {
     b1 = "\\b ";
     b2 = " \\b0";
    }     

    if (!post.m_sireTSPI2.equals(""))
     post.m_sireTSPI2 = post.m_sireTSPI2+" / "+b1+"DamSire/APRS-"+damSireAWD+"/"+damSireAPRS+b2;
    else 
     post.m_sireTSPI2 = b1+"DamSire/APRS-"+damSireAWD+"/"+damSireAPRS+b2;
   }
   
/*
   b1 = "";
   b2 = "";
   double sireTotalAWDn = sireAWDn + damSireAWDn;
   if (sireTotalAWDn > 0) {
    if (sireTotalAWDn >= 13.8) {
     b1 = "\\b ";
     b2 = " \\b0";
    }     
    post.m_sireTSPI2 = post.m_sireTSPI2+b1+" / Total AWD="+Lib.ftoa(sireTotalAWDn, 1)+b2;
   }
*/
   
  }
 }
 /*******************************
  * Assign Bonus Points:
  *
  * Right now, there is a Correlation Table with I think 8 Corelations. Each one
  * is getting 4 points. These are EPS1/EN1, EPS1/CS1, EPS1/AS1/RE1, EPS! and
  * any three other #1 rankings, and EPS1/QP1. Also CS1 and any three other #1
  * rankings, RE1 and any three other #1 rankings, and "any six #1 rankings".
  *
  * If we ever get to quesrying, there are 1025 permutations to check.
  *
  * Once all points are assigned, they are totalled for each horse. The final
  * ranking of the horses gives #1 to the horse with most points, #2 to the next
  * highest total, etc. Disregard the morning line that is presently generated.
  * This will be changed.
  *
  * @param race
  *         - The race being handicapped.
  * @return list of Posts in order by bonus point rankings.
  */
 private static Vector assignBonusPoints(Race race)
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("\n  Assign Corrolation Bonus Points in race #" + race.m_raceNo
     + "\n");
  Vector bonusRank = new Vector();
  Vector corrolation = new Vector();
  String race_surface = race.m_surface;
  String race_distance = "";
  if (race.m_distance > 1759)
   race_distance = "RT";
  else
   race_distance = "SP";
  String corrVersion = Truline.userProps.getProperty("CorrVersion",
    "ORIG");
  String corrVersion1 = "";
  int cnt = 0;
  for (Enumeration c = Truline.co.elements(); c.hasMoreElements();) {
   Properties prop = (Properties) c.nextElement();
   String track = prop.getProperty("TRACK");
   String trackCond = prop.getProperty("TRACKCOND");
   String surface = prop.getProperty("SURFACE");
   String distance = prop.getProperty("DISTANCE");
   if (track.substring(2).equals("X"))
    track = track.substring(0, 2);
   if (track.equals(race.m_track) && trackCond.equals(race.m_trackCond) && surface.equals(race_surface) && distance.equals(race_distance)) {
    corrolation.addElement(prop);
    cnt++;
   }
  }
  if (cnt == 0) {
    Log.print("   No track-specific Correlations for track "
        + race.m_track + "\n");
  }
  if (Log.isDebug(Log.TRACE))
   Log.print("   loaded " + cnt + " Correlations for track " + race.m_track
     + "\n");
  // Check each horse in the race.
  for (Enumeration e = race.m_posts.elements(); e.hasMoreElements();) {
   Post post = (Post) e.nextElement();
   String scratch = post.m_props.getProperty("ENTRY", "");
   if (post.m_horseName == null || scratch.equals("S"))
    continue; // position is empty or scratched
   // Log.print("     Post# "+post.m_postPosition+", Horse: "+post.m_horseName+"\n");
   int topRanks = 0;
   // Compute total of all Top Ranks for the horse
   for (int i = 0; i < names.length-3; i++) {
    if (post.m_handicap.rank[i] <= 0)
     continue;
    if (post.m_handicap.rank[i] == 1)
     topRanks++;
   }
   post.m_topRanks = topRanks;
   // Look at each corrolation
   post.m_handicap.bonus = 0;
   for (Enumeration c = corrolation.elements(); c.hasMoreElements();) {
    Properties prop = (Properties) c.nextElement();
    // Parse out the conditions, for example:
    // "ANY=6"
    // "CS=1 ANY=4"
    // "EPS=1 EN=1"
    // "EPS=1 QP=1"
    // "EPS=1 ANY=4"
    // "EPS=1 CS=1"
    // "FS=1 SS=1 QP=1"
    // "RE=1 ANY=4"
    // "EPS=1 AS=1 RE=1"
    String corr = prop.getProperty("CONDITION");
    String items = corr;
    int mark = 0;
    int count = 0;
    while (true) {
     int idx1 = items.indexOf("=");
     if (idx1 < 0)
      break;
     String name = items.substring(0, idx1);
     int idx2 = items.indexOf(" ", idx1);
     if (idx2 == -1)
      idx2 = items.length();
     int val = Lib.atoi(items.substring(idx1 + 1, idx2));
     count++;
     // Log.print("         Corrolation #"+count+"  "+name+"="+val+"\n");
     if (name.equals("ANY")) {
      if (topRanks >= val) {
       // Log.print("          HIT: ANY="+val+"\n");
       mark++;
      }
     } else {
      for (int i = 0; i < names.length; i++) {
       if (post.m_handicap.rank[i] <= 0)
        continue;
       if (name.equals(names[i]) && post.m_handicap.rank[i] == val) {
        // Log.print("          HIT: "+name+"="+val+" value="+post.m_handicap.rank[i]+"\n");
        mark++;
        break;
       }
      }
     }
     if (idx2 >= items.length())
      break;
     items = items.substring(idx2 + 1);
    }
    if (count > 0 && mark == count) {
     // Satisfied all of the elements of the corrolation
     int pnts = Lib.atoi(prop.getProperty("POINTS"));
     post.m_handicap.bonus += pnts;
     if (Log.isDebug(Log.TRACE))
      Log.print("     Post# " + post.m_postPosition + ", Horse: "
        + post.m_horseName + "  " + pnts + " points for " + corr + "\n");
    }
   }

   // Create Bonus Ranks
   int i;
   boolean found = false;
   for (i = 0; i < bonusRank.size(); i++) {
    Post p = (Post) bonusRank.elementAt(i);
    if (p.m_handicap.bonus + p.m_handicap.points < post.m_handicap.bonus
      + post.m_handicap.points) {
     bonusRank.insertElementAt(post, i);
     found = true;
     break;
    }
   }
   if (!found)
    bonusRank.addElement(post);
  }
  if (Log.isDebug(Log.TRACE))
   Log.print("\n  Rankings by Total points\n");
  for (int i = 0; i < bonusRank.size(); i++) {
   Post p = (Post) bonusRank.elementAt(i);
   p.m_handicap.bonusRank = i + 1;
   if (i == 0) {
    p.m_pointsAdv = p.m_handicap.bonus + p.m_handicap.points;
    if (i + 1 < bonusRank.size()) {
     Post p2 = (Post) bonusRank.elementAt(i + 1);
     p.m_pointsAdv = p.m_pointsAdv
       - (p2.m_handicap.bonus + p2.m_handicap.points);
    } else
     p.m_pointsAdv = 0;
   }
   if (Log.isDebug(Log.TRACE))
    Log.print("    #" + (i + 1) + " points="
      + (p.m_handicap.bonus + p.m_handicap.points) + " Post# "
      + p.m_postPosition + ", Horse: " + p.m_horseName + "\n");
  }
  return bonusRank;
 }
 /**
  * if it is greater than 8.0F, it is a route.
  */
 public boolean isRoute(Performance p)
 {
  if (p.ppDistance >= F8_0)
   return true;
  return false;
 }
 /**
  * Convert from yards to Furlongs and make a string out of it.
  */
 public static String toF(double d)
 {
  d = d / YdPerF;
  return Lib.ftoa(d, 1) + "F";
 }
 /**
  * Identify any horses that match previous profitable betting correlations
  */
 private static void identifyOtherPIFactors(Race race)
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("\n  Identify Other PI Factors in race #"
     + race.m_raceNo + "\n");
  // Check each horse in the race.
  for (Enumeration e = race.m_posts.elements(); e.hasMoreElements();) {
   Post post = (Post) e.nextElement();
   String scratch = post.m_props.getProperty("ENTRY", "");
   if (post.m_horseName == null || scratch.equals("S"))
    continue; // position is empty or scratched
   Log.print("     Post# " + post.m_postPosition + ", Horse: "
     + post.m_horseName + "\n");
   
   // Accumulate total points for race to enable computing odds
   race.totalPoints += post.m_handicap.bonus + post.m_handicap.points;
   
   // Add in other factors
   post.m_otherFactors = "";
   if (post.m_handicap.extraFlg || (post.m_handicap.value[FS] == 0 && post.m_handicap.rank[EPS] < 3) 
        || (post.m_handicap.value[FS] != 0 && post.m_handicap.rank[EPS] < 4) || !post.m_sireTSPI.equals("")) {
    post.m_otherFactors = "OtherFactors="+((post.m_handicap.extraFlg) ? "MBEN/" : "")
      +((post.m_handicap.value[FS] == 0 && post.m_handicap.rank[EPS] == 1) ? "NRL-EPS1/" : "")
      +((post.m_handicap.value[FS] == 0 && post.m_handicap.rank[EPS] == 2) ? "NRL-EPS2/" : "")
      +((post.m_handicap.value[FS] != 0 && post.m_handicap.rank[EPS] == 1) ? "EPS1/" : "")
      +((post.m_handicap.value[FS] != 0 && post.m_handicap.rank[EPS] == 2) ? "EPS2/" : "")
      +((post.m_handicap.value[FS] != 0 && post.m_handicap.rank[EPS] == 3) ? "EPS3/" : "")
      +post.m_sireTSPI;
   }
   else if (!post.m_sireTSPI2.equals("")) {
    post.m_otherFactors = "OtherFactors="+post.m_sireTSPI2;
    post.m_sireTSPI2 = "";
   }
   
   post.m_kimsEPS = "";
   post.m_kimsTT = "";
   post.m_kimsCS = "";
   post.m_kimsTTCS = "";
   // if (post.m_handicap.rank[EPS] < 4)
    post.m_kimsEPS = "EPS"+post.m_handicap.rank[EPS];
   if (post.m_handicap.rank[TT] < 4)
    post.m_kimsTT = "TT"+post.m_handicap.rank[TT];
   if (post.m_handicap.rank[CS] < 4)
    post.m_kimsCS = "CS"+post.m_handicap.rank[CS];
   if (post.m_handicap.rank[TTCS] < 4 && !post.m_repRaceDtl.equals(""))
    post.m_kimsTTCS = "TTCS"+post.m_handicap.rank[TTCS];
   if (Truline.userProps.getProperty("ArtAndKim", "N").equals("Y")) {
    if (post.m_handicap.rank[PP] < 4)
     post.m_kimsPP = "PP"+post.m_handicap.rank[PP]+"("+Lib.ftoa(post.m_primePower, 1)+")";
   }
   
  }

  // calculate TL odds
  for (Enumeration e = race.m_posts.elements(); e.hasMoreElements();) {
   Post post = (Post) e.nextElement();
   String scratch = post.m_props.getProperty("ENTRY", "");
   double rawOdds, rawPoints1, rawPoints2;
   if (post.m_horseName == null || scratch.equals("S"))
    continue; // position is empty or scratched
   
   // Get ML odds and caclulate our odds - set odds and DO
   double ml = Lib.atof(post.m_props.getProperty("MORNINGLINE"));
   post.m_morningLineD = ml;
   if ((post.m_handicap.bonus + post.m_handicap.points) == 0 || race.totalPoints == 0)
    rawOdds = 30;
   else {
    rawPoints1 = post.m_handicap.bonus + post.m_handicap.points;
    rawPoints2 = (rawPoints1 / race.totalPoints) * 125;
    rawOdds = (100.00/rawPoints2)-1.00;
    if (rawOdds < 0)
     rawOdds = rawOdds * -1;
   }
   String tlOdds = ""; 
   if (rawOdds < 1) 
    tlOdds = Lib.rjust(rawOdds, 2, 1);
   else
    if (rawOdds < 10) 
     tlOdds = Lib.rjust(rawOdds, 3, 1);
    else
     if (rawOdds > 30) 
      tlOdds = "30";
     else
      tlOdds = Lib.ftoa((double) rawOdds, 1);
   tlOdds = tlOdds + "-1";
   post.m_truLineDO = "";
   switch (tlOdds) {
    case ".1-1":
     post.m_truLine = "1-10";
     post.m_truLineD = .1;
     if (ml >= 1.5)
      post.m_truLineDO = "DO";
     break;
    case ".2-1":
     post.m_truLine = "1-5";
     post.m_truLineD = .2;
     if (ml >= 1.5)
      post.m_truLineDO = "DO";
     break;
    case ".3-1":
    case ".4-1":
    case ".5-1":
     post.m_truLine = "1-2";
     post.m_truLineD = .5;
     if (ml >= 1.5)
      post.m_truLineDO = "DO";
     break;
    case ".6-1":
     post.m_truLine = "3-5";
     post.m_truLineD = .6;
     if (ml >= 1.5)
      post.m_truLineDO = "DO";
     break;
    case ".7-1":
    case ".8-1":
     post.m_truLine = "4-5";
     post.m_truLineD = .8;
     if (ml >= 1.6)
      post.m_truLineDO = "DO";
     break;
    case "1.1-1":
    case "1.2-1":
     post.m_truLine = "6-5";
     post.m_truLineD = 1.2;
     if (ml >= 2.5)
      post.m_truLineDO = "DO";
     break;
    case "1.3-1":
    case "1.4-1":
     post.m_truLine = "7-5";
     post.m_truLineD = 1.4;
     if (ml >= 3)
      post.m_truLineDO = "DO";
     break;
    case "1.5-1":
     post.m_truLine = "3-2";
     post.m_truLineD = 1.5;
     if (ml >= 3)
      post.m_truLineDO = "DO";
     break;
    case "1.6-1":
     post.m_truLine = "8-5";
     post.m_truLineD = 1.6;
     if (ml >= 3.5)
      post.m_truLineDO = "DO";
     break;
    case "1.7-1":
    case "1.8-1":
     post.m_truLine = "9-5";
     post.m_truLineD = 1.8;
     if (ml >= 4)
      post.m_truLineDO = "DO";
     break;
    case "2.3-1":
    case "2.4-1":
    case "2.5-1":
     post.m_truLine = "5-2";
     post.m_truLineD = 2.5;
     if (ml >= 5)
      post.m_truLineDO = "DO";
     break;
    case "3.3-1":
    case "3.4-1":
    case "3.5-1":
     post.m_truLine = "7-2";
     post.m_truLineD = 3.5;
     if (ml >= 7)
      post.m_truLineDO = "DO";
     break;
    case "4.3-1":
    case "4.4-1":
    case "4.5-1":
     post.m_truLine = "9-2";
     post.m_truLineD = 4.5;
     if (ml >= 9)
      post.m_truLineDO = "DO";
     break;
    default:
     if (rawOdds > 30) {
      tlOdds = "30";
     }
     else {
      tlOdds = Lib.ftoa((float) rawOdds, 0);
     }
     int oddsDO = Integer.valueOf(tlOdds) * 2;
     post.m_truLine = tlOdds+"-1";
     post.m_truLineD = Integer.valueOf(tlOdds);
     if (ml >= oddsDO)
      post.m_truLineDO = "DO";
     break;
   }

  }
   return;
 }
 /**
  * Identify any horses that match previous profitable betting correlations
  */
 private static void identifyFlowBets(Race race)
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("\n  Identify Betting Factors and Flow Bets in race #"
     + race.m_raceNo + "\n");
  String post_betfactors = "";
  Vector betFactors = new Vector();
  String race_surface = race.m_surface;
  String distance = "";
  String trackCond = "";
  String surface = "";
  String raceType = "";
  String factor = "";
  String pct = "";
  String roi = "";
  String itmpct = "";
  String itmroi = "";
  String flowBet1 = "";
  String flowBet = "";
  double nrl = race.m_cntnrl;
  double horses = race.m_cnthorses;
  race.m_pctNRL = nrl / horses * 100;
  // if (pctNRL >= 50.0)
  // return;
  String betFactorVersion = Truline.userProps.getProperty("BetFactorVersion",
    "201212");
  String betFactorVersion1 = "";
  int cnt = 0;
  for (Enumeration c = Truline.bf.elements(); c.hasMoreElements();) {
   Properties prop = (Properties) c.nextElement();
   String track = prop.getProperty("TRACK");
   betFactorVersion1 = prop.getProperty("VERSION");
   trackCond = prop.getProperty("TRACKCOND");
   distance = prop.getProperty("DISTANCE");
   surface = prop.getProperty("SURFACE");
   raceType = prop.getProperty("RACETYPE");
   if (track.substring(2).equals("X"))
    track = track.substring(0, 2);
   if (betFactorVersion1.equals(betFactorVersion)) {
    if (track.equals("XX") || (track != null && track.equals(race.m_track))) {
     if (distance == null
       || ((distance.equals("RT") && race.m_distance > 1759) || (distance
         .equals("SP") && race.m_distance < 1760))) {
      if (surface == null || surface.equals(race_surface)) {
       if (raceType == null || raceType.equals(race.m_raceType)) {
        if (trackCond == null || trackCond.equals(race.m_trackCond)) {
         betFactors.addElement(prop);
         cnt++;
        }
       }
      }
     }
    }
   }
  }
  /*
   * No standard betting factors if (cnt == 0) { Log.print(
   * "   Loading standard Correlations - no track-specific Correlations for track "
   * +race.m_track+"\n"); for(Enumeration c = Truline.bf.elements();
   * c.hasMoreElements();) { Properties prop = (Properties)c.nextElement();
   * String track = prop.getProperty("TRACK"); distance =
   * prop.getProperty("DISTANCE"); surface = prop.getProperty("SURFACE"); if
   * (track != null && track.equals("XXX")) { if (distance == null ||
   * ((distance.equals("RT") && race.m_distance > 1759) ||
   * (distance.equals("SP") && race.m_distance < 1760))) { if (surface == null
   * || surface.equals(race_surface)) { if (raceType == null ||
   * raceType.equals(race.m_raceType)) { betFactors.addElement(prop); cnt++; } }
   * } } } }
   */
  if (Log.isDebug(Log.TRACE))
   Log.print("   loaded " + cnt + " Betting factors for track " + race.m_track
     + "\n");
  if (cnt == 0)
   return;
  // Check each horse in the race.
  for (Enumeration e = race.m_posts.elements(); e.hasMoreElements();) {
   Post post = (Post) e.nextElement();
   String scratch = post.m_props.getProperty("ENTRY", "");
   if (post.m_horseName == null || scratch.equals("S"))
    continue; // position is empty or scratched
   Log.print("     Post# " + post.m_postPosition + ", Horse: "
     + post.m_horseName + "\n");
   post.m_betfactors = 0;
   flowBet = "";
   int topRanks = post.m_topRanks;
   // Look at each betting factor
   int cnt_betting_factors = 0;
   for (Enumeration c2 = betFactors.elements(); c2.hasMoreElements();) {
    Properties prop2 = (Properties) c2.nextElement();
    flowBet1 = prop2.getProperty("FLOWBET");
    // Parse out the conditions, for example:
    // "ANY=6"
    // "CS=1 ANY=4"
    // "EPS=1 EN=1"
    // "EPS=1 QP=1"
    // "EPS=1 ANY=4"
    // "EPS=1 CS=1"
    // "FS=1 SS=1 QP=1"
    // "RE=1 ANY=4"
    // "EPS=1 AS=1 RE=1"
    String corr = prop2.getProperty("CONDITION");
    factor = prop2.getProperty("FACTOR");
    pct = prop2.getProperty("PCT");
    roi = prop2.getProperty("ROI");
    itmpct = prop2.getProperty("ITMPCT");
    itmroi = prop2.getProperty("ITMROI");
    String items = corr;
    int mark = 0;
    int count = 0;
    while (true) {
     int idx1 = items.indexOf("=");
     if (idx1 < 0)
      break;
     String name = items.substring(0, idx1);
     int idx2 = items.indexOf(" ", idx1);
     if (idx2 == -1)
      idx2 = items.length();
     int val = Lib.atoi(items.substring(idx1 + 1, idx2));
     double valD = val;
     valD = valD / 100;
     count++;
     Log.print("         Corrolation #" + count + "  " + name + "=" + val
       + "\n");
     if (name.equals("ANY")) {
      if (topRanks >= val) {
       Log.print("          HIT: ANY=" + val + "\n");
       mark++;
      }
     } else if (name.equals("TL")) {
      if (post.m_handicap.bonusRank <= val) {
       Log.print("          HIT: TL=" + val + "\n");
       mark++;
      }
     } else if (name.equals("TO")) {
      if (post.m_ownerTrn.equals("*")) {
       Log.print("          HIT: TO=" + val + "\n");
       mark++;
      }
     } else if (name.equals("SD")) {
      if (post.m_sireTS.equals("$") && post.m_sireTS2.equals("d")) {
       Log.print("          HIT: SD=" + val + "\n");
       mark++;
      }
     } else if (name.equals("TRN")) {
      if (post.m_trnPct >= val) {
       Log.print("          HIT: TRN>=" + val + "\n");
       mark++;
      }
     } else if (name.equals("JKY")) {
      if (post.m_jkyPct >= val) {
       Log.print("          HIT: JKY>=" + val + "\n");
       mark++;
      }
     } else if (name.equals("TRNM")) {
      if (post.m_trnPctM >= val) {
       Log.print("          HIT: TRNM>=" + val + "\n");
       pct = Lib.ftoa((int) post.m_trnPctM,0);
       roi = Lib.ftoa((double) post.m_trnROIM,2);
       mark++;
      }
     } else if (name.equals("ROI")) {
      if (post.m_trnROIM >= valD) {
       Log.print("          HIT: ROI>=" + val + "\n");
       mark++;
      }
     } else if (name.equals("CCHGD")) {
      double chgD = post.m_lastRaceClassChg * -1;
      if (chgD >= valD) {
       if (post.m_trainerClsChgDownOK == true) {
        Log.print("          HIT: CCHGD>=" + val + "\n");
        mark++;
       }
      }
     } else if (name.equals("WRK3")) {
      if (post.m_work3Short.equals("Y")) {
       Log.print("          HIT: WRK3=" + val + "\n");
       mark++;
      }
     } else if (name.equals("WRK4")) {
      if (post.m_work4Week.equals("Y")) {
       Log.print("          HIT: WRK4=" + val + "\n");
       mark++;
      }
     } else if (name.equals("BLT4")) {
      if (post.m_4furlongBullet.equals("@")) {
       Log.print("          HIT: BLT4=" + val + "\n");
       mark++;
      }
     } else if (name.equals("BLT5")) {
      if (post.m_5furlongBullet.equals("@")) {
       Log.print("          HIT: BLT5=" + val + "\n");
       mark++;
      }
     } else if (name.equals("NRL")) {
      if (race.m_pctNRL <= val) {
       Log.print("          HIT: NRL<" + val + "\n");
       mark++;
      }
     } else if (name.equals("NRLL")) {
      if (race.m_pctNRL >= val) {
       Log.print("          HIT: NRLL>=" + val + "\n");
       mark++;
      }
     } else if (name.equals("DSL")) {
      if (post.m_daysSinceLast <= val) {
       Log.print("          HIT: DSL<=" + val + "\n");
       mark++;
      }
     } else if (name.equals("FPL")) {
      if (post.m_daysSinceLast > 45 || post.m_finishPosLast > val) {
       Log.print("          HIT: FPL>" + val + "\n");
       mark++;
      }
     } else if (name.equals("MLL")) {
      if (post.m_morningLineD >= valD) {
       Log.print("          HIT: MLL>=" + valD + "\n");
       mark++;
      }
     } else if (name.equals("MLH")) {
      if (post.m_morningLineD <= valD) {
       Log.print("          HIT: MLH<" + valD + "\n");
       mark++;
      }
     } else if (name.equals("ONESD")) {
      if (race.m_cnt$d == 1 && post.m_sireTS.equals("$")
        && post.m_sireTS2.equals("d")) {
       Log.print("          HIT: ONESD=" + val + "\n");
       mark++;
      }
     } else {
      for (int i = 0; i < names.length; i++) {
       int rankW = post.m_handicap.rank[i];
       if (rankW <= 0)
        continue;
       if (name.equals(names[i]) && (rankW == val || (val > 1 && rankW <= val))) {
        Log.print("          HIT: " + name + "=" + val + " value="
          + post.m_handicap.rank[i] + "\n");
        mark++;
        break;
       }
      }
     }
     if (idx2 >= items.length())
      break;
     items = items.substring(idx2 + 1);
    }
    if (count > 0 && mark == count) {
     // Satisfied all of the elements of the betting factor
     if (post.cntHorseFlows < 19) {
      post.cntHorseFlows++;
      if (itmpct.equals("0"))
       post.horseFlows[post.cntHorseFlows] = "\\b B="+race.m_trackCond+"-"+factor+"/"+pct+"%/$"+roi+" \\b0";
      else
       post.horseFlows[post.cntHorseFlows] = "\\b B="+race.m_trackCond+"-"+factor+"/"+pct+"%/$"+roi+"/"+itmpct+"%/$"+itmroi+" \\b0";
     }
     if (flowBet1.equals("N")) {
      post.m_betfactors++;
     } else if (flowBet1.equals("Y")) {
      flowBet = "*";
      post.m_betfactors++;
     } else if (flowBet1.equals("F")) {
      flowBet = "$";
      post.m_betfactors++;
      if (race.cntRaceFlows < 20) {
       race.cntRaceFlows++;
       if (itmpct.equals("0"))
        race.raceFlows[race.cntRaceFlows] = String.format("%-22s", post.cloth+" "+post.m_horseName)+" "+factor+" / "+pct+"% / $"+roi;
       else
        race.raceFlows[race.cntRaceFlows] = String.format("%-22s", post.cloth+" "+post.m_horseName)+" "+factor+" / "+pct+"% / $"+roi+" / "+itmpct+"% / $"+itmroi;
      }
      if (race.cntRaceFlowsAK < 20 && Truline.userProps.getProperty("ArtAndKim", "N").equals("Y")) {
       race.cntRaceFlowsAK++;
       if (itmpct.equals("0"))
        race.raceFlowsAK[race.cntRaceFlowsAK] = String.format("%-22s", post.cloth+" "+post.m_horseName)+" "+factor+" / "+pct+"% / $"+roi;
       else
        race.raceFlowsAK[race.cntRaceFlowsAK] = String.format("%-22s", post.cloth+" "+post.m_horseName)+" "+factor+" / "+pct+"% / $"+roi+" / "+itmpct+"% / $"+itmroi;
      }
     }
     if (Log.isDebug(Log.TRACE))
      Log.print("     Post# " + post.m_postPosition + ", Horse: "
        + post.m_horseName + " met Betting Factor " + corr + "\n");
    }
   }
   if (post.m_betfactors > 0) {
    post.m_betfactorsPR = "b" + Lib.ftoa((int) post.m_betfactors, 0) + flowBet;
    race.cntHorseFlows++;
//   if (post.cntHorseFlows < 19 && post.m_betfactors >= Lib.atoi(Truline.userProps.getProperty("B-NumberIndicators", "99"))) {
//    post.cntHorseFlows++;
//    post.horseFlows[post.cntHorseFlows] = "B-NumberIndicators="+post.m_betfactors+flowBet;
//   }
   }
  }
  return;
 }

 /**
  * Identify any horses that match previous profitable betting correlations
  */
 private static void identifyRunStyle(Race race)
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("\n  Identify Run Style Profile for race #"
     + race.m_raceNo + "\n");
  String race_surface = race.m_surface;
  String distance = "";
  String trackCond = "";
  String surface = "";
  String runStyle = "";
  int cnt = 0;
  for (Enumeration c = Truline.rs.elements(); c.hasMoreElements();) {
   Properties prop = (Properties) c.nextElement();
   String track = prop.getProperty("TRACK");
   trackCond = prop.getProperty("TRACKCOND");
   distance = prop.getProperty("DISTANCE");
   surface = prop.getProperty("SURFACE");
   runStyle = prop.getProperty("RUNSTYLE");
   if (track.substring(2).equals("X"))
    track = track.substring(0, 2);
   // if (betFactorVersion1.equals(betFactorVersion)) {
    if (track != null && track.equals(race.m_track)) {
     if (distance == null
       || ((distance.equals("R") && race.m_distance > 1760) || (distance
         .equals("S") && race.m_distance < 1650) || distance.equals("M") &&
         race.m_distance >= 1650 && race.m_distance <= 1760)) {
      if (surface == null || surface.equals(race_surface)) {
        if (trackCond == null || trackCond.equals(race.m_trackCond)) {
         race.m_runStyleProfile = runStyle;
         cnt++;
        }
     }
    }
   }
  }
  /*
   * No run style stats if (cnt == 0)
   */
  if (Log.isDebug(Log.TRACE))
   Log.print("   loaded " + cnt + " Run Style Profile for track " + race.m_track
     + "\n");
  if (cnt == 0)
   return;
 }
 
 /**
  * Identify any horses that match previous profitable betting correlations
  */
 private static int identifyPowerTrainers(Race race, Post post)
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("\n  Identify New Power Trainer File for race " + race.m_raceNo + "\n");
  post.m_trainerNamePT = "  ";
  post.m_kimsPT = "";
  String race_surface = race.m_surface;
  String trainer = "";
  String distance = "";
  String surface = "";
  String roi = " ";
  String roi$ = " ";
  String pct = " ";
  String surfDist = " ";
  int points = 0;
  for (Enumeration c = Truline.pt.elements(); c.hasMoreElements();) {
   Properties prop = (Properties) c.nextElement();
   trainer = prop.getProperty("TRAINER");
   distance = prop.getProperty("DISTANCE");
   surface = prop.getProperty("SURFACE");
   roi$ = prop.getProperty("ROI");
   pct = prop.getProperty("PERCENT");
   int pointsW = Lib.atoi(prop.getProperty("POINTS"));
   if (post.m_trainerName != null) {
    if (post.m_trainerName.equals(trainer)) {
     if (distance == null) {
      if (surface == null) {
       roi = "*";
       post.m_trainerNamePT = roi + "#";
       post.m_trainerNamePT1 = " (" + pct + "%  $" + roi$ + ")";
       points += pointsW;
      }
     }
     if (distance != null) {
      if (((distance.equals("RT") && race.m_distance > 1759) || (distance
          .equals("SP") && race.m_distance < 1760))) {
       if (surface.equals(race_surface)) {
        post.m_trainerNamePT = roi + "#";
        surfDist = "#";
        post.m_kimsPT = "PT";
        post.m_trainerNamePT2 = " (" + pct + "%  $" + roi$ + ")";
        points += pointsW;
       }
      }
     }
    }
   }
  }
   /*
   * No power trainers for this surface / distance if (cnt == 0)
   */
   if (points == 0){
    if (Log.isDebug(Log.TRACE))
     Log.print("Did not find power trainers for horse " + post.m_horseName + " in race " + race.m_raceNo
       + "\n");
    return 0;
   }
   else {
    if (Log.isDebug(Log.TRACE))
     Log.print("Found power trainer for horse " + post.m_horseName + " in race " + race.m_raceNo
      + "\n");
    if (post.cntHorseFlows < 19) {
     race.cntHorseFlows++;
     post.cntHorseFlows++;
     post.horseFlows[post.cntHorseFlows] = "\\b Power Trainer for "+
       (roi.equals("*") ? "All Races" + post.m_trainerNamePT1 : "")+
       (roi.equals("*") && surfDist.equals("#") ? " + " : "")+
       ((surfDist.equals("#") && race.m_distance > 1759 && race.m_surface.equals("D")) ? "Dirt Routes" + post.m_trainerNamePT2 : "")+
       ((surfDist.equals("#") && race.m_distance > 1759 && race.m_surface.equals("T")) ? "Turf Routes" + post.m_trainerNamePT2 : "")+
       ((surfDist.equals("#") && race.m_distance > 1759 && race.m_surface.equals("A")) ? "All Weather Routes" + post.m_trainerNamePT2 : "")+
       ((surfDist.equals("#") && race.m_distance < 1760 && race.m_surface.equals("D")) ? "Dirt Sprints" + post.m_trainerNamePT2 : "")+
       ((surfDist.equals("#") && race.m_distance < 1760 && race.m_surface.equals("T")) ? "Turf Sprints" + post.m_trainerNamePT2 : "")+
       ((surfDist.equals("#") && race.m_distance < 1760 && race.m_surface.equals("A")) ? "All Weather Sprints" + post.m_trainerNamePT2 : "")+
       " \\b0";
    }
    return points;
   }
 }
 /**
  * Identify any horses that match previous profitable betting correlations for trainers
  */
 private static void identifyTrnFlowBets(Race race)
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("\n  Identify Trainer Flow Bets in race #" + race.m_raceNo + "\n");
  String post_trnfactors = "";
  Vector trnFactors = new Vector();
  String race_surface = race.m_surface;
  String trainer = "";
  String distance = "";
  String surface = "";
  String raceType = "";
  String age = "";
  String sex = "";
  String trnOwn = "";
  String tfactor = "";
  String pct = "";
  String roi = "";
  String ITMpct = "";
  String ITMroi = "";
  String flowBet1 = "";
  String flowBet = "";
  int cnt = 0;
  for (Enumeration c = Truline.tf.elements(); c.hasMoreElements();) {
   Properties prop = (Properties) c.nextElement();
   String track = prop.getProperty("TRACK");
   trainer = prop.getProperty("TRAINER");
   distance = prop.getProperty("DISTANCE");
   surface = prop.getProperty("SURFACE");
   raceType = prop.getProperty("RACETYPE");
   age = prop.getProperty("AGE");
   sex = prop.getProperty("SEX");
   trnOwn = prop.getProperty("TO");
   String sexAge = race.m_props.getProperty("AGESEX", "   ");
   if (track != null)
    if (track.substring(2).equals("X"))
     track = track.substring(0, 2);
   if (track == null || track.equals("All") || (track != null && track.equals(race.m_track))) {
    if (distance == null
      || ((distance.equals("RT") && race.m_distance > 1759) || (distance
        .equals("SP") && race.m_distance < 1760))) {
     if (surface == null || surface.equals(race_surface)) {
      if ((raceType == null || raceType.equals(race.m_raceType))
        || (raceType.equals("G") && race.m_purse >= 100000)
        || (raceType.equals("A") && race.m_purse < 100000 && (!race.m_raceType
          .equals("M") && !race.m_raceType.equals("S") && !race.m_raceType
           .equals("C")))) {
       if (age == null || (age.equals("2") && sexAge.charAt(0) == 'A')) {
        if (sex == null
          || (sex.equals("F") && (sexAge.charAt(2) == 'F' || sexAge.charAt(2) == 'M'))
          || (sex.equals("M") && sexAge.charAt(2) != 'F' && sexAge.charAt(2) != 'M')) {
         trnFactors.addElement(prop);
         cnt++;
        }
       }
      }
     }
    }
   }
  }
  /*
   * No trainer factors across all tracks if (cnt == 0) { Log.print(
   * "   Loading standard Correlations - no track-specific Correlations for track "
   * +race.m_track+"\n"); for(Enumeration c = Truline.tf.elements();
   * c.hasMoreElements();) { Properties prop = (Properties)c.nextElement();
   * String track = prop.getProperty("TRACK"); distance =
   * prop.getProperty("DISTANCE"); surface = prop.getProperty("SURFACE"); if
   * (track != null && track.equals("XXX")) { if (distance == null ||
   * ((distance.equals("RT") && race.m_distance > 1759) ||
   * (distance.equals("SP") && race.m_distance < 1760))) { if (surface == null
   * || surface.equals(race_surface)) { if (raceType == null ||
   * raceType.equals(race.m_raceType)) { trnFactors.addElement(prop); cnt++; } }
   * } } } }
   */
  if (Log.isDebug(Log.TRACE))
   Log.print("   loaded " + cnt + " trainer factors for track " + race.m_track
     + "\n");
  if (cnt == 0)
   return;
  // Check each horse in the race.
  for (Enumeration e = race.m_posts.elements(); e.hasMoreElements();) {
   Post post = (Post) e.nextElement();
   String scratch = post.m_props.getProperty("ENTRY", "");
   if (post.m_horseName == null || scratch.equals("S"))
    continue; // position is empty or scratched
   Log.print("     Post# " + post.m_postPosition + ", Horse: "
     + post.m_horseName + "\n");
   post.m_trnfactors = 0;
   post.m_trnPctF = 0;
   // Look at each trainer factor
   int cnt_trainer_factors = 0;
   for (Enumeration c = trnFactors.elements(); c.hasMoreElements();) {
    Properties prop = (Properties) c.nextElement();
    flowBet1 = prop.getProperty("FLOWBET");
    trnOwn = prop.getProperty("TO");
    distance = prop.getProperty("DISTANCE");
    surface = prop.getProperty("SURFACE");
    trainer = prop.getProperty("TRAINER");
    tfactor = prop.getProperty("TFACTOR");
    pct = prop.getProperty("PCT");
    roi = prop.getProperty("ROI");
    ITMpct = prop.getProperty("ITM");
    ITMroi = prop.getProperty("ITMROI");
    if (trainer.equals(post.m_trainerName)
      && (trnOwn == null || post.m_ownerTrn.equals("*"))) {
     if (distance != null && surface != null){
      post.m_trnPctF = Lib.atoi(pct);
     }
     if (flowBet1.equals("Y") && post.cntHorseFlows < 19) {
      post.cntHorseFlows++;
      if (Truline.userProps.getProperty("ArtAndKim", "N").equals("Y")) 
       post.horseFlows[post.cntHorseFlows] = "\\b T Factor="+tfactor+"/"+pct+"%/$"+roi+"/"+ITMpct+"%/$"+ITMroi+" \\b0";
      else
       post.horseFlows[post.cntHorseFlows] = "\\b T="+tfactor+"/"+pct+"%/$"+roi+" \\b0";
     }
     if (flowBet1.equals("Y")) {
      flowBet = "*";
      post.m_trnfactors++;
     } else if (flowBet1.equals("F")) {
      flowBet = "$";
      post.m_trnfactors++;
      if (Truline.userProps.getProperty("ArtAndKim", "N").equals("Y"))
       if (post.cntHorseFlows < 19) {
        post.cntHorseFlows++;
        post.horseFlows[post.cntHorseFlows] = "\\b T Factor="+tfactor+"/"+pct+"%/$"+roi+"/"+ITMpct+"%/$"+ITMroi+" \\b0";
       }
       else if (race.cntRaceFlows < 20) {
        race.cntRaceFlows++;
        race.raceFlows[race.cntRaceFlows] = String.format("%-22s", post.cloth+" "+post.m_horseName)+" 1-year Trainer Strength "+trainer+" "+tfactor+"/"+pct+"%/$"+roi;
       }
     }
    }
    /*
     * No conditions - only check number of times trainer name appears String
     * corr = prop.getProperty("CONDITION"); String items = corr; int mark = 0;
     * int count = 0; while (true) { int idx1 = items.indexOf("="); if (idx1 <
     * 0) break; String name = items.substring(0,idx1); int idx2 =
     * items.indexOf(" ", idx1); if (idx2 == -1) idx2 = items.length(); int val
     * = Lib.atoi(items.substring(idx1+1, idx2)); count++;
     * Log.print("         Corrolation #"+count+"  "+name+"="+val+"\n"); if
     * (name.equals("ANY")) { if (topRanks >= val) {
     * //Log.print("          HIT: ANY="+val+"\n"); mark++; break; } } else if
     * (name.equals("TO")) { if (post.m_ownerTrn.equals("*")) {
     * //Log.print("          HIT: TO="+val+"\n"); mark++; break; } } else if
     * (name.equals("SD")) { if (post.m_sireTS.equals("$") &&
     * post.m_sireTS2.equals("d")) { //Log.print("          HIT: SD="+val+"\n");
     * mark++; break; } } else if (name.equals("ONESD")) { if (race.m_cnt$d == 1
     * && post.m_sireTS.equals("$") && post.m_sireTS2.equals("d")) {
     * //Log.print("          HIT: ONESD="+val+"\n"); mark++; break; } } else {
     * for (int i=0; i < names.length; i++) { int rankW =
     * post.m_handicap.rank[i]; if (rankW <= 0) continue; if
     * (name.equals(names[i]) && (rankW == val || (val > 1 && rankW <= val))) {
     * Log
     * .print("          HIT: "+name+"="+val+" value="+post.m_handicap.rank[i]
     * +"\n"); mark++; break; } } } if (idx2 >= items.length()) break; items =
     * items.substring(idx2+1); } if (count > 0 && mark == count) { // Satisfied
     * all of the elements of the trainer factor post.m_trnfactors++; if
     * (Log.isDebug(Log.TRACE))
     * Log.print("     Post# "+post.m_postPosition+", Horse: "+post.m_horseName
     * +" met "+corr+"\n"); }
     */
   }
   if (post.m_trnfactors > 0) {
    post.m_trnfactorsPR = "/t" + Lib.ftoa((int) post.m_trnfactors, 0) + flowBet;
    post.m_kimsT1 = "t" + Lib.ftoa((int) post.m_trnfactors, 0) + flowBet;
    // race.cntHorseFlows++;
//   if (post.cntHorseFlows < 19 && post.m_trnfactors >= Lib.atoi(Truline.userProps.getProperty("T-NumberIndicators", "99"))) {
//    post.cntHorseFlows++;
//    post.horseFlows[post.cntHorseFlows] = "T-NumberIndicators="+post.m_trnfactors+flowBet;
//   }
   }
  }
  return;
 }
 /**
  * Identify any horses that match previous profitable betting correlations for jockeys
  */
 private static void identifyJkyFlowBets(Race race)
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("\n  Identify Jockey Flow Bets in race #" + race.m_raceNo + "\n");
  String post_jkyfactors = "";
  Vector jkyFactors = new Vector();
  String race_surface = race.m_surface;
  String jockey = "";
  String distance = "";
  String surface = "";
  String raceType = "";
  String age = "";
  String sex = "";
  String jfactor = "";
  String pct = "";
  String roi = "";
  String ITMpct = "";
  String ITMroi = "";
  String flowBet1 = "";
  String flowBet = "";
  int cnt = 0;
  for (Enumeration c = Truline.jf.elements(); c.hasMoreElements();) {
   Properties prop = (Properties) c.nextElement();
   String track = prop.getProperty("TRACK");
   jockey = prop.getProperty("JOCKEY");
   distance = prop.getProperty("DISTANCE");
   surface = prop.getProperty("SURFACE");
   raceType = prop.getProperty("RACETYPE");
   age = prop.getProperty("AGE");
   sex = prop.getProperty("SEX");
   String sexAge = race.m_props.getProperty("AGESEX", "   ");
   if (track != null)
    if (track.substring(2).equals("X"))
     track = track.substring(0, 2);
   if (track == null || track.equals("All") || (track != null && track.equals(race.m_track))) {
    if (distance == null
      || ((distance.equals("RT") && race.m_distance > 1759) || (distance
        .equals("SP") && race.m_distance < 1760))) {
     if (surface == null || surface.equals(race_surface)) {
      if ((raceType == null || raceType.equals(race.m_raceType))
        || (raceType.equals("G") && race.m_purse >= 100000)
        || (raceType.equals("A") && race.m_purse < 100000 && (!race.m_raceType
          .equals("M") && !race.m_raceType.equals("S") && !race.m_raceType
           .equals("C")))) {
       if (age == null || (age.equals("2") && sexAge.charAt(0) == 'A')) {
        if (sex == null
          || (sex.equals("F") && (sexAge.charAt(2) == 'F' || sexAge.charAt(2) == 'M'))
          || (sex.equals("M") && sexAge.charAt(2) != 'F' && sexAge.charAt(2) != 'M')) {
         jkyFactors.addElement(prop);
         cnt++;
        }
       }
      }
     }
    }
   }
  }
  /*
   * No jockey factors across all tracks if (cnt == 0) { Log.print(
   * "   Loading standard Correlations - no track-specific Correlations for track "
   * +race.m_track+"\n"); for(Enumeration c = Truline.tf.elements();
   * c.hasMoreElements();) { Properties prop = (Properties)c.nextElement();
   * String track = prop.getProperty("TRACK"); distance =
   * prop.getProperty("DISTANCE"); surface = prop.getProperty("SURFACE"); if
   * (track != null && track.equals("XXX")) { if (distance == null ||
   * ((distance.equals("RT") && race.m_distance > 1759) ||
   * (distance.equals("SP") && race.m_distance < 1760))) { if (surface == null
   * || surface.equals(race_surface)) { if (raceType == null ||
   * raceType.equals(race.m_raceType)) { jkyFactors.addElement(prop); cnt++; } }
   * } } } }
   */
  if (Log.isDebug(Log.TRACE))
   Log.print("   loaded " + cnt + " jockey factors for track " + race.m_track
     + "\n");
  if (cnt == 0)
   return;
  // Check each horse in the race.
  for (Enumeration e = race.m_posts.elements(); e.hasMoreElements();) {
   Post post = (Post) e.nextElement();
   String scratch = post.m_props.getProperty("ENTRY", "");
   if (post.m_horseName == null || scratch.equals("S"))
    continue; // position is empty or scratched
   Log.print("     Post# " + post.m_postPosition + ", Horse: "
     + post.m_horseName + "\n");
   post.m_jkyfactors = 0;
   post.m_jkyPctF = 0;
   post.m_jkyfactorsPR = "";
   // Look at each jockey factor
   int cnt_jockey_factors = 0;
   for (Enumeration c = jkyFactors.elements(); c.hasMoreElements();) {
    Properties prop = (Properties) c.nextElement();
    flowBet1 = prop.getProperty("FLOWBET");
    jockey = prop.getProperty("JOCKEY");
    jfactor = prop.getProperty("JFACTOR");
    distance = prop.getProperty("DISTANCE");
    surface = prop.getProperty("SURFACE");
    pct = prop.getProperty("PCT");
    roi = prop.getProperty("ROI");
    ITMpct = prop.getProperty("ITM");
    ITMroi = prop.getProperty("ITMROI");
    if (jockey.equals(post.m_jockeyName)) {
     if (distance != null && surface != null){
      post.m_jkyPctF = Lib.atoi(pct);
     }
     if (flowBet1.equals("Y") && post.cntHorseFlows < 19) {
      post.cntHorseFlows++;
      if (Truline.userProps.getProperty("ArtAndKim", "N").equals("Y")) 
       post.horseFlows[post.cntHorseFlows] = "\\b J Factor="+jfactor+"/"+pct+"%/$"+roi+"/"+ITMpct+"%/$"+ITMroi+" \\b0";
      else
       post.horseFlows[post.cntHorseFlows] = "\\b J="+jfactor+"/"+pct+"%/$"+roi+" \\b0";
     }
     if (flowBet1.equals("Y")) {
      flowBet = "*";
      post.m_jkyfactors++;
     } else if (flowBet1.equals("F")) {
      flowBet = "$";
      post.m_jkyfactors++;
/*
      if (race.cntRaceFlows < 20) {
       race.cntRaceFlows++;
       race.raceFlows[race.cntRaceFlows] = String.format("%-22s", post.cloth+" "+post.m_horseName)+" 1-year Jockey Strength "+jockey+" "+jfactor+"/"+pct+"%/$"+roi;
      }
 */
      }
     }
    }
    /*
     * No conditions - only check number of times jockey name appears String
     * corr = prop.getProperty("CONDITION"); String items = corr; int mark = 0;
     * int count = 0; while (true) { int idx1 = items.indexOf("="); if (idx1 <
     * 0) break; String name = items.substring(0,idx1); int idx2 =
     * items.indexOf(" ", idx1); if (idx2 == -1) idx2 = items.length(); int val
     * = Lib.atoi(items.substring(idx1+1, idx2)); count++;
     * Log.print("         Corrolation #"+count+"  "+name+"="+val+"\n"); if
     * (name.equals("ANY")) { if (topRanks >= val) {
     * //Log.print("          HIT: ANY="+val+"\n"); mark++; break; } } else if
     * (name.equals("TO")) { if (post.m_ownerTrn.equals("*")) {
     * //Log.print("          HIT: TO="+val+"\n"); mark++; break; } } else if
     * (name.equals("SD")) { if (post.m_sireTS.equals("$") &&
     * post.m_sireTS2.equals("d")) { //Log.print("          HIT: SD="+val+"\n");
     * mark++; break; } } else if (name.equals("ONESD")) { if (race.m_cnt$d == 1
     * && post.m_sireTS.equals("$") && post.m_sireTS2.equals("d")) {
     * //Log.print("          HIT: ONESD="+val+"\n"); mark++; break; } } else {
     * for (int i=0; i < names.length; i++) { int rankW =
     * post.m_handicap.rank[i]; if (rankW <= 0) continue; if
     * (name.equals(names[i]) && (rankW == val || (val > 1 && rankW <= val))) {
     * Log
     * .print("          HIT: "+name+"="+val+" value="+post.m_handicap.rank[i]
     * +"\n"); mark++; break; } } } if (idx2 >= items.length()) break; items =
     * items.substring(idx2+1); } if (count > 0 && mark == count) { // Satisfied
     * all of the elements of the jockey factor post.m_jkyFactors++; if
     * (Log.isDebug(Log.TRACE))
     * Log.print("     Post# "+post.m_postPosition+", Horse: "+post.m_horseName
     * +" met "+corr+"\n"); }
     */

    if (post.m_jkyfactors > 0) {
     post.m_jkyfactorsPR = "/j" + Lib.ftoa((int) post.m_jkyfactors, 0) + flowBet;
     post.m_kimsJ1 = "j" + Lib.ftoa((int) post.m_jkyfactors, 0) + flowBet;
     // race.cntHorseFlows++;
//   if (post.cntHorseFlows < 19 && post.m_jkyfactors >= Lib.atoi(Truline.userProps.getProperty("J-NumberIndicators", "99"))) {
//    post.cntHorseFlows++;
//    post.horseFlows[post.cntHorseFlows] = "J-NumberIndicators="+post.m_jkyfactors+flowBet;
//   }
    }
  }
  return;
 }
 /**
  * Identify any horses that match previous profitable betting correlations for trainers
  */
 private static void identifyTrnJkyFlowBets(Race race)
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("\n  Identify Trainer Jockey Flow Bets in race #" + race.m_raceNo + "\n");
  String post_trnJkyfactors = "";
  Vector trnJkyFactors = new Vector();
  String race_surface = race.m_surface;
  String race_date = race.m_props.getProperty("RACEDATE");
  String start_date = "";
  String stop_date = "";
  String trainer = "";
  String jockey = "";
  String distance = "";
  String surface = "";
  String raceType = "";
  String age = "";
  String sex = "";
  String fts = "";
  String layoff = "";
  String fav = "";
  String source = "";
  String tjfactor = "";
  String pct = "";
  String roi = "";
  String ITMpct = "";
  String ITMroi = "";
  String flowBet1 = "";
  String flowBet = "";
  Boolean SD1 = false;
  Boolean SD2 = false;
  Boolean SD3 = false;
  Boolean TYP = false;
  Boolean FAV = false;
  Boolean TRN = false;
  int cnt = 0;
  for (Enumeration c = Truline.t4.elements(); c.hasMoreElements();) {
   Properties prop = (Properties) c.nextElement();
   start_date = prop.getProperty("START");
   stop_date = prop.getProperty("STOP");
   String track = prop.getProperty("TRACK");
   trainer = prop.getProperty("TRAINER");
   jockey = prop.getProperty("JOCKEY");
   surface = prop.getProperty("SURFACE");
   distance = prop.getProperty("DISTANCE");
   raceType = prop.getProperty("RACETYPE");
   age = prop.getProperty("AGE");
   sex = prop.getProperty("SEX");
   fts= prop.getProperty("FTS");
   layoff = prop.getProperty("LAYOFF");
   fav = prop.getProperty("FAV");
   source = prop.getProperty("SOURCE");
   String sexAge = race.m_props.getProperty("AGESEX", "   ");
   if (track != null)
    if (track.substring(2).equals("X"))
     track = track.substring(0, 2);
   SD1 = (distance != null && surface != null 
     && ((distance.equals("RT") && race.m_distance > 1649) 
     || (distance.equals("SP") && race.m_distance < 1650))  
     && (surface.equals(race.m_surface) 
       || (surface.equals("D") && race.m_surface.equals("A"))));
   SD2 = (distance == null
       && surface != null && (surface.equals(race.m_surface) 
         || (surface.equals("D") && race.m_surface.equals("A"))));
   SD3 = (surface == null && distance != null
     && ((distance.equals("RT") && race.m_distance > 1649) 
     || (distance.equals("SP") && race.m_distance < 1650)));
   TYP = (raceType != null
     && ((raceType.equals(race.m_raceType)) 
     || (raceType.equals("M") && race.m_raceType.equals("S")) 
//     || (raceType.equals("C") && race.m_raceType.equals("CO")) 
     || (raceType.equals("A") && race.m_raceType.equals("AO"))
     || (raceType.equals("A") && (race.m_raceType.equals("N") && race.m_purse < 100000))
//     || (raceType.equals("A") && race.m_raceType.equals("R")) 
     || (raceType.equals("G") && (race.m_raceType.equals("N") && race.m_purse >= 100000))
     || (raceType.equals("G") && race.m_raceType.charAt(0) == 'G')));
   Boolean SEX = (sex != null);
//     && ((sex.equals("F") && (sexAge.charAt(2) == 'F' || sexAge.charAt(2) == 'M'))
//     ||  (sex.equals("M") && sexAge.charAt(2) != 'F' && sexAge.charAt(2) != 'M')));
   Boolean AGE = (age != null);
//     && ((age.equals("2") && sexAge.charAt(0) == 'A') 
//     || (age.equals("3") && sexAge.charAt(0) == 'B')));
   Boolean FTS = (fts != null);
   Boolean LAYOFF = (layoff != null);
   FAV = (fav != null);
   TRN = (jockey == null);
   
   if (track == null || track.equals("All") || (track != null && track.equals(race.m_track))) 
   {
    int activeT4 = race_date.compareTo(start_date);
    int activeT4X = -1;
    if (stop_date != null)
     activeT4X = race_date.compareTo(stop_date);
    if (activeT4 > -1 && activeT4X < 0)
    {
     if (SD1 || SD2 || SD3 || TYP || FAV || TRN)
//      if (SD1 || SD2 || SD3 || TYP || AGE || SEX || FTS || LAYOFF || FAV)
     {
      trnJkyFactors.addElement(prop);
      cnt++;
     }
    }
   }  
  }
  /*
   * No trainer-jockey flow bets if (cnt == 0) 
   */
  if (Log.isDebug(Log.TRACE))
   Log.print("   loaded " + cnt + " trainer jockey flow bets for track " + race.m_track
     + "\n");
  if (cnt == 0)
   return;
  // Check each horse in the race.
  for (Enumeration e = race.m_posts.elements(); e.hasMoreElements();) {
   Post post = (Post) e.nextElement();
   String scratch = post.m_props.getProperty("ENTRY", "");
   if (post.m_horseName == null || scratch.equals("S"))
    continue; // position is empty or scratched
   Log.print("     Post# " + post.m_postPosition + ", Horse: "
     + post.m_horseName + "\n");
   post.m_trnJkyFactors = 0;
   post.m_trnJkyPctF = 0;
   post.m_trnJkyfactorsSD = "";
   post.m_trnJkyfactorsTYP = "";
   post.m_trnJkyfactorsSEX = "";
   post.m_trnJkyfactorsAGE = "";
   post.m_trnJkyfactorsFTS = "";
   post.m_trnJkyfactorsLAY = "";
   post.m_trnJkyfactorsFAV = "";
   post.m_trnJkyfactorsTRN = "";
   post.m_trnJkyfactorsSOURCE = "";
   // Look at each trainer jockey factor
   int cnt_trainer_jockey_factors = 0;
   for (Enumeration c = trnJkyFactors.elements(); c.hasMoreElements();) {
    Properties prop = (Properties) c.nextElement();
    flowBet1 = prop.getProperty("FLOWBET");
    trainer = prop.getProperty("TRAINER");
    jockey = prop.getProperty("JOCKEY");
    surface = prop.getProperty("SURFACE");
    distance = prop.getProperty("DISTANCE");
    raceType = prop.getProperty("RACETYPE");
    age = prop.getProperty("AGE");
    sex = prop.getProperty("SEX");
    fts= prop.getProperty("FTS");
    layoff= prop.getProperty("LAYOFF");
    fav= prop.getProperty("FAV");
    source= prop.getProperty("SOURCE");
//    tjfactor = prop.getProperty("TJFACTOR");
//    pct = prop.getProperty("PCT");
//    roi = prop.getProperty("ROI");
//    ITMpct = prop.
//      getProperty("ITM");
//    ITMroi = prop.getProperty("ITMROI");
    if (trainer.equals(post.m_trainerName) && (jockey == null || jockey.equals(post.m_jockeyName))) {
     if (post.m_trnJkyfactorsSOURCE == "") {
      post.m_trnJkyFactors++;
      post.m_trnJkyfactorsSOURCE = source;
     }
     if (distance != null || surface != null){
      post.m_trnJkyfactorsSD = "Y";
     }
     if (raceType != null){
      post.m_trnJkyfactorsTYP = "Y";
     }
     if (age != null && age.equals(Lib.ftoa((int) post.m_age, 0))) {
      post.m_trnJkyfactorsAGE = "Y";
     }
     if (sex != null && ((sex.equals("F") && (post.m_sex.equals("F") || post.m_sex.equals("M")))
                     || (sex.equals("C") && (post.m_sex.equals("C") || post.m_sex.equals("G"))))) {
      post.m_trnJkyfactorsSEX = "Y";
     }
     if (fts != null && (race.m_raceType.equals("M") || race.m_raceType.equals("M"))
       && post.m_daysSinceLast == -1) {
      post.m_trnJkyfactorsFTS = "Y";
     }
     if (layoff != null && layoff.equals("Y") && post.m_daysSinceLast > 89) 
      post.m_trnJkyfactorsLAY = "Y";
     else if (layoff != null && layoff.equals("N"))
      post.m_trnJkyfactorsLAY = "N";
     if (fav != null && fav.equals("Y") && (post.m_handicap.rank[Handicap.ML] == 1 || post.m_handicap.value[Handicap.ML] <= 4)) 
      post.m_trnJkyfactorsFAV = "Y";
     else if (fav != null && fav.equals("N"))
      post.m_trnJkyfactorsFAV = "N";
     if (jockey == null)
      post.m_trnJkyfactorsTRN = "Y";
      

/* ---- Removed for Art and Kim flow bet logic
     if (flowBet1.equals("F") && post.cntHorseFlows < 19) {
      post.cntHorseFlows++;
      if (Truline.userProps.getProperty("ArtAndKim", "N").equals("Y")) 
       post.horseFlows[post.cntHorseFlows] = "\\b T-J="+tjfactor+"/"+pct+"%/$"+roi+"/"+ITMpct+"%/$"+ITMroi+" \\b0";
      else
       post.horseFlows[post.cntHorseFlows] = "\\b T-J="+tjfactor+"/"+pct+"%/$"+roi+" \\b0";
     }
     if (flowBet1.equals("N")) {
      post.m_trnJkyFactors++;
     } else if (flowBet1.equals("Y")) {
      flowBet = "*";
      post.m_trnJkyFactors++;
     } else if (flowBet1.equals("F")) {
      flowBet = "$";
      post.m_trnJkyFactors++;
      post.m_last25 = true;
      if (Truline.userProps.getProperty("ArtAndKim", "N").equals("Y"))
       if (post.cntHorseFlows < 19) {
        post.cntHorseFlows++;
        post.horseFlows[post.cntHorseFlows] = "\\b T-J Factor="+tjfactor+"/"+pct+"%/$"+roi+"/"+ITMpct+"%/$"+ITMroi+" \\b0";
       }
       else if (race.cntRaceFlows < 20) {
        race.cntRaceFlows++;
        race.raceFlows[race.cntRaceFlows] = String.format("%-22s", post.cloth+" "+post.m_horseName)+" 1-year Trainer Jockey Strength "+trainer+" "+tjfactor+"/"+pct+"%/$"+roi;
      }
     }
     */
    }
   }
   if (post.m_trnJkyFactors > 0) {
//    if(post.m_trnJkyfactorsSD == "" && post.m_trnJkyfactorsTYP.equals("Y"))
//     setTrainerJockeyTYPFactor(race, post);
    if (Log.isDebug(Log.TRACE))
     Log.print("   loaded " + post.m_trnJkyFactors + " trainer jockey flow bets for track " + race.m_track
       + "\n");
    post.m_trnJkyfactorsPR = "/t" + Lib.ftoa((int) post.m_trnJkyFactors, 0) + flowBet;
    // post.m_kimsT1 = "t" + Lib.ftoa((int) post.m_trnJkyFactors, 0) + flowBet;
    // race.cntHorseFlows++;
//   if (post.cntHorseFlows < 19 && post.m_trnfactors >= Lib.atoi(Truline.userProps.getProperty("T-NumberIndicators", "99"))) {
//    post.cntHorseFlows++;
//    post.horseFlows[post.cntHorseFlows] = "T-NumberIndicators="+post.m_trnfactors+flowBet;
//   }
   }
   if (Truline.userProps.getProperty("ArtAndKim", "N").equals("Y"))
    setTrainerJockeyLast25(race, post);
  }
  return;
 }
 
 /**
  * Lookup trainer-jockey performance over last 25 races (or 90 days)
  */
 private static void setTrainerJockeyTYPFactor(Race race, Post post)
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("\n  Remove T-J TYP if pair has a non-matching SD" + race.m_raceNo + "\n");

  String race_date = race.m_props.getProperty("RACEDATE");
  // Find Trainer Jockey Factors
  for (Enumeration c = Truline.t4.elements(); c.hasMoreElements();) {
   Properties prop = (Properties) c.nextElement();
   String track = prop.getProperty("TRACK");
   String trainer = prop.getProperty("TRAINER");
   String jockey = prop.getProperty("JOCKEY");
   String start_date = prop.getProperty("START");
   String stop_date = prop.getProperty("STOP");
   String surface = prop.getProperty("SURFACE");
   String distance = prop.getProperty("DISTANCE");
   int activeT4 = race_date.compareTo(start_date);
   int activeT4X = -1;
   if (stop_date != null)
    activeT4X = race_date.compareTo(stop_date);
   if (activeT4 > -1 && activeT4X < 0)
    if (track == null || track.equals("All") || (track != null && track.equals(race.m_track))
      && trainer.equals(post.m_trainerName) && jockey.equals(post.m_jockeyName)) {
     if (distance != null || surface != null) {
      post.m_trnJkyfactorsTYP = "N";
      return;
     }
    }
  }

 }

 /**
  * Lookup trainer-jockey performance over last 25 races (or 90 days)
  */
 private static void setTrainerJockeyLast25(Race race, Post post)
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("\n  Set Track-specific Trainer and Jockey Performance Over Last 25 Races" + race.m_raceNo + "\n");
  
  String ITMpct = "";
  String ITMroi = "";

  // Find Trainer Jockey Stats for Last 25 races
  for (Enumeration c = Truline.t5.elements(); c.hasMoreElements();) {
   Properties prop = (Properties) c.nextElement();
   String track = prop.getProperty("TRACK");
   String trainer = prop.getProperty("TRAINER");
   String jockey = prop.getProperty("JOCKEY");
   double races5 = Lib.atof(prop.getProperty("RACESL5"));
   double pct5 = Lib.atof(prop.getProperty("PCTL5"));
   double roi5 = Lib.atof(prop.getProperty("ROIL5"));
   double races10 = Lib.atof(prop.getProperty("RACESL10"));
   double pct10 = Lib.atof(prop.getProperty("PCTL10"));
   double roi10 = Lib.atof(prop.getProperty("ROIL10"));
   double wins5 = races5 * pct5 / 100;
   double wins10 = races10 * pct10 / 100;
   if (track.equals(race.m_track) && trainer.equals(post.m_trainerName) && jockey.equals(post.m_jockeyName)) {
    if (roi5 > 2.24 && pct5 > 23 && wins5 > 1) {
     if (race.cntRaceFlows < 20) {
      race.cntRaceFlows++;
      race.raceFlows[race.cntRaceFlows] = String.format("%-22s", post.cloth+" "+post.m_horseName)+" Trainer-Jockey Last 5 "+trainer+"+"+jockey;
     }
/*
     if (race.cntRaceFlowsAK < 20 && Truline.userProps.getProperty("ArtAndKim", "N").equals("Y")) {
      race.cntRaceFlowsAK++;
      race.raceFlowsAK[race.cntRaceFlowsAK] = String.format("%-22s", post.cloth+" "+post.m_horseName)+" Trainer-Jockey Last 5 "+trainer+"+"+jockey;
     }
*/
    }
    if (roi10 > 2.24 && pct10 > 23 && wins10 > 2 && races10 > races5) {
     if (race.cntRaceFlows < 20) {
      race.cntRaceFlows++;
      race.raceFlows[race.cntRaceFlows] = String.format("%-22s", post.cloth+" "+post.m_horseName)+" Trainer-Jockey Last 10 "+trainer+"+"+jockey;
     }
/*
     if (race.cntRaceFlowsAK < 20 && Truline.userProps.getProperty("ArtAndKim", "N").equals("Y")) {
      race.cntRaceFlowsAK++;
      race.raceFlowsAK[race.cntRaceFlowsAK] = String.format("%-22s", post.cloth+" "+post.m_horseName)+" Trainer-Jockey Last 10 "+trainer+"+"+jockey;
     }
*/
    }
    if (roi5 > 2.24 && pct5 > 23 && wins5 > 1) 
     post.m_trnJkyTrkL25Stat5 = "\\b "+prop.getProperty("RACESL5")+"/"+prop.getProperty("PCTL5")+"%/$"+prop.getProperty("ROIL5")+"/"+prop.getProperty("ITML5")+"%/$"+prop.getProperty("ITMROIL5")+" \\b0";      
    else 
     if (post.m_last25)
      post.m_trnJkyTrkL25Stat5 = prop.getProperty("RACESL5")+"/"+prop.getProperty("PCTL5")+"%/$"+prop.getProperty("ROIL5")+"/"+prop.getProperty("ITML5")+"%/$"+prop.getProperty("ITMROIL5");
    if (races10 > races5 && (post.m_last25 || (roi5 > 2.24 && pct5 > 23 && wins5 > 1))) {
     if (roi10 > 2.24 && pct10 > 23) 
      post.m_trnJkyTrkL25Stat10 = "\\b "+prop.getProperty("RACESL10")+"/"+prop.getProperty("PCTL10")+"%/$"+prop.getProperty("ROIL10")+"/"+prop.getProperty("ITML10")+"%/$"+prop.getProperty("ITMROIL10")+" \\b0";      
     else 
      post.m_trnJkyTrkL25Stat10 = prop.getProperty("RACESL10")+"/"+prop.getProperty("PCTL10")+"%/$"+prop.getProperty("ROIL10")+"/"+prop.getProperty("ITML10")+"%/$"+prop.getProperty("ITMROIL10");     
    }
   }
  }

 }
 /**
  * Set the track-specific trainer-jockey percentages and test for flow bet level
  */
 private static String setTrainerJockeyPercents(Race race, Post post)
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("\n  Set Track-specific Trainer and Jockey Percentages" + race.m_raceNo + "\n");

  // Always compute and display the current T-J win percent if > 0
  post.m_kimsT2 = "";
  int tstart, twin, jstart, jwin;
  tstart = Lib.atoi(post.m_props.getProperty("TRAINERSTARTS"));
  twin = Lib.atoi(post.m_props.getProperty("TRAINERWINS"));
  jstart = Lib.atoi(post.m_props.getProperty("JOCKEYSTARTS"));
  jwin = Lib.atoi(post.m_props.getProperty("JOCKEYWINS"));
  int jpcnt = (jstart > 0) ? (jwin) * 100 / jstart : 0;
  int tpcnt = (tstart > 0) ? (twin) * 100 / tstart : 0;
  if (tstart >= 10)
   post.m_trnPct = tpcnt;
  else
   post.m_trnPct = 10;
  if (jstart >= 10)
   post.m_jkyPct = jpcnt;
  else
   post.m_jkyPct = 10;    
  int tjcurrpct = jpcnt+tpcnt;
  if (tjcurrpct > 0){
   race.cntHorseFlows++;
   post.cntHorseFlows++;
   if (tjcurrpct > 49) {
    post.horseFlows[post.cntHorseFlows] = "\\b TrnJkyCurrMeet="+tpcnt+"+"+jpcnt+"="+tjcurrpct+" \\b0";
    post.m_kimsT2 = "\\b T2 \\b0";
   }
   else {
    post.horseFlows[post.cntHorseFlows] = "TrnJkyCurrMeet="+tpcnt+"+"+jpcnt+"="+tjcurrpct;
    post.m_kimsT2 = "T2";
   }
  }
  

  
  // set TJ percent for standard handicap
  String post_trnJkyPct = "TJ0";
  post.m_kimsT4 = "";
  String surfaceDist = "";
  String race_surface = race.m_surface;
  int trnPct = 0;
  int jkyPct = 0;
  int trnJkyTotal = 0;
  if (race.m_distance > 1759)
   surfaceDist = race.m_surface+"R";
  else
   surfaceDist = race.m_surface+"S";

  // Find Trainer Percentage
  for (Enumeration c = Truline.tt.elements(); c.hasMoreElements();) {
   Properties prop = (Properties) c.nextElement();
   String track = prop.getProperty("TRACK");
   if (track.substring(2).equals("X"))
    track = track.substring(0, 2);
   String trainer = prop.getProperty("TRAINER");
   if (track.equals(race.m_track) && trainer.equals(post.m_trainerName)) {
    switch (surfaceDist) {
     case "DS":
      post_trnJkyPct = "TJ"+prop.getProperty("DS");
      trnJkyTotal += Lib.atoi(prop.getProperty("DS", "0"));
      trnPct = Lib.atoi(prop.getProperty("DS", "0"));
      break;
     case "DR":
      post_trnJkyPct = "TJ"+prop.getProperty("DR");
      trnJkyTotal += Lib.atoi(prop.getProperty("DR", "0"));
      trnPct = Lib.atoi(prop.getProperty("DR", "0"));
      break;
     case "TS":
      post_trnJkyPct = "TJ"+prop.getProperty("TS");
      trnJkyTotal += Lib.atoi(prop.getProperty("TS", "0"));
      trnPct = Lib.atoi(prop.getProperty("TS", "0"));
      break;
     case "TR":
      post_trnJkyPct = "TJ"+prop.getProperty("TR");
      trnJkyTotal += Lib.atoi(prop.getProperty("TR", "0"));
      trnPct = Lib.atoi(prop.getProperty("TR", "0"));
      break;
     case "AS":
      post_trnJkyPct = "TJ"+prop.getProperty("AS");
      trnJkyTotal += Lib.atoi(prop.getProperty("AS", "0"));
      trnPct = Lib.atoi(prop.getProperty("AS", "0"));
      break;
     case "AR":
      post_trnJkyPct = "TJ"+prop.getProperty("AR");
      trnJkyTotal += Lib.atoi(prop.getProperty("AR", "0"));
      trnPct = Lib.atoi(prop.getProperty("AR", "0"));
      break;
    }
   break;
   }
  }

  // Find Jockey Percentage
  boolean foundJky = false;
  for (Enumeration c = Truline.jt.elements(); c.hasMoreElements();) {
   Properties prop = (Properties) c.nextElement();
   String track = prop.getProperty("TRACK");
   if (track.substring(2).equals("X"))
    track = track.substring(0, 2);
   String jockey = prop.getProperty("JOCKEY");
   if (track.equals(race.m_track) && jockey.equals(post.m_jockeyName)) {
    switch (surfaceDist) {
     case "DS":
      post_trnJkyPct = post_trnJkyPct+"/"+prop.getProperty("DS");
      trnJkyTotal += Lib.atoi(prop.getProperty("DS", "0"));
      jkyPct = Lib.atoi(prop.getProperty("DS", "0"));
      break;
     case "DR":
      post_trnJkyPct = post_trnJkyPct+"/"+prop.getProperty("DR");
      trnJkyTotal += Lib.atoi(prop.getProperty("DR", "0"));
      jkyPct = Lib.atoi(prop.getProperty("DR", "0"));
      break;
     case "TS":
      post_trnJkyPct = post_trnJkyPct+"/"+prop.getProperty("TS");
      trnJkyTotal += Lib.atoi(prop.getProperty("TS", "0"));
      jkyPct = Lib.atoi(prop.getProperty("TS", "0"));
      break;
     case "TR":
      post_trnJkyPct = post_trnJkyPct+"/"+prop.getProperty("TR");
      trnJkyTotal += Lib.atoi(prop.getProperty("TR", "0"));
      jkyPct = Lib.atoi(prop.getProperty("TR", "0"));
      break;
     case "AS":
      post_trnJkyPct = post_trnJkyPct+"/"+prop.getProperty("AS");
      trnJkyTotal += Lib.atoi(prop.getProperty("AS", "0"));
      jkyPct = Lib.atoi(prop.getProperty("AS", "0"));
      break;
     case "AR":
      post_trnJkyPct = post_trnJkyPct+"/"+prop.getProperty("AR");
      trnJkyTotal += Lib.atoi(prop.getProperty("AR", "0"));
      jkyPct = Lib.atoi(prop.getProperty("AR", "0"));
      break;
    }
   foundJky = true;
   break;
   }
  }
  if (!foundJky)
   post_trnJkyPct = post_trnJkyPct+"/0";
  if (trnPct > 19) {
   if (trnJkyTotal >= Lib.atoi(Truline.userProps.getProperty("TrnJkyPercent", "99")))
    post.m_kimsT4 = "\\b T4 \\b0";
   else
    post.m_kimsT4 = "T4";
  }
  if (post.cntHorseFlows < 19 && trnJkyTotal >= Lib.atoi(Truline.userProps.getProperty("TrnJkyPercent", "99"))) {
   race.cntHorseFlows++;
   post.cntHorseFlows++;
   post.horseFlows[post.cntHorseFlows] = "\\b TrnJkyPercent="+trnPct+"+"+jkyPct+"="+trnJkyTotal+" \\b0";
  }

  return post_trnJkyPct;
 }
 /**
  * Set the currently hot trainer-jockey percentages and test for flow bet level
  */
 private static void identifyTrainerJockeys(Race race, Post post)
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("\n  Set Trainer and Jockey Pairs" + race.m_raceNo + "\n");
  post.m_trnJkyStat = "";
  post.m_trnJkyTrkStat = "";
  post.m_trnJkyTrkL25Stat5 = "";
  post.m_trnJkyTrkL25Stat10 = "";
  post.m_last25 = false;

  // Find Trainer Jockey Stat - eliminate for insiders
  if (Truline.userProps.getProperty("Experimental", "No").equals("No")) {
   for (Enumeration c = Truline.tj.elements(); c.hasMoreElements();) {
    Properties prop = (Properties) c.nextElement();
    // String track = prop.getProperty("TRACK");
    String trainer = prop.getProperty("TRAINER");
    String jockey = prop.getProperty("JOCKEY");
    String flowBet = prop.getProperty("FLOWBET");
    if (trainer.equals(post.m_trainerName) && jockey.equals(post.m_jockeyName)) {
     if (flowBet.equals("Y")) {
      post.m_trnJkyHot = "$";
      if (race.cntRaceFlows < 20) {
       race.cntRaceFlows++;
       race.raceFlows[race.cntRaceFlows] = String.format("%-22s", post.cloth+" "+post.m_horseName)+" National Trainer-Jockey "+trainer+"+"+jockey;
      }
     }
     else
      post.m_trnJkyHot = "*";
     if (Truline.userProps.getProperty("ArtAndKim", "N").equals("Y")) {
      if (flowBet.equals("Y"))
       post.m_trnJkyStat = "\\b "+prop.getProperty("RACES")+"/"+prop.getProperty("PCT")+"%/$"+prop.getProperty("ROI")+"/"+prop.getProperty("ITM")+"%/$"+prop.getProperty("ITMROI")+" \\b0";      
      else 
       post.m_trnJkyStat = prop.getProperty("RACES")+"/"+prop.getProperty("PCT")+"%/$"+prop.getProperty("ROI")+"/"+prop.getProperty("ITM")+"%/$"+prop.getProperty("ITMROI");     
      }
     else if (flowBet.equals("Y"))
       post.m_trnJkyStat = "\\b "+prop.getProperty("ODDS")+"/"+prop.getProperty("PCT")+"%/$"+prop.getProperty("ROI")+" \\b0";
      else
       post.m_trnJkyStat = " "+prop.getProperty("ODDS")+"/"+prop.getProperty("PCT")+"%/$"+prop.getProperty("ROI");
    }
   }
  }

  // Find 1-year Trainer Jockey Track Stat
  if (Truline.userProps.getProperty("Experimental", "No").equals("Yes")) {
   String tjVersion = Truline.userProps.getProperty("TJVersion",
     "201505");
   int trnWinPct = Lib.atoi(Truline.userProps.getProperty("TrnWinPct", "12"));
   int jkyWinPct = Lib.atoi(Truline.userProps.getProperty("JkyWinPct", "12"));
   for (Enumeration c = Truline.t2.elements(); c.hasMoreElements();) {
    Properties prop = (Properties) c.nextElement();
    String version = prop.getProperty("VERSION");
    String track = prop.getProperty("TRACK");
    String trainer = prop.getProperty("TRAINER");
    String jockey = prop.getProperty("JOCKEY");
    String flowBet = prop.getProperty("FLOWBET");
    double roi = Lib.atof(prop.getProperty("ROI"));
    // version.equals(tjVersion) && 
    if (track.equals(race.m_track) && trainer.equals(post.m_trainerName) && jockey.equals(post.m_jockeyName)) {
     if (roi > 1.99) 
      post.m_trnJky = "$";
     else
      post.m_trnJky = "*";
     if (Truline.userProps.getProperty("ArtAndKim", "N").equals("Y")) {
      if (flowBet.equals("Y") && post.m_trnPct >= trnWinPct && post.m_jkyPct >= jkyWinPct) {
       if (race.cntRaceFlowsAK < 20 && Truline.userProps.getProperty("ArtAndKim", "N").equals("Y")) {
        // if (post.m_morningLineD >= 3.5 && post.m_morningLineD <= 8) {
         race.cntRaceFlowsAK++;
         race.raceFlowsAK[race.cntRaceFlowsAK] = String.format("%-22s", post.cloth+" "+post.m_horseName)+" Track Trainer-Jockey "+trainer+"+"+jockey;
        // }
       }
      }
      if (roi > 1.99) 
       post.m_trnJkyTrkStat = "\\b "+prop.getProperty("ODDS")+"/"+prop.getProperty("PCT")+"%/$"+prop.getProperty("ROI")+"/"+prop.getProperty("ITM")+"%/$"+prop.getProperty("ITMROI")+" \\b0";
      else
       post.m_trnJkyTrkStat = " "+prop.getProperty("ODDS")+"/"+prop.getProperty("PCT")+"%/$"+prop.getProperty("ROI")+"/"+prop.getProperty("ITM")+"%/$"+prop.getProperty("ITMROI");
     }
     else if (flowBet.equals("Y")) {
      if (race.cntRaceFlows < 20) {
       race.cntRaceFlows++;
       race.raceFlows[race.cntRaceFlows] = String.format("%-22s", post.cloth+" "+post.m_horseName)+" 1-year Trainer-Jockey "+trainer+"+"+jockey;
       post.m_last25 = true;
      }
     }
     if (roi > 1.99) 
      post.m_trnJkyTrkStat = "\\b "+prop.getProperty("ODDS")+"/"+prop.getProperty("PCT")+"%/$"+prop.getProperty("ROI")+" \\b0";
     else
      post.m_trnJkyTrkStat = " "+prop.getProperty("ODDS")+"/"+prop.getProperty("PCT")+"%/$"+prop.getProperty("ROI");
    }
   }
  }

  return;
 }
 /**
  * Set the trainer-owner percentages and test for flow bet level
  */
 private static void identifyTrainerOwners(Race race, Post post)
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("\n  Set Trainer and Owner Pairs" + race.m_raceNo + "\n");
  post.m_trnOwnStat = "";
  // Find Trainer Owner Stat - now is track-specific
  
  for (Enumeration c = Truline.to.elements(); c.hasMoreElements();) {
   Properties prop = (Properties) c.nextElement();
   String track = prop.getProperty("TRACK");
   String trainer = prop.getProperty("TRAINER");
   String owner = prop.getProperty("OWNER");
   String flowBet = prop.getProperty("FLOWBET");
   double roi = Lib.atof(prop.getProperty("ROI"));
   if (track.equals(race.m_track) && trainer.equals(post.m_trainerName) && owner.equals(post.m_ownerName)) {
    if (flowBet.equals("Y")){
     if (race.cntRaceFlows < 20) {
      race.cntRaceFlows++;
      race.raceFlows[race.cntRaceFlows] = String.format("%-22s", post.cloth+" "+post.m_horseName)+" 2-year Trainer-Owner "+trainer+"+"+owner;
     }
    }
    if (post.m_trnOwnStat.equals(""))
     if (roi > 1.99) {
      post.m_ownerTrn = "$";
      post.m_trnOwnStat = "\\b "+prop.getProperty("ODDS")+"/"+prop.getProperty("PCT")+"%/$"+prop.getProperty("ROI")+" \\b0";      
     }
     else {
      post.m_ownerTrn = "-";
      post.m_trnOwnStat = " "+prop.getProperty("ODDS")+"/"+prop.getProperty("PCT")+"%/$"+prop.getProperty("ROI");
     }
    else
     if (roi > 1.99) {
      post.m_ownerTrn = "$";
      post.m_trnOwnStat = post.m_trnOwnStat + "\\b /"+prop.getProperty("ODDS")+"/"+prop.getProperty("PCT")+"%/$"+prop.getProperty("ROI")+" \\b0";
     }
     else {
      post.m_ownerTrn = "-";
      post.m_trnOwnStat = post.m_trnOwnStat + "/"+prop.getProperty("ODDS")+"/"+prop.getProperty("PCT")+"%/$"+prop.getProperty("ROI");
     }
    return;
   }
  }

  return;
 }
 /**
  * Set the trainer percentages for surface 
  */
 private static void identifyTrainerSurfaceStat(Race race, Post post)
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("\n  Set Trainer Surface Stats" + race.m_raceNo + "\n");
  post.m_trnSurfaceStat = "";
  // post.m_trnPct = 0;
  // Find Trainer Surface Stat 
  for (Enumeration c = Truline.t3.elements(); c.hasMoreElements();) {
   Properties prop = (Properties) c.nextElement();
   String trainer = prop.getProperty("TRAINER");
   String surface = prop.getProperty("SURFACE");
   double roi = Lib.atof(prop.getProperty("ROI"));
   if (trainer.equals(post.m_trainerName) && surface.equals(race.m_surface)) {
    int trnPct = Lib.atoi(prop.getProperty("WINPCT"));
    if (Truline.userProps.getProperty("ArtAndKim", "N").equals("Y") && trnPct >= 25 && roi > 1.99) {
     post.m_trnSurfaceStat = "\\b "+prop.getProperty("RACES")+"/"+prop.getProperty("WINPCT")+"%/$"+prop.getProperty("ROI")+"/"+prop.getProperty("ITMPCT")+"%/$"+prop.getProperty("ITMROI")+" *** BF *** \\b0";
     if (Truline.userProps.getProperty("ArtAndKim", "N").equals("Y") && race.cntRaceFlows < 20) {
      race.cntRaceFlows++;
      race.raceFlows[race.cntRaceFlows] = String.format("%-22s", post.cloth+" "+post.m_horseName)+" Trainer Surface Stats="+prop.getProperty("RACES")+"/"+prop.getProperty("WINPCT")+"%/$"+prop.getProperty("ROI")+"/"+prop.getProperty("ITMPCT")+"%/$"+prop.getProperty("ITMROI")+" *** BF ***";;
     }
    }
    else if (roi > 1.99) 
     post.m_trnSurfaceStat = "\\b "+prop.getProperty("RACES")+"/"+prop.getProperty("WINPCT")+"%/$"+prop.getProperty("ROI")+" \\b0";      
     else 
      post.m_trnSurfaceStat = prop.getProperty("RACES")+"/"+prop.getProperty("WINPCT")+"%/$"+prop.getProperty("ROI");      
     return;
   }
  }
  
  return;
 }
 /**
  * Set the trainer percentages for last 60 days 
  */
 private static void identifyTrainerMeetStat(Race race, Post post)
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("\n  Set Trainer Meet Stats" + race.m_raceNo + "\n");
  post.m_trnMeetStatD = "";
  post.m_trnMeetStatS = "";
  post.m_trnPctM = 0;
  // Find Trainer Meet StatS 
  for (Enumeration c = Truline.tm.elements(); c.hasMoreElements();) {
   Properties prop = (Properties) c.nextElement();
   String track = prop.getProperty("TRACK");
   String trainer = prop.getProperty("TRAINER");
   String factor = prop.getProperty("FACTOR");
   String flowBet = prop.getProperty("FLOWBET");
   int winPct = Lib.atoi(prop.getProperty("WINPCT"));
   double roi = Lib.atof(prop.getProperty("ROI"));
   int ITMPct = Lib.atoi(prop.getProperty("ITMPCT","0"));
   double ITMroi = Lib.atof(prop.getProperty("ITMROI","0"));
   if (track.equals(race.m_track) && trainer.equals(post.m_trainerName)) {
    if ((factor.equals("RT") && race.m_distance > 1759) || 
      (factor.equals("SP") && race.m_distance < 1760)) {
     if (Truline.userProps.getProperty("ArtAndKim", "N").equals("Y")) {
      if (roi > 1.99) 
       post.m_trnMeetStatD = "\\b "+prop.getProperty("RACES")+"/"+prop.getProperty("WINPCT")+"%/$"+prop.getProperty("ROI")+"/"+prop.getProperty("ITMPCT")+"%/$"+prop.getProperty("ITMROI")+" \\b0";      
      else 
       post.m_trnMeetStatD = prop.getProperty("RACES")+"/"+prop.getProperty("WINPCT")+"%/$"+prop.getProperty("ROI")+"/"+prop.getProperty("ITMPCT")+"%/$"+prop.getProperty("ITMROI");     
     }
     else if (roi > 1.99) 
       post.m_trnMeetStatD = "\\b "+prop.getProperty("RACES")+"/"+prop.getProperty("WINPCT")+"%/$"+prop.getProperty("ROI")+" \\b0";      
      else 
       post.m_trnMeetStatD = prop.getProperty("RACES")+"/"+prop.getProperty("WINPCT")+"%/$"+prop.getProperty("ROI");      
     if (flowBet.equals("Y")) {
      if (race.cntRaceFlows < 20) {
       race.cntRaceFlows++;
       race.raceFlows[race.cntRaceFlows] = String.format("%-22s", post.cloth+" "+post.m_horseName)+" Trainer "+trainer+" 60-day Meet Distance "+prop.getProperty("WINPCT")+"%/$"+prop.getProperty("ROI");
      }
     }
    }
    if (factor.equals(race.m_surface)) { 
     post.m_trnPctM = winPct;
     post.m_trnROIM = roi;
     // if (winPct > post.m_trnPct)
     //  post.m_trnPct = winPct;
     if (Truline.userProps.getProperty("ArtAndKim", "N").equals("Y")) {
      if (roi > 1.99) 
       post.m_trnMeetStatS = "\\b "+prop.getProperty("RACES")+"/"+prop.getProperty("WINPCT")+"%/$"+prop.getProperty("ROI")+"/"+prop.getProperty("ITMPCT")+"%/$"+prop.getProperty("ITMROI")+" \\b0";      
      else 
       post.m_trnMeetStatS = prop.getProperty("RACES")+"/"+prop.getProperty("WINPCT")+"%/$"+prop.getProperty("ROI")+"/"+prop.getProperty("ITMPCT")+"%/$"+prop.getProperty("ITMROI");     
     }
     else if (roi > 1.99) 
       post.m_trnMeetStatS = "\\b "+prop.getProperty("RACES")+"/"+prop.getProperty("WINPCT")+"%/$"+prop.getProperty("ROI")+" \\b0";      
      else 
       post.m_trnMeetStatS = prop.getProperty("RACES")+"/"+prop.getProperty("WINPCT")+"%/$"+prop.getProperty("ROI");      
     if (flowBet.equals("Y")) {
      if (race.cntRaceFlows < 20) {
       race.cntRaceFlows++;
       race.raceFlows[race.cntRaceFlows] = String.format("%-22s", post.cloth+" "+post.m_horseName)+" Trainer "+trainer+" 60-day Meet Surface "+prop.getProperty("WINPCT")+"%/$"+prop.getProperty("ROI");
      }
     }
    }
   }
  }
  
  return;
 }
 /**
  * Set the jockey percentages for last 60 days 
  */
 private static void identifyJockeyMeetStat(Race race, Post post)
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("\n  Set Jockey Meet Stats" + race.m_raceNo + "\n");
  post.m_jkyMeetStatD = "";
  post.m_jkyMeetStatS = "";
  // Find Jockey Meet Stat 
  for (Enumeration c = Truline.jm.elements(); c.hasMoreElements();) {
   Properties prop = (Properties) c.nextElement();
   String track = prop.getProperty("TRACK");
   String jockey = prop.getProperty("JOCKEY");
   String factor = prop.getProperty("FACTOR");
   String flowBet = prop.getProperty("FLOWBET");
   int winPct = Lib.atoi(prop.getProperty("WINPCT"));
   double roi = Lib.atof(prop.getProperty("ROI"));
   int ITMPct = Lib.atoi(prop.getProperty("ITMPCT","0"));
   double ITMroi = Lib.atof(prop.getProperty("ITMROI","0"));
   if (track.equals(race.m_track) && jockey.equals(post.m_jockeyName)) {
    if ((factor.equals("RT") && race.m_distance > 1759) || 
      (factor.equals("SP") && race.m_distance < 1760)) {
     if (Truline.userProps.getProperty("ArtAndKim", "N").equals("Y")) {
      if (roi > 1.99) 
       post.m_jkyMeetStatD = "\\b "+prop.getProperty("RACES")+"/"+prop.getProperty("WINPCT")+"%/$"+prop.getProperty("ROI")+"/"+prop.getProperty("ITMPCT")+"%/$"+prop.getProperty("ITMROI")+" \\b0";      
      else 
       post.m_jkyMeetStatD = prop.getProperty("RACES")+"/"+prop.getProperty("WINPCT")+"%/$"+prop.getProperty("ROI")+"/"+prop.getProperty("ITMPCT")+"%/$"+prop.getProperty("ITMROI");     
      }
     else if (roi > 1.99) {
      post.m_jkyMeetStatD = "\\b "+prop.getProperty("RACES")+"/"+prop.getProperty("WINPCT")+"%/$"+prop.getProperty("ROI")+" \\b0";      
      }
      else {
       post.m_jkyMeetStatD = prop.getProperty("RACES")+"/"+prop.getProperty("WINPCT")+"%/$"+prop.getProperty("ROI");      
      }
     if (flowBet.equals("F")) {
      if (race.cntRaceFlows < 20) {
       race.cntRaceFlows++;
       race.raceFlows[race.cntRaceFlows] = String.format("%-22s", post.cloth+" "+post.m_horseName)+" Jockey "+jockey+" 60-day Meet Distance "+prop.getProperty("WINPCT")+"%/$"+prop.getProperty("ROI");
      }
     }
    }
    if (factor.equals(race.m_surface)) { 
     if (Truline.userProps.getProperty("ArtAndKim", "N").equals("Y")) {
      if (roi > 1.99) 
       post.m_jkyMeetStatS = "\\b "+prop.getProperty("RACES")+"/"+prop.getProperty("WINPCT")+"%/$"+prop.getProperty("ROI")+"/"+prop.getProperty("ITMPCT")+"%/$"+prop.getProperty("ITMROI")+" \\b0";      
      else 
       post.m_jkyMeetStatS = prop.getProperty("RACES")+"/"+prop.getProperty("WINPCT")+"%/$"+prop.getProperty("ROI")+"/"+prop.getProperty("ITMPCT")+"%/$"+prop.getProperty("ITMROI");     
     }
     else if (roi > 1.99) {
      post.m_jkyMeetStatS = "\\b "+prop.getProperty("RACES")+"/"+prop.getProperty("WINPCT")+"%/$"+prop.getProperty("ROI")+" \\b0";      
      }
      else {
       post.m_jkyMeetStatS = prop.getProperty("RACES")+"/"+prop.getProperty("WINPCT")+"%/$"+prop.getProperty("ROI");      
      }
     if (flowBet.equals("F")) {
      if (race.cntRaceFlows < 20) {
       race.cntRaceFlows++;
       race.raceFlows[race.cntRaceFlows] = String.format("%-22s", post.cloth+" "+post.m_horseName)+" Jockey "+jockey+" 60-day Meet Surface ";
      }
     }
    }
   }
  }
  
  return;
 }
 
 /**
  * Identify trainer stats from Equibase
  */
 private static void identifyEquibaseTrainerStats(Race race,Post post)
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("\n  Identify Equibase Trainer Stats" + race.m_raceNo + "/" + post.cloth + "\n");
  post.m_trainerClsChgDownOK = false;
  post.m_trainerClm1bk = false;
  String cat = "";
  int sts = 0;
  int win = 0;
  int itm = 0;
  double roi = 0;
  for (Enumeration e1 = post.m_trainerJockeyStats.elements(); e1
    .hasMoreElements();) {
   TrainerJockeyStats tjs = (TrainerJockeyStats) e1.nextElement();
   
   for (int k = 1; k < 7; k++) {
    cat = tjs.m_props.getProperty("TRAINERCAT"+k, "N/A");
    win = Lib.atoi(tjs.m_props.getProperty("TRAINERWIN"+k, "0"));
    itm = Lib.atoi(tjs.m_props.getProperty("TRAINERITM"+k, "0"));
    roi = Lib.atof(tjs.m_props.getProperty("TRAINERROI"+k, "0"));
    if (cat.indexOf("Claimed 1bk") >= 0)
     post.m_trainerClm1bk = true;
    if ((!cat.equals("N/A") && (cat.indexOf("Track") <= 0)) 
      && ((roi > 1.99) && (win >= 25)) || (win >= 30)) {

     if (cat.indexOf("Claimed 1bk") >= 0)
      if (race.cntRaceFlows < 20) {
       race.cntRaceFlows++;
       race.raceFlows[race.cntRaceFlows] = String.format("%-22s", post.cloth+" "+post.m_horseName)+" Claimed 1 back " 
         + Lib.pad(Lib.ftoa((int) win,0)+'%', 6)
         + Lib.pad(Lib.ftoa((int) itm,0)+'%', 6)
         + Lib.pad("$"+Lib.ftoa((double) roi, 2), 7)+" *** BF ***";
      }

     if (cat.indexOf("Dwn 20Pct") >= 0) {
      post.m_trainerClsChgDownROI = roi;
      post.m_trainerClsChgDownOK = true;
      if (race.cntRaceFlows < 20) {
       race.cntRaceFlows++;
       race.raceFlows[race.cntRaceFlows] = String.format("%-22s", post.cloth+" "+post.m_horseName)+" Class Down " 
        // race.raceFlows[race.cntRaceFlows] = Lib.pad(post.cloth+" "+post.m_horseName,22)+" Class Down " 
         + Lib.pad(Lib.ftoa((int) win,0)+'%', 6)
         + Lib.pad(Lib.ftoa((int) itm,0)+'%', 6)
         + Lib.pad("$"+Lib.ftoa((double) roi, 2), 7)+" *** BF ***";
      }
     }
      
     if (cat.indexOf("1stStart45") >= 0) {
      post.m_trainer45LayoffROI = roi;
      if (win > 24 || (win > 14 && roi > 2))
        post.m_trainer45LayoffOK = true;
     }
     
     if (cat.indexOf("3rdStart45") >= 0) {
      if (race.cntRaceFlows < 20) {
       race.cntRaceFlows++;
       race.raceFlows[race.cntRaceFlows] = String.format("%-22s", post.cloth+" "+post.m_horseName)+" 3rd Start > 45 days " 
         + Lib.pad(Lib.ftoa((int) win,0)+'%', 6)
         + Lib.pad(Lib.ftoa((int) itm,0)+'%', 6)
         + Lib.pad("$"+Lib.ftoa((double) roi, 2), 7)+" *** BF ***";
      }
     }
      
     if (cat.indexOf("Track") >= 0 && (((roi > 1.99) && (win >= 15)) || (win >= 30))) {
      if (race.cntRaceFlows < 20) {
       race.cntRaceFlows++;
       race.raceFlows[race.cntRaceFlows] = String.format("%-22s", post.cloth+" "+post.m_horseName)+" "+cat+" " 
         + Lib.pad(Lib.ftoa((int) win,0)+'%', 6)
         + Lib.pad(Lib.ftoa((int) itm,0)+'%', 6)
         + Lib.pad("$"+Lib.ftoa((double) roi, 2), 7);
      }
     }
     
      
     /*
     if (race.cntRaceFlows < 20) {
      race.cntRaceFlows++;
      race.raceFlows[race.cntRaceFlows] = Lib.pad(post.cloth+" "+post.m_horseName,22)+" Equibase Trainer Stat "
        + Lib.pad(cat, 18)
        + Lib.pad(tjs.m_props.getProperty("TRAINERSTS"+k, " "), 6)
        + Lib.pad(Lib.ftoa((int) win,0)+'%', 6)
        + Lib.pad("$"+Lib.ftoa((double) roi, 2), 7);
     }
       */
    }
   }
  }

  return;
 }

   /**
   * Identify if the race qualifies as a race flow bet
   */
  private static void identifyRaceFlowBets(Race race)
  {
   if (Log.isDebug(Log.TRACE))
    Log.print("\n  Identify Race Flow Bets in race #" + race.m_raceNo + "\n");
  String race_surface = race.m_surface;
  String distance = "";
  String surface = "";
  String raceType = "";
  String betType = "";
  String factors = "";
  String raceFlow = "";
  int cnt = 0;
  for (Enumeration c = Truline.rf.elements(); c.hasMoreElements();) {
   Properties prop = (Properties) c.nextElement();
   String track = prop.getProperty("TRACK");
   if (track.substring(2).equals("X"))
    track = track.substring(0, 2);
   distance = prop.getProperty("DISTANCE");
   surface = prop.getProperty("SURFACE");
   raceType = prop.getProperty("RACETYPE");
   betType = prop.getProperty("BETTYPE");
   factors = prop.getProperty("FACTORS");
   raceFlow = betType + "  " + factors;
   if (track.substring(2).equals("X"))
    track = track.substring(0, 2);
   if (track != null && track.equals(race.m_track)) {
    if (distance == null
      || ((distance.equals("RT") && race.m_distance > 1759) || (distance
        .equals("SP") && race.m_distance < 1760))) {
     if (surface == null || surface.equals(race_surface)) {
      if ((raceType == null || raceType.equals(race.m_raceType))
        || (raceType.equals("G") && race.m_purse >= 100000)
        || (raceType.equals("A") && race.m_purse < 100000 && (!race.m_raceType
          .equals("M") && !race.m_raceType.equals("S") && !race.m_raceType
           .equals("C")))) {
       if (race.cntRaceFlows < 4 && race.m_cnthorses > 6 && race.m_cntnrl < 4
         && race.m_cntnrlML == 0) {
        race.cntRaceFlows++;
        race.raceFlows[race.cntRaceFlows] = raceFlow;
       }
      }
     }
    }
   }
  }
  return;
 }
  /**
  * Identify if the race qualifies as a race flow bet
  */
 private static void identifyBonusFactors(Race race)
 {
  if (Log.isDebug(Log.TRACE))
   Log.print("\n  Identify Bonus Factors in race #" + race.m_raceNo + "\n");
  for (Enumeration e = race.m_posts.elements(); e.hasMoreElements();) {
   Post post = (Post) e.nextElement();
   String scratch = post.m_props.getProperty("ENTRY", "");
   if (post.m_horseName == null || scratch.equals("S"))
    continue; // position is empty or scratched

   if (!post.m_lastRacePurseComp.equals("")) 
    if (post.m_lastRacePurseComp.indexOf("CLASS DOWN") >= 0 && post.m_trainerClsChgDownOK && post.m_trainerClsChgDownROI > 1.99)
     if (race.cntRaceFlows < 20) {
      race.cntRaceFlows++;
      race.raceFlows[race.cntRaceFlows] = String.format("%-22s", post.cloth+" "+post.m_horseName)+" "+post.m_lastRacePurseComp + "- Trainer ROI is "+Lib.ftoa(post.m_trainerClsChgDownROI, 2)+" *** BF ***";
     }

   if (post.m_trnJkyPctF > post.m_trnPctF+5 && post.m_trnJkyPctF > post.m_jkyPctF+5
     && post.m_trnPctF > 20 && post.m_jkyPctF > 15)
    if (race.cntRaceFlows < 20) {
     race.cntRaceFlows++;
     race.raceFlows[race.cntRaceFlows] = String.format("%-22s", post.cloth+" "+post.m_horseName)+" Top Trainer-Top Jockey Team - T-J="+post.m_trnJkyPctF+"% / T="+post.m_trnPctF+"% / J="+post.m_jkyPctF+"%   *** BF ***";
    }

   if (post.m_jockeyTodayPrevWin)
    if (race.cntRaceFlows < 20) {
     race.cntRaceFlows++;
     race.raceFlows[race.cntRaceFlows] = String.format("%-22s", post.cloth+" "+post.m_horseName)+" Jockey Change - Today's Jockey won previously on horse   *** BF ***";
    }

  }
   return;
 }
  /**
   * See if horse has been scratched
   */
  private static boolean horseScratched(Race race, Post post)
  {
   Boolean scratched = false; 
   for (Enumeration c = Truline.sc.elements(); c.hasMoreElements();) {
    Properties prop = (Properties) c.nextElement();
    String track = prop.getProperty("TRACK");
    String date = prop.getProperty("DATE");
    int raceNo = Lib.atoi(prop.getProperty("RACENO"));
    String horseName = prop.getProperty("HORSENAME").toUpperCase();
    String scrReason = prop.getProperty("REASON");
    if (race.m_track.equals(track) && race.m_props.getProperty("RACEDATE", "").equals(date)
        && race.m_raceNo == raceNo && post.m_horseName.equals(horseName))
     scratched = true;
    }
   return scratched;
   }


/* Utility routine to calculate days between two dates  */
public int getDifferenceDays(Date d1, Date d2) {
 int daysdiff=0;
 long diff = d2.getTime() - d1.getTime();
 long diffDays = diff / (24 * 60 * 60 * 1000)+1;
 daysdiff = (int) diffDays;
 return daysdiff;
}

}

