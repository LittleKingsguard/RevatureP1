package com.revature.rarasey.project1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;


@Path("/customer")
public class Customer extends User {
    public Customer(String username, String firstName, String lastName) {
        super(username, firstName, lastName);
    }

    public Customer(String username) {
        super(username);
    }

    public Customer() {
        super();
    }

    private ArrayList<Account> accountsHeld;

    public ArrayList<Account> getAccountsHeld() {
        return accountsHeld;
    }
    public void setAccountsHeld(ArrayList<Account> accounts){
        for (Account acc: accounts) {
            acc.setHolders(null);
        }
        accountsHeld = accounts;
    }

    @GET
    @Path("/access/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAccounts(@PathParam("username") String user_id){
        System.out.println("This ran for user " + user_id);
        Customer cust = new Customer(user_id);
        cust = (Customer) UserDAO.getDAO().read(cust);
        cust.setAccountsHeld(AccountDAO.getDAO().getAccounts(cust));
        String response = "";
        ObjectMapper om = new ObjectMapper();
            try {
                response = om.writeValueAsString(cust);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
        }
        System.out.println(response);
        return response;
    }

    @POST
    @Path("/{username}/apply/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String applyAccount(@PathParam("username") String user_id, Account acc){
        System.out.println("This triggered");
        ArrayList<Customer> customer = new ArrayList<Customer>();
        customer.add(new Customer(user_id));
        acc = new Account(customer, acc.getBalance(), 2);
        acc = AccountDAO.getDAO().insertNew(acc);
        String response = "";
        ObjectMapper om = new ObjectMapper();
        try {
            response = om.writeValueAsString(acc);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(response);
        return response;
    }
}