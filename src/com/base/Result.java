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

import java.util.*;

class Result
{
 String     m_track;
 Date       m_raceDate;
 int        m_raceNo;
 String     m_surface;
 int        m_postPosition;
 String     m_programNumber = null;
 Properties m_props         = new Properties();
 String     m_horseName     = null;
 String     m_trackCond     = "";
 int        m_weight        = 0;
 String     m_finishPos     = "";
 public Result() {
 }
}
