package com.base;
/**

create table post (
 TRACKABBR  							  VARCHAR2(3) NOT NULL ,
 RACEDATE   							  DATE NOT NULL,
 RACENO 								  NUMBER NOT NULL,
 POSTPOSITION   						  NUMBER NOT NULL,
 ENTRY									  CHAR(1),
 HORSENAME  							  VARCHAR2(25),
 EQUIPMENTCHG   						  VARCHAR2(12)
	 CHECK (EQUIPMENTCHG IN ('Blinkers on', 'Blinkers off')), 
 LASIX  								  CHAR(1),
 BUTE   								  CHAR(1),
 OWNER  								  VARCHAR2(40),
 TRAINER								  VARCHAR2(30),
 TRAINERSTARTS  						  NUMBER,
 TRAINERWINS							  NUMBER,
 TRAINERPLACES  						  NUMBER,
 TRAINERSHOWS   						  NUMBER,
 JOCKEY 								  VARCHAR2(30),
 JOCKEYSTARTS   						  NUMBER,
 JOCKEYWINS 							  NUMBER,
 JOCKEYPLACES   						  NUMBER,
 JOCKEYSHOWS							  NUMBER,
 WEIGHTALLOW							  NUMBER,
 RUNSTYLE   							  VARCHAR2(3),
 QUIRIN 								  NUMBER,
 PASEPAR2F  							  NUMBER,
 PASEPAR4F  							  NUMBER,
 PASEPAR6F  							  NUMBER,
 SPEEDPAR   							  NUMBER,
 LATEPASEPAR							  NUMBER,
 DAYSSINCELAST  						  NUMBER,
 CLAIMINGPURSE  						  NUMBER,
 LRDSTARTS  							  NUMBER,
 LRDWIN 								  NUMBER,
 LRDPLACE   							  NUMBER,
 LRDSHOW								  NUMBER,
 LRDEARNINGS							  NUMBER,
 LRTSTARTS  							  NUMBER,
 LRTWIN 								  NUMBER,
 LRTPLACE   							  NUMBER,
 LRTSHOW								  NUMBER,
 LRTEARNINGS							  NUMBER,
 LRTURFSTARTS   						  NUMBER,
 LRTURFWIN  							  NUMBER,
 LRTURFPLACE							  NUMBER,
 LRTURFSHOW 							  NUMBER,
 LRTURFEARNINGS 						  NUMBER,
 LRWETSTARTS							  NUMBER,
 LRWETWIN   							  NUMBER,
 LRWETPLACE 							  NUMBER,
 LRWETSHOW  							  NUMBER,
 LRWETEARNINGS  						  NUMBER,
 LATESTYEAR 							  NUMBER,
 LATESTYEARSTARTS   					  NUMBER,
 LATESTYEARWIN  						  NUMBER,
 LATESTYEARPLACE						  NUMBER,
 LATESTYEARSHOW 						  NUMBER,
 LATESTYEAREARNINGS 					  NUMBER,
 PREVIOUSYEAR   						  NUMBER,
 PREVIOUSYEARSTART  					  NUMBER,
 PREVIOUSYEARWIN						  NUMBER,
 PREVIOUSYEARPLACE  					  NUMBER,
 PREVIOUSYEARSHOW   					  NUMBER,
 PREVIOUSYEAREARNINGS   				  NUMBER,
 LIFETIMESTARTS 						  NUMBER,
 LIFETIMEWIN							  NUMBER,
 LIFETIMEPLACE  						  NUMBER,
 LIFETIMESHOW   						  NUMBER,
 LIFETIMEEARNINGS   					  NUMBER,
 COMMENTS				  LONG,
constraint post_pk  PRIMARY KEY (TRACKABBR, RACEDATE, RACENO, POSTPOSITION),
constraint post_rf1 FOREIGN KEY (TRACKABBR, RACEDATE, RACENO)
			REFERENCES race (TRACKABBR, RACEDATE, RACENO)
			ON DELETE CASCADE,
constraint post_rf2 FOREIGN KEY (HORSENAME)
			REFERENCES horse (HORSENAME)
);
*/

import com.base.Handicap;
import com.base.Horse;
import com.base.Workout;

import java.util.*;

