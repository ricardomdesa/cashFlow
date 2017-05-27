/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 *
 * @author Rog�rio Lecari�o Leite
 */
public class PropApp {

    public static void main(String[] args) {
        store();
        load();
    }

    /*
        Customizable according to each project's database
     */
    public static void store() {

        Properties prop = new Properties();
        OutputStream properties = null;
        OutputStream xml = null;
        String userDir = System.getProperty("user.dir") + "\\db\\";

        try {

            properties = new FileOutputStream("db\\config.properties");
            xml = new FileOutputStream("db\\config.xml");

            // set the properties value
            prop.setProperty("derby.system.home", userDir);
            prop.setProperty("database.user.name", "CashFlowDB");        //Change the db name here
            prop.setProperty("database.user.pass", "CashFlowDB_&740");        //Change the db name here
            prop.setProperty("database.dbname", "CashFlowDB");           //Change the db name here
            prop.setProperty("database.create", "true");

            prop.setProperty("database.framework", "embedded");//derbyclient
            prop.setProperty("database.driver", "org.apache.derby.jdbc.EmbeddedDriver");
            prop.setProperty("database.protocol", "jdbc:derby:");
            prop.setProperty("database.persistence.unit.name", "CashFlowPU");

            prop.list(System.out);

            // save properties to project root folder
            prop.store(properties, null);
            prop.storeToXML(xml, null);

        } catch (IOException io) {
            io.printStackTrace(System.err);
        } finally {
            if (properties != null) {
                try {
                    properties.close();
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }

            if (xml != null) {
                try {
                    xml.close();
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }

        }
    }

    public static void load() {

        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream("db\\config.properties");
            //input = PropApp.class.getClassLoader().getResourceAsStream("db\\config.properties");

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            prop.list(System.out);

        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }
        }
    }
}
