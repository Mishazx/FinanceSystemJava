package ru.mishazx.financesystem.utils;

import ru.mishazx.financesystem.config.ConfigApp;

public class CustomIO {
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String WHITE = "\u001B[37m";
    private static final String XZ = "\u001B[38m";

    public static Boolean debug;

    static {
        try {
            debug = ConfigApp.getDebug();
        } catch (Exception e) {
            e.printStackTrace();
            debug = false;
        }
    }

    public static void PrintWarning(String print) {
        System.out.println(YELLOW + print + RESET);
    }

    public static void PrintError(String print){
        System.out.println(RED + print + RESET);
    }

    public static void PrintError(String print, boolean debug) {
        if (debug) {
            System.out.println(RED + print + RESET);
        }
    }

    public static void PrintSuccess(String print) {
        System.out.println(GREEN + print + RESET);
    }

    public static void PrintSuccess(String print, boolean debug) {
        if (debug) {
            System.out.println(GREEN + print + RESET);
        }
    }

    public static void PrintDebug(String print) {
        if (debug != null && debug) {
            System.out.println(WHITE + print + RESET);
        }
    }

    public static void PrintInfo(String print) {
        System.out.println(XZ + print + RESET);
    }
}
