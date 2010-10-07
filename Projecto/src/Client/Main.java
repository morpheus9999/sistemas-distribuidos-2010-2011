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
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JLA
 */
public class Main {

    /*
     * Global/Static variables
     */
    public static Socket sock;
    private final static String host = "localhost";
    private static OutputStream outStream;
    private static InputStream inStream;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;


    /**
     * Credit
     * asks for the actual user credit
     */
    public void printCredit() {

    }

    /**
     * Reset Credit
     * resets the credit of the user
     */
    public void resetCredit() {

    }

    /**
     * Current Matches
     * lets the user check the matches in play
     */
    public void checkMatches() {

    }

    /**
     * Bet
     * lets the user bet on any match
     */
    public void bet() {

    }

    /**
     * Online Users
     * presents the user with a list of online users
     */
    public void onlineUsers() {

    }

    /**
     * Message User
     * sends a message to a specific user
     */
    public void messageSingleUsers() {

    }

    /**
     * Message All Users
     * sends a message to all online users
     */
    public void messageAllUsers() {

    }

    /**
     * Logout
     * drops the connection with the server
     */
    public void logout() {

    }

    /**
     * Main method
     */
    public static void main(String args[]) {

        try {
            // Socket creation.
            sock = new Socket(host, Constants.serverPort);

            //  outputStreams
            outStream = sock.getOutputStream();
            out = new ObjectOutputStream(outStream);

            //  inputStreams
            inStream = sock.getInputStream();
            in = new ObjectInputStream(inStream);

            
            Login lg = new Login("ola", "adeus");
            out.writeObject(lg);


            String temp = in.readUTF();

            System.out.println("Resposta do servidor: "+temp);

        } catch (UnknownHostException ex) {
            System.out.println("unknown host");
        } catch (IOException ex) {
            System.out.println("io exception "+ex.getMessage());
        }


    }
}
