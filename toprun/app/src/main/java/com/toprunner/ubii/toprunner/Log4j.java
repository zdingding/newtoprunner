package com.toprunner.ubii.toprunner;


import android.os.Environment;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;

import de.mindpipe.android.logging.log4j.LogConfigurator;


/**
 * Created by Rec on 2016/4/18.
 */
public class Log4j {
    public Logger logger;
    String fileSeparator = File.separator;

    public void configLog() {
        LogConfigurator logConfigurator = new LogConfigurator();
        logConfigurator.setFileName(Environment.getExternalStorageDirectory() + fileSeparator + "TopRun"  + fileSeparator + "TopRunLog_log4j.log");
        logConfigurator.setRootLevel(Level.TRACE);
        logConfigurator.configure();
        try {
            logger = Logger.getLogger(Log4j.class.getName());
        }
        catch (Exception e) {
            System.err.println(e);
        }

    }

    public void configLog(String logFileName) {
        LogConfigurator logConfigurator = new LogConfigurator();

        logConfigurator.setFileName(Environment.getExternalStorageDirectory() + fileSeparator + "TopRun"  + fileSeparator +  logFileName);
        logConfigurator.setRootLevel(Level.TRACE);
        logConfigurator.configure();
        try {
            logger = Logger.getLogger(Log4j.class.getName());
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }
}
