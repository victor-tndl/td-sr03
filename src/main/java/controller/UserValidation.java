package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;

@WebServlet(name = "UserValidation", urlPatterns = {"/users_validation"})
public class UserValidation extends HttpServlet{

	private static final long serialVersionUID = 1L;

	/**
     * @return
	 * @see HttpServlet#HttpServlet()
     */
   public UserValidation() {
	   super();
   }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		boolean isUserValid = true;

        // Get values from the form
        String firstName = request.getParameter("User first name");
        String familyName = request.getParameter("User familly name");
        String login = request.getParameter("User email");
        String password = request.getParameter("User password");
        String gender = request.getParameter("gender");

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

        if (request.getParameter("validator") != null) {
            // des doublons ont été détectés et l'utilisateur à valider son choix
            if ("oui".equals(request.getParameter("valider"))) {
                // on insère les doublons
            	isUserValid = true;
            } else {
            	isUserValid = false;
                RequestDispatcher rd = request.getRequestDispatcher("create_user.html");
                // Abort insertion
                rd.forward(request, response);
            }

        } else {
            // TODO: Use this code
            // User user = User.findByFamilyNameAndFirstName(familyName, firstName);
            // if (user != false) {

            if (UserManager.getUsersTable().containsValue(new User(familyName, firstName))) {
                isUserValid = false;
                try (PrintWriter out = response.getWriter()) {
                    // TODO: Improve and use jsp
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
        }
        
        if (isUserValid) {
            RequestDispatcher rd = request.getRequestDispatcher("UserManager");
            rd.forward(request, response);
        }
	}
}
