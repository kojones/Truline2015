package com.base;
import java.util.*;
class TrainerJockeyStats
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
}
