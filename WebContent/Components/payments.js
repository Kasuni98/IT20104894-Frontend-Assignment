$(document).ready(function()
{
	$("#alertSuccess").hide();
	$("#alertError").hide();
});

//SAVE ============================================
$(document).on("click", "#btnSave", function(event)
{
	// Clear alerts---------------------
	 $("#alertSuccess").text("");
	 $("#alertSuccess").hide();
	 $("#alertError").text("");
	 $("#alertError").hide(); 
	 
	// Form validation-------------------
	 var status = validatePaymentForm();
	 if (status != true)
		 {
		  $("#alertError").text(status);
		  $("#alertError").show();
		  return;
	 }
	 //If status equals to true
	 var type = ($("#hidPaymentIdSave").val() == "") ? "POST" : "PUT";
	 var formData = new FormData($("#formPayments")[0]);
	 console.log(formData);
	 $.ajax(
	 {
		 url : "PaymentsAPI",
		 type : type,
		 data : formData,
		 enctype : "multipart/form-data",
		 complete : function(response, status)
		 {
			 onPaymentSaveComplete(response.responseText, status);
		 },
		 processData : false,
		 contentType :false
	 }); 
});

function onPaymentSaveComplete(response, status)
{
	if (status == "success")
	 {
		var resultSet = JSON.parse(response);
		if (resultSet.status.trim() == "success")
		{
			$("#alertSuccess").text("Successfully saved.");
			$("#alertSuccess").show();
			$("#divPaymentsGrid").html(resultSet.data);
		} else if (resultSet.status.trim() == "error")
		{
			 $("#alertError").text(resultSet.data);
			 $("#alertError").show();
		}
	 } else if (status == "error")
	 {
			 $("#alertError").text("Error while saving.");
			 $("#alertError").show();
	 } else
	 {
			 $("#alertError").text("Unknown error while saving..");
			 $("#alertError").show();
	 } 
	
	 $("#hidPaymentIdSave").val("");
	 $("#formPayments")[0].reset();
}

//UPDATE==========================================
$(document).on("click", ".btnUpdate", function(event)
{
	 $("#hidPaymentIdSave").val($(this).data("paymentid")); 
	 $("#cardHolderName").val($(this).closest("tr").find('td:eq(0)').text());
	 $("#cardNumber").val($(this).closest("tr").find('td:eq(1)').text());
	 $("#cvv").val($(this).closest("tr").find("td:eq(2)").text());
	 $("#expirationDate").val($(this).closest("tr").find('td:eq(3)').text());
	 
});

//client-model
function validatePaymentForm()
{
	// Card Holder Name
	if ($("#cardHolderName").val().trim() == "")
	{
		return "Insert Card Holder Name.";
	}
	// Card Number
	if ($("#cardNumber").val().trim() == "")
	{
		return "Card Number.";
	}
	// CVV
	if ($("#cvv").val().trim() == "")
	{
		return "CVV.";
	}
	// Expiration Date
	if ($("#expirationDate").val().trim() == "")
	{
		return "Expiration Date.";
	}
	
	
	
	
	return true;
}

$(document).on("click", ".btnRemove", function(event)
{
		$.ajax(
		 {
			 url : "PaymentsAPI",
			 type : "DELETE",
			 data : "paymentID=" + $(this).data("paymentid"),
			 dataType : "text",
			 complete : function(response, status)
			 {
				 onPaymentDeleteComplete(response.responseText, status);
			 }
		 });
});


function onPaymentDeleteComplete(response, status)
{
	if (status == "success")
	 {
		var resultSet = JSON.parse(response);
		if (resultSet.status.trim() == "success")
		{
			 $("#alertSuccess").text("Successfully deleted.");
			 $("#alertSuccess").show();
			 $("#divPaymentGrid").html(resultSet.data);
		} else if (resultSet.status.trim() == "error")
		{
			 $("#alertError").text(resultSet.data);
			 $("#alertError").show();
		}
	 } else if (status == "error")
	 {
			 $("#alertError").text("Error while deleting.");
			 $("#alertError").show();
	 } else
	 {
			 $("#alertError").text("Unknown error while deleting..");
			 $("#alertError").show();
	 }
}


