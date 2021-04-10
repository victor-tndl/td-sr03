package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.SessionToken;
import model.User;


@WebServlet(name="connexionController",urlPatterns={"/connexionController"})
public class ConnexionController extends HttpServlet{

	private static final long serialVersionUID = 1L;

	/**
     * Processes requests for both HTTP GET && POST methods.
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

        // Check if the user exist
        List<User> users = User.findAll();

        // Iterate over all the users
        User searchedUser = null;

        for (ListIterator<User> usersIterator = users.listIterator(); usersIterator.hasNext();) {
        	User user = usersIterator.next();
            if (user.getLogin().equals(request.getParameter("username"))
            		&& user.getPassword().equals(User.hashPassword(request.getParameter("password")))) {
            	searchedUser = user;
                break;
            } 
        }

        if (searchedUser == null) {
            request.setAttribute("error", "Wrong username or password");
            RequestDispatcher rd = request.getRequestDispatcher("connexionError.jsp");
            rd.forward(request, response);
        } else if (!searchedUser.getPassword().equals(User.hashPassword(request.getParameter("password")))) {
            request.setAttribute("error", "Wrong username or password");
            RequestDispatcher rd = request.getRequestDispatcher("connexionError.jsp");
            rd.forward(request, response);
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("sessionToken", new SessionToken(searchedUser));
            RequestDispatcher rd = request.getRequestDispatcher("dashboard.jsp");
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
        try {
            doRequest(request, response);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isConnected(HttpServletRequest request) {
        // Get user's session and token
        HttpSession session = request.getSession();
        SessionToken token = (SessionToken) session.getAttribute("sessionToken");
        if (token.getUserLogin() == null || "".equals(token.getUserLogin()) == true) {
            return false;
        }
        return true;
    }
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

    @Override
    public void init() throws ServletException {
        super.init();
        User user = new User("admin","admin", "admin@admin", User.hashPassword("admin"), "Male");
        try {
            user.save();
        } catch (ClassNotFoundException | SQLException | IOException e) {
            System.out.println("User already exist");
        }
    }
}
