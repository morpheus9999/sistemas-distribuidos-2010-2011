package Server;


import Client_Server.CallbackInterface;
import Client_Server.Constants;
import Client_Server.Generic;
import Client_Server.Login;
import Client_Server.Message;
import Client_Server.OnlineUsers;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jorge
 */
class ClientThreadTCP extends Thread{
    private boolean logout;
    private Socket socket;

    private Login lg = null;                   //  stores info about the logged user

    public ObjectOutputStream out;      //  streams made public so other threads can send annoucements
    public ObjectInputStream in;

    ClientThreadTCP(Socket clientSocket) {
        this.socket = clientSocket;
        this.logout = false;
    }

    /**
     * Thread main method
     */
    public void run() {
        Generic gen, temp;

        /*  opens comunication channels */
        this.openComChannels();

        try {
            while (!this.logout) {
                try {
                    /*  receives request from client    */
                    gen = (Generic) this.in.readObject();

                    /*  creates a new object with the received info to send to the client   */
                    temp = new Generic();
                    temp.setCode(gen.getCode());
                    temp.setObj(gen.getObj());
                    
                    /*  parses received object  */
                    switch (temp.getCode()) {
                        case Constants.creditCode:
                            temp=this.getCredito(temp);
                            break;
                        case Constants.resetCode:
                            temp=this.resetCredit(temp);
                            break;
                        case Constants.matchesCode:
                            temp=this.viewMatches(temp);
                            break;
                        case Constants.betCode:
                            temp=this.bet(temp);
                            break;
                        case Constants.onlineUsersCode:
                            temp = this.onlineUsers(gen);
                            break;
                        case Constants.messageCode:
                            temp = this.message(temp);
                            break;
                        case Constants.messageAllCode:
                            temp = this.messageAll(temp);
                            break;
                        case Constants.logoutCode:
                            temp = this.logout(temp);
                            break;
                        case Constants.loginCode:
                            temp = this.login(temp);
                            break;
                        case Constants.regCode:
                            temp = this.register(temp);
                            break;
                        default:
                            break;
                    }

                    /*  sends confirmation of the action to the client  */
                    this.out.writeObject(temp);
                    this.out.flush();
                } catch (ClassNotFoundException ex) {
                    //System.out.println("Class unindentified!");
                }
            }
        } catch  (IOException ex) {
            System.out.println("Error receiving/sending generic object!");
        }

        /*  removes user from onlineUsersTCP list  */
        if(lg != null)
            Main.onlineUsersTCP.remove(lg.getName());

        /*  close comunication channels */
        this.closeComChannels();
    }


    /**
     * open comunication channels
     */
    private void openComChannels() {
        try {
            this.out = new ObjectOutputStream(this.socket.getOutputStream());
            this.in = new ObjectInputStream(this.socket.getInputStream());
        } catch (IOException ex) {
            System.out.println("Error opening comunication channels: "+ex.getMessage());
        }
    }

    /**
     * close comunication channels
     */
    private void closeComChannels() {
        try {
            this.out.close();
            this.in.close();
            this.socket.close();
        } catch (IOException ex) {
            System.out.println("Error closing comunication channels: "+ex.getMessage());
        }
    }

    /**
     * Login
     * */
    private Generic login(Generic gen) throws IOException {
        /*  faz query   */
//       if(Queries.login(gen)) {
//            /*  sets user is logged  */
            gen.setConfirmation(true);
            lg = (Login) gen.getObj();
            Main.onlineUsersTCP.put(this.lg.getName(), this);
//        }
//        else
//            gen.setConfirmation(false);

        return gen;
    }

    /**
     * Logout
     */
    private Generic logout(Generic gen) throws IOException {
        /*  sends confirmation of session ending    */
        gen.setConfirmation(true);
        /*  exits thread    */
        this.logout = true;

        return gen;
    }

    /**
     * Register
     */
    private Generic register(Generic gen) throws IOException {
        if(Queries.register(gen))
            gen.setConfirmation(true);
        else
            gen.setConfirmation(false);

        return gen;
    }

     /**
     * Message a user
     * */
    public static void messageUser(String fromUser, String toUser, String message) throws IOException {
        /*  creates individual message  */
        Message mes = new Message(toUser, message);
        mes.setAuthor(fromUser);

        /*  wrapes in a generic object  */
        Generic gen = new Generic();
        gen.setCode(Constants.receiveMessage);
        gen.setConfirmation(true);
        gen.setObj(mes);
        
        /*  checks if the user is online and sends  */
        if(Main.onlineUsersTCP.containsKey(toUser)) {
            ClientThreadTCP sock = Main.onlineUsersTCP.get(toUser);
            sock.out.writeObject(gen);
        } else if(Main.onlineUsersRMI.containsKey(toUser)) {
            CallbackInterface callback = Main.onlineUsersRMI.get(toUser);
            callback.printMessage(fromUser, message);
        }
        else { /*    or stores to send later accordingly    */

            // ###################QUERIES AQUI########################
            Queries.setMensagens(fromUser, message, toUser);

            System.out.println(toUser+" esta offline");
        }
    }

