package com.example.microsoftproject;

import android.util.Log;

public class Logger {

	public static final String TAG = "BleScanCompatLib";

	  public static void logVerbose(String message) {
	    if (Log.isLoggable(TAG, Log.VERBOSE)) {
	      Log.v(TAG, message);
	    }
	  }

	  public static void logWarning(String message) {
	    if (Log.isLoggable(TAG, Log.WARN)) {
	      Log.w(TAG, message);
	    }
	  }

	  public static void logDebug(String message) {
	    if (Log.isLoggable(TAG, Log.DEBUG)) {
	      Log.d(TAG, message);
	    }
	  }

	  public static void logInfo(String message) {
	    if (Log.isLoggable(TAG, Log.INFO)) {
	      Log.i(TAG, message);
	    }
	  }

	  public static void logError(String message, Exception... e) {
	    if (Log.isLoggable(TAG, Log.ERROR)) {
	      if (e == null || e.length == 0) {
	        Log.e(TAG, message);
	      } else {
	        Log.e(TAG, message, e[0]);
	      }
	    }
	  }
}
