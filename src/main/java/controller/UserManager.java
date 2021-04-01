package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
        
        // Send html response
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        if (session.getAttribute("login") == null || !"Admin".equals(session.getAttribute("role"))) {
            try (PrintWriter out = response.getWriter()) {
                // TODO: Improve and use jsp
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<meta http-equiv='refresh' content='5; URL=connexion.html' />");
                out.println("<title> Non autorisé</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Vous n'êtes pas connecté ou vous n'êtes pas admin</h1>");
                out.println("<span>Vous allez être redirigé vers la page connexion</span>");
                out.println("</body>");
                out.println("</html>");
            }

        } else {

            // Get values from the form
            String familyName = request.getParameter("User first name");
            String lastName = request.getParameter("User familly name");
            String mail = request.getParameter("User email");
            String gender = request.getParameter("gender");
            String password = request.getParameter("User password");

            // Create the user
            User user = new User(lastName, familyName, mail, password, gender);

            if (request.getParameter("role") != null) {
                user.setRole(request.getParameter("role"));
            }

            // Save the user
            try {
                user.save();
			} catch (Exception e) {
				e.printStackTrace();
			}

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
                out.println("<li>Un nouveau utilisateur est ajouté</li>");
                out.println(user.toString());
                out.println("<li><a href='newUser.html'>Créer un nouveau utilisateur</a></li>");
                out.println(" <li><a href='userManager'>Afficher la liste des utilisateurs</a></li>");
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

        // Get user's session
        HttpSession session = request.getSession();

        // Send html response
        response.setContentType("text/html;charset=UTF-8");

        if (session.getAttribute("login") == null || "Admin".equals(session.getAttribute("role"))==false) {
            try (PrintWriter out = response.getWriter()) {
                // TODO: Improve and use jsp
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<meta http-equiv='refresh' content='5; URL=connexion.html' />");
                out.println("<title> Non autorisé</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Vous n'êtes pas connecté ou vous n'êtes pas admin</h1>");
                out.println("<span>Vous allez être redirigé vers la page connexion</span>");
                out.println("</body>");
                out.println("</html>");
            }
        } else {
            try (PrintWriter out = response.getWriter()) {
                // TODO: Improve and use jsp
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Un nouvel utilisateur</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Liste des utilisateurs:</h1>");
                out.println("<ul>");
                
                try {
                	List<User> users = User.findAll();

                    // Iterate over all the users
                    Iterator<User> usersIterator = users.iterator();
                    while(usersIterator.hasNext()) {
                        out.println("<li>");
                        out.println(usersIterator.next().toString());
                        out.println("</li>"); 
                    }
                } catch (Exception e) {
					e.printStackTrace();
				}
                

                out.println("</ul>");
                out.println("</body>");
                out.println("</html>");
            }
        }
   }
   
   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
       try {
			doRequest(request, response);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   }
}
