package com.base;
/**
*	Handles the data related to one race.
(KEY)	
  | VARCHAR[3] trackabbr;		
  |			Three Character track abbreviation as
  |			assigned by BRIS.
  |			References trackabbr in track table.
  |	DATE	RaceDate;  
  |	NUMBER  RaceNo;
  ---

create table race (
TRACKABBR   	VARCHAR2(3) NOT NULL,
RACEDATE		DATE NOT NULL,
RACENO  		NUMBER NOT NULL,
RACEIDNAME  	VARCHAR2(9),
SURFACE 		CHAR(1) CHECK (SURFACE IN (
			'D'=Dirt, 'T'=Turf, 'd'=Dirt_Inner, 't'=Turf_Inner)),
DISTANCE		NUMBER(4,2),
ABOUT   		CHAR(1),
RACETYPE		CHAR(20) CHECK  (RACETYPE IN ( 
					 'G1-Stake',
					 'G2-Stake',
					 'G3-Stake',
					 'N-nongraded stake',
					 'A-allowance',
					 'R-Starter Alw',
					 'T-Starter handicap',
					 'C-Claiming',
					 'S-Maiden sp wt',
					 'M-Maiden claimer')),
AGESEX  		VARCHAR2(3),
FILLIES 		CHAR(1),
PURSE   		NUMBER,
CLAIMINGPURSE   NUMBER,
TRACKRECORD 	NUMBER(5,2),
CONDITIONCODE   VARCHAR2(2),
NBRHORSES   	NUMBER,
COMMENTS		LONG,
constraint race_pk PRIMARY KEY (TRACKABBR, RACEDATE, RACENO),
constraint race_rf FOREIGN KEY (TRACKABBR) REFERENCES track (TRACKABBR)
)*/

import java.util.*;

public class Race
{
 String     m_track;
 Date       m_raceDate;
 int        m_raceNo;
 int        m_raceYear;
 Properties m_props          = new Properties();
 int        m_distance       = 0;                     // Race Distance in yards
 double     m_purseClass     = 0;
 double     m_trackClass     = 0;
 int        m_purse          = 0;
 int        m_claim          = 0;
 String     m_surface        = "";
 String     m_surfaceResult  = "";
 String     m_surfaceLC      = "";
 String     m_allweather     = "";
 String     m_raceType       = "";
 String     m_age            = "";
 String     m_bettable1      = "";
 String     m_bettable2      = "";
 String     m_bettable3      = "";
 String     m_ignoreRunLine  = "";
 String     m_resultsPosted  = "";
 String     m_runStyleProfile= "";
 int        m_cnthorses      = 0;
 int        m_cntnrl         = 0;
 double     m_pctNRL         = 0;
 int        m_cnt1st         = 0;
 int        m_cnttrnown      = 0;
 int        m_cntbrdown      = 0;
 int        m_cnt$d          = 0;
 int        m_cntnrlML       = 0;
 Date       m_recencyDate;
 String     m_trackCond      = "Fast";
 String     m_trackCondResult= "";
 String     m_cloth1         = "";
 String     m_win1           = "";
 String     m_place1         = "";
 String     m_show1          = "";
 String     m_cloth2         = "";
 String     m_win2           = "";
 String     m_place2         = "";
 String     m_show2          = "";
 String     m_cloth3         = "";
 String     m_win3           = "";
 String     m_place3         = "";
 String     m_show3          = "";
 String     m_cloth4         = "";
 String     m_exactaPayoff   = "";
 String     m_trifectaPayoff = "";
 String     m_superPayoff    = "";
 String     m_pick3Payoff    = "";
 String     m_pick4Payoff    = "";
 String     m_pick5Payoff    = "";
 String     m_pick6Payoff    = "";
 String     m_doublePayoff   = "";
 Vector     m_posts          = new Vector();          // List of Post elements
                                                       // (horses in a race)
 // handicap data
 double     m_maxvariant;
 boolean    m_useMaiden      = false;
 Date       m_cutoff;                                 // date associated with
                                                       // m_maxdays
 int        m_maxdays;                                // period to consider for
                                                       // rep races (how many
                                                       // days to go back)
 String[]   raceFlows        = { "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" };
 String[]   raceFlowsAK      = { "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" };
 int        cntRaceFlows     = -1;
 int        cntRaceFlowsAK   = -1;
 int        cntHorseFlows    = 0;
 int        totalPoints    = 0;
 Vector     ranking;                                  // Vector of Post objects
                                                       // sorted by Bonus
                                                       // points.
}
