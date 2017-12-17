package com.base;
/**
 *	BRIS data parser
 */
import com.base.Lib;
import com.base.Log;
import com.base.Performance;
import com.base.Post;
import com.base.Race;
import com.base.Scratch;
import com.base.TrainerJockeyStats;

import java.util.*;
import java.util.zip.*;
import java.io.*;

import com.mains.Truline;
public class BrisJCP
{
 public Vector<Race> m_races = new Vector<Race>();
 /**
  * Constructor
  */
 public BrisJCP() {
 }
 public boolean load(String file, String filename)
 {
  if (file.indexOf('\\') == -1 && file.indexOf('/') == -1) {
   // no path given, use the base address if known.
   String base = Truline.userProps.getProperty("DATADIR");
   if (base != null) {
    file = base + file;
   }
  }
  if (!parseJcp(file, filename))
   return false;
  return true;
 }
 public String[] namesJcp = {
   //
   // MAX
   // FIELD # DESCRIPTION TYPE FORMAT LENGTH COMMENTS
   //
   // *** Today's Race Data ***
   //
   // 1 Track CHARACTER XXX 3
   "TRACKABBR",
   // 2 Date CHARACTER XXXXXXXX 8
   "RACEDATE",
   // 3 Race # NUMERIC 99 2
   "RACENO",
   // 4 Post Position NUMERIC 99 2
   "POSTPOSITION",
   // 5 Entry CHARACTER X 1 A- part of A entry
   // B- part of B entry
   // C- part of C entry
   // F- part of FIELD
   // S- if scratched
   "ENTRY",
   // 6 Distance (in yards) NUMERIC 99999 5 Negative value for
   // about distances
   "DISTANCE",
   // 7 Surface CHARACTER X 1 D- dirt
   // T- turf
   // d- inner dirt
   // t- inner turf
   // s- steeplechase
   // h- hunt
   "SURFACE",
   // 8 Reserved
   "",
   //
   // 9 Race Type CHARACTER XX 2 G1- Grade I stk/hcp
   // G2- Grade II stk/hcp
   // G3- Grade III stk/hcp
   // N- nongraded stake/
   // handicap
   // A- allowance
   // R- Starter Alw
   // T- Starter Hcp
   // C- claiming
   // CO- Optional Clmg
   // S- mdn sp wt
   // M- mdn claimer
   // AO- Alw Opt Clm
   // MO- Mdn Opt Clm
   // NO- Opt Clm Stk
   "RACETYPE",
   // 10 Age/Sex Restrictions CHARACTER XXX see codes below
   "AGESEX",
   // 11 Today's Race Classification CHARACTER X(14) 14 (eg. Alw44000n2L)
   "",
   // 12 Purse NUMERIC 99999999 8
   "PURSE",
   // 13 Claiming Price NUMERIC 9999999 7
   "CLAIMPRICE",
   // 14 Claiming Price (of horse) NUMERIC 9999999 7 blank if N.A.
   "CLAIMINGPURSE",
   // 15 Track Record NUMERIC 999.99 6 seconds &
   // hundredths
   "TRACKRECORD",
   // 16 Race Conditions CHARACTER 500 see also
   // field #225-239
   //
   "COMMENTS",
   // 17 Today's Lasix list CHARACTER 400 (Blank except 1st
   // (see also field #63) horse each race)
   // 18 Today's Bute list CHARACTER 400 "        "
   // 19 Today's Coupled list CHARACTER 200 "        "
   // 20 Today's Mutuel list CHARACTER 200 "        "
   //
   "",
   "",
   "",
   "",
   // 21 Simulcast host track code CHARACTER XXX 3 (actual track
   // code if not
   // a simulcast)
   // 22 Simulcast host track race # NUMERIC 99 2 (actual race # if
   // not a simulcast)
   // 23 Reserved for future use
   // 24 Today's Nasal Strip Change NUMERIC 9 1 (0=No Change,
   // 1=Nasal Strip ON,
   // 2=Nasal Strip OFF,
   // 9=Information
   // Unavailable)
   // 25 Today's All-Weather Surface flag X 1 - A- All Weather
   // Surface flag
   "",
   "",
   "",
   "",
   "ALLWEATHER",
   // 26-27 Reserved for future use
   //
   "",
   "",
   // *** Today's Horse/Trainer/Jockey/Owner ***
   //
   // 28 Today's Trainer CHARACTER 30
   // 29 Trainer Sts Current Meet NUMERIC 9999 4
   // 30 Trainer Wins Current Meet NUMERIC 999 3
   // 31 Trainer Places Current Meet NUMERIC 999 3
   // 32 Trainer Shows Cureent Meet NUMERIC 999 3
   "TRAINER",
   "TRAINERSTARTS",
   "TRAINERWINS",
   "TRAINERPLACES",
   "TRAINERSHOWS",
   // 33 Today's Jockey CHARACTER 25
   // 34 Apprentice wgt allow.(if any) NUMERIC 99 2
   // 35 Jockey Sts Current Meet NUMERIC 9999 4
   // 36 Jockey Wins Current Meet NUMERIC 999 3
   // 37 Jockey Places Current Meet NUMERIC 999 3
   // 38 Jockey Shows Current Meet NUMERIC 999 3
   "JOCKEY",
   "WEIGHTALLOW",
   "JOCKEYSTARTS",
   "JOCKEYWINS",
   "JOCKEYPLACES",
   "JOCKEYSHOWS",
   // 39 Today's Owner CHARACTER 40
   "OWNER",
   // 40 Owner's Silks CHARACTER 100
   // 41 Main Track Only Indicator CHARACTER 1 "M" for MTO
   // "A" for A.E.
   // 42 Reserved for possible future expansion
   "",
   "",
   "",
   // 43 Program Number (if available) CHARACTER XXX 3
   "PROGRAMNUMBER",
   // 44 Morn. Line Odds(if available) NUMERIC 999.99 6
   "MORNINGLINE",
   //
   // *** Horse History Data ***
   //
   // 45 Horse Name CHARACTER 25
   // 46 Year of Birth NUMERIC 99 2
   // 47 Horse's Foaling Month NUMERIC 99 2 (1 for Jan
   // 12 for Dec)
   // 48 Reserved
   // 49 Sex CHARACTER X 1
   // 50 Horse's color CHARACTER 5
   // 51 Weight NUMERIC 999 3
   "HORSENAME",
   "YEARBORN",
   "MONTHBORN",
   "",
   "SEX",
   "COLOR",
   "WEIGHT",
   // 52 Sire CHARACTER 25
   // 53 Sire's sire CHARACTER 25
   // 54 Dam CHARACTER 25
   // 55 Dam's sire CHARACTER 25
   // 56 Breeder CHARACTER 67
   // 57 State/Country abrv. where bred CHARACTER 5
   "SIRE",
   "SIRESSIRE",
   "DAM",
   "DAMSSIRE",
   "BREEDER",
   "WHEREBRED",
   // 58 Program Post Position (if available) 99 2 Updated Post
   // after early
   // scratches (as
   // displayed in
   // program)
   "PROGRAMPOSTPOS",
   // 59-61 Blank fields reserved for possible future expansion
   //
   "",
   "",
   "",
   // *** Current Horse Stats ***
   //
   // New: 62 Today's Medication NUMERIC 2 (0=None, 1=Lasix,
   // w/1st time Lasix info 2=Bute,
   // 3=Bute & Lasix
   // see also fields #17 & #18 4=1st time Lasix
   // 5=Bute & 1st Lasix
   // 9=Medication info
   // unavailable)
   //
   "MEDICATION",
   // Old: 63 Today's Medication NUMERIC 1 (0=None, 1=Lasix,
   // w/o 1st time Lasix info 2=Bute,
   // 3=Bute & Lasix
   // see also fields #17 & #18 9=Medication info
   // unavailable)
   //
   "",
   // 64 Equipment Change NUMERIC 1 (0=No change,
   // 1=Blinkers on,
   // 2=Blinkers off
   // 9=Equipment info
   // unavailable)
   //
   "EQUIPMENTCHG",
   // Horse's Lifetime Record @ Today's Distance:
   //
   // 65 Starts NUMERIC 999 3
   // 66 Wins NUMERIC 99 2
   // 67 Places NUMERIC 99 2
   // 68 Shows NUMERIC 99 2
   // 69 Earnings NUMERIC 99999999 8
   "LRDSTARTS",
   "LRDWINS",
   "LRDPLACES",
   "LRDSHOWS",
   "LRDEARNINGS",
   //
   // Horse's Lifetime Record @ Today's track:
   //
   // 70 Starts NUMERIC 999 3
   // 71 Wins NUMERIC 99 2
   // 72 Places NUMERIC 99 2
   // 73 Shows NUMERIC 99 2
   // 74 Earnings NUMERIC 99999999 8
   "LRTSTARTS",
   "LRTWINS",
   "LRTPLACES",
   "LRTSHOWS",
   "LRTEARNINGS",
   //
   // Horse's Lifetime Turf Record:
   //
   // 75 Starts NUMERIC 999 3
   // 76 Wins NUMERIC 99 2
   // 77 Places NUMERIC 99 2
   // 78 Shows NUMERIC 99 2
   // 79 Earnings NUMERIC 99999999 8
   "LRTURFSTARTS",
   "LRTURFWINS",
   "LRTURFPLACES",
   "LRTURFSHOWS",
   "LRTURFEARNINGS",
   //
   // Horse's Lifetime Wet Record:
   //
   // 80 Starts NUMERIC 999 3
   // 81 Wins NUMERIC 99 2
   // 82 Places NUMERIC 99 2
   // 83 Shows NUMERIC 99 2
   // 84 Earnings NUMERIC 99999999 8
   "LRWETSTARTS",
   "LRWETWINS",
   "LRWETPLACES",
   "LRWETSHOWS",
   "LRWETEARNINGS",
   //
   // Horse's Current Year Record:
   //
   // 85 Year NUMERIC 9999 4 (ie. 2005)
   // 86 Starts NUMERIC 99 2
   // 87 Wins NUMERIC 99 2
   // 88 Places NUMERIC 99 2
   // 89 Shows NUMERIC 99 2
   // 90 Earnings NUMERIC 99999999 8
   "LATESTYEAR",
   "LATESTYEARSTARTS",
   "LATESTYEARWINS",
   "LATESTYEARPLACES",
   "LATESTYEARSHOWS",
   "LATESTYEAREARNINGS",
   //
   // Horse's Previous Year Record:
   //
   // 91 Year NUMERIC 9999 4 (ie. 2004)
   // 92 Starts NUMERIC 99 2
   // 93 Wins NUMERIC 99 2
   // 94 Places NUMERIC 99 2
   // 95 Shows NUMERIC 99 2
   // 96 Earnings NUMERIC 99999999 8
   "PREVIOUSYEAR",
   "PREVIOUSYEARSTARTS",
   "PREVIOUSYEARWINS",
   "PREVIOUSYEARPLACES",
   "PREVIOUSYEARSHOWS",
   "PREVIOUSYEAREARNINGS",
   //
   // Horse's Lifetime Record:
   //
   // 97 Starts NUMERIC 999 3
   // 98 Wins NUMERIC 999 3
   // 99 Places NUMERIC 999 3
   // 100 Shows NUMERIC 999 3
   // 101 Earnings NUMERIC 99999999 8
   "LIFETIMESTARTS",
   "LIFETIMEWINS",
   "LIFETIMEPLACES",
   "LIFETIMESHOWS",
   "LIFETIMEEARNINGS",
   //
   // 102 Date of Workout #1 DATE 99999999 8 CYMD
   // 103 #2
   // 104 #3
   // 105 #4
   // 106 #5
   // 107 #6
   // 108 #7
   // 109 #8
   // 110 #9
   // 111 #10
   // 112 #11
   // 113 #12
   "WORKDATE",
   // 114 Time of Workout #1 NUMERIC 9999.99 7 seconds &
   // hundredths
   // 115 #2 Negative time if a
   // 116 #3 "bullet" work
   // 117 #4 (ie. -34.80 means
   // 118 #5 a bullet work in
   // 119 #6 a time of 34 4/5)
   // 120 #7
   // 121 #8
   // 122 #9
   // 123 #10
   // 124 #11
   // 125 #12
   "WORKTIME",
   // 126 Track of Workout #1 CHARACTER 10
   // 127 #2
   // 128 #3
   // 129 #4
   // 130 #5
   // 131 #6
   // 132 #7
   // 133 #8
   // 134 #9
   // 135 #10
   // 136 #11
   // 137 #12
   "WORKTRACK",
   // 138 Distance of Workout #1 NUMERIC 99999 5 (Dist. in yards)
   // 139 #2 (- value for
   // 140 #3 about distances)
   // 141 #4
   // 142 #5
   // 143 #6
   // 144 #7
   // 145 #8
   // 146 #9
   // 147 #10
   // 148 #11
   // 149 #12
   "WORKDISTANCE",
   // 150 Track Condition of Workout #1 CHARACTER XX 2
   // 151 #2
   // 152 #3
   // 153 #4
   // 154 #5
   // 155 #6
   // 156 #7
   // 157 #8
   // 158 #9
   // 159 #10
   // 160 #11
   // 161 #12
   "WORKCONDITION",
   // 162 Description of Workout #1 CHARACTER XXX 3
   // 163 #2
   // 164 #3 1st Character: H or B
   // 165 #4 H for Handily B for Breezing
   // 166 #5
   // 167 #6 2nd Character: g
   // 168 #7 if worked from gate
   // 169 #8
   // 170 #9 3rd Character: D
   // 171 #10 if 'Dogs are up'
   // 172 #11
   // 173 #12
   "WORKDESCR",
   // 174 Main/Inner track indicator #1 CHARACTER XX 1 MT-main dirt
   // 175 #2 IM-inner dirt
   // 176 #3 TT-Training Trk
   // 177 #4 T-main turf
   // 178 #5 IT-inner turf
   // 179 #6 WC-wood chip
   // 180 #7 HC-hillside course
   // 181 #8 TN-trf trn trk
   // 182 #9 IN-inner trf trn
   // track
   // 183 #10 TR-training race
   // 184 #11 -if blank, track
   // 185 #12 type unknown
   //
   "WORKSURFACE",
   // # of Works that
   // 186 day/distance #1
   // 187 #2
   // 188 #3
   // 189 #4
   // 190 #5
   // 191 #6
   // 192 #7
   // 193 #8
   // 194 #9
   // 195 #10
   // 196 #11
   // 197 #12
   //
   "WORKQTY",
   // "Rank" of the work among
   // 198 other works that day/dist #1
   // 199 #2
   // 200 #3
   // 201 #4
   // 202 #5
   // 203 #6
   // 204 #7
   // 205 #8
   // 206 #9
   // 207 #10
   // 208 #11
   // 209 #12
   "WORKRANK",
   //
   // 210 BRIS Run Style designation CHARACTER XXX 3
   "RUNSTYLE",
   // 211 "Quirin" style Speed Points NUMERIC 9 1
   "QUIRIN",
   // 212 Reserved
   // 213 Reserved
   "",
   "",
   // 214 2f BRIS Pace Par for level NUMERIC 999 3
   // 215 4f BRIS Pace Par for level NUMERIC 999 3
   // 216 6f BRIS Pace Par for level NUMERIC 999 3
   // 217 BRIS Speed Par for class level NUMERIC 999 3
   // 218 BRIS Late Pace Par for level NUMERIC 999 3
   "PASEPAR2F",
   "PASEPAR4F",
   "PASEPAR6F",
   "SPEEDPAR",
   "LATEPAR",
   // 219-223 Reserved
   "",
   "",
   "",
   "",
   "",
   // 224 # of days since last race NUMERIC 9999 4
   "DAYSSINCELAST",
   //
   // 225-230 complete race condition lines CHARACTER 254 Sometimes blank
   "COMMENTS",
   "",
   "",
   "",
   "",
   "",
   // because data is
   // not always
   // available...
   // Use field # 16
   // when necessary
   // 231 Lifetime Starts - All Weather Surface 999 3
   // 232 Lifetime Wins - All Weather Surface 999 3
   // 233 Lifetime Places - All Weather Surface 999 3
   // 234 Lifetime Shows - All Weather Surface 999 3
   // 235 Lifetime Earnings - All Weather Surface 99999999 8
   "LRAWESTARTS",
   "LRAWEWINS",
   "LRAWEPLACES",
   "LRAWESHOWS",
   "LRAWEEARNINGS",
   // 236 Best BRIS Speed - All Weather Surface 999 3
   // 237 Reserved...
   // 238 "Low" Claiming Price(for today's race) NUMERIC 9999999 7
   "",
   "",
   "",
   // 238 Statebred flag "s"(for today's race) NUMERIC 9999999 7
   "STATEBRED",
   // 240-248 Wager Types for this race CHARACTER X(50) 50 (if available)
   "WAGERTYPES",
   "",
   "",
   "",
   "",
   "",
   "",
   "",
   "",
   // 249 Reserved CHARACTER X(58) 58
   // 250 Reserved CHARACTER X(12) 12
   // 251 BRIS Prime Power Rating NUMERIC 999.99 6
   // 252-255 Reserved fields for future use
   "",
   "",
   "PRIMEPOWER",
   "",
   "",
   "",
   "",
   //
   // *** Horse's Past Performace Data for last 10 races ***
   //
   // For each of the last 10 races (most recent to furthest back):
   //
   // 256- 265 Race Date CHARACTER XXXXXXXX 8
   // 266- 274 # of days since previous race NUMERIC 9999 4 Blank-First timer
   // 275 Reserved (# days since prev. race for 10th race back might not be
   // available)
   // 276- 285 Track Code CHARACTER 30
   // 286- 295 BRIS Track Code CHARACTER XXX 3
   // 296- 305 Race # NUMERIC 99 2
   "PPRACEDATE",
   "DAYSSINCEPREVRACE",
   "PPTRACK",
   "PPTRACKABBR",
   "PPRACENO",
   // 306- 315 Track Condition CHARACTER XX 2
   // 316- 325 Distance (in yards) NUMERIC 99999 5 (- value for
   // about distances)
   // 326- 335 Surface CHARACTER X 1 see field #7
   // 336- 345 Special Chute indicator CHARACTER X 1 c - chute
   // 346- 355 # of entrants NUMERIC 99 2
   "CONDITION",
   "PPDISTANCE",
   "PPSURFACE",
   "CHUTE",
   "ENTRANTS",
   // 356- 365 Post Position NUMERIC 99 2
   // 366- 375 Equipment CHARACTER X 1 b - blinkers
   // 376- 385 Reserved
   // 386- 395 Medication NUMERIC 9 1 0=None, 1=Lasix,
   // see also field #62 2=Bute,
   // 3=Bute & Lasix
   // 396- 405 Trip Comment CHARACTER 100
   "PPPOSTPOSITION",
   "BLINKERS",
   "",
   "MEDICATION",
   "COMMENT",
   // 406- 415 Winner's Name CHARACTER
   // 416- 425 2nd Place finishers Name CHARACTER
   // 426- 435 3rd Place finishers Name CHARACTER
   // 436- 445 Winner's Weight carried NUMERIC 999 3
   // 446- 455 2nd Place Weight carried NUMERIC 999 3
   // 456- 465 3rd Place Weight carried NUMERIC 999 3
   "WINNER",
   "PLACER",
   "SHOWER",
   "WINWEIGHT",
   "PLACEWEIGHT",
   "SHOWWEIGHT",
   // 466- 475 Winner's Margin NUMERIC 99.99 5 ( 0 if DeadHeat )
   // 476- 485 2nd Place Margin NUMERIC 99.99 5 ( 0 if DeadHeat )
   // 486- 495 3rd Place Margin NUMERIC 99.99 5 ( 0 if DeadHeat )
   // 496- 505 Alternate/Extra Comment line CHARACTER 200 (includes
   // "claimed from"
   // text & misc.
   // other comments)
   // 506- 515 Weight NUMERIC 999 3
   // 516- 525 Odds NUMERIC 9999.99 7
   // 526- 535 Entry CHARACTER X 1 e - entry
   // 536- 545 Race Classification CHARACTER 25
   // 546- 555 Claiming Price (of horse) NUMERIC 9999999 7
   // 556- 565 Reserved (Purse) NUMERIC 99999999 8
   "WINMARGIN",
   "PLACEMARGIN",
   "SHOWMARGIN",
   "COMMENT2",
   "PPWEIGHT",
   "ODDS",
   "PPENTRY",
   "RACECLASSIFICATION",
   "PPCLAIMPRICE",
   "PPPURSE",
   // 566- 575 Start Call Position CHARACTER 2 A- bled B- bolted
   // 576- 585 1st Call Position(if any) CHARACTER 2 C- broke down
   // 586- 595 2nd Call Position(if any) CHARACTER 2 D- distanced
   // 596- 605 Gate Call Position(if any) CHARACTER 2 E- dwelt F- eased
   // 606- 615 Stretch Position (if any) CHARACTER 2 G- fell H- lame
   // 616- 625 Finish Position CHARACTER 2 I- left at post
   // 626- 635 Money Position CHARACTER 2 J- left course
   "POSITION1",
   "POSITION2",
   "POSITION3",
   "POSITION4",
   "POSITION5",
   "POSITION6",
   "POSITION7",
   // K- lost rider
   // L- running postions
   // omitted because
   // of weather
   // conditions
   // M- propped
   // N- sulked O- sore
   // P- refused to break
   // Q- pulled up
   // R- wheeled
   // S- saddle slipped
   // T- lost irons
   // U- beaten off
   // V- reared W- bucked
   // X- did not finish
   // Y- unplaced
   // '?' or '*' - unspecified
   // reason for
   // missed call
   // 636- 645 Reserved
   // 646- 655 Reserved
   "",
   "",
   // 656- 665 1st Call BtnLngths/Ldr margin NUMERIC 99.99 5
   // 666- 675 1st Call BtnLngths only
   // 676- 685 2nd Call BtnLngths/Ldr margin NUMERIC 99.99 5
   // 686- 695 2nd Call BtnLngths only
   // 696- 705 BRIS Race Shape - 1st Call NUMERIC 999 3
   // 706- 715 Reserved
   // 716- 725 Stretch BtnLngths/Ldr margin NUMERIC 99.99 5
   // 726- 735 Stretch BtnLngths only
   // 736- 745 Finish BtnLngths/Wnrs marginNUMERIC 99.99 5
   // 746- 755 Finish BtnLngths only
   "LENLDRMGN1",
   "LENGTHS1",
   "LENLDRMGN2",
   "LENGTHS2",
   "",
   "",
   "LENLDRMGN3",
   "LENGTHS3",
   "LENLDRMGN4",
   "LENGTHS4",
   // 756- 765 BRIS Race Shape - 2nd Call NUMERIC 999 3
   "",
   // 766- 775 BRIS 2f Pace Fig NUMERIC 999 3
   // 776- 785 BRIS 4f Pace Fig
   // 786- 795 BRIS 6f Pace Fig
   // 796- 805 BRIS 8f Pace Fig
   // 806- 815 BRIS 10f Pace Fig
   // 816- 825 BRIS Late Pace Fig
   // 826- 835 BRIS Race Rating NUMERIC 999.9 5
   // 836- 845 BRIS Class Rating NUMERIC 999.9 5
   "PACE2F",
   "PACE4F",
   "PACE6F",
   "PACE8F",
   "PACE10F",
   "PACELATE",
   "",
   "",
   // 846- 855 BRIS Speed Rating NUMERIC 999 3
   // 856- 865 Speed Rating NUMERIC 999 3
   // 866- 875 Track Variant NUMERIC 99 2
   "BRISSPEED",
   "DRFSPEED",
   "VARIENT",
   // 876- 885 2f Fraction (if any) NUMERIC 999.99 6 seconds &
   // hundredths
   // 886- 895 3f Fraction (if any) NUMERIC 999.99 6
   // 896- 905 4f Fraction (if any) NUMERIC 999.99 6
   // 906- 915 5f Fraction (if any) NUMERIC 999.99 6
   // 916- 925 6f Fraction (if any) NUMERIC 999.99 6
   // 926- 935 7f Fraction (if any) NUMERIC 999.99 6
   // 936- 945 8f Fraction (if any) NUMERIC 999.99 6
   // 946- 955 10f Fraction (if any) NUMERIC 999.99 6
   // 956- 965 12f Fraction (if any) NUMERIC 999.99 6
   // 966- 975 14f Fraction (if any) NUMERIC 999.99 6
   // 976- 985 16f Fraction (if any) NUMERIC 999.99 6
   "FRACTION2F",
   "FRACTION3F",
   "FRACTION4F",
   "FRACTION5F",
   "FRACTION6F",
   "FRACTION7F",
   "FRACTION8F",
   "FRACTION10F",
   "FRACTION12F",
   "FRACTION14F",
   "FRACTION16F",
   // 986- 995 Fraction #1 NUMERIC 999.99 6
   // 996-1005 #2 NUMERIC 999.99 6
   // 1006-1015 #3 NUMERIC 999.99 6
   // 1016-1025 Reserved
   // 1026-1035 Reserved
   // 1036-1045 Final Time NUMERIC 999.99 6 seconds &
   "FRACTION1",
   "FRACTION2",
   "FRACTION3",
   "",
   "PPRUNSTYLE",
   "FINALTIME",
   // hundredths
   // 1046-1055 Claimed code CHARACTER X 1 c - claimed
   // 1056-1065 Trainer (when available) CHARACTER X 30
   // 1066-1075 Jockey CHARACTER 25
   // 1076-1085 Apprentice Wt allow (if any) NUMERIC 99 2
   // 1086-1095 Race Type CHARACTER XX 2 (G1,G2,G3,N,A,
   // R,T,C,CO,S,M,
   // AO,MO,NO)
   // see field #9
   //
   // 1096-1105 Age and Sex Restrictions CHARACTER XXX 3 see codes below
   // 1106-1115 Statebred flag CHARACTER X 1 s- statebred
   // 1116-1125 Restricted/Qualifier flag CHARACTER X 1 R- restricted
   // Q- qualifier
   // O- optional
   // claimer
   // 1126-1135 Favorite indicator NUMERIC 9 1 0- Non-favorite
   // 1- Favorite
   // 1136-1145 Front Bandages indicator NUMERIC 9 1 0- No front wraps
   "CLAIMED",
   "PPTRAINER",
   "PPJOCKEY",
   "PPWEIGHTALLOW",
   "PPRACETYPE",
   "PPAGESEX",
   "PPSTATEBRED",
   "PPRESTRICTED",
   "FAVORITE",
   "FRONTBANDAGE",
   // 1- front wraps
   // 1146 Avg BRIS Class Rating last 3 sts NUMERIC 999.9 5
   // 1147 Trainer Sts Current Year NUMERIC 9999 4
   // 1148 Wins 4
   // 1149 Places 4
   // 1150 Shows 4
   // 1151 ROI Current Year NUMERIC 999.99 6
   "BRISCLS",
   "TRNSTSCY",
   "TRNWINCY",
   "TRNPLCCY",
   "TRNSHWCY",
   "TRNROICY",
   // 1152 Trainer Sts Previous Year NUMERIC 9999 4
   // 1153 Wins 4
   // 1154 Places 4
   // 1155 Shows 4
   // 1156 ROI Previous Year NUMERIC 999.99 6
   "TRNSTSPY",
   "TRNWINPY",
   "TRNPLCPY",
   "TRNSHWPY",
   "TRNROIPY",
   // 1157 Jockey Sts Current Year NUMERIC 9999 4
   // 1158 Wins 4
   // 1159 Places 4
   // 1160 Shows 4
   // 1161 ROI Current Year NUMERIC 999.99 6
   "JKYSTSCY",
   "JKYWINCY",
   "JKYPLCCY",
   "JKYSHWCY",
   "JKYROICY",
   // 1162 Jockey Sts Previous Year NUMERIC 9999 4
   // 1163 Wins 4
   // 1164 Places 4
   // 1165 Shows 4
   // 1166 ROI Previous Year NUMERIC 999.99 6
   "JKYSTSPY",
   "JKYWINPY",
   "JKYPLCPY",
   "JKYSHWPY",
   "JKYROIPY",
   // 1167-1176 BRIS Speed Par for class level of Last 10 races 3
   "",
   "",
   "",
   "",
   "",
   "",
   "",
   "",
   "",
   "",
   // 1177 Sire Stud Fee (current) NUMERIC 9999999 7
   // 1178 Best BRIS Speed - Fast track NUMERIC 999 3
   // 1179 Best BRIS Speed - Turf NUMERIC 999 3
   // 1180 Best BRIS Speed - Off track NUMERIC 999 3
   // 1181 Best BRIS Speed - Distance NUMERIC 999 3
   "", "",
   "",
   "",
   "",
   // 1182-1191 Bar shoe CHARACTER X 1 r- bar shoe
   "BARSHOE1",
   "BARSHOE2",
   "BARSHOE3",
   "BARSHOE4",
   "BARSHOE5",
   "BARSHOE6",
   "BARSHOE7",
   "BARSHOE8",
   "BARSHOE9",
   "BARSHOE10",
   // 1192-1201 Company Line Codes CHARACTER XXXX 4
   "", 
   "", "", "", "", "", "",
   "",
   "",
   "",
   // 1202-1211 "Low" Claiming Price of race NUMERIC 9999999 7
   "", "", "", "", "", "", "",
   "",
   "",
   "",
   // 1212-1221 "High" Claiming Price of race NUMERIC 9999999 7
   "",
   "",
   "",
   "",
   "",
   "",
   "",
   "",
   "",
   "",
   // 1222 Auction Price NUMERIC 999999999 9
   // 1223 Where/When Sold at Auction CHARACTER 12
   "",
   "",
   // 1224-1253 Reserved for future use
   "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
   "", "", "", "",
   "",
   "",
   "",
   "",
   "",
   "",
   "",
   // 1254-1263 Code for prior 10 starts 1 "s"-Nasal Strip
   "NOSEPATCH1",
   "NOSEPATCH2",
   "NOSEPATCH3",
   "NOSEPATCH4",
   "NOSEPATCH5",
   "NOSEPATCH6",
   "NOSEPATCH7",
   "NOSEPATCH8",
   "NOSEPATCH9",
   "NOSEPATCH10",
   // "x"-Off the Turf
   // 1264 BRIS Dirt Pedigree Rating CHARACTER XXXX 4 eg. "115*"
   // 1265 BRIS Mud Pedigree Rating CHARACTER XXXX 4
   // 1266 BRIS Turf Pedigree Rating CHARACTER XXXX 4
   // 1267 BRIS Dist Pedigree Rating CHARACTER XXXX 4
   "", "",
   "",
   "",
   // 1268-1327 Reserved
   "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
   "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
   "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
   "", "", "",
   // 1328 Best BRIS Speed: Life NUMERIC 999 3
   // 1329 Best BRIS Speed: Most Recent Yr horse ran 3
   // 1330 Best BRIS Speed: 2nd Most Recent Yr horse ran 3
   // 1331 Best BRIS Speed: Today's Track 3
   // 1332 # Starts (FAST Dirt) NUMERIC 999 3
   // 1333 # Wins (FAST Dirt) NUMERIC 99 2
   // 1334 # Places (FAST Dirt) NUMERIC 99 2
   // 1335 # Shows (FAST Dirt) NUMERIC 99 2
   // 1336 Earnings (FAST Dirt) NUMERIC 99999999 8
   "", "", "", "",
   "LRDIRTSTARTS",
   "LRDIRTWINS",
   "LRDIRTPLACES",
   "LRDIRTSHOWS",
   "LRDIRTEARNINGS",
   // 1337 Key Trnr Stat Category #1 CHARACTER X(16) 16
   // 1338 # of starts #1 NUMERIC 9999 4
   // 1339 Win% #1 NUMERIC 999.99 6
   // 1340 in-the-money (itm) % #1 NUMERIC 999.99 6
   // 1341 $2ReturnOnInvestment #1 NUMERIC 999.99 6
   "TRAINERCAT1", "TRAINERSTS1", "TRAINERWIN1", "TRAINERITM1", "TRAINERROI1",
   // 1342 Key Trnr Stat Category #2 CHARACTER X(16) 16
   // 1343 # of starts #2 NUMERIC 9999 4
   // 1344 Win% #2 NUMERIC 999.99 6
   // 1345 in-the-money (itm) % #2 NUMERIC 999.99 6
   // 1346 $2ReturnOnInvestment #2 NUMERIC 999.99 6
   "TRAINERCAT2", "TRAINERSTS2", "TRAINERWIN2", "TRAINERITM2", "TRAINERROI2",
   // 1347 Key Trnr Stat Category #3 CHARACTER X(16) 16
   // 1348 # of starts #3 NUMERIC 9999 4
   // 1349 Win% #3 NUMERIC 999.99 6
   // 1350 in-the-money (itm) % #3 NUMERIC 999.99 6
   // 1351 $2ReturnOnInvestment #3 NUMERIC 999.99 6
   "TRAINERCAT3", "TRAINERSTS3", "TRAINERWIN3", "TRAINERITM3", "TRAINERROI3",
   // 1352 Key Trnr Stat Category #4 CHARACTER X(16) 16
   // 1353 # of starts #4 NUMERIC 9999 4
   // 1354 Win% #4 NUMERIC 999.99 6
   // 1355 in-the-money (itm) % #4 NUMERIC 999.99 6
   // 1356 $2ReturnOnInvestment #5 NUMERIC 999.99 6
   "TRAINERCAT4", "TRAINERSTS4", "TRAINERWIN4", "TRAINERITM4", "TRAINERROI4",
   // 1357 Key Trnr Stat Category #5 CHARACTER X(16) 16
   // 1358 # of starts #5 NUMERIC 9999 4
   // 1359 Win% #5 NUMERIC 999.99 6
   // 1360 in-the-money (itm) % #5 NUMERIC 999.99 6
   // 1361 $2ReturnOnInvestment #5 NUMERIC 999.99 6
   "TRAINERCAT5", "TRAINERSTS5", "TRAINERWIN5", "TRAINERITM5", "TRAINERROI5",
   // 1362 Key Trnr Stat Category #6 CHARACTER X(16) 16
   // 1363 # of starts #6 NUMERIC 9999 4
   // 1364 Win% #6 NUMERIC 999.99 6
   // 1365 in-the-money (itm) % #6 NUMERIC 999.99 6
   // 1366 $2ReturnOnInvestment #6 NUMERIC 999.99 6
   "TRAINERCAT6", "TRAINERSTS6", "TRAINERWIN6", "TRAINERITM6", "TRAINERROI6",
                          // 1367 JKY@Dis/JKYonTurf Label CHARACTER X(13) 13
                          // 1368 JKY@Dis/JKYonTurf Starts NUMERIC 9999 4
                          // 1369 JKY@Dis/JKYonTurf Wins NUMERIC 9999 4
                          // 1370 JKY@Dis/JKYonTurf Places NUMERIC 9999 4
                          // 1371 JKY@Dis/JKYonTurf Shows NUMERIC 9999 4
                          // 1372 JKY@Dis/JKYonTurf ROI NUMERIC 999.99 6
                          // 1373 JKY@Dis/JKYonTurf Earnings NUMERIC 999999999 9
                          // 1374 Post Times (by region) CHARACTER X(50) 50
                          // eg: 1:15/(12:15)/11:15/10:15
                          // 1375-1382 Reserved
   "", "", "", "", "", "", "", "", "", "",
   "", "", "", "", "", "", "", "", "", "",
   "", "", "", "", "", "", "", "", "", "",
   "", "", "", "", "", "",     
                          // 1383-1392 Extended Start Comment CHARACTER X(90) 90
                          // 1393-1402 "Sealed" track indicator CHARACTER X 1
                          // "s"
                          // 1403-1412 Prev. All-Weather Surface flagCHARACTER X
   "ALLWEATHER1",
   "ALLWEATHER2",
   "ALLWEATHER3",
   "ALLWEATHER4",
   "ALLWEATHER5",
   "ALLWEATHER6",
   "ALLWEATHER7",
   "ALLWEATHER8",
   "ALLWEATHER9",
   "ALLWEATHER10"
                          // 1 A - All Weather
                          // Surface
                          //
                          // 1413-1435 Reserved fields
                          //
                          //
                          // Age/Sex Restriction Codes (3 character sting):
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
                          // Special data in MultiCaps datafile:
                          // #826- 835 BRIS Race Rating
                          // #836- 845 BRIS Class Rating
                          // #1146 Avg BRIS Class Rating last three starts
                          //
                          // 03/10/1999 Added field : #62 Today's Medication
                          // w/1st time Lasix info
                          // Added field : #1177 Sire Stud Fee (current)
                          // Added field : #1178 Best BRIS Speed - Fast track
                          // Added field : #1179 Best BRIS Speed - Turf
                          // Added field : #1180 Best BRIS Speed - Off track
                          // Added field : #1181 Best BRIS Speed - Distance
                          // Added fields: #1182-1191 Bar shoe indicator
                          // Added fields: #1192-1201 Company Line Codes
                          // Added fields: #1202-1211 "Low" Claiming Price of
                          // race
                          // Added fields: #1212-1221 "High" Claiming Price of
                          // race
                          // 10/25/1999 Added field : #1222 Auction Price
                          // Added field : #1223 Where/When Sold at Auction
                          // 03/15/2000 Added field : #24 Today's Nasal Strip
                          // Change
                          // #1254-1263 Nasal Strip Code for prior 10 starts
                          // 12/04/2000 Updated field:# 9 Today's Race Type
                          // (added "CO": OptClm
                          // 01/03/2001 #1264-1267 BRIS Dirt,Mud,Turf,Dist
                          // Pedigree Rating
                          // #1328 Best BRIS Speed: Life
                          // #1329 Best BRIS Speed: Most Recent Yr horse ran
                          // #1330 Best BRIS Speed: 2nd Most Recent Yr ran
                          // #1331 Best BRIS Speed: Today's Track
                          // #1332 # Starts (FAST Dirt)
                          // #1333 # Wins (FAST Dirt)
                          // #1334 # Places (FAST Dirt)
                          // #1335 # Shows (FAST Dirt)
                          // #1336 Earnings (FAST Dirt)
                          // #1337 Key Trn Stat Category #1
                          // #1338 # of starts #1
                          // #1339 Win% #1
                          // #1340 In-the-money% #1
                          // #1341 $2ReturnOnInvestment #1
                          // #1342-1366 Key Trn Stat #2-=#6
                          // # 636-645 Start Call BtnLngths/Ldr Margin
                          // # 646-655 Start Call BtnLngths only
                          // 5/2002 # 596-605 Gate Call Position
                          // 10/02/2002 # 58 Program Post Position
                          // #1374 Post Times (by region)
                          //
                          // # 40 Owner Silks
                          // # 41 Main Track Only Indicator
                          // #1254-1263 Off The Turf Indicator
                          // 9/30/2004 #238 "Low" Claiming Price (for today's
                          // race)
                          // #239 Statebred flag (for today's race)
                          // #1383-1392 Extended Start Comment
                          // 06/15/2005 #1393-1402 "Sealed" track indicator
                          // 08/29/2005 # 25 Today's All-Weather Surface flag
                          // #1403-1412 Prv All-Weather Surface flag
                          // 09/2006 #231 Lifetime Starts - All Weather Surface
                          // #232 Lifetime Wins - All Weather Surface
                          // #233 Lifetime Places - All Weather Surface
                          // #234 Lifetime Shows - All Weather Surface
                          // #235 Lifetime Earnings - All Weather Surface
                          // #236 Best BRIS Speed - All Weather Surface
                          // 02/07/2007 # 696- 705 BRIS Race Shape - 1st Call
                          // # 756- 765 BRIS Race Shape - 2nd Call
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
  * Parse the file for the races - one record per horse per race.
  */
 public boolean parseJcp(String file, String filename)
 {
  boolean status = true;
  String buffer;
  BufferedReader in = null;
  StreamTokenizer parser = null;
  try {
   in = openFile(file, ".JCP");
  } catch (Exception e) {
   int i = file.indexOf('.');
   if (i > 0)
    file = file.substring(0, i);
   Truline.println("Could not open file - " + file + ".JCP\n   "
     + e.getMessage());
   Log
     .print("Could not open file - " + file + ".JCP\n   " + e.getMessage());
   return false;
  }
  Truline.println("Parsing " + filename + ".JCP");
  try {
   parser = new StreamTokenizer(in);
   parser.resetSyntax();
   parser.wordChars(33, 255);
   parser.whitespaceChars(0, 32);
   parser.whitespaceChars(91, 93);
   parser.eolIsSignificant(true);
   parser.commentChar('#');
   parser.quoteChar('"');
   parser.ordinaryChar(',');
   // parser.ordinaryChar('\\');
  } catch (Exception e) {
   Truline.println("Could not read file - " + filename + ".jcp\n   "
     + e.getMessage());
   Log
     .print("Could not read file - " + filename + ".jcp\n   " + e.getMessage());
   return false;
  }
  String value;
  int fld = 0;
  int w_raceNo = 0;
  int j = 0;
  int fld_in = 0;
  int w_StartPP = 0;
  int w_postPosition = 0;
  int c = StreamTokenizer.TT_EOL;
  String w_morningLine = null;
  String w_track = null;
  String w_trackPP = null;
  String name = null;
  String nameVal = "";
  String w_position = null;
  Date w_raceDate = null;
  String w_raceDateStr = null;
  // Instantiate a new race and put it in the list.
  Race race = new Race();
  m_races.addElement(race);
  Post post = new Post();
  Performance perAll = new Performance();
  boolean running = true;
  boolean postData = false;
  boolean horseData = false;
  boolean pastPerf = false;
  boolean trainerJockey = false;
  boolean badLine = false;
  boolean noPP = false;
  TrainerJockeyStats tjs = new TrainerJockeyStats();
  while (running) {
   try {
    c = parser.nextToken();
    switch (c) {
     case '"':
     case StreamTokenizer.TT_WORD:
      fld_in++;
      if (fld < namesJcp.length) {
       if (parser.sval == null)
        nameVal = "";
       else
        nameVal = parser.sval;
       name = namesJcp[fld].trim();
       if (name == "FRONTBANDAGE" && j == 9 && !nameVal.equals("0") && !nameVal.equals("1")) {
        fld++;
        name = namesJcp[fld].trim();
       }
       if (name.length() > 0) {
        // Check for bad line
        if (name == "TRACKABBR" && race.m_track != null) {
         String testTrack = nameVal.trim();
         if (!testTrack.equals("AJX") && !testTrack.equals("FPX") && !testTrack.equals("PRX"))
          if (testTrack.substring(2).equals("X") || testTrack.equals("FPK"))
           testTrack = testTrack.substring(0, 2);
         if (testTrack.equals(race.m_track)) {
          badLine = false;
         } else {
          badLine = true;
          fld++;
          break;
         }
        }
        if (badLine) {
         fld++;
         break;
        }
        if (name == "TRAINER") {
         postData = true;
         // If first race or change in races, save it
         value = race.m_props.getProperty("RACENO");
         if (w_raceNo == 0 || w_raceNo != Lib.atoi(value)) {
          if (race.m_props.size() > 0) {
           /*
            * // First copy all of the properties for this race Boolean moreProp
            * = true; fld = 1; while (moreProp) { name = namesJcp[fld].trim();
            * if (name.length() > 0) { value = raceW.m_props.getProperty(name);
            * if (value != null) race.m_props.put(name, value); } fld++; if
            * (name == "COMMENTS") moreProp = false; }
            */
           race.m_track = race.m_props.getProperty("TRACKABBR");
           if (!race.m_track.equals("AJX") && !race.m_track.equals("FPX") && !race.m_track.equals("PRX"))
            if (race.m_track.substring(2).equals("X") || race.m_track.equals("FPK"))
             race.m_track = race.m_track.substring(0, 2);
           w_track = race.m_track;
           w_raceDateStr = race.m_props.getProperty("RACEDATE");
           race.m_raceDate = Lib.atoDate(w_raceDateStr);
           w_raceDate = race.m_raceDate;
           value = w_raceDateStr.substring(2, 4);
           race.m_raceYear = Lib.atoi(value);
           value = race.m_props.getProperty("RACENO");
           race.m_raceNo = Lib.atoi(value);
           w_raceNo = race.m_raceNo;
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
            Log.print("Race " + race.m_track + " "
              + Lib.datetoa(race.m_raceDate) + " #" + race.m_raceNo + "\n\n");
          }
         }
        }
        if (name == "HORSENAME")
         horseData = true;
        if (name == "PROGRAMPOSTPOS" || name == "MEDICATION")
         horseData = false;
        if (name == "PPRACEDATE") {
         pastPerf = true;
         w_StartPP = fld;
        }
        if (name.length() > 6)
         // if (name.substring(0,7).equals("BARSHOE") || name.substring(0,7).equals("NOSEPAT")
         //  || name.substring(0,6).equals("LRDIRT") || name.substring(0,6).equals("AVGBRI") 
         // || name.substring(0,6).equals("TRNSTS"))
        if (name.substring(0,7).equals("BRISCLS"))
         {
          pastPerf = false;
         }
        if (name == "TRAINERCAT1") {
         pastPerf = false;
         trainerJockey = true;
        }
        if (name.length() > 9)
         if (name.substring(0,10).equals("ALLWEATHER")) {
          trainerJockey = false;
         }
         
        if (pastPerf) {
         if (Log.isDebug(Log.PARSE1))
          Log.print(name + j + "=" + nameVal + "\n");
        } else {
         if (Log.isDebug(Log.PARSE1)) {
          if (horseData)
           Log.print("horseData=");
          if (postData)
           Log.print("postData=");
          if (trainerJockey)
           Log.print("trainerJockeyData=");
          Log.print(name + "=" + nameVal + "\n");
         }
        }
        if (name == "RACENO") {
         if (w_raceNo > 0 && w_raceNo != Lib.atoi(nameVal)) {
          race = new Race();
          m_races.addElement(race);
          race.m_props.put("TRACKABBR", w_track);
          race.m_props.put("RACEDATE", w_raceDateStr);
         }
         race.m_props.put("RACENO", nameVal);
        } else if (postData && name.startsWith("WORK")) {
         if (nameVal != null && nameVal.length() > 0) {
          if (name.equals("WORKDATE")) {
           post.m_work[j].m_workDate = Lib.atoDate(nameVal);
          }
          post.m_work[j].m_props.put(name, nameVal);
         }
        } else if (horseData) {
         // save horse data for post position
         post.m_horse.m_props.put(name, nameVal);
        } else if (pastPerf) {
         if (nameVal != null && nameVal.length() > 0) {
          // save past performance field post.m_work[j].m_props.put(name,
          // nameVal);
          perAll.m_props.put(name + j, nameVal);
         }
        } else if (trainerJockey) {
         if (nameVal != null && nameVal.length() > 0) {
          // save trainer stats data
          tjs.m_props.put(name, nameVal);
         }
        } else if (postData) {
         // save post position data
         post.m_props.put(name, nameVal);
        } else {
         if (name == "ENTRY" || name == "POSTPOSITION"
           || name == "CLAIMINGPURSE") {
          post.m_props.put(name, nameVal);
          if (name == "POSTPOSITION")
           w_postPosition = Lib.atoi(nameVal);
         } else
          race.m_props.put(name, nameVal.trim());
        }
       }
      }
      if (nameVal == null || nameVal.length() < 5)
       break;
      // if (nameVal.substring(nameVal.length()-1) != ",")
      if (nameVal.substring(0, 4) == "Near" || nameVal.lastIndexOf(",") != -1)
       // fall through - comma sucked into previous value
       Log.print("***Bad parse character " + name + "=" + nameVal + "\n");
      else
       break;
     case ',':
      if (fld < namesJcp.length && namesJcp[fld].startsWith("WORK")) {
       j++;
       if (j >= 12) {
        j = 0;
        fld++;
       }
      } else if (fld < namesJcp.length && namesJcp[fld].equals("PPRACEDATE") && !(pastPerf)) {
        pastPerf = true;
        noPP = true;
        w_StartPP = fld;  
      } else if (pastPerf) {
       j++;
       if (j >= 10) {
        if (running) {
         if (namesJcp[fld].equals("FRONTBANDAGE"))
          pastPerf = false;
         j = 0;
         fld++;
        }
       }
      } else
       fld++;
      break;
     case StreamTokenizer.TT_EOF:
      running = false;
      // fall through
     case StreamTokenizer.TT_EOL:
      postData = false;
      pastPerf = false;
      trainerJockey = false;
      badLine = false;
      if (tjs.m_props.size() > 0) {
       post.m_trainerJockeyStats.addElement(tjs);
      }
      // end of post position data processing
      if (post.m_props.size() > 0) {
       post.m_track = w_track;
       post.m_raceDate = w_raceDate;
       post.m_raceNo = w_raceNo;
       value = post.m_props.getProperty("POSTPOSITION");
       post.m_postPosition = Lib.atoi(value);
       post.cloth = post.m_props.getProperty("PROGRAMNUMBER", "");
       // if (post.cloth.equals("XX") || post.cloth.equals(""))
       // post.cloth = post.m_props.getProperty("POSTPOSITION");
       w_morningLine = post.m_props.getProperty("MORNINGLINE");
       // Log.print("Morning Line = "+w_morningLine+"\n\n");
       if (w_morningLine == null)
        post.m_morningLine = "";
       else {
        if (w_morningLine.length() < 3)
         w_morningLine = w_morningLine;
        else if (w_morningLine.substring(w_morningLine.length() - 2).equals("00"))
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
       if (post.m_jockeyName == null)
        post.m_jockeyName = "None";
       post.m_ownerName = post.m_props.getProperty("OWNER");
       post.m_runStyle = post.m_props.getProperty("RUNSTYLE");
       post.m_primePower = Lib.atof(post.m_props.getProperty("PRIMEPOWER")); 
       post.m_quirin = Lib.atoi(post.m_props.getProperty("QUIRIN"));
       post.m_claim = Lib.atoi(post.m_props.getProperty("CLAIMPRICE", "0"));
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
       // if (post.m_sireSireName.equals(null))
       // post.m_sireSireName = " ";
       post.m_weight = Lib.atoi(post.m_horse.m_props.getProperty("WEIGHT"));
       if (Log.isDebug(Log.PARSE1))
        Log.print("Post: " + post.m_track + " " + Lib.datetoa(post.m_raceDate)
          + " #" + post.m_raceNo + " post=" + post.m_postPosition + " "
          + post.m_horseName + " Trainer=" + post.m_trainerName + "\n\n");
       // Race raceP = findRace(post.m_raceNo);
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
      // move past performances into race-by-race instances for horse
      Post postP = findPost(w_raceNo, w_postPosition);
      if (perAll.m_props.size() <= 0) {
       fld = w_StartPP;
      } else {
       j = 0;
       Boolean morePP = true;
       while (morePP) {
        Performance per = new Performance();
        // First set all of the race overhead information
        per.m_track = w_track;
        per.m_raceDate = w_raceDate;
        per.m_raceNo = w_raceNo;
        per.m_postPosition = w_postPosition;
        // Second copy all of the properties for this PP
        Boolean moreProp = true;
        fld = w_StartPP;
        while (moreProp) {
         name = namesJcp[fld].trim();
         if (name.length() > 0) {
          value = perAll.m_props.getProperty(name + j);
          // if (name.startsWith("POSITION"))
          // w_position = value;
          if (value != null) {
           // if (name.startsWith("LENGTHS") && w_position == "1")
           // per.m_props.put(name, "0");
           // else
           per.m_props.put(name, value);
          }
          /*
           * else if (name.startsWith("LENGTHS")) per.m_props.put(name, " ");
           */
         }
         fld++;
         if (fld >= namesJcp.length)
          moreProp = false;
        }
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
        if (per.ppTrack.equals("HOL"))
         per.ppTrack = "BHP";
        if (!per.ppTrack.equals("AJX") && !per.ppTrack.equals("FPX") && !per.ppTrack.equals("PRX"))
         if (per.ppTrack.substring(2).equals("X") || per.ppTrack.equals("FPK"))
          per.ppTrack = per.ppTrack.substring(0, 2);
        value = per.m_props.getProperty("PPRACEDATE");
        per.ppRaceDate = Lib.atoDate(value);
        value = per.m_props.getProperty("PPRACENO");
        per.ppRaceNo = Lib.atoi(value);
        value = per.m_props.getProperty("PPPOSTPOSITION");
        per.ppPostPosition = Lib.atoi(value);
        // extract the purse from Race Classification
        String string = perAll.m_props
          .getProperty("RACECLASSIFICATION" + j, "");
        for (int i = 0; i < string.length(); i++) {
         if (Character.isDigit(string.charAt(i))) {
          int purse = Lib.atoi(string.substring(i));
          if (purse >= 100) {
           per.m_props.put("PURSE", "" + purse);
           break;
          }
         }
        }
        if (postP != null)
         postP.m_performances.addElement(per);
        else {
         System.out.println("Error " + file + ".DR3: Race=" + per.m_raceNo
           + " pos=" + per.m_postPosition + "; Post Not found");
         // running = false;
         // status = false;
        }
        if (Log.isDebug(Log.PARSE1))
         Log.print("Performance: " + per.ppTrack + " "
           + Lib.datetoa(per.ppRaceDate) + " #" + per.ppRaceNo + " post="
           + per.ppPostPosition + " " + post.m_horseName + "\n\n");
        j++;
        if (j < 10) {
         w_trackPP = perAll.m_props.getProperty("PPTRACKABBR" + j);
         if (w_trackPP == null)
          morePP = false;
        } else {
         if (w_trackPP.length() > 2)
          if (w_trackPP.equals("HOL"))
           w_trackPP = "BHP";
         if (!w_trackPP.equals("AJX") && !w_trackPP.equals("FPX") && !w_trackPP.equals("PRX"))
          if (w_trackPP.substring(2).equals("X") || w_trackPP.equals("FPK"))
           w_trackPP = w_trackPP.substring(0, 2);
         morePP = false;
        }
       }
      }
      // set up for new post position and past performance if not EOF
      if (running) {
       j = 0;
       fld = 0;
       fld_in = 0;
       post = new Post();
       perAll = new Performance();
       tjs = new TrainerJockeyStats();
      }
      break;
    }
   } catch (Exception e) {
    // an error condition...
    System.out.println("Error in " + filename + ".JCP: line=" + parser.lineno()
      + " fld=" + fld + " name=" + name + "  fld_in=" + fld_in + "\n   " + e);
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
       if (!scr.m_track.equals("AJX") && !scr.m_track.equals("FPX") && !scr.m_track.equals("PRX"))
        if (scr.m_track.substring(2).equals("X") || scr.m_track.equals("FPK"))
         scr.m_track = scr.m_track.substring(0, 2);
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
 public Post findPost(int raceNo, int position)
 {
  Race race = findRace(raceNo);
  if (race != null) {
   for (Enumeration e = race.m_posts.elements(); e.hasMoreElements();) {
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
