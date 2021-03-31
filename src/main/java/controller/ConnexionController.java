package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;


@WebServlet(name = "ConnexionController", urlPatterns = {"/connect"})
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
        Set<Integer> keys = UserManager.getUsersTable().keySet();

        //Obtaining iterator over set entries
        Iterator<Integer> itr = keys.iterator();
        int searchedUser = -1;
        while (itr.hasNext() && searchedUser < 0) {
            int index = (int) itr.next();
            if (UserManager.getUsersTable().get(index).getLogin().equals(request.getParameter("username"))
            		&& UserManager.getUsersTable().get(index).getPassword().equals(request.getParameter("password"))) {
            	searchedUser = index;
                break;
            }
        }
        if (searchedUser < 0) {
            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                // TODO: Improve and use jsp
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Connection</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<div clas='erreurConnexion'>");
                out.println("<h1>Erreur lors de la connexion</h1>");
                out.println("<span>Mauvais identifiants</span>");
                out.println("</div>");
                out.println("</body>");
                out.println("</html>");
            }
        } else if (!UserManager.getUsersTable().get(searchedUser).getPassword().equals(request.getParameter("password"))) {
            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                // TODO: Improve and use jsp
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet Connexion</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Echec :mot de passe érroné </h1>");
                out.println("</body>");
                out.println("</html>");

            }
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("login", UserManager.getUsersTable().get(searchedUser).getLogin());
            String role = UserManager.getUsersTable().get(searchedUser).getRole();
            session.setAttribute("role", role);
            response.setContentType("text/html;charset=UTF-8");
            if ("Admin".equals(role)) {
                try (PrintWriter out = response.getWriter()) {
                    out.println("<!DOCTYPE html>");
                    out.println("<html><head><title>Navigation Administrateur</title></head>");
                    out.println("<body>");
                    out.println("<h1>Hello " + session.getAttribute("login") + "</h1>");
                    out.println("<nav> <ul>");
                    out.println("<li>Connected</li>");
                    out.println("<li><a href='create_user.html'>Ajouter un nouvel utilisateur</a></li>");
                    out.println("<li><a href='UserManager'>Afficher la liste des utilisateurs</a></li>");
                    out.println("<li><a href='Deconnexion'>Déconnecter</a></li>");
                    out.println("</ul>");
                    out.println("</nav>");
                    out.println("</body>");
                    out.println("</html>");
                }
            } else {
                try (PrintWriter out = response.getWriter()) {
                    // TODO: Improve and use jsp
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>Servlet Connexion</title>");
                    out.println("</head>");
                    out.println("<body>");
                    out.println("<h1>Succes : utilisateur non admin </h1>");
                    out.println("</body>");
                    out.println("</html>");
                }
            }

        }

        // TODO: USe this code when the driver SQL is ready

        // List<User> users = User.findAll();

        // // Iterate over all the users
        // Iterator<User> usersIterator = users.iterator();
        // User searchedUser = null;
        // while(usersIterator.hasNext() && searchedUser != null) {
        //     User user = usersIterator.next();
        //     if (user.getLogin().equals(request.getParameter("username"))
        //     		&& user.getPassword().equals(request.getParameter("password"))) {
        //     	searchedUser = user;
        //         break;
        //     } 
        // }

        // if (searchedUser == null) {
        //     response.setContentType("text/html;charset=UTF-8");
        //     try (PrintWriter out = response.getWriter()) {
        //         // TODO: Improve and use jsp
        //         out.println("<!DOCTYPE html>");
        //         out.println("<html>");
        //         out.println("<head>");
        //         out.println("<title>Connection</title>");
        //         out.println("</head>");
        //         out.println("<body>");
        //         out.println("<div clas='erreurConnexion'>");
        //         out.println("<h1>Erreur lors de la connexion</h1>");
        //         out.println("<span>Mauvais identifiants</span>");
        //         out.println("</div>");
        //         out.println("</body>");
        //         out.println("</html>");
        //     }
        // } else if (!searchedUser.getPassword().equals(request.getParameter("password"))) {
        //     response.setContentType("text/html;charset=UTF-8");
        //     try (PrintWriter out = response.getWriter()) {
        //         // TODO: Improve and use jsp
        //         out.println("<!DOCTYPE html>");
        //         out.println("<html>");
        //         out.println("<head>");
        //         out.println("<title>Servlet Connexion</title>");
        //         out.println("</head>");
        //         out.println("<body>");
        //         out.println("<h1>Echec :mot de passe érroné </h1>");
        //         out.println("</body>");
        //         out.println("</html>");

        //     }
        // } else {
        //     HttpSession session = request.getSession();
        //     session.setAttribute("login", searchedUser.getLogin());
        //     String role = searchedUser.getRole();
        //     session.setAttribute("role", role);
        //     response.setContentType("text/html;charset=UTF-8");
        //     if ("Admin".equals(role)) {
        //         try (PrintWriter out = response.getWriter()) {
        //             out.println("<!DOCTYPE html>");
        //             out.println("<html><head><title>Navigation Administrateur</title></head>");
        //             out.println("<body>");
        //             out.println("<h1>Hello " + session.getAttribute("login") + "</h1>");
        //             out.println("<nav> <ul>");
        //             out.println("<li>Connected</li>");
        //             out.println("<li><a href='create_user.html'>Ajouter un nouvel utilisateur</a></li>");
        //             out.println("<li><a href='UserManager'>Afficher la liste des utilisateurs</a></li>");
        //             out.println("<li><a href='Deconnexion'>Déconnecter</a></li>");
        //             out.println("</ul>");
        //             out.println("</nav>");
        //             out.println("</body>");
        //             out.println("</html>");
        //         }
        //     } else {
        //         try (PrintWriter out = response.getWriter()) {
        //             // TODO: Improve and use jsp
        //             out.println("<!DOCTYPE html>");
        //             out.println("<html>");
        //             out.println("<head>");
        //             out.println("<title>Servlet Connexion</title>");
        //             out.println("</head>");
        //             out.println("<body>");
        //             out.println("<h1>Succes: utilisateur non admin </h1>");
        //             out.println("</body>");
        //             out.println("</html>");
        //         }
        //     }
        // }
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
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            doRequest(request, response);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

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
        try {
            doRequest(request, response);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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

    // TODO: Remove when driver SQL is ready
    @Override
    public void init() throws ServletException {
        super.init(); //To change body of generated methods, choose Tools | Templates.
        //Créer le premier utilisateur de l'application pour pouvoir connecter et ajouter d'autres utilisateurs
        int id = UserManager.getUsersTable().size();
        User user = new User("admin", "admin", "admin@admin", "admin", "Male");
        user.setRole("Admin");

        UserManager.getUsersTable().put(id, user);
        try {
			user.save();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
