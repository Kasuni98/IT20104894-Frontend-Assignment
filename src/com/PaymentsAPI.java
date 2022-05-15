package com;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet implementation class PaymentsAPI
 */
@WebServlet("/PaymentsAPI")
@MultipartConfig
public class PaymentsAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	Payment payment = new Payment();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PaymentsAPI() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NumberFormatException {
		String cardHolderName = request.getParameter("cardHolderName");
		String cardNumber = request.getParameter("cardNumber");
		String cvv = request.getParameter("cvv");
		String expirationDate = request.getParameter("expirationDate");
		
//		int year = Integer.parseInt(request.getParameter("year"));
//		Part part = request.getPart("uploadFile");

		String output = payment.insertPayment(cardHolderName,cardNumber,cvv,expirationDate);
		response.getWriter().write(output);
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int paymentId = Integer.parseInt(request.getParameter("hidPaymentIdSave"));
		String cardHolderName = request.getParameter("cardHolderName");
		String cardNumber = request.getParameter("cardNumber");
		String cvv = request.getParameter("cvv");
		String expirationDate = request.getParameter("expirationDate");
		

		String output = payment.updatePayment(paymentId, cardHolderName,cardNumber,cvv,expirationDate);
		response.getWriter().write(output);
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map paras = getParasMap(request);
		int paymentId = Integer.parseInt(paras.get("paymentID").toString());
		
	String output = payment.deletePayment(paymentId);
		response.getWriter().write(output);
}
	
	// Convert request parameters to a Map
		private static Map getParasMap(HttpServletRequest request)
		{
			Map<String, String> map = new HashMap<String, String>();
			try
			{
				 Scanner scanner = new Scanner(request.getInputStream(), "UTF-8");
				 String queryString = scanner.hasNext() ?
				 scanner.useDelimiter("\\A").next() : "";
				 scanner.close();
				 String[] params = queryString.split("&");
				 for (String param : params)
				 { 
					 String[] p = param.split("=");
					 map.put(p[0], p[1]);
				}
			}catch (Exception e){
				
			}
				return map;
		}

}
