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
import Client_Server.User;
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
    public static String message = null;
    public static boolean logged = false;
    public static Selection opt = new Selection();
    private static boolean exit = false;

    
    /**
     * Main method
     */
    public static void main(String args[]) {

        try {
            // Socket creation
            sock = new Socket(host, Constants.serverPort);

            //  outputStreams
            outStream = sock.getOutputStream();
            out = new ObjectOutputStream(outStream);

            //  inputStreams
            inStream = sock.getInputStream();
            in = new ObjectInputStream(inStream);

            /*  sender thread   */
            senderThread sender = new senderThread();
            sender.start();

            /*  receiver thread */
            receiverThread receiver = new receiverThread();
            receiver.start();

            /*  initates interface  */
            Interface inter = new Interface();
            /*  open comunication channels with user    */
            inter.openTIChannel();

            /*  initiates interaction with user */
            while(!logged) {
                switch(inter.welcomeMenu()) {
                    case 1:
                        inter.login();

                        /*  waits for receiver thread confirmation  */
                        synchronized(Main.class) {
                            try {
                                Main.class.wait();
                            } catch (InterruptedException ex) {
                                System.out.println("Error waiting for login confirmation");
                            }
                        }
                        break;
                    case 2:
                        inter.register();
                        break;
                    default:
                        System.out.println("Wrong code!");
                        break;
                }
            }

            while(!exit) {
                switch(inter.mainMenu()) {
                    case 1:
                        /*  Credit  */
                        inter.credit();
                        break;
                    case 2:
                        /*  Reset credit    */
                        inter.resetCredit();
                        break;
                    case 3:
                        /*  Current matches */
                        inter.checkMatches();
                        break;
                    case 4:
                        /*  Bet */
                        inter.bet();
                        break;
                    case 5:
                        /*  Online users    */
                        inter.onlineUsers();
                        break;
                    case 6:
                        /*  Message user    */
                        inter.messageSingleUsers();
                        break;
                    case 7:
                        /*  Message all users   */
                        inter.messageAllUsers();
                        break;
                    case 8:
                        /*  logout  */
                        inter.logout();
                        exit = true;
                        break;
                    default:
                        System.out.println("Wrong code!");
                        break;
                }
            }

            /*  closes comunication channels with user  */
            inter.closeTIChannel();

            System.out.println("i'm going to terminate the threads");
            /*  wait for threads termination    */
            sender.join();
            receiver.join();


        } catch (InterruptedException ex) {
            System.out.println("error closing threads");
        } catch (UnknownHostException ex) {
            System.out.println("unknown host");
        } catch (IOException ex) {
            System.out.println("io exception "+ex.getMessage());
        }


    }
}
