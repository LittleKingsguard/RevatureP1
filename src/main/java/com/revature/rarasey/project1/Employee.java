package com.revature.rarasey.project1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

@Path("/employee")
public class Employee extends User{
    public Employee(String username, String firstName, String lastName) {
        super(username, firstName, lastName);
    }

    public Employee() {
        super();
    }

    @GET
    @Path("/transactions")
    @Produces(MediaType.APPLICATION_JSON)
    public String viewTransactions(){
        System.out.println("Trying to get transactions");
        TransactionDAO td = TransactionDAO.getDAO();
        ArrayList<Transaction> trans = td.readAll();
        ObjectMapper om = new ObjectMapper();
        StringBuilder transactions = new StringBuilder();
        try {
            transactions.append(om.writeValueAsString(trans));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(transactions.toString());
        return transactions.toString();
    }

    @GET
    @Path("/customers")
    @Produces(MediaType.APPLICATION_JSON)
    public String viewCustomers(){
        UserDAO ud = UserDAO.getDAO();
        ArrayList<Customer> customers = ud.readCustomers();
        ObjectMapper om = new ObjectMapper();
        StringBuilder value = new StringBuilder();
            try {
                value.append(om.writeValueAsString(customers));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        return value.toString();
    }


    @GET
    @Path("/accounts")
    @Produces(MediaType.APPLICATION_JSON)
    public String processAccounts(){

        AccountDAO ad = AccountDAO.getDAO();
        ArrayList<Account> accounts = ad.getPending();
        if (accounts.size() == 0){
            return "";
        }
        StringBuilder value = new StringBuilder();
        ObjectMapper om = new ObjectMapper();
            try {
                value.append(om.writeValueAsString(accounts));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            System.out.println(value.toString());
        return value.toString();
    }

    public boolean getEmployee() {
        return true;
    }
}