package com.utilsframework.android.analytics;

import android.util.Log;

/**
 * "Less-word" analog of Android {@link Log logger}
 * 
 */
public final class L {

	private static final String TAG = "<DBBEST>";
	private static final String LOG_FORMAT = "%1$s\n%2$s";
	private static volatile boolean DISABLED = false;

	private L() {
	}

	/** Enables logger (if {@link #disableLogging()} was called before) */
	public static final void enableLogging() {
		DISABLED = false;
	}

	/** Disables logger, no logs will be passed to LogCat, all log methods will do nothing */
	public static final void disableLogging() {
		DISABLED = true;
	}

	public static final void d(String message, Object... args) {
		log(Log.DEBUG, null, message, args);
	}

	public static final void i(String message, Object... args) {
		log(Log.INFO, null, message, args);
	}

	public static final void w(String message, Object... args) {
		log(Log.WARN, null, message, args);
	}

	public static final void e(Throwable ex) {
		log(Log.ERROR, ex, null);
	}

	public static final void e(String message, Object... args) {
		log(Log.ERROR, null, message, args);
	}

	public static final void e(Throwable ex, String message, Object... args) {
		log(Log.ERROR, ex, message, args);
	}

	private static final void log(int priority, Throwable ex, String message, Object... args) {
		if (DISABLED)
			return;
		if (args.length > 0) {
			message = String.format(message, args);
		}

		String log;
		if (ex == null) {
			log = message;
		} else {
			String logMessage = message == null ? ex.getMessage() : message;
			String logBody = Log.getStackTraceString(ex);
			log = String.format(LOG_FORMAT, logMessage, logBody);
		}
		Log.println(priority, TAG, log);
	}
}