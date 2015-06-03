package org.itemtracker.common.utils;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Custom Logger Class for logging Errors and Exceptions <br>
 * Will put them into a database located in logs
 *
 * @author James rockking1379@gmail.com
 */
public class Logger
{
    /**
     * Create Statement for Error File <br>
     * Could make a single database file but this seems better to have one per
     * day
     */
    private final static String createError = "CREATE TABLE IF NOT EXISTS Error(error_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,error_time DATE NOT NULL, error_message VARCHAR(100) NOT NULL,error_stacktrace TEXT NOT NULL)";
    private final static String createEvent = "CREATE TABLE IF NOT EXISTS Event(event_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, event_time DATE NOT NULL, event_message VARCHAR(100) NOT NULL, event_user_name VARCHAR(100) NOT NULL)";

    /**
     * Logs data from Exception
     *
     * @param ex Exception caught
     * @return status of logging success
     */
    public static boolean logException(Exception ex)
    {
        boolean retVal;

        if (logExists())
        {
            Date d = new Date();
            String sDate = new SimpleDateFormat("yyyy-MM-dd").format(d);
            String fileName = "./Logs/" + sDate + ".db";

            try
            {
                Connection con = DriverManager.getConnection("jdbc:sqlite:"
                        + fileName);
                java.sql.PreparedStatement stmnt = con
                        .prepareStatement("INSERT INTO Error(error_time, error_message, error_stacktrace) VALUES(?,?,?)");

                stmnt.setString(1, String.valueOf(d.getTime()));
                stmnt.setString(2, ex.getMessage());
                Writer stackTrace = new StringWriter();
                PrintWriter pWriter = new PrintWriter(stackTrace);
                ex.printStackTrace(pWriter);
                stmnt.setString(3, stackTrace.toString());

                retVal = stmnt.execute();
                pWriter.close();
                stackTrace.close();
                stmnt.close();
                con.close();
            }
            catch (SQLException e)
            {
                System.err.println("Logging Error");
                e.printStackTrace();
                retVal = false;
            }
            catch (IOException e)
            {
                System.err.println("Logging Error");
                e.printStackTrace();
                retVal = false;
            }
        }
        else
        {
            if (createLog())
            {
                return logException(ex);
            }
            else
            {
                retVal = false;
            }
        }

        return retVal;
    }

    /**
     * Logs System Event
     * Used for general purpose logging
     *
     * @param eventMessage Message of Event
     * @param userName User Name of User logging event. Can and often will be 'SYSTEM'
     * @return status of logging success
     */
    public static boolean logEvent(String eventMessage, String userName)
    {
        boolean retVal;

        if (logExists())
        {
            Date d = new Date();
            String sDate = new SimpleDateFormat("yyyy-MM-dd").format(d);
            String fileName = "./Logs/" + sDate + ".db";

            try
            {
                Connection con = DriverManager.getConnection("jdbc:sqlite:"
                        + fileName);
                java.sql.PreparedStatement stmnt = con
                        .prepareStatement("INSERT INTO Event(event_time, event_message, event_user_name) VALUES(?,?,?)");

                stmnt.setString(1, String.valueOf(d.getTime()));
                stmnt.setString(2, eventMessage);
                stmnt.setString(3, userName);

                retVal = stmnt.execute();
                stmnt.close();
                con.close();
            }
            catch (SQLException e)
            {
                System.err.println("Logging Error");
                e.printStackTrace();
                retVal = false;
            }
        }
        else
        {
            if (createLog())
            {
                return logEvent(eventMessage, userName);
            }
            else
            {
                retVal = false;
            }
        }

        return retVal;
    }

    private static boolean logExists()
    {
        File dir = new File("./Logs");
        Date d = new Date();
        String sDate = new SimpleDateFormat("yyyy-MM-dd").format(d);
        String fileName = "./Logs/" + sDate + ".db";
        File f = new File(fileName);

        return (dir.exists() && f.exists());
    }

    private static boolean createLog()
    {
        File dir = new File("./Logs");
        boolean retVal = true;

        if (!dir.exists())
        {
            retVal = dir.mkdir();
        }

        Date d = new Date();
        String sDate = new SimpleDateFormat("yyyy-MM-dd").format(d);
        String fileName = "./Logs/" + sDate + ".db";
        File f = new File(fileName);

        try
        {
            Class.forName("org.sqlite.JDBC");
        }
        catch (ClassNotFoundException e)
        {
            System.err.println("Logging Error");
            e.printStackTrace();
            retVal = false;
        }

        if (!f.exists())
        {
            try
            {
                retVal = f.createNewFile();

                Connection con = DriverManager.getConnection("jdbc:sqlite:"
                        + fileName);
                Statement stmnt = con.createStatement();

                stmnt.execute(createError);
                stmnt.execute(createEvent);

                stmnt.close();
                con.close();
            }
            catch (IOException e)
            {
                System.err.println("Logging Error");
                e.printStackTrace();
                retVal = false;
            }
            catch (SQLException e)
            {
                System.err.println("Logging Error");
                e.printStackTrace();

                retVal = false;
            }
        }

        return retVal;
    }
}
