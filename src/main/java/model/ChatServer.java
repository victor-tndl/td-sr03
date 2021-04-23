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
     * Acquisition de notre unique instance ChatServer
     */
    public static ChatServer getInstance() {
        return ChatServer.singleton;
    }

    /**
     * On maintient toutes les sessions utilisateurs dans une collection.
     */
    private HashMap<Integer, ChatServerConnexion> currentSessions = new HashMap<Integer, ChatServerConnexion>();

    /**
     * Cette méthode est déclenchée à chaque connexion d'un utilisateur.
     */
    @OnOpen
    public void open(Session session, @PathParam("chat_id") String chatId, @PathParam("pseudo") String pseudo ) {
        sendMessage( "Server >>> Connection established for " + pseudo, chatId );
        session.getUserProperties().put( "pseudo", pseudo );
        ChatServerConnexion chatServerConnexion = new ChatServerConnexion( chatId, session);
        id++;
        currentSessions.put(id, chatServerConnexion);
    }

    /**
     * Cette méthode est déclenchée à chaque déconnexion d'un utilisateur.
     */
    @OnClose
    public void close(Session session, @PathParam("chat_id") String chatId) {
        String pseudo = (String) session.getUserProperties().get("pseudo");
        for (Entry<Integer, ChatServerConnexion> entry : currentSessions.entrySet()) {
            Integer key = entry.getKey();
            ChatServerConnexion value = entry.getValue();

            if (value.getChatId().equals(chatId) && session.getId() == value.getSession().getId()) {
                System.out.println("Disconnecting userId: "+ value.getSession().getId());
                currentSessions.remove(key);
                break;
            }
        }

        sendMessage("Server >>> Connection closed for " + pseudo, chatId);
    }

    /**
     * Cette méthode est déclenchée en cas d'erreur de communication.
     */
    @OnError
    public void onError(Throwable error) {
        System.out.println( "Error: " + error.getMessage() );
    }

    /**
     * Cette méthode est déclenchée à chaque réception d'un message utilisateur.
     */
    @OnMessage
    public void handleMessage(String message, Session session, @PathParam("chat_id") String chatId) {
        String pseudo = (String) session.getUserProperties().get("pseudo");
        String chat_id = (String) session.getUserProperties().get("chat_id");
        String fullMessage = pseudo + " >>> " + message;
        sendMessage(fullMessage, chatId);
    }

    /**
     * Une méthode privée, spécifique é notre exemple.
     * Elle permet l'envoie d'un message aux participants de la discussion.
     */
    private void sendMessage( String fullMessage, String chatId ) {
        // Affichage sur la console du server Web.
        System.out.println(fullMessage);

        // On envoie le message é tout le monde.
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
     * Permet de ne pas avoir une instance différente par client.
     * ChatServer est donc gérer en "singleton" et le configurateur utilise ce singleton.
     */
    public static class EndpointConfigurator extends ServerEndpointConfig.Configurator {
        @Override 
        @SuppressWarnings("unchecked")
        public <T> T getEndpointInstance(Class<T> endpointClass) {
            return (T) ChatServer.getInstance();
        }
    }
}