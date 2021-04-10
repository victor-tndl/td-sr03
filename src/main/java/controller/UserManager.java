package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Token;
import model.User;

@WebServlet(name ="userManager",urlPatterns={"/userManager"})
public class UserManager extends HttpServlet{

	private static final long serialVersionUID = 1L;

	/**
     * @return 
	 * @see HttpServlet#HttpServlet()
     */
   public UserManager() {
	   super();
   }

   /**
    * Processes requests for both HTTP POST methods.
    *
    * @param request servlet request
    * @param response servlet response
    * @throws ServletException if a servlet-specific error occurs
    * @throws IOException if an I/O error occurs
    * @throws SQLException 
    * @throws ClassNotFoundException 
    */
   protected void doRequest(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException, ClassNotFoundException, SQLException {
        
        if (ConnexionController.isConnected(request) == false) {
            RequestDispatcher rd = request.getRequestDispatcher("toConnexion.jsp");
			rd.forward(request, response);
        } else {
            // Get values from the form
            String firstName = request.getParameter("firstName");
            String familyName = request.getParameter("familyName");
            String login = request.getParameter("login");
            String password = User.hashPassword(request.getParameter("password"));
            String gender = request.getParameter("gender");

            // Create the user
            User user = new User(firstName, familyName, login, password, gender);

            if (request.getParameter("role") != null) {
                user.setRole(request.getParameter("role"));
            }

            // Save the user
            user.save();

            // Redirect the user
            RequestDispatcher rd = request.getRequestDispatcher("newUserSuccess.jsp");
            rd.forward(request, response);
        }
   }

   /**
    * Handles the HTTP GET method.
    *
    * @param request servlet request
    * @param response servlet response
    * @throws ServletException if a servlet-specific error occurs
    * @throws IOException if an I/O error occurs
    */
   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {

        if (ConnexionController.isConnected(request) == false) {
            RequestDispatcher rd = request.getRequestDispatcher("toConnexion.jsp");
			rd.forward(request, response);
        } else {
            // Get user's session and token
            HttpSession session = request.getSession();
            Token token = (Token) session.getAttribute("token");
            List<User> allUsers = null;
            try {
                if (token.getIsAdmin() == true) {
                    // The user is an admin
                    allUsers = User.findAll();
                } else {
                    // TODO: add jsp page
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }

            // Redirect the user to the wanted page with allUsers in parameter
            request.setAttribute("allUsers", allUsers);
            RequestDispatcher rd = request.getRequestDispatcher("users.jsp");
            rd.forward(request, response);
        }
   }

    /**
     * Handles the HTTP POST method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
       try {
			doRequest(request, response);
		} catch (ClassNotFoundException | ServletException | IOException | SQLException e) {
			e.printStackTrace();
		}
   }

    /**
     * Handles the HTTP DELETE method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Token token = (Token) session.getAttribute("token");
        if (token.getLogin() == null || !token.getIsAdmin()) {
            RequestDispatcher rd = request.getRequestDispatcher("toConnexion.jsp");
			rd.forward(request, response);
        } else {
            String userId = request.getParameter("user_id");
            if (userId != null) {
                try {
                    User userToDelete = User.findById(Integer.parseInt(userId, 10));
                    if (userToDelete != null) {
                        userToDelete.delete();
                    }
                } catch (NumberFormatException | ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
