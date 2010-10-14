/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import Client.Interface;
import Client.receiverThread;
import Client.senderThread;
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
    public static Hashtable<String, ClientThreadTCP> onlineUsers = new Hashtable<String, ClientThreadTCP>();

    public static void main(String args[]) {
        int counter = 0;

        try {
            /*  opens a port to check for requests  */
            ServerSocket listener = new ServerSocket(Constants.serverPort);

            /*  creates a thread pool   */
            ExecutorService pool = Executors.newCachedThreadPool();
            while(true) {
                System.out.println("Waiting for connection...");

                /*  waits for a connection  */
                Socket sock = listener.accept();

                System.out.println(++counter+"º conection!");
                System.out.println("Running Thread...");

                /*  runs the thread */
                pool.submit(new ClientThreadTCP(sock));
            }

        } catch (IOException ex) {
            System.out.println("io exception");
        }


    }
}
