/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IOTB.controller;

import IOTB.model.beans.*;
import IOTB.model.dao.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Alexis
 */
public class PaymentManager extends HttpServlet {

    // _____________________________________________
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            HttpSession session = request.getSession();
            DBManager manager = (DBManager) session.getAttribute("manager");
            String customerID = request.getParameter("custId");
//                Customer editCus = (Customer) manager.findCustomerID(customerID);
//                session.setAttribute("editCus", editCus);
            System.out.println(customerID);
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet PaymentMan2ager</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<a href=\"PaymentDetails.jsp\">Details</a>");
            out.println("<a href=\"paymentscentral.jsp\">THIS LINK SHOWS ALL YOUR PAYMENT CARDS</a>");
            Payment payment = (Payment) request.getAttribute("payment");
            Customer customer = (Customer) session.getAttribute("customer");

//            try {
//                manager.deleteCustomer(customer.getCustomerId());
//            } catch (Exception e) {
//                e.printStackTrace();
            if (payment != null) {
                out.println("<p>Payment Card Number: " + payment.getPaymentCardNumber() + "</p>");
                out.println("<p>CustomerID: " + session.getAttribute("customerID") + "</p>");
                out.println("<p>Payment PaymentID: " + payment.getPaymentID() + "</p>");
                out.println("<p>Payment PaymentID: " + customer.getCustomerId() + "</p>");
                // }
                out.println("<h1>Servlet PaymentManassger at " + request.getContextPath() + "</h1>");
                out.println("</body>");
                out.println("</html>");
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        String paymentID = request.getParameter("paymentID");
//        String paymentCardName = request.getParameter("cardName");
//        long paymentCardNumber = Long.parseLong(request.getParameter("cardNum"));
//        int paymentCardCVC = Integer.parseInt(request.getParameter("CVC"));
//        LocalDate paymentCardExpiryDate = LocalDate.parse(request.getParameter("Expiry"));
//        Payment payment = new Payment(paymentID, paymentCardName, paymentCardNumber, paymentCardCVC, paymentCardExpiryDate);
//        request.setAttribute("payment", payment);
//        processRequest(request, response);
//    }
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Validator validator = new Validator();
        DBManager manager = (DBManager) session.getAttribute("manager");
        CardValidators cardvalid = new CardValidators();
        
        String paymentID = request.getParameter("paymentID");
        String paymentCardName = request.getParameter("cardName");
        long paymentCardNumber = Long.parseLong(request.getParameter("cardNum"));
        int paymentCardCVC = Integer.parseInt(request.getParameter("CVC"));
        LocalDate paymentCardExpiryDate = LocalDate.parse(request.getParameter("Expiry"));
        Customer customer = (Customer) session.getAttribute("customer");
        
        if (!(cardvalid.cvcValidator(String.valueOf(paymentCardCVC)))) {
            request.setAttribute("cvvError", "Invalid CVV. Please enter a 3-digit CVV.");
            RequestDispatcher rd = request.getRequestDispatcher("/PaymentDetails.jsp");
            rd.forward(request, response);
            return;
        }
        if (!(cardvalid.cardNumValidator(String.valueOf(paymentCardNumber)))) {
            request.setAttribute("cardNumError", "Invalid Card Number. Make sure you number is at least 16 digits.");
            RequestDispatcher rd = request.getRequestDispatcher("/PaymentDetails.jsp");
            rd.forward(request, response);
            return;
        }
            if(paymentCardExpiryDate == null){
                System.out.println("hehe");
            } else {
                System.out.println("haha");
            }
         if (paymentCardExpiryDate == null) {
            request.setAttribute("expDateError", "Invalid EXP. Please enter a valid Expiry Date.");
            RequestDispatcher rd = request.getRequestDispatcher("/PaymentDetails.jsp");
            rd.forward(request, response);
            return;
        }
       
        
        
        String customerID = customer.getCustomerId();
        Payment payment = new Payment(paymentID, paymentCardName, paymentCardNumber, paymentCardCVC, paymentCardExpiryDate);

        processRequest(request, response);
        try {
            manager.addPayment(paymentID, paymentCardName, paymentCardNumber, paymentCardCVC, paymentCardExpiryDate, customerID);
        } catch (SQLException e) {
            // Handle the exception here
            System.out.println("An error occurred while adding the payment: " + e.getMessage());
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
