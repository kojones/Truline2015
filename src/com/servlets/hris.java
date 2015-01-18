package com.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mains.Truline;
import com.http.StreamToBrowser;

@WebServlet("/hris")
public class hris extends HttpServlet
{
 /**
  * 
  */
 private static final long serialVersionUID = 5731066378216983408L;
 /* ---------------------------------------------------------------------------- */
 public void doGet(HttpServletRequest request, HttpServletResponse response)
   throws IOException, ServletException
 {
  doTask(request, response);
 } /* end doGet */
 /* ---------------------------------------------------------------------------- */
 public void doPost(HttpServletRequest request, HttpServletResponse response)
   throws IOException, ServletException
 {
  doTask(request, response);
 } /* end doPost */
 /* ---------------------------------------------------------------------------- */
 public void doTask(HttpServletRequest request, HttpServletResponse response)
   throws IOException, ServletException
 {
  response.setContentType("text/html");
  PrintWriter out = response.getWriter();
  boolean b = false;
  String fileRequested = null;
  do {
   if (b) {
    break;
   }
   out.println("<HTML>");
   out.println("<HEAD>");
   out.println("<TITLE>");
   out.println("Hello Kim");
   out.println("</TITLE>");
   out.println("</HEAD>");
   out.println("<BODY>");
   out.println("<BIG>Hello Kim</BIG>");
   Enumeration<String> en = request.getParameterNames();
   String paramName = null;
   String paramData = null;
   while(en.hasMoreElements()) {
    paramName = en.nextElement();
    paramData = request.getParameter(paramName);
    out.println( paramName + " = " + paramData );
    System.out.println( paramName + " = " + paramData );
    if (paramName.equalsIgnoreCase("fileRequested")) {
     fileRequested = paramData;
    }
   }
   out.println("</BODY>");
   out.println("</HTML>");
  } while( false );
  // ---------------------------------------------------------------------------
  // First Run TruLine
  b = true;
  if (fileRequested == null) {
   fileRequested = "AQU0223";
  }
  Truline truline = new Truline();
  truline.setFile("C:\\Truline2012\\DATA\\" + fileRequested + ".DRF");
  truline.servletCalled = true;
  String[] argv = new String[0]; 
  truline.doMain(argv);
  // ---------------------------------------------------------------------------
  StreamToBrowser stb = new StreamToBrowser();
  String urlstr = "C:/Truline2012/" + fileRequested + ".html";
  File file = new File(urlstr);
  URL url = null;
  URI uri = null;
  String format = "html";
  uri = file.toURI();
  url = uri.toURL();
  System.out.println( "URL = " + url );
  urlstr = url.toString();
  System.out.println( "urlstr = " + urlstr );
  if (b) {
   stb.streamCharacterData(urlstr, format, out, response);
  }
  System.out.println( "urlstr = " + urlstr + " done....");
 }
 /* ---------------------------------------------------------------------------- */
}
/* ----------------------------------------------------------------------------- */
