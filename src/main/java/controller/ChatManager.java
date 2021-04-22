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

import model.Chat;
import model.SessionToken;
import model.User;

@WebServlet(name="chatManager",urlPatterns={"/chatManager"})
public class ChatManager extends HttpServlet {
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
            return;
        } else {
            // Get values from the form
			String title = request.getParameter("title");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			Date beginDate = df.parse(request.getParameter("beginDate")+":00Z");
			Date endDate = df.parse(request.getParameter("endDate")+":00Z");

            // Create the chat
            SessionToken token = (SessionToken) session.getAttribute("sessionToken");
            User owner = User.findByLogin(token.getUserLogin());
            DateFormat mysqlDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
            Chat chat = new Chat(title, mysqlDateFormat.format(beginDate), mysqlDateFormat.format(endDate), owner);

            // Try save the chat
            chat.save();

            // Redirect user 
            RequestDispatcher rd = request.getRequestDispatcher("newChatSuccess.jsp");
            rd.forward(request, response);
            return;
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
            return;
        } else {
            if (request.getParameter("chat_id") != null) {
                request.setAttribute("chat_id", request.getParameter("chat_id"));
                RequestDispatcher rd = request.getRequestDispatcher("client.jsp");
                rd.include(request, response);
                return;
            } else {
                // Get user's session and token
                HttpSession session = request.getSession();
                SessionToken token = (SessionToken) session.getAttribute("sessionToken");

                List<Chat> allChats = null;
                try {
                    if (token.getUserIsAdmin() == true) {
                        // The user is an admin
                        allChats = Chat.findAll();
                    } else {
                        allChats = Chat.findByUser(User.findByLogin(token.getUserLogin()));
                    }
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }

                // Redirect the user to the wanted page with allChats in parameter
                request.setAttribute("allChats", allChats);
                RequestDispatcher rd = request.getRequestDispatcher("chats.jsp");
                rd.forward(request, response);
                return;
            }
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
            return;
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
            String chatId = request.getParameter("chat_id");
            if (chatId != null) {
                Chat chatToDelete = null;
                try {
                    chatToDelete = Chat.findById(Integer.parseInt(chatId, 10));
                } catch (NumberFormatException | ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
                if (chatToDelete != null) {
                    SessionToken token = (SessionToken) session.getAttribute("sessionToken");
                    if (token.getUserIsAdmin()) {
                        try {
                            chatToDelete.delete();
                            return;
                        } catch (NumberFormatException | ClassNotFoundException | SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (chatToDelete.getOwner().getId() == token.getUserId() ) {
                            try {
                                chatToDelete.delete();
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