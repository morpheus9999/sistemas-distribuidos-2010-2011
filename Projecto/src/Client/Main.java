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
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JLA
 */
public class Main {

    /**
     * Global/Static variables
     */
    private static Socket sock;
    private final static String host = "localhost";
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
     * Main method
     */
    public static void main(String args[]) {
        /*  init    */
        /*  tries to connect 1 time, if it fails, the app ends  */
        if(!connect()) {
            System.out.println("System is down :(\nBye Bye");
            return;
        }

        /*  initates interface  */
        Interface inter = new Interface();
        inter.openTIChannel();

        /*  initiates interaction with user */
        while(!logged && !exit) {
            switch(inter.welcomeMenu()) {
                case Constants.loginCode:
                    inter.login();

                    if(Main.connected)
                        /*  waits for receiver thread confirmation  */
                        synchronized(Main.class) {
                            try {
                                Main.class.wait();
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

        while(!exit) {
            if(Main.connected) {
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
    public static boolean openChannels() {
        try {
            sock = null;
            sock = new Socket(host, Constants.serverPort);

            if(sock != null && sock.isConnected()) {
                outStream = sock.getOutputStream();
                out = new ObjectOutputStream(outStream);

                inStream = sock.getInputStream();
                in = new ObjectInputStream(inStream);

                Main.connected = true;
                return true;
            }
        } catch (UnknownHostException ex) {
            //System.out.println("unknown host");
        } catch (IOException ex) {
            //System.out.println("io exception "+ex.getMessage());
        }
        
        Main.connected = false;
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
        
        Main.connected = false;
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
            Main.connected = true;
            return true;
        } else {
            Main.connected = false;
            return false;
        }
    }

    /**
     * connect
     * */
    public static boolean connect() {
        openChannels();
        
        if(Main.connected) {
            initThreads();
            return true;
        }
        
        return false;
    }

    /**
     * reconnect
     * */
    public static boolean reconnect() {
        Main.connected = false;
        
        try {
            for (int i = 1; i <= Constants.tries && !Main.connected && !Main.exit; i++, Thread.sleep(Constants.reconnectTime)) {
                if (i == 1)
                    System.out.println("Connection lost. Trying to reconnect...");

                if(openChannels()) {
                    if(Main.logged == true)
                        Main.opt.setOption(Constants.loginCode);

                    return true;
                }
                else
                    if(i == Constants.tries) {
                        System.out.println("Reconnect failed :(");
                        return false;
                }
            }
        } catch (Exception ex) {
            //System.out.println("Error in sleeping thread");
        }
        return false;
    }
}
