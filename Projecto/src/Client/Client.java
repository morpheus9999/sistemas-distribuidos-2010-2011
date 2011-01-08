/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Client;

import Client_Server.Login;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import Client_Server.Constants;
import Client_Server.Message;
import Client_Server.User;
import Client_Server.Bet;
import Client_Server.Selection;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JLA
 */
public class Client {

    /**
     * Global/Static variables
     */
    private static Socket sock;
    private static OutputStream outStream;
    private static InputStream inStream;
    public static ObjectOutputStream out;
    public static ObjectInputStream in;
    public static Login log = new Login();
    public static User reg = new User();
    public static Bet bet = new Bet();
    public static Message buffer = new Message();
    public static Message bufferAll = new Message();
    public static boolean logged = false;
    public static boolean connected = false;
    public static Selection opt = new Selection();
    public static boolean exit = false;
    public static senderThread sender;
    public static receiverThread receiver;
    
    /**
     * Client method
     */
    public static void main(String args[]) {
        /*  init    */
        /*  tries to connect 1 time, if it fails, the app ends  */
        if(!connect()) {
            reconnect();
            initThreads();
        }

        /*  initates interface  */
        Interface inter = new Interface();
        inter.openTIChannel();

        /*  initiates interaction with user */
        while(!logged && !exit) {
            switch(inter.welcomeMenu()) {
                case Constants.loginCode:
                    inter.login();

                    if(Client.connected)
                        /*  waits for receiver thread confirmation  */
                        synchronized(Client.class) {
                            try {
                                Client.class.wait();
                            } catch (InterruptedException ex) {
                                //System.out.println("Error waiting for login confirmation");
                            }
                        }
                    else
                        /*  if connection is down, the app ends */
                        exit = true;
                    break;
                case Constants.regCode:
                    inter.register();
                    break;
                    
                default:
                    System.out.println("Wrong code!");
                    break;
            }
        }

        /*  requests messages that were received while offline  */
        if(Client.connected)
            inter.requestMessage();

        while(!exit) {
            if(Client.connected) {
                switch(inter.mainMenu()) {
                    case Constants.creditCode:
                        /*  Credit  */
                        inter.credit();
                        break;
                    case Constants.resetCode:
                        /*  Reset credit    */
                        inter.resetCredit();
                        break;
                    case Constants.matchesCode:
                        /*  Current matches */
                        inter.checkMatches();
                        break;
                    case Constants.betCode:
                        /*  Bet */
                        inter.bet();
                        break;
                    case Constants.onlineUsersCode:
                        /*  Online users    */
                        inter.onlineUsers();
                        break;
                    case Constants.messageCode:
                        /*  Message user    */
                        inter.messageSingleUser();
                        break;
                    case Constants.messageAllCode:
                        /*  Message all users   */
                        inter.messageAllUsers();
                        break;
                    case Constants.logoutCode:
                        exit = true;
                        /*  logout  */
                        inter.logout();
                        break;
                    default:
                        System.out.println("Wrong code!");
                        break;
                }
            } else {
                switch(inter.offlineMenu()) {
                    case Constants.messageCode:
                        /*  Message user    */
                        inter.messageSingleUser();
                        break;
                    case Constants.messageAllCode:
                        /*  Message all users   */
                        inter.messageAllUsers();
                        break;
                    case Constants.logoutCode:
                        /*  logout  */
                        inter.logout();
                        exit = true;
                        break;
                    default:
                        System.out.println("Wrong code!");
                        break;
                }
            }
        }

        /*  closes comunication channels with user  */
        inter.closeTIChannel();

        /*  wait for threads termination    */
        closeThreads();

        System.out.println("bye bye :)");
    }

    /**
     * opens comunication channels
     * */
    public static boolean openChannels(int flagServer) {
        try {
            sock = null;
            if(flagServer == 0)
                sock = new Socket(Constants.primaryServerTCP, Constants.primaryServerTCPPort);
            else
                sock = new Socket(Constants.backupServerTCP, Constants.backupServerTCPPort);
                
            if(sock != null && sock.isConnected()) {
                outStream = sock.getOutputStream();
                out = new ObjectOutputStream(outStream);

                inStream = sock.getInputStream();
                in = new ObjectInputStream(inStream);

                Client.connected = true;
                return true;
            }
        } catch (UnknownHostException ex) {
            //System.out.println("unknown host");
        } catch (IOException ex) {
            //System.out.println("io exception "+ex.getMessage());
        }
        
        Client.connected = false;
        return false;
    }

    /**
     * closes comunication channels
     * */
    public static void closeChannels() {
        try {
            if (sock != null && !sock.isClosed())
                sock.close();
        } catch (IOException error) {
            //System.out.println("Error closing comunication channels: "+ error.getMessage());
        }
        
        Client.connected = false;
    }
    
    /**
     * inits sender and receiver threads
     * */
    public static void initThreads() {
        sender = new senderThread();
        sender.start();

        receiver = new receiverThread();
        receiver.start();
    }
    
    /**
     * waits for thread termination
     * */
    public static void closeThreads() {
        try {
            sender.join();
            receiver.join();
        } catch (InterruptedException ex) {
            //System.out.println("error closing threads");
        }
    }

    /**
     * checks if threads are alive
     * */
    public static boolean threadsAlive() {
        if(sender.isAlive() && receiver.isAlive()) {
            Client.connected = true;
            return true;
        } else {
            Client.connected = false;
            return false;
        }
    }

    /**
     * connect
     * */
    public static boolean connect() {
        openChannels(0);
        
        if(Client.connected) {
            initThreads();
            return true;
        }
        
        return false;
    }

    /**
     * reconnect
     * */
    public static boolean reconnect() {
        Client.connected = false;
        int flagServer = 0;

        System.out.println("Connection lost");

        try {
            while(!Client.exit) {
                System.out.print("Trying to connect");
                for (int i = 1; i <= Constants.tries && !Client.connected && !Client.exit; i++, Thread.sleep(Constants.reconnectTime)) {
                    System.out.print(".");

                    if(openChannels(flagServer)) {
                        System.out.println("Connection recovered!");

                        if(Client.logged == true){
                            Client.opt.setOption(Constants.loginCode);
                            System.out.println("Sending buffers...");
                            Client.opt.setOption(Constants.messageCode);
                            Client.opt.setOption(Constants.messageAllCode);
                        }

                        
                        return true;
                    }
                }

                System.out.println("");

                /*  changes server to connect   */
                flagServer = ++flagServer%2;
                
                System.out.print("Changing servers: ");
                if(flagServer == 0)
                    System.out.println("Primary");
                else
                    System.out.println("Backup");
            }
        } catch (Exception ex) {
            //System.out.println("Error in sleeping thread");
        }
        System.out.println("");
        return false;
    }
}
