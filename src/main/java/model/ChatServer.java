package model;

import java.util.HashMap;
import java.util.Map.Entry;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;


@ServerEndpoint(value="/chatserver/{chat_id}/{pseudo}", 
                configurator=ChatServer.EndpointConfigurator.class)
public class ChatServer {
    private class ChatServerConnexion {
        private String chatId;
        private Session session;

        ChatServerConnexion(String _chatId, Session _session) {
            this.setChatId(_chatId);
            this.session = _session;
        }

		public Session getSession() {
			return session;
		}

		public void setSession(Session _session) {
			this.session = _session;
		}

		public String getChatId() {
			return chatId;
		}

		public void setChatId(String _chatId) {
			this.chatId = _chatId;
		}

    }

    private static ChatServer singleton = new ChatServer();
    private static Integer id = 0;

    private ChatServer() {
    }

    /**
     * Get ChatServer -> singleton
     */
    public static ChatServer getInstance() {
        return ChatServer.singleton;
    }

    /**
     * Get every user's session and chatId connexion
     */
    private HashMap<Integer, ChatServerConnexion> currentSessions = new HashMap<Integer, ChatServerConnexion>();

    /**
     * Trigger on every user's connexion
     */
    @OnOpen
    public void open(Session session, @PathParam("chat_id") String chatId, @PathParam("pseudo") String pseudo) {
        // Send connexion message
        sendMessage( "Server >>> Connection established for " + pseudo, chatId );

        // Insert/Get usefull properties
        session.getUserProperties().put( "pseudo", pseudo );
        ChatServerConnexion chatServerConnexion = new ChatServerConnexion( chatId, session);

        // Increment HashMap id
        id++;
        // Insert the current connexion into currentConnexions (HashMap) 
        currentSessions.put(id, chatServerConnexion);
    }

    /**
     * Trigger on every user's deconnexion
     */
    @OnClose
    public void close(Session session, @PathParam("chat_id") String chatId) {
        String pseudo = (String) session.getUserProperties().get("pseudo");

        // Iterate over all the connected sessions
        for (Entry<Integer, ChatServerConnexion> entry : currentSessions.entrySet()) {
            Integer key = entry.getKey();
            ChatServerConnexion value = entry.getValue();

            // Check if the current value is the good one
            if (value.getChatId().equals(chatId) && session.getId() == value.getSession().getId()) {
                System.out.println("Disconnecting userId: "+ value.getSession().getId());
                currentSessions.remove(key);
                break;
            }
        }

        // Send deconnexion message
        sendMessage("Server >>> Connection closed for " + pseudo, chatId);
    }

    /**
     * Trigger on every communication error
     */
    @OnError
    public void onError(Throwable error) {
        System.out.println( "Error: " + error.getMessage() );
    }

    /**
     * Trigger on every received message
     */
    @OnMessage
    public void handleMessage(String message, Session session, @PathParam("chat_id") String chatId) {
        String pseudo = (String) session.getUserProperties().get("pseudo");
        String fullMessage = pseudo + " >>> " + message;
        sendMessage(fullMessage, chatId);
    }

    /**
     * Send message to concerned users
     */
    private void sendMessage( String fullMessage, String chatId ) {
        // Print into web console
        System.out.println(fullMessage);

        // Send message to every session connected to the same chat (chatId)
        for( ChatServerConnexion chatServerConnexion : currentSessions.values() ) {
            try {
                if (chatServerConnexion.getChatId().equals(chatId) == true) {
                    chatServerConnexion.getSession().getBasicRemote().sendText(fullMessage);
                }
            } catch( Exception exception ) {
                System.out.println("ERROR: cannot send message to " + chatServerConnexion.getSession().getId());
            }
        }
    }

    /**
     * Configure singleton use for ChatServer class
     */
    public static class EndpointConfigurator extends ServerEndpointConfig.Configurator {
        @Override 
        @SuppressWarnings("unchecked")
        public <T> T getEndpointInstance(Class<T> endpointClass) {
            return (T) ChatServer.getInstance();
        }
    }
}