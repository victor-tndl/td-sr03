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
			Date beginDate = df.parse(request.getParameter("beginDate")+":00Z");
			Date endDate = df.parse(request.getParameter("endDate")+":00Z");

			if (title == null || beginDate.equals(null) || endDate.equals(null)) {
				System.out.println("Champs non renseignés");
				RequestDispatcher rd = request.getRequestDispatcher("newForum.jsp");
				rd.forward(request, response);
				isForumValid = false;
			} else if ("".equals(title) || beginDate.equals(null) || endDate.equals(null)) {
				System.out.println("Champs vides");
				RequestDispatcher rd = request.getRequestDispatcher("newForum.jsp");
				rd.forward(request, response);
				isForumValid = false;
			} else if (endDate.before(beginDate)) {
				System.out.println("End date is before begin date");
				RequestDispatcher rd = request.getRequestDispatcher("newForum.jsp");
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
					RequestDispatcher rd = request.getRequestDispatcher("newForum.jsp");
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