class Post
{
 String     m_track;
 Date       m_raceDate;
 int        m_raceNo;
 int        m_postPosition;
 String     cloth                = null;
 Properties m_props              = new Properties();
 String     m_horseName          = null;
 String     m_horseNameP         = null;
 int        m_weight             = 0;
 int        m_age                = 0;
 int        m_daysSinceLast      = 0;
 int        m_daysSinceWork1     = 0;
 int        m_daysSinceWork2     = 0;
 int        m_daysSinceWork3     = 0;
 int        m_finishPosLast      = 99;
 int        m_topRanks           = 0;
 int        m_pointsAdv          = 0;
 int        m_betfactors         = 0;
 int        m_jkyfactors         = 0;
 int        m_trnfactors         = 0;
 int        m_trnJkyFactors      = 0;
 int        m_biasRank           = 0;
 int        m_quirin             = 0;
 int        m_claim              = 0;
 String     m_betfactorsPR       = "";
 String     m_jkyfactorsPR       = "";
 String     m_trnfactorsPR       = "";
 String     m_trnJkyfactorsPR    = "";
 String     m_trnJkyfactorsSD    = "";
 String     m_trnJkyfactorsTYP   = "";
 String     m_trnJkyfactorsSEX   = "";
 String     m_trnJkyfactorsAGE   = "";
 String     m_trnJkyfactorsFTS   = "";
 String     m_trnJkyfactorsLAY   = "";
 String     m_trnJkyfactorsFAV   = "";
 String     m_trnJkyfactorsTRN   = "";
 String     m_trnJkyfactorsWP    = "";
 String     m_trnJkyfactorsSOURCE = "";
 String     m_morningLine        = "";
 double     m_morningLineD       = 0;
 double     m_primePower         = 0;
 double     m_truLineD           = 0;
 String     m_truLine            = "";
 String     m_truLineDO          = "";
 String     m_repRaceDtl         = "";
 String     m_repRacePurseComp   = "";
 String     m_repRaceClassChg    = "";
 String     m_lastRaceTrackClass = "";
 String     m_lastRacePurseComp  = "";
 double     m_lastRaceClassChg   = 0;
 String     m_lastRaceClaimComp  = "";
 double     m_lastRaceClaimChg   = 0;
 boolean    m_trainerClsChgDownOK = false;
 boolean    m_jockeyChgToday      = false;
 boolean    m_trainerChgToday     = false;
 boolean    m_jockeyTodayPrevWin  = false;
 double     m_trainerClsChgDownROI = 0;
 boolean    m_trainer45LayoffOK  = false;
 double     m_trainer45LayoffROI = 0;
 boolean    m_trainerClm1bk      = false;
 boolean    m_freshHorse         = false;
 boolean    m_last25             = false;
 boolean    m_firstClmRace       = false;
 boolean    m_lowestClmPrice     = false;
 boolean    m_lastMdnClmWin      = false;
 boolean    m_bigWinLast         = false;
 String     m_formCycle          = "";
 String     m_formCycle2         = "";
 String     m_formCycle3         = "";
 String     m_formCycle4         = "";
 String     m_formCycle5         = "";
 String     m_trainerName        = "";
 String     m_trainerNamePT      = "";
 String     m_trainerNamePT1     = "";
 String     m_trainerNamePT2     = "";
 String     m_jockeyName         = "";
 String     m_ownerName          = "";
 String     m_sex                = "";
 String     m_runStyle           = "";
 String[]   horseFlows           = { " ", " ", " ", " ", " ", " ", " ", " ",
   " ", " ",  " ", " ", " ", " ", " ", " ", " ", " ",
   " ", " "    };
 int        cntHorseFlows        = -1;
 String     m_sireName           = "";
 String     m_sireAWD            = "";
 int        m_sireAPRS           = 0;
 String     m_damName            = "";
 String     m_damAWD             = "";
 int        m_damAPRS            = 0;
 String     m_damSireName        = "";
 String     m_damSireAWD         = "";
 int        m_damSireAPRS        = 0;
 String     m_sireSireName       = "";
 String     m_whereBred          = "";
 String     m_sireTS             = "";
 String     m_sireTSp            = "";
 String     m_sireTS2            = "";
 String     m_sireTSPI           = "";
 String     m_sireTSPI2          = "";
 String     m_sireTSPI3          = "";
 String     m_otherFactors       = "";
 String     m_kimsPT             = "";
 String     m_kimsT1             = "";
 String     m_kimsT2             = "";
 String     m_kimsT3             = "";
 String     m_kimsT4             = "";
 String     m_kimsTOB            = "";
 String     m_kimsJ1             = "";
 String     m_kimsEPS            = "";
 String     m_kimsTT             = "";
 String     m_kimsCS             = "";
 String     m_kimsTTCS           = "";
 String     m_kimsPP             = "";
 String     m_ownerTrn           = "";
 String     m_ownerBrd           = "";
 String     m_trnJkyPct          = "";
 String     m_trnJkyStat         = "";
 String     m_trnJky             = "";
 String     m_trnJkyHot          = "";
 String     m_trnJkyTrkStat      = "";
 String     m_trnJkyTrkL25Stat5  = "";
 String     m_trnJkyTrkL25Stat10 = "";
 String     m_trnOwnStat         = "";
 String     m_trnSurfaceStat     = "";
 int        m_trnPct             = 0;
 int        m_jkyPct             = 0;
 int        m_trnPctM            = 0;
 double     m_trnROIM            = 0;
 int        m_trnPctF            = 0;
 int        m_jkyPctF            = 0;
 int        m_trnJkyPctF         = 0;
 String     m_trnMeetStatD       = "";
 String     m_trnMeetStatS       = "";
 String     m_jkyMeetStatD       = "";
 String     m_jkyMeetStatS       = "";
 String     m_4furlongBullet     = "";
 String     m_5furlongBullet     = "";
 String     m_bias               = "";
 String     m_work3Short         = "";
 String     m_work4Week          = "";
 int        m_workCnt            = 0;
 int        m_biasN              = 0;
 String     m_finishPos          = "";
 String     m_odds               = "";
 String     m_winPayoff          = "";
 String     m_placePayoff        = "";
 String     m_showPayoff         = "";
 Horse      m_horse              = new Horse();
 Workout    m_work[]             = new Workout[12];
 Vector     m_performances       = new Vector();
 Vector     m_trainerJockeyStats = new Vector();
 Handicap   m_handicap           = null;
 public Post() {
  for (int i = 0; i < m_work.length; i++)
   m_work[i] = new Workout();
 }
}
