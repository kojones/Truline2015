package com.base;
/**
 create table performance (
 TRACKABBR  							  VARCHAR2(3) NOT NULL,
 RACEDATE   							  DATE NOT NULL,
 RACENO 								  NUMBER NOT NULL,
 POSTPOSITION   						  NUMBER NOT NULL,
 PPTRACKABBR  							  VARCHAR2(3) NOT NULL,
 PPRACEDATE   							  DATE NOT NULL,
 PPRACENO 								  NUMBER NOT NULL,
 PPPOSTPOSITION   						  NUMBER NOT NULL,
 HORSENAME  							  VARCHAR2(25) NOT NULL,
 DAYSSINCEPREVRACE  					  NUMBER,
 CHUTE  								  CHAR(1),
 FAVORITE   							  CHAR(1),
 ENTRY  								  CHAR(1),
 RACECLASSIFICATION 					  VARCHAR2(25),
 BRIS_ODDS  							  NUMBER,
 WINMARGIN  							  NUMBER(4,2),
 PLACEMARGIN							  NUMBER(4,2),
 SHOWMARGIN 							  NUMBER(4,2),
 FINISHPOS  							  NUMBER,
 RACEAMT								  NUMBER,
 RACEDISTANCE   						  NUMBER(4,2),
 FIRSTCALLLEN   						  NUMBER,
 FIRSTCALLLENLDR						  NUMBER,
 LENGTHS2   							  NUMBER,
 LENGTHS3   							  NUMBER,
 LENGTHS4   							  NUMBER,
 LENLDRMGN1 							  NUMBER(3,2),
 LENLDRMGN2 							  NUMBER(3,2),
 LENLDRMGN3 							  NUMBER(3,2),
 LENLDRMGN4 							  NUMBER(3,2),
 PROBLEM1   							  VARCHAR2(20),
 PROBLEM2   							  VARCHAR2(20),
 PROBLEM3   							  VARCHAR2(20),
 PROBLEM4   							  VARCHAR2(20),
 FRACTION2F 							  NUMBER(6,2),
 FRACTION3F 							  NUMBER(6,2),
 FRACTION4F 							  NUMBER(6,2),
 FRACTION5F 							  NUMBER(6,2),
 FRACTION6F 							  NUMBER(6,2),
 FRACTION7F 							  NUMBER(6,2),
 FRACTION8F 							  NUMBER(6,2),
 FRACTION10F							  NUMBER(6,2),
 FRACTION12F							  NUMBER(6,2),
 FRACTION14F							  NUMBER(6,2),
 FRACTION16F							  NUMBER(6,2),
 FRACTION1                                NUMBER(6,2),
 FRACTION2                                NUMBER(6,2),
 FRACTION3                                NUMBER(6,2),
 FINALTIME                                NUMBER(6,2),
 RACEWEIGHT 							  NUMBER,
 BLINKERS   							  CHAR(1),
 LASIX  								  CHAR(1),
 BRUTE  								  CHAR(1),
 ODDSRACE   							  NUMBER(5,2),
 TOTALTIME  							  NUMBER(6,2),
 PAYOFF 								  NUMBER,
 PAYOFFCODE 							  CHAR(1),
 EXOTICPAYOFF   						  NUMBER,
 EXOTICPAYOFFTYPE   					  NUMBER,
 CONDITIONS 							  LONG,
 constraint performance_pk  PRIMARY KEY (TRACKABBR, RACEDATE, RACENO, POSTPOSITION)
 );
 */
import java.util.*;
class Performance
{
 String     m_track;
 Date       m_raceDate;
 int        m_raceNo;
 int        m_postPosition;
 String     ppTrack;
 int        ppDistance;
 Date       ppRaceDate;
 int        ppRaceNo;
 int        ppPostPosition;
 boolean    isExcluded;
 boolean    isRoute;
 Properties m_props = new Properties();
 double     drfSpeed;
 double     variant;                   // Adjustment applied when used as a rep
                                        // race.
 // adjust running line by the amount of the variant.
 // 20% for first time. (FS)
 // 50% for second time, (SS) and
 // 100% to the final time.(FT)
 // The TT and CS are obtained by subtracting.
 // Adjusted values:
 double     fs      = 0;                // First Call (Front Speed)
 // For sprints this will be the time to the quarter-mile pole.
 // This is a guage of early speed, particularly to identify a
 // sole front runner.
 // In routes [8F to 10F] the first call is the half-mile pole.
 // Races longer than 10F usually will have the 6F pole as first call.
 double     ss      = 0;                // Second Call (Sustained Speed)
 // Sprints, this is the half mile point. This is a
 // In Routes it is at 6F pole. Routes over 1-13/16 mile
 // the second call may be the one mile pole.
 double     ft      = 0;                // Final time
 double     tt      = 0;                // Turn time (ss - fs)
 double     cs      = 0;                // Closing time (ft - ss)
 // For races at 6,7,8,9,10F this wil be the final quarter.
 // For races at 5.5, 6.5, 7.5, 8.5, 9.5, and 10.5F the
 // last 1/16th.
 double     as      = 0;                // Adjusted speed = DRF Speed figure +
                                        // DRF Variant as modified.
 /**
  * Quick sort of a vector of performances by Race Date Order by most recent
  * first.
  */
 public static void qSort(Vector v)
 {
  Performance a, b;
  int len = v.size();
  for (int i = 0; i < len; i++) {
   for (int j = i + 1; j < len; j++) {
    // Compare keys
    a = (Performance) v.elementAt(i);
    b = (Performance) v.elementAt(j);
    if (a.ppRaceDate.compareTo(b.ppRaceDate) < 0) {
     // Swap elements
     v.setElementAt(b, i);
     v.setElementAt(a, j);
    }
   }
  }
 }
}
