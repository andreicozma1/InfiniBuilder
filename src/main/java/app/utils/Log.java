package app.utils;

import app.GameBuilder;

import java.sql.Timestamp;

public class Log {
    // 0 1 2 3
    public static boolean verbose = true;

    // error - prints if something goes wrong - important
    public static void e(String TAG, String message) {
        if (verbose)
            System.out.println("*E* -> " + new Timestamp(GameBuilder.time_current).getTime() + " : " + TAG + " : " + message);
    }

    // warning - prints if something could go wrong
    public static void w(String TAG, String message) {
        if (verbose) System.out.println("*W* -> " + GameBuilder.time_current + " : " + TAG + " : " + message);
    }

    // debug - print out debugging information
    public static void d(String TAG, String message) {
        if (verbose) System.out.println(new Timestamp(GameBuilder.time_current) + " *D-> " + TAG + ": " + message);
    }

    // info - print info about a class
    public static void i(String TAG, String message) {
        if (verbose) System.out.println("*I* -> " + GameBuilder.time_current + " : " + TAG + " : " + message);
    }


    public static boolean getVerbose() {
        return verbose;
    }

    public static void setVerbose(boolean verbose) {
        Log.verbose = !verbose;
    }
}

