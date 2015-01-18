package com.base;
/**
 *
 *	A GUI screen to identify race results for the Truline program.
 *
 */
import com.base.Bris;
import com.base.BrisMCP;
import com.base.BrisJCP;
import com.base.GUI;
import com.base.GUIReport;
import com.base.Handicap;
import com.base.Lib;
import com.base.Log;
import com.base.Post;
import com.base.Race;
// import com.base.Scratch;
import com.base.ScratchFile;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import com.mains.Truline;
class GUIResults extends Dialog implements ActionListener
{
 private TextField scrtitle, horse1, horse2, horse3, horse4, horse5, horse6,
 horse7, horse8, horse9, horse10;
private TextField horse11, horse12, horse13, horse14, horse15, horse16,
 horse17, horse18, horse19, horse20;
private GUI       m_gui;
private int       m_horse;
private Race      m_race;
public GUIResults(Frame fr) {
super(fr, Truline.title + " - Enter Results");
}
public void init(GUI gui, Bris bris, int raceNbr)
{
m_gui = gui;
// Create the display
// width, height
addWindowListener(new WindowAdapter() {
 public void windowClosing(WindowEvent e)
 {
  dispose();
 }
});
setSize(new Dimension(100, 100));
setBackground(Color.white);
setFont(new Font("Helvetica", Font.PLAIN, 14));
GridBagLayout gridbag = new GridBagLayout();
setLayout(gridbag);
GridBagConstraints c = new GridBagConstraints();
c.fill = GridBagConstraints.BOTH;
c.weightx = 1.0;
setRow(c, gridbag, new Label("PP-Horse"), new Label("Enter finish position"));
displayHorses(gridbag, c, bris, raceNbr);
Panel panel1 = new Panel();
panel1.setLayout(new BorderLayout());
Button OKButton = new Button(" OK ");
OKButton.setActionCommand("ok");
OKButton.addActionListener(this);
panel1.add(OKButton, BorderLayout.CENTER);
Panel panel2 = new Panel();
panel2.setLayout(new BorderLayout());
Button cancelButton = new Button("Cancel");
cancelButton.setActionCommand("cancel");
cancelButton.addActionListener(this);
panel2.add(cancelButton, BorderLayout.CENTER);
setRow(c, gridbag, panel2, panel1);
pack();
show();
}
public void init(GUI gui, BrisMCP brisMCP, int raceNbr)
{
m_gui = gui;
// Create the display
// width, height
addWindowListener(new WindowAdapter() {
 public void windowClosing(WindowEvent e)
 {
  dispose();
 }
});
setSize(new Dimension(100, 100));
setBackground(Color.white);
setFont(new Font("Helvetica", Font.PLAIN, 14));
GridBagLayout gridbag = new GridBagLayout();
setLayout(gridbag);
GridBagConstraints c = new GridBagConstraints();
c.fill = GridBagConstraints.BOTH;
c.weightx = 1.0;
setRow(c, gridbag, new Label("PP-Horse"), new Label("Enter finish position"));
displayHorses(gridbag, c, brisMCP, raceNbr);
Panel panel1 = new Panel();
panel1.setLayout(new BorderLayout());
Button OKButton = new Button(" OK ");
OKButton.setActionCommand("ok");
OKButton.addActionListener(this);
panel1.add(OKButton, BorderLayout.CENTER);
Panel panel2 = new Panel();
panel2.setLayout(new BorderLayout());
Button cancelButton = new Button("Cancel");
cancelButton.setActionCommand("cancel");
cancelButton.addActionListener(this);
panel2.add(cancelButton, BorderLayout.CENTER);
setRow(c, gridbag, panel2, panel1);
pack();
show();
}
public void init(GUI gui, BrisJCP brisJCP, int raceNbr)
{
m_gui = gui;
// Create the display
// width, height
addWindowListener(new WindowAdapter() {
 public void windowClosing(WindowEvent e)
 {
  dispose();
 }
});
setSize(new Dimension(100, 100));
setBackground(Color.white);
setFont(new Font("Helvetica", Font.PLAIN, 14));
GridBagLayout gridbag = new GridBagLayout();
setLayout(gridbag);
GridBagConstraints c = new GridBagConstraints();
c.fill = GridBagConstraints.BOTH;
c.weightx = 1.0;
setRow(c, gridbag, new Label("PP-Horse"), new Label("Enter finish position"));
displayHorses(gridbag, c, brisJCP, raceNbr);
Panel panel1 = new Panel();
panel1.setLayout(new BorderLayout());
Button OKButton = new Button(" OK ");
OKButton.setActionCommand("ok");
OKButton.addActionListener(this);
panel1.add(OKButton, BorderLayout.CENTER);
Panel panel2 = new Panel();
panel2.setLayout(new BorderLayout());
Button cancelButton = new Button("Cancel");
cancelButton.setActionCommand("cancel");
cancelButton.addActionListener(this);
panel2.add(cancelButton, BorderLayout.CENTER);
setRow(c, gridbag, panel2, panel1);
pack();
show();
}
public boolean displayHorses(GridBagLayout gridbag, GridBagConstraints c,
 Bris bris, int raceNbr)
{
for (Enumeration e = bris.m_races.elements(); e.hasMoreElements();) {
 Race race = (Race) e.nextElement();
 if (raceNbr == race.m_raceNo) {
  m_race = race;
  writeScrMenu(gridbag, c, race);
  return true;
 }
}
return false;
}
public boolean displayHorses(GridBagLayout gridbag, GridBagConstraints c,
  BrisMCP brisMCP, int raceNbr)
 {
 for (Enumeration e = brisMCP.m_races.elements(); e.hasMoreElements();) {
  Race race = (Race) e.nextElement();
  if (raceNbr == race.m_raceNo) {
   m_race = race;
   writeScrMenu(gridbag, c, race);
   return true;
  }
 }
 return false;
 }
public boolean displayHorses(GridBagLayout gridbag, GridBagConstraints c,
  BrisJCP brisJCP, int raceNbr)
 {
 for (Enumeration e = brisJCP.m_races.elements(); e.hasMoreElements();) {
  Race race = (Race) e.nextElement();
  if (raceNbr == race.m_raceNo) {
   m_race = race;
   writeScrMenu(gridbag, c, race);
   return true;
  }
 }
 return false;
 }
public void writeScrMenu(GridBagLayout gridbag, GridBagConstraints c, Race race)
{
// Display each horse in the race.
m_horse = 0;
for (Enumeration e = race.m_posts.elements(); e.hasMoreElements();) {
 Post post = (Post) e.nextElement();
 /*
  * if (post.m_handicap == null || post.m_horseName == null) continue; //
  * position is empty
  */
 /* horse is scratched - no results */
 String entry = post.m_props.getProperty("ENTRY", "");
 if (entry.equals("S"))
  continue;
 m_horse++;
 switch (m_horse) {
  case 1:
   horse1 = new TextField((post.m_finishPos.equals("99") ? "" : post.m_finishPos), 2);
   setRow(c, gridbag,
     new Label(Lib.pad(post.cloth, 3) + Lib.pad(post.m_horseName, 16)),
     horse1);
   continue;
  case 2:
   horse2 = new TextField((post.m_finishPos.equals("99") ? "" : post.m_finishPos), 2);
   setRow(c, gridbag,
     new Label(Lib.pad(post.cloth, 3) + Lib.pad(post.m_horseName, 16)),
     horse2);
   continue;
  case 3:
   horse3 = new TextField((post.m_finishPos.equals("99") ? "" : post.m_finishPos), 2);
   setRow(c, gridbag,
     new Label(Lib.pad(post.cloth, 3) + Lib.pad(post.m_horseName, 16)),
     horse3);
   continue;
  case 4:
   horse4 = new TextField((post.m_finishPos.equals("99") ? "" : post.m_finishPos), 2);
   setRow(c, gridbag,
     new Label(Lib.pad(post.cloth, 3) + Lib.pad(post.m_horseName, 16)),
     horse4);
   continue;
  case 5:
   horse5 = new TextField((post.m_finishPos.equals("99") ? "" : post.m_finishPos), 2);
   setRow(c, gridbag,
     new Label(Lib.pad(post.cloth, 3) + Lib.pad(post.m_horseName, 16)),
     horse5);
   continue;
  case 6:
   horse6 = new TextField((post.m_finishPos.equals("99") ? "" : post.m_finishPos), 2);
   setRow(c, gridbag,
     new Label(Lib.pad(post.cloth, 3) + Lib.pad(post.m_horseName, 16)),
     horse6);
   continue;
  case 7:
   horse7 = new TextField((post.m_finishPos.equals("99") ? "" : post.m_finishPos), 2);
   setRow(c, gridbag,
     new Label(Lib.pad(post.cloth, 3) + Lib.pad(post.m_horseName, 16)),
     horse7);
   continue;
  case 8:
   horse8 = new TextField((post.m_finishPos.equals("99") ? "" : post.m_finishPos), 2);
   setRow(c, gridbag,
     new Label(Lib.pad(post.cloth, 3) + Lib.pad(post.m_horseName, 16)),
     horse8);
   continue;
  case 9:
   horse9 = new TextField((post.m_finishPos.equals("99") ? "" : post.m_finishPos), 2);
   setRow(c, gridbag,
     new Label(Lib.pad(post.cloth, 3) + Lib.pad(post.m_horseName, 16)),
     horse9);
   continue;
  case 10:
   horse10 = new TextField((post.m_finishPos.equals("99") ? "" : post.m_finishPos), 2);
   setRow(c, gridbag,
     new Label(Lib.pad(post.cloth, 3) + Lib.pad(post.m_horseName, 16)),
     horse10);
   continue;
  case 11:
   horse11 = new TextField((post.m_finishPos.equals("99") ? "" : post.m_finishPos), 2);
   setRow(c, gridbag,
     new Label(Lib.pad(post.cloth, 3) + Lib.pad(post.m_horseName, 16)),
     horse11);
   continue;
  case 12:
   horse12 = new TextField((post.m_finishPos.equals("99") ? "" : post.m_finishPos), 2);
   setRow(c, gridbag,
     new Label(Lib.pad(post.cloth, 3) + Lib.pad(post.m_horseName, 16)),
     horse12);
   continue;
  case 13:
   horse13 = new TextField((post.m_finishPos.equals("99") ? "" : post.m_finishPos), 2);
   setRow(c, gridbag,
     new Label(Lib.pad(post.cloth, 3) + Lib.pad(post.m_horseName, 16)),
     horse13);
   continue;
  case 14:
   horse14 = new TextField((post.m_finishPos.equals("99") ? "" : post.m_finishPos), 2);
   setRow(c, gridbag,
     new Label(Lib.pad(post.cloth, 3) + Lib.pad(post.m_horseName, 16)),
     horse14);
   continue;
  case 15:
   horse15 = new TextField((post.m_finishPos.equals("99") ? "" : post.m_finishPos), 2);
   setRow(c, gridbag,
     new Label(Lib.pad(post.cloth, 3) + Lib.pad(post.m_horseName, 16)),
     horse15);
   continue;
  case 16:
   horse16 = new TextField((post.m_finishPos.equals("99") ? "" : post.m_finishPos), 2);
   setRow(c, gridbag,
     new Label(Lib.pad(post.cloth, 3) + Lib.pad(post.m_horseName, 16)),
     horse16);
   continue;
  case 17:
   horse17 = new TextField((post.m_finishPos.equals("99") ? "" : post.m_finishPos), 2);
   setRow(c, gridbag,
     new Label(Lib.pad(post.cloth, 3) + Lib.pad(post.m_horseName, 16)),
     horse17);
   continue;
  case 18:
   horse18 = new TextField((post.m_finishPos.equals("99") ? "" : post.m_finishPos), 2);
   setRow(c, gridbag,
     new Label(Lib.pad(post.cloth, 3) + Lib.pad(post.m_horseName, 16)),
     horse18);
   continue;
  case 19:
   horse19 = new TextField((post.m_finishPos.equals("99") ? "" : post.m_finishPos), 2);
   setRow(c, gridbag,
     new Label(Lib.pad(post.cloth, 3) + Lib.pad(post.m_horseName, 16)),
     horse19);
   continue;
  case 20:
   horse20 = new TextField((post.m_finishPos.equals("99") ? "" : post.m_finishPos), 2);
   setRow(c, gridbag,
     new Label(Lib.pad(post.cloth, 3) + Lib.pad(post.m_horseName, 16)),
     horse20);
   continue;
 }
 setRow(c, gridbag,
   new Label(Lib.pad(post.cloth, 3) + Lib.pad(post.m_horseName, 16)),
   new TextField(" ", 10));
}
}
private void setRow(GridBagConstraints c, GridBagLayout gridbag,
 Component obj1, Component obj2)
{
c.gridwidth = GridBagConstraints.RELATIVE;
gridbag.setConstraints(obj1, c);
add(obj1);
c.gridwidth = GridBagConstraints.REMAINDER; // end row
gridbag.setConstraints(obj2, c);
add(obj2);
c.fill = GridBagConstraints.BOTH;
}
/**
* Action listener for the Buttons.
*/
public void actionPerformed(ActionEvent e)
{
String cmd = e.getActionCommand();
if (cmd.equals("ok")) {
 // Update each horse in the race.
 m_horse = 0;
 for (Enumeration e1 = m_race.m_posts.elements(); e1.hasMoreElements();) {
  Post post = (Post) e1.nextElement();
  /*
   * if (post.m_handicap == null || post.m_horseName == null) continue; //
   * position is empty
   */
  /* horse is scratched - no results */
  String entry = post.m_props.getProperty("ENTRY", "");
  if (entry.equals("S"))
   continue;
  m_horse++;
  switch (m_horse) {
   case 1:
    post.m_finishPos = horse1.getText();
    continue;
   case 2:
    post.m_finishPos = horse2.getText();
    continue;
   case 3:
    post.m_finishPos = horse3.getText();
    continue;
   case 4:
    post.m_finishPos = horse4.getText();
    continue;
   case 5:
    post.m_finishPos = horse5.getText();
    continue;
   case 6:
    post.m_finishPos = horse6.getText();
    continue;
   case 7:
    post.m_finishPos = horse7.getText();
    continue;
   case 8:
    post.m_finishPos = horse8.getText();
    continue;
   case 9:
    post.m_finishPos = horse9.getText();
    continue;
   case 10:
    post.m_finishPos = horse10.getText();
    continue;
   case 11:
    post.m_finishPos = horse11.getText();
    continue;
   case 12:
    post.m_finishPos = horse12.getText();
    continue;
   case 13:
    post.m_finishPos = horse13.getText();
    continue;
   case 14:
    post.m_finishPos = horse14.getText();
    continue;
   case 15:
    post.m_finishPos = horse15.getText();
    continue;
   case 16:
    post.m_finishPos = horse16.getText();
    continue;
   case 17:
    post.m_finishPos = horse17.getText();
    continue;
   case 18:
    post.m_finishPos = horse18.getText();
    continue;
   case 19:
    post.m_finishPos = horse19.getText();
    continue;
   case 20:
    post.m_finishPos = horse20.getText();
    continue;
  }
 }
 // Make it affective
 m_gui.clear();
 ScratchFile.m_scr.init2();
 if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("MCP")) {
  for (Enumeration el = m_gui.m_brisMCP.m_races.elements(); el
    .hasMoreElements();) {
   Race race = (Race) el.nextElement();
   saveResult(race);
   Handicap.compute(race);
  }
  GUIReport rpt4 = new GUIReport();
  rpt4.generate(m_gui.m_filename, m_gui.m_brisMCP, m_gui, m_gui.m_raceNbr);
 } else if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("JCP")) {
   for (Enumeration el = m_gui.m_brisJCP.m_races.elements(); el
     .hasMoreElements();) {
    Race race = (Race) el.nextElement();
    saveResult(race);
    Handicap.compute(race);
   }
   GUIReport rpt4 = new GUIReport();
   rpt4.generate(m_gui.m_filename, m_gui.m_brisJCP, m_gui, m_gui.m_raceNbr);
  } else {
  for (Enumeration el = m_gui.m_bris.m_races.elements(); el.hasMoreElements();) {
   Race race = (Race) el.nextElement();
   saveResult(race);
   Handicap.compute(race);
  }
  GUIReport rpt4 = new GUIReport();
  rpt4.generate(m_gui.m_filename, m_gui.m_bris, m_gui, m_gui.m_raceNbr);
 }
 // Close SCR file
 ScratchFile.m_scr.close();

 dispose();
} else if (cmd.equals("cancel")) {
 dispose();
}
}
private void saveResult(Race race)
{
 for (Enumeration e = race.m_posts.elements(); e.hasMoreElements();) {
  Post post = (Post) e.nextElement();
  if (post.m_finishPos.equals(""))
   post.m_finishPos = "99";
  String xrdLine = race.m_track + "," + race.m_props.getProperty("RACEDATE") + ","
    + race.m_raceNo + ",," + race.m_surfaceLC + ",,,,,,,,,,,," + race.m_trackCond + ","
    + post.m_postPosition + ",,\"" + post.m_horseName + "\",,,,,,,,,,"
    + (post.m_props.getProperty("ENTRY", " ").equals("S") ? "," : post.m_finishPos+",")
    + ",,,,,,0.00,,,,,,,,,,,,," 
    + post.m_props.getProperty("PROGRAMNUMBER", " ")
    + ",,,,,,,,,,,,,,,,,,,,,,,,,0,0,0,,,,,,,,,,,,,,,,,,,,,,,"
    + ",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"
    + ",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"
    + ",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"    
    + "\n";
  ScratchFile.m_scr.print(xrdLine);
 }
}
}
