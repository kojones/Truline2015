package com.base;
/**
*	Read/Write a comma delimited file.
*	1) Assumes the first line contains the list of field names in the
*		same order as the data.  An alternative list may be provided.
*	2) Fields are numeric or double quoted.  adjacent commas mean an 
*	   empty field.
*	3) The super class contains an ordered list of Properties objects.
*/
import java.util.*;
import java.io.*;

public class CommaDelimited extends Vector<Object>
{
	Vector<String> m_names = new Vector<String>();   // list of Strings containing field names
 int    nextProperty = 1;

	/**
	*	Constructor
	*/
	public CommaDelimited()
	{
	}
	
	
	/**
 	*	Read the file using the first line of file for names
	*	@param filename - name of file (full path or reletive to executable)
	*	@returns - true if successful.
	*/
	public boolean load(String filename)
	{
		return load(filename, null);
	}
	
	
	/**
 	*	Read the file using an external name list
	*	@param filename - name of file (full path or reletive to executable)
	*	@param names - list of field names corresponding to data fields
	*	@returns - true if successful.
	*/
	public boolean load(String filename, Vector names)
	{
		FileReader in;
		StreamTokenizer parser;		
		Properties props;
		boolean stat = true;
		
		if (Log.isDebug(Log.TRACE))
			Log.print("Loading table "+filename+"\n");
		try
		{
			in = new FileReader(filename);
			parser = new StreamTokenizer(in);
			parser.resetSyntax();
			parser.wordChars(33, 255);
			parser.whitespaceChars(0,32);
			parser.eolIsSignificant(true);
			parser.commentChar('#');
			parser.quoteChar('"');
			parser.ordinaryChar(',');
		}
		catch(Exception e)
		{
			System.out.println("Could not open file "+filename+"\n   "+e);
			return false;
		}
		
		if (names != null)
		{
			m_names = names;
		}
		else
		{
			// Read the first line to get the list of names
			try
			{
				m_names = readNames(parser);
			}
			catch(Exception e)
			{
				Log.print("Problem reading names from "+filename+"\n   "+e);
				stat = false;
			}
		}
			
		int line = 0;
		while(stat)
		{
			line++;
			try
			{
				props = readLine(parser, m_names);
				if (props == null)
					break;  // End of file
				if (props.size() > 0)
					addElement(props);
			}
			catch(Exception e)
			{
				System.out.println("Problem reading line "+line+" of "+filename+"\n   "+e);
				stat = false;
			}
		}
		try
		{
			in.close();
		}
		catch(Exception e){ }
		return stat;
	}
	
	/**
	*	Read first line from the file and load it into the names list.
	*
	*	@param parser - the StreamTokenizer opened on the file to be read.
	*	@return the names read from the line.
	*/
	private Vector readNames(StreamTokenizer parser) throws Exception
	{
		int fld = 0;
		int c = 0;
		Vector names = new Vector();
		String name = "";
		while(c != StreamTokenizer.TT_EOF)
		{
			try
			{
				c = parser.nextToken();
				switch(c)
				{
				case StreamTokenizer.TT_NUMBER:
					name += ""+parser.nval;
					break;
					
				case '"':
				case StreamTokenizer.TT_WORD:
					name += parser.sval;
					break;
					
				case ',':
					if (name.length() == 0)
						name = "placeholder";
					names.addElement(name);
					name = "";
					fld++;
					break;
					
				case StreamTokenizer.TT_EOF:
				case StreamTokenizer.TT_EOL:
					if (name.length() > 0)
					{
						names.addElement(name);
						fld++;
					}
					if (fld > 0)
						return names;
					break;
				}	
				
			}
			catch(Exception e) 
			{ 
				// an error condition...
				throw new Exception("Error parsing field="+fld+"\n   "+e);
			}
		}
		return names;
	}

	/**
	*	Read one line from the file and load it into the properties list.
	*	If there are more names than data, the remaining names are skipped.
	*	If there are less names than data, the extra data is ignored.
	*
	*	@param parser - the StreamTokenizer opened on the file to be read.
	*	@param names  - A vector containing the names of the fields.
	*	@return the properties read from the line.
	*/
	public Properties readLine(StreamTokenizer parser, Vector names) throws Exception
	{
		int fld = 0;
		int c = 0;
		Properties props = new Properties();
		String name = null;
		if (names.size() > fld)
			name = (String)names.get(fld);
		
		while(c != StreamTokenizer.TT_EOL && c != StreamTokenizer.TT_EOF)
		{
			try
			{
				c = parser.nextToken();
				switch(c)
				{
				case StreamTokenizer.TT_NUMBER:
					parser.sval = ""+parser.nval;
					// fall through
					
				case '"':
				case StreamTokenizer.TT_WORD:
					if (names.size() > fld)
					{
						name = (String)names.get(fld);
						props.put(name, parser.sval);
//Log.print("found "+name+"="+parser.sval+"\n");
					}
					break;
					
				case ',':
					fld++;
					break;
					
				case StreamTokenizer.TT_EOL:
					return props;
				case StreamTokenizer.TT_EOF:
					if (props.size() == 0)
						return null;
					return props;

				}	
				
			}
			catch(Exception e1) 
			{ 
				// an error condition...
				throw new Exception("Error in field="+fld+" ("+name+")\n    "+e1);
			}
		}
	
		
		return props;
	}
	
	/**
	*	Get the properties containing the specified field.
	*
	*	@param name - field name to be used as the key
	*	@param value - key field value
	*	@return properties for the set that matches the key.
	*/
	public Properties get(String name, String value) 
	{
		//Log.print("looking for "+name+"="+value+"\n");
		for(Enumeration c = elements(); c.hasMoreElements();)
		{
			Properties prop = (Properties)c.nextElement();
			String v = prop.getProperty(name);
			//Log.print("get: "+name+"="+v+"\n");
			if (v != null && v.equals(value))
				return prop;
		}
		return null;
	}
	
 /**
  * Get the first property
  * 
  * @param name
  *         - field name to be used as the key
  * @param value
  *         - key field value
  * @return properties for the set that matches the key.
  */
 public Properties getFirst(String name)
 {
  nextProperty = 2;
  // Log.print("looking for "+name+"="+value+"\n");
  for (Enumeration c = elements(); c.hasMoreElements();) {
   Properties prop = (Properties) c.nextElement();
   String v = prop.getProperty(name);
   // Log.print("get: "+name+"="+v+"\n");
   if (v != null)
    return prop;
  }
  return null;
 }
 /**
  * Get the properties containing the specified field.
  * 
  * @param name
  *         - field name to be used as the key
  * @param value
  *         - key field value
  * @return properties for the set that matches the key.
  */
 public String getNext(String name)
 {
  int rowCount = 0;
  // Log.print("looking for "+name+"="+value+"\n");
  for (Enumeration c = elements(); c.hasMoreElements();) {
   rowCount++;
   Properties prop = (Properties) c.nextElement();
   String v = prop.getProperty(name);
   // Log.print("get: "+name+"="+v+"\n");
   if (v != null && rowCount == nextProperty) {
    nextProperty++;
    return v;
   }
  }
  return null;
 }
}
	

