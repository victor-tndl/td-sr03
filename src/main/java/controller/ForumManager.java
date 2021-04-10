package controller;

import java.io.IOException;
import java.io.PrintWriter;
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
import model.Token;
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
        // Send html response
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        if (session.getAttribute("token") == null) {
            try (PrintWriter out = response.getWriter()) {
                // TODO: Improve and use jsp
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<meta http-equiv='refresh' content='5; URL=connexion.jsp' />");
                out.println("<title> Non autorisé</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Vous n'êtes pas connecté</h1>");
                out.println("<span>Vous allez être redirigé vers la page connexion</span>");
                out.println("</body>");
                out.println("</html>");
            }
        } else {
            // Get values from the form
			String title = request.getParameter("title");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			Date begin_date = df.parse(request.getParameter("begin_date")+":00Z");
			Date end_date = df.parse(request.getParameter("end_date")+":00Z");

            // Create the forum
            Token token = (Token) session.getAttribute("token");
            User owner = User.findByLogin(token.getLogin());
            DateFormat mysqlDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
            Forum forum = new Forum(title, mysqlDateFormat.format(begin_date), mysqlDateFormat.format(end_date), owner);

            // Try save the forum
            forum.save();

            // Send html response
            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                // TODO: Improve and use jsp
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Nouvel utilisateur</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<li>Un nouveau forum est ajouté</li>");
                out.println(forum.toString());
                out.println("<li><a href='newUser.jsp'>Créer un nouveau utilisateur</a></li>");
                out.println("<li><a href='newForum.jsp'>Créer un nouveau forum</a></li>");
                out.println(" <li><a href='userManager'>Afficher la liste des utilisateurs</a></li>");
                out.println(" <li><a href='forumManager'>Afficher la liste des forums</a></li>");
                out.println(" <li><a href='deconnexionController'>Déconnecter</a></li>");
                out.println("</body>");
                out.println("</html>");
            }
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
            RequestDispatcher rd = request.getRequestDispatcher("toConnxion.jsp");
			rd.forward(request, response);
        } else {
            // Get user's session and token
            HttpSession session = request.getSession();
            Token token = (Token) session.getAttribute("token");

            List<Forum> allForums = null;
            try {
                if (token.getIsAdmin() == true) {
                    // The user is an admin
                    allForums = Forum.findAll();
                } else {
                    allForums = Forum.findByUser(User.findByLogin(token.getLogin()));
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
            RequestDispatcher rd = request.getRequestDispatcher("toConnxion.jsp");
			rd.forward(request, response);
        } else if ( "Admin".equals(session.getAttribute("role")) == false) {
            // TODO: Add delete jsp
        } else {
            String forumId = request.getParameter("forum_id");
            if (forumId != null) {
                try {
                    Forum forumToDelete = Forum.findById(Integer.parseInt(forumId, 10));
                    if (forumToDelete != null) {
                        forumToDelete.delete();
                    }
                    RequestDispatcher rd = request.getRequestDispatcher("dashboard.jsp");
			        rd.forward(request, response);
                } catch (NumberFormatException | ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    } 
}