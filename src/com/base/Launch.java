package com.base;
/////////////////////////////////////////////////////////////////////////////
//  FILE:           Launch.java
//
//  AUTHOR:          David Keeney
//
//  DATE:           November 9, 2000
//
//  DESCRIPTION:    This defines the Launch class that launches outside processes.
//
// Derived from Giant Java Tree source
//    http://www.gjt.org/servlets/JCVSlet/show/gjt/org/gjt/util/Exec.java/1.1.1.1
//    @author Tim Endres, <a href="mailto:time@gjt.org">time@gjt.org</a>
//  
/////////////////////////////////////////////////////////////////////////////


import java.io.*;
import java.util.*;
import java.net.*;

/**
*	A class to launch a command line program.  It attempts to do it in
*	a platform independent way but it is impossible to be completely
*	platform independent.  The best we can hope for is to write
*	scripts for each platform and then start the scripts in a platform
*	independent way.
*<p>
*	To use this class, provide the following steps:
*	1) Adjust the run command for operating system differences.
*		String[] args = Launch.fixArgs("your_command_here.bat arguments following");
*
*		For scripts, use the .bat name.  The method will substitute the ".bat"
*		for ".sh" if running on a unix/Linux environment.  You can use redirection.
*
*		The command can be a program to execute, a shell script, or a built-in
*		command.
*
*	2) Allocate a class instance.
*		Launch proc = new Launch(args, env, dir, result); 
*
*			args  - The array of arguments compiled by fixArgs().
*			env	  - Array of environment strings. (if null, inherit from parent)
*			dir   - The default directory.  (if null, inherit from parent)
*			result- Where to send the output. There is some variation here:
*				StringBuffer  - place all output in the provided string buffer.
*				PrintWriter   - place all output in the writer.
*				PrintStream   - place all output in the print stream.
*				LogIF		  - place all output in log, calls print()
*				null          - discard all output
*
*	2a)	At this point you can set a prefix string using setOutPrefix().
*		This string will be prepended to all output lines.
*
*	2b) You can also set a Launchlistener which will be notified when the
*		launched program terminates.
*	
*	3) Execute the process.  
*		proc.exec();
*
*		This will start up the program as a subprocess with a separate thread(s) 
*		watching for output.  There will be no window created for this program.
*
*		The exec() method will return immediately.
*		if there were no errors in launching it will throw an IOException.  
*		If you do nothing else, the program should continue to run in parallel 
*		and when terminated will be cleaned up on this end and the listeners 
*		will be notified.
*
*		NOTE: the launched process is a "sub-process" and if the parent process 
*		terminates it will also terminate the launched sub-processes.
*
*	3a) Once the program has been launched, the method isRunning() will
*		return true until the program terminates.
*
*	3b) The waitfor(array, timeout) method can be used to wait for a
*		specific string in the output.
*
*	3c) The launched process can be forceably terminated by calling the 
*		proc.kill() method.
*
*	4)  Waiting for termination.  You can call the waitfor() method if you want
*	    your thread to block until the process is finished.  When it completes
*		it returns the processes exit value.
*
*		int exitVal = proc.waitfor();
*
*	4a) At any time after the program terminates you can also query for the 
*		exit value by calling the getExitValue() method.  If the program has
*		not yet terminated it returns -1 but since the real exit value may
*		also be a -1, do not use the value unless isRunning() is false.
*
*		int exitValue = proc.getExitValue();	    
*/
public class Launch implements Runnable
{
	private String[]		m_argv;				// The argument string array
	private String[]		m_envp;				// The environment string array
	private File            m_dir;				// The default directory
	
	private StringBuffer	m_sBuffer = null;	// String buffer to place output
	private PrintWriter     m_pWriter = null;	// PrintWriter to place output
	private PrintStream		m_pStream = null;	// PrintStream to place output
	private LogIF			m_log = null;		// A log to send output
	private String			m_outPfx;			// Prefix string to put on each output line.
	
	private Process			m_proc;				// The process object
	private int				m_exitVal;			// The return result
	private Thread			m_stdoutThread;		// The deamon thread watching stdout output
	private Thread			m_stderrThread;		// The deamon thread watching stderr output
	private BufferedReader	m_stdout;			// The stdout stream being monitored.
	private BufferedReader	m_stderr;			// The stderr stream being monitored.
	private boolean			m_running;			// Flag indicating if process is running.
	private Vector			m_listeners;		// Vector of LaunchListenerIF objects.
	private String[]		m_waitforText;		// Array of text to wait for.
	private int				m_waitforResult;	// result of the search.
	
    private String          m_lineSep;			// The line seperator for this OS.
	
