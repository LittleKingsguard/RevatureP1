package com.revature.rarasey.project1;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TransactionDAO implements CRUD<Transaction> {
    private static TransactionDAO transDAO;
    private Connection conn;

    public static TransactionDAO getDAO() {
        return transDAO;
    }

    public static void setDAO(TransactionDAO transDAO) {
        TransactionDAO.transDAO = transDAO;
    }

    public TransactionDAO (Connection connection){
        this.conn = connection;
    }

    @Override
    public Transaction saveChanges(Transaction target) {
        boolean valid = false;
        if (target.getApproved() == 2){
            try(PreparedStatement ps = conn.prepareStatement("SELECT public.approve_transfer( ? )")){
                ps.setInt(1, target.getId());
                ResultSet rs = ps.executeQuery();
                rs.next();
                valid = rs.getBoolean(1);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            try(PreparedStatement ps = conn.prepareStatement("UPDATE public.transactions " +
                    "SET status_id=3 " +
                    "WHERE transaction_id= ? ;")){
                ps.setInt(1, target.getId());
                int rows = ps.executeUpdate();
                valid = rows > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (valid){
            Menu.getLogger().info("Altered: " + target.toFullString());
            return target;
        }
        else {
            Menu.getLogger().info("Failed attempt to alter: " + target.toFullString());
            return null;
        }
    }

    @Override
    public Transaction insertNew(Transaction target) {
        boolean valid = false;
        if (target.getDestination() == 0){
            try (PreparedStatement ps = conn.prepareStatement("SELECT public.make_withdrawal(" +
                    " ? , ? )")){
                ps.setBigDecimal(1, new BigDecimal(target.getValue()));
                ps.setInt(2, target.getOrigin());
                ResultSet rs = ps.executeQuery();
                rs.next();
                valid = rs.getBoolean(1);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else if (target.getOrigin() == 0){
            try (PreparedStatement ps = conn.prepareStatement("SELECT public.make_deposit(" +
                    " ? , ? )")){
                ps.setBigDecimal(1, new BigDecimal(target.getValue()));
                ps.setInt(2, target.getDestination());
                ResultSet rs = ps.executeQuery();
                rs.next();
                valid = rs.getBoolean(1);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO public.transactions(" +
                    "amount, " +
                    "origin, " +
                    "destination, " +
                    "status_id) " +
                    "VALUES (?, ?, ?, 1);")){
                ps.setBigDecimal(1, new BigDecimal(target.getValue()));
                ps.setInt(2, target.getOrigin());
                ps.setInt(3, target.getDestination());
                int rows = ps.executeUpdate();
                valid = rows == 1;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (valid){
            Menu.getLogger().info("Added: " + target.toFullString());
            return target;
        }
        else {

            Menu.getLogger().info("Failed attempt to insert: " + target.toFullString());
            return null;
        }
    }

    @Override
    public Transaction read(Transaction target) {
        Transaction trans =  null;
        try (PreparedStatement ps = conn.prepareStatement("SELECT transaction_id, amount, origin, destination, status_id\n" +
                "\tFROM public.transactions " +
                "WHERE transaction_id = ?;")) {
            ps.setInt(1, target.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                trans = new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getFloat("amount"),
                        rs.getInt("destination"),
                        rs.getInt("origin"),
                        rs.getInt("status_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trans;
    }

    public ArrayList<Transaction> readAll() {
        ArrayList<Transaction> trans =  new ArrayList<Transaction>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT transaction_id, amount, origin, destination, status_id\n" +
                "\tFROM public.transactions;")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                trans.add(new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getFloat("amount"),
                        rs.getInt("destination"),
                        rs.getInt("origin"),
                        rs.getInt("status_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trans;
    }
}