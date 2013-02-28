package com.icbat.game;

import java.util.Date;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * LJ, or Lumberjack, is a logging utility class to allow seam less logging to either console, file, or both
 * 
 * Set file logging a file with setLogFile. Logs naturally to CSV
 *  
 * When extending this: a boolean, edit constructor and the appropriate lines to log(string, string, string) 
 * 
 * @author icbat
 * @see log(String, String, String)
 * */
public class LJ {
	
	// Categories
	public static final int ERROR = 0;
	public static final int LOG = 1;
	public static final int DEBUG = 2;
	
	private static FileHandle logfile = null;
	
	/** Static class, doesn't need to instantiate */
	private LJ() {}
	
	/**
	 * Workhorse of the class. This method handles the actual logging of events.
	 * 
	 * @param	message				The message to be logged
	 * @param	additionalMessage	Additional info if necessary	
	 * @param	logLevel			int (or constant) specifying ERROR, LOG, or DEBUG mode. If anything else, defaults to LOG
	 * */
	public static void log( String message, String additionalMessage, int logLevel ) {
		// Only add the comma is there's something coming
		if (additionalMessage != "")
			message = message + "," + additionalMessage;
		
		// Console Logging
		switch ( logLevel ) {
		case LOG:
			Gdx.app.log( "Log ", message );
			break;
		case ERROR:
			Gdx.app.error( "ERROR ", message);
			Gdx.app.error( "#", "Java heap in bytes:  " + Gdx.app.getJavaHeap() );
			Gdx.app.error( "#", "Native heap in bytes:  " + Gdx.app.getNativeHeap() );
			break;
		case DEBUG:
			Gdx.app.debug( "Debug", message );
			break;	
		// If something's wrong with the category, go ahead and log
		default:
			Gdx.app.log( "Log", message );
			break;
		}
		
		// File logging
		if ( logfile != null ) {
			switch( logLevel ) {
			case LOG:
				logfile.writeString("Log," + message, true);
				break;
			case ERROR:
				logfile.writeString("ERROR," + message, true);
				logfile.writeString( "Java heap in bytes:  " + Gdx.app.getJavaHeap() + "\n", true );
				logfile.writeString( "Native heap in bytes:  " + Gdx.app.getNativeHeap() + "\n", true );
				break;
			case DEBUG:
				logfile.writeString("Debug," + message, true);
				break;
			default:
				logfile.writeString("Log," + message, true);
				break;
			}
		}
	}
	
	/**
	 * Specify a category but no additional column (like variables, stack types, etc.)
	 * 
	 * @param	message		The message to be logged
	 * @param	logLevel	int (or constant) specifying ERROR, LOG, or DEBUG mode
	 * */
	public static void log( String message, int logLevel ) {
		LJ.log ( message, "", logLevel );
	}
	
	/**
	 * By default, this will assume LOG-level of sensitivity
	 * 
	 * @param	message	The message to be logged
	 * */
	public static void log( String message ) {
		LJ.log( message, LOG );
	}
	
	/** Wrapper so this class can encapsulate all logging
	 * ONLY pertains to console logging; file logging and any others will still log everything always
	 * 
	 * @see	Gdx
	 *  */
	public static void setLevel( int logLevel ) {
		Gdx.app.setLogLevel( logLevel );
	}

	public static FileHandle getLogfile() {
		return logfile;
	}

	public static void setLogfile(String gameName) {
		LJ.logfile = new FileHandle(gameName + "." + new Date().toString().replaceAll("\\W", ".") + ".log");
		LJ.log("Logfile created at " + LJ.logfile.path());
	}
	
}
