package com.base;
/**

create table scratch (
 TRACKABBR  							  VARCHAR2(3) NOT NULL ,
 RACEDATE   							  DATE NOT NULL,
 RACENO 								  NUMBER NOT NULL,
 POSTPOSITION   						  NUMBER NOT NULL,
 HORSENAME  							  VARCHAR2(25),
constraint post_pk  PRIMARY KEY (TRACKABBR, RACEDATE, RACENO, POSTPOSITION),
constraint post_rf1 FOREIGN KEY (TRACKABBR, RACEDATE, RACENO)
			REFERENCES race (TRACKABBR, RACEDATE, RACENO)
			ON DELETE CASCADE,
constraint post_rf2 FOREIGN KEY (HORSENAME)
			REFERENCES horse (HORSENAME)
);
*/

import java.util.*;

class Scratch
{
	String m_track;
	Date m_raceDate;
	int m_raceNo;
	int m_postPosition;
	String cloth = null;
	Properties m_props = new Properties();
	
	String m_horseName = null;

	public Scratch()
	{
	}


}

