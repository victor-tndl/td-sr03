<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Chat</title>
        <script>
            window.addEventListener( "load", function( event ) {
   
                let pseudo = prompt( "Veuillez saisir votre pseudo :" );
                let ws = new WebSocket( "ws://localhost:8080/td-sr03/chatserver/" + "${requestScope.chat_id}" +  "/" + pseudo );
                
                let txtHistory = document.getElementById( "history" );
                let txtMessage = document.getElementById( "txtMessage" );
                txtMessage.focus();
                
                ws.addEventListener( "open", function( evt ) {
                    console.log( "Connection established" );
                });
            
                ws.addEventListener( "message", function( evt ) {
                    let message = evt.data;
                    console.log( "Receive new message: " + message );
                    txtHistory.value += message + "\n";
                });
                
                ws.addEventListener( "close", function( evt ) {
                    console.log( "Connection closed" );
                });
                
                
                let btnSend = document.getElementById( "btnSend" );
                btnSend.addEventListener( "click", function( clickEvent ) {
                    ws.send( txtMessage.value );
                    txtMessage.value = "";
                    txtMessage.focus();
                });
             
                let btnClose = document.getElementById( "btnClose" );
                btnClose.addEventListener( "click", function( clickEvent ) {
                    ws.close();
                });
                
            });
        </script>
        <style>
            #history {
                display: block;
                width: 500px;
                height: 300px;
            }

            #txtMessage {
                display: inline-block;
                width: 300px;
            }

            #btnSend {
                display: inline-block;
                width: 180px;
            }

            #btnClose {
                display: block;
                width: 500px;
            }
        </style>
    </head>
    <body>
        <textarea id="history" readonly></textarea>
        <input id="txtMessage" type="text" />
        <button id="btnSend">Send message</button>
        <button id="btnClose">Close connection</button>
    </body>
</html>
