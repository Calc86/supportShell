package com.ui.console;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by calc on 24.03.14.
 *
 */
public class Environment {
    public Environment() {
    }

    private static String getOsName(){
        return System.getProperty("os.name").toLowerCase();
    }

    private static boolean isMac(){
        return getOsName().contains("mac");
    }

    private static boolean isWin(){
        return getOsName().contains("win");
    }

    private static boolean isUnix(){
        return (getOsName().contains("nix") || getOsName().contains("nux"));
    }

    private static String getOsVersion(){
        return System.getProperty("os.version");
    }

    public void setEncoding(){
        String encoding = "cp1251";

        if(Environment.isWin()) encoding = "Cp866";
        //if(Environment.isWin()) encoding = "utf8";
        else if (Environment.isMac()) encoding = "utf8";
        else if (Environment.isUnix()) encoding = "utf8";

        try{
            System.setOut(new PrintStream(System.out, true, encoding));
        }catch (UnsupportedEncodingException e){
            //
        }
        try{
            System.setErr(new PrintStream(System.out, true, encoding));
        }catch (UnsupportedEncodingException e){
            //
        }
    }
}
