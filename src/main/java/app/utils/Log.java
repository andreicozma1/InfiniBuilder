package app.utils;

public class Log {
    public static int verbose = 0;

    public static void p(String TAG, String message) {
        if (verbose == 0) System.out.println(TAG + ": " + message);
    }

    public static int getVerbose() {
        return verbose;
    }

    public static void setVerbose(int verbose) {
        Log.verbose = verbose;
    }
}
