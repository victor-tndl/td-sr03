package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Forum;
import model.SessionToken;
import model.User;

@WebServlet(name="forumManager",urlPatterns={"/forumManager"})
public class ForumManager extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Processes requests for both HTTP POST methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @throws ParseException
     */
     protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ClassNotFoundException, SQLException, ParseException {

        HttpSession session = request.getSession();
        if (ConnexionController.isConnected(request) == false) {
            RequestDispatcher rd = request.getRequestDispatcher("toConnexion.jsp");
			rd.forward(request, response);
        } else {
            // Get values from the form
			String title = request.getParameter("title");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			Date beginDate = df.parse(request.getParameter("beginDate")+":00Z");
			Date endDate = df.parse(request.getParameter("endDate")+":00Z");

            // Create the forum
            SessionToken token = (SessionToken) session.getAttribute("sessionToken");
            User owner = User.findByLogin(token.getUserLogin());
            DateFormat mysqlDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
            Forum forum = new Forum(title, mysqlDateFormat.format(beginDate), mysqlDateFormat.format(endDate), owner);

            // Try save the forum
            forum.save();

            // Redirect user 
            RequestDispatcher rd = request.getRequestDispatcher("newForumSuccess.jsp");
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
            SessionToken token = (SessionToken) session.getAttribute("sessionToken");

            List<Forum> allForums = null;
            try {
                if (token.getUserIsAdmin() == true) {
                    // The user is an admin
                    allForums = Forum.findAll();
                } else {
                    allForums = Forum.findByUser(User.findByLogin(token.getUserLogin()));
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }

            // Redirect the user to the wanted page with allForums in parameter
            request.setAttribute("allForums", allForums);
            RequestDispatcher rd = request.getRequestDispatcher("forums.jsp");
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
        } catch (ServletException | ParseException | ClassNotFoundException | SQLException e) {
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
        if (ConnexionController.isConnected(request) == false) {
            response.sendError(401, "Not connected");
            return;
        } else {
            String forumId = request.getParameter("forum_id");
            if (forumId != null) {
                Forum forumToDelete = null;
                try {
                    forumToDelete = Forum.findById(Integer.parseInt(forumId, 10));
                } catch (NumberFormatException | ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
                if (forumToDelete != null) {
                    SessionToken token = (SessionToken) session.getAttribute("sessionToken");
                    if (token.getUserIsAdmin()) {
                        try {
                            forumToDelete.delete();
                            return;
                        } catch (NumberFormatException | ClassNotFoundException | SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (forumToDelete.getOwner().getId() == token.getUserId() ) {
                            try {
                                forumToDelete.delete();
                                return;
                            } catch (NumberFormatException | ClassNotFoundException | SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    response.sendError(403, "Unhautorized");
                    return;
                }
                response.sendError(404, "Entity not found");
                return;
            }
        }
        response.sendError(404, "An error occured");
        return;
    } 
}