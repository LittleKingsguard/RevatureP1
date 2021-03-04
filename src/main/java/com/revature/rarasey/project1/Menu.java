package com.revature.rarasey.project1;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

    public class Menu {
        private static User u;
        private static final Logger logger = Logger.getLogger(Menu.class);

        public static Logger getLogger() {
            return logger;
        }

        private static Connection conn;
        static{
            try{
                conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Bank_Demo", "JavaDemo", "javapass");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public static void main(String []args){
            PropertyConfigurator.configure("C:\\Users\\raras\\IdeaProjects\\project0\\src\\main\\resources\\log4j.properties");
            logger.info("Something");
            setConnections();
        }
        public static User getU(){
            return u;
        }

        public static User registerUser(Scanner scan, UserDAO ud){
            System.out.print("Enter username: ");
            String username = scan.next();
            System.out.print("Enter password: ");
            String password = scan.next();
            System.out.print("Enter first name: ");
            String firstName = scan.next();
            System.out.print("Enter last name: ");
            String lastName = scan.next();
            return ud.registerUser(username, firstName, lastName, password, false);
        }

        public static User login(Scanner scan, UserDAO ud){
            System.out.print("Enter username: ");
            String username = scan.next();
            System.out.print("Enter password: ");
            String password = scan.next();
            return ud.loginUser(username, password);
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
