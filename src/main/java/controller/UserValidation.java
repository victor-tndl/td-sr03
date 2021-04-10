package controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;

@WebServlet(name="userValidation",urlPatterns={"/userValidation"})
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
        String password = User.hashPassword(request.getParameter("User password"));
        String gender = request.getParameter("gender");

        if (firstName == null || familyName == null || login == null || password == null) {
            System.out.println("Champs non renseignés");
            RequestDispatcher rd = request.getRequestDispatcher("newUser.jsp");
            rd.forward(request, response);
            isUserValid = false;
        } else if ("".equals(firstName) || "".equals(familyName) || "".equals(login) || "".equals(password)) {
            System.out.println("Champs vides");
            RequestDispatcher rd = request.getRequestDispatcher("newUser.jsp");
            rd.forward(request, response);
            isUserValid = false;
        } else {
            User user = null;
            try {
                user = User.findByLogin(login);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
            if (user != null) {
                System.out.println("email déjà utilisé");
                RequestDispatcher rd = request.getRequestDispatcher("newUser.jsp");
                rd.forward(request, response);
                isUserValid = false;
            }
        }

        if (request.getParameter("validator") != null) {
            // des doublons ont été détectés et l'utilisateur à valider son choix
            if ("oui".equals(request.getParameter("valider"))) {
                // on insère les doublons
            	isUserValid = true;
            } else {
            	isUserValid = false;
                RequestDispatcher rd = request.getRequestDispatcher("newUser.jsp");
                // Abort insertion
                rd.forward(request, response);
            }
        } else {
            User user = null;
            try {
                user = User.findByFamilyNameAndFirstName(familyName, firstName);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
            if (user != null) {
                isUserValid = false;
                RequestDispatcher rd = request.getRequestDispatcher("newUserDuplicate.jsp");
                rd.forward(request, response);
            }
        }
        
        if (isUserValid) {
            RequestDispatcher rd = request.getRequestDispatcher("userManager");
            rd.forward(request, response);
        }
	}
}
