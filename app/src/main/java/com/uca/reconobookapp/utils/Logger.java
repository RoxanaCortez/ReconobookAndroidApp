/*

MIT License

Copyright (c) 2021 TensorFlow

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons
to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

* */




package com.uca.reconobookapp.utils;

import android.util.Log;

import java.util.HashSet;
import java.util.Set;

public final class Logger {
    private static final String DEFAULT_TAG = "tensorflow";
    private static final int DEFAULT_MIN_LOG_LEVEL = Log.DEBUG;

    // Classes to be ignored when examining the stack trace
    private static final Set<String> IGNORED_CLASS_NAMES;

    static {
        IGNORED_CLASS_NAMES = new HashSet<String>(3);
        IGNORED_CLASS_NAMES.add("dalvik.system.VMStack");
        IGNORED_CLASS_NAMES.add("java.lang.Thread");
        IGNORED_CLASS_NAMES.add(Logger.class.getCanonicalName());
    }

    private final String tag;
    private final String messagePrefix;
    private int minLogLevel = DEFAULT_MIN_LOG_LEVEL;

    public Logger(final Class<?> clazz) {
        this(clazz.getSimpleName());
    }

    public Logger(final String messagePrefix) {
        this(DEFAULT_TAG, messagePrefix);
    }

    public Logger(final String tag, final String messagePrefix) {
        this.tag = tag;
        final String prefix = messagePrefix == null ? getCallerSimpleName() : messagePrefix;
        this.messagePrefix = (prefix.length() > 0) ? prefix + ": " : prefix;
    }

    /** Creates a Logger using the caller's class name as the message prefix. */
    public Logger() {
        this(DEFAULT_TAG, null);
    }

    /** Creates a Logger using the caller's class name as the message prefix. */
    public Logger(final int minLogLevel) {
        this(DEFAULT_TAG, null);
        this.minLogLevel = minLogLevel;
    }

    private static String getCallerSimpleName() {
        // Get the current callstack so we can pull the class of the caller off of it.
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        for (final StackTraceElement elem : stackTrace) {
            final String className = elem.getClassName();
            if (!IGNORED_CLASS_NAMES.contains(className)) {
                // We're only interested in the simple name of the class, not the complete package.
                final String[] classParts = className.split("\\.");
                return classParts[classParts.length - 1];
            }
        }

        return Logger.class.getSimpleName();
    }

    public void setMinLogLevel(final int minLogLevel) {
        this.minLogLevel = minLogLevel;
    }

    public boolean isLoggable(final int logLevel) {
        return logLevel >= minLogLevel || Log.isLoggable(tag, logLevel);
    }

    private String toMessage(final String format, final Object... args) {
        return messagePrefix + (args.length > 0 ? String.format(format, args) : format);
    }

    public void v(final String format, final Object... args) {
        if (isLoggable(Log.VERBOSE)) {
            Log.v(tag, toMessage(format, args));
        }
    }

    public void v(final Throwable t, final String format, final Object... args) {
        if (isLoggable(Log.VERBOSE)) {
            Log.v(tag, toMessage(format, args), t);
        }
    }

    public void d(final String format, final Object... args) {
        if (isLoggable(Log.DEBUG)) {
            Log.d(tag, toMessage(format, args));
        }
    }

    public void d(final Throwable t, final String format, final Object... args) {
        if (isLoggable(Log.DEBUG)) {
            Log.d(tag, toMessage(format, args), t);
        }
    }

    public void i(final String format, final Object... args) {
        if (isLoggable(Log.INFO)) {
            Log.i(tag, toMessage(format, args));
        }
    }

    public void i(final Throwable t, final String format, final Object... args) {
        if (isLoggable(Log.INFO)) {
            Log.i(tag, toMessage(format, args), t);
        }
    }

    public void w(final String format, final Object... args) {
        if (isLoggable(Log.WARN)) {
            Log.w(tag, toMessage(format, args));
        }
    }

    public void w(final Throwable t, final String format, final Object... args) {
        if (isLoggable(Log.WARN)) {
            Log.w(tag, toMessage(format, args), t);
        }
    }

    public void e(final String format, final Object... args) {
        if (isLoggable(Log.ERROR)) {
            Log.e(tag, toMessage(format, args));
        }
    }

    public void e(final Throwable t, final String format, final Object... args) {
        if (isLoggable(Log.ERROR)) {
            Log.e(tag, toMessage(format, args), t);
        }
    }
}
