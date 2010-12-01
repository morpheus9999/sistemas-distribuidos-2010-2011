/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import Client.Interface;
import Client.receiverThread;
import Client.senderThread;
import Client_Server.CallbackInterface;
import Client_Server.CallbackInterfaceTomcat;
import Client_Server.Constants;
import Client_Server.Credit;
import Client_Server.Generic;
import Client_Server.Login;
import Client_Server.RMIInterface;
import Client_Server.Selection;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Hashtable;
import java.util.Vector;
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
    public static Vector <String> onlineUsersRMITomcat =new Vector<String>();
    //falta isto 
    public static CallbackInterfaceTomcat calbackInterfaceTomcat=null;
    
    public static Selection opt = new Selection();
    public static BetThread game;
    
    public static void main(String args[]) {
        int counter = 0;

        try {
            
            /*  waits for server to be primary before continuing    */
            UDPThreadPrimary udp = new UDPThreadPrimary();
            udp.start();

            /*  it waits!   */
            Main.opt.getOption();

            System.out.println("\n\n(Primary): sou Master :D\n\n");

            /*  opens a port to check for requests  */
            game = new BetThread(Constants.numJogos);
            game.start();

            /*  opens RMI connections   */
            try {
                RMIInterface obj = new RMIMethods();
                LocateRegistry.createRegistry(Constants.primaryServerRMIPort).rebind(Constants.primaryServerRMIObj, obj);
            } catch (RemoteException ex) {
                System.out.println("RMI connection error");
            }

            /*  opens socket to listen for connections  */
            ServerSocket listener = new ServerSocket(Constants.primaryServerTCPPort);

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
