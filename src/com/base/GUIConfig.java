package com.base;
/**
 *
 *	A GUI configuration screen for the truline 2000 program.
 *
 */
import com.base.GUIReport;
import com.base.Handicap;
import com.base.Race;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import com.mains.Truline;
class GUIConfig extends Dialog implements ActionListener
{
 private TextField recency, maxdays, maxvariant, maiden, sirePoints,
                   bNumberFlowBet, tNumberFlowBet, jNumberFlowBet, trnJkyFlowBet,
                   datadir, fontsize, printProgram, shell;
 private GUI       m_gui;
 public GUIConfig(Frame fr) {
  super(fr, Truline.title + " - Configuration");
 }
 public void init(GUI gui)
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
  recency = new TextField(Truline.userProps.getProperty("RecencyDays", "28"), 5);
  setRow(c, gridbag, new Label("Recency Days"), recency);
  maxdays = new TextField(Truline.userProps.getProperty("MaxDays", "120"), 5);
  setRow(c, gridbag, new Label("Max Days"), maxdays);
  maxvariant = new TextField(Truline.userProps.getProperty("MaxVariant", "25"),5);
  setRow(c, gridbag, new Label("Max Variant"), maxvariant);
  maiden = new TextField(Truline.userProps.getProperty("UseMaiden", "Y"), 2);
  setRow(c, gridbag, new Label("Use Maiden"), maiden);
  sirePoints = new TextField(Truline.userProps.getProperty("SirePoints", "N"), 2);
  setRow(c, gridbag, new Label("Sire Points?"), sirePoints);
  bNumberFlowBet = new TextField(Truline.userProps.getProperty("B-NumberIndicators", "1"), 2);
  setRow(c, gridbag, new Label("B-Number Indicators"), bNumberFlowBet);
  tNumberFlowBet = new TextField(Truline.userProps.getProperty("T-NumberIndicators", "1"), 2);
  setRow(c, gridbag, new Label("T-Number Indicators"), tNumberFlowBet);
  jNumberFlowBet = new TextField(Truline.userProps.getProperty("J-NumberIndicators", "1"), 2);
  setRow(c, gridbag, new Label("J-Number Indicators"), jNumberFlowBet);
  trnJkyFlowBet = new TextField(Truline.userProps.getProperty("TrnJkyPercent", "50"), 2);
  setRow(c, gridbag, new Label("Trn-Jky Percent"), trnJkyFlowBet);
  // betFactorVersion = new TextField(Truline.userProps.getProperty(
  //   "BetFactorVersion", " "), 7);
  // setRow(c, gridbag, new Label("Bet Factor Version"), betFactorVersion);
  datadir = new TextField(Truline.userProps.getProperty("DATADIR", "."), 40);
  setRow(c, gridbag, new Label("Data Directory"), datadir);
  fontsize = new TextField(Truline.userProps.getProperty("FontSize", "8"), 40);
  setRow(c, gridbag, new Label("Print Font Size (8,9,10)"), fontsize);
  printProgram = new TextField(Truline.userProps.getProperty("PrintProgram",
    "WordPad.exe /p"), 40);
  setRow(c, gridbag, new Label("Print program"), printProgram);
  // shell = new TextField(Truline.userProps.getProperty("Shell", "command"),
  // 40);
  // setRow(c, gridbag, new Label("Shell program"), shell);
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
   Truline.userProps.put("RecencyDays", recency.getText());
   Truline.userProps.put("MaxDays", maxdays.getText());
   Truline.userProps.put("MaxVariant", maxvariant.getText());
   Truline.userProps.put("UseMaiden", maiden.getText());
   Truline.userProps.put("SirePoints", sirePoints.getText());
   Truline.userProps.put("B-NumberIndicators", bNumberFlowBet.getText());
   Truline.userProps.put("T-NumberIndicators", tNumberFlowBet.getText());
   Truline.userProps.put("J-NumberIndicators", jNumberFlowBet.getText());
   Truline.userProps.put("TrnJkyPercent", trnJkyFlowBet.getText());
   Truline.userProps.put("DATADIR", datadir.getText());
   Truline.userProps.put("FontSize", fontsize.getText());
   Truline.userProps.put("PrintProgram", printProgram.getText());
   // Truline.userProps.put("Shell", shell.getText());
   
   try { FileOutputStream out = new FileOutputStream("c:/truline2015/truline.ini");
   // try { FileOutputStream out = new FileOutputStream(Truline.userProps.getProperty("DATADIR")+"truline.ini");
    if (Truline.userProps.getProperty("TL2014","N").equals("Yes"))
     Truline.userProps.store(out, "Truline2014 configuration"); 
    else
     Truline.userProps.store(out, "Truline2012 configuration"); }
    catch(Exception ef){}
    
   // Make it affective
   m_gui.clear();
   if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("MCP")) {
    for (Enumeration el = m_gui.m_brisMCP.m_races.elements(); el
      .hasMoreElements();) {
     Race race = (Race) el.nextElement();
     Handicap.compute(race);
    }
    GUIReport rpt4 = new GUIReport();
    rpt4.generate(m_gui.m_filename, m_gui.m_brisMCP, m_gui, m_gui.m_raceNbr);
   } else if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("JCP")) {
     for (Enumeration el = m_gui.m_brisJCP.m_races.elements(); el
       .hasMoreElements();) {
      Race race = (Race) el.nextElement();
      Handicap.compute(race);
     }
     GUIReport rpt4 = new GUIReport();
     rpt4.generate(m_gui.m_filename, m_gui.m_brisJCP, m_gui, m_gui.m_raceNbr);
    } else {
    for (Enumeration el = m_gui.m_bris.m_races.elements(); el.hasMoreElements();) {
     Race race = (Race) el.nextElement();
     Handicap.compute(race);
    }
    GUIReport rpt4 = new GUIReport();
    rpt4.generate(m_gui.m_filename, m_gui.m_bris, m_gui, m_gui.m_raceNbr);
   }
   dispose();
  } else if (cmd.equals("cancel")) {
   dispose();
  }
 }
}
