package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;

@WebServlet(name = "UserManager", urlPatterns = {"/UserManager"})
public class UserManager extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static Hashtable<Integer,User> usersTable = new Hashtable<Integer, User>();

	/**
     * @return 
	 * @see HttpServlet#HttpServlet()
     */
   public UserManager() {
	   super();
	   // TODO Auto-generated constructor stub
   }

   public static Hashtable<Integer,User> getUsersTable() {
	   return usersTable;
   }
   /**
    * Processes requests for both HTTP  <code>POST</code> methods.
    *
    * @param request servlet request
    * @param response servlet response
    * @throws ServletException if a servlet-specific error occurs
    * @throws IOException if an I/O error occurs
 * @throws SQLException 
 * @throws ClassNotFoundException 
    */
   protected void processRequest(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException, ClassNotFoundException, SQLException {
       response.setContentType("text/html;charset=UTF-8");
       HttpSession session = request.getSession();
       if (session.getAttribute("login") == null || !"admin".equals(session.getAttribute("role"))) {
           try (PrintWriter out = response.getWriter()) {
               /* TODO output your page here. You may use following sample code. */
               out.println("<!DOCTYPE html>");
               out.println("<html>");
               out.println("<head>");
               out.println("<meta http-equiv='refresh' content='5; URL=connexion.html' />");
               out.println("<title> Non autorisé</title>");
               out.println("</head>");
               out.println("<body>");
               out.println("<h1>Vous n'êtes pas connecté ou vous n'êtes pas admin => redirigé vers la page connexion </h1>");
               out.println("</body>");
               out.println("</html>");
           }

       } else {
           String familyName = request.getParameter("User first name");
           String lastName = request.getParameter("User familly name");
           String mail = request.getParameter("User email");
           String gender = request.getParameter("gender");
           String password = request.getParameter("User password");
           User user = new User(lastName, familyName, mail, gender, password);
           if (request.getParameter("role") != null) {
               user.setRole(request.getParameter("role"));
           }
           usersTable.put(usersTable.size(), user);
           // user.save();
           response.setContentType("text/html;charset=UTF-8");
           try (PrintWriter out = response.getWriter()) {
               /* TODO output your page here. You may use following sample code. */
               out.println("<!DOCTYPE html>");
               out.println("<html>");
               out.println("<head>");
               out.println("<title>Nouvel utilisateur</title>");
               out.println("</head>");
               out.println("<body>");
               out.println("<li>Un nouveau utilisateur est ajouté</li>");
               out.println(usersTable.get(usersTable.size() - 1).toString());
               out.println("<li><a href='create_user.html'>Ajouter un nouvel utilisateur</a></li>");
               out.println("<li><a href='UserManager'>Afficher la liste des utilisateurs</a></li>");
               out.println("<li><a href='Deconnexion'>Déconnecter</a></li>");
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
   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
       response.setContentType("text/html;charset=UTF-8");
       response.setContentType("text/html;charset=UTF-8");
       HttpSession session = request.getSession();
       if (session.getAttribute("login") == null || "admin".equals(session.getAttribute("role"))==false) {
           try (PrintWriter out = response.getWriter()) {
               /* TODO output your page here. You may use following sample code. */
               out.println("<!DOCTYPE html>");
               out.println("<html>");
               out.println("<head>");
               out.println("<meta http-equiv='refresh' content='5; URL=connexion.html' />");
               out.println("<title> Non autorisé</title>");
               out.println("</head>");
               out.println("<body>");
               out.println("<h1>Vous n'êtes pas connecté ou vous n'êtes pas admin => redirigé vers la page connexion </h1>");
               out.println("</body>");
               out.println("</html>");
           }

       } else {
           try (PrintWriter out = response.getWriter()) {
               /* TODO output your page here. You may use following sample code. */
               out.println("<!DOCTYPE html>");
               out.println("<html>");
               out.println("<head>");
               out.println("<title>Un nouveau utilisateur </title>");
               out.println("</head>");
               out.println("<body>");
               out.println("<h1> Liste des utilisateurs : </h1>");
               out.println("<ul>");

               Set<Integer> keys = usersTable.keySet();

               //Obtaining iterator over set entries
               Iterator<Integer> itr = keys.iterator();
               while (itr.hasNext()) {
                   out.println("<li>");
                   out.println(usersTable.get(itr.next()).toString());
                   out.println("</li>");
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
			processRequest(request, response);
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
