package com.base;
/**
 *
 *	A GUI for the truline 2000 program.
 *
 */
import com.base.RichTextReport;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;
// import javax.swing.JFrame;

import com.mains.Truline;
public class GUI extends Frame implements ActionListener, Runnable
{
 private TextArea         display;
 private Label            filetitle;
 public int               m_raceNbr    = 1;
 private int              m_maxRaceNbr = 0;
 public String            m_filename;
 public Bris              m_bris;
 public BrisMCP           m_brisMCP;
 public BrisJCP           m_brisJCP;
 private String           m_dir;
 public boolean           m_ready      = false;
 private static final int FORWARD      = 1;
 private static final int BACKWORD     = 2;
 public GUI() {
   super(Truline.title);
 }
 public void init()
 {
  // Create the display
  // width, height
  setSize(new Dimension(320, 200));
  setLayout(new BorderLayout());
  setBackground(Color.white);
  addWindowListener(new WindowAdapter() {
   public void windowClosing(WindowEvent e)
   {
    // System.exit(0);
   }
  });
  MenuBar loadmenuBar = new MenuBar();
  Menu waitMenu = new Menu("Loading - Please wait...        ");
  loadmenuBar.add(waitMenu);
  setMenuBar(loadmenuBar);
  show();
  // Menu Bar
  MenuBar menuBar = new MenuBar();
  Menu fileMenu = new Menu("File");
  menuBar.add(fileMenu);
  MenuItem openMenuItem = new MenuItem("Open");
  openMenuItem.setActionCommand("open");
  openMenuItem.addActionListener(this);
  fileMenu.add(openMenuItem);
  MenuItem configMenuItem = new MenuItem("Configure");
  configMenuItem.setActionCommand("config");
  configMenuItem.addActionListener(this);
  fileMenu.add(configMenuItem);
  MenuItem scratchesMenuItem = new MenuItem("Enter Scratches");
  scratchesMenuItem.setActionCommand("scratches");
  scratchesMenuItem.addActionListener(this);
  fileMenu.add(scratchesMenuItem);
  MenuItem resultsMenuItem = new MenuItem("Enter Results");
  resultsMenuItem.setActionCommand("results");
  resultsMenuItem.addActionListener(this);
  fileMenu.add(resultsMenuItem);
  if (Truline.userProps.getProperty("Experimental", "N").equals("Yes")) {
   MenuItem ignoreRunLineMenuItem = new MenuItem("Ignore Running Lines?");
   ignoreRunLineMenuItem.setActionCommand("ignoreRunLine");
   ignoreRunLineMenuItem.addActionListener(this);
   fileMenu.add(ignoreRunLineMenuItem);
  }
  MenuItem printFlowBets = new MenuItem("Print PI Report All");
  printFlowBets.setActionCommand("printFlowBets");
  printFlowBets.addActionListener(this);
  fileMenu.add(printFlowBets);
  MenuItem printFlowBets1 = new MenuItem("Print PI Report Race");
  printFlowBets1.setActionCommand("printFlowBets1");
  printFlowBets1.addActionListener(this);
  fileMenu.add(printFlowBets1);
  MenuItem changeSurfaceMenuItem = new MenuItem("Change Surface");
  changeSurfaceMenuItem.setActionCommand("changeSurface");
  changeSurfaceMenuItem.addActionListener(this);
  fileMenu.add(changeSurfaceMenuItem);
  MenuItem changeTrackCondMenuItem = new MenuItem("Change Track Condition");
  changeTrackCondMenuItem.setActionCommand("changeTrackCond");
  changeTrackCondMenuItem.addActionListener(this);
  fileMenu.add(changeTrackCondMenuItem);

  // MenuItem printSetupMenuItem = new MenuItem("Printer Setup");
  // printSetupMenuItem.setActionCommand("printsetup");
  // printSetupMenuItem.addActionListener(this);
  // fileMenu.add(printSetupMenuItem);
  MenuItem printallMenuItem = new MenuItem("Print All");
  printallMenuItem.setActionCommand("printall");
  printallMenuItem.addActionListener(this);
  fileMenu.add(printallMenuItem);
  MenuItem printMenuItem = new MenuItem("Print Race");
  printMenuItem.setActionCommand("print");
  printMenuItem.addActionListener(this);
  fileMenu.add(printMenuItem);
  MenuItem closeMenuItem = new MenuItem("Close");
  closeMenuItem.setActionCommand("close");
  closeMenuItem.addActionListener(this);
  fileMenu.add(closeMenuItem);
  Menu helpMenu = new Menu("Help");
  MenuItem aboutMenuItem = new MenuItem("About Truline2014");
  aboutMenuItem.setActionCommand("about");
  aboutMenuItem.addActionListener(this);
  helpMenu.add(aboutMenuItem);
  menuBar.setHelpMenu(helpMenu);
  display = new TextArea("", 40, 95, TextArea.SCROLLBARS_VERTICAL_ONLY);
  // Font displayFont = new Font("Lucida Sans", Font.PLAIN, 12);
  Font displayFont = new Font("Courier", Font.PLAIN, 12);
  display.setFont(displayFont);
  add(display, "South");
  // Create a panel containing the buttons
  Panel panel1 = new Panel();
  panel1.setLayout(new BorderLayout());
  panel1.setBackground(Color.white);
  Label copyright = new Label(Truline.copyright);
  Font titleFont = new Font("Arial", Font.BOLD, 12);
  copyright.setFont(titleFont);
  panel1.add(copyright, "North");
  Panel panel3 = new Panel();
  panel3.setLayout(new BorderLayout());
  panel3.setBackground(Color.white);
  filetitle = new Label("------------------------");
  filetitle.setFont(titleFont);
  panel3.add(filetitle, "West");
  Panel panel2 = new Panel();
  panel2.setLayout(new BorderLayout());
  panel2.setBackground(Color.white);
  Button prevButton = new Button("Prev Race");
  prevButton.setActionCommand("prev");
  prevButton.addActionListener(this);
  panel2.add(prevButton, "Center");
  Button nextButton = new Button("Next Race");
  nextButton.setActionCommand("next");
  nextButton.addActionListener(this);
  panel2.add(nextButton, "East");
  panel3.add(panel2, "East");
  panel1.add(panel3, "South");
  add(panel1, "North");
  setMenuBar(menuBar);
  pack();
  show();
  m_ready = true;
 }
 /**
  * Fill in the filename field.
  */
 public void setFilename(String filename)
 {
  filetitle.setText(filename);
 }
 /**
  * Action listener for the Buttons.
  */
 public void actionPerformed(ActionEvent e)
 {
  String cmd = e.getActionCommand();
  if (cmd.equals("next")) {
   generateReport(FORWARD);
  } else if (cmd.equals("prev")) {
   generateReport(BACKWORD);
  } else if (cmd.equals("open")) {
   String file = getFile();
   if (file != null) {
    m_filename = Truline.fixupFile(file);
    clear();
    if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("MCP")) {
     m_brisMCP = new BrisMCP();
     if (m_brisMCP.load(file, m_filename)) {
      if (Truline.userProps.getProperty("PostResults", "N").equals("Y")) {
       BrisXRD brisXRD = new BrisXRD();
       if (brisXRD.load(file, m_filename, m_brisMCP.m_races))
        System.out.println("Results loaded.\n");
       else
        System.out.println("No results file found.\n");
      }
      for (Enumeration el = m_brisMCP.m_races.elements(); el.hasMoreElements();) {
       Race race = (Race) el.nextElement();
       Handicap.compute(race);
      }
     }
    } else if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("JCP")) {
      m_brisJCP = new BrisJCP();
      if (m_brisJCP.load(file, m_filename)) {
       if (Truline.userProps.getProperty("PostResults", "N").equals("Y")) {
        BrisXRD brisXRD = new BrisXRD();
        if (brisXRD.load(file, m_filename, m_brisJCP.m_races))
         System.out.println("Results loaded.\n");
        else
         System.out.println("No results file found.\n");
       }
       for (Enumeration el = m_brisJCP.m_races.elements(); el.hasMoreElements();) {
        Race race = (Race) el.nextElement();
        Handicap.compute(race);
       }
      }
     } else {
     m_bris = new Bris();
     if (m_bris.load(file, m_filename)) {
      if (Truline.userProps.getProperty("PostResults", "N").equals("Y")) {
       BrisXRD brisXRD = new BrisXRD();
       if (brisXRD.load(file, m_filename, m_bris.m_races))
        System.out.println("Results loaded.\n");
       else
        System.out.println("No results file found.\n");
      }
      for (Enumeration el = m_bris.m_races.elements(); el.hasMoreElements();) {
       Race race = (Race) el.nextElement();
       Handicap.compute(race);
      }
     }
    }
    doReport();
   }
  } else if (cmd.equals("printall")) {
   // TextPrint rpt5 = new TextPrint();
   RichTextReport rpt5 = new RichTextReport();
   if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("MCP"))
    rpt5.generate(m_filename, m_brisMCP, -1, true);
   else
    if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("JCP"))
     rpt5.generate(m_filename, m_brisJCP, -1, true);
    else
    rpt5.generate(m_filename, m_bris, -1, true);
  } else if (cmd.equals("print")) {
   // TextPrint rpt5 = new TextPrint();
   RichTextReport rpt5 = new RichTextReport();
   if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("MCP"))
    rpt5.generate(m_filename, m_brisMCP, m_raceNbr, true);
   else
    if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("JCP"))
     rpt5.generate(m_filename, m_brisJCP, m_raceNbr, true);
    else
    rpt5.generate(m_filename, m_bris, m_raceNbr, true);
  } else if (cmd.equals("printFlowBets")) {
   // TextPrint rpt5 = new TextPrint();
   RichTextReport rpt5 = new RichTextReport();
   if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("MCP"))
    rpt5.generateHF(m_filename, m_brisMCP, true);
   else
    if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("JCP"))
     rpt5.generateHF(m_filename, m_brisJCP, true);
    else
    rpt5.generateHF(m_filename, m_bris, true);
  } else if (cmd.equals("printFlowBets1")) {
   // TextPrint rpt5 = new TextPrint();
   RichTextReport rpt5 = new RichTextReport();
   if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("MCP"))
    rpt5.generateHF(m_filename, m_brisMCP, m_raceNbr, true);
   else
    if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("JCP"))
     rpt5.generateHF(m_filename, m_brisJCP, m_raceNbr, true);
    else
    rpt5.generateHF(m_filename, m_bris, m_raceNbr, true);
  } else if (cmd.equals("close")) {
   dispose();
   System.exit(0);
  } else if (cmd.equals("about")) {
   Dialog dlg = new Dialog(this, "About Truline2015", true);
   dlg.addWindowListener(new WindowAdapter() {
    public void windowClosing(WindowEvent e)
    {
     e.getWindow().dispose();
    }
   });
   dlg.setSize(new Dimension(50, 50));
   dlg.setLayout(new BorderLayout());
   // dlg.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
   dlg
     .add(new Label(Truline.title + " " + Truline.version), BorderLayout.NORTH);
   dlg.add(new Label(Truline.copyright), BorderLayout.SOUTH);
   dlg.pack();
   dlg.show();
  } else if (cmd.equals("config")) {
   GUIConfig dlg = new GUIConfig(this);
   dlg.init(this);
  } else if (cmd.equals("scratches")) {
   GUIScratches dlg = new GUIScratches(this);
   if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("MCP"))
    dlg.init(this, m_brisMCP, m_raceNbr);
   else
    if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("JCP"))
     dlg.init(this, m_brisJCP, m_raceNbr);
    else
    dlg.init(this, m_bris, m_raceNbr);
  } else if (cmd.equals("results")) {
   GUIResults dlg = new GUIResults(this);
   if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("MCP"))
    dlg.init(this, m_brisMCP, m_raceNbr);
   else
    if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("JCP"))
     dlg.init(this, m_brisJCP, m_raceNbr);
    else
    dlg.init(this, m_bris, m_raceNbr);
  } else if (cmd.equals("changeSurface")) {
   if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("MCP")) {
    for (Enumeration f = m_brisMCP.m_races.elements(); f.hasMoreElements();) {
     Race race = (Race) f.nextElement();
     if (m_raceNbr == race.m_raceNo) {
      String surface = race.m_props.getProperty("SURFACE", "").toUpperCase();
      if (surface.equals("T"))
       surface = "D";
      else if (surface.equals("D"))
       surface = "T";
      race.m_props.setProperty("SURFACE", surface);
      Handicap.compute(race);
      int raceNbr = m_raceNbr;
      GUIReport rpt4 = new GUIReport();
      rpt4.generate(m_filename, m_brisMCP, this, raceNbr);
     }
    }
   } else if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("JCP")) {
     for (Enumeration f = m_brisJCP.m_races.elements(); f.hasMoreElements();) {
      Race race = (Race) f.nextElement();
      if (m_raceNbr == race.m_raceNo) {
       String surface = race.m_props.getProperty("SURFACE", "").toUpperCase();
       if (surface.equals("T"))
        surface = "D";
       else if (surface.equals("D"))
        surface = "T";
       race.m_props.setProperty("SURFACE", surface);
       Handicap.compute(race);
       int raceNbr = m_raceNbr;
       GUIReport rpt4 = new GUIReport();
       rpt4.generate(m_filename, m_brisJCP, this, raceNbr);
      }
     }
    } else {
    for (Enumeration f = m_bris.m_races.elements(); f.hasMoreElements();) {
     Race race = (Race) f.nextElement();
     if (m_raceNbr == race.m_raceNo) {
      String surface = race.m_props.getProperty("SURFACE", "").toUpperCase();
      if (surface.equals("T"))
       surface = "D";
      else if (surface.equals("D"))
       surface = "T";
      race.m_props.setProperty("SURFACE", surface);
      Handicap.compute(race);
      int raceNbr = m_raceNbr;
      GUIReport rpt4 = new GUIReport();
      rpt4.generate(m_filename, m_bris, this, raceNbr);
     }
    }
   }
  } else if (cmd.equals("changeTrackCond")) {
   if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("MCP")) {
    for (Enumeration f = m_brisMCP.m_races.elements(); f.hasMoreElements();) {
     Race race = (Race) f.nextElement();
     if (m_raceNbr == race.m_raceNo) {
      if (race.m_trackCond.equals("Fast"))
       race.m_trackCond = "Off";
      else
       race.m_trackCond = "Fast";
      Handicap.compute(race);
      int raceNbr = m_raceNbr;
      GUIReport rpt4 = new GUIReport();
      rpt4.generate(m_filename, m_brisMCP, this, raceNbr);
     }
    }
   } else if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("JCP")) {
     for (Enumeration f = m_brisJCP.m_races.elements(); f.hasMoreElements();) {
      Race race = (Race) f.nextElement();
      if (m_raceNbr == race.m_raceNo) {
       if (race.m_trackCond.equals("Fast"))
        race.m_trackCond = "Off";
       else
        race.m_trackCond = "Fast";
       Handicap.compute(race);
       int raceNbr = m_raceNbr;
       GUIReport rpt4 = new GUIReport();
       rpt4.generate(m_filename, m_brisJCP, this, raceNbr);
      }
     }
    } else {
    for (Enumeration f = m_bris.m_races.elements(); f.hasMoreElements();) {
     Race race = (Race) f.nextElement();
     if (m_raceNbr == race.m_raceNo) {
      if (race.m_trackCond.equals("Fast"))
       race.m_trackCond = "Off";
      else
       race.m_trackCond = "Fast";
      Handicap.compute(race);
      int raceNbr = m_raceNbr;
      GUIReport rpt4 = new GUIReport();
      rpt4.generate(m_filename, m_bris, this, raceNbr);
     }
    }
   }
  } else if (cmd.equals("ignoreRunLine")) {
   if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("MCP")) {
    for (Enumeration f = m_brisMCP.m_races.elements(); f.hasMoreElements();) {
     Race race = (Race) f.nextElement();
     if (m_raceNbr == race.m_raceNo) {
      if (race.m_ignoreRunLine.equals("Y"))
       race.m_ignoreRunLine = "N";
      else
       race.m_ignoreRunLine = "Y";
      Handicap.compute(race);
      int raceNbr = m_raceNbr;
      GUIReport rpt4 = new GUIReport();
      rpt4.generate(m_filename, m_brisMCP, this, raceNbr);
     }
    }
   } else if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("JCP")) {
     for (Enumeration f = m_brisJCP.m_races.elements(); f.hasMoreElements();) {
      Race race = (Race) f.nextElement();
      if (m_raceNbr == race.m_raceNo) {
       if (race.m_ignoreRunLine.equals("Y"))
        race.m_ignoreRunLine = "N";
       else
        race.m_ignoreRunLine = "Y";
       Handicap.compute(race);
       int raceNbr = m_raceNbr;
       GUIReport rpt4 = new GUIReport();
       rpt4.generate(m_filename, m_brisJCP, this, raceNbr);
      }
     }
    } else {
    for (Enumeration f = m_bris.m_races.elements(); f.hasMoreElements();) {
     Race race = (Race) f.nextElement();
     if (m_raceNbr == race.m_raceNo) {
      if (race.m_ignoreRunLine.equals("Y"))
       race.m_ignoreRunLine = "N";
      else
       race.m_ignoreRunLine = "Y";
      Handicap.compute(race);
      int raceNbr = m_raceNbr;
      GUIReport rpt4 = new GUIReport();
      rpt4.generate(m_filename, m_bris, this, raceNbr);
     }
    }
   }
  }
  // else if (cmd.equals("printsetup"))
  // {
  // PrinterJob pj = PrinterJob.getPrinterJob();
  // if (TextPrint.fmt == null)
  // TextPrint.fmt = pj.defaultPage();
  // TextPrint.fmt = pj.pageDialog(TextPrint.fmt);
  // }
 }
 /**
  * Start up a dialog to get the filename...while everything loads.
  */
 public Thread startGetfile()
 {
  Thread thread = new Thread(this);
  thread.start();
  try {
   thread.sleep(1000);
  } catch (Exception dontcare) {
  }
  return thread;
 }
 public void run()
 {
  String file = null;
  if (m_filename == null)
   file = getFile();
  else
   file = m_filename;
  m_filename = Truline.fixupFile(file);
  if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("MCP")) {
   m_brisMCP = new BrisMCP();
   if (m_brisMCP.load(file, m_filename)) {
    if (Truline.userProps.getProperty("PostResults", "N").equals("Y")) {
     BrisXRD brisXRD = new BrisXRD();
     if (brisXRD.load(file.substring(0, file.length() - 4) + ".xrd",
       m_filename.substring(0, 7) + ".xrd", m_brisMCP.m_races))
      System.out.println("Results loaded.\n");
     else
      System.out.println("No results file found.\n");
    }
    for (Enumeration e = m_brisMCP.m_races.elements(); e.hasMoreElements();) {
     Race race = (Race) e.nextElement();
     Handicap.compute(race);
    }
   } else
    System.exit(0);
  } else if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("JCP")) {
    m_brisJCP = new BrisJCP();
    if (m_brisJCP.load(file, m_filename)) {
     if (Truline.userProps.getProperty("PostResults", "N").equals("Y")) {
      BrisXRD brisXRD = new BrisXRD();
      if (brisXRD.load(file.substring(0, file.length() - 4) + ".xrd",
        m_filename.substring(0, 7) + ".xrd", m_brisJCP.m_races))
       System.out.println("Results loaded.\n");
      else
       System.out.println("No results file found.\n");
     }
     for (Enumeration e = m_brisJCP.m_races.elements(); e.hasMoreElements();) {
      Race race = (Race) e.nextElement();
      Handicap.compute(race);
     }
    } else
     System.exit(0);
   } else {
   m_bris = new Bris();
   if (m_bris.load(file, m_filename)) {
    if (Truline.userProps.getProperty("PostResults", "N").equals("Y")) {
     BrisXRD brisXRD = new BrisXRD();
     if (brisXRD.load(file.substring(0, file.length() - 4) + ".xrd",
       m_filename.substring(0, 7) + ".xrd", m_bris.m_races))
      System.out.println("Results loaded.\n");
     else
      System.out.println("No results file found.\n");
    }
    for (Enumeration e = m_bris.m_races.elements(); e.hasMoreElements();) {
     Race race = (Race) e.nextElement();
     Handicap.compute(race);
    }
   } else
    System.exit(0);
  }
 }
 /**
  * Ask the user for a file name.
  */
 public String getFile()
 {
  String file;
  String dir = Truline.userProps.getProperty("DATADIR");
  FileDialog dialog = new FileDialog(this, "Bris File");
  if (dir != null)
   dialog.setDirectory(dir);
  if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("MCP"))
   dialog.setFile("*.mcp");
  else
   if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("JCP"))
    dialog.setFile("*.jcp");
   else
   dialog.setFile("*.drf");
  dialog.show();
  m_dir = dialog.getDirectory();
  if (m_dir != null) {
   if (dialog.getFile() == null)
    return null;
   file = m_dir + "\\" + dialog.getFile();
  } else
   file = m_dir;
  Truline.userProps.put("DATADIR", m_dir);
  return file;
 }
 /**
  * Specify the report to run.
  */
 public void doReport()
 {
  m_raceNbr = 0;
  if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("MCP"))
   m_maxRaceNbr = m_brisMCP.getMaxRaceNbr();
  else
   if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("JCP"))
    m_maxRaceNbr = m_brisJCP.getMaxRaceNbr();
   else
   m_maxRaceNbr = m_bris.getMaxRaceNbr();
  filetitle.setText(m_filename);
  generateReport(FORWARD);
 }
 /**
  * Generate one race of the report.
  */
 public boolean generateReport(int direction)
 {
  int raceNbr = m_raceNbr;
  GUIReport rpt4 = new GUIReport();
  if (direction == FORWARD) {
   while (raceNbr <= m_maxRaceNbr) {
    raceNbr++;
    if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("MCP")) {
     if (rpt4.generate(m_filename, m_brisMCP, this, raceNbr)) {
      m_raceNbr = raceNbr;
      return true;
     }
    } else if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("JCP")) {
      if (rpt4.generate(m_filename, m_brisJCP, this, raceNbr)) {
       m_raceNbr = raceNbr;
       return true;
      }
     } else {
     if (rpt4.generate(m_filename, m_bris, this, raceNbr)) {
      m_raceNbr = raceNbr;
      return true;
     }
    }
   }
  } else {
   while (raceNbr > 1) {
    raceNbr--;
    if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("MCP")) {
     if (rpt4.generate(m_filename, m_brisMCP, this, raceNbr)) {
      m_raceNbr = raceNbr;
      return true;
     }
    } else if (Truline.userProps.getProperty("DATATYPE", "DRF").equals("JCP")) {
      if (rpt4.generate(m_filename, m_brisJCP, this, raceNbr)) {
       m_raceNbr = raceNbr;
       return true;
      }
     } else {
     if (rpt4.generate(m_filename, m_bris, this, raceNbr)) {
      m_raceNbr = raceNbr;
      return true;
     }
    }
   }
  }
  return false;
 }
 /**
  * Append to the text in the display.
  */
 public void print(String text)
 {
  if (m_ready)
   display.append(text);
 }
 /**
  * Append to the text in the display.
  */
 public void println()
 {
  if (m_ready)
   display.append("\n");
 }
 /**
  * Append to the text in the display.
  */
 public void println(String text)
 {
  if (m_ready)
   display.append(text + "\n");
 }
 /**
  * Clear the display area.
  */
 public void clear()
 {
  if (m_ready)
   display.setText("");
 }
}
