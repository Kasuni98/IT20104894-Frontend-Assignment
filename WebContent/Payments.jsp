<%@page import="com.Payment" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Payment System Management</title>
<link rel="stylesheet" href="Views/bootstrap.min.css">
<link rel="stylesheet" href="Views/payments.css">
<script src="Components/jquery-3.2.1.js"></script>
<script src="Components/payments.js"></script>

</head>
<body>
<div class="container">
 <div class="row">
 <div class="col-6">

<h1>Electricity Payment Management</h1><br><br>
<h4>Add Payment Details</h4>
<form id="formPayments" name="formPayments" method="post" enctype="multipart/form-data">
	 Card Holder Name:
	<input id="cardHolderName" name="cardHolderName" type="text" class="form-control form-control-sm" placeholder="Enter card Holder Name">
	<br> Card Number:
	<input id="cardNumber" name="cardNumber" type="text" class="form-control form-control-sm" placeholder="Enter Card Number">
	<br> CVV:
	<input id="cvv" name="cvv" type="text" class="form-control form-control-sm" placeholder="Enter CVV">
	<br> Expiration Date:
	<input id="expirationDate" name="expirationDate" type="text" class="form-control form-control-sm" placeholder="Enter Expiration Date">
	
	<br>
	<br>
	<input id="btnSave" name="btnSave" type="button" value="Save" class="btn btn-success">
	<input type="hidden" id="hidPaymentIdSave" name="hidPaymentIdSave" value="">
</form>
<br>
<div id="alertSuccess" class="alert alert-info"></div>
<div id="alertError" class="alert alert-danger"></div>

<br>
<h4>All Payments Details</h4>
<div id="divPaymentsGrid">
	 <%
	 	Payment payment = new Payment();
	 	out.print(payment.readPayment());
	 %>
</div>

</div>
</div>
</div>
</body>
</html>