	/**
	 *	Will attempt to exec a program or script file.
	 *	@param argv[]  Arguments to exec. The first argument is the command.
	 *				   The first argument needs to be the shell program to use
	 *                 unless it is an executable program.
	 *	@param envp[]  The environment.  If null, use parent's environment.
	 *	@param dir    The directory that will be the default directory.  If null, use 
	 *				  current default directory.
	 *	@param sBuffer  A string buffer to return results.
	 *     or
	 *	@param pWriter  A print writer to return results.
	 *     or
	 *  @param pStream A print stream to return results.
	 *	   or 
	 *	@param log      A log to send output to.
	 */
	public Launch(String[] argv, String[] envp, File dir)
	{
		initialize(argv, envp, dir);
	}
	public Launch(String[] argv, String[] envp, File dir, StringBuffer sBuffer)
	{
		initialize(argv, envp, dir);
		m_sBuffer = sBuffer;
	}
	public Launch(String[] argv, String[] envp, File dir, PrintWriter pWriter)
	{
		initialize(argv, envp, dir);
		m_pWriter = pWriter;
	}
	public Launch(String[] argv, String[] envp, File dir, PrintStream pStream)
	{
		initialize(argv, envp, dir);
		m_pStream = pStream;
	}
	public Launch(String[] argv, String[] envp, File dir, LogIF log)
	{
		initialize(argv, envp, dir);
		m_log = log;
	}
	
	
	// Initialize the variables.
	private void initialize( String[] argv, String[] envp, File dir)
	{
		m_exitVal = -1;
		m_argv = argv;
		m_envp = envp;
		m_dir = dir;
		m_outPfx = "";
		m_listeners = null;
		m_running = false;
		m_lineSep = System.getProperty( "line.separator", "\n" );
		m_waitforText = null;
		m_waitforResult = -1;
	}
	
	
	/**
	 * Run the program.
	 *	1) Cal getRuntime.exec() to get it going.
	 *  2) close the stdin to the process...not passing it anything.
	 *  3) Open stdout and stderr from the process.
	 *	4) Create Threads to read stdout and atderr and clean up after.
	 * @throws IOException on any errors in launching.
	 */
	public void exec()
		throws IOException
	{
String cmd = "";
for (int i = 0; i < m_argv.length; i++) cmd += " "+m_argv[i];
println("Launch Command:"+cmd);
//println("dir: "+((m_dir == null)?"(default)":m_dir.getAbsolutePath()));
		try 
		{
			if (m_argv.length == 1)
				m_proc = Runtime.getRuntime().exec(m_argv[0], m_envp, m_dir);
			else
				m_proc = Runtime.getRuntime().exec(m_argv, m_envp, m_dir);
		}
		catch ( Exception ex )
		{
			String err = "ERROR exec-ing process '" + m_argv[0] + "'" + m_lineSep 
					+ "   "+ ex.toString();
			println(err);
			throw new IOException(err);
		}
		if (m_proc == null)
			throw new IOException("ERROR exec-ing process "+m_argv[0]);
		
		
		try
		{
			m_running = true;
			m_proc.getOutputStream().close();
			m_stdout = new BufferedReader(new InputStreamReader (m_proc.getInputStream()));
			m_stderr = new BufferedReader(new InputStreamReader (m_proc.getErrorStream()));
		}
		catch ( Exception ex )
		{
			String err = "ERROR Setting up I/O streams " + m_lineSep + "   "+ ex.toString();
			println(err);
			m_proc.destroy();
			m_running = false;
			try
			{
				m_stdout.close();
			}
			catch(Exception dontcare) {};
			try
			{
				m_stderr.close();
			}
			catch(Exception dontcare) {};
			throw new IOException(err);
		}
		
		try
		{
			m_stdoutThread = new Thread(this, "Stdout");
			m_stdoutThread.setDaemon(true);
			m_stdoutThread.start();
			m_stderrThread = new Thread(this, "Stderr");
			m_stderrThread.setDaemon(true);
			m_stderrThread.start();
			
		}
		catch ( Exception ex )
		{
			String err = "ERROR starting threads" + m_lineSep  + "   "+ ex.toString();
			m_proc.destroy();
			m_running = false;
			try
			{
				m_stdout.close();
			}
			catch(Exception dontcare) {};
			try
			{
				m_stderr.close();
			}
			catch(Exception dontcare) {};
			throw new IOException(err);
		}
	}
	
	
	/**
	*	Kill the sub-process spawned by this class instance.
	*/
	public void kill()
	{
		if (m_running)
			m_proc.destroy();
		println("Process killed");
	}
	
	
	/**
	*	Return current running status.
	*/
	public boolean isRunning()
	{
		return m_running;
	}
	
