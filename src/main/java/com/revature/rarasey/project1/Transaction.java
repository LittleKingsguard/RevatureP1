package com.revature.rarasey.project1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Scanner;

@Path("/transaction")
public class Transaction {
    private int id;
    private float value;
    private int destination;
    private int origin;
    private int approved;

    public String getApprovalString(){
        switch (approved){
            case 1:
                return "Pending";
            case 2:
                return "Active";
            case 3:
                return "Rejected";
            default:
                return "N/A";
        }

    }

    public static void creationController (int owner, char type){
        Scanner scan = new Scanner(System.in);
        TransactionDAO td = TransactionDAO.getDAO();
        float input = 0f;
        input = SafeScan.getPosFloat(scan);
        Transaction trans = null;
        if (type == 'D'){
            trans = td.insertNew(new Transaction(input, owner, 0, 2));
        }
        else if (type == 'W'){
            trans = td.insertNew(new Transaction(input, 0, owner, 2));
        }
        else {
            int target = SafeScan.getPosIntBan(scan, owner);
            trans = td.insertNew(new Transaction(input, target, owner, 1));
        }
        if (trans == null){
            System.out.println("Invalid transaction. Enter any input to return to account #" + owner);
            scan.next();
        }
    }

    public String getType(){
        if (destination == 0 ){
            return "Withdrawal";
        }
        if (origin == 0 ){
            return "Deposit";
        }
        return "Transfer";
    }

    public Transaction(int id, float value, int destination, int origin, int approved) {
        this.id = id;
        this.value = value;
        this.destination = destination;
        this.origin = origin;
        this.approved = approved;
    }

    public Transaction(float value, int destination, int origin, int approved) {
        this.value = value;
        this.destination = destination;
        this.origin = origin;
        this.approved = approved;
    }

    public void approve(){
        this.setApproved(2);
        TransactionDAO td = TransactionDAO.getDAO();
        Transaction target = td.saveChanges(this);
        if (target == null){
            this.setApproved(3);
            System.out.println("Funds did not clear. com.project0.Transaction rejected.");
        }
        else {
            System.out.println("com.project0.Transaction cleared.");
        }
    }

    public void reject(){
        this.setApproved(3);
        TransactionDAO td = TransactionDAO.getDAO();
        Transaction target = td.saveChanges(this);
        if (target == null){
            this.setApproved(1);
            System.out.println("Something went wrong.");
        }
        else {
            System.out.println("com.project0.Transaction rejected.");
        }
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public int getOrigin() {
        return origin;
    }

    public void setOrigin(int origin) {
        this.origin = origin;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        if (this.getType().equals("Transfer")){
            return "Transfer #" + id + ": $" + value + " from " + origin + " to " + destination;
        }
        return this.getType() +
                " #" + id +
                ": $" + value;
    }

    public String toFullString() {
        if (this.getType().equals("Transfer")){
            return "Transfer #" + id + ": $" + value + " from " + origin + " to " + destination;
        }
        return this.getType() +
                " #" + id +
                ": $" + value +
                " in Account #" + Math.max(origin, destination);
    }

    public String toString(int i) {
        if (incoming(i)) {
            if (this.getType().equals("Transfer")) {
                return "Transfer #" + id + ": $" + value + " from " + origin;
            }
            return "#" + id +
                    ": $" + value;
        }
        else {
            if (this.getType().equals("Transfer")){
                return "Transfer #" + id + ": $" + value + " to " + destination;
            }
            return " #" + id +
                    ": $" + value;
        }
    }

    public boolean incoming(int i){
        if (destination == i){
            return true;
        }
        return false;
    }

    public Transaction(int id, int approved) {
        this.id = id;
        this.approved = approved;
    }

    public Transaction() {
    }

    public Transaction(float value, int destination, int origin) {
        this.value = value;
        this.destination = destination;
        this.origin = origin;
    }

    @POST
    @Path("/deposit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String deposit(Transaction trans){
        trans.setApproved(2);
        trans.setOrigin(0);
        return getTransactionResponse(trans);
    }

    @POST
    @Path("/withdraw")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String withdrawal(Transaction trans){
        System.out.println(trans.toFullString());
        trans.setApproved(2);
        trans.setDestination(0);
        return getTransactionResponse(trans);
    }

    @POST
    @Path("/transfer")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String transfer(Transaction trans){
        trans.setApproved(1);
        return getTransactionResponse(trans);
    }

    private String getTransactionResponse(Transaction trans) {
        trans = TransactionDAO.getDAO().insertNew(trans);
        ObjectMapper om = new ObjectMapper();
        String response = "";
        try {
            response = om.writeValueAsString(trans);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(trans.toFullString());
        return response;
    }

    @GET
    @Path("{id}/approve")
    @Produces(MediaType.APPLICATION_JSON)
    public String approve(@PathParam("id") int id){
        System.out.println(id);
        Transaction trans = TransactionDAO.getDAO().saveChanges(new Transaction(id, 2));
        System.out.println(trans.toFullString());
        if (trans != null){
            trans = TransactionDAO.getDAO().read(trans);
        }
        String response = "";
        ObjectMapper om = new ObjectMapper();
        try {
            response = om.writeValueAsString(trans);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(response);
        return response;
    }



    @GET
    @Path("{id}/deny")
    @Produces(MediaType.APPLICATION_JSON)
    public String deny(@PathParam("id") int id){
        Transaction trans = TransactionDAO.getDAO().saveChanges(new Transaction(id, 3));
        if (trans != null){
            trans = TransactionDAO.getDAO().read(trans);
        }
        String response = "";
        ObjectMapper om = new ObjectMapper();
        try {
            response = om.writeValueAsString(trans);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(response);
        return response;
    }
}