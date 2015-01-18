package com.base;
/*
create table workout (
 HORSENAME                                VARCHAR2(30) NOT NULL,
 WORKDATE                                 DATE NOT NULL,
 WHEREWORKED                              VARCHAR2(10),
 DISTANCE                                 NUMBER(4,2),
 TRACKCONDITION                           VARCHAR2(2),
 SURFACE                                  VARCHAR2(21),
 HANDILY                                  CHAR(1),
 BREEZING                                 CHAR(1),
 GATE                                     CHAR(1),
 DOGS                                     CHAR(1),
 BULLET                                   CHAR(1),
 WORKQTY                                  NUMBER,
 WORKRANK                                 NUMBER,
constraint workout_pk PRIMARY KEY (HORSENAME, WORKDATE),
constraint workout_rf FOREIGN KEY (HORSENAME)
			REFERENCES horse (HORSENAME)
			ON DELETE CASCADE
);
*/

import java.util.*;

class Workout
{
	Date m_workDate = null;
	Properties m_props = new Properties();


}

