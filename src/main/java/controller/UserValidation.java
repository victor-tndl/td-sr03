package controller;

import javax.servlet.http.*;

import model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;

import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;

@WebServlet(name = "UserValidation", urlPatterns = {"/users_validation"})
public class UserValidation extends HttpServlet{

	private static final long serialVersionUID = 1L;

	/**
     * @return 
	 * @see HttpServlet#HttpServlet()
     */
   public UserValidation() {
	   super();
	   // TODO Auto-generated constructor stub
   }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		boolean isUserValid = true;

        String firstName = request.getParameter("User first name");
        String familyName = request.getParameter("User familly name");
        String login = request.getParameter("User email");
        String gender = request.getParameter("gender");
        String password = request.getParameter("User password");

        if (firstName == null || familyName == null || login == null || password == null) {
            System.out.println("Champs non renseignés");
            RequestDispatcher rd = request.getRequestDispatcher("create_user.html");
            rd.forward(request, response);
            isUserValid = false;
        } else if ("".equals(firstName) || "".equals(familyName) || "".equals(login) || "".equals(password)) {
            System.out.println("Champs vides");
            RequestDispatcher rd = request.getRequestDispatcher("create_user.html");
            rd.forward(request, response);
            isUserValid = false;
        }

        if (request.getParameter("validator") != null) {// des doublons ont été détectés et l'utilisateur à valider son choix
            if ("oui".equals(request.getParameter("valider"))) {// on insère les doublons
            	isUserValid = true;
            } else {
            	isUserValid = false;
                RequestDispatcher rd = request.getRequestDispatcher("create_user.html");//abandonner l'insertion
                rd.forward(request, response);
            }

        } else if (UserManager.getUsersTable().containsValue(new User(familyName, firstName))) {
        	isUserValid = false;
            try (PrintWriter out = response.getWriter()) {
                /* TODO output your page here. You may use following sample code. */
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet UserValidation</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Un utilisateur avec les mêmes nom et prénom existe déjà. Voulez-vous l'enregistrer ?  </h1>");
                out.println("<form method='POST' action='UserValidation'>");
                out.println("Oui <input type='radio' name='valider' value='oui' /> ");
                out.println("Nom <input type='radio' name='valider' value='nom' />");
                out.println("<input type='hidden' name='User first name' value='" + firstName + "'/>");
                out.println("<input type='hidden' name='User familly name' value='" + familyName + "'/>");
                out.println("<input type='hidden' name='User email' value='" + login + "'/>");
                out.println("<input type='hidden' name='gender' value='" + gender + "'/>");
                out.println("<input type='hidden' name='User password' value='" + password + "' />");
                out.println("<br>");
                out.println("<input type ='submit' value='Envoyer' name='validator' />");
                out.println("</form>");
                out.println("</body>");
                out.println("</html>");
            }
        }
        
        if (isUserValid) {
            RequestDispatcher rd = request.getRequestDispatcher("UserManager");
            rd.forward(request, response);
        }

	}
}
