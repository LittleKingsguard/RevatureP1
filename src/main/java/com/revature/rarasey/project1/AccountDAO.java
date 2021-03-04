package com.revature.rarasey.project1;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class AccountDAO implements CRUD<Account> {

    private static AccountDAO ACCOUNT_DAO;
    private Connection conn;

    public AccountDAO(Connection conn) {
        this.conn = conn;
    }

    public static AccountDAO getDAO() {
        return ACCOUNT_DAO;
    }

    public static void setDAO(AccountDAO DAO) {
        ACCOUNT_DAO = DAO;
    }

    public ArrayList<Account> getAccounts(Customer user){
        ArrayList<Account> accounts = new ArrayList<Account>();
        ArrayList<Customer> u = new ArrayList<Customer>();
        u.add(user);
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM public.account JOIN public.User_Account " +
                "ON Account.account_id = User_Account.account_id " +
                "WHERE User_Account.user_id = ?")) {
            ps.setString(1, user.getUsername());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                accounts.add(new Account(
                        rs.getInt("account_id"),
                        u,
                        rs.getFloat("balance"),
                        rs.getInt("status_id")));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public ArrayList<Account> getPending(){
        ArrayList<Account> accounts = new ArrayList<Account>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM account " +
                "JOIN User_Account ON account.account_id = user_account.account_id " +
                "JOIN Users ON user_account.user_id = users.username " +
                "WHERE status_id = 2 " +
                "ORDER BY account.account_id")) {
            ResultSet rs = ps.executeQuery();
            int id = 0;
            float balance = 0f;
            ArrayList<Customer> customers = new ArrayList<Customer>();
            boolean found = false;
            while (rs.next()){
                if (id == rs.getInt("account_id")){
                    customers.add(new Customer (
                            rs.getString("users.username"),
                            rs.getString("users.first_name"),
                            rs.getString("users.last_name")
                    ));
                }
                else if (!found){
                    found = true;
                    id = rs.getInt("account_id");
                    balance = rs.getFloat("balance");
                    customers.add(new Customer (
                            rs.getString("username"),
                            rs.getString("first_name"),
                            rs.getString("last_name")
                    ));
                }
                else {
                    accounts.add(new Account(
                            id,
                            customers,
                            balance,
                            2));
                    id = rs.getInt("account_id");
                    balance = rs.getFloat("balance");
                    customers.add(new Customer (
                            rs.getString("username"),
                            rs.getString("first_name"),
                            rs.getString("last_name")
                    ));
                }
            }
            if (found){
                accounts.add(new Account(
                        id,
                        customers,
                        balance,
                        2));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    @Override
    public Account saveChanges(Account target) {
        Account acc = null;
        try(PreparedStatement ps = conn.prepareStatement("UPDATE Account " +
                "SET status_id = ? " +
                "WHERE account_id = ?")){
            ps.setInt(1, target.getStatus());
            ps.setInt(2, target.getId());
            if (0 <= ps.executeUpdate()){
                acc = target;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return acc;
    }

    @Override
    public Account insertNew(Account target) {
        Account acc = null;
        try(CallableStatement cs = conn.prepareCall("CALL public.add_account( ? , ?)")){
            ArrayList<Customer> users = target.getHolders();
            String[] usernames = new String[users.size()];
            int i = 0;
            for (Customer c: users) {
                usernames[i] = c.getUsername();
                i++;
            }
            Array names = conn.createArrayOf("text", usernames);
            cs.setArray(1, names);
            cs.setBigDecimal(2, new BigDecimal(target.getBalance()));
            if (0 <= cs.executeUpdate()){
                acc = target;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return acc;
    }

    @Override
    public Account read(Account target) {
        Account acc = null;
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM account " +
                "JOIN User_Account ON account.account_id = user_account.account_id " +
                "JOIN Users ON user_account.user_id = users.username " +
                "WHERE account.account_id = ? " +
                "ORDER BY account.account_id")) {
            ps.setInt(1, target.getId());
            ResultSet rs = ps.executeQuery();
            int id = 0;
            float balance = 0f;
            int approval = 0;
            ArrayList<Customer> customers = new ArrayList<Customer>();
            boolean found = false;
            while (rs.next()) {
                id = rs.getInt("account_id");
                balance = rs.getFloat("balance");
                approval = rs.getInt("status_id");
                customers.add(new Customer(
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name")
                ));
            }
            ArrayList<Transaction> trans = new ArrayList<Transaction>();
            PreparedStatement transPs = conn.prepareStatement("SELECT * FROM Transactions " +
                    "WHERE origin = ?");
            transPs.setInt(1, id);
            rs = transPs.executeQuery();
            while (rs.next()){
                trans.add(new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getFloat("amount"),
                        rs.getInt("destination"),
                        rs.getInt("origin"),
                        rs.getInt("status_id")
                ));
            }
            PreparedStatement destPs = conn.prepareStatement("SELECT * FROM Transactions " +
                    "WHERE destination = ?");
            destPs.setInt(1, id);
            rs = destPs.executeQuery();
            while (rs.next()){
                trans.add(new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getFloat("amount"),
                        rs.getInt("destination"),
                        rs.getInt("origin"),
                        rs.getInt("status_id")
                ));
            }
            acc = new Account(id, trans, customers, balance, approval);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return acc;
    }
}