    /**
     * Message to user
     * */
    private Generic message(Generic gen) throws IOException {
        String fromUser = null, toUser = null;
        Vector<String> messageVector;
        Enumeration<String> message;
        Message mes = (Message) gen.getObj();

/*
        System.out.println("From: "+mes.getAuthor());
        System.out.println("To: "+mes.getKeysEnumeration().nextElement());
        System.out.println("Message: "+mes.getEntry(mes.getKeysEnumeration().nextElement()));
*/
        fromUser = mes.getAuthor();
        
        /*  runs through the received buffer and sends/stores messages  */
        Enumeration<String> enumerator = mes.getKeysEnumeration();
        

        /*  goes through all the messages for the desired user  */
        while(enumerator.hasMoreElements()) {
            toUser = enumerator.nextElement();
            messageVector = mes.getEntry(toUser);
            message = messageVector.elements();

            while(message.hasMoreElements())
                ClientThreadTCP.messageUser(fromUser, toUser, message.nextElement());
        }

        gen.setConfirmation(true);

        return gen;
    }

    /**
     * Message to all users
     * */
    private Generic messageAll(Generic gen) throws IOException {
        String fromUser = null, toUser = null, id = null;
        Vector<String> messageVector, userVector;
        Message mes = (Message) gen.getObj();
        Enumeration<String> keys, onlineEnumeratorTCP, onlineEnumeratorRMI, userEnumerator, message;
        

        fromUser = mes.getAuthor();
        /*
         *  runs through the received buffer and sends/stores messages
         *  to all people registered
         */
        keys = mes.getKeysEnumeration();
        while(keys.hasMoreElements()) {
            /*
             *  since message is for all
             *  the key doesn't matter, only for obtaining each message
             */
            id = keys.nextElement();
            messageVector = mes.getEntry(id);

            userVector = Queries.getUsers();
            userEnumerator = userVector.elements();

            while(userEnumerator.hasMoreElements()) {
                toUser = userEnumerator.nextElement();
                message = messageVector.elements();

                while(message.hasMoreElements())
                    ClientThreadTCP.messageUser(fromUser, toUser, message.nextElement());
            }

//            /*  first send to online users TCP */
//            onlineEnumeratorTCP = Main.onlineUsersTCP.keys();
//
//            while(onlineEnumeratorTCP.hasMoreElements()) {
//                toUser = onlineEnumeratorTCP.nextElement();
//
//                message = messageVector.elements();
//
//                while(message.hasMoreElements())
//                    ClientThreadTCP.messageUser(fromUser, toUser, message.nextElement());
//            }
//
//            /*  after that it sends to online users RMI */
//            onlineEnumeratorRMI = Main.onlineUsersRMI.keys();
//
//            while(onlineEnumeratorRMI.hasMoreElements()) {
//                toUser = onlineEnumeratorRMI.nextElement();
//
//                message = messageVector.elements();
//
//                while(message.hasMoreElements())
//                    ClientThreadTCP.messageUser(fromUser, toUser, message.nextElement());
        }
        
        gen.setConfirmation(true);

        return gen;
    }

    /**
     *
     * */
    private Generic onlineUsers(Generic gen) throws IOException {
        OnlineUsers list = new OnlineUsers();
        Enumeration<String> temp;

        /*  adds TCP users to the list  */
        temp = Main.onlineUsersTCP.keys();
        while(temp.hasMoreElements())
            list.addEntry(temp.nextElement());

        /*  adds RMI users to the list  */
        temp = Main.onlineUsersRMI.keys();
        while(temp.hasMoreElements())
            list.addEntry(temp.nextElement());

        gen.setConfirmation(true);
        gen.setObj(list);

        return gen;
    }

    private Generic bet(Generic gen) {
        //meter a variavel da ronda....
        if(Queries.newBet(gen,lg,Main.game.getRonda()))
            gen.setConfirmation(true);
        else
            gen.setConfirmation(false);

        return gen;
    }

    private Generic getCredito(Generic temp) {
        return Queries.getCredit(temp, lg);

    }

    private Generic resetCredit(Generic temp) {
        return Queries.resetCredit(temp,lg);
    }

    private Generic viewMatches(Generic temp) {
        return Queries.viewMatches(temp,Main.game.getRonda());
    }
}