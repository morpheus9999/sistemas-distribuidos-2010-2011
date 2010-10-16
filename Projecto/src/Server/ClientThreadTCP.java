package Server;


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
                            break;
                        case Constants.resetCode:
                            break;
                        case Constants.matchesCode:
                            break;
                        case Constants.betCode:
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

        /*  removes user from onlineUsers list  */
        if(lg != null)
            Main.onlineUsers.remove(lg.getName());

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
            /*  sets user is logged  */
            gen.setConfirmation(true);
            lg = (Login) gen.getObj();
            Main.onlineUsers.put(this.lg.getName(), this);
 //       }
 //       else
  //          gen.setConfirmation(false);

        return gen;
    }

    /**
     * Logout
     * */
    private Generic logout(Generic gen) throws IOException {
        /*  sends confirmation of session ending    */
        gen.setConfirmation(true);
        /*  exits thread    */
        this.logout = true;
        
        return gen;
    }

    /**
     * Register
     * */
    private Generic register(Generic gen) throws IOException {
        if(Queries.register(gen))
            gen.setConfirmation(true);
        else
            gen.setConfirmation(true);
        
        return gen;
    }

    /**
     * Message to user
     * */
    private Generic message(Generic gen) throws IOException {
        /*  envia dados para a base de dados e utilizadores */
        Message mes = (Message) gen.getObj();
        System.out.println("From: "+mes.getAuthor());
        System.out.println("To: "+mes.getKeysEnumeration().nextElement());
        System.out.println("Message: "+mes.getEntry(mes.getKeysEnumeration().nextElement()));

        /*  runs through the received buffer and sends/stores messages  */
        //for(String )


        gen.setConfirmation(true);

        return gen;
    }

    /**
     * Message a user
     * */
    private void messageUser(String fromUser, String toUser, String message) throws IOException {
        /*  creates individual message  */
        Message mes = new Message();
        mes.setAuthor(fromUser);
        mes.addEntry(toUser, message);

        /*  wrapes in a generic object  */
        Generic gen = new Generic();
        gen.setCode(Constants.receiveMessage);
        gen.setConfirmation(true);

        /*  checks if the user is online and sends  */
        if(Main.onlineUsers.containsKey(toUser)) {
            ClientThreadTCP sock = Main.onlineUsers.get(toUser);
            sock.out.writeObject(gen);
        }
        else { /*    or stores to send later accordingly    */
            System.out.println(toUser+" esta offline");
        }
    }

    /**
     * Message to all users
     * */
    private Generic messageAll(Generic gen) throws IOException {
        /*  envia dados para a base de dados e utilizadores */
        Message mes = (Message) gen.getObj();
        System.out.println("From: "+mes.getAuthor());
        System.out.println("Message: "+mes.getEntry(mes.getKeysEnumeration().nextElement()));

        gen.setConfirmation(true);

        return gen;
    }

    /**
     *
     * */
    private Generic onlineUsers(Generic gen) throws IOException {
        OnlineUsers list = new OnlineUsers();
        Enumeration<String> temp = Main.onlineUsers.keys();

        while(temp.hasMoreElements())
            list.addEntry(temp.nextElement());
        
        gen.setConfirmation(true);
        gen.setObj(list);

        return gen;
    }
}
