package org.itemtracker.testgui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.itemtracker.common.database.DatabaseManager;
import org.itemtracker.common.database.SQLiteManager;
import org.itemtracker.common.utils.Logger;

import java.io.IOException;
import java.util.Properties;

/**
 * Main Entry point for Client Application
 * Created by james on 5/29/15.
 */
public class MainFrame extends Application
{
    public static DatabaseManager dbManager;

    public static Stage stage;

    public static Properties properties = null;

    public static void main(String[] args)
    {
        //crap goes here to start shit up
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        MainFrame.stage = stage;
        //load them properties
//        boolean setup = true;
//        try
//        {
//            properties = new Properties();
//            File propFile = new File("./configuration.properties");
//
//            if (propFile.exists())
//            {
//                //do stuff to read
//                setup = false;
//
//                FileInputStream fStream = new FileInputStream(propFile);
//                properties.load(fStream);
//
//                switch (Integer.valueOf(properties.getProperty("DBID")))
//                {
//                    case SQLiteManager.dbId:
//                    {
//                        dbManager = new SQLiteManager(properties.getProperty("DATABASE"));
//                        break;
//                    }
//                    case MSSQLManager.dbId:
//                    {
//                        dbManager = new MSSQLManager(properties.getProperty("DATABASE"), Integer.valueOf(properties.getProperty("DBPORT")), properties.getProperty("DBNAME"),
//                                properties.getProperty("DBUSER"), properties.getProperty("DBPWD"));
//                        break;
//                    }
//                    default:
//                    {
//                        throw new ConfigException("Configuration Error\nUnknown Database Type");
//                    }
//                }
//
//                fStream.close();
//            }
//        }
//        catch (IOException | ConfigException e)
//        {
//            // set up logger
//            System.err.println(e.getMessage());
//        }
//
//        if (setup)
//        {
//            //load setup page
//        }
//        else
//        {
//            //move to main screen
//        }
        dbManager = new SQLiteManager("./database.db");
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource(
                    "/org/itemtracker/fxml/testgui/MainPage.fxml"));
            Scene scene = new Scene(root, 600, 400);
            MainFrame.stage.setResizable(false);
            MainFrame.stage.centerOnScreen();
            MainFrame.stage.setScene(scene);
            MainFrame.stage.setTitle("Main Page");
            MainFrame.stage.show();
        }
        catch (IOException e)
        {
            Logger.logException(e);
        }
    }
}