	/**
	 *	Wait for this process to finish.  When this returns
	 *	the output and return status should be available.
	 *	@param timeout  - how long to wait for it to terminate.
	 *					  Time is in milliseconds.
	 *					  A timeout of 0 means to wait forever.
	 *	@return process exit status.  Returns -1 on an error.
	 */
	public int waitfor()
	{
		return waitfor(0);
	}
	public int waitfor(long timeout)
	{
		if (m_running)
		{
			try 
			{ 
				if (timeout >= 0)
					m_stdoutThread.join(timeout); 
			}
			catch ( InterruptedException ex )
			{
				println("ERROR joining exec stdout thread; " +ex);
			}
		}
		return m_exitVal;
	}
	
	/**
	*	Wait for a specific output to appear in the output stream.
	*	@param text     An array of text to look for in output stream.
	*					If any of these elements are found within a line
	*					of output, the method will return with that element's
	*					index.  It does not search backward into previous output.
	*					If text is a String (not an array), its return index will be 0.
	*	@param timeout  Maximum duration to wait for the matching characters.
	*					Time is in Milliseconds.
	*	@returns	The index of the matching text or -1 for timeout.
	*/
	public int waitfor(String text, long timeout)
	{
		String[] textArray = new String[1];
		textArray[0] = text;
		return waitfor(textArray, timeout);
	}
	public int waitfor(String[] text, long timeout)
	{
		m_waitforResult = -1;
		m_waitforText = text;
		synchronized(m_waitforText)
		{
			try
			{
				m_waitforText.wait(timeout);
			}
			catch(InterruptedException x)
			{
				// timeout
			}
			m_waitforText = null;
		}
		return m_waitforResult;
	}
	
	/**
	 * Runtime.exec() has a problem in that is the output streams
	 * (stdout and stderr) are not read in a timely manner (Bug
	 * parade indicates 512 byte buffers), the process will HANG
	 * as the stream BLOCKS forever on readLn(). To fix this, we
	 * must start a thread for each stream to help ensure the data
	 * is read in a timely fashion.
	 *<p>
	 * The run method will be called twice, once for the stdout
	 * thread and once for the stderr thread.
	 */
	public void run()
	{
		boolean isStdout = Thread.currentThread().getName().equals("Stdout");
		BufferedReader out = (isStdout)?m_stdout:m_stderr;
		
		// Read the output from the sub-process and add it to the buffer.
		try 
		{
			while(m_running)
			{
				String line = out.readLine();
				if ( line == null )
					break;                
				println( m_outPfx + line );
			}
		}
		catch(IOException ex)
		{
			if (m_running)
				println("ERROR reading exec "+Thread.currentThread().getName()+" stream." );
		}
		finally
		{
			try
			{
				out.close();
			}
			catch(IOException dontcare){}
		}
		
		// If this is the stdout thread then at the end, 
		// 1) wait for stderr to finish up, 
		// 2) wait for the process termination status,
		// 3) then notify all listeners.
		if (isStdout)
		{
			
			// make sure the Stderr reader has finished.
			try { m_stderrThread.join(); }
			catch ( InterruptedException ex )
			{
				println("ERROR joining exec stderr thread; " + ex );
			}
			
			// Make sure the process has finished.
			try { m_proc.waitFor(); }
			catch ( InterruptedException ex )
			{
				println("ERROR waiting for exec process; "+ex );
			}
			// get the return value.
			m_exitVal = m_proc.exitValue();
			m_running = false;
			
			// Notify any listeners
			if (m_listeners != null)
			{
				for (Enumeration en = m_listeners.elements(); en.hasMoreElements();)
				{
					LaunchListenerIF listener = (LaunchListenerIF)en.nextElement();
					listener.processFinished(this, m_exitVal);
				}
			}
		}
	}
	
	
	// Add something to the output.
	private void println(String msg)
	{
		if ( m_sBuffer != null )
			m_sBuffer.append(msg + m_lineSep);
		
		if (m_pWriter != null)
			m_pWriter.println(msg);
			
		if (m_pStream != null)
			m_pStream.println(msg);
			
		if (m_log != null)
			m_log.println(msg);
			
		if (m_waitforText != null)
		{
			for (int idx = 0; idx < m_waitforText.length; idx++)
			{
				if (msg.indexOf(m_waitforText[idx]) >= 0)
				{
					synchronized(m_waitforText)
					{
						m_waitforResult = idx;
						m_waitforText.notifyAll();
					}
				}
			}
		}
	}
	
	
	/**
	 * Set the string that will prefix all stdout lines.
	 */
	public void setOutPrefix( String outPfx )
	{
		m_outPfx = outPfx;
	}
	
	/**
	*	Set a LaunchListener which will be notified when the
	*	launched program terminates.
	*/
	public void setListener(LaunchListenerIF listener)
	{
		m_listeners.addElement(listener);
	}
	
