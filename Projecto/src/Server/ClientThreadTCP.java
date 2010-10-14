package Server;


import Client_Server.Constants;
import Client_Server.Generic;
import Client_Server.Login;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
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

    private Login lg;                   //  stores info about the logged user

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

        while (!this.logout) {
            try {
                /*  receives request from client    */
                gen = (Generic) this.in.readObject();

                /*  creates a new object with the received info to send to the client   */
                temp = new Generic();
                temp.setCode(gen.getCode());
                temp.setObj(gen.getObj());
                
                /*  parses received object  */
                switch (gen.getCode()) {
                    case Constants.creditCode:
                        break;
                    case Constants.resetCode:
                        break;
                    case Constants.matchesCode:
                        break;
                    case Constants.betCode:
                        break;
                    case Constants.onlineUsersCode:
                        break;
                    case Constants.messageCode:
                        break;
                    case Constants.messageAllCode:
                        break;
                    case Constants.logoutCode:
                        temp = this.logout(temp);
                        break;
                    case Constants.loginCode:
                        temp = this.login(temp);
                        break;
                    case Constants.regCode:
                        temp =this.register(temp);
                        break;
                    default:
                        break;
                }

                /*  sends confirmation of the action to the client  */
                this.out.writeObject(temp);
                this.out.flush();
            } catch (IOException ex) {
                System.out.println("Error receiving/sending generic object!");
            } catch (ClassNotFoundException ex) {
                System.out.println("Class unindentified!");
            }
        }

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

    /*
     * Login
     */
    private Generic login(Generic gen) throws IOException {
        /*  faz query   */
        if(Queries.login(gen)) {
            /*  sets user is logged  */
            gen.setConfirmation(true);
            lg = (Login) gen.getObj();
            Main.onlineUsers.put(lg.getName(), this);
        }
        else
            gen.setConfirmation(false);

        return gen;
    }

    /*
     * Logout
     */
    private Generic logout(Generic gen) throws IOException {
        /*  sends confirmation of session ending    */
        gen.setConfirmation(true);
        /*  removes user from online users  */
        Main.onlineUsers.remove(this.lg.getName());
        /*  exits thread    */
        this.logout = true;

        return gen;
    }

    /*
     * Register
     */
    private Generic register(Generic gen) throws IOException {
        if(Queries.register(gen))
            gen.setConfirmation(true);
        else
            gen.setConfirmation(true);
        
        return gen;
    }
}
