package controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="forumValidation",urlPatterns={"/forumValidation"})
public class ForumValidation extends HttpServlet{

	private static final long serialVersionUID = 1L;

	/**
     * @return
	 * @see HttpServlet#HttpServlet()
     */
	public ForumValidation() {
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
		try {
			boolean isForumValid = true;

			// Get values from the form
			String title = request.getParameter("title");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			Date begin_date = df.parse(request.getParameter("begin_date")+":00Z");
			Date end_date = df.parse(request.getParameter("end_date")+":00Z");

			if (title == null || begin_date.equals(null) || end_date.equals(null)) {
				System.out.println("Champs non renseignés");
				RequestDispatcher rd = request.getRequestDispatcher("newForum.html");
				rd.forward(request, response);
				isForumValid = false;
			} else if ("".equals(title) || begin_date.equals(null) || end_date.equals(null)) {
				System.out.println("Champs vides");
				RequestDispatcher rd = request.getRequestDispatcher("newForum.html");
				rd.forward(request, response);
				isForumValid = false;
			} else if (end_date.before(begin_date)) {
				System.out.println("End date is before begin date");
				RequestDispatcher rd = request.getRequestDispatcher("newForum.html");
				rd.forward(request, response);
				isForumValid = false;
			}

			if (request.getParameter("validator") != null) {
				// des doublons ont été détectés et l'utilisateur à valider son choix
				if ("oui".equals(request.getParameter("valider"))) {
					// on insère les doublons
					isForumValid = true;
				} else {
					isForumValid = false;
					RequestDispatcher rd = request.getRequestDispatcher("newForum.html");
					// Abort insertion
					rd.forward(request, response);
				}
			}

			if (isForumValid) {
				RequestDispatcher rd = request.getRequestDispatcher("forumManager");
				rd.forward(request, response);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
