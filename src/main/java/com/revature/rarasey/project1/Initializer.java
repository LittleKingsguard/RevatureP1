package com.revature.rarasey.project1;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Initializer implements ServletContextListener {
    private static final Logger logger = Logger.getLogger(Initializer.class);

    private static Connection conn;
    static{
        PropertyConfigurator.configure("C:\\Users\\raras\\IdeaProjects\\project0\\src\\main\\resources\\log4j.properties");
        logger.info("Something");
        try{
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Bank_Demo", "JavaDemo", "javapass");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        setConnections();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
    public static void setConnections(){
        UserDAO ud = new UserDAO(conn);
        UserDAO.setDAO(ud);
        AccountDAO ad = new AccountDAO(conn);
        AccountDAO.setDAO(ad);
        TransactionDAO td = new TransactionDAO(conn);
        TransactionDAO.setDAO(td);
    }
}
