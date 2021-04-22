package model;

import java.util.Hashtable;

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
    
    private static ChatServer singleton = new ChatServer();

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
    private Hashtable<String, Session> sessions = new Hashtable<>();

    /**
     * Cette méthode est déclenchée à chaque connexion d'un utilisateur.
     */
    @OnOpen
    public void open(Session session, @PathParam("chat_id") String chatId, @PathParam("pseudo") String pseudo ) {
        System.out.println("chatId: " +chatId+ " pseudo: "+pseudo);
        sendMessage( "Server >>> Connection established for " + pseudo );
        session.getUserProperties().put( "pseudo", pseudo );
        sessions.put( session.getId(), session );
    }

    /**
     * Cette méthode est déclenchée à chaque déconnexion d'un utilisateur.
     */
    @OnClose
    public void close(Session session) {
        String pseudo = (String) session.getUserProperties().get( "pseudo" );
        sessions.remove( session.getId() );
        sendMessage( "Server >>> Connection closed for " + pseudo );
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
    public void handleMessage(String message, Session session) {
        String pseudo = (String) session.getUserProperties().get( "pseudo" );
        String fullMessage = pseudo + " >>> " + message;
        sendMessage( fullMessage );
    }

    /**
     * Une méthode privée, spécifique é notre exemple.
     * Elle permet l'envoie d'un message aux participants de la discussion.
     */
    private void sendMessage( String fullMessage ) {
        // Affichage sur la console du server Web.
        System.out.println( fullMessage );

        // On envoie le message é tout le monde.
        for( Session session : sessions.values() ) {
            try {
                session.getBasicRemote().sendText( fullMessage );
            } catch( Exception exception ) {
                System.out.println( "ERROR: cannot send message to " + session.getId() );
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