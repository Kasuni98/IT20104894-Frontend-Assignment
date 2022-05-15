package com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.Part;


public class Payment {
	private Connection connect() {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");     

			
			// Provide the correct details: DBServer/DBName, user name, password
			
			con= DriverManager.getConnection("jdbc:mysql://localhost:3306/gb_payments_client","root", ""); 
			//For testing
			System.out.print("Successfully connected!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}
	//inserting data
	public String insertPayment(String cardHolderName, String cardNumber, String cvv, String expirationDate) {
		String output = "";
		try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for inserting...";
			}
			
			
			// create a prepared statement
			String query = " insert into payment( `cardHolderName`, `cardNumber`, `cvv`, `expirationDate`)"
					+ " values( ?, ?, ?, ? )";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			
			
			// binding values
			preparedStmt.setString(1, cardHolderName);
			preparedStmt.setString(2, cardNumber);
			preparedStmt.setString(3, cvv);
			preparedStmt.setString(4, expirationDate);
			
			
			// execute the statement
			preparedStmt.execute();
			con.close();
			
			String payment = readPayment();
			output = "{\"status\":\"success\", \"data\": \"" + payment + "\"}";
		} catch (Exception e) {
			output = "{\"status\":\"error\", \"data\": \"Error while inserting the payment.\"}";
			System.err.println(e.getMessage());
		}
		return output;
		
	}
	
	
	//reading all payments
	public String readPayment() {
		String output = "";
		try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for reading...";
			}
			
			
			// Prepare the html table to be displayed
			output = "<table border='1' class='table table-bordered'>"
					+ "<tr>"
					+ "<th>Card Holder Name</th>"
					+ "<th>Card Number</th>" 
					+ "<th>CVV</th>"
					+ "<th>Expiration Date</th>" 
					+ "<th>Update</th>"
					+ "<th>Delete</th>"
					+ "</tr>";

			String query = "select * from payment";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			
			// iterate through the rows in the result set
			while (rs.next()) {
			String paymentId = Integer.toString(rs.getInt("paymentId"));
				String cardHolderName = rs.getString("cardHolderName");
				String cardNumber = rs.getString("cardNumber");
				String cvv = rs.getString("cvv");
				String expirationDate = rs.getString("expirationDate");
				
				
				// Add into the html table
			output += "<tr>";
		
				output += "<td>" + cardHolderName + "</td>";
				output += "<td>" + cardNumber + "</td>";
				output += "<td>" + cvv + "</td>";
				output += "<td>" + expirationDate + "</td>";
				
				
				 //action buttons
			output += "<td><input name='btnUpdate' type='button' value='Update' class='btnUpdate btn btn-success' data-paymentid='" + paymentId + "'></td>"	
				
				 + "<td><input name='btnRemove' type='button' value='Remove' class='btnRemove btn btn-danger' data-paymentid='" + paymentId + "'></td></tr>";
			}
			con.close();
			// Complete the html table
			output += "</table>";
			
		} catch (Exception e) {
			output = "Error while reading the payment...";
			System.err.println(e.getMessage());
		}
		return output;	
	}
		
	//updating payments
	public String updatePayment(int paymentId, String cardHolderName, String cardNumber, String cvv, String expirationDate){
		String output = "";
	    try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for updating...";
			}
			
			// create a prepared statement
			String query = "UPDATE payment SET cardHolderName=?,cardNumber=?,cvv=?,expirationDate=? WHERE paymentId=?";
			
			PreparedStatement preparedStmt = con.prepareStatement(query);
			// binding values
			preparedStmt.setString(1, cardHolderName);
			preparedStmt.setString(2, cardNumber);
			preparedStmt.setString(3, cvv);
			preparedStmt.setString(4, expirationDate);
			preparedStmt.setInt(5, paymentId);
			
			
			
			// execute the statement
			 preparedStmt.execute();
			 con.close();
			 String payment = readPayment();
			 output = "{\"status\":\"success\", \"data\": \"" + payment + "\"}";
		} catch (Exception e) {
			 output = "{\"status\":\"error\", \"data\": \"Error while updating the payment.\"}"; 
			 System.err.println(e.getMessage());
		}
		return output;
	}
	
	//delete payment
	public String deletePayment(int paymentId) { 
	    String output = ""; 
	    try { 
		    Connection con = connect(); 
		    if (con == null) {
		    	   return "Error while connecting to the database for deleting..."; 
		      } 
		       
		     // create a prepared statement
		     String query = "delete from payment where paymentId=?"; 
		     PreparedStatement preparedStmt = con.prepareStatement(query); 
		     
		     // binding values
		     preparedStmt.setInt(1, paymentId); 
		     
		    // execute the statement
		    preparedStmt.execute(); 
		    con.close(); 
		    
		    String payment = readPayment();
		    output = "{\"status\":\"success\", \"data\": \"" + payment + "\"}";
		    } 
	    catch (Exception e) { 
	            output = "{\"status\":\"error\", \"data\": \"Error while deleting the payment.\"}";  
	            System.err.println(e.getMessage()); 
	        } 
	        return output; 
	 }
	
	
	//read most relevant payment details
		public String readOnePayment()
		 {
		 String output = "";
		 try
		 {
		 Connection con = connect();
		 if (con == null)
		 {
			 return "Error while connecting to the database for reading..."; 
			 }
		 // Prepare the view table to be displayed
	 output = "<table border='1'>"					
					+ "<tr>"
					+ "<th>Payment ID</th>"
					+ "<th>Card Holder Name</th>"
					+ "<th>Card Number</th>" 
					+ "<th>CVV</th>"
					+ "<th>Expiration Date</th>"  
					+ "<th>Action</th>"
					+ "</tr>";

		 String query = "select * from payment where PaymentId= (Select max(PaymentId) from payment)";
		 Statement stmt = con.createStatement();
		 ResultSet rs = stmt.executeQuery(query);
		 // iterate through the rows in the result set
		 while (rs.next())
		 {
			 
				String paymentId = rs.getString("paymentId");
				String cardHolderName = rs.getString("cardHolderName");
				String cardNumber = rs.getString("cardNumber");
				String cvv = rs.getString("cvv");
				String expirationDate = rs.getString("expirationDate");
				
				
		 // Add into the html table
				output += "<tr><td>" + paymentId + "</td>";
				output += "<td>" + cardHolderName + "</td>";
				output += "<td>" + cardNumber + "</td>";
				output += "<td>" + cvv + "</td>";
				output += "<td>" + expirationDate + "</td>";
				
		 // buttons
				output += "<td>" + "<input name='btnUpdate' type='button' value='Update' class='btn btn-secondary'>"
						+ " <input name='btnRemove' type='submit' value='Remove' class='btn btn-danger'>" + "</td></tr>"
					
					 +"<input name='paymentId' type='hidden' value='" + paymentId + "'>" + "</form></td></tr>";
		 }
		 con.close();
		 // Complete the html table
		 output += "</table>";
		 }
		   catch (Exception e)
		 {
		   output = "Error while reading the items.";
		 System.err.println(e.getMessage());
		 }
		 return output;
		 } 
		



}
