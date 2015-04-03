package com.base;
/**
 *	BRIS data parser
 */
import java.util.*;
import java.util.zip.*;
import java.io.*;

import com.mains.Truline;

public class Bris
{
 public Vector<Race> m_races = new Vector<Race>();
 /**
  * Constructor
  */
 public Bris() {
 }
 public boolean load(String file, String filename)
 {
  if (file.indexOf('\\') == -1) {
   // no path given, use the base address if known.
   String base = Truline.userProps.getProperty("DATADIR");
   if (base != null) {
    file = base + file;
   }
  }
  if (!parseDrf(file, filename))
   return false;
  if (!parseDr2(file, filename))
   return false;
  if (!parseDr3(file, filename))
   return false;
  if (!parseDr4(file, filename))
   return false;
  return true;
 }
 public String[] names1   = {
                          // Data structure for the comma delimited DRF PP's in
                          // the "Multi File" format:
                          //
                          // MAX.
                          // FIELD # DESCRIPTION TYPE FORMAT LENGTH COMMENTS
                          //
                          // Beginning of 1st of 3 files
                          //
                          // *** Today's Race Data ***
                          //
                          // k 1 Track CHARACTER XXX 3
   "TRACKABBR",
   // k 2 Date CHARACTER XXXXXXXX 8
   "RACEDATE",
   // k 3 Race # NUMERIC 99 2
   "RACENO",
   // 4 Distance (in yards) NUMERIC 9999 4
   // Negative value for about distances, convert yds to furlongs.
   "DISTANCE",
   // 5 Surface CHARACTER X 1 D- Dirt
   // T- Turf
   // d- Dirt Inner
   // t- Turf Inner
   // s- Steeplechase
   // h- Hunt
   "SURFACE",
   // 6 Reserved
   "ALLWEATHER",
   // 7 Race Type CHARACTER XX G1-Stake I
   // G2-Stake II
   // G3-Stake III
   // N -nongraded stake
   // A -allowance
   // R -Starter Alw
   // T -Starter Hcp
   // C -claiming
   // S -mdn sp wt
   // M -mdn claimer
   "RACETYPE",
   // 8 Age/Sex Restrictions CHARACTER XXX 3 see codes below
   "AGESEX",
   // 9 Reserved
   "",
   // 10 Purse NUMERIC 99999999 8
   "PURSE",
   // 11 Claiming Price NUMERIC 9999999 7
   "CLAIMPRICE",
   // 12 Track Record NUMERIC 999.99 6 seconds &
   // hundredths
   "TRACKRECORD",
   // 13 Race Conditions CHARACTER 500
   "COMMENTS"
                          // 14 Today's Lasix list CHARACTER 400
                          // 15 Today's Bute list CHARACTER 400
                          // 16 Today's Coupled list CHARACTER 200
                          // 17 Today's Mutuel list CHARACTER 200
                          // 18 Simulcast host track code CHARACTER XXX 3
                          // (actual track
                          // code if not
                          // a simulcast)
                          // 19 Simulcast host track race # NUMERIC 99 2 (actual
                          // race # if
                          // not a simulcast)
                          // 20-24 Blank fields reserved for possible future
                          // expansion
                          };
 public String[] names2   = {
                          // Beginning of 2nd of 3 files*** Today's
                          // Trainer/Jockey/Owner ***
   // k 1 Track CHARACTER XXX 3
   "TRACKABBR",
   // k 2 Date CHARACTER XXXXXXXX 8
   "RACEDATE",
   // k 3 Race # NUMERIC 99 2
   "RACENO",
   // k 4 Post Position NUMERIC 99 2
   "POSTPOSTION",
   // 5 Entry CHARACTER X 1 A- part of A entry
   // B- part of B entry
   // C- part of C entry
   // F- part of FIELD
   // S- if scratched
   "ENTRY",
   // 6 Claiming Price (of horse) NUMERIC 9999999 7 blank if N.A.
   "CLAIMINGPURSE",
   // 7 Today's Trainer CHARACTER 30
   "TRAINER",
   // 8 Trainer Sts Current Meet NUMERIC 9999 4
   "TRAINERSTARTS",
   // 9 Trainer Wins Current Meet NUMERIC 999 3
   "TRAINERWINS",
   // 10 Trainer Places Current Meet NUMERIC 999 3
   "TRAINERPLACES",
   // 11 Trainer Shows Cureent Meet NUMERIC 999 3
   "TRAINERSHOWS",
   // 12 Today's Jockey CHARACTER 25
   "JOCKEY",
   // 13 Apprentice wgt allow.(if any) NUMERIC 99 2
   "WEIGHTALLOW",
   // 14 Jockey Sts Current Meet NUMERIC 9999 4
   "JOCKEYSTARTS",
   // 15 Jockey Wins Current Meet NUMERIC 999 3
   "JOCKEYWINS",
   // 16 Jockey Places Current Meet NUMERIC 999 3
   "JOCKEYPLACES",
   // 17 Jockey Shows Current Meet NUMERIC 999 3
   "JOCKEYSHOWS",
   // 18 Today's Owner CHARACTER 40
   "OWNER",
   // 19 Trainer Record Current Year CHARACTER 28 ???
   "TRAINERTHISYR",
   // 20 Trainer Record Previous Year CHARACTER 28 ???
   "TRAINERLASTYR",
   // 21 Jockey Record Current Year CHARACTER 28 ???
   "JOCKEYTHISYR",
   // 22 Jockey Record Previous Year CHARACTER 28 ???
   "JOCKEYLASTYR",
   // 23 Blank field reserved for possible future expansion
   "",
   // *** Horse History Data ***
   // 24 Horse Name CHARACTER 25
   "HORSENAME",
   // 25 Year of Birth NUMERIC 99 2
   "YEARBORN",
   // 26 Horse's Foaling Month NUMERIC 99 2 (1 for JAN
   // 12 for DEC)
   "MONTHBORN",
   // 27 Reserved
   "",
   // 28 Sex CHARACTER X 1
   "SEX",
   // 29 Horse's color CHARACTER 5
   "COLOR",
   // 30 Weight NUMERIC 999 3
   "WEIGHT",
   // 31 Sire CHARACTER 25
   "SIRE",
   // 32 Sire's sire CHARACTER 25
   "SIRESSIRE",
   // 33 Dam CHARACTER 25
   "DAM",
   // 34 Dam's sire CHARACTER 25
   "DAMSSIRE",
   // 35 Breeder CHARACTER 67
   "BREEDER",
   // 36 State/Country abrv. where bredCHARACTER 5
   "WHEREBRED",
   // 37-41 Blank fields reserved for possible future expansion
   "", "", "", "", "",
   // *** Current Horse Stats ***
   // 42 Today's Medication NUMERIC 1 (0=None, 1=Lasix,
   // 2=Bute,
   // 3=Bute & Lasix
   // 9=Medication info
   // unavailable)
   "MEDICATION",
   // 43 Equipment Change NUMERIC 1 (0=No change,
   // 1=Blinkers on,
   // 2=Blinkers off
   // 9=Equipment info
   // unavailable)
   "EQUIPMENTCHG",
   // Horse's Lifetime Record @ Today's Distance:
   // 44 Starts NUMERIC 999 3
   "LRDSTARTS",
   // 45 Wins NUMERIC 99 2
   "LRDWINS",
   // 46 Places NUMERIC 99 2
   "LRDPLACES",
   // 47 Shows NUMERIC 99 2
   "LRDSHOWS",
   // 48 Earnings NUMERIC 99999999 8
   "LRDEARNINGS",
   // Horse's Lifetime Record @ Today's track:
   // 49 Starts NUMERIC 999 3
   "LRTSTARTS",
   // 50 Wins NUMERIC 99 2
   "LRTWINS",
   // 51 Places NUMERIC 99 2
   "LRTPLACES",
   // 52 Shows NUMERIC 99 2
   "LRTSHOWS",
   // 53 Earnings NUMERIC 99999999 8
   "LRTEARNINGS",
   // Horse's Lifetime Turf Record:
   // 54 Starts NUMERIC 999 3
   "LRTURFSTARTS",
   // 55 Wins NUMERIC 99 2
   "LRTURFWINS",
   // 56 Places NUMERIC 99 2
   "LRTURFPLACES",
   // 57 Shows NUMERIC 99 2
   "LRTURFSHOWS",
   // 58 Earnings NUMERIC 99999999 8
   "LRTURFEARNINGS",
   // Horse's Lifetime Wet Record:
   // 59 Starts NUMERIC 999 3
   "LRWETSTARTS",
   // 60 Wins NUMERIC 99 2
   "LRWETWINS",
   // 61 Places NUMERIC 99 2
   "LRWETPLACES",
   // 62 Shows NUMERIC 99 2
   "LRWETSHOWS",
   // 63 Earnings NUMERIC 99999999 8
   "LRWETEARNINGS",
   // Horse's Latest Year Record: (most recent year the horse last ran)
   // 64 Year NUMERIC 9999 4 (ie. 1994)
   "LATESTYEAR",
   // 65 Starts NUMERIC 99 2
   "LATESTYEARSTARTS",
   // 66 Wins NUMERIC 99 2
   "LATESTYEARWINS",
   // 67 Places NUMERIC 99 2
   "LATESTYEARPLACES",
   // 68 Shows NUMERIC 99 2
   "LATESTYEARSHOWS",
   // 69 Earnings NUMERIC 99999999 8
   "LATESTYEAREARNINGS",
   // Horse's Previous Year Record:
   // (2nd most recent year in which the horse ran)
   // 70 Year NUMERIC 9999 4 (ie. 1993)
   "PREVIOUSYEAR",
   // 71 Starts NUMERIC 99 2
   "PREVIOUSYEARSTARTS",
   // 72 Wins NUMERIC 99 2
   "PREVIOUSYEARWINS",
   // 73 Places NUMERIC 99 2
   "PREVIOUSYEARPLACES",
   // 74 Shows NUMERIC 99 2
   "PREVIOUSYEARSHOWS",
   // 75 Earnings NUMERIC 99999999 8
   "PREVIOUSYEAREARNINGS",
   // Horse's Lifetime Record:
   // 76 Starts NUMERIC 999 3
   "LIFETIMESTARTS",
   // 77 Wins NUMERIC 999 3
   "LIFETIMEWINS",
   // 78 Places NUMERIC 999 3
   "LIFETIMEPLACES",
   // 79 Shows NUMERIC 999 3
   "LIFETIMESHOWS",
   // 80 Earnings NUMERIC 99999999 8
   "LIFETIMEEARNINGS",
   // 81 Date of Workout #1 DATE 99999999 8 CYMD
   // 82 #2 83 #3
   // 84 #4 85 #5
   // 86 #6 87 #7
   // 88 #8 89 #9
   // 90 #10 91 #11
   // 92 #12
   "WORKDATE",
   // 93 Time of Workout #1 NUMERIC 9999.99 7 seconds &
   // hundredths
   // 94 #2 Negative time if a
   // 95 #3 "bullet" work
   // 96 #4 (ie. -34.80 means
   // 97 #5 a bullet work in
   // 98 #6 a time of 34 4/5)
   // 99 #7
   // 100 #8
   // 101 #9 102 #10
   // 103 #11 104 #12
   "WORKTIME",
   // 105 Track of Workout #1 CHARACTER 10
   // 106 #2 107 #3
   // 108 #4 109 #5
   // 110 #6 111 #7
   // 112 #8 113 #9
   // 114 #10 115 #11
   // 116 #12
   "WORKTRACK",
   // 117 Distance of Workout #1 NUMERIC 99999 5 (Dist. in yards)
   // 118 #2 (- value for
   // 119 #3 about distances)
   // 120 #4 121 #5
   // 122 #6 123 #7
   // 124 #8 125 #9
   // 126 #10 127 #11
   // 128 #12
   "WORKDISTANCE",
   // 129 Track Condition of Workout #1 CHARACTER XX 2
   // 130 #2 131 #3
   // 132 #4 133 #5
   // 134 #6 135 #7
   // 136 #8 137 #9
   // 138 #10 139 #11
   // 140 #12
   "WORKCONDITION",
   // 141 Description of Workout #1 CHARACTER XXX 3
   // 142 #2
   // 143 #3 1st Character: H or B
   // 144 #4 H for Handily B for Breezing
   // 145 #5
   // 146 #6 2nd Character: g
   // 147 #7 if worked from gate
   // 148 #8
   // 149 #9 3rd Character: D
   // 150 #10 if 'Dogs are up'
   // 151 #11 152 #12
   "WORKDESCR",
   // 153 Main/Inner track indicator #1 CHARACTER XX 1 MT-main dirt
   // 154 #2 IM-inner dirt
   // 155 #3 TT-Trning Trk
   // 156 #4 T-main turf
   // 157 #5 IT-inner turf
   // 158 #6 WC-wood chip
   // 159 #7 HC-hillside course
   // 160 #8 TN-trf trn trk
   // 161 #9 IN-inner trf trn
   // track
   // 162 #10 TR-training race
   // 163 #11 -if blank, track
   // 164 #12 type unknown
   "WORKSURFACE",
   // # of Works that 165 day/distance #1
   // 166 #2 167 #3
   // 168 #4 169 #5
   // 170 #6 171 #7
   // 172 #8 173 #9
   // 174 #10 175 #11
   // 176 #12
   "WORKQTY",
   // "Rank" of the work among
   // 177 other works that day/dist #1 178 #2
   // 179 #3 180 #4
   // 181 #5 182 #6
   // 183 #7 184 #8
   // 185 #9 186 #10
   // 187 #11 188 #12
   "WORKRANK",
   // 189 BRIS Run Style designation CHARACTER XXX 3
   "RUNSTYLE",
   // 190 "Quirin" Style speed points NUMERIC 9 1
   "QUIRIN",
   // 191-192 Reserved RRCRP
   "", "",
   // 193 BRIS 2f Pace Fig Par for level NUMERIC 999 3
   "PASEPAR2F",
   // 194 BRIS 4f Pace Fig Par for level NUMERIC 999 3
   "PASEPAR4F",
   // 195 BRIS 6f Pace Fig Par for level NUMERIC 999 3
   "PASEPAR6F",
   // 196 BRIS SpeedFig Par for class level NUMERIC 999 3
   "SPEEDPAR",
   // 197 BRIS Late Pace Par for level NUMERIC 999 3
   "LATEPAR",
   // 198-199 Reserved
   "", "",
   // 200 Program Post Position NUMERIC 999 3
   "PROGRAMPOSTPOS",
   // 201 Program Number CHARACTER 3
   "PROGRAMNUMBER",
   // 202 Morning Line Odds NUMERIC 999.99 6
   "MORNINGLINE",
   // 203 # of days since last race NUMERIC 9999 4
   "DAYSSINCELAST",
   // 204-209 DRF race condition lines
   "COMMENTS", "", "", "", "", "",
                          // conditions lines CHARACTER 254 Sometimes blank
                          // because data is
                          // not always
                          // available...
   // 210  Lifetime Starts - All Weather Surface        999      3
   // 211  Lifetime Wins - All Weather Surface          999      3
   // 212  Lifetime Places - All Weather Surface        999      3
   // 213  Lifetime Shows - All Weather Surface         999      3
   // 214  Lifetime Earnings - All Weather Surface 99999999      8
   "LRAWESTARTS",
   "LRAWEWINS",
   "LRAWEPLACES",
   "LRAWESHOWS",
   "LRAWEEARNINGS"
   // 215  Best BRIS Speed - All Weather Surface        999      3
   // 216 Reserved...
   // 217     "Low" Claiming Price         NUMERIC  9999999      7
   //           (for today's race)
   // 218     Statebred flag               CHARACTER      X      1
   //           (for today's race)
   // 219-227 Wager Types for this race    CHARACTER   X(50)    50  (if available)
   // 228     Sire Stud Fee (current)      NUMERIC  9999999      7  (if available)
   // 229     Best BRIS Speed - Fast track NUMERIC      999      3
   // 230     Best BRIS Speed - Turf       NUMERIC      999      3
   // 231     Best BRIS Speed - Off track  NUMERIC      999      3
   // 232     Best BRIS Speed - Distance   NUMERIC      999      3
   // 233-234 Reserved for possible future expansion
   //
   //                   *** END of second file  ***
                          };
 // Beginning of 3rd file of the 3 files
 public String[] names3   = {
                          // k 1 Track CHARACTER XXX 3
   "TRACKABBR",
   // k 2 Date CHARACTER XXXXXXXX 8
   "RACEDATE",
   // k 3 Race # NUMERIC 99 2
   "RACENO",
   // k 4 Post Position NUMERIC 99 2
   "POSTPOSITION",
   // k 5 Previous Race Date CHARACTER XXXXXXXX 8
   "PPRACEDATE",
   // 6 # of days since previous race NUMERIC 9999 4 Blank- First timer
   // NOTE: # days since prev. race for 10th race back is not provided
   // ( blank )
   "DAYSSINCEPREVRACE",
   // 7 Track Code CHARACTER 30
   "PPTRACK",
   // 8 BRIS Track Code CHARACTER XXX 3
   "PPTRACKABBR",
   // 9 Race # NUMERIC 99 2
   "PPRACENO",
   // 10 Track Condition CHARACTER XX 2
   "CONDITION",
   // 11 Distance (in yards) NUMERIC 99999 5 (-value for about
   // distances)
   "PPDISTANCE",
   // 12 Surface CHARACTER X 1 see field #7
   "PPSURFACE",
   // 13 Special Chute indicator CHARACTER X 1 c - chute
   "CHUTE",
   // 14 # of entrants NUMERIC 99 2
   "ENTRANTS",
   // 15 Post Position NUMERIC 99 2
   "PPPOSTPOSITION",
   // 16 Equipment CHARACTER X 1 b - blinkers
   "BLINKERS",
   // 17 Reserved
   "",
   // 18 Medication NUMERIC 9 1 0=None, 1=Lasix,
   // 2=Bute,
   //
   //
   // 3=Bute & Lasix
   "MEDICATION",
   // 19 Trip Comment CHARACTER 100
   "COMMENT",
   // 20 Winner's Name CHARACTER
   "WINNER",
   // 21 2nd Place finishers Name CHARACTER
   "PLACER",
   // 22 3rd Place finishers Name CHARACTER
   "SHOWER",
   // 23 Winner's Weight carried NUMERIC 999
   "WINWEIGHT",
   // 24 2nd Place Weight carried NUMERIC 999
   "PLACEWEIGHT",
   // 25 3rd Place Weight carried NUMERIC 999
   "SHOWWEIGHT",
   // 26 Winner's Margin NUMERIC 99.99 ( 0 if DeadHeat )
   "WINMARGIN",
   // 27 2nd Place Margin NUMERIC 99.99 ( 0 if DeadHeat )
   "PLACEMARGIN",
   // 28 3rd Place Margin NUMERIC 99.99 ( 0 if DeadHeat )
   "SHOWMARGIN",
   // 29 Alternate/Extra Comment line CHARACTER 200 (includes "claimed
   // from" text & misc.
   // other comments )
   "COMMENT2",
   // 30 Weight NUMERIC 999 3
   "PPWEIGHT",
   // 31 Odds NUMERIC 9999.99 7
   "ODDS",
   // 32 Entry CHARACTER X 1 e - entry
   "PPENTRY",
   // 33 Race Classification CHARACTER 25
   "RACECLASSIFICATION",
   // 34 Claiming Price (of horse) NUMERIC 9999999 7
   "PPCLAIMPRICE",
   // 35 Purse                         NUMERIC    99999999  8
   "PPPURSE",
   // 36 Start Call Position CHARACTER 2
   "POSITION1",
   // 37 1st Call Position(if any) CHARACTER 2
   "POSITION2",
   // 38 2nd Call Position(if any) CHARACTER 2
   "POSITION3",
   // 39 Reserved
   "POSITION4",
   // 40 Stretch Position (if any) CHARACTER 2
   "POSITION5",
   // 41 Finish Position CHARACTER 2
   "POSITION6",
   // 42 Money Position CHARACTER 2
   "POSITION7",
   // Position number or:
   // A-bled B- bolted
   // C-broke down
   // D-distanced
   // E-dwelt F- eased
   // G-fell H- lame
   // I-left at post
   // J-left course
   // K-lost rider
   // L-running postions
   // omitted because
   // of weather
   // conditions
   // M-propped
   // N-sulked O- sore
   // P-refused to break
   // Q-pulled up
   // R-wheeled
   // S-saddle slipped
   // T-lost irons
   // U-beaten off
   // V-reared W- bucked
   // X-did not finish
   // Y-unplaced
   // *-unspecified
   // reason for
   // missed call
   // 43 Reserved
   "",
   // 44 Reserved
   "",
   // 45 1st Call BtnLngths/Ldr margin NUMERIC 99.99 5
   "LENLDRMGN1",
   // 46 1st Call BtnLngths only
   "LENGTHS1",
   // 47 2nd Call BtnLngths/Ldr margin NUMERIC 99.99 5
   "LENLDRMGN2",
   // 48 2nd Call BtnLngths only
   "LENGTHS2",
   // 49 Reserved
   "",
   // 50 Reserved
   "",
   // 51 Stretch BtnLngths/Ldr margin NUMERIC 99.99 5
   "LENLDRMGN3",
   // 52 Stretch BtnLngths only
   "LENGTHS3",
   // 53 Finish BtnLngths/Wnrs marginNUMERIC 99.99 5
   "LENLDRMGN4",
   // 54 Finish BtnLngths only
   "LENGTHS4",
   // 55 BRIS Race Shape - 2nd Call NUMERIC 999 3
   "RACESHAPE2",
   // 56 BRIS 2f Pace Fig NUMERIC 999 3
   "PACE2F",
   // 57 BRIS 4f Pace Fig NUMERIC 999 3
   "PACE4F",
   // 58 BRIS 6f Pace Fig NUMERIC 999 3
   "PACE6F",
   //
   //
   // 59 BRIS 8f Pace Fig NUMERIC 999 3
   "PACE8F",
   // 60 BRIS 10f Pace Fig NUMERIC 999 3
   "PACE10F",
   // 61 BRIS Late Pace Fig NUMERIC 999 3
   "PACELATE",
   // 55 BRIS Race Shape - 1st Call NUMERIC 999 3
   "RACESHAPE1",
   // 63 Reserved
   "",
   // 64 BRIS Speed Rating NUMERIC 999 3
   "BRISSPEED",
   // 65 DRF Speed Rating NUMERIC 999 3
   "DRFSPEED",
   // 66 DRF Track Variant NUMERIC 99 2
   "VARIENT",
   // Fractional times are the times of the leader, not this horse.
   // 67 2f Fraction (if any) NUMERIC 999.99 6 seconds &
   // hundredths
   "FRACTION2F",
   // 68 3f Fraction (if any) NUMERIC 999.99 6
   "FRACTION3F",
   // 69 4f Fraction (if any) NUMERIC 999.99 6
   "FRACTION4F",
   // 70 5f Fraction (if any) NUMERIC 999.99 6
   "FRACTION5F",
   // 71 6f Fraction (if any) NUMERIC 999.99 6
   "FRACTION6F",
   // 72 7f Fraction (if any) NUMERIC 999.99 6
   "FRACTION7F",
   // 73 8f Fraction (if any) NUMERIC 999.99 6
   "FRACTION8F",
   // 74 10f Fraction (if any) NUMERIC 999.99 6
   "FRACTION10F",
   // 75 12f Fraction (if any) NUMERIC 999.99 6
   "FRACTION12F",
   // 76 14f Fraction (if any) NUMERIC 999.99 6
   "FRACTION14F",
   // 77 16f Fraction (if any) NUMERIC 999.99 6
   "FRACTION16F",
   // 78 Fraction #1 NUMERIC 999.99 6
   "FRACTION1",
   // 79 #2 NUMERIC 999.99 6
   "FRACTION2",
   // 80 #3 NUMERIC 999.99 6
   "FRACTION3",
   // 81 Reserved
   "",
   // 82 Reserved
   "",
   // 83 Final Time NUMERIC 999.99 6 seconds &
   // hundredths
   "FINALTIME",
   // 84 Claimed code CHARACTER X 1 c - claimed
   "CLAIMED",
   // 85 Trainer (if available) CHARACTER X 30
   "PPTRAINER",
   // 86 Jockey CHARACTER 25
   "PPJOCKEY",
   // 87 Apprentice Wt allow (if any) NUMERIC 99 2
   "WEIGHTALLOW",
   // 88 Race Type CHARACTER XX 2 (G1,G2,G3,N,A,
   // R,T,C,S,M)
   // see field #7
   "PPRACETYPE",
   //
   // 89 Age and Sex Restrictions CHARACTER XXX 3 see codes below
   "PPAGESEX",
   // 90 Statebred flag CHARACTER X 1 s-statebred
   "PPSTATEBRED",
   // 91 Restricted/Qualifier flag CHARACTER X 1 R-restricted
   // Q-qualifier
   // O-optional claimer
   "RESTRICTED",
   // 92 Favorite indicator NUMERIC 9 1 0-Non-favorite
   // 1-Favorite
   "FAVORITE",
   // 93 Front Bandages indicator NUMERIC 9 1 0-No Front Wraps
   // 1-Front Wraps
   "FRONTBANDAGE"
                          // 94-122 29 Blank fields (for possible future
                          // additions)
                          //
                          // k- This field is a "key field" used to link the
                          // appropriate records of
                          // the three different files.
                          //
                          // Age/Sex Restriction Codes (3 character sting):
                          //
                          // 1st character
                          // -------------
                          // A - 2 year olds
                          // B - 3 year olds
                          // C - 4 year olds
                          // D - 5 year olds
                          // E - 3 & 4 year olds
                          // F - 4 & 5 year olds
                          // G - 3, 4, and 5 year olds
                          // H - all ages
                          //
                          //
                          //
                          // 2nd character
                          // -------------
                          // O - That age Only
                          // U - That age and Up
                          //
                          // 3rd character
                          // -------------
                          // N - No Sex Restrictions
                          // M - Mares and Fillies Only
                          // C - Colts and/or Geldings Only
                          // F - Fillies Only
                          //
                          // Example: "BON" - means a "3 year olds only" race
                          // with no sex restrictions
                          //
                          };
 // *** Beginning of 4th file ***
 public String[] names4   = {
                          // k 1 Track CHARACTER XXX 3
   "TRACKABBR",
   // k 2 Date CHARACTER XXXXXXXX 8
   "RACEDATE",
   // k 3 Race # NUMERIC 99 2
   "RACENO",
   // k 4 Post Position NUMERIC 99 2
   "POSTPOSITION",
   // 5 BRIS Prime Power Rating NUMERIC 999.99 6
   // 6 BRIS Dirt Pedigree Rating CHARACTER XXXX 4 eg. "115*"
   // 7 BRIS Mud Pedigree Rating CHARACTER XXXX 4
   // 8 BRIS Turf Pedigree Rating CHARACTER XXXX 4
   // 9 BRIS Dist Pedigree Rating CHARACTER XXXX 4
   // 10 Best BRIS Speed:Life NUMERIC 999 3
   // 11 Best BRIS Speed:MostRecentYr NUMERIC 999 3
   // 12 Best BRIS Speed:2ndMstRcntYr NUMERIC 999 3
   // 13 Best BRIS Speed:Track NUMERIC 999 3
   // 14 # Starts (FAST Dirt) NUMERIC 999 3
   "", "", "", "", "", "", "", "", "",
   // 15 # Wins (FAST Dirt) NUMERIC 99 2
   // 16 # Places (FAST Dirt) NUMERIC 99 2
   // 17 # Shows (FAST Dirt) NUMERIC 99 2
   // 18 Earnings (FAST Dirt) NUMERIC 99999999 8
   "LRDIRTSTARTS",
   "LRDIRTWINS",
   "LRDIRTPLACES",
   "LRDIRTSHOWS",
   "LRDIRTEARNINGS",
   // 19 Key Trnr Stat Category #1 CHARACTER X(16) 16
   "TRAINERCAT1",
   // 20 # of starts #1 NUMERIC 9999 4
   "TRAINERSTS1",
   // 21 Win% #1 NUMERIC 999.99 6
   "TRAINERWIN1",
   // 22 in-the-money (itm)% #1 NUMERIC 999.99 6
   "TRAINERITM1",
   // 23 $2ReturnOnInvestment #1 NUMERIC 999.99 6
   "TRAINERROI1",
   // 24 Key Trnr Stat Category #2 CHARACTER X(16) 16
   "TRAINERCAT2",
   // 25 # of starts #2 NUMERIC 9999 4
   "TRAINERSTS2",
   // 26 Win% #2 NUMERIC 999.99 6
   "TRAINERWIN2",
   // 27 in-the-money (itm)% #2 NUMERIC 999.99 6
   "TRAINERITM2",
   // 28 $2ReturnOnInvestment #2 NUMERIC 999.99 6
   "TRAINERROI2",
   // 29 Key Trnr Stat Category #3 CHARACTER X(16) 16
   "TRAINERCAT3",
   // 30 # of starts #3 NUMERIC 9999 4
   "TRAINERSTS3",
   // 31 Win% #3 NUMERIC 999.99 6
   "TRAINERWIN3",
   // 32 in-the-money (itm)% #3 NUMERIC 999.99 6
   "TRAINERITM3",
   // 33 $2ReturnOnInvestment #3 NUMERIC 999.99 6
   "TRAINERROI3",
   // 34 Key Trnr Stat Category #4 CHARACTER X(16) 16
   "TRAINERCAT4",
   // 35 # of starts #4 NUMERIC 9999 4
   "TRAINERSTS4",
   // 36 Win% #4 NUMERIC 999.99 6
   "TRAINERWIN4",
   // 37 in-the-money (itm)% #4 NUMERIC 999.99 6
   "TRAINERITM4",
   // 38 $2ReturnOnInvestment #4 NUMERIC 999.99 6
   "TRAINERROI4",
   // 39 Key Trnr Stat Category #5 CHARACTER X(16) 16
   "TRAINERCAT5",
   // 40 # of starts #5 NUMERIC 9999 4
   "TRAINERSTS5",
   // 41 Win% #5 NUMERIC 999.99 6
   "TRAINERWIN5",
   // 42 in-the-money (itm)% #5 NUMERIC 999.99 6
   "TRAINERITM5",
   // 43 $2ReturnOnInvestment #5 NUMERIC 999.99 6
   "TRAINERROI5",
   // 44 Key Trnr Stat Category #6 CHARACTER X(16) 16
   "TRAINERCAT6",
   // 45 # of starts #6 NUMERIC 9999 4
   "TRAINERSTS6",
   // 46 Win% #6 NUMERIC 999.99 6
   "TRAINERWIN6",
   // 47 in-the-money (itm)% #6 NUMERIC 999.99 6
   "TRAINERITM6",
   // 48 $2ReturnOnInvestment #6 NUMERIC 999.99 6
   "TRAINERROI6",
   // 49 Reserved
   "",
   // 50 JKY@Distance/Turf Label CHARACTER X(13) 13
   "JOCKEYDISTCAT",
   // 51 JKY@Distance/Turf Starts NUMERIC 9999 4
   "JOCKEYDISTSTS",
   // 52 JKY@Distance/Turf Wins NUMERIC 9999 4
   "JOCKEYDISTWIN",
   // 53 JKY@Distance/Turf Places NUMERIC 9999 4
   "JOCKEYDISTPLC",
   // 54 JKY@Distance/Turf Shows NUMERIC 9999 4
   "JOCKEYDISTSHW",
   // 55 JKY@Distance/Turf ROI NUMERIC 999.99 6
   "JOCKEYDISTROI",
   // 56 JKY@Distance/Turf Earnings NUMERIC 999999999 9
   "JOCKEYDISTERN"
                          // 57-254 Reserved for future use
                          //
                          //
                          };
 public String[] namesScr = {
                          // Beginning of Scratch file **
   // k 1 Track CHARACTER XXX 3
   "TRACKABBR",
   // k 2 Date CHARACTER XXXXXXXX 8
   "RACEDATE",
   // k 3 Race # NUMERIC 99 2
   "RACENO",
   // k 4 Post Position NUMERIC 99 2
   "POSTPOSTION",
   // 5 Horse Name CHARACTER 25
   "HORSENAME"
                          // *** END of Scratches file ***
                          };
 /**
  * Parse the first file for races.
  */
 public boolean parseDrf(String file, String filename)
 {
  boolean status = true;
  String buffer;
  BufferedReader in = null;
  StreamTokenizer parser = null;
  try {
   in = openFile(file, ".drf");
  } catch (Exception e) {
   int i = file.indexOf('.');
   if (i > 0)
    file = file.substring(0, i);
   Truline.println("Could not open file - " + filename + ".drf\n   "
     + e.getMessage());
   Log
     .print("Could not open file - " + filename + ".drf\n   " + e.getMessage());
   return false;
  }
  Truline.println("Parsing " + filename + ".drf");
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
   Truline.println("Could not read file - " + filename + ".drf\n   "
     + e.getMessage());
   Log
     .print("Could not read file - " + filename + ".drf\n   " + e.getMessage());
   return false;
  }
  String value;
  int fld = 0;
  int c = StreamTokenizer.TT_EOL;
  Race race = new Race();
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
        race.m_props.put(name, parser.sval);
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
      if (race.m_props.size() > 0) {
       race.m_track = race.m_props.getProperty("TRACKABBR");
       value = race.m_props.getProperty("RACEDATE");
       race.m_raceDate = Lib.atoDate(value);
       value = value.substring(2, 4);
       race.m_raceYear = Lib.atoi(value);
       value = race.m_props.getProperty("RACENO");
       race.m_raceNo = Lib.atoi(value);
       // 4 Distance (in yards) NUMERIC 9999 4
       // Negative value for about distances.
       value = race.m_props.getProperty("DISTANCE");
       int distance = Lib.atoi(value);
       if (distance > 0)
        race.m_props.put("ABOUT", "N");
       else {
        race.m_props.put("ABOUT", "Y");
        distance = -distance;
       }
       race.m_props.put("DISTANCE", "" + distance);
       if (Log.isDebug(Log.PARSE1))
        Log.print("Race " + race.m_track + " " + Lib.datetoa(race.m_raceDate)
          + " #" + race.m_raceNo + "\n\n");
       // Put it in the list.
       m_races.addElement(race);
      }
      // Set up for the next race
      if (running) {
       race = new Race();
       fld = 0;
      }
      break;
    }
   } catch (Exception e) {
    // an error condition...
    System.out.println("Error in " + filename + ".DRF: line=" + parser.lineno()
      + " fld=" + fld + "\n   " + e);
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
  * Parse the three file set to generate the full data set.
  */
 public boolean parseDr2(String file, String filename)
 {
  boolean status = true;
  BufferedReader in = null;
  StreamTokenizer parser = null;
  try {
   in = openFile(file, ".dr2");
  } catch (Exception e) {
   int i = file.indexOf('.');
   if (i > 0)
    file = file.substring(0, i);
   Truline.println("Could not open file - " + filename + ".dr2\n   "
     + e.getMessage());
   Log
     .print("Could not open file - " + filename + ".dr2\n   " + e.getMessage());
   return false;
  }
  Truline.println("Parsing " + filename + ".dr2");
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
   Truline.println("Could not open file - " + filename + ".dr2\n   "
     + e.getMessage());
   Log
     .print("Could not open file - " + filename + ".dr2\n   " + e.getMessage());
   try {
    in.close();
   } catch (Exception e1) {
   }
   return false;
  }
  int fld = 0;
  int j = 0;
  int c = StreamTokenizer.TT_EOL;
  String w_morningLine = null;
  Post post = new Post();
  boolean running = true;
  while (running) {
   try {
    c = parser.nextToken();
    switch (c) {
     case '"':
     case StreamTokenizer.TT_WORD:
      if (fld < names2.length) {
       if (Log.isDebug(Log.PARSE2))
        Log.print(names2[fld] + "=" + parser.sval + "\n");
       if (fld > 22 && fld < 36) {
        // Horse related fields
        String name = names2[fld].trim();
        if (name.length() > 0)
         post.m_horse.m_props.put(name, parser.sval);
       } else if (names2[fld].startsWith("WORK")) {
        if (parser.sval != null && parser.sval.length() > 0) {
         if (names2[fld].equals("WORKDATE")) {
          post.m_work[j].m_workDate = Lib.atoDate(parser.sval);
         }
         post.m_work[j].m_props.put(names2[fld], parser.sval);
        }
       } else {
        String name = names2[fld].trim();
        if (name.length() > 0)
         post.m_props.put(name, parser.sval);
       }
      }
      break;
     case ',':
      if (fld < names2.length && names2[fld].startsWith("WORK")) {
       j++;
       if (j >= 12) {
        j = 0;
        fld++;
       }
      } else
       fld++;
      break;
     case StreamTokenizer.TT_EOF:
      running = false;
      // fall through
     case StreamTokenizer.TT_EOL:
      if (post.m_props.size() > 0) {
       post.m_track = post.m_props.getProperty("TRACKABBR");
       String value = post.m_props.getProperty("RACEDATE");
       post.m_raceDate = Lib.atoDate(value);
       value = post.m_props.getProperty("RACENO");
       post.m_raceNo = Lib.atoi(value);
       value = post.m_props.getProperty("POSTPOSTION");
       post.m_postPosition = Lib.atoi(value);
       post.cloth = post.m_props.getProperty("PROGRAMNUMBER");
       if (post.cloth == null)
        post.cloth = post.m_props.getProperty("POSTPOSITION");
       w_morningLine = post.m_props.getProperty("MORNINGLINE");
       // Log.print("Morning Line = "+w_morningLine+"\n\n");
       if (w_morningLine == null)
        post.m_morningLine = "";
       else {
        if (w_morningLine.substring(w_morningLine.length() - 2).equals("00"))
         w_morningLine = w_morningLine.substring(0, w_morningLine.length() - 3);
        else if (w_morningLine.substring(w_morningLine.length() - 1)
          .equals("0"))
         w_morningLine = w_morningLine.substring(0, w_morningLine.length() - 1);
        w_morningLine = w_morningLine + "-1";
        switch (w_morningLine) {
         case ".1-1":
          post.m_morningLine = "1-10";
          break;
         case ".2-1":
          post.m_morningLine = "1-5";
          break;
         case ".5-1":
          post.m_morningLine = "1-2";
          break;
         case ".6-1":
          post.m_morningLine = "3-5";
          break;
         case ".8-1":
          post.m_morningLine = "4-5";
          break;
         case "1.2-1":
          post.m_morningLine = "6-5";
          break;
         case "1.4-1":
          post.m_morningLine = "7-5";
          break;
         case "1.5-1":
          post.m_morningLine = "3-2";
          break;
         case "1.6-1":
          post.m_morningLine = "8-5";
          break;
         case "1.8-1":
          post.m_morningLine = "9-5";
          break;
         case "2.5-1":
          post.m_morningLine = "5-2";
          break;
         case "3.5-1":
          post.m_morningLine = "7-2";
          break;
         case "4.5-1":
          post.m_morningLine = "9-2";
          break;
         default:
          post.m_morningLine = w_morningLine;
          break;
        }
       }
       // Log.print("Morning Line 2 = "+post.m_morningLine+"\n\n");
       post.m_horseName = post.m_horse.m_props.getProperty("HORSENAME");
       post.m_sex = post.m_horse.m_props.getProperty("SEX");
       post.m_trainerName = post.m_props.getProperty("TRAINER");
       post.m_jockeyName = post.m_props.getProperty("JOCKEY");
       post.m_runStyle = post.m_props.getProperty("RUNSTYLE");
       post.m_quirin = Lib.atoi(post.m_props.getProperty("QUIRIN"));
       post.m_sireName = post.m_horse.m_props.getProperty("SIRE");
       int idx1 = post.m_sireName.indexOf("(");
       if (idx1 > 0)
        post.m_sireName = post.m_sireName.substring(0, idx1-1).trim();
       post.m_damName = post.m_horse.m_props.getProperty("DAM");
       idx1 = post.m_damName.indexOf("(");
       if (idx1 > 0)
        post.m_damName = post.m_damName.substring(0, idx1-1).trim();
       post.m_damSireName = post.m_horse.m_props.getProperty("DAMSSIRE");
       idx1 = post.m_damSireName.indexOf("(");
       if (idx1 > 0)
        post.m_damSireName = post.m_damSireName.substring(0, idx1-1).trim();
       post.m_sireSireName = post.m_horse.m_props.getProperty("SIRESSIRE", "");
       idx1 = post.m_sireSireName.indexOf("(");
       if (idx1 > 0)
        post.m_sireSireName = post.m_sireSireName.substring(0, idx1-1).trim();
       post.m_whereBred = post.m_horse.m_props.getProperty("WHEREBRED", "");
       post.m_weight = Lib.atoi(post.m_horse.m_props.getProperty("WEIGHT"));
       if (Log.isDebug(Log.PARSE2))
        Log.print("Post: " + post.m_track + " " + Lib.datetoa(post.m_raceDate)
          + " #" + post.m_raceNo + " post=" + post.m_postPosition + " "
          + post.m_horseName + " Trainer=" + post.m_trainerName + "\n\n");
       Race race = findRace(post.m_raceNo);
       if (race != null) {
        race.m_posts.addElement(post);
        post.m_age = race.m_raceYear
          - Lib.atoi(post.m_horse.m_props.getProperty("YEARBORN"));
        if (post.m_horseName.length() > 15)
         post.m_horseNameP = post.m_horseName.substring(0, 14) + '-'
           + post.m_age + post.m_sex;
        else
         post.m_horseNameP = post.m_horseName + '-' + post.m_age + post.m_sex;
       } else {
        System.out.println("DR2: line " + parser.lineno() + " Race="
          + post.m_raceNo + "  Not found");
        running = false;
        status = false;
       }
      }
      if (running) {
       j = 0;
       fld = 0;
       post = new Post();
      }
      break;
    }
   } catch (Exception e) {
    // an error condition...
    Truline.println("Error in " + filename + ".DR2 line=" + parser.lineno()
      + " fld=" + fld + "\n   " + e);
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
  * Parse the three file set to generate the full data set.
  */
 public boolean parseDr3(String file, String filename)
 {
  boolean status = true;
  BufferedReader in = null;
  StreamTokenizer parser = null;
  String value;
  try {
   in = openFile(file, ".dr3");
  } catch (Exception e) {
   int i = file.indexOf('.');
   if (i > 0)
    file = file.substring(0, i);
   Truline.println("Could not open file - " + filename + ".dr3\n   "
     + e.getMessage());
   Log
     .print("Could not open file - " + filename + ".dr3\n   " + e.getMessage());
   return false;
  }
  Truline.println("Parsing " + filename + ".dr3");
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
   Truline.println("Could not open file - " + filename + ".dr3\n   "
     + e.getMessage());
   Log
     .print("Could not open file - " + filename + ".dr3\n   " + e.getMessage());
   try {
    in.close();
   } catch (Exception e1) {
   }
   return false;
  }
  int fld = 0;
  int c = StreamTokenizer.TT_EOL;
  Performance per = new Performance();
  boolean running = true;
  while (running) {
   // if (parser.lineno() > 2)
   // System.exit(1);
   try {
    c = parser.nextToken();
    switch (c) {
     case '"':
     case StreamTokenizer.TT_WORD:
      if (fld < names3.length) {
       if (Log.isDebug(Log.PARSE3))
        Log.print(names3[fld] + "=" + parser.sval + "\n");
       per.m_props.put(names3[fld], parser.sval);
      }
      break;
     case ',':
      fld++;
      break;
     case StreamTokenizer.TT_EOF:
      running = false;
      // fall through
     case StreamTokenizer.TT_EOL:
      if (per.m_props.size() > 0) {
       per.m_track = per.m_props.getProperty("TRACKABBR");
       value = per.m_props.getProperty("RACEDATE");
       per.m_raceDate = Lib.atoDate(value);
       value = per.m_props.getProperty("RACENO");
       per.m_raceNo = Lib.atoi(value);
       value = per.m_props.getProperty("POSTPOSITION");
       per.m_postPosition = Lib.atoi(value);
       value = per.m_props.getProperty("PPDISTANCE");
       per.ppDistance = Lib.atoi(value);
       if (per.ppDistance > 0)
        per.m_props.put("ABOUT", "N");
       else {
        per.m_props.put("ABOUT", "Y");
        per.ppDistance = -per.ppDistance;
        per.m_props.put("PPDISTANCE", "" + per.ppDistance);
       }
       per.ppTrack = per.m_props.getProperty("PPTRACKABBR");
       if (per.ppTrack.substring(2).equals("X"))
        per.ppTrack = per.ppTrack.substring(0, 2);
       value = per.m_props.getProperty("PPRACEDATE");
       per.ppRaceDate = Lib.atoDate(value);
       value = per.m_props.getProperty("PPRACENO");
       per.ppRaceNo = Lib.atoi(value);
       value = per.m_props.getProperty("PPPOSTPOSITION");
       per.ppPostPosition = Lib.atoi(value);
       Post post = findPost(per.m_raceNo, per.m_postPosition);
       if (post != null)
        post.m_performances.addElement(per);
       else {
        System.out.println("Error " + file + ".DR3: Race=" + per.m_raceNo
          + " pos=" + per.m_postPosition + "; Post Not found");
        running = false;
        status = false;
       }
       // extract the purse from Race Classification
       String string = per.m_props.getProperty("RACECLASSIFICATION", "");
       for (int i = 0; i < string.length(); i++) {
        if (Character.isDigit(string.charAt(i))) {
         int purse = Lib.atoi(string.substring(i));
         if (purse >= 100) {
          per.m_props.put("PURSE", "" + purse);
          break;
         }
        }
       }
       if (Log.isDebug(Log.PARSE3))
        Log.print("Performance: " + per.ppTrack + " "
          + Lib.datetoa(per.ppRaceDate) + " #" + per.ppRaceNo + " post="
          + per.ppPostPosition + " " + post.m_horseName + "\n\n");
      }
      if (running) {
       fld = 0;
       per = new Performance();
      }
      break;
    }
   } catch (Exception e) {
    // an error condition...
    Truline.println("DR3 line=" + parser.lineno() + " fld=" + fld + ": " + e);
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
  * Parse the fourth file for trainer/jockey stats
  */
 public boolean parseDr4(String file, String filename)
 {
  boolean status = true;
  BufferedReader in = null;
  StreamTokenizer parser = null;
  String value = "";
  try {
   in = openFile(file, ".dr4");
  } catch (Exception e) {
   int i = file.indexOf('.');
   if (i > 0)
    file = file.substring(0, i);
   Truline.println("Could not open file - " + filename + ".dr4\n   "
     + e.getMessage());
   Log
     .print("Could not open file - " + filename + ".dr4\n   " + e.getMessage());
   return false;
  }
  Truline.println("Parsing " + filename + ".dr4");
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
   Truline.println("Could not open file - " + filename + ".dr4\n   "
     + e.getMessage());
   Log
     .print("Could not open file - " + filename + ".dr4\n   " + e.getMessage());
   try {
    in.close();
   } catch (Exception e1) {
   }
   return false;
  }
  int fld = 0;
  int c = StreamTokenizer.TT_EOL;
  TrainerJockeyStats tjs = new TrainerJockeyStats();
  boolean running = true;
  while (running) {
   try {
    c = parser.nextToken();
    switch (c) {
     case '"':
     case StreamTokenizer.TT_WORD:
      if (fld < names4.length) {
       if (Log.isDebug(Log.PARSE3))
        Log.print(names4[fld] + "=" + parser.sval + "\n");
       if (names4[fld].length() > 0) {
        if (names4[fld].substring(0,3).equals("LRD")) {
         value = tjs.m_props.getProperty("RACENO");
         tjs.m_raceNo = Lib.atoi(value);
         value = tjs.m_props.getProperty("POSTPOSITION");
         tjs.m_postPosition = Lib.atoi(value);
         Post post = findPost(tjs.m_raceNo, tjs.m_postPosition);
         if (post != null)
          post.m_props.put(names4[fld], parser.sval);
        }
        else
         tjs.m_props.put(names4[fld], parser.sval);
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
      if (tjs.m_props.size() > 0) {
       tjs.m_track = tjs.m_props.getProperty("TRACKABBR");
       value = tjs.m_props.getProperty("RACEDATE");
       tjs.m_raceDate = Lib.atoDate(value);
       value = tjs.m_props.getProperty("RACENO");
       tjs.m_raceNo = Lib.atoi(value);
       value = tjs.m_props.getProperty("POSTPOSITION");
       tjs.m_postPosition = Lib.atoi(value);
       Post post = findPost(tjs.m_raceNo, tjs.m_postPosition);
       if (post != null)
        post.m_trainerJockeyStats.addElement(tjs);
       else {
        System.out.println("Error " + file + ".DR4: Race=" + tjs.m_raceNo
          + " pos=" + tjs.m_postPosition + "; Post Not found");
        running = false;
        status = false;
       }
       if (Log.isDebug(Log.PARSE3))
        Log.print("Trainer Jockey Stats: " + tjs.ppTrack + " "
          + Lib.datetoa(tjs.ppRaceDate) + " #" + tjs.ppRaceNo + " post="
          + tjs.ppPostPosition + " " + post.m_horseName + "\n\n");
      }
      if (running) {
       fld = 0;
       tjs = new TrainerJockeyStats();
      }
      break;
    }
   } catch (Exception e) {
    // an error condition...
    Truline.println("DR4 line=" + parser.lineno() + " fld=" + fld + ": " + e);
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
  * Parse the scratch file
  */
 public boolean parseScr(String file, String filename)
 {
  boolean status = true;
  BufferedReader in = null;
  StreamTokenizer parser = null;
  String value;
  try {
   in = openFile(file, ".scr");
  } catch (Exception e) {
   int i = file.indexOf('.');
   if (i > 0)
    file = file.substring(0, i);
   Truline.println("Could not open file - " + filename + ".scr\n   "
     + e.getMessage());
   Log
     .print("Could not open file - " + filename + ".scr\n   " + e.getMessage());
   return false;
  }
  Truline.println("Parsing " + filename + ".scr");
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
   Truline.println("Could not open file - " + filename + ".scr\n   "
     + e.getMessage());
   Log
     .print("Could not open file - " + filename + ".scr\n   " + e.getMessage());
   try {
    in.close();
   } catch (Exception e1) {
   }
   return false;
  }
  int fld = 0;
  Scratch scr = new Scratch();
  int c = StreamTokenizer.TT_EOL;
  boolean running = true;
  while (running) {
   // if (parser.lineno() > 2)
   // System.exit(1);
   try {
    c = parser.nextToken();
    switch (c) {
     case '"':
     case StreamTokenizer.TT_WORD:
      if (fld < namesScr.length) {
       if (Log.isDebug(Log.PARSES))
        Log.print(namesScr[fld] + "=" + parser.sval + "\n");
       scr.m_props.put(namesScr[fld], parser.sval);
      }
      break;
     case ',':
      fld++;
      break;
     case StreamTokenizer.TT_EOF:
      running = false;
      // fall through
     case StreamTokenizer.TT_EOL:
      if (scr.m_props.size() > 0) {
       scr.m_track = scr.m_props.getProperty("TRACKABBR");
       value = scr.m_props.getProperty("RACEDATE");
       scr.m_raceDate = Lib.atoDate(value);
       value = scr.m_props.getProperty("RACENO");
       scr.m_raceNo = Lib.atoi(value);
       value = scr.m_props.getProperty("POSTPOSITION");
       scr.m_postPosition = Lib.atoi(value);
       scr.m_horseName = scr.m_props.getProperty("HORSENAME");
       // Change horse to be scratched
       Post post = findPost(scr.m_raceNo, scr.m_postPosition);
       post.m_props.setProperty("ENTRY", "S");
       if (Log.isDebug(Log.PARSES))
        Log.print("Performance: " + scr.m_track + " " + scr.m_raceDate + " #"
          + scr.m_raceNo + " post=" + scr.m_postPosition + " "
          + scr.m_horseName + "\n\n");
      }
      if (running) {
       fld = 0;
      }
      break;
    }
   } catch (Exception e) {
    // an error condition...
    Truline.println("DR3 line=" + parser.lineno() + " fld=" + fld + ": " + e);
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
  for (Enumeration<Race> e = m_races.elements(); e.hasMoreElements();) {
   Race race = (Race) e.nextElement();
   if (race.m_raceNo == raceNo)
    return race;
  }
  return null;
 }
 /**
  * Locate the post record.
  */
 public Post findPost(int raceNo, int position)
 {
  Race race = findRace(raceNo);
  if (race != null) {
   for (Enumeration<Post> e = race.m_posts.elements(); e.hasMoreElements();) {
    Post post = (Post) e.nextElement();
    if (post.m_raceNo == raceNo && post.m_postPosition == position)
     return post;
   }
  }
  return null;
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
    for (Enumeration<?> e = z.entries(); e.hasMoreElements();) {
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
  for (Enumeration<Race> e = m_races.elements(); e.hasMoreElements();) {
   Race race = (Race) e.nextElement();
   if (race.m_raceNo > raceNbr)
    raceNbr = race.m_raceNo;
  }
  return raceNbr;
 }
}
