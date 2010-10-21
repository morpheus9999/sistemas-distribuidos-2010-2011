/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import Client.Interface;
import Client.receiverThread;
import Client.senderThread;
import Client_Server.CallbackInterface;
import Client_Server.Constants;
import Client_Server.Credit;
import Client_Server.Generic;
import Client_Server.Login;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JLA
 */
public class Main {

    /*  stores info about online users  */
    public static Hashtable<String, ClientThreadTCP> onlineUsersTCP = new Hashtable<String, ClientThreadTCP>();
    public static Hashtable<String, CallbackInterface> onlineUsersRMI = new Hashtable<String, CallbackInterface>();
    public static BetThread game;
    
    public static void main(String args[]) {
        int counter = 0;

        try {
            /*  opens a port to check for requests  */
            game = new BetThread(Constants.numJogos);
            game.start();

            /*  opens RMI thread    */
            ClientThreadRMI thr = new ClientThreadRMI();
            thr.start();

            /*  opens socket to listen for connections  */
            ServerSocket listener = new ServerSocket(Constants.serverPort);

            /*  creates a thread pool   */
            ExecutorService pool = Executors.newCachedThreadPool();
            while(true) {
                System.out.println("Waiting for connection...");

                /*  waits for a connection  */
                Socket sock = listener.accept();

                System.out.println("Running "+(++counter)+"º conection!");

                /*  runs the thread */
                pool.submit(new ClientThreadTCP(sock));
            }

        } catch (IOException ex) {
            ex.printStackTrace(System.out);
            System.out.println("io exception");
        }


    }
}
