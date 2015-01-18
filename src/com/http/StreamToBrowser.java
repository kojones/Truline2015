package com.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StreamToBrowser
{
 /**
  * Session constructor comment.
  */
 public StreamToBrowser() {
  super();
 }
 /**
  * Insert the method's description here. Creation date: (6/29/00 9:25:23 AM)
  * 
  * @param EOL
  *         java.lang.String
  */
 /*
  * Step One Set the appropriate MIME type based on the format with which you're
  * working. The getMimeType() method supports various MIME types: This Method
  * Returns the right MIME type for a particular format <p>
  * 
  * @param String format ex: xml or HTML etc.
  * 
  * @return String MIMEtype
  */
 public String getMimeType(String format)
 {
  if (format.equalsIgnoreCase("pdf")) // check the out type
   return "application/pdf";
  else if (format.equalsIgnoreCase("audio_basic"))
   return "audio/basic";
  else if (format.equalsIgnoreCase("audio_wav"))
   return "audio/wav";
  else if (format.equalsIgnoreCase("image_gif"))
   return "image/gif";
  else if (format.equalsIgnoreCase("image_jpeg"))
   return "image/jpeg";
  else if (format.equalsIgnoreCase("image_bmp"))
   return "image/bmp";
  else if (format.equalsIgnoreCase("image_x-png"))
   return "image/x-png";
  else if (format.equalsIgnoreCase("msdownload"))
   return "application/x-msdownload";
  else if (format.equalsIgnoreCase("video_avi"))
   return "video/avi";
  else if (format.equalsIgnoreCase("video_mpeg"))
   return "video/mpeg";
  else if (format.equalsIgnoreCase("html"))
   return "text/html";
  else if (format.equalsIgnoreCase("xml"))
   return "text/xml";
  else
   return null;
 }
 /*
  * This Method Handles streaming Binary data <p>
  * 
  * @param String urlstr ex: http;//localhost/test.pdf etc.
  * 
  * @param String format ex: pdf or audio_wav or msdocuments etc.
  * 
  * @param ServletOutputStream outstr
  * 
  * @param HttpServletResponse resp
  */
 public void streamBinaryData(String urlstr, String format,
   ServletOutputStream outstr, HttpServletResponse resp)
 {
  String ErrorStr = null;
  try {
   // find the right mime type and set it as contenttype
   resp.setContentType(getMimeType(format));
   BufferedInputStream bis = null;
   BufferedOutputStream bos = null;
   try {
    URL url = new URL(urlstr);
    URLConnection urlc = url.openConnection();
    int length = urlc.getContentLength();
    resp.setContentLength(length);
    // Use Buffered Stream for reading/writing.
    InputStream in = urlc.getInputStream();
    bis = new BufferedInputStream(in);
    bos = new BufferedOutputStream(outstr);
    byte[] buff = new byte[length];
    int bytesRead;
    // Simple read/write loop.
    while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
     bos.write(buff, 0, bytesRead);
    }
   } catch (Exception e) {
    e.printStackTrace();
    ErrorStr = "Error Streaming the Data";
    outstr.print(ErrorStr);
    ErrorStr = "URL = " + urlstr;
    outstr.print(ErrorStr);
   } finally {
    if (bis != null) {
     bis.close();
    }
    if (bos != null) {
     bos.close();
    }
    if (outstr != null) {
     outstr.flush();
     outstr.close();
    }
   }
  } catch (Exception e) {
   e.printStackTrace();
  }
 }
 /*
  * This Method Handles streaming Character data <p>
  * 
  * @param String urlstr ex: http;//localhost/test.pdf etc.
  * 
  * @param String format ex: xml or html etc.
  * 
  * @param PrintWriter outstr
  * 
  * @param HttpServletResponse resp
  */
 public void streamCharacterData(String urlstr, String format,
   PrintWriter outstr, HttpServletResponse resp)
 {
  String ErrorStr = null;
  try {
   // find the right mime type and set it as contenttype
   String mimeType = getMimeType(format);
   resp.setContentType(mimeType);
   InputStream in = null;
   try {
    URL url = new URL(urlstr);
    URLConnection urlc = url.openConnection();
    int length = urlc.getContentLength();
    resp.setContentLength(length);
    in = urlc.getInputStream();
    InputStreamReader is = new InputStreamReader( in );
    BufferedReader br = new BufferedReader( is ); 
    int ch;
    while ((ch = br.read()) != -1) {
     outstr.print((char) ch);
    }
   } catch (Exception e) {
    e.printStackTrace();
    ErrorStr = "Error Streaming the Data from " + urlstr;
    outstr.print(ErrorStr);
   } finally {
    if (in != null) {
     in.close();
    }
    if (outstr != null) {
     outstr.flush();
     outstr.close();
    }
   }
  } catch (Exception e) {
   e.printStackTrace();
  }
 }
 /* ------------------------------------------------------------------- */
 /*
  * This Method Handles streaming Character data <p>
  * 
  * @param HttpServletRequest request
  * 
  * @param String addThis ex: htmlclient.html fred.pdf etc
  * 
  * @param boolean debug = true or false.
  * 
  * Builds the request String :- http://www.bbh.com/Messaging/Skandia and strips
  * the last argument Skandia and build's
  * http://www.bbh.com/Messaging/htmlclient.html
  */
 public String getURL(HttpServletRequest request, String addThis, boolean debug)
 {
  StringBuffer requestURL = request.getRequestURL();
  String requestURI = request.getRequestURI();
  if (debug) {
   System.out.println("RequestURL = " + requestURL.toString());
   System.out.println("RequestURI = " + requestURI);
  }
  // True means return the tokens with the seperators as tokens
  // this way we can rebuild accurately...
  StringTokenizer st = new StringTokenizer(requestURL.toString(), "/", true);
  int stCount = st.countTokens();
  StringBuffer newURL = new StringBuffer();
  // So know we remove the last token ( the servlet name ) and replace it with
  // the
  // addThis which I know is the html to be served.
  for (int i = 0; i < stCount; i++) {
   String sToken = st.nextToken();
   sToken = sToken.trim();
   if (i == (stCount - 1)) {
    newURL.append(addThis);
   } else {
    newURL.append(sToken);
   }
  }
  String newRURL = newURL.toString();
  return (newRURL);
 } /* end getURL */
 /*
  * --------------------------------------------------------------------------------- */
}
/* ---------------------------------------------------------------------------------- */
