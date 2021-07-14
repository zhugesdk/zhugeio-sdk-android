package com.zhuge.analysis.deepshare.utils;


import com.zhuge.analysis.util.ZGLogger;

public class Log {

    public static final boolean LOGA = true;

    public static final boolean LOGE = true;

    public static final boolean LOGW = true;

    public static final boolean LOGI = true;

    public static final boolean LOGD = true;

    public static final boolean LOGV = true;

    /**
     * Priority constant for the println method; use Log.v.
     */
    public static final int VERBOSE = 2;

    /**
     * Priority constant for the println method; use Log.d.
     */
    public static final int DEBUG = 3;

    /**
     * Priority constant for the println method; use Log.i.
     */
    public static final int INFO = 4;

    /**
     * Priority constant for the println method; use Log.w.
     */
    public static final int WARN = 5;

    /**
     * Priority constant for the println method; use Log.e.
     */
    public static final int ERROR = 6;

    /**
     * Priority constant for the println method.
     */
    public static final int ASSERT = 7;

    private Log() {
    }

    private static boolean isDebug() {
        return ZGLogger.logEnable;
    }

    /**
     * Send a {@link #VERBOSE} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @return int
     */
    public static int v(String tag, String msg) {
        if (!isDebug()) {
            return 0;
        }
        if (LOGV) {
            return android.util.Log.v(tag, msg);
        }
        return 0;
    }

    /**
     * Send a {@link #VERBOSE} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     * @return int
     */
    public static int v(String tag, String msg, Throwable tr) {
        if (!isDebug()) {
            return 0;
        }
        if (LOGV) {
            return android.util.Log.v(tag, msg, tr);
        }
        return 0;
    }

    /**
     * Send a {@link #DEBUG} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @return int
     */
    public static int d(String tag, String msg) {
        if (!isDebug()) {
            return 0;
        }
        if (LOGD) {
            return android.util.Log.d(tag, msg);
        }
        return 0;
    }

    /**
     * Send a {@link #DEBUG} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     * @return int
     */
    public static int d(String tag, String msg, Throwable tr) {
        if (!isDebug()) {
            return 0;
        }
        if (LOGD) {
            return android.util.Log.d(tag, msg, tr);
        }
        return 0;
    }

    /**
     * Send an {@link #INFO} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @return int
     */
    public static int i(String tag, String msg) {
        if (!isDebug()) {
            return 0;
        }
        if (LOGI) {
            return android.util.Log.i(tag, msg);
        }
        return 0;
    }

    /**
     * Send a {@link #INFO} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     * @return int
     */
    public static int i(String tag, String msg, Throwable tr) {
        if (!isDebug()) {
            return 0;
        }
        if (LOGI) {
            return android.util.Log.i(tag, msg, tr);
        }
        return 0;
    }

    /**
     * Send a {@link #WARN} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @return int
     */
    public static int w(String tag, String msg) {
        if (!isDebug()) {
            return 0;
        }
        if (LOGW) {
            return android.util.Log.w(tag, msg);
        }
        return 0;
    }

    /**
     * Send a {@link #WARN} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     * @return int
     */
    public static int w(String tag, String msg, Throwable tr) {
        if (!isDebug()) {
            return 0;
        }
        if (LOGW) {
            return android.util.Log.w(tag, msg, tr);
        }
        return 0;
    }

    /**
     * Send a {@link #WARN} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param tr An exception to log
     * @return int
     */
    public static int w(String tag, Throwable tr) {
        if (!isDebug()) {
            return 0;
        }
        if (LOGW) {
            return android.util.Log.w(tag, tr);
        }
        return 0;
    }

    /**
     * Send an {@link #ERROR} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @return int
     */
    public static int e(String tag, String msg) {
        if (LOGE) {
            return android.util.Log.e(tag, msg);
        }
        return 0;
    }

    /**
     * Send a {@link #ERROR} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     * @return int
     */
    public static int e(String tag, String msg, Throwable tr) {
        if (LOGE) {
            return android.util.Log.e(tag, msg, tr);
        }
        return 0;
    }

    /**
     * What a Terrible ErrorString: Report a condition that should never happen.
     * The error will always be logged at level ASSERT with the call stack.
     * Depending on system configuration, a report may be added to the
     * {@link android.os.DropBoxManager} and/or the process may be terminated
     * immediately with an error dialog.
     *
     * @param tag Used to identify the source of a log message.
     * @param msg The message you would like logged.
     * @return int
     */
    public static int wtf(String tag, String msg) {
        if (LOGA) {
            return android.util.Log.e(tag, msg);
        }
        return 0;
    }

    /**
     * What a Terrible ErrorString: Report an exception that should never happen.
     * Similar to {@link #wtf(String, String)}, with an exception to log.
     *
     * @param tag Used to identify the source of a log message.
     * @param tr  An exception to log.
     * @return int
     */
    public static int wtf(String tag, Throwable tr) {
        if (LOGA) {
            return android.util.Log.e(tag, "", tr);
        }
        return 0;
    }

    /**
     * What a Terrible ErrorString: Report an exception that should never happen.
     * Similar to {@link #wtf(String, Throwable)}, with a message as well.
     *
     * @param tag Used to identify the source of a log message.
     * @param msg The message you would like logged.
     * @param tr  An exception to log.  May be null.
     * @return int
     */
    public static int wtf(String tag, String msg, Throwable tr) {
        if (LOGA) {
            return android.util.Log.e(tag, msg, tr);
        }
        return 0;
    }

    /**
     * Handy function to get a loggable stack trace from a Throwable
     *
     * @param tr An exception to log
     * @return string
     */
    public static String getStackTraceString(Throwable tr) {
        return android.util.Log.getStackTraceString(tr);
    }

    /**
     * Low-level logging call.
     *
     * @param priority The priority/type of this log message
     * @param tag      Used to identify the source of a log message.  It usually identifies
     *                 the class or activity where the log call occurs.
     * @param msg      The message you would like logged.
     * @return The number of bytes written.
     */
    public static int println(int priority, String tag, String msg) {
        if (!isDebug()) {
            return 0;
        }
        boolean allowed = false;
        switch (priority) {
            case VERBOSE:
                allowed = LOGV;
                break;
            case DEBUG:
                allowed = LOGD;
                break;
            case INFO:
                allowed = LOGI;
                break;
            case WARN:
                allowed = LOGW;
                break;
            case ERROR:
                allowed = LOGE;
                break;
            case ASSERT:
                allowed = LOGA;
                break;
            default:
                break;
        }
        if (allowed) {
            return android.util.Log.println(priority, tag, msg);
        }
        return 0;
    }

    public static int printCallstack(String tag) {
        if (!isDebug()) {
            return 0;
        }
        if (LOGV) {
            Throwable t = new Throwable();
            String log = getStackTraceString(t);
            return android.util.Log.v(tag, log);
        }
        return 0;
    }
}