	/**
	*	Remove a listener.
	*/
	public void removeListener(LaunchListenerIF listener)
	{
		m_listeners.remove(listener);
	}
	
	/**
	 * Get the exit value of the exec(). The value is not
	 * valid until the subprocess completes.
	 */
	public int getExitValue()
	{
		return m_exitVal;
	}
	
	
	
	/**
	 *  Prepair a command string for execution.  Since the underlining 
	 *  Runtime.exec() call does not assume a shell, this methed inserts
	 *  the appropreate shell for the operating system.  For unix and lenux
	 *  it asumes the Bourne shell.
	 *
	 *	Given the script name, make the extension .bat or .sh depending on OS.
	 *<pre>     
	 *	Example:  If name passed in is "MyBat.bat abc", convert to one of the following:
	 *     Win95/98/me   {"command.com", "/E:1900", "/C", "MyBat.bat abc"}
	 *     WinNT/2000    {"cmd.exe", "/E:1900", "/C", "MyBat.bat abc"}
	 *     Unix/Linux    {"bin/sh", "-c", "MyBat.sh abc"}
	 *  NOTE: There is no hope for a Mac.
	 *</pre>
	 *	@param name   The command to execute.
	 */
	private static final int WINNT = 1;		// WindowsNT, Windows2000
	private static final int WIN95 = 2;     // Windows95/98/ME
	private static final int UNIX  = 3;		// Unix, Linux.
	
	public static String[] fixArgs(String name)
	{
		
		// Determine the operating system
		String os = System.getProperty("os.name");
//System.out.println("os="+os);
		int osType;
		if (os.startsWith("Windows NT") || os.startsWith("Windows 2000"))
			osType = WINNT;
		else if (os.startsWith("Windows"))
			osType = WIN95;
		else
			osType = UNIX;
		
		
		if (name == null)
			return null;
		
		System.out.println("name="+name);
		
		String[] args = null;
		switch(osType)
		{
		case WINNT:		// WindowsNT, Windows2000
			args = new String[4];
			args[0] = "cmd.exe";
			args[1] = "/E:1900";
			args[2] = "/C";
			args[3] = name;
			break;
			
		case WIN95:		// Windows95/98/ME
			args = new String[1];
			args[0] = name;
			break;
		
		case UNIX:		// Unix, Linux
			// Substutute .sh for .bat on the script name.
			int idx = name.indexOf(" ");
			if (idx >= 0)
				idx = name.substring(0,idx).lastIndexOf(".bat");
			else
				idx = name.lastIndexOf(".bat");
			if (idx != -1)
				name = name.substring(0,idx)+".sh"+name.substring(idx+4);
			args = new String[3];
			args[0] = "bin/sh";
			args[1] = "-c";
			args[2] = name;
			break;
		}
		return args;
	}
	
	
	//////////////////////////////////////////////////////////
	//   Unit Test Method
	//
	// java -cp . COM/perimetertechnology/netvu/lib/Launch dir *.jar
	//
	/**
	public static void main(String[] args)
	{
		boolean waitfor = false;
		
		if (args.length < 1)
		{
			System.out.println("usage: java Launch <scriptname> <arguments> ...");
			System.out.println("  <scriptname> is the name of the script to run.");
			System.out.println("  <arguments>  are optional arguments");
			System.exit(1);
		}
		String arg = args[0];
		for (int i=1;i<args.length;i++)
			arg += " "+args[i];
		System.out.println("main: "+arg);
		
		// Make OS variations on argument.
		args = Launch.fixArgs(arg);
		
		
		
		// Using std output to display results
		Launch proc = new Launch(args, null, null, System.out); 
		try
		{
			proc.exec();
		}
		catch(IOException e)
		{
			System.out.println(e.toString());
		}
		
		int idx = proc.waitfor("hello world", 10000);
		System.out.println("found it");
		
		int exitVal = proc.waitfor();
		//System.out.println("Result="+result.toString());
		System.out.println("exitVal="+exitVal);
		
		
		
		// collect the results into a string.
		StringBuffer result = new StringBuffer();
		proc = new Launch(args, null, null, result); 
		try
		{
			proc.exec();
		}
		catch(IOException e)
		{
			System.out.println(e.toString());
		}
		exitVal = proc.waitfor();
		System.out.println("\n\nResult as a string="+result.toString());
		System.out.println("exitVal="+exitVal);
		
		
		// Send results to a log file (that goes to screen).
		Debug log = new Debug();
		System.out.println("\n\nSending to a log");
		proc = new Launch(args, null, null, log); 
		try
		{
			proc.exec();
		}
		catch(IOException e)
		{
			System.out.println(e.toString());
		}
		exitVal = proc.waitfor();
		System.out.println("exitVal="+exitVal);
	}
	/**** ******/
	
}

