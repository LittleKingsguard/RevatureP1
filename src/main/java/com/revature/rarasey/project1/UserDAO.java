package com.revature.rarasey.project1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDAO implements CRUD<User> {
    private static UserDAO USER_DAO;
    private Connection conn;

    public Connection getConn() {
        return conn;
    }

    public UserDAO(Connection conn){
        this.conn = conn;
    }

    public static UserDAO getDAO() {
        return USER_DAO;
    }

    public static void setDAO(UserDAO self) {
        USER_DAO = self;
    }

    public User registerUser(String username, String firstName, String lastName, String password, boolean isEmployee) {
        User u = null;
        try (PreparedStatement ps = conn.prepareStatement("Select public.register_user("
                + "?, ?, ?, ?, ?);")) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, firstName);
            ps.setString(4, lastName);
            ps.setBoolean(5, isEmployee);
            ResultSet rs = ps.executeQuery();
            rs.next();
            boolean inserted = rs.getBoolean(1);
            if (!inserted) {
                return null;
            }
            if(isEmployee) {
                u = new Employee(
                        username,
                        firstName,
                        lastName);
            }
            else {
                u = new Customer(
                        username,
                        firstName,
                        lastName);
            }
            return u;
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public User loginUser(String username, String password){
        User u = null;
        try (PreparedStatement ps = conn.prepareStatement("Select public.login_user("
                + "?, ?);")) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            rs.next();
            boolean inserted = rs.getBoolean(1);
            if (!inserted) {
                return null;
            }
            else {
                rs.close();
                PreparedStatement newPs = conn.prepareStatement("Select * from Users Where username = ?;");
                newPs.setString(1, username);
                rs = newPs.executeQuery();
                rs.next();
                boolean isEmployee = rs.getBoolean(5);

                if(isEmployee) {
                    u = new Employee(
                            username,
                            rs.getString("first_name"),
                            rs.getString("last_name"));
                }
                else {
                    u = new Customer(
                            username,
                            rs.getString("first_name"),
                            rs.getString("last_name"));
                }
            }
            return u;
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
    public ArrayList<Customer> readCustomers(){
        ArrayList<Customer> u = new ArrayList<Customer>();
        try (PreparedStatement newPs = conn.prepareStatement("Select * from Users Where is_employee = ?;"))
        {
            newPs.setBoolean(1, false);
            ResultSet rs = newPs.executeQuery();
            while(rs.next()) {
                u.add(new Customer(
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return u;
    }

    @Override
    public User saveChanges(User target) {
        return null;
    }

    @Override
    public User insertNew(User target) {
        return null;
    }

    @Override
    public User read(User target) {

        try (PreparedStatement newPs = conn.prepareStatement("Select * from Users Where username = ?;"))
        {
            newPs.setString(1, target.username);
            ResultSet rs = newPs.executeQuery();
            while(rs.next()) {
                target = new Customer(
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return target;
    }
}
