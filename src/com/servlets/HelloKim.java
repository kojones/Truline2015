package com.servlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/hris")
public class HelloKim extends HttpServlet
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
  out.println("<HTML>");
  out.println("<HEAD>");
  out.println("<TITLE>");
  out.println("Hello Kim");
  out.println("</TITLE>");
  out.println("</HEAD>");
  out.println("<BODY>");
  out.println("<BIG>Hello Kim</BIG>");
  out.println("</BODY>");
  out.println("</HTML>");
 }
 /* ---------------------------------------------------------------------------- */
}
/* ----------------------------------------------------------------------------- */
